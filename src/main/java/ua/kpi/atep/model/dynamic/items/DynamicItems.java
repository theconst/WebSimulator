package ua.kpi.atep.model.dynamic.items;

/**
 * Contains various methods for creating typical dynamic items from existing
 * ones
 *
 * Caution ! shallow copy for now
 *
 * Rarely are they used
 *
 * @author Kostiantyn Kovalhuk
 */
public class DynamicItems {

    /* Error messages */
    private static final String SAMPLING_TIMES_MUST_BE_EQUAL = "Sampling times must be equal";
    private static final String SPECIFY_MORE_THAN_ONE_ITEM = "Specify more than one item";
    private static final String ITEMS_SHOULD_NOT_BE_NULL = "Items should not be null";

    //TODO overload the common case of two items in case of slowdown
    public static DynamicItem parallelSumConnection(DynamicItem... items) {
        checkItems(items);

        double samplingTime = items[0].getSamplingTime();
        return new DynamicItem(samplingTime) {

            @Override
            public double handleValue(double in) {
                double sum = 0;

                for (DynamicItem item : items) {
                    sum += item.handleValue(in);
                }

                return sum;
            }
        };
    }

    public static DynamicItem sequentialConnection(DynamicItem... items) {
        checkItems(items);

        double samplingTime = items[0].getSamplingTime();

        double initialCondition = items[0].getInitialCondition();

        /* set initial condition as for the first item in chain */
        for (DynamicItem item : items) {
            item.setInitialCondition(initialCondition);
        }

        return new DynamicItem(samplingTime) {

            @Override
            public double handleValue(double in) {
                double nextInput = in;
                for (int i = 0; i < items.length - 1; ++i) {
                    nextInput = items[i].handleValue(nextInput);
                }

                return items[items.length - 1].handleValue(nextInput);
            }

        };
    }

    public static DynamicItem negativeFeedbackConnection(DynamicItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item shouldn't be null");
        }

        return new DynamicItem(item.getSamplingTime()) {

            private double prev = 0;    //! zero initial conditions

            @Override
            public double handleValue(double in) {
                double error = in - prev;                   //add negative feedback
                double result = item.handleValue(error);

                prev = result;                      //remember the previous state
                return result;
            }

        };
    }

    /* checks the correctness of the arguments */
    private static void checkItems(DynamicItem... items) {

        for (DynamicItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException(ITEMS_SHOULD_NOT_BE_NULL);
            }
        }

        if (items.length < 1) {
            throw new IllegalArgumentException(SPECIFY_MORE_THAN_ONE_ITEM);
        }

        double samplingTime = items[0].getSamplingTime();

        for (DynamicItem item : items) {
            if (item.getSamplingTime() != samplingTime) {
                throw new IllegalArgumentException(SAMPLING_TIMES_MUST_BE_EQUAL);
            }
        }
    }

}
