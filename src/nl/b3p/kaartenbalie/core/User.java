/*
 * User.java
 *
 * Created on 7 september 2006, 11:08
 */

package nl.b3p.kaartenbalie.core;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Roy
 */
public class User implements Principal{
    
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Set roles;
    private long id;

    
    /** Creates a new instance of User */
    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }
/* impl principal*/
    public String getName() {
        return username;
    }
    
}
