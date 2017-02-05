/*
 * AbstractDAO.java 5.01.2016
 */
package ua.kpi.atep.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Provides the common interface for all DAOs.
 * Operations are never committed automatically.
 *
 * @author Kostiantyn Kovalchuk
 * @param <K> KEY of the entity
 * @param <E> Type of the entity
 */
public interface AbstractDAO<K extends Serializable, E extends Serializable> {
    

    /**
     * Find all the entities in the source
     *
     * @return all the entities from the source, null if no such entries
     */
    List<E> findAll();

    /**
     * Finds entity by key
     *
     * @param key key value to find
     * @return entity if it is found, null otherwise
     */
    E find(K key);

    /**
     * Deletes the entity
     *
     * @param key
     */
    void delete(K key);
    /**
     * Template method Creates the corresponding entries in database
     *
     * @param entity entity to create
     */
    void create(E entity);

    /**
     * Updates the entity
     *
     * @param entity entity to update
     */
    void update(E entity);
   
    
}
