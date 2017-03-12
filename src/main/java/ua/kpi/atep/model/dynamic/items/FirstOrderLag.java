package ua.kpi.atep.model.dynamic.items;

/**
 * Represents a first order lag in the discrete dynamic system
 *
 * K ./(T.s + 1)
 *
 * @author Kostiantyn Kovalchuk
 */
class FirstOrderLag extends DynamicItem {

    /*
     *  If reference fields are ever added, modify the clone method! 
     */
    private static final long serialVersionUID = 22L;

    /**
     * K - gain
     */
    private final double gain;

    /**
     * T - time constant
     */
    private final double timeConstant;

    /**
     * Value on the previous tick
     */
    private double prev;															//! null initial conditions

    public FirstOrderLag(double samplingTime, double gain,
            double timeConstant) {
        super(samplingTime);

        this.gain = gain;
        this.timeConstant = timeConstant;

        //this was a bug
        //this.prev = initialCondition;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public DynamicItem clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void setInitialCondition(double initialCondition) {
        super.setInitialCondition(initialCondition);
        prev = initialCondition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double handleValue(double in) {

        /* backward Euler method for first-order item */
        double result
                = prev + (getSamplingTime() * (gain * in - prev)) / timeConstant;

        /* remember the previous item */
        prev = result;
        return result;
    }
}
