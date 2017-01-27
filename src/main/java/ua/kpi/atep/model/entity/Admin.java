/*
 * Admin.java
 */
package ua.kpi.atep.model.entity;

import javax.persistence.*;


/**
 *
 * @author Home
 */
@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Admin extends User {
    
    @Column(name = "info")
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMIN;
    }
    
}
