/*
 * SimulationWebSocket.java 05.01.2017
 */
package ua.kpi.atep.controller.socket;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import ua.kpi.atep.services.SimulationService;

import ua.kpi.atep.services.CSVWriter;

import static ua.kpi.atep.controller.WebSocketMediator.getDynamicModel;
import static ua.kpi.atep.controller.WebSocketMediator.getUserID;

import ua.kpi.atep.dynamic.generic.DynamicModel;


/*
 * Low-level code here purely relies on arrays and ordering of the arguments
 * 
 * 
 * Adding abstractions like HashMap<String, Double> add signinificant overhead
 * because of repeating hasCode() computation
 * 
 * 
 * Streaming to history to the array of bytes also speeds the application up
 */
/**
 * This is a socket connection that operates with model
 *
 * @author Konstantin Kovalchuk
 */
@ServerEndpoint(
        /* statically coded, verbose workaround */
        value = "/model",
        encoders = {JsonEncoder.class},
        decoders = {JsonDecoder.class},
        configurator = SimulationWebSocketConfigurator.class
)
public class SimulationWebSocket {

    public static final String TICKS = "ticks";

    public static final String TIMESPAN = "timespan";

    public static final String SAMPLING = "sampling";

    private int userID;

    private DynamicModel model;

    private CSVWriter story;

    /**
     * Stores input variables only
     */
    private double[] inputs;

    /* [input1] [1][2][3]...
       [input2] [1][2][3]...
       ....
       [output1][1][2][3]...
       [output2][1][2][3]...
       ....
       [ticks]  [1][2][3]... */
    private double[][] processVariables;

    private int ticksInd;

    private double timespan;

    private double sampling;

    private int inputCount;

    private int outputCount;

    private double nextTickStart = 0.0;

    private String[] inputNames;

    private String[] outputNames;

    @Autowired
    SimulationService simulationService;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config)
            throws IOException, EncodeException {

        /* get parameters from mediator */
        model = getDynamicModel(session);
        userID = getUserID(session);

        /* do prior memory allocation */
        int count = model.getInputsCount() //inputs
                + model.getOutputsCount() //no of outputs
                + 1;                                         //for ticks
        ticksInd = count - 1;

        inputs = new double[model.getInputsCount()];
        processVariables = new double[count][0];

        /* initialize headers */
        String[] headers = new String[model.getInputsCount() //inputs
                + model.getOutputsCount() //no of outputs
                + 1];                                         //for ticks

        inputNames = model.getInputs();                       //names of inputs
        outputNames = model.getOutputs();                     //names of outputs
        inputCount = model.getInputsCount();
        outputCount = model.getOutputsCount();

        /* fill in arrays */
        int k = 0;

//        System.arraycopy(inputNames, 0, headers, 0, inputNames.length);
        for (String input : inputNames) {
            headers[k++] = input;
        }

//        System.arraycopy(outputNames, 0, headers, inputNames.length, outputNames.length);
        for (String output : outputNames) {
            headers[k++] = output;
        }

//      headers[headers.length - 1] = TICKS;
        headers[k++] = TICKS;

        /* initialize story */
        story = new CSVWriter(headers);
    }

    /**
     * Logs to server log if an error occurs
     *
     * @param error error
     */
    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(SimulationWebSocket.class.getName())
                .log(Level.SEVERE, null, error);
    }

    private double getDouble(JsonObject ob, String name) {
        return ob.getJsonNumber(name).doubleValue();
    }

    /**
     * Executes the command given to websocket
     *
     * @param message input to the model
     * @param session current session
     */
    @OnMessage
    public void onMessage(JsonObject message, Session session) {
        getInputs(message);
        doSimulationStep();

        /* Do the logging */
        story.writeChunk(processVariables);

        try {

            /* send outputs */
            JsonObjectBuilder ob = Json.createObjectBuilder();

            for (int k = 0; k < outputNames.length; k++) {

                //add outputs
                JsonEncoder.addArray(ob, outputNames[k],
                        JsonEncoder.addAll(processVariables[inputs.length + k]));
            }
            //add ticks
            JsonEncoder.addArray(ob, TICKS,
                    JsonEncoder.addAll(processVariables[ticksInd]));

            session.getBasicRemote().sendObject(ob.build());

        } catch (IOException | EncodeException ex) {
            Logger.getLogger(SimulationWebSocket.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * When the connection closes, simply get all data out
     */
    @OnClose
    public void onClose() {
        try {
            story.close();
        } catch (IOException ex) {
            Logger.getLogger(SimulationWebSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        simulationService.saveModellingData(userID, story.toString());
    }

    /**
     * Get input parameter from message
     *
     * @param message message with input parameters
     */
    private void getInputs(JsonObject message) {

        /* simply go through ordered outputs and get parameters from message */
        int k = 0;
        for (String input : inputNames) {
            inputs[k++] = getDouble(message, input);
        }

        /* get timespan and sampling */
        timespan = getDouble(message, TIMESPAN);
        sampling = getDouble(message, SAMPLING);
    }

    private void doSimulationStep() {

        /* calculate length of the output */
        int len = (int) (timespan / sampling) + 1;

        /* allocate memory */
        for (int i = 0; i < processVariables.length; ++i) {
            processVariables[i] = allocate(processVariables[i], len);
        }

        /* --> if it is NOT the first tick, add sampling */
        nextTickStart = (nextTickStart == 0) ? 0 : nextTickStart + sampling;

        for (int i = 0; i < len; ++i) {

            /* fill in array according to the convention */
            for (int j = 0; j < inputCount; ++j) {
                processVariables[j][i] = inputs[j];               //fill in inpus
            }

            /* evaluate the output on the period of ONE Sampling */
            model.setTimespan(sampling);

            /* evalueate value through sampling period */
            double[] values = model.value(inputs);

            for (int j = 0; j < outputCount; ++j) {
                processVariables[j + inputCount][i] = values[j]; //outputs
            }

            //fill in ticks
            processVariables[ticksInd][i] = (i == 0) ? nextTickStart 
                    : processVariables[ticksInd][i - 1] + sampling;
        }

        /* 
         * nextTickStart = {last finished tick} + samling
         * (samling can be changed, so samling is added above -->)
         */
        nextTickStart = processVariables[ticksInd][len - 1];
    }

    private double[] allocate(double[] array, int len) {
        return (array.length == len) ? array : new double[len];
    }
}
