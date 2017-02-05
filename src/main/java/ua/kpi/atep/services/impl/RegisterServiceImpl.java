/*
 * RegisterServiceImpl.java
 */
package ua.kpi.atep.services.impl;

import java.util.Arrays;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.atep.services.RegisterService;
import ua.kpi.atep.dao.AssignmentDAO;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.model.dynamic.object.DynamicModel;
import ua.kpi.atep.model.dynamic.object.DynamicModelType;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.EntityFactory;
import ua.kpi.atep.model.entity.Permission;
import ua.kpi.atep.model.entity.Student;
import ua.kpi.atep.services.AppModelState;
import ua.kpi.atep.services.PasswordHasher;
import ua.kpi.atep.services.UserSession;

/**
 *
 * Implementation of registration service that register students (admins are not
 * allowed to register through the default interface)
 *
 * @author Konstantin Kovalchuk
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    /*
     * Every student is assigned a default model
     */
    private static final int DEFAULT_TASK_ID = 0;

    /**
     * Auto generated task
     */
    private static final String DEFAULT_TASK_COMMENT = "Autogenerated task";

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
    @Override
    @Transactional
    public AppModelState register(UserSession sesion, String name, String email,
            String password, String group) {
        if (userDAO.findByLogin(email) != null) {
            return AppModelState.REGISTER_FAILURE;               //user exists
        }
        //register student with default task
        Assignment assignment = assignmentDAO.find(DEFAULT_TASK_ID);
        if (assignment == null) {
            //create default model
            assignment = entityFactory.createAssignment();
            assignment.setId(DEFAULT_TASK_ID);
            assignment.setComment(DEFAULT_TASK_COMMENT);
            assignment.setModel(
                    DynamicModel.newInstance(DynamicModelType.VAPOR_HEATER)
            );
            assignmentDAO.create(assignment);
        }

        /* Set properties */
        Student student = (Student) entityFactory.createUser(Permission.STUDENT);
        student.setName(name);
        student.setLogin(email);
        student.setPassword(passwordHasher.toHash(password));
        student.setGroup(group);
        student.setAssignment(assignment);
        userDAO.create(student);

        return AppModelState.REGISTER_SUCCESS;
    }
    
}
