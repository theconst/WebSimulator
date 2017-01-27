/*
 * RegisterServiceImpl.java
 */
package ua.kpi.atep.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.atep.services.RegisterService;
import ua.kpi.atep.dao.AssignmentDAO;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.dynamic.generic.DynamicModel;

import ua.kpi.atep.dynamic.generic.DynamicModelType;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.EntityFactory;
import ua.kpi.atep.model.entity.Permission;
import ua.kpi.atep.model.entity.Student;
import ua.kpi.atep.services.AppModelState;
import ua.kpi.atep.services.PasswordHasher;
import ua.kpi.atep.services.UserSession;

/**
 *
 * Implementation of registration service that register students
 * (admins are not allowed to register through the default interface)
 * 
 * @author Konstantin Kovalchuk
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    
    /*
     * Every student is assigned a default model
    */
    private static final String DEFAULT_TASK_COMMENT = "default task";
    
    @Autowired
    private EntityFactory entityFactory;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private AssignmentDAO assignmentDAO;
    
    @Autowired
    private PasswordHasher passwordHasher;
    
    /**
     * Register the user
     * 
     * @param sesion
     * @param name name of the user
     * @param email email of the user
     * @param password password of the user
     * @param group group of the user
     * @return true if registration successful
     */
    @Transactional
    @Override
    public AppModelState register(UserSession sesion, String name, String email,
            String password, String group) {
        if (userDAO.findByLogin(email) != null) {
            return AppModelState.REGISTER_FAILURE;               //user exists
        }
        
        /* Set properties */
        Student student = (Student) entityFactory.createUser(Permission.STUDENT); 
        student.setName(name);
        student.setLogin(email);
        student.setPassword(passwordHasher.toHash(password));
        student.setGroup(group);
        
        Assignment assignment = entityFactory.createAssignment();
        assignment.setComment(DEFAULT_TASK_COMMENT);
        assignment.setModel(
                
                //TODO: create default model on startup of the web-app
                //in order to avoid creating new instance when the user 
                //registers
                DynamicModel.newInstance(DynamicModelType.VAPOR_HEATER)
        );
        
        /* Persist to database */
        assignmentDAO.update(assignment);
        student.setAssignment(assignment);      
        userDAO.update(student);
        
        return AppModelState.REGISTER_SUCCESS;
    }
}
