package cz.matyapav.models;

import cz.matyapav.utils.Utils;
/*TODO invite/add user to getBillForm ..spise add na invite nebude cas, ten se casem dodela
  mozna request to join + approve .. jenom join ne protoze to by se pak kdokoliv pripojil a delal by tam bordel*/

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pavel on 21.12.2016.
 */
@Entity
@Table(name = "Bill")
public class Bill {

    //TODO rename to bill_id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bill_id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 255)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    @Column(name="creator", nullable = false)
    private String creatorUsername;

    @Column(name="opened", nullable = false)
    private boolean opened;

    @Column(name="locked", nullable = false)
    private boolean locked;

    @Column(name = "password")
    @Size(min = 4, max = 60)
    private String password;

    @Transient
    private String passwordRetype;


    public Bill() {

    }

    public Bill(int id, String name, String creatorUsername, boolean opened) {
        this.id = id;
        this.name = name;
        this.creatorUsername = creatorUsername;
        this.opened = opened;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void addUser(User user) {
        if (user != null) {
            if(users == null){
                users = new HashSet<>();
            }
            users.add(user);
        }
    }

    public void removeUserByUserName(String username){
        for(User user: users){
            if(user.getUsername().equals(username)){
                users.remove(user);
                break;
            }
        }
    }

    public boolean containsLoggedUser(){
        for(User user : getUsers()){
            if(user.getUsername().equals(Utils.getLoggedUser().getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean containsUserWithUsername(String username){
        if(users != null) {
            for (User user : getUsers()) {
                if (user.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRetype() {
        return passwordRetype;
    }

    public void setPasswordRetype(String passwordRetype) {
        this.passwordRetype = passwordRetype;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
