package org.vaadin.appfoundation.test.authorization;

import java.util.Set;
import java.util.UUID;

import org.vaadin.appfoundation.authorization.Role;

public class RoleMock implements Role {

    private final String id;

    public RoleMock() {
        id = UUID.randomUUID().toString();
    }

    public String getIdentifier() {
        return id;
    }

    public void addRole(Role role) {
        // TODO Auto-generated method stub

    }

    public Set<Role> getRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeRole(Role role) {
        // TODO Auto-generated method stub

    }

    public void setRoles(Set<Role> roles) {
        // TODO Auto-generated method stub

    }

}
