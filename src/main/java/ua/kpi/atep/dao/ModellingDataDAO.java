/*
 * 
 */
package ua.kpi.atep.dao;

import ua.kpi.atep.model.entity.ModellingData;

/**
 *
 * @author Home
 */
public interface ModellingDataDAO extends AbstractDAO<Integer, ModellingData> {
    ModellingData getByUserId(int uid);
    
    void deleteByUserId(int uid);
}
