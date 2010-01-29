package org.vaadin.appfoundation.authentication.util;

import org.vaadin.appfoundation.authentication.data.User;

/**
 * A utility class for handling user sessions
 * 
 * @author Kim
 * 
 */
public class SessionUtil {

    // Store the user object of the currently inlogged user
    private static ThreadLocal<User> currentUser = new ThreadLocal<User>();

    /**
     * Set the User object for the currently inlogged user for this application
     * instance
     * 
     * @param user
     */
    public static void setUser(User user) {
        currentUser.set(user);
    }

    /**
     * Get the User object of the currently inlogged user for this application
     * instance.
     * 
     * @return
     */
    public static User get() {
        return currentUser.get();
    }

    /**
     * Method for logging out a user
     */
    public static void logout() {
        setUser(null);
    }

}
