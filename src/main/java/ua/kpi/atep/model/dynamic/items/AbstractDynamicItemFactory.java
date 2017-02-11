/*
 * AbstractDynamicItemFactory.java
 */
package ua.kpi.atep.model.dynamic.items;

/**
 * Factory for creating dynamic items
 * 
 * @author Konstantin Kovalchuk
 */
public interface AbstractDynamicItemFactory {
    
    DynamicItem createFirstOrderLag(double gain, double timeConstant);
    
    DynamicItem createDelay(double time);
    
    DynamicItem createMeasurmentNoise(double standardDeviation);
    
    DynamicItem createPID(double p, double i, double d);
}
