/*
 * 
 */
package ua.kpi.atep.services;

import java.io.InputStream;
import ua.kpi.atep.model.entity.Assignment;

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
