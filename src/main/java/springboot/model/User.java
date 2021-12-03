package springboot.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = 100)
    private String login;

    @Column(name="password", nullable = false, length = 100)
    private String password;

    public User() {
    }

    @Override
    public String toString() {
        return getLogIn();
    }

    // accessors

    public String getLogIn() {
        return this.login;
    }

    public void setLogIn(String logIn) {
        this.login = logIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword(){return this.password;}

    public boolean checkPass(String pass){
        if( this.password == pass){
            return true;
        }
        else{
            return false;
        }
    }
}