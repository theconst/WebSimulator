/*
 * EntityFactory.java 29.12.2016
 */
package ua.kpi.atep.model.entity;




import org.springframework.stereotype.Component;



/**
 * Entity factory creates entities of the model using default constructors
 *
 * @author Kostiantyn Kovalchuk
 */
@Component
public class EntityFactory {

    private static final String ILLGEGAL_PERMISSION_MESSAGE
            = "Unable to create user from permission ";

    public User createUser(Permission permission) {
        switch (permission) {
            case STUDENT:
                return new Student();
            case ADMIN:
                return new Admin();
            default:
                throw new IllegalArgumentException(ILLGEGAL_PERMISSION_MESSAGE
                        + permission);
        }
    }

    public Assignment createAssignment() {
        return new Assignment();
    }

    public ModellingData createModellingData() {
        return new ModellingData();
    }
}
