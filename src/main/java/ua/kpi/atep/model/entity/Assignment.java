/*
 * 
 */
package ua.kpi.atep.model.entity;


import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import ua.kpi.atep.model.dynamic.object.DynamicModel;



/**
 *
 * @author Home
 */
@Entity
@Table(name = "model")
public class Assignment implements Serializable {
    
    /**
     * Variant of the assigment
     * Assignment has no surrogate id, since it is a managed
     * item and id is specified manually and conveniently
     * fits to the model
     */
   
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Column(name = "id")
    private Integer id;
    
    @Lob
    @Column(name = "model")
    private DynamicModel model;
   
    @Column(name = "comment")
    private String comment;
   
    
// not needed    
//    //! if ever needed, should be called in the same session (just for reference)
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "assignment")
//    private Set<Student> students;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

//    public Set<Student> getStudents() {
//        return students;
//    }
//    
//    public void setStudents(Set<Student> students) {
//        this.students = students;
//    }
    
}
