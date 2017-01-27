/*
 * DynamicModel.java
 */
package ua.kpi.atep.dynamic.generic;

import java.io.Serializable;
import java.util.Arrays;
import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 *
 * @author Home
 */
abstract public class DynamicModel implements Serializable {
    
    public static double DEFAULT_SAMPLING = 0.1;

    private double timespan;

    private final DynamicItem[][] transferFunctions;
    private final int noOfInputs;
    private final int noOfOutputs;

    private final double[] output;

    public DynamicModel(int noOfInputs, int noOfOutputs) {
        this.noOfInputs = noOfInputs;
        this.noOfOutputs = noOfOutputs;
        output = new double[noOfOutputs];
        transferFunctions = new DynamicItem[noOfInputs][noOfOutputs];
    }

    protected final void setTransferFunction(DynamicItem it, int i, int j) {
        transferFunctions[i][j] = it;
    }

    public final double[] value(double[] in, boolean deep) {
        Arrays.fill(output, 0.0);                   //fill in arrays with zeros
        
        for (int inp = 0; inp < noOfInputs; ++inp) {
            for (int out = 0; out < noOfOutputs; ++out) {
                output[out] += value(transferFunctions[inp][out], in[inp],
                        getTimespan());
            }
        }
        return deep? Arrays.copyOf(output, output.length) : output;
    }

    private double value(DynamicItem tf, double in, double timespan) {
        return (tf == null) ? 0 : tf.value(in, timespan);
    }

    public final double[] value(double[] in) {
        return value(in, false);
    }
    
    abstract public int inputToIndex(String inputName);
    
    abstract public int outputToIndex(String ouputName);

    public final int getInputsCount() {
        return noOfInputs;
    }

    public final int getOutputsCount() {
        return noOfOutputs;
    }

    public final double getTimespan() {
        return timespan;
    }

    public final void setTimespan(double timespan) {
        this.timespan = timespan;
    }
    
    abstract public String[] getOutputs();
    
    abstract public String[] getInputs();
    
    abstract public String[] getParameters();
    
    public static DynamicModel newInstance(DynamicModelType type) {
        switch (type) {
            case VAPOR_HEATER:
                return new VaporHeater(DEFAULT_SAMPLING);
            default:
                throw new IllegalArgumentException();
        }
    }
}
