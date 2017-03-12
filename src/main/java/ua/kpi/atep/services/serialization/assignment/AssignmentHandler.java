/*
 * AssignmentHandler.java
 */
package ua.kpi.atep.services.serialization.assignment;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.util.*;
import java.util.logging.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import ua.kpi.atep.model.dynamic.items.*;

import ua.kpi.atep.model.dynamic.object.*;
import ua.kpi.atep.model.entity.Assignment;

/**
 * Handle that parses xml in event-based fashion
 *
 * Low-level desirealizer of the assignment entity
 *
 * Parser will parse virtually anything, so validation is nesessary before
 * proceeding
 * 
 * //TODO: refactor this to multiple handlers:
 *  1. TransferFunctionHandler: getTransferFunctions => Map<String, DynamicItem>
 *  2. InputHandler: getNaming => Map<String, String>, getInput => DynamicModelInput
  3. OutputHandler 
 *
 * @author Konstantin Kovalchuk
 */
public class AssignmentHandler extends DefaultHandler {

//    private static final Logger log
//            = Logger.getLogger(AssignmentHandler.class.getName());

    private static final String TAG_NOT_DEFINED = "Tag not defined";
    
    private static final String ILLEGAL_ARGUMENT_OF_THE_OPERATION 
             = "Illegal argument of the operation";

    /**
     * Parent parser of this handler    //not used yet, but should be as dependency
     */
    private XMLReader parent;

    /**
     * Assignment resulting from the deserialization
     */
    private Assignment assignment;

    /**
     * variant of the assignment
     */
    private int variant;

    /**
     * Comment of the assignment
     */
    private StringBuilder comment = new StringBuilder();
    
    /**
     * input names
     */
    private List<String> inputNames = new LinkedList<>();
    
    
    /**
     * output names
     */
    private List<String> outputNames = new LinkedList<>();

    /**
     * Keep track of minimum values of inputs
     */
    private List<Double> mins = new LinkedList<>();
    
    /**
     * Keep track of maximum values of inputs
     */
    private List<Double> maxs = new LinkedList<>();
    
    /**
     * Keep track of initial values of inputs
     */
    private List<Double> initialInputs = new LinkedList<>();
    
    
    /**
     * Inputs of the model
     */
    private DynamicModelInput modelInput;
    
    /**
     * High alarms for outputs
     */
    private List<Double> alarmHis = new LinkedList<>();
    
    /**
     * Low alarms for inputs
     */
    private List<Double> alarmLows = new LinkedList<>();
    
    /**
     * Critical alarms for outputs
     */
    private List<Double> alarmHiCriticals = new LinkedList<>();
    
    /**
     * Low Alarms for ouputs
     */
    private List<Double> alarmLowCriticals = new LinkedList<>();

    /**
     * Initial values for outputs
     */
    private List<Double> initialOutputs = new LinkedList<>();
    
    /**
     * Outputs of the model
     */
    private DynamicModelOutput modelOutput;

    /**
     * id => name
     */
    private Map<String, String> inputs = new HashMap<>();

    /**
     * id => name
     */
    private Map<String, String> outputs = new HashMap<>();

    /**
     * Model associated to the assignment
     */
    private DynamicModel model;

    /** Data needed for dynamic model creation */
    private DynamicItemFactory itemFactory;

    /**
     * Transfer functions of the model
     */
    private Map<String, DynamicItem> transferFunctions = new HashMap();

    /* contains the computeValue of the current processed element */
    private Tags current;

    /* Keep track of current transfer function */
    private String currentTranferFunctionId;

    /* stack of operations */
    private Deque<Token> stack = new LinkedList<>();

