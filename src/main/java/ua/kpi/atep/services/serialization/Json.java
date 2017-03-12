/*
 * Json.java
 */
package ua.kpi.atep.services.serialization;

import com.google.gson.Gson;

/**
 *
 * Generic service to serialize and deserialize POJO's
 * 
 * @author Home
 */
public class Json {
    
    private static Gson gson = new Gson();

    public static <T> String stringify(T what) {
        return gson.toJson(what);
    }
    
    public static <T> T parse(String source, Class<T> clazz) {
        return gson.fromJson(source, clazz);
    }
    
}
