/*
 
 */
package ua.kpi.atep.services;

/**
 * Enum of the application states
 * that specifies the state of the application logic
 * 
 * @author Home
 */
public enum AppModelState {
    SIMULATION_START, 
    SIMULATION_END,
    SIMULATION_NO_ASSIGNMENT,
    UNATORIZED_ACCESS,
    LOGIN_SUCCESS,
    REGISTER_SUCCESS,
    LOGIN_FAILURE,
    REGISTER_FAILURE,
}
