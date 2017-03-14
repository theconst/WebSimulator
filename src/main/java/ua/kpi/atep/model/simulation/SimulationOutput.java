/*
 * SimulationOutput.java    TODO: decouple from json representaition
 */
package ua.kpi.atep.model.simulation;


import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import ua.kpi.atep.model.dynamic.object.AlarmType;

/**
 *
 * Output that is tightly coupled to json representation
 * 
 * @author Home
 */
public class SimulationOutput implements JsonValue {
    
    public static final String TICKS = "ticks";
    
    private final JsonObjectBuilder ob = Json.createObjectBuilder();
    
    private final Map<String, Object> values = new HashMap<>();

    public Map<String, Object> getAllTrends() {
        return values;
    }
    
    public double[] getTrend(String name) {
        return (double[]) values.get(name);
    }
    
    public double[] getTicks() {
        return (double[]) values.get(TICKS);
    }
    
    public void setTrend(String name, double[] value) {
        values.put(name, value);
        ob.add(name, addAll(value));
    }
    
    public void setAlarm(String name, AlarmType alarmType) {
        ob.add(alarmType.toString(), name);
    }
    
    public void setTicks(double[] value) {
        values.put(TICKS, value);
        ob.add(TICKS, addAll(value));
    }
  
    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }
    
    @Override
    public String toString() {
        return ob.build().toString();
    }
    
    private static JsonArray addAll(double... vals) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        for (double v: vals) {
            builder.add(v);
        }
        return builder.build();
    }
} 
