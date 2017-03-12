/*
 * MessageSettings.java
 */
package ua.kpi.atep.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class that contains settings for common messages needed for simulation
 * 
 * @author Home
 */
@Component
public class MessageSettings {
   
    @Value("${web.param.heartbeat.url}")
    private String heartbeatURL;
    
    @Value("${web.param.history.url}")
    private String historyURL;
    
    @Value("${web.param.socket.url}")
    private String websocketURL;
    
    @Value("${web.param.heartbeat.period}")
    private int heartbeatPeriod;
    
    @Value("${web.socket.min.message.period}")
    private int minMessagePeriod;
    

    public String getHeartbeatURL() {
        return heartbeatURL;
    }

    public void setHeartbeatURL(String heartbeatURL) {
        this.heartbeatURL = heartbeatURL;
    }

    public int getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public void setHeartbeatPeriod(int heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    public int getMinMessagePeriod() {
        return minMessagePeriod;
    }

    public void setMinMessagePeriod(int minMessagePeriod) {
        this.minMessagePeriod = minMessagePeriod;
    }

    public String getWebsocketURL() {
        return websocketURL;
    }

    public void setWebsocketURL(String websocketURL) {
        this.websocketURL = websocketURL;
    }

    public String getHistoryURL() {
        return historyURL;
    }

    public void setHistoryURL(String historyURL) {
        this.historyURL = historyURL;
    }
}
