package org.vaadin.appfoundation.authentication.util;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.appfoundation.authentication.SessionHandler;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.exceptions.InvalidCredentialsException;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

/**
 * 
 * Utility class for authenticating users
 * 
 * @author Kim
 * 
 */
public class AuthenticationUtil {

    /**
     * Try to log in a user with the given user credentials
     * 
     * @param username
     *            Username of the user
     * @param password
     *            Password of the user
     * @return The authenticated user object
     * @throws InvalidCredentialsException
     *             Thrown if the crendentials are incorrect
     */
    public static User authenticate(String username, String password)
            throws InvalidCredentialsException {
        // Login fails if either the username or password is null
        if (username == null || password == null) {
            throw new InvalidCredentialsException();
        }

        // Create a query which searches the database for a user with the given
        // username
        String query = "SELECT u FROM User u WHERE u.username = :username";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);
        User user = (User) FacadeFactory.getFacade().find(query, parameters);

        // Check if the user exists
        if (user != null) {
            // Check if the user's password is correct
            if (PasswordUtil.verifyPassword(user, password)) {
                // The user's password was correct, so set the user as the
                // current user (inlogged)
                SessionHandler.setUser(user);

                return user;
            }
        }
        // Either the username didn't exist or the passwords did not match
        throw new InvalidCredentialsException();
    }

}
