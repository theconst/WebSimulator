/*
 * HibernateAssignmentDAO.java 29.12.2016
 */
package ua.kpi.atep.dao.hibernate;

import org.springframework.stereotype.Repository;
import ua.kpi.atep.model.entity.Assignment;
import ua.kpi.atep.dao.AssignmentDAO;

/**
 * DAO for Assignment entity
 * 
 * @author Kostiantyn Kovalchuk
 */
@Repository
public class HibernateAssignmentDAO extends AbstractHibernateDAO<Integer, Assignment>
        implements AssignmentDAO  {
    // all methods are implemented by @see AbstractHibernateDAO
}
