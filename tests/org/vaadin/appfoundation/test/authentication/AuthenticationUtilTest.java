package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.authentication.AuthenticationMessage;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.util.AuthenticationUtil;
import org.vaadin.appfoundation.authentication.util.PasswordUtil;
import org.vaadin.appfoundation.authentication.util.AuthenticationUtil.AFAuthenticationMessage;
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

    @Test
    public void authenticateNoUsername() {
        AuthenticationMessage msg = AuthenticationUtil
                .authenticate(null, "foo");
        assertEquals(AFAuthenticationMessage.INVALID_CREDENTIALS, msg);
    }

    @Test
    public void authenticateNoPassword() {
        AuthenticationMessage msg = AuthenticationUtil
                .authenticate("foo", null);
        assertEquals(AFAuthenticationMessage.INVALID_CREDENTIALS, msg);
    }

    @Test
    public void authenticationUserNotFound() {
        AuthenticationMessage msg = AuthenticationUtil.authenticate("foo",
                "foo");
        assertEquals(AFAuthenticationMessage.INVALID_CREDENTIALS, msg);
    }

    @Test
    public void authenticationInvalidPassword() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        FacadeFactory.getFacade().store(user);

        AuthenticationMessage msg = AuthenticationUtil.authenticate("test",
                "foo");
        assertEquals(AFAuthenticationMessage.INVALID_CREDENTIALS, msg);
    }

    @Test
    public void authenticate() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(PasswordUtil.generateHashedPassword("foobar"));

        FacadeFactory.getFacade().store(user);
        AuthenticationMessage msg = AuthenticationUtil.authenticate("test",
                "foobar");
        assertEquals(AFAuthenticationMessage.AUTH_SUCCESSFUL, msg);
    }

}
