package ua.kpi.atep.model.dynamic.items;

import java.util.Arrays;

/**
 * Represents delay in discrete dynamic system
 *
 * @author Kostiantyn Kovalchuk
 */
class Delay extends DynamicItem {
    
    private static final String DELAY_GREATER_THAN_ZERO_MSG 
            = "Delay should be greater than zero";
    
    private static final long serialVersionUID = 11L;

    private final int ticksDelay;

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
        this(samplingTime, (int) Math.ceil(delay / samplingTime));
    }

    /**
     * Constructs the delay item with the specified sampling time and delay in
     * ticks
     *
     * @param samplingTime
     * @param ticksDelay
     */
    private Delay(double samplingTime, int ticksDelay) {
        super(samplingTime);
        if (ticksDelay < 0) {
            throw new IllegalArgumentException();
        }
        
        this.ticksDelay = ticksDelay != 0 ? ticksDelay : 1;
        this.prev = new double[ticksDelay];									//! zero initial conditions
        this.ticks = 0;
    }

    /**
     * clones the delay item
     */
    @Override
    public DynamicItem clone() throws CloneNotSupportedException {
        Delay clone = (Delay) super.clone();
        clone.prev = Arrays.copyOf(prev, prev.length);
        
        return clone;
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
    
    @Override
    public void setInitialCondition(double initialCondition) {
        super.setInitialCondition(initialCondition);
        Arrays.fill(prev, initialCondition);   
    }

}
