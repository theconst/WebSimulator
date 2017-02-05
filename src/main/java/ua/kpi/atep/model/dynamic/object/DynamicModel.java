/*
 * DynamicModel.java
 */
package ua.kpi.atep.model.dynamic.object;

import java.io.Serializable;
import java.util.Arrays;
import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 *
 * @author Home
 */
abstract public class DynamicModel implements Serializable {
    
    private static final String MODEL_IS_IMMUTABLE_MESSAGE = "Model is immutable";

    public static double DEFAULT_SAMPLING = 0.1;

    private double timespan;

    private final DynamicItem[][] transferFunctions;
    private final int noOfInputs;
    private final int noOfOutputs;

    private final double[] output;

    protected DynamicModel(int noOfInputs, int noOfOutputs) {
        this.noOfInputs = noOfInputs;
        this.noOfOutputs = noOfOutputs;
        output = new double[noOfOutputs];
        transferFunctions = new DynamicItem[noOfInputs][noOfOutputs];
    }

    /* Model is responsible for its inputs and outputs*/
    
    abstract public int inputToIndex(String inputName);

    abstract public int outputToIndex(String ouputName);

    abstract public String[] getOutputs();

    abstract public String[] getInputs();

    abstract public String[] getParameters();
    
    
    public void setInputs(String... inputs) {
        throw new UnsupportedOperationException(MODEL_IS_IMMUTABLE_MESSAGE);
    }
    
    
    public void setOutputs(String... outputs) {
        throw new UnsupportedOperationException(MODEL_IS_IMMUTABLE_MESSAGE);
    }
    
    public void setParameters(String... params) {
        throw new UnsupportedOperationException(MODEL_IS_IMMUTABLE_MESSAGE);
    }
    

    public final void setTransferFunction(DynamicItem it, int i, int j) {

        //! add check
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
        return deep ? Arrays.copyOf(output, output.length) : output;
    }

    private double value(DynamicItem tf, double in, double timespan) {
        return (tf == null) ? 0 : tf.value(in, timespan);
    }

    public final double[] value(double[] in) {
        return value(in, false);
    }

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
        assert (timespan > 0);
        this.timespan = timespan;
    }

    public static DynamicModel newInstance(DynamicModelType type) {
        switch (type) {
            case VAPOR_HEATER:
                return new VaporHeater(DEFAULT_SAMPLING);
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public static DynamicModel newInstance(int inputs, int outputs) {
        return new MutableDynamicModel(inputs, outputs);
    }
}
