/*
 * UserSessionImpl.java 29.12.2016
 */

package ua.kpi.atep.services.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ua.kpi.atep.model.entity.User;
import ua.kpi.atep.services.UserSession;

/**
 * User session is responsible for handling user's data during session
 *
 * @author Kostiantyn Kovalchuk
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class UserSessionImpl implements UserSession {
    
    /**
     * String attributes for the session
     */
    private Map<String, String> stringAttrs = new HashMap<>();

    /**
     * User associated to the session
     */
    private User user = null;
    
    /**
     * Set user
     * @param user user of the session
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Gets the user of the current session
     *
     * @return user owning the session
     */
    @Override
    public User getUser() {
        return user;
    }

//    /**
//     * Sets value of the message
//     * 
//     * @param key
//     * @param value 
//     */
//    @Override
//    public void setMessage(String key, String value) {
//        stringAttrs.put(key, value);
//    }
//
//    /**
//     * 
//     * Get value of the message
//     * 
//     * @param key
//     * @return 
//     */
//    @Override
//    public String getMessage(String key) {
//        return stringAttrs.get(key);
//    }
    
}
