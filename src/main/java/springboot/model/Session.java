package springboot.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "session")
public class Session implements Serializable{
    @Id Long id;
    @MapsId
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "token", nullable = false, length = 100)
    private String token;


    public Session() {
    }

    public Session(User id, String token){
        this.token = token;
        this.userId = id;
    }
    @Override
    public String toString() {
        return getToken();
    }

    // accessors

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId.getId();
    }

    public void setId(User id) {this.userId = id;}

    public User getUser() {
        return this.userId;
    }
}