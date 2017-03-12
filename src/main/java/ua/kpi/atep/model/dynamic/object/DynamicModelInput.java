/*
 * DynamicModelInput.java
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 *
 * @author Home
 */
public class DynamicModelInput extends ProcessVariable {
    
    private double[] min;
    
    private double[] max;
    
    public DynamicModelInput(String[] names, double[] initial) {
        super(names, initial);
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
