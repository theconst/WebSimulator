/*
 * WebSocketMediator.java
 */
package ua.kpi.atep.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import ua.kpi.atep.model.dynamic.object.DynamicModel;
import ua.kpi.atep.services.UserSession;

/**
 * Incapsulates interaction between the websocket and http
 * 
 * Session controller and websocket endpoint interact via static method 
 * and controller chooses the right mediator to interact with
 *
 * @author
 */
public class WebSocketMediator {

    private static final String INTERACT_WITH_HTTP_NOT_CALLED
            = "interact with http first";

    private static final String INTERACT_WITH_WS_NOT_CALLED
            = "interact with ws first";

    private static final String MEDIATOR_ATTR = "mediator";

    private int userID;

    private DynamicModel dynamicModel;

    private boolean interactedWithHttp = false;

    private boolean interactedWithWS = false;

    private <T> void putAttributeToWS(String name, T value,
            ServerEndpointConfig config) {
        config.getUserProperties().put(name, value);
    }

    private int getUserId() {
        checkHttpInteraction();
        return userID;
    }

    private DynamicModel getDynamicModel() {
        checkHttpInteraction();
        return dynamicModel;
    }

    private void checkHttpInteraction() {
        if (!interactedWithHttp) {
            throw new IllegalStateException(INTERACT_WITH_HTTP_NOT_CALLED);
        }
    }

    private void checkWSInteraction() {
        if (!interactedWithWS) {
            throw new IllegalStateException(INTERACT_WITH_WS_NOT_CALLED);
        }
    }

    private void visitHttp(HttpServletRequest request,
            UserSession session) {

        userID = session.getUser().getId();
        dynamicModel = session.getUser().getAssignment().getModel();

        request.getSession(true).setAttribute(MEDIATOR_ATTR, this);

        interactedWithHttp = true;
    }

    private void visitWebSocketConfigurator(ServerEndpointConfig config) {
        checkHttpInteraction();
        putAttributeToWS(MEDIATOR_ATTR, this, config);
        interactedWithWS = true;
    }

    public static void interactWithHttp(HttpServletRequest request,
            UserSession session) {
        WebSocketMediator instance = new WebSocketMediator();
        instance.visitHttp(request, session);
    }

    public static void interactWithWebSocketConfigurator(
            ServerEndpointConfig config,
            HandshakeRequest request) {
        getMediator(request).visitWebSocketConfigurator(config);
    }

    private static WebSocketMediator getMediator(Session s) {
        WebSocketMediator result = (WebSocketMediator) s.getUserProperties()
                .get(MEDIATOR_ATTR);

        //check result correctness
        result.checkWSInteraction();

        return result;
    }

    private static WebSocketMediator getMediator(HandshakeRequest request) {
        WebSocketMediator result = (WebSocketMediator) ((HttpSession) request
                .getHttpSession()).getAttribute(MEDIATOR_ATTR);

        //checke result correctness
        result.checkHttpInteraction();
        return result;
    }


    /* methods for accessing the ws */
    public static int getUserID(Session s) {
        return getMediator(s).getUserId();
    }

    public static DynamicModel getDynamicModel(Session s) {
        return getMediator(s).getDynamicModel();
    }
}
