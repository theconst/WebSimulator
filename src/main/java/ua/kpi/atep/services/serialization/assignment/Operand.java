/*
 * Operand.java
 */
package ua.kpi.atep.services.serialization.assignment;

import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 * Operand of the dynamic item connection expression
 * 
 * @author Konstantin Kovalchuk
 */
class Operand implements Token {

    private DynamicItem value;

    public Operand(DynamicItem value) {
        this.value = value;
    }

    @Override
    public boolean isOperation() {
        return false;
    }

    @Override
    public DynamicItem value() {
        return this.value;
    }

    @Override
    public Tags operation() {
        throw new UnsupportedOperationException("Not an operation");
    }

}
