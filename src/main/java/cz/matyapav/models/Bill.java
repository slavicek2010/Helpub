package cz.matyapav.models;

import cz.matyapav.utils.Utils;
import org.springframework.transaction.annotation.Transactional;
/*TODO invite/add user to getBillForm ..spise add na invite nebude cas, ten se casem dodela
  mozna request to join + approve .. jenom join ne protoze to by se pak kdokoliv pripojil a delal by tam bordel*/

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @Column(name="creator", nullable = false)
    private String creatorUsername;

    @Column(name="opened", nullable = false)
    private boolean opened;

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
        if (users != null && user != null) {
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
}
