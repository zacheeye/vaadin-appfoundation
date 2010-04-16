package org.vaadin.appfoundation.authentication.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.vaadin.appfoundation.authentication.data.User;

/**
 * Utility class containing useful helper methods related to passwords.
 * 
 * @author Kim
 * 
 */
public class PasswordUtil implements Serializable {

    private static final long serialVersionUID = 7823991334001022227L;

    // Store the password salt in a static variable
    private static String salt = null;

    /**
     * Get the salt value for the passwords
     * 
     * @return
     */
    protected static String getSalt() {
        // Check if the salt has been set. If not, then create a default salt
        // value.
        String systemSalt = System.getProperty("authentication.password.salt");
        if (salt != null && !salt.equals(systemSalt)) {
            throw new UnsupportedOperationException(
                    "Password salt is already set");
        }

        if (salt == null && systemSalt != null) {
            salt = systemSalt;
        }

        if (salt == null) {
            salt = ")%gersK43q5)=%3qiyt34389py43pqhgwer8l9";
            System.setProperty("authentication.password.salt", salt);
        }

        return salt;
    }

    /**
     * Set the properties for the PasswordUtil. The properties must contain the
     * password.salt -property.
     * 
     * @param properties
     * @deprecated Use System.setProperties instead
     * 
     */
    @Deprecated
    public static void setProperties(Properties properties) {
        // Make sure we don't get null values
        if (properties == null) {
            throw new IllegalArgumentException("Properties may not be null");
        }

        // Make sure we have the needed property
        if (!properties.containsKey("password.salt")) {
            throw new IllegalArgumentException(
                    "Properties must contain the password.salt -property");
        }

        // Salt should only be defined once. If it is already defined, then an
        // exception should be thrown
        if (salt == null) {
            salt = properties.getProperty("password.salt");
            System.setProperty("authentication.password.salt", salt);
        } else {
            throw new UnsupportedOperationException(
                    "Password salt is already set");
        }
    }

    /**
     * Verify if the given password (unhashed) matches with the user's password
     * 
     * @param user
     *            User to whome's password we are comparing
     * @param password
     *            The unhashed password we are comparing
     * @return Returns true if passwords match, otherwise false
     */
    public static boolean verifyPassword(User user, String password) {
        // Return null if either the username or password is null
        if (user == null || password == null) {
            return false;
        }

        // Hash the generated password
        String hashedPassword = generateHashedPassword(password);

        // Check if the password matches with the one stored in the User object
        if (user.getPassword().equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates a hashed password of the given string.
     * 
     * @param password
     *            String which is to be hashed
     * @return Hashed password
     */
    public static String generateHashedPassword(String password) {
        StringBuffer hashedPassword = new StringBuffer();

        // Get a byte array of the password concatenated with the password salt
        // value
        byte[] defaultBytes = (password + getSalt()).getBytes();
        try {
            // Perform the hashing with SHA
            MessageDigest algorithm = MessageDigest.getInstance("SHA");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                hashedPassword.append(Integer
                        .toHexString(0xFF & messageDigest[i]));
            }
        } catch (NoSuchAlgorithmException nsae) {

        }

        return hashedPassword.toString();
    }
}
