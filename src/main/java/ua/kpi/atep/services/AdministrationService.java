/*
 * AdministrationService.java
 *
 */
package ua.kpi.atep.services;
import java.util.List;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.Student;

/**
 *
 * Actions of the administrator
 * 
 * @author Konstantin Kovalchuk
 */
public interface AdministrationService {
    
    public void createAdminAccount(String login, String password);
    
    
    /**
     * Set assignment
     * 
     * @param session current user session
     * @param variant variant of the student
     * @param login login of the student
     * @return state of the operation
     */
    AppModelState setAssigmnent(UserSession session, int variant, String login);
    
    /**
     * Creates the assignment
     * @param session session of the user
     * @param assigment assignment correspoing to the user
     * @return state of operation
     */
    AppModelState createAssignment(UserSession session, 
            Assignment assigment);
    
    List<Student> getStudentList(UserSession session);
}
