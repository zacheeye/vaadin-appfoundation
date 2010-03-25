package org.vaadin.appfoundation.test.authorization;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.vaadin.appfoundation.authorization.AuthorizationUtil;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;

public class AuthorizationUtilTest {

    @Test
    public void deny() {
        Role role = new RoleMock();
        Resource resource = new ResourceMock();

        AuthorizationUtil.deny(role, resource);
        assertFalse(AuthorizationUtil.hasAccess(role, resource));
    }

    @Test
    public void allow() {

    }

}
