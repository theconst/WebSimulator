/*
 * AbstractHibernateDAO.java 29.12.2016
 */
package ua.kpi.atep.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.kpi.atep.dao.AbstractDAO;

/**
 * Abstract data access object for hibernate;
 *
 * Provides generic implmentation of CRUD operations
 *
 * Throws hibernate's specific unchecked excpetion
 *
 * Example taken from: https://developer.jboss.org/wiki/GenericDataAccessObjects
 *
 * @param <K> key
 * @param <E> entity
 * @author Kostiantyn Kovalchuk
 */
abstract public class AbstractHibernateDAO<K extends Serializable, E extends Serializable> implements AbstractDAO<K, E> {

    private static final int SECOND_ARG = 1;

    /* is wired from hibernate session factory from the applicationContext.xml file */
    @Autowired
    private SessionFactory sessionFactory;

    private Class<E> persistentClass;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public AbstractHibernateDAO() {

        /*
         * Allows to infer actual type parameters from SUBCLASSED instance
         * trick from https://developer.jboss.org/wiki/GenericDataAccessObjects
         */
        this.persistentClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[SECOND_ARG];
    }

    @Override
    public List<E> findAll() {
        return getCurrentSession().createCriteria(persistentClass).list();
    }

    @Override
    public E find(K key) {
        return (E) getCurrentSession().byId(persistentClass).load(key);
    }

    @Override
    public void delete(K key) {
        E result = find(key);
        getCurrentSession().delete(result);
    }

    @Override
    public void create(E entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(E entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

}
