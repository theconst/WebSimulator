/*
 *
 *
 */
package ua.kpi.atep.services;
import java.util.List;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.Student;

/**
 *
 * @author Home
 */
public interface AdministrationService {
    
    AppModelState setAssigmnent(int variant, String login);
    
    AppModelState createAssignment(UserSession session, 
            Assignment assigment);
    
    List<Student> getStudentList(UserSession session);
}
