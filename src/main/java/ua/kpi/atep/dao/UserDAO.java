/*
 * UserDAO.java
 */
package ua.kpi.atep.dao;

import ua.kpi.atep.model.entity.User;

/**
 *
 * Gets user by login
 * 
 * @author Kovalchuk Kostiantyn
 */
public interface UserDAO extends AbstractDAO<Integer, User> {

    /**
     * Find the user by login
     * 
     * @param login login
     * @return user corresponding to login or null if no such user
     */
    abstract public User findByLogin(String login);
}
