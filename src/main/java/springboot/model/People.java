package springboot.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "people")
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String first_name;

    @Column(name="last_name", nullable = false, length = 100)
    private String last_name;

    @Column(name="dob", nullable = false, length = 100)
    private Date dob;

    public People() {
    }

    @Override
    public String toString() {
        return getFirst_name()+" "+getLast_name();
    }

    // accessors

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setdob(Date dob) {
        this.dob = dob;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}