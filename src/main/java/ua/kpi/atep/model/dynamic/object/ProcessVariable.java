/*
 *
 */
package ua.kpi.atep.model.dynamic.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Home
 */
public abstract class ProcessVariable implements Serializable {
    
    private final double[] initial;
    
    private final String[] names;
    
    public int size() {
        return names.length;
    }
    
    public ProcessVariable(String[] names, double[] initial) {
        checkEquals(names.length, initial.length);
        this.initial = Arrays.copyOf(initial, initial.length);
        this.names = Arrays.copyOf(names, names.length);
    }
    
    protected <T> T[] getCopyOf(T[] array, boolean deep) {
        if (deep) {
            return Arrays.copyOf(array, array.length);
        }
        return array;
    }

    protected double[] getCopyOf(double[] array, boolean deep) {
        if (deep) {
            return Arrays.copyOf(array, array.length);
        }
        return array;
    }
    
    protected final void checkEquals(int first, int second) {
        if (first != second) {
            throw new IllegalArgumentException();
        }
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
