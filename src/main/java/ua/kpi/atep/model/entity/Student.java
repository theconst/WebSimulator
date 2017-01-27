package ua.kpi.atep.model.entity;

import javax.persistence.*;

/*
 * 
 */

/**
 *
 * @author Home
 */
@Entity
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Student extends User {
    
    @Column(name = "studyGroup")
    private String group;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelID")
    private Assignment assignment;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "userID")
    private ModellingData modellingData;
    

    @Override
    public Assignment getAssignment() {
        return assignment;
    }
    
    public void setAssignment(Assignment model) {
        this.assignment = model;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Permission getPermission() {
        return Permission.STUDENT;
    }

    @Override
    public ModellingData getModellingData() {
        return modellingData;
    }

    @Override
    public void setModellingData(ModellingData modellingData) {
        this.modellingData = modellingData;
    }
}
