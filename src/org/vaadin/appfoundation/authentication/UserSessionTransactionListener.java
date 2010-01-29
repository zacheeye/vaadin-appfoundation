package org.vaadin.appfoundation.authentication;

import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.util.SessionUtil;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 * Transaction listener which makes sure the SessionUtil has the correct user
 * instance for every thread.
 * 
 * @author Kim
 * 
 */
public class UserSessionTransactionListener implements TransactionListener {

    private static final long serialVersionUID = 4142938996955537395L;

    private final Application application;

    private User user;

    /**
     * Constructor
     * 
     * @param application
     *            Current application instance
     */
    public UserSessionTransactionListener(Application application) {
        this.application = application;
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear the currentApplication field
        if (this.application == application) {
            // Get the current user
            user = SessionUtil.get();
        }
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Check if the application instance we got as parameter is actually
        // this application instance. If it is, then we should define the thread
        // local variable for this request.
        if (this.application == application) {
            // Set the current user
            SessionUtil.setUser(user);
        }
    }

}
