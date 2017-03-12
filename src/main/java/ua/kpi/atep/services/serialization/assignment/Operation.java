/*
 * Operation.java
 */
package ua.kpi.atep.services.serialization.assignment;

import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 * Operation
 * 
 * @author Konstantin Kovalchuk
 */
class Operation implements Token {

    private Tags operation;

    public Operation(Tags operation) {
        this.operation = operation;
    }

    @Override
    public boolean isOperation() {
        return true;
    }

    @Override
    public DynamicItem value() {
        throw new UnsupportedOperationException("Not an operand");
    }

    @Override
    public Tags operation() {
        return this.operation;
    }

}
