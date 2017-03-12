package ua.kpi.atep.services.serialization.assignment;

/**
 * TagAttributes of assignment and its elements 
 */
enum TagAttributes {
    
    ID, 
    NAME, 
    VARIANT, 
    SAMPLING,
    INITIAL, 
    GAIN, 
    TIMECONSTANT, 
    DELAY, 
    TIME, 
    VARIANCE, 
    P,                          //proporional gain
    I,                          //integral time constant
    D,                          //differential time constant
    INPUT, OUTPUT, FUNCTION,
    MAX, MIN,
    ALARMLOW, ALARMHI, ALARMLOWCRITICAL, ALARMHICRITICAL;

    public static TagAttributes fromName(String name) {
        return TagAttributes.valueOf(name.toUpperCase());
    }
}
