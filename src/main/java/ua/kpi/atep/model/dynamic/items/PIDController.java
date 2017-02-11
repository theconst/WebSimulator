package ua.kpi.atep.model.dynamic.items;

//TODO use Tustin transform
class PIDController extends DynamicItem {

    private static final long serialVersionUID = 44L;

    private double proportionalGain;
    private double differentialTime;
    private double integralTime;

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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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

    public double getProportionalGain() {
        return proportionalGain;
    }

    public void setProportionalGain(double proportionalGain) {
        this.proportionalGain = proportionalGain;
    }

    public double getDifferentialTime() {
        return differentialTime;
    }

    public void setDifferentialTime(double differentialTime) {
        this.differentialTime = differentialTime;
    }

    public double getIntegralTime() {
        return integralTime;
    }

    public void setIntegralTime(double integralTime) {
        this.integralTime = integralTime;
    }

}
