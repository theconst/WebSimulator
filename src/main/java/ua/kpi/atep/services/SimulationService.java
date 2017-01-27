/*
 * 
 */
package ua.kpi.atep.services;





/**
 *
 * @author Home
 */
public interface SimulationService {
    
    AppModelState initSimulation(UserSession session);
    
    String getUserActivityContentType();
    
    String getUserActivity(int userID);
    
    public void saveModellingData(int userID, CharSequence userActivity);
    
}
