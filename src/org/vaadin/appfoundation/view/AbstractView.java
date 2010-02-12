package org.vaadin.appfoundation.view;

import java.io.Serializable;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;

/**
 * Interface which all main views should implement
 * 
 * @author Kim
 * 
 */
public abstract class AbstractView<A extends ComponentContainer> extends
        CustomComponent implements Serializable {

    private static final long serialVersionUID = -1420553541682132603L;

    protected A content;

    protected AbstractView(A layout) {
        setCompositionRoot(layout);
        content = layout;
        setSizeFull();
    }

    /**
     * This method is called when the view is activated.
     */
    public abstract void activated(Object... params);

}
