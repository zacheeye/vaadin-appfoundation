package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.authentication.SessionHandler;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.test.MockApplication;

public class SessionHandlerTest {

    private MockApplication application;

    @Before
    public void setUp() {
        // Create a new instance of the MockApplication
        application = new MockApplication();
        // Initialize the SessionHandler class with the MockApplication
        new SessionHandler(application);
    }

    @Test
    public void user() {
        User user = new User();
        assertNull(SessionHandler.get());

        SessionHandler.setUser(user);
        assertEquals(user, SessionHandler.get());
    }

    @Test
    public void logout() {
        User user = new User();
        SessionHandler.setUser(user);
        SessionHandler.logout();
        assertNull(SessionHandler.get());
    }
}
