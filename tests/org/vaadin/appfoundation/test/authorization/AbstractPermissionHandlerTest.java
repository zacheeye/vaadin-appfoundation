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

    @Test(expected = IllegalArgumentException.class)
    public void denyNullRole() {
        PermissionHandler pm = getPermissionHandler();
        pm.deny(null, "test", createResource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void denyNullResource() {
        PermissionHandler pm = getPermissionHandler();
        pm.deny(createRole(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void allowNullRole() {
        PermissionHandler pm = getPermissionHandler();
        pm.allow(null, "test", createResource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void allowNullResource() {
        PermissionHandler pm = getPermissionHandler();
        pm.allow(createRole(), "test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void denyAllNullRole() {
        PermissionHandler pm = getPermissionHandler();
        pm.denyAll(null, createResource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void denyAllNullResource() {
        PermissionHandler pm = getPermissionHandler();
        pm.denyAll(createRole(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void allowAllNullRole() {
        PermissionHandler pm = getPermissionHandler();
        pm.allowAll(null, createResource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void allowAllNullResource() {
        PermissionHandler pm = getPermissionHandler();
        pm.allowAll(createRole(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hasAccessNullRole() {
        PermissionHandler pm = getPermissionHandler();
        pm.hasAccess(null, "test", createResource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void hasAccessNullResource() {
        PermissionHandler pm = getPermissionHandler();
        pm.hasAccess(createRole(), "test", null);
    }

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
    public void allowAll() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.allowAll(role, resource);
        pm.allow(role2, "write", resource);

        assertTrue(pm.hasAccess(role, "read", resource));
        assertTrue(pm.hasAccess(role, "test", resource));
        assertTrue(pm.hasAccess(role, "write", resource));
        pm.deny(role, "write", resource);
        assertFalse(pm.hasAccess(role, "write", resource));
    }

    @Test
    public void denyAll() {
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.denyAll(role, resource);
        pm.deny(role2, "write", resource);

        assertFalse(pm.hasAccess(role, "read", resource));
        assertFalse(pm.hasAccess(role, "test", resource));
        assertFalse(pm.hasAccess(role, "write", resource));
        pm.allow(role, "write", resource);
        assertTrue(pm.hasAccess(role, "write", resource));
    }

    @Test
    public void combinationAllowed() {
        // If one is denied, then others are allowed
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.deny(role2, "test", resource);
        assertTrue(pm.hasAccess(role, "test", resource));
    }

    @Test
    public void combinationDenied() {
        // If one is allowed, then others are denied
        Role role = createRole();
        Role role2 = createRole();
        Resource resource = createResource();

        PermissionHandler pm = getPermissionHandler();
        pm.allow(role2, "test", resource);
        assertFalse(pm.hasAccess(role, "test", resource));
    }
}
