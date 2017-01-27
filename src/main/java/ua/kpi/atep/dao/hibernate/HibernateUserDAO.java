/*
 * HibernateUserDAO.java 29.12.2016
 */
package ua.kpi.atep.dao.hibernate;

import org.springframework.stereotype.Repository;
import ua.kpi.atep.dao.UserDAO;
import ua.kpi.atep.model.entity.User;

/**
 * DAO for user (Student or Admin)
 * 
 * @author Kovalchuk Kostiantyn
 */
@Repository
public class HibernateUserDAO extends AbstractHibernateDAO<Integer, User> implements UserDAO {

    @Override
    public User findByLogin(String login)  {
        return (User) getCurrentSession()
                .bySimpleNaturalId(User.class).load(login);
    }
}
