package ua.kpi.atep.services.serialization.assignment;

import ua.kpi.atep.model.dynamic.items.DynamicItem;

/**
 * 
 * Parser considers Dynamic ites as tokens,
 * Enclosing tags are interpreted as operators (Polish notation)
 * 
 * Span of operator is determined by closing tag
 * 
 * TOKEN ::= OPERATION | dynamic_item
 * OPERATION ::= NEGATIVEFEEDBACK | SEQUENTIALCONNECTION | PARALLELCONNECTION
 * 
 * @author Kovalchuk Kostiantyn
 */
interface Token {

    default boolean isOperand() {
        return !isOperation();
    }

    boolean isOperation();

    DynamicItem value();

    Tags operation();

}
