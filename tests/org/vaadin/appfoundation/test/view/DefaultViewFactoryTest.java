package org.vaadin.appfoundation.test.view;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.vaadin.appfoundation.view.DefaultViewFactory;

public class DefaultViewFactoryTest {

    @Test
    public void initView() {
        DefaultViewFactory factory = new DefaultViewFactory();
        assertNull(factory.initView("foobar"));
        assertTrue(factory.initView(MockView.class) instanceof MockView);
    }
}
