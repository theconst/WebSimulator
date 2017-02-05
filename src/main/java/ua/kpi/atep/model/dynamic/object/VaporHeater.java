/*
 * VaporHeater.java
 */
package ua.kpi.atep.model.dynamic.object;

import static ua.kpi.atep.model.dynamic.object.VaporHeater.Inputs.VALVE_POS_1;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Inputs.VALVE_POS_2;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Inputs.VALVE_POS_3;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_1;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_2;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_3;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_4;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_5;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.PARAM_6;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Outputs.indexOf;
import static ua.kpi.atep.model.dynamic.object.VaporHeater.Inputs.indexOf;
import ua.kpi.atep.model.dynamic.items.DynamicItem;
import ua.kpi.atep.model.dynamic.items.DynamicItemFactory;
import ua.kpi.atep.model.dynamic.items.DynamicItemUtils;

/**
 *
 * @author Home
 */
public class VaporHeater extends DynamicModel {
    
    public static final long serialVersionUID = 129L;

    

    //TODO: parameters go here
    public static enum Parameters {

        ;

        private static String[] names() {
            return null;
        }

    }

    public static enum Inputs {
        VALVE_POS_1("input1"),
        VALVE_POS_2("input2"),
        VALVE_POS_3("input3");

        private String value;

        private Inputs(String value) {
            this.value = value;
        }

        public static Inputs fromString(String value) {
            for (Inputs i : Inputs.values()) {
                if (i.getValue().equalsIgnoreCase(value)) {
                    return i;
                }
            }
            return null;
        }

        public static String[] names() {
            Inputs[] values = Inputs.values();
            String[] result = new String[values.length];

            for (int i = 0; i < result.length; i++) {
                result[i] = values[i].toString();
            }

            return result;
        }

        public static int indexOf(Inputs i) {
            return i.ordinal();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    public enum Outputs {
        PARAM_1("param1"),
        PARAM_2("param2"),
        PARAM_3("param3"),
        PARAM_4("param4"),
        PARAM_5("param5"),
        PARAM_6("param6");

        private String value;

        private Outputs(String value) {
            this.value = value;
        }

        public static String[] names() {
            Outputs[] values = Outputs.values();
            String[] result = new String[values.length];

            for (int i = 0; i < result.length; i++) {
                result[i] = values[i].toString();
            }

            return result;
        }

        public static Outputs fromString(String value) {
            for (Outputs o : Outputs.values()) {
                if (o.getValue().equalsIgnoreCase(value)) {
                    return o;
                }
            }
            return null;
        }

        public static int indexOf(Outputs o) {
            return o.ordinal();
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    //items of the 
    private final DynamicItem temp1;
    private final DynamicItem temp2;
    private final DynamicItem temp3;
    private final DynamicItem temp4;
    private final DynamicItem temp5;
    private final DynamicItem temp6;

    public VaporHeater(double sampling) {
        super(Inputs.values().length, Outputs.values().length);

        /* Creating the dynamic items of the */
        DynamicItemFactory factory = new DynamicItemFactory(sampling);
        temp1 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(30, 100),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        temp2 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(50, 50),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        temp3 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(20, 200),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        temp4 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(20, 200),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        temp5 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(20, 200),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        temp6 = DynamicItemUtils.sequentialConnection(
                factory.createFirstOrderLag(20, 200),
                factory.createDelay(4),
                factory.createMeasurmentNoise(1));

        /* creating the dynamic items */
        setTransferFunction(temp1, indexOf(VALVE_POS_1), indexOf(PARAM_1));
        setTransferFunction(temp2, VALVE_POS_2.ordinal(), PARAM_2.ordinal());
        setTransferFunction(temp4, VALVE_POS_1.ordinal(), PARAM_3.ordinal());
        setTransferFunction(temp3, VALVE_POS_2.ordinal(), PARAM_4.ordinal());
        setTransferFunction(temp5, VALVE_POS_1.ordinal(), PARAM_5.ordinal());
        setTransferFunction(temp6, VALVE_POS_3.ordinal(), PARAM_6.ordinal());
    }

    @Override
    public int inputToIndex(String inputName) {
        return Inputs.indexOf(Inputs.fromString(inputName));
    }

    @Override
    public int outputToIndex(String outputName) {
        return Outputs.indexOf(Outputs.fromString(outputName));
    }

    @Override
    public String[] getOutputs() {
        return Outputs.names();
    }

    @Override
    public String[] getInputs() {
        return Inputs.names();
    }

    @Override
    public String[] getParameters() {
        return Parameters.names();
    }
}
