/*
 * DynamicItemFactory.java
 */
package ua.kpi.atep.model.dynamic.items;


/**
 *
 * 
 * @author Konstantin Kovalchuk
 */
public class DynamicItemFactory {

    private final double sampling;
    
    public DynamicItemFactory(double sampling) {
        this.sampling = sampling;
    }
    
    public DynamicItem createFirstOrderLag(double gain, double timeConstant) {
        return new FirstOrderLag(sampling, gain, timeConstant);
    }
    
    public DynamicItem createDelay(double time) {
        return new Delay(sampling, time);
    }
    
    public DynamicItem createMeasurmentNoise(double standardDeviation) {
        return new MeasurementNoise(sampling, standardDeviation);
    }
    
    
}
