/*
 * 
 */
package ua.kpi.atep.model.entity;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import ua.kpi.atep.dynamic.generic.DynamicModel;



/**
 *
 * @author Home
 */
@Entity
@Table(name = "model")
public class Assignment implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    
    @Lob
    @Column(name = "model")
    private DynamicModel model;
   
    @Column(name = "comment")
    private String comment;
   
    //! if ever needed, should be called in the same session (just for reference)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment")
    private Set<Student> students;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DynamicModel getModel() {
        return model;
    }

    public void setModel(DynamicModel model) {
        this.model = model;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    
}
