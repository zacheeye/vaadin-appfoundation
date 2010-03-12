package org.vaadin.appfoundation.authentication.data;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vaadin.appfoundation.persistence.data.AbstractPojo;

/**
 * Entity class for users. This class keeps information about registered users.
 * 
 * @author Kim
 * 
 */
@Entity
@Table(name = "appuser")
public class User extends AbstractPojo {

    private static final long serialVersionUID = 4417119399127203109L;

    protected String username;

    protected String password;

    private String name;

    private String email;

    public User() {

    }

    /**
     * Get the username of the user
     * 
     * @return User's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for the user
     * 
     * @param username
     *            New username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the hashed password of the user
     * 
     * @return Hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the hashed password of the user
     * 
     * @param password
     *            New hHashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set the actual name of the user
     * 
     * @param name
     *            New name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the actual name of the user
     * 
     * @return Name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Set an email address for the user
     * 
     * @param email
     *            New email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's email address
     * 
     * @return User's email address
     */
    public String getEmail() {
        return email;
    }

}
