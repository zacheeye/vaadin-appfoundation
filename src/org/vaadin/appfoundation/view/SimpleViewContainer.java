package org.vaadin.appfoundation.view;

import com.vaadin.ui.Panel;

/**
 * A simple implementation of the {@link ViewContainer} interface. This class
 * simply contains a panel and adds the activated view to the panel. When a view
 * is activated, then any previous views are removed from the panel.
 * Deactivating a view will clear the panel from any components. This class is a
 * view itself, so it can be added to the {@link ViewHandler} as any other view.
 * 
 * @author Kim
 * 
 */
public class SimpleViewContainer extends AbstractView<Panel> implements
        ViewContainer {

    private static final long serialVersionUID = 9010669373711637452L;

    public SimpleViewContainer() {
        super(new Panel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activated(Object... params) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivated(Object... params) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void activate(AbstractView<?> view) {
        getContent().removeAllComponents();
        getContent().addComponent(view);
    }

    /**
     * {@inheritDoc}
     */
    public void deactivate(AbstractView<?> view) {
        getContent().removeAllComponents();
    }

}
