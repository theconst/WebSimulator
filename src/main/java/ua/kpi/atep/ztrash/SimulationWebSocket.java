///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ua.kpi.atep.controller.socket;
//
//import java.io.IOException;
//import javax.json.Json;
//import javax.json.JsonObjectBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import ua.kpi.atep.services.serialization.Serializer;
//import ua.kpi.atep.model.dynamic.object.Input;
//
//import static ua.kpi.atep.controller.socket.JsonTextMessageHandler.decode;
//import static ua.kpi.atep.controller.socket.WebSocketConstants.TICKS;
//import ua.kpi.atep.model.dynamic.object.DynamicModel;
//import ua.kpi.atep.services.SimulationService;
//import ua.kpi.atep.services.UserSession;
//import ua.kpi.atep.services.serialization.CSVWriter;
//
///**
// *
// * @author Home
// */
//public class SimulationWebSocket extends TextWebSocketHandler {
//
//    //find a less dependent way for accessing the session
//    private static final String USER_SESSION = "scopedTarget.userSessionImpl";
//
//    /**
//     * Id of the user
//     */
//    private int userID;
//
//    /**
//     * User model
//     */
//    private DynamicModel model;
//
//    /**
//     * writer of the story
//     */
//    private CSVWriter story;
//
//    /**
//     * Service for simulation initialization
//     */
//    @Autowired
//    SimulationService simulationService;
//
//    /**
//     * holds size of memory for user story recording
//     */
//    //temporary fix
//    @Value("${web.socket.story.size}")
//    private int storySize;
//
//    
//    private SimulationRunner runner;
//    
//    
//    @Autowired
//    private Serializer<Input, String, String> inputSerializer;
//    
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//        /* get parameters from http session */
//        UserSession userSession = (UserSession) session.getAttributes()
//                                        .get(USER_SESSION);
//
//        model = userSession.getUser().getAssignment().getModel();
//        userID = userSession.getUser().getId();
//        
//        runner = SimulationRunner.newInstance(model);
//    }
//    
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message)
//            throws Exception {
//        Input input = inputSerializer.deserialize(message.getPayload());
//        
//        runner.doSimulationStep()
//
//        /* Do the logging */
//        story.writeChunk(processVariables);
//
//        try {
//            /* If user overflows allocated memory, close session,
//               it is CLIENT'S RESPONSIBILITY to hanle messages and 
//               interation */
//            if (isThresholdReached(story)) {
//                session.close();
//                return;
//            }
//
//            /* encode data to client */
//            JsonObjectBuilder ob = Json.createObjectBuilder();
//
//            for (int k = 0; k < outputNames.length; k++) {
//
//                /* add outputs */
//                addArray(ob, outputNames[k],
//                        addAll(processVariables[inputs.length + k]));
//            }
//            /* add ticks */
//            addArray(ob, TICKS,
//                    addAll(processVariables[ticksInd]));
//
//            session.sendMessage(encode(ob.build()));
//            /**
//             * *****************************
//             */
//        } catch (IOException ex) {
//            story.close();
//            throw new IOException(ex);
//        } finally {
//            //story will be closed in after close event
//        }
//    }
//
//    private boolean isThresholdReached(CSVWriter story) {
//        return story.getSize() >= storySize - 1;
//    }
//    
//}


//        if (!initialized) {
//            /* pass session id of the user */
//            
//           String sid = message.getPayload();
//            HttpSession httpSession = sessionListenter.getSession(sid);
//            initialize(httpSession);
//            initialized = true;
//            
//            //echo jsession id to back to the client
//            session.sendMessage(message);
//            
//            return; //return from the initialization section
//        }