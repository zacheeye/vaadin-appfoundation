package org.vaadin.appfoundation.authentication.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

/**
 * Controller for managing user accounts
 * 
 * @author Kim
 * 
 */
public class UserUtil implements Serializable {

    private static final long serialVersionUID = 6394812141386916155L;

    // Define the minimum length for a username
    private static int minUsernameLength = 4;

    // Define the minimum length for a password
    private static int minPasswordLength = 8;

    /**
     * List of all possible error that can occur during registration
     * 
     * @author Kim
     * 
     */
    public static enum RegistrationMsg {
        TOO_SHORT_PASSWORD,
        TOO_SHORT_USERNAME,
        PASSWORDS_DO_NOT_MATCH,
        USERNAME_TAKEN,
        REGISTRATION_COMPLETED,
        ERROR
    }

    /**
     * List of error messages during updating a profile
     * 
     * @author Kim
     * 
     */
    public static enum ProfileMsg {
        TOO_SHORT_PASSWORD,
        PASSWORDS_DO_NOT_MATCH,
        PASSWORD_CHANGED,
        WRONG_PASSWORD,
        ERROR
    }

    /**
     * Get the user object with the given primary key
     * 
     * @param id
     * @return An instance of User
     */
    public static User getUser(Long id) {
        return FacadeFactory.getFacade().find(User.class, id);
    }

    /**
     * This method tries to register a new user
     * 
     * @param username
     *            Desired username
     * @param password
     *            Desired password
     * @param verifyPassword
     *            Verification of the desired password
     * @return A RegistrationMsg defining the final state of the process
     */
    public static RegistrationMsg registerUser(String username,
            String password, String verifyPassword) {
        // Make sure that the username and password fulfill the minimum size
        // requirements.
        if (username == null || username.length() < minUsernameLength) {
            return RegistrationMsg.TOO_SHORT_USERNAME;
        } else if (password == null || password.length() < minPasswordLength) {
            return RegistrationMsg.TOO_SHORT_PASSWORD;
        }
        // Make sure that the password is verified correctly
        else if (!password.equals(verifyPassword)) {
            return RegistrationMsg.PASSWORDS_DO_NOT_MATCH;
        }
        // Make sure the username is available
        else if (!checkUsernameAvailability(username)) {
            return RegistrationMsg.USERNAME_TAKEN;
        }

        // Everything is ok, create the user
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.generateHashedPassword(password));

        FacadeFactory.getFacade().store(user);

        return RegistrationMsg.REGISTRATION_COMPLETED;
    }

    /**
     * Checks if the given username is available.
     * 
     * @param username
     *            Desired username
     * @return Returns true if the username doesn't exist, false if it exists
     */
    private static boolean checkUsernameAvailability(String username) {
        String query = "SELECT u FROM User u WHERE u.username = :username";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);

        User user = FacadeFactory.getFacade().find(query, parameters);

        return user != null ? false : true;
    }

    /**
     * Change the password of the given user. Verifies that the new password
     * meets all the set conditions.
     * 
     * @param user
     *            User object for which we want to change the password
     * @param currentPassword
     *            Current password
     * @param newPassword
     *            Desired new password
     * @param verifiedNewPassword
     *            New password verification
     * @return ProfileMsg defining the final state of the process
     */
    public static ProfileMsg changePassword(User user, String currentPassword,
            String newPassword, String verifiedNewPassword) {

        // Verify that the current password is correct
        if (!PasswordUtil.verifyPassword(user, currentPassword)) {
            return ProfileMsg.WRONG_PASSWORD;
        }

        // Check the new password's constraints
        if (newPassword == null || newPassword.length() < minPasswordLength) {
            return ProfileMsg.TOO_SHORT_PASSWORD;
        } else if (!newPassword.equals(verifiedNewPassword)) {
            return ProfileMsg.PASSWORDS_DO_NOT_MATCH;
        }

        // Password is ok, hash it and change it
        user.setPassword(PasswordUtil.generateHashedPassword(newPassword));
        // Store the user
        FacadeFactory.getFacade().store(user);

        return ProfileMsg.PASSWORD_CHANGED;
    }

    /**
     * Stores to the database any changes made to the user object
     * 
     * @param user
     *            An instance of the User object
     */
    public static void storeUser(User user) {
        FacadeFactory.getFacade().store(user);
    }

    /**
     * Set the properties for the UserUtil. Minimum lengths for the username and
     * password must be defined.
     * 
     * @param properties
     *            Configuration properties
     */
    public static void setProperties(Properties properties) {
        // Make sure properties is not null
        if (properties == null) {
            throw new IllegalArgumentException("Properties may not be null");
        }

        // Make sure we have both required properties. Check them separately so
        // we can give more detailed messages to the user
        if (!properties.containsKey("username.length.min")) {
            throw new IllegalArgumentException(
                    "Properties must contain username.length.min");
        }

        if (!properties.containsKey("password.length.min")) {
            throw new IllegalArgumentException(
                    "Properties must contain password.length.min");
        }

        // Get the property values
        String minUsernameLengthString = properties
                .getProperty("username.length.min");
        String minPasswordLengthString = properties
                .getProperty("password.length.min");

        // Temporarily store the new lengths in these variables. We want to
        // change both values in one "transaction", meaning either both values
        // are change or neither are.
        int newUsernameLength = 0;
        int newPasswordLength = 0;

        // Make sure the values are integers
        try {
            newUsernameLength = Integer.valueOf(minUsernameLengthString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "username.length.min must be an integer");
        }

        try {
            newPasswordLength = Integer.valueOf(minPasswordLengthString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "password.length.min must be an integer");
        }

        // Everything is ok, now we can define the new values
        minUsernameLength = newUsernameLength;
        minPasswordLength = newPasswordLength;

    }

    /**
     * Returns the minimum length of a username
     * 
     * @return Minimum username length
     */
    public static int getMinUsernameLength() {
        return minUsernameLength;
    }

    /**
     * Returns the minimum length of a password
     * 
     * @return Minimum password length
     */
    public static int getMinPasswordLength() {
        return minPasswordLength;
    }

}
