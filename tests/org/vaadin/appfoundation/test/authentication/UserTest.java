package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.*;

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
    
    @Test
    public void incrementFailedLoginAttempts() {
        User user = new User();
        assertEquals(0, user.getFailedLoginAttempts());
        
        user.incrementFailedLoginAttempts();
        assertEquals(1, user.getFailedLoginAttempts());
        
        user.incrementFailedLoginAttempts();
        user.incrementFailedLoginAttempts();
        assertEquals(3, user.getFailedLoginAttempts());
    }
    
    @Test
    public void clearFailedLoginAttempts() {
        User user = new User();
        user.incrementFailedLoginAttempts();
        user.clearFailedLoginAttempts();
        assertEquals(0, user.getFailedLoginAttempts());
    }
    
    @Test
    public void reasonForLockedAccount() {
        User user = new User();

        user.setReasonForLockedAccount("test");
        assertEquals("test", user.getReasonForLockedAccount());
        user.setReasonForLockedAccount("test2");
        assertEquals("test2", user.getReasonForLockedAccount());
    }
    
    @Test
    public void accountLocked() {
        User user = new User();

        user.setAccountLocked(false);
        assertFalse(user.isAccountLocked());
        user.setAccountLocked(true);
        assertTrue(user.isAccountLocked());
    }

}
