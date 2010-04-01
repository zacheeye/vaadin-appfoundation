package org.vaadin.appfoundation.test.authorization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.authorization.Permissions;
import org.vaadin.appfoundation.authorization.Role;
import org.vaadin.appfoundation.test.MockApplication;

public class PermissionsTest {

    private MockApplication application;
    private Permissions permissions = null;
    private PermissionManagerMock manager;

    @Before
    public void setUp() {
        application = new MockApplication();
        manager = new PermissionManagerMock();
        permissions = new Permissions(application, manager);
        permissions.transactionStart(application, null);
    }

    @After
    public void tearDown() {
        permissions.transactionEnd(application, null);
        permissions = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullApplication() {
        new Permissions(null, manager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullManager() {
        new Permissions(application, null);
    }

    @Test
    public void allow() {
        assertFalse(manager.wasInvoked("allow"));
        Permissions.allow(null, null, null);
        assertTrue(manager.wasInvoked("allow"));
    }

    @Test
    public void allowAll() {
        assertFalse(manager.wasInvoked("allowAll"));
        Permissions.allowAll(null, null);
        assertTrue(manager.wasInvoked("allowAll"));
    }

    @Test
    public void deny() {
        assertFalse(manager.wasInvoked("deny"));
        Permissions.deny(null, null, null);
        assertTrue(manager.wasInvoked("deny"));
    }

    @Test
    public void denyAll() {
        assertFalse(manager.wasInvoked("denyAll"));
        Permissions.denyAll(null, null);
        assertTrue(manager.wasInvoked("denyAll"));
    }

    @Test
    public void hasAccessObject() {
        assertFalse(manager.wasInvoked("hasAccessObject"));
        Permissions.hasAccess((Role) null, null, null);
        assertTrue(manager.wasInvoked("hasAccessObject"));
    }

    @Test
    public void hasAccessSet() {
        assertFalse(manager.wasInvoked("hasAccessSet"));
        Permissions.hasAccess((Set<Role>) null, null, null);
        assertTrue(manager.wasInvoked("hasAccessSet"));
    }

}
