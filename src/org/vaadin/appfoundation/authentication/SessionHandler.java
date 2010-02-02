package org.vaadin.appfoundation.authentication;

import org.vaadin.appfoundation.authentication.data.User;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 * A utility class for handling user sessions
 * 
 * @author Kim
 * 
 */
public class SessionHandler implements TransactionListener {

    private static final long serialVersionUID = 4142938996955537395L;

    private final Application application;

    private User user;
    
    // Store the user object of the currently inlogged user
    private static ThreadLocal<User> currentUser = new ThreadLocal<User>();

    /**
     * Constructor
     * 
     * @param application
     *            Current application instance
     */
    public SessionHandler(Application application) {
        this.application = application;
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear the currentApplication field
        if (this.application == application) {
            // Get the current user
            user = SessionHandler.get();
        }
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Check if the application instance we got as parameter is actually
        // this application instance. If it is, then we should define the thread
        // local variable for this request.
        if (this.application == application) {
            // Set the current user
            SessionHandler.setUser(user);
        }
    }

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
     * @return The currently inlogged user
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
