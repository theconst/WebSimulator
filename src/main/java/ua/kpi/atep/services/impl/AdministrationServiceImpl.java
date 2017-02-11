/*
 * AdministratioServiceImpl.java 05.02.2017
 */
package ua.kpi.atep.services.impl;

import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.atep.dao.AssignmentDAO;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.EntityFactory;
import ua.kpi.atep.model.entity.Permission;
import ua.kpi.atep.model.entity.Student;
import ua.kpi.atep.model.entity.User;

import ua.kpi.atep.services.AdministrationService;
import ua.kpi.atep.services.AppModelState;
import ua.kpi.atep.services.PasswordHasher;
import ua.kpi.atep.services.UserSession;

/**
 * Actions of the administrator
 *
 * @author Konstantin Kovalchuk
 */
@Service
public class AdministrationServiceImpl implements AdministrationService {

    @Autowired
    private AssignmentDAO assignmentDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EntityFactory entityFactory;

    @Autowired
    private PasswordHasher hasher;

    @Override
    @Transactional
    public void initAdministration(String adminAccount, String adminPassword) {
        //todo rethink as one service with register and login
        User user = entityFactory.createUser(Permission.ADMIN);
        user.setPassword(hasher.toHash(adminPassword));
        user.setLogin(adminAccount);
        userDAO.update(user);
    }

    @Override
    @Transactional
    public AppModelState setAssigmnent(UserSession session, int variant, 
            String login) {
        if (isAdmin(session.getUser())) {
            User user = userDAO.findByLogin(login);
            if (isStudent(user)) {
                Assignment assignment = assignmentDAO.find(variant);

                if (assignment != null) {
                    user.setAssignment(assignment);
                    userDAO.update(user);
                    return AppModelState.STUDENT_ASSIGNMENT_UPDATE_SUCCESS;
                }
            }
        }
        return AppModelState.STUDENT_ASSIGNMENT_UPDATE_FAILURE;
    }

    /**
     * Creates assignment from xml file
     *
     * @param session
     * @param assignment
     * @return
     */
    @Override
    @Transactional
    public AppModelState createAssignment(UserSession session,
            Assignment assignment) {
        if (isAdmin(session.getUser())) {
            assignmentDAO.update(assignment);
            return AppModelState.ASSIGNMENT_CREATION_SUCCESS;
        }
        return AppModelState.ASSIGNMENT_CREATION_FAILURE;
    }

    /**
     * Lists the students
     *
     * @param session session of the user
     * @return serialized version of student list
     */
    @Override
    @Transactional
    public List<Student> getStudentList(UserSession session) {

        if (!isAdmin(session.getUser())) {
            Collections.emptyList();
        }

        //TODO: add paging
        // assumming collection is not too large <= 100
        return userDAO.findAll().stream().filter(this::isStudent)
                .map(x -> (Student) x)
                .collect(Collectors.toList());
    }

    private boolean isAdmin(User u) {
        if (u == null) {
            return false;
        }
        return u.getPermission() == Permission.ADMIN;
    }

    private boolean isStudent(User u) {
        if (u == null) {
            return false;
        }
        return u.getPermission() == Permission.STUDENT;
    }

}
