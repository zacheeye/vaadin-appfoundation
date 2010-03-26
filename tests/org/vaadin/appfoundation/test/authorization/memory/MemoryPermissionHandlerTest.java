package org.vaadin.appfoundation.test.authorization.memory;

import org.vaadin.appfoundation.authorization.PermissionHandler;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;
import org.vaadin.appfoundation.authorization.memory.MemoryPermissionHandler;
import org.vaadin.appfoundation.test.authorization.AbstractPermissionHandlerTest;
import org.vaadin.appfoundation.test.authorization.ResourceMock;
import org.vaadin.appfoundation.test.authorization.RoleMock;

public class MemoryPermissionHandlerTest extends AbstractPermissionHandlerTest {

    @Override
    public Resource createResource() {
        return new ResourceMock();
    }

    @Override
    public Role createRole() {
        return new RoleMock();
    }

    @Override
    public PermissionHandler getPermissionHandler() {
        return new MemoryPermissionHandler();
    }

}
