package org.vaadin.appfoundation.view;

/**
 * Interface which all views who has child views needs to implement.
 * 
 * @author Kim
 * 
 */
public interface ViewContainer {

    /**
     * Activates the given view.
     * 
     * @param view
     */
    public void activate(AbstractView<?> view);

    /**
     * Deactivates the given view.
     * 
     * @param view
     */
    public void deactivate(AbstractView<?> view);

}
