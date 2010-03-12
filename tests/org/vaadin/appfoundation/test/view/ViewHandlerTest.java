package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.test.MockApplication;
import org.vaadin.appfoundation.view.AbstractView;
import org.vaadin.appfoundation.view.DefaultViewFactory;
import org.vaadin.appfoundation.view.DispatchEvent;
import org.vaadin.appfoundation.view.DispatchEventListener;
import org.vaadin.appfoundation.view.DispatchException;
import org.vaadin.appfoundation.view.ViewContainer;
import org.vaadin.appfoundation.view.ViewFactory;
import org.vaadin.appfoundation.view.ViewHandler;
import org.vaadin.appfoundation.view.ViewItem;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;

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

    @Test
    public void removeViewNonExisting() {
        assertFalse(ViewHandler.removeView("test"));
    }

    @Test
    public void removeView() {
        ViewHandler.addView("test");
        assertTrue(ViewHandler.removeView("test"));
        assertNull(ViewHandler.getViewItem("test"));
        assertFalse(ViewHandler.removeView("test"));
    }

    @Test
    /**
     * Tests that the uri is removed for a view when the view is removed.
     */
    public void removeViewUriIsRemoved() {
        // Add two views
        ViewHandler.addView("test");
        ViewHandler.addView("test2");
        // Add an uri to the first view
        ViewHandler.addUri("test", "test");

        ViewHandler.removeView("test");
        // The "test" uri should have been removed when the view was removed.
        // Hence we should now be able to define a new view for the same uri
        ViewHandler.addUri("test", "test2");
    }

    @Test
    public void activateView() {
        final ValueContainer viewActivated = new ValueContainer();
        final ValueContainer parentCalled = new ValueContainer();
        viewActivated.setValue(false);
        parentCalled.setValue(false);

        AbstractView<ComponentContainer> view = new AbstractView<ComponentContainer>(
                new VerticalLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void activated(Object... params) {
                viewActivated.setValue(true);
            }
        };

        ViewItem item = ViewHandler.addView("test");
        item.setView(view);

        ViewHandler.activateView("test");
        // Parent not set
        assertFalse((Boolean) viewActivated.getValue());

        ViewContainer container = new ViewContainer() {
            public void activate(AbstractView<?> view) {
                parentCalled.setValue(true);
            }
        };

        ViewHandler.setParent("test", container);

        ViewHandler.activateView("test");
        // Parent is now set
        assertTrue((Boolean) viewActivated.getValue());
        assertTrue((Boolean) parentCalled.getValue());
    }

    @Test
    public void activateViewParamsPassed() {
        final ValueContainer parameters = new ValueContainer();

        AbstractView<ComponentContainer> view = new AbstractView<ComponentContainer>(
                new VerticalLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void activated(Object... params) {
                parameters.setValue(params);
            }
        };

        ViewItem item = ViewHandler.addView("test");
        item.setView(view);
        ViewContainer container = new ViewContainer() {
            public void activate(AbstractView<?> view) {
            }
        };

        ViewHandler.setParent("test", container);

        ViewHandler.activateView("test", "foo", "bar");
        assertEquals(2, ((Object[]) parameters.getValue()).length);
        assertEquals("foo", ((Object[]) parameters.getValue())[0]);
        assertEquals("bar", ((Object[]) parameters.getValue())[1]);
    }

    @Test
    public void dispatchEventListeners() {
        final ValueContainer preCalls = new ValueContainer(0);
        final ValueContainer postCalls = new ValueContainer(0);

        DispatchEventListener listener = new DispatchEventListener() {
            private boolean preCall = false;
            private boolean postCall = false;

            public void preDispatch(DispatchEvent event)
                    throws DispatchException {
                if (!preCall) {
                    preCall = true;
                    preCalls.setValue(((Integer) preCalls.getValue()) + 1);
                    throw new DispatchException();
                }
            }

            public void postDispatch(DispatchEvent event) {
                if (!postCall) {
                    postCall = true;
                    postCalls.setValue(((Integer) postCalls.getValue()) + 1);
                }

            }
        };

        DispatchEventListener listener2 = new DispatchEventListener() {
            private boolean preCall = false;
            private boolean postCall = false;

            public void preDispatch(DispatchEvent event)
                    throws DispatchException {
                if (!preCall) {
                    preCall = true;
                    preCalls.setValue(((Integer) preCalls.getValue()) + 1);
                }
            }

            public void postDispatch(DispatchEvent event) {
                if (!postCall) {
                    postCall = true;
                    postCalls.setValue(((Integer) postCalls.getValue()) + 1);
                }
            }
        };

        ViewHandler.addListener(listener);
        ViewHandler.addListener(listener2);

        final ValueContainer viewActivated = new ValueContainer();
        final ValueContainer parentCalled = new ValueContainer();
        viewActivated.setValue(false);
        parentCalled.setValue(false);

        AbstractView<ComponentContainer> view = new AbstractView<ComponentContainer>(
                new VerticalLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void activated(Object... params) {
                viewActivated.setValue(true);
            }
        };

        ViewItem item = ViewHandler.addView("test");
        item.setView(view);

        ViewHandler.activateView("test");
        // Parent not set

        ViewContainer container = new ViewContainer() {
            public void activate(AbstractView<?> view) {
                parentCalled.setValue(true);
            }
        };
        ViewHandler.setParent("test", container);
        ViewHandler.activateView("test");

        assertEquals(1, ((Integer) preCalls.getValue()).intValue());
        assertEquals(0, ((Integer) postCalls.getValue()).intValue());
        assertFalse((Boolean) viewActivated.getValue());
        assertFalse((Boolean) parentCalled.getValue());
    }

    @Test
    public void cancelDispatch() {
        final ValueContainer preCalls = new ValueContainer(0);
        final ValueContainer postCalls = new ValueContainer(0);

        DispatchEventListener listener = new DispatchEventListener() {
            private boolean preCall = false;
            private boolean postCall = false;

            public void preDispatch(DispatchEvent event)
                    throws DispatchException {
                if (!preCall) {
                    preCall = true;
                    preCalls.setValue(((Integer) preCalls.getValue()) + 1);
                }
            }

            public void postDispatch(DispatchEvent event) {
                if (!postCall) {
                    postCall = true;
                    postCalls.setValue(((Integer) postCalls.getValue()) + 1);
                }

            }
        };

        DispatchEventListener listener2 = new DispatchEventListener() {
            private boolean preCall = false;
            private boolean postCall = false;

            public void preDispatch(DispatchEvent event)
                    throws DispatchException {
                if (!preCall) {
                    preCall = true;
                    preCalls.setValue(((Integer) preCalls.getValue()) + 1);
                }
            }

            public void postDispatch(DispatchEvent event) {
                if (!postCall) {
                    postCall = true;
                    postCalls.setValue(((Integer) postCalls.getValue()) + 1);
                }
            }
        };

        ViewHandler.addListener(listener);
        ViewHandler.addListener(listener2);

        MockViewContainer parent = new MockViewContainer();
        ViewHandler.addView(MockView.class, parent);

        assertEquals(0, ((Integer) preCalls.getValue()).intValue());
        assertEquals(0, ((Integer) postCalls.getValue()).intValue());

        ViewHandler.activateView(MockView.class);

        assertEquals(2, ((Integer) preCalls.getValue()).intValue());
        assertEquals(2, ((Integer) postCalls.getValue()).intValue());
    }

    @Test
    public void uriChangedOnActivation() {
        MockViewContainer parent = new MockViewContainer();
        ViewHandler.addView(MockView.class, parent);
        ViewHandler.addUri("test", MockView.class);
        UriFragmentUtility util = ViewHandler.getUriFragmentUtil();

        assertNull(util.getFragment());
        ViewHandler.activateView(MockView.class, true);
        assertEquals("test", util.getFragment());
    }

    @Test
    public void uriNotChangedOnActivation() {
        MockViewContainer parent = new MockViewContainer();
        ViewHandler.addView(MockView.class, parent);
        ViewHandler.addUri("test", MockView.class);
        UriFragmentUtility util = ViewHandler.getUriFragmentUtil();

        assertNull(util.getFragment());
        ViewHandler.activateView(MockView.class, false);
        assertNull(util.getFragment());
    }

    @Test
    public void viewActivatedOnUriChange() {
        final ValueContainer viewActivated = new ValueContainer(false);

        AbstractView<ComponentContainer> view = new AbstractView<ComponentContainer>(
                new VerticalLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void activated(Object... params) {
                viewActivated.setValue(true);
            }
        };

        ViewItem item = ViewHandler.addView("test", new MockViewContainer());
        item.setView(view);

        // Add two uris for the same view
        ViewHandler.addUri("test", "test");
        ViewHandler.addUri("test2", "test");

        UriFragmentUtility util = ViewHandler.getUriFragmentUtil();
        util.setFragment("test", false);
        assertFalse((Boolean) viewActivated.getValue());
        // Clear the fragment so that a change will happen
        util.setFragment("clear", false);

        util.setFragment("test", true);
        assertTrue((Boolean) viewActivated.getValue());

        viewActivated.setValue(false);
        util.setFragment("test2", true);
        assertTrue((Boolean) viewActivated.getValue());

    }

    private class ValueContainer {

        private Object value;

        public ValueContainer() {
        }

        public ValueContainer(Object value) {
            this.value = value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
