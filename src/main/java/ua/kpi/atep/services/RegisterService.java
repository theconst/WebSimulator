/*
 * 
 */
package ua.kpi.atep.services;

/**
 *
 * @author Home
 */
public interface RegisterService {
    
    AppModelState register(UserSession session,
            String name, String email, String password, String group);
    
}
