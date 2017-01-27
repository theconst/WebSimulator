/*
 * SimulationWebSocketConfigurator.java
 */
package ua.kpi.atep.controller.socket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import static ua.kpi.atep.controller.WebSocketMediator.interactWithWebSocketConfigurator;

/**
 *
 * @author Home
 */
public class SimulationWebSocketConfigurator extends SpringConfigurator {

    
    /**
     * Modifies the handshake
     * 
     * Main goal of this method is to put model from the http session 
     * to the WebSocket session
     * 
     * @param config
     * @param request
     * @param response 
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
            HandshakeRequest request,
            HandshakeResponse response) {
        interactWithWebSocketConfigurator(config, request);
    }
   
}
