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

        User user = getUserForUsername(username);

        if (user == null) {
            // Invalid suername
            throw new InvalidCredentialsException();
        }

        if (!PasswordUtil.verifyPassword(user, password)) {
            // Invalid password
            incrementFailedLoginAttempts(user);
            throw new InvalidCredentialsException();
        }
        // The user's password was correct, so set the user as the
        // current user (inlogged)
        SessionHandler.setUser(user);
        clearFailedLoginAttempts(user);

        return user;
    }

    /**
     * Fetches the User object from the database for the given username
     * 
     * @param username
     * @return User object for username
     */
    private static User getUserForUsername(String username) {
        // Create a query which searches the database for a user with the given
        // username
        String query = "SELECT u FROM User u WHERE u.username = :username";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);
        return (User) FacadeFactory.getFacade().find(query, parameters);
    }

    /**
     * Clear the number of failed login attempts if the user has any failed
     * attempts.
     * 
     * @param user
     *            The user whose failed login attempt record should be cleared
     */
    private static void clearFailedLoginAttempts(User user) {
        if (user.getFailedLoginAttempts() > 0) {
            user.clearFailedLoginAttempts();
            FacadeFactory.getFacade().store(user);
        }
    }

    /**
     * Increment the user's number of failed login attempts by one
     * 
     * @param user
     */
    public static void incrementFailedLoginAttempts(User user) {
        user.incrementFailedLoginAttempts();
        FacadeFactory.getFacade().store(user);
    }

}
