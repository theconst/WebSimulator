/*
 * SimulationServiceImpl.java
 */
package ua.kpi.atep.services.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.atep.dao.ModellingDataDAO;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.model.entity.EntityFactory;
import ua.kpi.atep.model.entity.ModellingData;
import ua.kpi.atep.model.entity.User;
import ua.kpi.atep.services.AppModelState;
import ua.kpi.atep.services.SimulationService;
import ua.kpi.atep.services.UserSession;


/**
 *
 * @author Home
 */
@Service
public class SimulationServiceImpl implements SimulationService {
    
    //Since file is created externally the service defines content type

    /**
     * message to logger with placeholder for user name
     */
    private static final String USER_STARTED_MESSAGE
            = "User {0} started simulation";

    private static final String SIMULATION_ERROR_MESSAGE
            = "Anatorized user could not start simulation";

    private static final String CONTENT_TYPE = "text/csv";
    
    private static final Logger logger = Logger.getLogger(SimulationServiceImpl.class.getName());

    @Autowired
    private ModellingDataDAO dataDAO;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private EntityFactory factory;

    private boolean isAutorized(UserSession session) {
        return (session.getUser() != null)
                && (session.getUser().getAssignment() != null);
    }
    
    @Override
    public Assignment getAssignmentById(int studentId) {
        User student = userDAO.find(studentId);
        
        return student.getAssignment();
    }

    @Override
    public AppModelState initSimulation(UserSession session) {
        if (!isAutorized(session)) {
            return AppModelState.UNATORIZED_ACCESS;
        }

        logger.log(Level.INFO, USER_STARTED_MESSAGE, session.getUser().getLogin());

        return AppModelState.SIMULATION_START;
    }

    
    /**
     * TODO: store user activity in some internal format,
     * then serialize / deserialize it 
     * @return 
     */
    @Override
    public String getUserActivityContentType() {
        return CONTENT_TYPE;
    }

    @Override
    @Transactional
    public String getUserActivity(int userID) {
        ModellingData story = dataDAO.getByUserId(userID);
        
        return story.getData();
    }

    
    @Override
    @Transactional
    public void saveModellingData(int userID, CharSequence userActivity) {
        ModellingData data = dataDAO.getByUserId(userID);
   
        data = (data != null) ? data : factory.createModellingData(); 
        data.setUserID(userID);
        
        data.setData(userActivity.toString());
                
        dataDAO.update(data);
    }
    
    
    
    
}
