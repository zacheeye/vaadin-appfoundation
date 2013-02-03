package org.vaadin.appfoundation.authorization;

import java.io.Serializable;
import java.util.Set;

import com.apple.eawt.Application;
import com.vaadin.ui.UI;

/**
 * Utility class for invoking permission manager's method in a static way.
 * 
 * @author Kim
 * 
 */
public class Permissions implements Serializable {

    private static final long serialVersionUID = -7370158934115935499L;

    private final PermissionManager pm;

    /**
     * Constructor.
     * 
     * @param ui
     *            Application instance for which this object belongs
     * @param manager
     *            The permission manager to be used
     */
    public Permissions(UI ui, PermissionManager manager) {
        if (ui == null) {
            throw new IllegalArgumentException("Application must be set");
        }

        if (manager == null) {
            throw new IllegalArgumentException("PermissionManager must be set");
        }

        ui.getSession().setAttribute(ui.getId() + "-permissions", this);
        pm = manager;
    }

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
     * 
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void allow(Role role, String action, Resource resource) {
        getCurrent().pm.allow(role, action, resource);
    }

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
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void allowAll(Role role, Resource resource) {
        getCurrent().pm.allowAll(role, resource);
    }

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
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void deny(Role role, String action, Resource resource) {
        getCurrent().pm.deny(role, action, resource);
    }

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
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void denyAll(Role role, Resource resource) {
        getCurrent().pm.denyAll(role, resource);
    }

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
     * @return True if role has access to the given action in the given resource
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static boolean hasAccess(Role role, String action, Resource resource) {
        return getCurrent().pm.hasAccess(role, action, resource);
    }

    /**
     * Checks if the given roles has the permission to perform the given action
     * for the given resource. If no restrictions have been set for the
     * action-resource pair, then access is granted.
     * 
     * @param roles
     *            A set of roles for which we want to check the permissions
     * @param action
     *            The identifier for the action for which we want to check the
     *            permissions. This value should be null if we want to check
     *            default permissions.
     * @param resource
     *            The resource for which the permission is being requested
     * @return True if role has access to the given action in the given resource
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static boolean hasAccess(Set<Role> roles, String action,
            Resource resource) {
        return getCurrent().pm.hasAccess(roles, action, resource);
    }

    /**
     * Initializes the {@link PermissionManager} for the given
     * {@link Application}
     * 
     * @param ui
     */
    public static void initialize(UI ui,
            PermissionManager manager) {
        if (ui == null) {
            throw new IllegalArgumentException("Application may not be null");
        }

        if (manager == null) {
            throw new IllegalArgumentException("PermissionManager must be set");
        }

        new Permissions(ui, manager);
    }

    /**
     * Removes any permissions set for the given role, for the given action in
     * the given resource.
     * 
     * @param role
     *            The role whose permissions are being removed
     * @param action
     *            The identifier for the action
     * @param resource
     *            The resource from which permissions are removed
     * 
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void removePermission(Role role, String action,
            Resource resource) {
        getCurrent().pm.removePermission(role, action, resource);
    }

    /**
     * Removes the ALL permission set for the given role in the given resource.
     * 
     * @param role
     *            The role whose permissions are being removed
     * @param resource
     *            The resource from which permissions are removed
     * 
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void removeAllPermission(Role role, Resource resource) {
        getCurrent().pm.removeAllPermission(role, resource);
    }

    /**
     * Removes all permissions set for the given role in the given resource.
     * 
     * @param role
     *            The role whose permissions are being removed
     * @param resource
     *            The resource from which permissions are removed
     * 
     * @throws IllegalArgumentException
     *             If either role or resource is null
     */
    public static void removeAllPermissions(Role role, Resource resource) {
        getCurrent().pm.removeAllPermissions(role, resource);
    }
    
	private static Permissions getCurrent() {
		UI ui = UI.getCurrent();
		Permissions current = (Permissions) ui.getSession().getAttribute(
				ui.getId() + "-permissions");
		return current;
	}


}
