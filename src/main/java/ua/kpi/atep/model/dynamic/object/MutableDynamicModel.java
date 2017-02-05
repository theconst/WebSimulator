/*
 * 
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 * Represents a fully mutable model with getters and setters for common values
 *
 * @author Home
 */
public class MutableDynamicModel extends DynamicModel {
    
    public static final long serialVersionUID = 1297L;

    private String[] inputs;

    private String[] outputs;

    private String[] parameters;

    protected MutableDynamicModel(int noOfInputs, int noOfOutputs) {
        super(noOfInputs, noOfOutputs);
    }

    @Override
    public void setInputs(String[] inputs) {
        if (inputs.length != this.getInputsCount()) {
            throw new IllegalArgumentException();
        }
        
        this.inputs = Arrays.copyOf(inputs, inputs.length);
    }

    @Override
    public void setOutputs(String[] outputs) {
        if (outputs.length != this.getOutputsCount()) {
            throw new IllegalArgumentException();
        }
        
        this.outputs = Arrays.copyOf(outputs, outputs.length);
    }

    @Override
    public void setParameters(String[] parameters) {
        this.parameters = Arrays.copyOf(parameters, parameters.length);
    }

    @Override
    public int inputToIndex(String inputName) {
        return Arrays.asList(inputs).indexOf(inputName);
    }

    @Override
    public int outputToIndex(String outputName) {
        return Arrays.asList(outputs).indexOf(outputName);
    }

    @Override
    public String[] getOutputs() {
        return Arrays.copyOf(outputs, outputs.length);
    }

    @Override
    public String[] getInputs() {
        return Arrays.copyOf(inputs, inputs.length);
    }

    @Override
    public String[] getParameters() {
        return Arrays.copyOf(parameters, parameters.length);
    }

}
