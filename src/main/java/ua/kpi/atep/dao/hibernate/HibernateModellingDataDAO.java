/*
 * ModellingDataDAO.java
 */
package ua.kpi.atep.dao.hibernate;

import org.springframework.stereotype.Service;
import ua.kpi.atep.dao.ModellingDataDAO;
import ua.kpi.atep.model.entity.ModellingData;

/**
 *
 * @author 
 */
@Service
public class HibernateModellingDataDAO extends AbstractHibernateDAO<Integer, ModellingData>
        implements ModellingDataDAO {

    @Override
    public ModellingData getByUserId(int uid) {
        return (ModellingData)getCurrentSession().bySimpleNaturalId(ModellingData.class)
                .load(uid);
    }

    @Override
    public void deleteByUserId(int uid) {
        
    }
    
}
