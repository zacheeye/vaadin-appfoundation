package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.test.MockApplication;
import org.vaadin.appfoundation.view.ViewHandler;
import org.vaadin.appfoundation.view.ViewItem;

public class ViewHandlerTest {

    @Before
    public void setUp() {
        // Initialize the Lang class with the MockApplication
        new ViewHandler(new MockApplication());
    }

    @Test
    public void addView() {
        // With no parameters
        Object id1 = ViewHandler.addView();
        assertNotNull(id1);
        assertNotNull(ViewHandler.getViewItem(id1));

        // With a view id param
        String id2 = "id2";
        ViewHandler.addView(id2);
        ViewItem item = ViewHandler.getViewItem(id2);
    }

}
