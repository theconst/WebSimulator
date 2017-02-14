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
public abstract class DoubleVector implements Serializable {

    double[] value;

    public DoubleVector(int size) {
        value = new double[size];
    }

    protected final void checkEquals(int first, int second) {
        if (first != second) {
            throw new IllegalArgumentException();
        }
    }

    public void setValue(double[] value) {
        checkEquals(this.value.length, value.length);
        this.value = value;
    }

    public DoubleVector(double[] values) {
        this.value = Arrays.copyOf(values, values.length);
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

    public double[] toArray(boolean deep) {
        return getCopyOf(value, deep);
    }

    public int size() {
        return value.length;
    }

    public double[] toArray() {
        return toArray(false);
    }
}