    public AssignmentHandler(XMLReader parent) {
        try {
            //assignment is a POJO, utilize this contract
            this.assignment = Assignment.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(AssignmentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.parent = parent;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {
        current = Tags.fromLocalName(localName);

        /* Get values of the attributes */
        Map<TagAttributes, String> vals
                = getAttributes(new EnumMap<>(TagAttributes.class), attrs);
        switch (current) {
            case ASSIGNMENT:
                variant = parseInt(vals.get(TagAttributes.VARIANT));
                assignment.setId(variant);
                break;
            case COMMENT:
                //do nothing, comment tag needs to be processed as text
                break;
            case MODEL:

                //create factory of items 
                double sampling = parseDouble(vals.get(TagAttributes.SAMPLING));
                itemFactory = DynamicItemFactory.newInstance(sampling);
                break;
            case TRANSFERFUNCTIONS:
                //do nothing, it is just a marker fo user and validation
                break;

            /* Push operation onto the stack */
            case TRANSFERFUNCTION:
                currentTranferFunctionId = vals.get(TagAttributes.ID);
            //!intentionally ommited break;
            case SEQUENTIALCONNECTION:
            case PARALLELCONNECTION:
            case NEGATIVEFEEDBACK:
                //be aware of ommited break
                stack.push(new Operation(current));
                break;
            /* ************************************* */
     
            case DELAY:    // Push operand onto the stack
            case LAG:
            case NOISE:
            case PID:
            case FOLPDN:
                stack.push(new Operand(createItem(current, vals)));
                break;
            case INPUTS:
                //do nothing
                break;
                
            /* Add attributes to inputs */
            case INPUT:
                String inputName = vals.get(TagAttributes.NAME);
                
                inputNames.add(inputName);
                inputs.put(vals.get(TagAttributes.ID), inputName);
                initialInputs.add(parseDouble(vals.get(TagAttributes.INITIAL)));
                maxs.add(parseDouble(vals.get(TagAttributes.MAX)));
                mins.add(parseDouble(vals.get(TagAttributes.MIN)));
                break;
            case OUTPUTS:
                //do nothing
                break;
                
            /*
             * Add attributes to outpur
             */
            case OUTPUT:
                String outputName = vals.get(TagAttributes.NAME);
                
                outputNames.add(outputName);
                outputs.put(vals.get(TagAttributes.ID), outputName);
                
                /* default value of output is zero */
                initialOutputs.add(getOrZero(vals, TagAttributes.INITIAL));
                alarmHis.add(parseDouble(vals.get(TagAttributes.ALARMHI)));
                alarmLows.add(parseDouble(vals.get(TagAttributes.ALARMLOW)));
                alarmHiCriticals.add(parseDouble(
                        vals.get(TagAttributes.ALARMHICRITICAL)));
                alarmLowCriticals.add(parseDouble(
                        vals.get(TagAttributes.ALARMLOWCRITICAL)));
                break;
            case RELATIONSHIPS:
                model = createModel(modelInput, modelOutput);
                break;
            case RELATIONSHIP:
                model = addRelationship(model, vals, transferFunctions,
                        inputs, outputs);
                break;
            default:
                throw new SAXException(TAG_NOT_DEFINED);
        }
    }

    //handles values for internal tags
    @Override
    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length).trim();
        switch (current) {
            case COMMENT:
                //characters may be called more than once for comment
                //certainly comments may not be too large
                comment.append(value);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        current = Tags.fromLocalName(localName);
        switch (current) {
            /* transfer function is used as bottom marker */
            case TRANSFERFUNCTION:
                /* pop computeValue off the stack and assign
                   to currently process transfer function */
                Token result = stack.pop();

                if (result.isOperation()) {
                    throw new SAXException(ILLEGAL_ARGUMENT_OF_THE_OPERATION);
                }
                transferFunctions.put(currentTranferFunctionId, result.value());

                //remove bottom marker
                stack.pop();
                break;

            case SEQUENTIALCONNECTION:
            case PARALLELCONNECTION:
            case NEGATIVEFEEDBACK:
                /* fetch all operands */
                Deque<DynamicItem> operands = new LinkedList<>();

                Token top = stack.pop();
                while (top.isOperand()) {
                    operands.push(top.value());
                    top = stack.pop();
                }

                /* perform operation*/
                DynamicItem operationResult = connect(current,
                        operands.toArray(new DynamicItem[operands.size()]));

                /* push result back as new expression */
                stack.push(new Operand(operationResult));
                break;
            case INPUTS:
                modelInput = new DynamicModelInput(toStringArray(inputNames), 
                        toDoubleArray(initialInputs));
                modelInput.setMax(toDoubleArray(maxs));
                modelInput.setMin(toDoubleArray(mins));
                break;
            case OUTPUTS:
                modelOutput = new DynamicModelOutput(toStringArray(outputNames),
                    toDoubleArray(initialOutputs));
                modelOutput.setAlarmHi(toDoubleArray(alarmHis));
                modelOutput.setAlarmLow(toDoubleArray(alarmLows));
                modelOutput.setAlarmHiCritical(toDoubleArray(alarmHiCriticals));
                modelOutput.setAlarmLowCritical(toDoubleArray(alarmLowCriticals));
                break;
            default:
            //do nothing for other items
        }
    }

    @Override
    public void endDocument() {

        //initialize assignment
        assignment.setComment(comment.toString());
        assignment.setModel(model);
        assignment.setId(variant);
    }

    /**
     * Add relationship to the model
     *
     * @param model dynamic model to modify (will be changed)
     * @param values values of the attributes
     * @param transferFunctions transfer functions
     * @param inputs id => name
     * @param outputs id => name
     * @return modified dynamic model
     */
    private DynamicModel addRelationship(
            DynamicModel model,
            Map<TagAttributes, String> values,
            Map<String, DynamicItem> transferFunctions,
            Map<String, String> inputs,
            Map<String, String> outputs) {
        String functionId = values.get(TagAttributes.FUNCTION);
        String inputId = values.get(TagAttributes.INPUT);
        String outputId = values.get(TagAttributes.OUTPUT);

        model.setTransferFunction(inputs.get(inputId),
                transferFunctions.get(functionId),
                outputs.get(outputId));

        return model;
    }

    /**
     * Produce map of attributes out of the values
     *
     */
    private Map<TagAttributes, String> getAttributes(
            Map<TagAttributes, String> map,
            Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); ++i) {
            TagAttributes attr = TagAttributes.fromName(attributes.getLocalName(i));
            map.put(attr, attributes.getValue(i));
        }
        return map;
    }

