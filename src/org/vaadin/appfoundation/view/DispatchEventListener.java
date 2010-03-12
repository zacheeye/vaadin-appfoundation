package org.vaadin.appfoundation.view;

/**
 * Interface for view dispatch events listeners.
 * 
 * @author Kim
 * 
 */
public interface DispatchEventListener {

    /**
     * Called before the ViewHandler activates a view.
     * 
     * @param event
     */
    public void preDispatch(DispatchEvent event) throws DispatchException;

    /**
     * Called after the ViewHandler have activated a view.
     * 
     * @param event
     */
    public void postDispatch(DispatchEvent event);

}
