package org.vaadin.appfoundation.test.authorization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.vaadin.appfoundation.authorization.PermissionHandler;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;

public abstract class AbstractPermissionHandlerTest {

    public abstract Role createRole();

    public abstract Resource createResource();

    public abstract PermissionHandler getPermissionHandler();

    @Test
    public void deny() {
        Role role = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        assertTrue(pm.hasAccess(role, "test", resource));

        pm.deny(role, "test", resource);
        assertFalse(pm.hasAccess(role, "test", resource));
    }

    @Test
    public void allow() {
        Role role = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.deny(role, "test", resource);
        assertFalse(pm.hasAccess(role, "test", resource));
        pm.allow(role, "test", resource);
        assertTrue(pm.hasAccess(role, "test", resource));
    }

    @Test
    public void allowDefault() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.allowDefault(role, resource);
        pm.allow(role2, "write", resource);

        assertTrue(pm.hasAccess(role, "read", resource));
        assertTrue(pm.hasAccess(role, "test", resource));
        assertFalse(pm.hasAccess(role, "write", resource));
    }

    @Test
    public void allowAll() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.allowDefault(role, resource);
        pm.allow(role2, "write", resource);

        assertTrue(pm.hasAccess(role, "read", resource));
        assertTrue(pm.hasAccess(role, "test", resource));
        assertTrue(pm.hasAccess(role, "write", resource));
        pm.deny(role, "write", resource);
        assertFalse(pm.hasAccess(role, "write", resource));
    }

    @Test
    public void denyDefault() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.denyDefault(role, resource);
        pm.deny(role2, "write", resource);

        assertFalse(pm.hasAccess(role, "read", resource));
        assertFalse(pm.hasAccess(role, "test", resource));
        assertTrue(pm.hasAccess(role, "write", resource));
    }

    @Test
    public void denyAll() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.denyDefault(role, resource);
        pm.deny(role2, "write", resource);

        assertFalse(pm.hasAccess(role, "read", resource));
        assertFalse(pm.hasAccess(role, "test", resource));
        assertFalse(pm.hasAccess(role, "write", resource));
        pm.deny(role, "write", resource);
        assertTrue(pm.hasAccess(role, "write", resource));
    }
}