    /**
     * Creates model with inputs
     *
     * @param inputs model inputs
     * @param outputs model outputs
     * @return resulting model
     */
    private DynamicModel createModel(DynamicModelInput modelInput, DynamicModelOutput modelOutput) {
        DynamicModel result = DynamicModel.newInstance(modelInput,
                modelOutput);
        return result;
    }

    /**
     * Utility method for connectiong items
     *
     * @param connectionType type of the connection
     * @param items type of the item
     * @return resulting dynamic item
     * @throws SAXException if illegal arguments are passed
     */
    private DynamicItem connect(Tags connectionType, DynamicItem... items)
            throws SAXException {
        switch (connectionType) {
            case SEQUENTIALCONNECTION:
                return DynamicItems.sequentialConnection(items);
            case PARALLELCONNECTION:
                return DynamicItems.parallelSumConnection(items);
            case NEGATIVEFEEDBACK:
                if (items.length > 1) {
                    DynamicItem seq = DynamicItems.sequentialConnection(items);
                    return DynamicItems.negativeFeedbackConnection(seq);
                }
                return DynamicItems.negativeFeedbackConnection(items[0]);
        }
        throw new SAXException(TAG_NOT_DEFINED);
    }

    /**
     * Create dynamic item according to the type and attributes values
     *
     * @param type type of the item
     * @param values computeValue of the item
     * @return resulting dynamic item
     * @throws SAXException sax parser exception
     */
    private DynamicItem createItem(Tags type, Map<TagAttributes, String> values)
            throws SAXException {

        /* some generic attributes for all items */
        double initial = getOrZero(values, TagAttributes.INITIAL);
        double gain;
        double timeConstant;
        double delay;
        double variance;

        DynamicItem result = null;
        switch (type) {
            case FOLPDN:
                gain = getOrZero(values, TagAttributes.GAIN);
                timeConstant = getOrZero(values, TagAttributes.TIMECONSTANT);
                delay = getOrZero(values, TagAttributes.DELAY);
                variance = getOrZero(values, TagAttributes.VARIANCE);
                result = createFolpdn(initial, gain, timeConstant, delay,
                        variance);
                break;
            case PID:
                double p = getOrZero(values, TagAttributes.P);
                double i = getOrZero(values, TagAttributes.I);
                double d = getOrZero(values, TagAttributes.D);
                result = itemFactory.createPID(p, i, d);
                break;
            case DELAY:
                double time = getOrZero(values, TagAttributes.TIME);
                result = itemFactory.createDelay(time);
                break;
            case LAG:
                gain = getOrZero(values, TagAttributes.GAIN);
                timeConstant = getOrZero(values, TagAttributes.TIMECONSTANT);
                result = itemFactory.createFirstOrderLag(gain, timeConstant);
                break;
            case NOISE:
                variance = getOrZero(values, TagAttributes.VARIANCE);
                result = itemFactory.createMeasurmentNoise(variance);
                break;
            default:
                throw new SAXException(TAG_NOT_DEFINED);
        }
        
        //ide warning useless, because exception will be thrown
        result.setInitialCondition(initial);

        return result;
    }
    
    /* Helper methods */

    private double getOrZero(Map<TagAttributes, String> vals, TagAttributes attr) {
        return parseDouble(vals.getOrDefault(attr, "0"));
    }

    private DynamicItem createFolpdn(double initial, double gain,
            double timeConstant, double delay, double noise) {
        DynamicItem item = DynamicItems.sequentialConnection(
                itemFactory.createFirstOrderLag(gain, timeConstant),
                itemFactory.createDelay(delay),
                itemFactory.createMeasurmentNoise(noise));
        item.setInitialCondition(initial);

        return item;
    }
    
    private String[] toStringArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }
    
    private double[] toDoubleArray(List<Double> list) {
        
        //the most foolish line in project
        return list.stream().mapToDouble(d -> (double) d).toArray();
    }
}
