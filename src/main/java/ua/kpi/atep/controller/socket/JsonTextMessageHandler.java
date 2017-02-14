/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.controller.socket;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.springframework.web.socket.TextMessage;

/**
 * Utility class to deal with json messages
 * 
 * @author Home
 */
class JsonTextMessageHandler {
    
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
    
    public static JsonObject decode(TextMessage message) {
        return Json.createReader(new StringReader(message.getPayload()))
                .readObject();
    }
    
    public static TextMessage encode(JsonValue message) {
        return new TextMessage(message.toString());
    }
}
