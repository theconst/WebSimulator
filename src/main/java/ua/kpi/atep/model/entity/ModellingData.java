/*
 * 
 */
package ua.kpi.atep.model.entity;


import java.io.Serializable;
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
public class ModellingData implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
   
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

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
   
    
    public Integer getId() {
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
