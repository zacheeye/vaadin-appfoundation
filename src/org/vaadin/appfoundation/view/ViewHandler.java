package org.vaadin.appfoundation.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.vaadin.appfoundation.authorization.Permissions;

import com.apple.eawt.Application;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.UI;

/**
 * Utility class for handling and chaning views in an application.
 * 
 * @author Kim
 * 
 */
public class ViewHandler implements Serializable, UriFragmentChangedListener {

	private static final long serialVersionUID = -3548570790687380424L;

	// A map between the view id and the view item
	private final Map<Object, ViewItem> viewMap = new HashMap<Object, ViewItem>();

	// A map between parent ids and parent views
	private final Map<Object, ViewContainer> parentMap = new HashMap<Object, ViewContainer>();

	// A list of all known dispatch event listeners.
	private final List<DispatchEventListener> listeners = new ArrayList<DispatchEventListener>();

	// A map between URIs and view ids
	private final Map<String, Object> uriMap = new HashMap<String, Object>();

	private ViewFactory defaultViewFactory = null;

	/**
	 * 
	 * @param ui
	 *            Current application instance
	 */
	public ViewHandler(UI ui) {
		ui.getSession().setAttribute(ui.getId() + "-viewhandler", this);
		ui.getPage().addUriFragmentChangedListener(this);
	}

	/**
	 * Add a new View to the ViewHandler. Takes as input a viewId. The user is
	 * responsible of setting the view class. If the viewId is already in use,
	 * then null is returned.
	 * 
	 * @param viewId
	 *            The view's id
	 * @return The resulting ViewItem object
	 */
	public static ViewItem addView(Object viewId) {
		if (viewId == null) {
			throw new IllegalArgumentException("View id may not be null");
		}

		// Check if the viewId is already in use. If it is, then return null.
		if (getCurrent().viewMap.containsKey(viewId)) {
			return null;
		}

		// Create a new ViewItem and add it to the map.
		ViewItem item = new ViewItem(viewId);
		getCurrent().viewMap.put(viewId, item);

		// Check if we have a default ViewFactory defined. If one is defined,
		// then set it to the item.
		if (getCurrent().defaultViewFactory != null) {
			item.setFactory(getCurrent().defaultViewFactory);
		}
		return item;
	}

	/**
	 * Add a new view to the view handler.
	 * 
	 * @param viewId
	 *            The view's id
	 * @param parent
	 *            Parent view for the given view
	 * @return The resulting ViewItem object
	 */
	public static ViewItem addView(Object viewId, ViewContainer parent) {
		ViewItem item = addView(viewId);
		setParent(viewId, parent);
		return item;
	}

	/**
	 * Add a new view. Returns the viewId. Make sure to set either the view
	 * instance or the view class for the ViewItem.
	 * 
	 * @return The viewId
	 */
	public static Object addView() {
		Object viewId = UUID.randomUUID();
		ViewItem item = new ViewItem(viewId);
		getCurrent().viewMap.put(viewId, item);
		return viewId;
	}

	/**
	 * Fetch the ViewItem for the given viewId. If the viewId is not found, then
	 * null is returned.
	 * 
	 * @param viewId
	 * @return The ViewItem object for the given viewId
	 */
	public static ViewItem getViewItem(Object viewId) {
		// Check if the viewId exists in the map
		if (viewId != null && getCurrent().viewMap.containsKey(viewId)) {
			return getCurrent().viewMap.get(viewId);
		}

		return null;
	}

	/**
	 * Removes the ViewItem from the handler for the given viewId.
	 * 
	 * @param viewId
	 * @return Returns true if the viewId existed, otherwise false.
	 */
	public static boolean removeView(Object viewId) {
		if (viewId != null && getCurrent().viewMap.containsKey(viewId)) {
			getCurrent().viewMap.remove(viewId);

			// Check if the view has an uri defined
			String uri = getUriForViewId(viewId);
			if (uri != null) {
				// An uri definition was found, remove it
				getCurrent().uriMap.remove(uri);
			}
			return true;
		}

		return false;
	}

