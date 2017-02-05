/*
 * Serializator for the model
 */
package ua.kpi.atep.services.serialization;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import ua.kpi.atep.model.dynamic.items.DynamicItem;
import ua.kpi.atep.model.dynamic.items.DynamicItemBuilder;
import ua.kpi.atep.model.dynamic.items.DynamicItemFactory;
import ua.kpi.atep.model.dynamic.object.DynamicModel;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.EntityFactory;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 *
 * @author Home
 */
@Service
public class XMLAssignmentSerializer
        implements Serializer<Assignment, Reader, Writer> {

    @Autowired
    private EntityFactory entityFactory;

    @Override
    public Assignment deserialize(Reader xml) throws SerializationException {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            AssignmentHandler handler = new AssignmentHandler();

            reader.setContentHandler(handler);
            reader.parse(new InputSource(xml));

            return handler.getAssignment();

        } catch (SAXException | IOException ex) {
            throw new SerializationException(ex);
        }
    }

    private enum Tags {

        ASSIGNMENT,
        COMMENT,
        MODEL,
        TRANSFERFUNCTIONS, TRANSFERFUNCTION, FOLPDN, LAG, DELAY, NOISE, PID,
        PARALLELCONNECTION, SEQUENTIALCONNECTION, NEGATIVEFEEDBACK,
        INPUTS, INPUT,
        OUTPUTS, OUTPUT,
        RELATIONSHIPS, RELATIONSHIP;

        public static Tags fromLocalName(String localName) {
            return Tags.valueOf(localName.toUpperCase());
        }
    }

    private enum Attrs {
        ID, NAME, VARIANT, SAMPLING,
        INITIAL, GAIN, TIMECONSTANT, DELAY, VARIANCE,
        INPUT, OUTPUT, FUNCTION;

        public static Attrs fromName(String name) {
            return Attrs.valueOf(name.toUpperCase());
        }
    }

    /* Parsing XML via event-based handler, this should later go to the dao layer */
    private class AssignmentHandler extends DefaultHandler {

        private final Assignment assignment = entityFactory.createAssignment();

        DynamicModel model;

        private int variant;

        private StringBuilder comment = new StringBuilder();

        /* data needed for dynamic model creation */
        DynamicItemFactory factory;

        DynamicItemBuilder builder = new DynamicItemBuilder();

        private double sampling;

        Map<String, String> inputs = new HashMap();

        Map<String, String> outputs = new HashMap();

        Map<String, DynamicItem> transferFunctions = new HashMap();

        /* Keep track of current transfer function */
        private String currentTranferFuntionId;

//        private String currentTranferFunction;
        // contains the value of the current processed element
        private Tags current;

        @Override
        public void startElement(String uri,
                String localName,
                String qName,
                Attributes attrs) throws SAXException {

            current = Tags.fromLocalName(localName);                //track of the current tag
            Map<Attrs, String> vals
                    = getAttributes(new EnumMap<>(Attrs.class), attrs);
            switch (current) {
                case ASSIGNMENT:
                    variant = parseInt(vals.get(Attrs.VARIANT));
                    assignment.setId(variant);
                    break;
                case COMMENT:
                    break;
                case MODEL:
                    sampling = parseDouble(vals.get(Attrs.SAMPLING));
                    factory = DynamicItemFactory.newInstance(sampling);
                    break;
                case TRANSFERFUNCTIONS:

                    break;

                case TRANSFERFUNCTION:
                    currentTranferFuntionId = vals.get(Attrs.ID);
                    break;

                case SEQUENTIALCONNECTION:
                case PARALLELCONNECTION:
                case NEGATIVEFEEDBACK:
                case DELAY:
                case LAG:
                case NOISE:
                case PID:
                    throw new UnsupportedOperationException("use folpdn only");
//                    break;
                case FOLPDN:

                    //stub for now
                    transferFunctions.put(currentTranferFuntionId,
                            createTransferFunction(vals));
                    break;
                case INPUTS:
                    break;
                case INPUT:
                    inputs.put(vals.get(Attrs.ID), vals.get(Attrs.NAME));
                    break;
                case OUTPUTS:
                    break;
                case OUTPUT:
                    outputs.put(vals.get(Attrs.ID), vals.get(Attrs.NAME));
                    break;
                case RELATIONSHIPS:
                    model = createModel(inputs.values(), outputs.values());
                    break;
                case RELATIONSHIP:
                    model = createRelationship(model, vals);
                    break;
                default:
                    throw new SAXException("Couldn't parse");
            }
        }

        private DynamicModel createRelationship(DynamicModel model,
                Map<Attrs, String> vals) {
            String functionId = vals.get(Attrs.FUNCTION);
            String inputId = vals.get(Attrs.INPUT);
            String outputId = vals.get(Attrs.OUTPUT);

            model.setTransferFunction(
                    transferFunctions.get(functionId),
                    model.inputToIndex(inputs.get(inputId)),
                    model.outputToIndex(outputs.get(outputId)));
            return model;
        }

        /**
         * Record current values of attributes to map
         *
         *
         * @param attrs
         */
        private Map<Attrs, String> getAttributes(Map<Attrs, String> map,
                Attributes attributes) {
            for (int i = 0; i < attributes.getLength(); ++i) {
                Attrs attr = Attrs.fromName(attributes.getLocalName(i));
                map.put(attr, attributes.getValue(i));
            }
            return map;
        }

        private DynamicModel createModel(Collection<String> inputs,
                Collection<String> outputs) {
            //initialize model
            int inputsCount = inputs.size();
            int outputsCount = outputs.size();
            DynamicModel result = DynamicModel.newInstance(inputsCount,
                    outputsCount);
            result.setInputs(inputs.toArray(new String[inputsCount]));
            result.setOutputs(outputs.toArray(new String[outputsCount]));
            result.setParameters(new String[0]);

            return result;
        }

        private DynamicItem createTransferFunction(Map<Attrs, String> vals) {
            double gain = parseDouble(vals.get(Attrs.GAIN));
            double timeConstant = parseDouble(vals.get(Attrs.TIMECONSTANT));
            double delay = parseDouble(vals.get(Attrs.DELAY));
            double variance = parseDouble(vals.get(Attrs.VARIANCE));
            double initial = parseDouble(vals.get(Attrs.INITIAL));

            return createFolpdn(initial, gain, timeConstant, delay, variance);
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

        //build entity on the end of parsing
        @Override
        public void endDocument() {

            //initialize assignment
            assignment.setComment(comment.toString());
            assignment.setModel(model);
            assignment.setId(variant);
        }

        public Assignment getAssignment() {
            return assignment;
        }

        private DynamicItem createFolpdn(double initial, double lag,
                double timeConstant, double delay, double noise) {
            DynamicItem item = builder.connectSequentially(
                    factory.createFirstOrderLag(lag, timeConstant))
                    .connectSequentially(factory.createDelay(delay))
                    .connectSequentially(factory.createMeasurmentNoise(noise))
                    .create();
            item.setInitialCondition(initial);

            return item;
        }
    }

}
