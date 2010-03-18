package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.vaadin.appfoundation.authentication.data.User;

public class UserTest {

    @Test
    public void username() {
        User user = new User();

        user.setUsername("test");
        assertEquals("test", user.getUsername());
        user.setUsername("test2");
        assertEquals("test2", user.getUsername());
    }

    @Test
    public void password() {
        User user = new User();

        user.setPassword("test");
        assertEquals("test", user.getPassword());
        user.setPassword("test2");
        assertEquals("test2", user.getPassword());
    }

    @Test
    public void email() {
        User user = new User();

        user.setEmail("test");
        assertEquals("test", user.getEmail());
        user.setEmail("test2");
        assertEquals("test2", user.getEmail());
    }

    @Test
    public void name() {
        User user = new User();

        user.setName("test");
        assertEquals("test", user.getName());
        user.setName("test2");
        assertEquals("test2", user.getName());
    }

}
