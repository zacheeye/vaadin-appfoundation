package org.vaadin.appfoundation.test.authorization;

import java.util.UUID;

import org.vaadin.appfoundation.authorization.Role;

public class RoleMock implements Role {

    private String id;

    public RoleMock() {
        id = UUID.randomUUID().toString();
    }

    public Object getIdentifier() {
        return id;
    }

}
