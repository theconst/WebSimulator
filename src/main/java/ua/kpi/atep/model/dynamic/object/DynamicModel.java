/*
 * DynamicModel.java
 */
package ua.kpi.atep.model.dynamic.object;

import java.io.Serializable;
import java.util.Arrays;
import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 * Represents the dynamic model (MIMO)
 * 
 * 
 * @author Konstantin Kovalchuk
 */
public final class DynamicModel implements Serializable {
    
    public static final double DEFAULT_SAMPLING = 0.1;

    private double timespan;

    private final DynamicItem[][] transferFunctions;

    protected DynamicModelOutput output;  
    
    protected DynamicModelInput input;
    
    protected int noOfInputs;
    
    protected int noOfOutputs;
   
    private final double[]  outputValue;

    //input and output are bount to the dynamic model, so no copy is made
    protected DynamicModel(DynamicModelInput input, DynamicModelOutput output) {
        this.input = input;
        this.output = output;
        this.noOfInputs = input.size();
        this.noOfOutputs = output.size();
        outputValue = new double[output.size()];
        this.transferFunctions = new DynamicItem[noOfInputs][noOfOutputs];
    }

    /* Model is responsible for its inputs and outputs*/

    public final DynamicModelInput getInput() {
        return input;
    }
    
    public final DynamicModelOutput getOutput() {
        return output;
    }
    
    public final int getInputsCount() {
        return noOfInputs;
    }
    
    public final int getOutputsCount() {
        return noOfOutputs;
    }
    
    
    public final void setTransferFunction(String from, 
            DynamicItem transferFunction, String to) {
        int fromIndex = Arrays.asList(input.getNames()).indexOf(from);
        int toIndex = Arrays.asList(output.getNames()).indexOf(to);
        
        if (fromIndex < 0 || toIndex < 0) {
            throw new IllegalArgumentException();
        }
        setTransferFunction(transferFunction, fromIndex, toIndex);
    }
    
    
    private void setTransferFunction(DynamicItem it, int i, int j) {
        if ((i >= noOfInputs) || (j >= noOfOutputs) || (it == null)) {
            throw new IllegalArgumentException();
        }
        transferFunctions[i][j] = it;
    }
    
    public final double[] value(double inputValue[], boolean deep) {
        if (inputValue.length != this.input.size()) {
            throw new IllegalArgumentException();
        }
        
        Arrays.fill(outputValue, 0.0);
        
        for (int inp = 0; inp < noOfInputs; ++inp) {
            for (int out = 0; out < noOfOutputs; ++out) {
                //overwrite output 
                outputValue[out] += value(transferFunctions[inp][out], 
                        inputValue[inp], timespan);
            }
        }
        return outputValue;
    }

    private double value(DynamicItem tf, double in, double timespan) {
        return (tf == null) ? 0 : tf.value(in, timespan);
    }

    /**
     * Old interface left the samle
     * 
     * @param in
     * @return 
     */
    public final double[] value(double[] in) {
        return value(in, false);
    }

    public final double getTimespan() {
        return timespan;
    }

    public final void setTimespan(double timespan) {
        if (timespan <= 0) {
            throw new IllegalArgumentException();
        }
        this.timespan = timespan;
    }
    
    public static DynamicModel newInstance(DynamicModelInput input, 
            DynamicModelOutput output) {
        return new DynamicModel(input, output);
    }
}
