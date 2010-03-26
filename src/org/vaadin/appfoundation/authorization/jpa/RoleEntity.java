package org.vaadin.appfoundation.authorization.jpa;

import java.util.Set;

import javax.persistence.Entity;

import org.vaadin.appfoundation.authorization.Role;
import org.vaadin.appfoundation.persistence.data.AbstractPojo;

/**
 * A JPA based implementation for Role. This implementation is an entity, so it
 * can be stored to a database via the persistence module.
 * 
 * @author Kim
 * 
 */
@Entity
public class RoleEntity extends AbstractPojo implements Role {

    private static final long serialVersionUID = -3208015070809046113L;

    /**
     * {@inheritDoc}
     */
    public void addRole(Role role) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public String getIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Role> getRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(Role role) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void setRoles(Set<Role> roles) {
        // TODO Auto-generated method stub

    }

}
