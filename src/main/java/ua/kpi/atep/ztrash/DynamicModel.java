///*
// * DynamicModel.java
// */
//package ua.kpi.atep.ztrash;
//
//import ua.kpi.atep.dynamic.generic.DynamicModelType;
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;
//import ua.kpi.atep.model.dynamic.VaporHeater;
//
///**
// * Represents the dynamic system
// *
// * Each channel is a dynamic item and has its own string id
// *
// * The caller is responsible for storing the date
// *
// * @author Kostiantyn Kovalchuk
// */
//abstract public class DynamicModel implements Serializable {
//
//    private static final long serialVersionUID = 997L;
//
//    private static final String ILLEGAL_TYPE_MESSAGE
//            = "Specify valid dynamic model model";
//
//    private static final String ILLEGAL_OUTPUT_MESSAGE
//            = "Not all output parameters specified";
//
//    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_FORMAT
//            = "Invalid arguments passed to the dynamic model: passed=[%s],%n required=[%s]";
//
//    public static DynamicModel createModel(DynamicModelType type) {
//        switch (type) {
//            case VAPOR_HEATER:
//                return new VaporHeater();
//            default:
//                throw new IllegalArgumentException(ILLEGAL_TYPE_MESSAGE);
//        }
//    }
//   
//    public final Map<String, List<Double>> value(Map<String, Double> input) {
//        if (!isValidInput(input)) {
//            throw new IllegalArgumentException(
//                    String.format(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE_FORMAT, input,
//                            getInputParams())
//            );
//        }
//        Map<String, List<Double>> result = handleInput(input);
//        if (!isValidOutput(result)) {
//            throw new IllegalStateException();
//        }
//        return result;
//    }
//
//    private boolean isValidInput(Map<String, Double> input) {
//        return getInputParams().stream().allMatch(input::containsKey);
//    }
//
//    private boolean isValidOutput(Map<String, List<Double>> output) {
//        return getOutputParams().stream().allMatch(output::containsKey);
//    }
//
//    abstract public List<String> getInputParams();
//
//    abstract public List<String> getOutputParams();
//
//    // template method, specific for each model
//    /**
//     *
//     * @param input
//     * @return
//     */
//    abstract protected Map<String, List<Double>> handleInput(Map<String, Double> input);
//
//}
