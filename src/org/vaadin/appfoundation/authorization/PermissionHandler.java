package org.vaadin.appfoundation.authorization;

public interface PermissionHandler {

    /**
     * Grants the given role the permission to perform the given action for the
     * given resource.
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param action
     *            The identifier for the action
     * @param resource
     *            The resource to which the permission applies
     */
    public void allow(Role role, String action, Resource resource);

    /**
     * <p>
     * Grants the given role to perform <b>any action</b> for the given resource
     * if and only if no permissions have been explicitly set for the requested
     * action.
     * </p>
     * 
     * <p>
     * Example:<br />
     * allowDefault(role, resource);<br />
     * allow(anotherRole, "write", resource);
     * 
     * hasAccess(role, "read", resource) - returns true<br />
     * hasAccess(role, "close", resource) - returns true<br />
     * hasAccess(role, "write", resource) - returns false
     * </p>
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param resource
     *            The resource to which the permission applies
     */
    public void allowDefault(Role role, Resource resource);

    /**
     * <p>
     * Grants the given role to perform <b>any action</b> for the given
     * resource.
     * </p>
     * 
     * <p>
     * Example:<br />
     * allowAll(role, resource);<br />
     * allow(anotherRole, "write", resource);
     * </p>
     * <p>
     * hasAccess(role, "read", resource) - returns true<br />
     * hasAccess(role, "close", resource) - returns true<br />
     * hasAccess(role, "write", resource) - returns true<br />
     * deny(role, "write", resource);<br />
     * hasAccess(role, "write", resource) - returns false
     * </p>
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param resource
     *            The resource to which the permission applies
     */
    public void allowAll(Role role, Resource resource);

    /**
     * Grants the given role the permission to perform
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param resource
     *            The resource to which the permission applies
     */
    public void allow(Role role, Resource resource);

    /**
     * Denies the given role the permission to perform the given action for the
     * given resource.
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param action
     *            The identifier for the action
     * @param resource
     *            The resource to which the permission applies
     */
    public void deny(Role role, String action, Resource resource);

    /**
     * <p>
     * Denies the given role to perform <b>any action</b> for the given resource
     * if and only if no permissions have been explicitly set for the requested
     * action.
     * </p>
     * 
     * <p>
     * Example:<br />
     * denyDefault(role, resource);<br />
     * deny(anotherRole, "write", resource);
     * 
     * hasAccess(role, "read", resource) - returns false<br />
     * hasAccess(role, "close", resource) - returns false<br />
     * hasAccess(role, "write", resource) - returns true
     * </p>
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param resource
     *            The resource to which the permission applies
     */
    public void denyDefault(Role role, Resource resource);

    /**
     * <p>
     * Denies the given role to perform <b>any action</b> for the given
     * resource.
     * </p>
     * 
     * <p>
     * Example:<br />
     * denyAll(role, resource);<br />
     * deny(anotherRole, "write", resource);
     * </p>
     * <p>
     * hasAccess(role, "read", resource) - returns false<br />
     * hasAccess(role, "close", resource) - returns false<br />
     * hasAccess(role, "write", resource) - returns false<br />
     * allow(role, "write", resource);<br />
     * hasAccess(role, "write", resource) - returns true
     * </p>
     * 
     * @param role
     *            The role which is being assigned the permission
     * @param resource
     *            The resource to which the permission applies
     */
    public void denyAll(Role role, Resource resource);

    /**
     * Checks if the given role has the permission to perform the given action
     * for the given resource. If no restrictions have been set for the
     * action-resource pair, then the role is granted access.
     * 
     * @param role
     *            The Role for which we want to check the permissions
     * @param action
     *            The identifier for the action for which we want to check the
     *            permissions. This value should be null if we want to check
     *            default permissions.
     * @param resource
     *            The resource for which the permission is being requested
     * @return
     */
    public boolean hasAccess(Role role, String action, Resource resource);

}
