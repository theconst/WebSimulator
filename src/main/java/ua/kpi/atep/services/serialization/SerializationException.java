/*
 * 
 */
package ua.kpi.atep.services.serialization;

/**
 *
 * @author Home
 */
public class SerializationException extends Exception {
    
    public SerializationException(String message) {
        super(message);
    }
    
    public SerializationException(Exception reason) {
        super(reason);
    }
}