	/**
	 * Search and return the uri for the given view id
	 * 
	 * @param viewId
	 *            View id we want to get the uri for
	 * @return Returns the uri if one is found, otherwise returns null
	 */
	private static String getUriForViewId(Object viewId) {
		Iterator<String> it = getCurrent().uriMap.keySet().iterator();
		while (it.hasNext()) {
			String uri = it.next();
			if (getCurrent().uriMap.get(uri).equals(viewId)) {
				return uri;
			}
		}

		return null;
	}

	/**
	 * Activate the view with the given viewId. You can specify any given amount
	 * of parameters for the activation request. Each parameter is forwarded to
	 * the View's activated() method.
	 * 
	 * @param viewId
	 *            The view's viewId
	 * @param params
	 *            Parameters used for activating the view
	 */
	public static void activateView(Object viewId, Object... params) {
		if (params != null && params.length > 0 && params[0] instanceof Boolean) {
			boolean changeUriFragment = (Boolean) params[0];
			// Workaround to remove first parameter
			List<Object> parameters = new ArrayList<Object>(
					Arrays.asList(params));
			parameters.remove(0);
			activateView(viewId, changeUriFragment, parameters.toArray());
		} else {
			activateView(viewId, false, params);
		}
	}

	/**
	 * Activate the view with the given viewId. You can specify any given amount
	 * of parameters for the activation request. Each parameter is forwarded to
	 * the View's activated() method.
	 * 
	 * @param viewId
	 *            The view's viewId
	 * @param changeUriFragment
	 *            Should the uri fragment be changed if the view has one set
	 * @param params
	 *            Parameters used for activating the view
	 */
	@SuppressWarnings("deprecation")
	private static void activateView(Object viewId, boolean changeUriFragment,
			Object... params) {
		if (viewId != null && getCurrent().viewMap.containsKey(viewId)
				&& getCurrent().parentMap.containsKey(viewId)) {
			// Get the ViewItem and parent for this viewId
			ViewItem item = getCurrent().viewMap.get(viewId);
			ViewContainer parent = getCurrent().parentMap.get(viewId);

			// Create the dispatch event object
			DispatchEvent event = new DispatchEvent(item, params);
			// Loop through all the dispatch event listeners
			try {
				for (DispatchEventListener listener : getCurrent().listeners) {
					listener.preDispatch(event);
					listener.preActivation(event);
				}
			} catch (DispatchException e) {
				// The dispatch was canceled, stop the execution of this method.
				return;
			}

			// Tell the parent to activate the given view
			parent.activate(item.getView());

			// Tell the view that it has been activated
			item.getView().activated(params);

			String uri = getUriForViewId(viewId);
			if (changeUriFragment && uri != null) {
				UI.getCurrent().getPage().setUriFragment(uri, false);
			}

			// View has been dispatched, send event
			for (DispatchEventListener listener : getCurrent().listeners) {
				listener.postDispatch(event);
				listener.postActivation(event);
			}
		}
	}

	/**
	 * Deactivate the view with the given viewId. You can specify any given
	 * amount of parameters for the deactivation request. Each parameter is
	 * forwarded to the View's deactivated() method.
	 * 
	 * @param viewId
	 *            The view's viewId
	 * @param params
	 *            Parameters used for activating the view
	 */
	public static void deactivateView(Object viewId, Object... params) {
		if (viewId != null && getCurrent().viewMap.containsKey(viewId)
				&& getCurrent().parentMap.containsKey(viewId)) {
			// Get the ViewItem and parent for this viewId
			ViewItem item = getCurrent().viewMap.get(viewId);
			ViewContainer parent = getCurrent().parentMap.get(viewId);

			// Create the dispatch event object
			DispatchEvent event = new DispatchEvent(item, params);
			// Loop through all the dispatch event listeners
			try {
				for (DispatchEventListener listener : getCurrent().listeners) {
					listener.preDeactivation(event);
				}
			} catch (DispatchException e) {
				// The dispatch was canceled, stop the execution of this method.
				return;
			}

			// Tell the parent to activate the given view
			parent.deactivate(item.getView());

			// Tell the view that it has been activated
			item.getView().deactivated(params);

			// View has been dispatched, send event
			for (DispatchEventListener listener : getCurrent().listeners) {
				listener.postDeactivation(event);
			}
		}
	}

