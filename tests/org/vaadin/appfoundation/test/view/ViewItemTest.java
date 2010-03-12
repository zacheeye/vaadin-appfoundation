package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.vaadin.appfoundation.view.DefaultViewFactory;
import org.vaadin.appfoundation.view.ViewItem;

public class ViewItemTest {

    @Test
    public void initWithRandomId() {
        ViewItem item = new ViewItem("test");
        assertNull(item.getViewClass());
        assertNull(item.getFactory());
        assertNull(item.getView());
        assertEquals("test", item.getViewId());
    }

    @Test
    public void initWithView() {
        ViewItem item = new ViewItem(MockView.class);
        assertEquals(MockView.class, item.getViewClass());
        assertTrue(item.getFactory() instanceof DefaultViewFactory);
        assertEquals(MockView.class, item.getViewId());
        assertTrue(item.getView() instanceof MockView);
    }

    @Test
    public void setView() {
        ViewItem item = new ViewItem("test");
        MockView view = new MockView();
        item.setView(view);
        assertEquals(view, item.getView());
    }
}
