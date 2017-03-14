/*
 * 
 */
package ua.kpi.atep.services;

import ua.kpi.atep.model.entity.Assignment;

/**
 *
 * @author Home
 */
public interface SimulationService {
    
    AppModelState initSimulation(UserSession session);
    
    String getUserActivityContentType();
    
    String getUserActivity(int userId);
    
    Assignment getAssignmentById(int userId);
    
    void saveModellingData(int userID, CharSequence userActivity);
    
}
