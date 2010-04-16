package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authentication.util.PasswordUtil;

public class PasswordUtilTest {

    @After
    public void tearDown() throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = PasswordUtil.class.getDeclaredField("salt");
        field.setAccessible(true);
        field.set(null, null);

        System.clearProperty("authentication.password.salt");
    }

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

        properties.setProperty("password.salt", "foobar2");
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
        Properties properties = new Properties();
        properties.setProperty("password.salt", "test");
        PasswordUtil.setProperties(properties);

        User user = new User();
        // Hashed value of "foobar"+"test" (the salt value)
        user.setPassword("61e38e2b77827e10777ee8f1a138b7cfb1eb895");
        assertTrue(PasswordUtil.verifyPassword(user, "foobar"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getSaltSaltAlreadySet() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        Field field = PasswordUtil.class.getDeclaredField("salt");
        field.setAccessible(true);
        field.set(null, "foobar");

        System.setProperty("authentication.password.salt", "test");
        PasswordUtil.generateHashedPassword("test");
    }

    @Test
    public void generateDefaultSalt() {
        assertEquals("a4f1fbb1274f1fceba9dfae181d7afe6fca96f", PasswordUtil
                .generateHashedPassword("foobar"));
    }

}
