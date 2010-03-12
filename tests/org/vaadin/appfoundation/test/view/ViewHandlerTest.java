package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void getViewItem() {
        ViewItem item1 = ViewHandler.addView("test");
        ViewItem item2 = ViewHandler.addView("test2");

        assertTrue(ViewHandler.getViewItem("test").getViewId().equals("test"));
        assertEquals(item1, ViewHandler.getViewItem("test"));

        assertTrue(ViewHandler.getViewItem("test2").getViewId().equals("test2"));
        assertEquals(item2, ViewHandler.getViewItem("test2"));

        assertNull(ViewHandler.getViewItem("nonExistingId"));
    }

}
