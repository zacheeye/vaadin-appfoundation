package org.vaadin.appfoundation.view;

/**
 * The view item is a a wrapper class used by the ViewHandler. It contains
 * information about the views added to the ViewHandler.
 * 
 * @author Kim
 * 
 */
public class ViewItem {

    private final Object viewId;

    private AbstractView<?> view;

    private Class<? extends AbstractView<?>> viewClass = null;

    /**
     * Constructor. Takes as input the viewId. If the viewId is an instance of
     * Class<? extends AbstractView>, then the viewId is used as the default
     * viewClass.
     * 
     * @param viewId
     *            The id for the ViewItem
     */
    @SuppressWarnings("unchecked")
    public ViewItem(Object viewId) {
        if (viewId instanceof Class) {
            if (AbstractView.class.isAssignableFrom((Class<?>) viewId)) {
                viewClass = (Class<? extends AbstractView<?>>) viewId;
            }

        }
        this.viewId = viewId;
    }

    /**
     * Set the view object for this ViewItem
     * 
     * @param view
     *            The view instance
     */
    public void setView(AbstractView<?> view) {
        this.view = view;
    }

    /**
     * Get the view object for this item. If view isn't set and the viewClass is
     * defined, then an instance of the class is created and returned.
     * 
     * @return The view instance
     */
    public AbstractView<?> getView() {
        if (view == null && viewClass != null) {
            try {
                view = viewClass.newInstance();
            } catch (InstantiationException e) {
                // TODO
            } catch (IllegalAccessException e) {
                // TODO
            }
        }
        return view;
    }

    /**
     * Returns the view id for this ViewItem.
     * 
     * @return The item's id
     */
    public Object getViewId() {
        return viewId;
    }

    /**
     * Set the viewClass. This is only required if a view instance is not
     * provided. The viewClass is used for creating an instance of View when
     * getView() is called in case an instance of View is not available.
     * 
     * @param viewClass
     *            The view's class
     */
    public void setViewClass(Class<? extends AbstractView<?>> viewClass) {
        this.viewClass = viewClass;
    }

    /**
     * Get the current view class.
     * 
     * @return The view's class
     */
    public Class<? extends AbstractView<?>> getViewClass() {
        return viewClass;
    }

}
