package ua.kpi.atep.services.serialization.assignment;

/* TagAttributes of assignment and its elements */
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
    P,                          //!
    I,                          //!
    D,                          //!
    INPUT, OUTPUT, FUNCTION;

    public static TagAttributes fromName(String name) {
        return TagAttributes.valueOf(name.toUpperCase());
    }
}
