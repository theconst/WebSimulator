/*
 * 
 */
package ua.kpi.atep.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

/**
 * 
 * Data of the user modelling
 * 
 * 
 * @author 
 */
@Entity
@Table(name = "modelling_data")
public class ModellingData {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
   
    @NaturalId
    @Column(name = "userID")
    private int userID;
    
    //csv file stored in the database
    @Lob
    @Column(name = "data")
    private String data;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
   
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
}
