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
    private static final String CLONE_CONTRACT_VIOLATION = "Clone contract violated";

    
    public static DynamicItem parallelSumConnection(DynamicItem... items) {
        checkItems(items);

        double samplingTime = items[0].getSamplingTime();
        
        
        double acc = 0.0;
        /* initial is the sum of all connections */
        for (DynamicItem item : items) {
            acc += item.getInitialCondition();
        }
        
        double initial = acc;
        DynamicItem result = new DynamicItem(samplingTime) {
            
            {
                 this.initialCondition = initial;
            }

            @Override
            public double handleValue(double in) {
                double sum = 0;

                for (DynamicItem item : items) {
                    sum += item.handleValue(in);
                }

                return sum;
            }
            
            @Override
            public void setInitialCondition(double value) {
                //determined by items
                throw new IllegalStateException(
                        "Parallel connection is determined by items");
            }
        };
        
        return result;
    }

    public static DynamicItem sequentialConnection(DynamicItem... items) {
        checkItems(items);

        double samplingTime = items[0].getSamplingTime();

        /* initial condition is the last item's condition */
        double initial = items[items.length - 1].getInitialCondition();
        
        //set equal initial condtion fo all items
        for (DynamicItem item : items) {
            item.setInitialCondition(initial);
        }

        DynamicItem result = new DynamicItem(samplingTime) {
            
            {   //set initial condition
                this.initialCondition = initial;
            }

            @Override
            public double handleValue(double in) {
                double nextInput = in;
                for (int i = 0; i < items.length - 1; ++i) {
                    nextInput = items[i].handleValue(nextInput);
                }

                return items[items.length - 1].handleValue(nextInput);
            }
            
            @Override
            public void setInitialCondition(double initial) {
                this.initialCondition = initial;
                for (DynamicItem item : items) {
                    item.setInitialCondition(initialCondition);
                } 
            }
            
        };
        
        return result;
    }

    public static DynamicItem negativeFeedbackConnection(DynamicItem item) {
        if (item == null) {
            throw new IllegalArgumentException(ITEMS_SHOULD_NOT_BE_NULL);
        }

        DynamicItem result = new DynamicItem(item.getSamplingTime()) {

            private double prev = item.getInitialCondition();

            @Override
            public double handleValue(double in) {
                double error = in - prev;                //add negative feedback
                double result = item.handleValue(error);

                prev = result;                           //remember the previous state
                return result;
            }
            
             @Override
            public void setInitialCondition(double value) {
                //determined by items
                throw new IllegalStateException(
                        "Negative feedback connection is determined by items");
            }
            
            
        }; 
        
        return result;
    }
    
    /**
     * Deep copy of the current dynamic item
     * 
     * @param item item to be copied
     * @return dynamic item to be copied
     */
    public static DynamicItem deepCopyOf(DynamicItem item) {
        try {
            return item.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(CLONE_CONTRACT_VIOLATION, ex);
        }
    }

    public static DynamicItem[] deepCopyOf(DynamicItem[] items) {
        DynamicItem[] result = new DynamicItem[items.length];
        
        for (int i = 0; i < items.length; ++i) {
            result[i] = deepCopyOf(items[i]);
        }
        
        return result;
    }
    
    /**
     * Checks the correctness of the arguments 
     */
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
