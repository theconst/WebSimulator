/*
 * SimulationWebSocketHander.java
 */
package ua.kpi.atep.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.*;
import ua.kpi.atep.services.serialization.*;
import ua.kpi.atep.model.dynamic.object.*;
import ua.kpi.atep.model.simulation.*;
import ua.kpi.atep.services.*;

/**
 * This is a socket connection that operates with simulation
 * 
 *
 * @author Konstantin Kovalchuk
 */
public class SimulationWebSocketHandler extends TextWebSocketHandler {
    
    private static final String USER_FINISHED_SIMULATION = "User {0} finished simulation";
    
    private static final Logger LOGGER = Logger.getLogger(SimulationWebSocketHandler.class.getName());
    
    //find a less dependent way for accessing the session
    private static final String USER_SESSION = "scopedTarget.userSessionImpl";

    /**
     * holds size of memory for user story recording
     */
    //temporary fix
    @Value("${web.socket.story.size}")
    private int storySize;

    /*
    * Low-level code here purely relies on arrays and ordering of the arguments
    * 
    * 
    * Adding abstractions like HashMap<String, Double> add signinificant overhead
    * because of repeating hashCode() computation
    * 
    * 
    * Streaming to history to the array of bytes also speeds the application up
     */
    /**
     * Id of the user
     */
    private int userID;

    /**
     * Logging implemented here
     *
     */
    private CSVOutputWriter story;
    
    /**
     * Chunk of the story
     */
    private double[][] chunk;

    private UserSession userSession;
    
    /**
     * Service for simulation initialization
     */
    @Autowired
    private SimulationService simulationService;

    @Autowired
    private Serializer<SimulationOutput, String, String> outputSerializer;    
    /**
     * Runner of the simulation
     */
    private Simulation runner;
    
    private DynamicModelInput initial;
    
    /**
     * Runs of after the connection is established
     * @param session
     * @throws Exception 
     */
    private void initialize(WebSocketSession session) throws Exception {
         /* get parameters from http session */
       userSession = (UserSession) session.getAttributes().get(USER_SESSION);


        userID = userSession.getUser().getId();

        DynamicModel model = userSession.getUser().getAssignment().getModel();

        initial = model.getInput();   

        story = initStory(model);

        runner = Simulation.newInstance(model); 
    }

    /**
     * Executes the command given to websocket
     *
     * @param message input to the model
     * @param session current session
     * @throws java.lang.Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        if (runner == null) {
            initialize(session);           //initialize lazily
        }
        
        String text = message.getPayload();
        
        if (text.contains("initial")) {  //send initial parameters
           session.sendMessage(new TextMessage(Json.stringify(initial)));
           return;
        } 
        
        if (text.contains("finish")) {
            simulationService.saveModellingData(userID, story.toString());
            LOGGER.log(Level.INFO, USER_FINISHED_SIMULATION, 
                    userSession.getUser().getLogin());
            session.sendMessage(new TextMessage("{}"));
            story.close();
            return;
        }
        
        UserInput input = Json.parse(message.getPayload(), UserInput.class);
        SimulationOutput output = runner.doSimulationStep(input);

        String outputMessage = outputSerializer.serialize(output);
        session.sendMessage(new TextMessage(outputMessage));
        writeChunk(story, output);
        if (isFull(story)) {
            session.close();
        }    
    }

    /**
     *
     * @param session
     * @param status
     * @throws java.lang.Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus status) throws Exception {
        if (story != null) {
            story.close();
        }
    }
    
    
    /* Helper methods for story initializing */
    private boolean isFull(CSVOutputWriter story) {
        return story.getSize() > storySize - 1;
    }

    private void writeChunk(CSVOutputWriter story, SimulationOutput output) {
        int i = 0;
        for (String header : story.getHeaders()) {
            chunk[i++] = output.getTrend(header);
        }
        story.writeChunk(chunk);
    }

    private CSVOutputWriter initStory(DynamicModel model) {
        //some side effects in the form of a story
        int inputCount = model.getInputsCount();
        int outputCount = model.getOutputsCount();
        int size = inputCount + outputCount + 1;
        String[] headers = new String[size];
        String[] in = model.getInput().getNames();
        String[] out = model.getOutput().getNames();

        System.arraycopy(in, 0, headers, 0, inputCount);
        System.arraycopy(out, 0, headers, inputCount, outputCount);
        headers[headers.length - 1] = SimulationOutput.TICKS;
        chunk = new double[size][];

        return new CSVOutputWriter(storySize, headers);
    }
}
