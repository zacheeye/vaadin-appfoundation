package org.vaadin.appfoundation.authentication;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.appfoundation.authentication.data.User;

import com.apple.eawt.Application;
import com.vaadin.ui.UI;

/**
 * A utility class for handling user sessions
 * 
 * @author Kim
 * 
 */
public class SessionHandler {

	private static final long serialVersionUID = 4142938996955537395L;

	private final UI ui;

	private User user;

	private List<LogoutListener> listeners = new ArrayList<LogoutListener>();

	// Store the user object of the currently inlogged user

	/**
	 * Constructor
	 * 
	 * @param ui
	 *            Current application instance
	 */
	public SessionHandler(UI ui) {
		this.ui = ui;
		ui.getSession().setAttribute(ui.getId() + "-sessionhandler", this);
	}

	/**
	 * Set the User object for the currently inlogged user for this application
	 * instance
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		getCurrent().user = user;
	}

	/**
	 * Get the User object of the currently inlogged user for this application
	 * instance.
	 * 
	 * @return The currently inlogged user
	 */
	public static User get() {
		return getCurrent().user;
	}

	/**
	 * Method for logging out a user
	 */
	public static void logout() {
		LogoutEvent event = new LogoutEvent(getCurrent().user);
		setUser(null);
		dispatchLogoutEvent(event);
	}

	/**
	 * Dispatches the {@link LogoutEvent} to all registered logout listeners.
	 * 
	 * @param event
	 *            The LogoutEvent
	 */
	private static void dispatchLogoutEvent(LogoutEvent event) {
		for (LogoutListener listener : getCurrent().listeners) {
			listener.logout(event);
		}
	}

	/**
	 * Initializes the {@link SessionHandler} for the given {@link Application}
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new SessionHandler(ui);
	}

	/**
	 * Add a logout listener.
	 * 
	 * @param listener
	 */
	public static void addListener(LogoutListener listener) {
		getCurrent().listeners.add(listener);
	}

	/**
	 * Remove the given listener from the active logout listeners.
	 * 
	 * @param listener
	 */
	public static void removeListener(LogoutListener listener) {
		getCurrent().listeners.remove(listener);
	}
	
	private static SessionHandler getCurrent() {
		UI ui = UI.getCurrent();
		SessionHandler handler = (SessionHandler) ui.getSession().getAttribute(
				ui.getId() + "-sessionhandler");
		return handler;
	}

}
