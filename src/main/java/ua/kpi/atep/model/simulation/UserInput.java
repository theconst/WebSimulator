/*
 * User input tightly coupled to the 
 */
package ua.kpi.atep.model.simulation;

import java.util.Arrays;


/**
 * User input tightly coupled ot the json representation
 *  
 * @author Home
 */
public class UserInput {
    
    private double[] inputs;
    
    private String[] names;
    
    private double timespan;
    
    private double sampling;
    
    public double getTimespan() {
        return timespan;
    }
    
    public double getSampling() {
        return sampling;
    }
    
    public double[] getInput(String[] names) {
        return getInput(names, false);
    }

    public double[] getInput(String[] names, boolean deep) {
        return deep ? Arrays.copyOf(inputs, 0) : inputs;
    }
    
    /* getters //setters for pojo contract */

    public double[] getInputs() {
        return this.inputs;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public void setTimespan(double timespan) {
        this.timespan = timespan;
    }

    public void setSampling(double sampling) {
        this.sampling = sampling;
    }
}
