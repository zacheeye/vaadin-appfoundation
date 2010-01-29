package org.vaadin.appfoundation.authentication.util;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.appfoundation.authentication.data.User;
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
     * Authentication messages which are returned by the authenticate() method.
     * 
     * @author Kim
     * 
     */
    public enum AuthenticationMessage {
        INVALID_CREDENTIALS,
        AUTH_SUCCESSFUL,
        DATABASE_ERROR
    }

    /**
     * Try to log in a user with the given user credentials
     * 
     * @param username
     *            Username of the user
     * @param password
     *            Password of the user
     * @return Returns an integer representing the authentication message
     */
    public static int authenticate(String username, String password) {
        // Login fails if either the username or password is null
        if (username == null || password == null) {
            return AuthenticationMessage.INVALID_CREDENTIALS.ordinal();
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
                SessionUtil.setUser(user);

                return AuthenticationMessage.AUTH_SUCCESSFUL.ordinal();
            }
        }
        // Either the username didn't exist or the passwords did not match
        return AuthenticationMessage.INVALID_CREDENTIALS.ordinal();
    }

}
