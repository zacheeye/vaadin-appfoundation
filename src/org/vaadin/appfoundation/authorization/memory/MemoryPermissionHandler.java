package org.vaadin.appfoundation.authorization.memory;

import org.vaadin.appfoundation.authorization.PermissionHandler;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;

/**
 * An implementation of the PermissionHandler which keeps all the permission
 * details in memory. No permission are persisted.
 * 
 * @author Kim
 * 
 */
public class MemoryPermissionHandler implements PermissionHandler {

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
