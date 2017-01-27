/*
 * JsonEncoder.java 29.09.2016
 */
package ua.kpi.atep.controller.socket;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * Encoder relies only on the collections such as lists and maps, primitive
 * types and classes that are JsonValues
 *
 * @author Konstantin Kovalchuk
 */
public class JsonEncoder implements Encoder.Text<JsonValue> {

    /**
     * Retrieve the process data
     *
     * @param output
     * @return
     * @throws EncodeException
     */
    @Override
    public String encode(JsonValue output) throws EncodeException {
        return output.toString();
    }

    @Override
    public void init(EndpointConfig config) {
        //do nothing
        //calls when in service
    }

    @Override
    public void destroy() {
        //do noting
        //calls when out of service
    }
  
    
    public static JsonArray addAll(double... vals) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        for (double v: vals) {
            builder.add(v);
        }
        return builder.build();
    }
    
    public static void addArray(JsonObjectBuilder ob, String name,
            JsonArray array) {
        ob.add(name, array);
    }
}
