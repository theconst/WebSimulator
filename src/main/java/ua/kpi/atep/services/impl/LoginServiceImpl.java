/*
 * LoginServiceImpl.java
 */
package ua.kpi.atep.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.atep.services.LoginService;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.model.entity.User;
import ua.kpi.atep.services.AppModelState;
import ua.kpi.atep.services.PasswordHasher;
import ua.kpi.atep.services.UserSession;

/**
 *
 * @author Home
 */
@Service
public class LoginServiceImpl implements LoginService {
   
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private PasswordHasher passwordHasher;   

    @Transactional
    @Override
    public AppModelState login(
            UserSession session, String email, String password) {      
        User user = userDAO.findByLogin(email);
        
        if (user != null && user.getPassword().equals(
                passwordHasher.toHash(password))) {
           session.setUser(user);
           return AppModelState.REGISTER_FAILURE;
        }
        
        return AppModelState.LOGIN_FAILURE;
    }
    
}
