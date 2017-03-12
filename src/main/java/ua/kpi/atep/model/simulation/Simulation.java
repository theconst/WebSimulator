/*
 * SimulationWebSocket.java 05.01.2017
 */
package ua.kpi.atep.model.simulation;


import ua.kpi.atep.model.dynamic.object.*;


/**
 * This is a socket connection that operates with model
 *
 * //TODO: extend text message with Simulation message and send them
 *
 * @author Konstantin Kovalchuk
 */
public class Simulation {

    private final DynamicModel model;

    private double nextTickStart = 0;
    
    /* Flag which stores the */
    private boolean isFirstTick = true;

    //values needed to process 
    private final String[] outputNames;
    private final String[] inputNames;
    private double[][] outputs;
    private double[][] inputs;
    private double[] ticks;
    
    private Simulation(DynamicModel model) {
        this.model = model;
        
        DynamicModelInput input = model.getInput();
        DynamicModelOutput output = model.getOutput();

        this.inputNames = input.getNames();
        this.outputNames = output.getNames();
       
        
        this.inputs = init(inputs, model.getInputsCount());
        this.outputs = init(outputs, model.getOutputsCount());
        this.ticks = new double[0];
    }

    private double[][] init(double[][] array, int size) {
        return new double[size][0];
    }

    private void ensureCapacity(double[][] array, int cap) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = allocate(array[i], cap);
        }
    }

    private double[][] add(double[][] array, double[] values, int current) {
        assert(array.length == values.length);
        assert(current <= array[0].length && current >= 0);
        
        for (int j = 0; j < array.length; ++j) {
            array[j][current] = values[j];
        }
        return array;
    }

    public SimulationOutput doSimulationStep(UserInput input) {
        double timespan = input.getTimespan();
        double sampling = input.getSampling();

        /* calculate length of the output */
        int noOfSamples = (int) (Math.ceil(timespan / sampling) + 0.1);

        ensureCapacity(inputs, noOfSamples);
        ensureCapacity(outputs, noOfSamples);
        ticks = allocate(ticks, noOfSamples);

        /* --> if it is NOT the first tick, add sampling */
        if (isFirstTick) {
            nextTickStart = 0;
            isFirstTick = false;
        } else {
            nextTickStart += sampling;
        }
       
        /* evaluate the output on the period of ONE Sampling */
        model.setTimespan(sampling);

        double[] in = input.getInput(inputNames);
        for (int i = 0; i < noOfSamples; ++i) {

            /* evalueate computeValue through sampling period */
            double[] out = model.value(in);   

            outputs = add(outputs, out, i);
            inputs = add(inputs, in, i);
            
            //fill in ticks
            ticks[i] = ((i == 0) ? nextTickStart : ticks[i - 1] + sampling);
        }

        /* 
         * nextTickStart = {last finished tick} + samling
         * (samling can be changed, so samling is added above -->)
         */
        nextTickStart = ticks[noOfSamples - 1];
        
        SimulationOutput result = new SimulationOutput();
        
        result = addTrend(result, inputNames, inputs);
        result = addTrend(result, outputNames, outputs);
        result.setTicks(ticks);
        
        result = checkAlarms(result, outputNames, outputs);
        
        return result;
    }
    
    private SimulationOutput addTrend(SimulationOutput output, 
            String[] names, double[][] values) {
        assert(names.length == values.length);
        
        for (int i = 0; i < names.length; ++i) {
            output.setTrend(names[i], values[i]);
        }
        return output;
    }

    private SimulationOutput checkAlarms(SimulationOutput output, 
           String[] names, double[][] result) {
        
        //todo: reconsider this code
        double[] alarmLowCritical = model.getOutput().getAlarmLowCritical();
        double[] alarmHiCritical = model.getOutput().getAlarmHiCritical();
        double[] alarmLow = model.getOutput().getAlarmLow();
        double[] alarmHi = model.getOutput().getAlarmHi();
        
        AlarmType[] alarm = new AlarmType[result.length];
         
        
        //scan the alarm for alarm rate
        for (int i = 0; i < result.length; ++i) {
            for (double value: result[i]) {
                if (value <= alarmLowCritical[i]) {
                    alarm[i] = AlarmType.LO_CRITICAL;
                } else if (value <= alarmLow[i]) {
                    alarm[i] = AlarmType.LOW;
                } else if (value >= alarmHiCritical[i]) {
                    alarm[i] = AlarmType.HI_CRITICAL;
                } else if (value >= alarmHi[i]) {
                    alarm[i] = AlarmType.HI;
                }
            }
        }
        
        for (int i = 0; i < result.length; i++) {
            if (alarm[i] != null) {
                output.setAlarm(names[i], alarm[i]);
            }
        }
        
        return output;
    }
      
    private double[] allocate(double[] array, int size) {

        //strict equality, or else more advanced strategy
        return (array.length == size) ? array : new double[size];
    }
    
    public static Simulation newInstance(DynamicModel model) {
        return new Simulation(model);
    }
}
