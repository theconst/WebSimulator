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
     * 
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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns the value of the output at the next tick
     *
     * @param in - handleValue of the input on the current step
     * @return output after the next tick
     */
    protected abstract double handleValue(double in);

    
    /**
     * 
     * 
     * @param in
     * @param steps
     * @return 
     */
    public double value(double in, int steps) {
        if (initialized) {
            for (int i = 0; i < steps - 1; i++) {
                handleValue(in);
            }
            return handleValue(in);
        }
        initialized = true;
        return getInitialCondition();
    }
    
    public double value(double in, double timespan) {
        return value(in, (int) (timespan / samplingTime) + 1);
    }
    

    public double value(double in) {
        return value(in, 1);
    }

    public double getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(double samplingTime) {
        this.samplingTime = samplingTime;
    }

    /**
     * @return the initialCondition
     */
    public double getInitialCondition() {
        return initialCondition;
    }

    /**
     * @param initialCondition the initialCondition to set
     */
    public void setInitialCondition(double initialCondition) {
        this.initialCondition = initialCondition;
    }
    
    
    /**
     * Returns completely new item with feedback connection
     * 
     * @param other
     * @return 
     */
    public DynamicItem parallel(DynamicItem other) {
        try {
            return DynamicItems.parallelSumConnection(
                    (DynamicItem) this.clone(), (DynamicItem) other.clone());
        } catch (CloneNotSupportedException ex) {
           throw new UnsupportedOperationException(ex);
        } 
    } 
    
    /**
     * Returns completely new dynamic item thie feedbakcConnection
     * 
     * @param other
     * @return 
     */
    public DynamicItem sequential(DynamicItem other) {
        try {
            return DynamicItems.sequentialConnection(
                    (DynamicItem) this.clone(), (DynamicItem) other.clone());
        } catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
    
    public DynamicItem negativeFeedback() {
        try {
            return DynamicItems
                    .negativeFeedbackConnection((DynamicItem) this.clone());
        } catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException(ex);
        } 
    }
}
