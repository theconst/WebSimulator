///*
// *  
// */
//package ua.kpi.atep.dynamic.generic;
//
//import java.util.Arrays;
//
///**
// *
// * @author Home
// */
//public class DynamicModelRunner {
//
//    private final DynamicModel model;
//
//    private double sampling;
//
//    private double timespan;
//
//    private double[] inputs;
//
//    private double[][] outputs;
//    
//    private double[] ticks;
//    
//    private double nextTickStart = 0;
//
//    public DynamicModelRunner(DynamicModel model) {
//        this.model = model;
//        inputs = new double[model.getInputsCount()];
//        outputs = new double[model.getInputsCount()][];
//    }
//
//    public void setTimespan(double timespan) {
//        this.timespan = timespan;
//    }
//
//    public void setSampling(double sampling) {
//        this.sampling = sampling;
//    }
//    
//    public void setInputs(double[] inputs, boolean deep) {
//        this.inputs = deep ? Arrays.copyOf(inputs, inputs.length) : inputs;
//    }
//    
//    public void setInputs(double[] inputs) {
//        setInputs(inputs, false);
//    }
//
//    public void setInput(String name, double value) {
//        int i = model.inputToIndex(name);
//        inputs[i] = value;
//    }
//
//    public void doSimulationStep() {
//        int len = (int) (timespan / sampling) + 1;
//        for (int i = 0; i < outputs.length; ++i) {
//            outputs[i] = allocate(outputs[i], len);
//        }
//
//        nextTickStart = (nextTickStart == 0) ? 0 : nextTickStart + sampling;
//        for (int i = 0; i < len; ++i) {
//            double[] values = model.value(inputs);
//            for (int j = 0; j < outputs.length; ++j) {
//                outputs[j][i] = values[j];
//                ticks[i] = (i == 0) ? nextTickStart: ticks[i - 1] + sampling;
//            }
//        }
//        
//        nextTickStart = ticks[ticks.length];
//    }
//
//    private double[] allocate(double[] array, int len) {
//        return (array.length == len) ? array : new double[len];
//    }
//
//    public double[] getTicks(boolean deep) {
//        return deep ? Arrays.copyOf(ticks, ticks.length) : ticks;
//    }
//
//    public double[] getOutput(String name, boolean deep) {
//        int index = model.outputToIndex(name);
//        double[] output = outputs[index];
//        
//        return deep ? Arrays.copyOf(output, output.length) : output;
//    }
//    
//    public double[] getOutput(String name) {
//        return getOutput(name, false);
//    }
//    
//    public double[][] getOutputs(boolean deep) {
//        return deep ? Arrays.copyOf(outputs, outputs.length) : outputs;
//    }
//    
//    public double[][] getOutputs() {
//        return getOutputs(false);
//    }
//    
//    public double[] getTicks() {
//        return getTicks(false);
//    }
//    
//    public double[] getOuput(String name) {
//        return getOutput(name, false);
//    }
//}
