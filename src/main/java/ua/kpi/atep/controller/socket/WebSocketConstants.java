/*
 * Utility class for storing constants associated with websocket endpoint
 *
 * WebSocketConstants.java
 */
package ua.kpi.atep.controller.socket;

/**
 * Utility class for storing constants associated with websocket
 *
 * @author Home
 */
class WebSocketConstants {

    public static final String ENDPOINT = "/model";
    public static final String TICKS = "ticks";
    public static final String TIMESPAN = "timespan";
    public static final String SAMPLING = "sampling";
    public static int SIZE = 2097152;           //2MB

    private WebSocketConstants() {
        //do not instantiate
    }
}
