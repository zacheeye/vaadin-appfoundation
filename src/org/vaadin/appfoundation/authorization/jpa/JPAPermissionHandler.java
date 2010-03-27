package org.vaadin.appfoundation.authorization.jpa;

import org.vaadin.appfoundation.authorization.PermissionManager;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;

/**
 * JPA based implementation for the PermissionHandler interface. This class will
 * communicate directly with the database, where it will store all defined
 * permissions.
 * 
 * @author Kim
 * 
 */
public class JPAPermissionHandler implements PermissionManager {

    /**
     * {@inheritDoc}
     */
    public void allow(Role role, String action, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void allow(Role role, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void allowAll(Role role, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void allowDefault(Role role, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void deny(Role role, String action, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void denyAll(Role role, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void denyDefault(Role role, Resource resource) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAccess(Role role, String action, Resource resource) {
        // TODO Auto-generated method stub
        return false;
    }

}
