package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.exceptions.InvalidCredentialsException;
import org.vaadin.appfoundation.authentication.util.AuthenticationUtil;
import org.vaadin.appfoundation.authentication.util.PasswordUtil;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

public class AuthenticationUtilTest {

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        FacadeFactory.registerFacade("default", true);
    }

    @After
    public void tearDown() {
        FacadeFactory.clear();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void authenticateNoUsername() throws InvalidCredentialsException {
        AuthenticationUtil.authenticate(null, "foo");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void authenticateNoPassword() throws InvalidCredentialsException {
        AuthenticationUtil.authenticate("foo", null);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void authenticationUserNotFound() throws InvalidCredentialsException {
        AuthenticationUtil.authenticate("foo", "foo");
    }

    @Test(expected = InvalidCredentialsException.class)
    public void authenticationInvalidPassword()
            throws InvalidCredentialsException {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        FacadeFactory.getFacade().store(user);

        AuthenticationUtil.authenticate("test", "foo");
    }

    @Test
    public void authenticate() throws InvalidCredentialsException {
        User user = new User();
        user.setUsername("test");
        user.setPassword(PasswordUtil.generateHashedPassword("foobar"));

        FacadeFactory.getFacade().store(user);
        User authenticatedUser = AuthenticationUtil.authenticate("test",
                "foobar");
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
        assertEquals(user.getPassword(), authenticatedUser.getPassword());
        assertEquals(user.getId(), authenticatedUser.getId());
    }

    @Test
    public void incrementFailedLoginAttempts() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(PasswordUtil.generateHashedPassword("foobar"));
        assertEquals(0, user.getFailedLoginAttempts());

        FacadeFactory.getFacade().store(user);
        try {
            AuthenticationUtil.authenticate("test", "test");
        } catch (InvalidCredentialsException e) {
            // This is expected
        }

        user = FacadeFactory.getFacade().find(User.class, user.getId());
        assertEquals(1, user.getFailedLoginAttempts());
    }

    @Test
    public void clearFailedLoginAttempts() throws InvalidCredentialsException {
        User user = new User();
        user.setUsername("test");
        user.setPassword(PasswordUtil.generateHashedPassword("foobar"));
        assertEquals(0, user.getFailedLoginAttempts());

        FacadeFactory.getFacade().store(user);
        try {
            AuthenticationUtil.authenticate("test", "test");
        } catch (InvalidCredentialsException e) {
            // This is expected
        }
        AuthenticationUtil.authenticate("test", "foobar");
        user = FacadeFactory.getFacade().find(User.class, user.getId());
        assertEquals(0, user.getFailedLoginAttempts());
    }

}