	/**
	 * Set the parent view for the given viewId.
	 * 
	 * @param viewId
	 *            The viewId of the ViewItem
	 * @param parent
	 *            New parent for the view
	 */
	public static void setParent(Object viewId, ViewContainer parent) {
		if (viewId != null && parent != null
				&& getCurrent().viewMap.containsKey(viewId)) {
			getCurrent().parentMap.put(viewId, parent);
		}
	}

	/**
	 * Add a dispatch event listener.
	 * 
	 * @param listener
	 *            The new listener
	 */
	public static void addListener(DispatchEventListener listener) {
		if (listener != null) {
			getCurrent().listeners.add(listener);
		}
	}

	/**
	 * Remove a dispatch event listener.
	 * 
	 * @param listener
	 *            The listener to be removed
	 */
	public static void removeListener(DispatchEventListener listener) {
		if (listener != null) {
			getCurrent().listeners.remove(listener);
		}
	}

	/**
	 * Set the default ViewFactory which is to be used in all <b>newly</b> added
	 * views.
	 * 
	 * @param defaultViewFactory
	 *            Default ViewFactory to be used in all new views.
	 */
	public static void setDefaultViewFactory(ViewFactory defaultViewFactory) {
		getCurrent().defaultViewFactory = defaultViewFactory;
	}

	/**
	 * Get the current default ViewFactory.
	 * 
	 * @return Default ViewFactory
	 */
	public static ViewFactory getDefaultViewFactory() {
		return getCurrent().defaultViewFactory;
	}

	/**
	 * Maps an uri to the given view id
	 * 
	 * @param uri
	 * @param viewId
	 */
	public static void addUri(String uri, Object viewId) {
		// Make sure the uri is valid
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("Uri must be defined");
		}

		// Make sure the view id is set
		if (viewId == null) {
			throw new IllegalArgumentException("View id must be defined");
		}

		if (getCurrent().uriMap.containsKey(uri)) {
			throw new IllegalArgumentException(
					"A view is already defined for this uri");
		}

		// Make sure that a view has been added for the given view id
		if (!getCurrent().viewMap.containsKey(viewId)) {
			throw new IllegalArgumentException(
					"View id not found - a valid view id must be provided");
		}

		// Add the uri to the map
		getCurrent().uriMap.put(uri, viewId);
	}

	/**
	 * Remove an uri which have been mapped to a specific view id
	 * 
	 * @param uri
	 */
	public static void removeUri(String uri) {
		// Make sure the uri is valid
		if (uri == null || uri.isEmpty()) {
			throw new IllegalArgumentException("Uri must be defined");
		}

		getCurrent().uriMap.remove(uri);
	}

	/**
	 * Initializes the {@link ViewHandler} for the given {@link Application}
	 * 
	 * @param ui
	 */
	public static void initialize(UI ui) {
		if (ui == null) {
			throw new IllegalArgumentException("Application may not be null");
		}
		new ViewHandler(ui);
	}

	private static ViewHandler getCurrent() {
		UI ui = UI.getCurrent();
		ViewHandler current = (ViewHandler) ui.getSession().getAttribute(
				ui.getId() + "-viewhandler");
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void uriFragmentChanged(UriFragmentChangedEvent event) {
		// TODO Auto-generated method stub
		if (event != null) {
			// Get the uri fragment
			String fragment = event.getUriFragment();

			int i = fragment.indexOf('/');
			Object[] params = null;
			String uri;
			if (i < 0) {
				uri = fragment;
			} else {
				uri = fragment.substring(0, i);
				params = fragment.subSequence(i + 1, fragment.length())
						.toString().split("/");
			}

			// Check if the fragment exists in the map
			if (getCurrent().uriMap.containsKey(uri)) {
				// The view was found, activate it
				Object viewId = getCurrent().uriMap.get(uri);
				if (params != null) {
					activateView(viewId, params);
				} else {
					activateView(viewId);
				}
			}
		}
	}

}
