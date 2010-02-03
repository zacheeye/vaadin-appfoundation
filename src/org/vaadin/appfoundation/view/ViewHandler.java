package org.vaadin.appfoundation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 * Utility class for handling and chaning views in an application.
 * 
 * @author Kim
 * 
 */
public class ViewHandler implements TransactionListener {

    private static final long serialVersionUID = -3548570790687380424L;

    // A map between the view id and the view item
    private final Map<Object, ViewItem> viewMap = new HashMap<Object, ViewItem>();

    // A map between parent ids and parent views
    private final Map<Object, ViewContainer> parentMap = new HashMap<Object, ViewContainer>();

    // A list of all known dispatch event listeners.
    private final List<DispatchEventListener> listeners = new ArrayList<DispatchEventListener>();

    // Store this instance of the view handler in this thread local variable
    private static final ThreadLocal<ViewHandler> instance = new ThreadLocal<ViewHandler>();

    private final Application application;

    /**
     * 
     * @param application
     *            Current application instance
     */
    public ViewHandler(Application application) {
        instance.set(this);
        this.application = application;
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear thread local instance at the end of the transaction
        if (this.application == application) {
            instance.set(null);
        }
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Set the thread local instance
        if (this.application == application) {
            instance.set(this);
        }
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
        if (instance.get().viewMap.containsKey(viewId)) {
            return null;
        }

        // Create a new ViewItem and add it to the map.
        ViewItem item = new ViewItem(viewId);
        instance.get().viewMap.put(viewId, item);
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
        instance.get().viewMap.put(viewId, item);
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
        if (viewId != null && instance.get().viewMap.containsKey(viewId)) {
            return instance.get().viewMap.get(viewId);
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
        if (viewId != null && instance.get().viewMap.containsKey(viewId)) {
            instance.get().viewMap.remove(viewId);
            return true;
        }

        return false;
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
        if (viewId != null && instance.get().viewMap.containsKey(viewId)
                && instance.get().parentMap.containsKey(viewId)) {
            // Get the ViewItem and parent for this viewId
            ViewItem item = instance.get().viewMap.get(viewId);
            ViewContainer parent = instance.get().parentMap.get(viewId);

            // Create the dispatch event object
            DispatchEvent event = new DispatchEvent(item, params);
            // Loop through all the dispatch event listeners
            try {
                for (DispatchEventListener listener : instance.get().listeners) {
                    listener.preDispatch(event);
                }
            } catch (DispatchException e) {
                // The dispatch was canceled, stop the execution of this method.
                return;
            }

            // Tell the parent to activate the given view
            parent.activate(item.getView());

            // Tell the view that it has been activated
            item.getView().activated(params);

            // View has been dispatched, send event
            for (DispatchEventListener listener : instance.get().listeners) {
                listener.postDispatch(event);
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
                && instance.get().viewMap.containsKey(viewId)) {
            instance.get().parentMap.put(viewId, parent);
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
            instance.get().listeners.add(listener);
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
            instance.get().listeners.remove(listener);
        }
    }
}
