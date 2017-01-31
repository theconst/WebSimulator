package ua.kpi.atep.model.dynamic.items;

import java.util.Random;

public class MeasurementNoise extends DynamicItem {
    
    private static final long serialVersionUID = 33L;

    private double standardDeviation;
    
    //mean of noise is zero, unlikely to be changed
    private double mean = 0.0;
    
    Random generator = new Random(); 
  
    public MeasurementNoise(double samplingTime, double standardDeviation) {
        super(samplingTime);
        this.standardDeviation = standardDeviation;
        generator.setSeed(System.currentTimeMillis());
    
    }

    /**
     * Computes the gaussian noise 
     * of the signal
     * (thanks to 
     * http://stackoverflow.com/questions/20016372/how-to-generate-gaussian-noise-in-java)
     * @param in
     * @return 
     */
    @Override
    public double handleValue(double in) {
        return in + generator.nextGaussian() * standardDeviation + mean;
    }

    /* getters and setters, primarily for debug
    
    /**
     * @return the standardDeviation
     */
    double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * @param standardDeviation the standardDeviation to set
     */
    void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    
    
}