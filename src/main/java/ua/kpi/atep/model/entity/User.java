/*
 * User.java 10.01.2016
 */
package ua.kpi.atep.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.NaturalId;

/**
 * User of the store
 *
 * @author Kostiantyn Kovalchuk
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Serializable {

    private static final String UNSUPORTED_OPERATION_EXCEPTION
            = "Invalid operation for the user";

    /**
     * Id of the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * Login of the user
     */
    @NaturalId
    @Column(name = "login", unique = true)
    private String login;

    /**
     * Password of the user
     */
    @Column(name = "password")
    private String password;

    /**
     * Get user's id
     *
     * @return id of the user
     */
    public Integer getId() {
        return id;
    }

    /**
     * Assign new value to the user's id
     *
     * @param id new user's id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get user's login
     *
     * @return user's login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Assign new value to the user's login
     *
     * @param login user's login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Get user's password
     *
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Assign new value to the user's password
     *
     * @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get user permission
     *
     * @return
     */
    abstract public Permission getPermission();

    /*
     * Common user actions
     * 
     */
    public Assignment getAssignment() {
        return null;
    }

    public void setAssignment(Assignment assignment) {
        throw new UnsupportedOperationException(UNSUPORTED_OPERATION_EXCEPTION);
    }

    public ModellingData getModellingData() {
        return null;
    }

    public void setModellingData(ModellingData modellingData) {
        throw new UnsupportedOperationException(UNSUPORTED_OPERATION_EXCEPTION);
    }
    
}
