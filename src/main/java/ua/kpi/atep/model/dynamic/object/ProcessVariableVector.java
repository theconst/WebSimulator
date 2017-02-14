/*
 *
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 *
 * @author Home
 */
public class ProcessVariableVector extends DoubleVector {
    
    private double[] initial;
    
    private String[] names;
    
    public ProcessVariableVector(String[] names, double[] initial) {
        super(initial);
        checkEquals(names.length, initial.length);
        this.initial = Arrays.copyOf(initial, initial.length);
        this.names = Arrays.copyOf(names, names.length);
    }
   

    public double[] getInitial(boolean deep) {
        return getCopyOf(initial, deep);
    }
    
    public String[] getNames(boolean deep) {
        return getCopyOf(names, deep);
    }
    
    public double[] getInitial() {
        return getInitial(false);
    }
    
    public String[] getNames() {
        return getNames(false);
    }
}
