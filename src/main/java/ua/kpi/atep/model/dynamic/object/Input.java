/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 *
 * @author Home
 */
public class Input extends ProcessVariableVector {
    
    private double[] min;
    
    private double[] max;
    
    public Input(String[] names, double[] initial) {
        super(names, initial);
    }
    
    public Input(Input other) {
        this(other.getNames(true), other.getInitial(true));
        this.max = other.getMax(true);
        this.min = other.getMin(true);
    }

    public double[] getMin(boolean deep) {
        return getCopyOf(min, deep);
    }
    
    public double[] getMin() {
        return getMin(false);
    }

    public void setMin(double[] min) {
        checkEquals(size(), min.length);
        this.min = Arrays.copyOf(min, min.length);
    }

    public double[] getMax(boolean deep) {
        return getCopyOf(max, deep);
    }
    
    public double[] getMax() {
        return getMax(false);
    }

    public void setMax(double[] max) {
        checkEquals(size(), max.length);
        this.max = Arrays.copyOf(max, max.length);
    }
    
}
