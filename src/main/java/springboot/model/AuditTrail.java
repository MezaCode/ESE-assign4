package springboot.model;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "auditTrail")
public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "changed_msg", nullable = false, length = 1000)
    private String changed_msg;

    @MapsId
    @ManyToOne
    @JoinColumn(name="changed_by", nullable = false)
    private User changed_by;

    @MapsId
    @ManyToOne
    @JoinColumn(name="personId", nullable = false)
    private People personId;

    @Column(name="when", nullable = false, length = 100)
    private Date when;



    public AuditTrail() {
    }

    // accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChanged_msg() {
        return changed_msg;
    }

    public void setChanged_msg(String changed_msg) {
        this.changed_msg = changed_msg;
    }

    public User getChanged_by() {
        return changed_by;
    }

    public void setChanged_by(User changed_by) {
        this.changed_by = changed_by;
    }

    public People getPersonId() {
        return personId;
    }

    public void setPersonId(People personId) {
        this.personId = personId;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
    public String toString(){
        String ret = "ID: "+this.id+", changed_by: "+ this.changed_by+", msg: "+ this.changed_msg+", Person: "+ this.personId+", when: "+this.when;
        return ret;
    }
}