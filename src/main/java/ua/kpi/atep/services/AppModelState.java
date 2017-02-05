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
    ADMIN_LOGIN,
    LOGIN_SUCCESS,
    REGISTER_SUCCESS,
    LOGIN_FAILURE,
    REGISTER_FAILURE,
    ASSIGNMENT_CREATION_SUCCESS,
    ASSIGNMENT_CREATION_FAILURE,
    STUDENT_ASSIGNMENT_UPDATE_SUCCESS,
    STUDENT_ASSIGNMENT_UPDATE_FAILURE
}
