package ua.kpi.atep.services.serialization.assignment;

/**
 * Tags used in assignment deserialization
 *
 * @author Home
 */
enum Tags {

    ASSIGNMENT,
    COMMENT,
    MODEL,
    TRANSFERFUNCTIONS,
    TRANSFERFUNCTION,
    FOLPDN,
    LAG,
    DELAY,
    NOISE,
    PID,
    PARALLELCONNECTION,
    SEQUENTIALCONNECTION,
    NEGATIVEFEEDBACK,
    INPUTS,
    INPUT,
    OUTPUTS,
    OUTPUT,
    RELATIONSHIPS,
    RELATIONSHIP;

    public static Tags fromLocalName(String localName) {
        return Tags.valueOf(localName.toUpperCase());
    }
}
