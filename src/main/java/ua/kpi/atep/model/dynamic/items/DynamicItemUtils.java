package ua.kpi.atep.model.dynamic.items;

//! untested
/**
 * Contains various methods for creating typical dynamic items from existing
 * ones
 *
 * @author Kostiantyn Kovalhuk
 */
public class DynamicItemUtils {

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

            private double prev = 0;											//! zero initial conditions

            @Override
            public double handleValue(double in) {
                double error = in - prev; 										//add negative feedback
                double result = item.handleValue(error);

                prev = result;													//remember the previous state
                return result;
            }

        };
    }

    /* checks the correctness of the arguments */
    private static void checkItems(DynamicItem... items) {

        for (DynamicItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("items should not be null");
            }
        }

        if (items.length < 2) {
            throw new IllegalArgumentException("Specify more than two items");
        }

        double samplingTime = items[0].getSamplingTime();

        for (DynamicItem item : items) {
            if (item.getSamplingTime() != samplingTime) {
                throw new IllegalArgumentException("Sampling times must be equal");
            }
        }
    }
}
