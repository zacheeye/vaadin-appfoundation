package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.util.PasswordUtil;

public class PasswordUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void setPropertiesWithNull() {
        PasswordUtil.setProperties(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPropertiesMissingProperty() {
        PasswordUtil.setProperties(new Properties());
    }

    @Test
    public void setProperties() {
        Properties properties = new Properties();
        properties.setProperty("password.salt", "test");
        PasswordUtil.setProperties(properties);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setPropertiesTwice() {
        // Properties is already set
        Properties properties = new Properties();
        properties.setProperty("password.salt", "foobar");
        PasswordUtil.setProperties(properties);
    }

    @Test
    public void verifyPasswordNullUser() {
        assertFalse(PasswordUtil.verifyPassword(null, "foo"));
    }

    @Test
    public void verifyPasswordNullPassword() {
        assertFalse(PasswordUtil.verifyPassword(new User(), null));
    }

    @Test
    public void verifyPasswordWrongPassword() {
        User user = new User();
        // Note that the password should be hashed for this to pass
        user.setPassword("test");
        assertFalse(PasswordUtil.verifyPassword(user, "test"));
    }

    @Test
    public void verifyPassword() {
        User user = new User();
        // Hashed value of "foobar"+"test" (the salt value)
        user.setPassword("61e38e2b77827e10777ee8f1a138b7cfb1eb895");
        assertTrue(PasswordUtil.verifyPassword(user, "foobar"));
    }

}
