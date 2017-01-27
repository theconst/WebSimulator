package ua.kpi.atep.ztrash;

///*
// *
// */
//package ua.kpi.atep.model.dynamic;
//
//
//import javax.json.JsonValue;
//
///**
// *
// * @author Home
// */
//public abstract class DynamicModelOutput implements JsonValue {
//    
//    public static final String TICKS = "ticks";
//    
//    private double[] ticks;
//    
//    public DynamicModelOutput(double[][] output) {
//        ticks = output[output.length - 1];
//    }
//    
//    public double[] getTicks() {
//        return ticks;
//    }
//    
//    abstract double[] get(int ordinal) {
//        return output[ordinal];
//    }
//
//    @Override
//    abstract public ValueType getValueType();
//    
//    @Override
//    abstract public String toString();
//    
//}
