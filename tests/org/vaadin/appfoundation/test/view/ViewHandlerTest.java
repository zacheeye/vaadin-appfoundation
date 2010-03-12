package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.test.MockApplication;
import org.vaadin.appfoundation.view.AbstractView;
import org.vaadin.appfoundation.view.DefaultViewFactory;
import org.vaadin.appfoundation.view.ViewContainer;
import org.vaadin.appfoundation.view.ViewFactory;
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

    @Test
    public void addViewNoParams() {
        // With no parameters
        Object id1 = ViewHandler.addView();
        assertNotNull(id1);
        assertNotNull(ViewHandler.getViewItem(id1));
    }

    @Test
    public void addViewObjectParam() {
        // With a view id param
        String id2 = "id2";
        ViewHandler.addView(id2);
        ViewItem item = ViewHandler.getViewItem(id2);
        assertNotNull(item);
        assertEquals(id2, item.getViewId());
    }

    @Test
    public void addViewClassParam() {
        // Add view with an id of a View class
        ViewItem item2 = ViewHandler.addView(MockView.class);
        assertEquals(MockView.class, item2.getViewClass());
        assertTrue(item2.getFactory() instanceof DefaultViewFactory);
    }

    @Test
    public void addViewExistingView() {
        String id2 = "id2";
        ViewHandler.addView(id2);

        // Try adding a view with an existing id
        assertNull(ViewHandler.addView(id2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addViewNullParam() {
        ViewHandler.addView(null);
    }

    @Test
    public void addViewWithParent() {
        final ValueContainer value = new ValueContainer();

        ViewContainer container = new ViewContainer() {
            public void activate(AbstractView<?> view) {
                value.setValue(view);
            }
        };

        ViewItem item = ViewHandler.addView(MockView.class, container);

        ViewHandler.activateView(MockView.class);
        assertNotNull(value.getValue());
        assertEquals(item.getView(), value.getValue());
    }

    @Test
    public void setDefaultViewFactory() {
        ViewFactory factory = new ViewFactory() {
            public AbstractView<?> initView(Object viewId) {
                return null;
            }
        };

        // Make sure the new view factory is set
        assertNull(ViewHandler.getDefaultViewFactory());
        ViewHandler.setDefaultViewFactory(factory);
        assertNotNull(ViewHandler.getDefaultViewFactory());
        assertEquals(factory, ViewHandler.getDefaultViewFactory());

        // Add new view and make sure it got our new default view factory as its
        // factory
        ViewItem item = ViewHandler.addView(MockView.class);
        assertNotNull(item.getFactory());
        assertEquals(factory, item.getFactory());
    }

    private class ValueContainer {

        private Object value;

        public ValueContainer() {
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
