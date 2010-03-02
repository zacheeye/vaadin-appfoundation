package org.vaadin.appfoundation.view;

/**
 * Interface for view factories. A view factory's responsibility is to
 * initialize the view object instance for the given view id.
 * 
 * @author Kim
 * 
 */
public interface ViewFactory {

    /**
     * Initializes the view object for the given view id.
     * 
     * @param viewId
     *            View id for the view we want to initialize
     * @return Instance of the initialized view
     */
    public AbstractView<?> initView(Object viewId);

}
