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
