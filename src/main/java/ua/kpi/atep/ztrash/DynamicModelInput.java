/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.ztrash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Home
 */
abstract public class DynamicModelInput {
    
    public static final String TIMESPAN = "timespan";
    public static final String SAMPLING = "sampling";
    
    private final double timespan;
    private final double sampling;
    private final double[] inputs;
    
    
    public DynamicModelInput(Map<String, Double> map) {
        timespan = map.get("timespan");
        sampling = map.get("sampling");
        inputs = assignInputs(map);
    }
    
    
    public double getTimespan() {
        return timespan;
    }
    
    public double getSampling() {
        return sampling;
    }
    
    public double[] getInputs() {
        return inputs;
    }
    
    abstract protected double[] assignInputs(Map<String, Double> map);
}
