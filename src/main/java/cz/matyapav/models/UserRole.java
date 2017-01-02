package cz.matyapav.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 24.12.2016.
 */
@Entity
@Table(name = "roles")
public class UserRole implements Serializable {


    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Id
    @Column(name="role", nullable = false, unique = true)
    private String role;

    public UserRole(){

    }
    public UserRole(List<User> users, String role) {
        this.users = new ArrayList<>();
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        if(user != null) {
            users.add(user);
        }
    }
}
