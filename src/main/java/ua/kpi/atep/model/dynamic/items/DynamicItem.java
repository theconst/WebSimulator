package ua.kpi.atep.model.dynamic.items;

import java.io.Serializable;

/**
 * Item of discrete dynamic system
 *
 * TODO: create prototype to mimic the behaviour of real item (real item
 * is unchanged when it is used multiple times)
 * 
 * @author Kostiantyn Kovalchuk
 */
public abstract class DynamicItem implements Serializable, Cloneable {
    
    private static final String SAMPLING_GREATER_THAN_ZERO_MSG
            = "Samling time must be greater than zero";
    
    private static final String TIMESPAN_GREATER_OR_EQUAL_TO_ZERO_MSG 
            = "Timespan must be greater than zero";
    
    protected static final String SETTING_INITIAL_CONDITION_ON_INITIALIZED_ITEM 
            = "Setting initial condition on initialized item";
    
    private static final long serialVersionUID = 65535L;
    
    private static final double DEFAULT_SAMPLING = 0.1;

    /**
     * Sampling time - caller has to ensure it is the same for all dynamic items
     * in the system
     */
    protected double samplingTime;

    /**
     * was the model initialized ?
     */
    protected boolean initialized = false;

    /**
     * Initial condition of the item
     */
    protected double initialCondition;
    
    /**
     * Construct the dynamic item with the specified sampling
     *
     * @param samplingTime
     * @param initialCondition
     */
    public DynamicItem(double samplingTime, double initialCondition) {
        if (samplingTime <= 0.0) {
            throw new IllegalArgumentException(SAMPLING_GREATER_THAN_ZERO_MSG);
        }
        this.samplingTime = samplingTime;
        this.initialCondition = initialCondition;
    }
    
    public DynamicItem(double samplingTime) {
        this(samplingTime, 0.0);
    }
    
    /**
     * Default constructor
     */
    public DynamicItem() {
        this(DEFAULT_SAMPLING, 0.0);
    }
    
    
    /**
     * Clone method is used to create copies of items
     * 
     * Dynamic items DO NOT HAVE references in them,
     * so the returned copy is deep
     * 
     * @return deep copy of the object
     * @throws CloneNotSupportedException 
     */
    @Override
    public DynamicItem clone() throws CloneNotSupportedException {
        return (DynamicItem) super.clone();
    }

    /**
     * Returns the value of the output at the next tick
     *
     * @param in - handleValue of the input on the current step
     * @return output after the next tick
     */
    protected abstract double handleValue(double in);

    
    /** 
     * Evalueates value after samples * sampling time
     * 
     * @param in            input of the dynamic item
     * @param samples       number of samples
     * @return              value of the dynamic item after samples passed
     */
    private double value(double in, int samples) {
        if (initialized) {
            for (int i = 0; i < samples - 1; i++) {
                handleValue(in);
            }
            return handleValue(in);
        }
        initialized = true;
        return getInitialCondition();
    }
    
    
    /**
     * Evalueates value after timespan time passed
     * 
     * @param in            input value
     * @param timespan      
     * @return              value of the dynamic item after timespan passed    
     */
    public double value(double in, double timespan) {
        checkTimespan(timespan);
        return value(in, (int) Math.ceil(timespan / samplingTime));
    }
    

    /**
     * Convinience method for evaluation of value after one sample
     * 
     * @param in input value
     * @return value after one sample
     */
    public double value(double in) {
        return value(in, 1);
    }

    /**
     * Gets the sampling time of the item
     * 
     * @return 
     */
    public double getSamplingTime() {
        return samplingTime;
    }
    
    
    /**
     * @return the initialCondition
     */
    public double getInitialCondition() {
        return initialCondition;
    }
    
    /**
     * Checks in the item was initialized
     */
    protected void checkNotInitialized() {
         if (initialized) {
            throw new IllegalStateException();
        }
    }

    /**
     * Overrided in the sublasses
     * 
     * @param initialCondition the initialCondition to set
     */
    public void setInitialCondition(double initialCondition) {
        checkNotInitialized();
        this.initialCondition = initialCondition;
    }
    
    
    /**
     * Checks timespan on correctness
     * 
     * @param arg 
     */
    private void checkTimespan(double arg) {
        if (arg <= 0) {
            throw new IllegalArgumentException(TIMESPAN_GREATER_OR_EQUAL_TO_ZERO_MSG);
        }
    }
}
