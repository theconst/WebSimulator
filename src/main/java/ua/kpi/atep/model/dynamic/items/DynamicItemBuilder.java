package ua.kpi.atep.model.dynamic.items;

/**
 * Builds the dynamic item
 *
 * @author Kostiantyn Kovalchuk
 */
public class DynamicItemBuilder {

    /**
     * Holds the result of the last building operation
     */
    private DynamicItem result;

    /**
     * Constructs the initialized item of the class
     */
    public DynamicItemBuilder() {
        result = null;
    }

    /**
     * Constructs the initialized item of the class
     *
     * @param initial initial value of the builder result
     */
    public DynamicItemBuilder(DynamicItem initial) {
        this.result = initial;
    }

    /**
     * Connect {@value #result} to {@code other} using parallel sum connection
     * type
     *
     * @param other
     * @return 
     */
    public DynamicItemBuilder connectParallely(DynamicItem other) {
        if (result == null) {
            this.result = other;												//default behavior
        } else {
            result = DynamicItemUtils.parallelSumConnection(result, other);
        }
        return this;
    }

    //TODO test the connectParallely method
    /**
     * Connect {@value #result} with {@code other} sequentially
     *
     * @param other
     * @return
     */
    public DynamicItemBuilder connectSequentially(DynamicItem other) {
        if (result == null) {
            result = other; 													//default behavior
        } else {
            result = DynamicItemUtils.sequentialConnection(result, other);
        }
        return this;
    }

    /**
     * Surround current item with negative feedback loop
     *
     * @return
     */
    public DynamicItemBuilder addNegativeFeedBackLoop() {
        result = DynamicItemUtils.negativeFeedbackConnection(result);
        return this;
    }

    /**
     * Get the build instance
     *
     * @return build dynamic item
     */
    public DynamicItem create() {
        if (result == null) {
            throw new IllegalStateException("No building operations were applied!");
        }

        return result;
    }
}
