///*
// * SimulationWebSocket.java 05.01.2017
// */
//package ua.kpi.atep.ztrash;
//
//import java.io.IOException;
//
//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.json.JsonObjectBuilder;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import static ua.kpi.atep.controller.socket.JsonUtils.addAll;
//import static ua.kpi.atep.controller.socket.JsonUtils.addArray;
//import static ua.kpi.atep.controller.socket.JsonUtils.decode;
//import static ua.kpi.atep.controller.socket.JsonUtils.encode;
//
//import ua.kpi.atep.services.serialization.CSVWriter;
//import ua.kpi.atep.services.SimulationService;
//
//import static ua.kpi.atep.controller.socket.WebSocketConstants.SAMPLING;
//import static ua.kpi.atep.controller.socket.WebSocketConstants.TICKS;
//import static ua.kpi.atep.controller.socket.WebSocketConstants.TIMESPAN;
//
//import ua.kpi.atep.model.dynamic.object.DynamicModel;
//import ua.kpi.atep.services.UserSession;
//
///**
// * This is a socket connection that operates with model
// *
// * //TODO: extend text message with Simulation message and send them
// *
// * @author Konstantin Kovalchuk
// */
//public class SimulationWebSocketHandler extends TextWebSocketHandler {
//
//    //find a less dependent way for accessing the session
//    private static final String USER_SESSION = "scopedTarget.userSessionImpl";
//
//    /*
//    * Low-level code here purely relies on arrays and ordering of the arguments
//    * 
//    * 
//    * Adding abstractions like HashMap<String, Double> add signinificant overhead
//    * because of repeating hashCode() computation
//    * 
//    * 
//    * Streaming to history to the array of bytes also speeds the application up
//     */
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
//    
//    /**
//     * holds size of memory for user story recording
//     */
//    //temporary fix
//    @Value("${web.socket.story.size}")
//    private int storySize;
//
//
//    /**
//     * writer of the story
//     */
//    private CSVWriter story;
//
//    /**
//     * Stores input variables only
//     */
//    private double[] inputs;
//
//    /* [input1] [1][2][3]...
//       [input2] [1][2][3]...
//       ....
//       [output1][1][2][3]...
//       [output2][1][2][3]...
//       ....
//       [ticks]  [1][2][3]... */
//    private double[][] processVariables;
//
//    /**
//     * Index of ticks in processVariables array (used for timing)
//     */
//    private int indexOfTicks;
//
//    /**
//     * Memory interval to track
//     */
//    private double timespan;
//
//    /**
//     * Desired sampling (interval between measurments) This DOES NOT influences
//     * sampling of the actual model, e.g. it it is smaller than sampling of the
//     * actual dynamic model, the sampling will be minimum possible, else it will
//     * remain the same, but less measurements will be made (decimation will
//     * occur)
//     */
//    private double sampling;
//
//    /**
//     * Number of inputs in the model
//     */
//    private int inputCount;
//
//    /**
//     * Number of outputs of the model
//     */
//    private int outputCount;
//
//    /**
//     * Time of the next measurment
//     */
//    private double nextTickStart = 0.0;
//
//    /**
//     * Names of inputs to track
//     */
//    private String[] inputNames;
//
//    /**
//     * Names of outputs to track
//     */
//    private String[] outputNames;
//    
//    /**
//     * alarms low
//     */
//    private boolean[] alarmsLow;
//    
//    /**
//     * alarms high
//    */
//    private boolean[] alarmsHi;
//       
//    /**
//     * alarms critical
//     */
//    private boolean[] alarmsHiCritical;
//    
//    /**
//     * alarms low critical
//     */
//    private boolean[] alarmsLowCritical;
// 
//    
//    /**
//     * Service for simulation initialization
//     */
//    @Autowired
//    SimulationService simulationService;
//    
//    
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//        /* get parameters from http session */
//        UserSession userSession 
//                = (UserSession) session.getAttributes().get(USER_SESSION);
//
//        model = userSession.getUser().getAssignment().getModel();
//        userID = userSession.getUser().getId();
//
//        /* do prior memory allocation */
//        int count = model.getInputsCount() //no of inputs
//                + model.getOutputsCount() //no of outputs
//                + 1;                                         //for ticks
//        indexOfTicks = count - 1;                                //index of ticks
//
//        /* memory allocation */
//        inputs = new double[model.getInputsCount()];
//        processVariables = new double[count][0];
//
//        /* initialize headers */
//        String[] headers = new String[model.getInputsCount() //inputs
//                + model.getOutputsCount()                     //no of outputs
//                + 1];                                         //for ticks
//
//        /* getting required paramters for modelling */
//        inputNames = model.getInput().getNames();             //names of inputs
//        outputNames = model.getOutput().getNames();           //names of outputs
//
//        inputCount = model.getInputsCount();
//        outputCount = model.getOutputsCount();
//
//        /* fill in arrays */
//        System.arraycopy(inputNames, 0, headers, 0, inputNames.length);
//        System.arraycopy(outputNames, 0, headers, inputNames.length, outputNames.length);
//        headers[indexOfTicks] = TICKS;
//
//        /* initialize story */
//        story = new CSVWriter(storySize, headers);
//    }
//
//    private double getDouble(JsonObject ob, String name) {
//        return ob.getJsonNumber(name).doubleValue();
//    }
//
//    /**
//     * Executes the command given to websocket
//     *
//     * @param message input to the model
//     * @param session current session
//     * @throws java.lang.Exception
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message)
//            throws Exception {
//
//        /* do the main loop of messaging */
//
//        getInputs(decode(message));
//        doSimulationStep();
//
//        /* Do the logging */
//        story.writeChunk(processVariables);
//
//        try {
//            /* If user overflows allocated memory, close session,
//               it is CLIENT'S RESPONSIBILITY to hanle messages and 
//               interation */
//            if (story.getSize() >= storySize - 1) {
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
//                    addAll(processVariables[indexOfTicks]));
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
//    /**
//     * When the connection closes, persist user data to database
//     *
//     * @param session
//     * @param status
//     * @throws java.lang.Exception
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, 
//            CloseStatus status) throws Exception {
//        if (story != null) {
//            story.close();
//            simulationService.saveModellingData(userID, story.toString());
//        }
//    }
//
//    /**
//     * Get input parameter from message
//     *
//     * @param message message with input parameters
//     */
//    private void getInputs(JsonObject message) {
//
//        /* simply go through ordered outputs and get parameters from message */
//        int k = 0;
//        for (String input : inputNames) {
//            inputs[k++] = getDouble(message, input);
//        }
//
//        /* get timespan and sampling */
//        timespan = getDouble(message, TIMESPAN);
//        sampling = getDouble(message, SAMPLING);
//    }
//
//    private void doSimulationStep() {
//
//        /* calculate length of the output */
//        int len = (int) (Math.ceil(timespan / sampling) + 0.1);
//
//        /* allocate memory */
//        for (int i = 0; i < processVariables.length; ++i) {
//            processVariables[i] = allocate(processVariables[i], len);
//        }
//
//        /* --> if it is NOT the first tick, add sampling */
//        nextTickStart = (nextTickStart == 0) ? 0 : nextTickStart + sampling;
//
//        for (int i = 0; i < len; ++i) {
//
//            /* fill in array according to the convention */
//            for (int j = 0; j < inputCount; ++j) {
//                processVariables[j][i] = inputs[j];            //fill in inpus
//            }
//
//            /* evaluate the output on the period of ONE Sampling */
//            model.setTimespan(sampling);
//
//            /* evalueate computeValue through sampling period */
//            double[] values = model.value(inputs);
//
//            for (int j = 0; j < outputCount; ++j) {
//                processVariables[j + inputCount][i] = values[j]; //outputs
//            }
//
//            //fill in ticks
//            processVariables[indexOfTicks][i] = (i == 0) ? nextTickStart
//                    : processVariables[indexOfTicks][i - 1] + sampling;
//        }
//
//        /* 
//         * nextTickStart = {last finished tick} + samling
//         * (samling can be changed, so samling is added above -->)
//         */
//        nextTickStart = processVariables[indexOfTicks][len - 1];
//    }
//
//    /**
//     * Allocates the array lazily
//     *
//     * If already allocated enough memory, just does nothing
//     *
//     * @param array array to allocate
//     * @param size size of the array
//     * @return newly allocated array or the same array
//     */
//    private double[] allocate(double[] array, int size) {
//
//        //strict equality, or else more advanced strategy
//        return (array.length == size) ? array : new double[size];
//    }
//}
