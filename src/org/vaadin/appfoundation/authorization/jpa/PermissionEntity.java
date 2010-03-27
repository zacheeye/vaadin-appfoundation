package org.vaadin.appfoundation.authorization.jpa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.vaadin.appfoundation.persistence.data.AbstractPojo;

/**
 * This entity class is used for persisting permissions used by the
 * JPAPermissionHandler.
 * 
 * @author Kim
 * 
 */
@Entity
public class PermissionEntity extends AbstractPojo {

    private static final long serialVersionUID = 3895345053504819128L;

    @Enumerated(EnumType.STRING)
    private PermissionType type;

    @ManyToOne
    private RoleEntity role;

    private String resource;

    private String action;

    private boolean allPermissions = false;

    /**
     * Default constructor.
     */
    public PermissionEntity() {

    }

    /**
     * Alternative constructor, sets the permission type upon initialization.
     * 
     * @param type
     *            The permission's type
     */
    public PermissionEntity(PermissionType type) {
        this.type = type;
    }

    /**
     * Sets the type of the permission (allow/deny)
     * 
     * @param type
     *            The permission's type
     */
    public void setType(PermissionType type) {
        this.type = type;
    }

    /**
     * Returns the type of the permission (allow/deny)
     * 
     * @return The permission's type
     */
    public PermissionType getType() {
        return type;
    }

    /**
     * Get the role for which this permission is being applied on.
     * 
     * @return The role for which the permission is being set
     */
    public RoleEntity getRole() {
        return role;
    }

    /**
     * Set the role for which this permission is being applied on.
     * 
     * @param role
     *            The role for which the permission is being set
     */
    public void setRole(RoleEntity role) {
        this.role = role;
    }

    /**
     * Get the resource's identifier for which this permission is being applied
     * on.
     * 
     * @return The resource's identifier
     */
    public String getResource() {
        return resource;
    }

    /**
     * Set the resource's identifier for which this permission is being applied
     * on.
     * 
     * @param resource
     *            The resource's identifier
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Get the action for which this permission is being applied on.
     * 
     * @return The action parameter for this permission
     */
    public String getAction() {
        return action;
    }

    /**
     * Set the action for which this permission is being applied on.
     * 
     * @param action
     *            The action parameter for this permission
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Should this permission be applied for all the actions in the given
     * resource for the given role.
     * 
     * @return True if this permission is being applied for all action, false if
     *         not
     */
    public boolean isAllPermissions() {
        return allPermissions;
    }

    /**
     * Defines if this permission should be applied for all the actions in the
     * given resource for the given role.
     * 
     * @param allPermissions
     *            True if this permission is being applied for all action, false
     *            if not
     */
    public void setAllPermissions(boolean allPermissions) {
        this.allPermissions = allPermissions;
    }

}
