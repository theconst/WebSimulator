/*
 * DynamicItemFactory.java
 */
package ua.kpi.atep.model.dynamic.items;

/**
 * Factory for creating dynamic item
 *
 * @author Konstantin Kovalchuk
 */
public class DynamicItemFactory implements AbstractDynamicItemFactory {
    
    private static final String SAMPLING_GREATER_THAN_ZERO 
            = "sampling should be greater than zero";

    private final double sampling;

    public DynamicItemFactory(double sampling) {
        if (sampling <= 0.0) {
            throw new IllegalArgumentException(SAMPLING_GREATER_THAN_ZERO);
        }
        
        this.sampling = sampling;
    }

    @Override
    public DynamicItem createFirstOrderLag(double gain, double timeConstant) {
        return new FirstOrderLag(sampling, gain, timeConstant);
    }

    @Override
    public DynamicItem createDelay(double time) {
        return new Delay(sampling, time);
    }

    @Override
    public DynamicItem createPID(double p, double i, double d) {
       return new PIDController(sampling, p, i, d);
    }

    @Override
    public DynamicItem createMeasurmentNoise(double standardDeviation) {
        return new MeasurementNoise(sampling, standardDeviation);
    }

    public static DynamicItemFactory newInstance(double sampling) {
        return new DynamicItemFactory(sampling);
    }

}
