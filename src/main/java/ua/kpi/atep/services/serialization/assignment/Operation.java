/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.services.serialization.assignment;

import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 *
 * @author Home
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
