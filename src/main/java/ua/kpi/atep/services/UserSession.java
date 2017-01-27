/*
 * UserSession.java 03.01.2017
 */
package ua.kpi.atep.services;

import java.io.Serializable;
import ua.kpi.atep.model.entity.User;

/**
 *
 * @author Home
 */
public interface UserSession extends Serializable {

    /**
     * Set user
     *
     * @param user user of the session
     */
    void setUser(User user);

    /**
     * Gets the user of the current session
     *
     * @return user owning the session
     */
    User getUser();
    
    
    
}
