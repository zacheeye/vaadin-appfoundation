package org.vaadin.appfoundation.view;

/**
 * Default implementation for the ViewFactory interface. This factory expects
 * the viewId to an instance of the view's class object and uses that class
 * object to initialize the view.
 * 
 * @author Kim
 * 
 */
public class DefaultViewFactory implements ViewFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractView<?> initView(Object viewId) {
        if (viewId instanceof Class<?>) {
            if (AbstractView.class.isAssignableFrom((Class<?>) viewId))
                try {
                    return (AbstractView<?>) ((Class<?>) viewId).newInstance();
                } catch (InstantiationException e) {
                    return null;
                } catch (IllegalAccessException e) {
                    return null;
                }
        }
        return null;
    }

}
