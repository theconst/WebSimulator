package ua.kpi.atep.model.dynamic.items;

/**
 * PID regulator item
 * 
 * @author PID regulator
 */
class PIDController extends DynamicItem {

    private static final long serialVersionUID = 44L;

    private final double proportionalGain;
    private final double differentialTime;
    private final double integralTime;

    private double prev = 0.0;																					//! zero initial conditions

    private double integral = 0.0;

    public PIDController(double samplingTime, double proportionalGain,
            double integralTime, double differentialTime) {
        super(samplingTime);

        this.proportionalGain = proportionalGain;
        this.integralTime = integralTime;
        this.differentialTime = differentialTime;
    }

    /**
     * @inheritdoc
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

    @Override
    public double handleValue(double in) {
        integral += in * getSamplingTime();
        double derivative = (in - prev) / getSamplingTime();
        double result = proportionalGain
                * (in + ((integralTime != 0.0) ? (integral / integralTime) : 0)
                + derivative * differentialTime);
        prev = in;

        return result;
    }

}
