package ua.kpi.atep.model.dynamic.items;

/**
 * Represents a first order lag in the discrete dynamic system
 *
 * K ./(T.s + 1)
 *
 * @author Kostiantyn Kovalchuk
 */
public class FirstOrderLag extends DynamicItem {

    private static final long serialVersionUID = 22L;

    /**
     * K - gain
     */
    private double gain;

    /**
     * T - time constant
     */
    private double timeConstant;

    /**
     * Value on the previous tick
     */
    transient private double prev;															//! null initial conditions

    public FirstOrderLag(double samplingTime, double gain,
            double timeConstant) {
        super(samplingTime);

        this.gain = gain;
        this.timeConstant = timeConstant;
        this.prev = initialCondition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double handleValue(double in) {

        /* backward Euler method for first-order item */
        double result = prev + (getSamplingTime() * (gain * in - prev)) / timeConstant;

        /* remember the previous item */
        prev = result;
        return result;
    }

    /* Getters and setters, primarily for debugging */
    double getGain() {
        return this.gain;
    }

    void setGain(double gain) {
        this.gain = gain;
    }

    double getTimeConstant() {
        return this.timeConstant;
    }

    void setTimeConstant(double timeConstant) {
        this.timeConstant = timeConstant;
    }
}
