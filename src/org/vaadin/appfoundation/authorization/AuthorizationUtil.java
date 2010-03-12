package org.vaadin.appfoundation.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A light weight authorization utility
 * 
 * @author Kim
 * 
 */
public class AuthorizationUtil {

    // Map between allowed resources and roles Map<Resource, List<Role>>
    private static Map<Object, List<Object>> allowed = new HashMap<Object, List<Object>>();

    // Map between denied resources and roles Map<Resource, List<Role>>
    private static Map<Object, List<Object>> denied = new HashMap<Object, List<Object>>();

    /**
     * Deny a role the access to a resource
     * 
     * @param role
     * @param resource
     */
    public static void deny(Role role, Resource resource) {
        if (role != null && resource != null && role.getIdentifier() != null
                && resource.getIdentifier() != null) {
            List<Object> deniedList = getDenied(resource);
            deniedList.add(role.getIdentifier());
            denied.put(resource.getIdentifier(), deniedList);
        }
    }

    /**
     * Allow a role the access to a resource
     * 
     * @param role
     * @param resource
     */
    public static void allow(Role role, Resource resource) {
        List<Object> allowedList = getAllowed(resource);
        allowedList.add(role.getIdentifier());
        allowed.put(resource.getIdentifier(), allowedList);
    }

    /**
     * Checks if the given role has access to the given resource.
     * 
     * @param role
     * @param resource
     * @return
     */
    public static boolean hasAccess(Role role, Resource resource) {
        if (role != null && resource != null && role.getIdentifier() != null
                && resource.getIdentifier() != null) {
            // Get the list of all allowed roles
            List<Object> allowedList = getAllowed(resource);
            // If the given role is in the list, then access is granted
            if (allowedList.contains(role.getIdentifier())) {
                return true;
            }

            // Get a list all all denied roles for this resource
            List<Object> deniedList = getDenied(resource);

            // If the resource doesn't have any allowed roles, then by default,
            // all roles should have access to it unless the given role is
            // explicitly denied access to the given resource.
            if (allowedList.size() == 0
                    && !deniedList.contains(role.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the list of all allowed roles for the given resource
     * 
     * @param resource
     * @return
     */
    private static List<Object> getAllowed(Resource resource) {
        List<Object> roles = null;
        if (allowed.containsKey(resource.getIdentifier())) {
            roles = allowed.get(resource.getIdentifier());
        } else {
            roles = new ArrayList<Object>();
            allowed.put(resource.getIdentifier(), roles);
        }

        return roles;
    }

    /**
     * Get the list of all denied roles for the given resource
     * 
     * @param resource
     * @return
     */
    private static List<Object> getDenied(Resource resource) {
        List<Object> roles = null;
        if (denied.containsKey(resource.getIdentifier())) {
            roles = denied.get(resource.getIdentifier());
        } else {
            roles = new ArrayList<Object>();
            denied.put(resource.getIdentifier(), roles);
        }

        return roles;
    }

}
