/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.model.dynamic.items;

/**
 *
 * @author Home
 */
public interface AbstractDynamicItemFactory {
    
    public DynamicItem createFirstOrderLag(double gain, double timeConstant);
    
    public DynamicItem createDelay(double time);
    
    public DynamicItem createMeasurmentNoise(double standardDeviation);
    
}
