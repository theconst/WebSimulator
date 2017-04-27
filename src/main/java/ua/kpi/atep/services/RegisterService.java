/*
 * 
 */
package ua.kpi.atep.services;

/**
 *
 * @author Home
 */
public interface RegisterService {
    
    int DEFAULT_ASSIGMENT_ID = 0;
    
    
    AppModelState register(UserSession session,
            String name, String email, String password, String group);
    
}
