package cz.matyapav.models;

import cz.matyapav.utils.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Pavel on 14.12.2016.
 */
@Entity
@Table(name = "User")
public class User implements Serializable{

    @Id
    @Column(name = "username", nullable = false, length = 40, unique = true)
    private String username;

    @Column(name = "firstName", nullable = false, length = 80)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 80)
    private String lastName;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "enabled")
    private int enabled;

    @Transient
    private String passwordRetype;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Bill> bills = new HashSet<>();

    @ManyToMany
    private List<UserRole> roles;

    public User() {
    }

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }


    public void removeBill(Bill bill) {
        if (bill != null && bills != null && bills.contains(bill)) {
            bills.remove(bill);
        }
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void addRole(UserRole role){
        if(roles == null){
            roles = new ArrayList<>();
        }
        if(role != null){
            roles.add(role);
        }
    }

    public boolean isInBill(Bill bill){
        for(Bill bill1 : getBills()){
            if(bill1.getId() == bill.getId()){
                return true;
            }
        }
        return false;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
