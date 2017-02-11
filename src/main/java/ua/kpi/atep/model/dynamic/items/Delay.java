package ua.kpi.atep.model.dynamic.items;

/**
 * Represents delay in discrete dynamic system
 *
 * @author Kostiantyn Kovalchuk
 */
class Delay extends DynamicItem {
    
    private static final long serialVersionUID = 11L;

    private int ticksDelay;

    /**
     * Previous handleValue of the input
     */
    private double[] prev;

    /**
     * Ticks modulo ticksDelay
     *
     * this variable stores index of the {@value #prev} array to be returned on
     * the next step
     */
    private int ticks;

    /**
     * Constructs the delay item with the specified sampling and delay in time
     * units
     *
     * @param samplingTime sampling time
     * @param delay delay
     */
    public Delay(double samplingTime, double delay) {
        this(samplingTime, (int) (delay / samplingTime));
    }

    /**
     * Constructs the delay item with the specified sampling time and delay in
     * ticks
     *
     * @param samplingTime
     * @param ticksDelay
     */
    public Delay(double samplingTime, int ticksDelay) {
        super(samplingTime);

        this.ticksDelay = ticksDelay;
        this.prev = new double[ticksDelay];									//! zero initial conditions
        this.ticks = 0;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double handleValue(double in) {
        double result = prev[ticks];

        prev[ticks] = in;

        /* increment ticks */
        ticks = (ticks + 1) % ticksDelay;
        return result;
    }

    /* getters and setters, primarily for debugging */
    int getTicksDelay() {
        return this.ticksDelay;
    }

    void setTicksDelay(int ticksDelay) {
        this.ticksDelay = ticksDelay;
    }

    double[] getPrev() {
        return this.prev;
    }

    void setPrev(double[] prev) {
        this.prev = prev;
    }

    int getTicks() {
        return this.ticks;
    }

    void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
