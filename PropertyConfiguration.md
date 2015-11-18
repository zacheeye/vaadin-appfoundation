# Code example #
```
// Define the used password salt
System.setProperty("authentication.password.salt", "foobar");

// Set the minimum length for passwords
System.setProperty("authentication.password.validation.length", "4");

// Define is passwords must contain lower-case letters (a-z)
System.setProperty("authentication.password.validation.lowerCaseRequired", "true");

// Define is passwords must contain upper-case letters (A-Z)
System.setProperty("authentication.password.validation.upperCaseRequired", "true");

// Define is passwords must contain numbers (0-9)
System.setProperty("authentication.password.validation.numericRequired", "true");

// Define is passwords must contain special characters (anything else than a-z, A-Z or 0-9)
System.setProperty("authentication.password.validation.specialCharacterRequired", "true");

// Set the minimum length for usernames
System.setProperty("authentication.username.validation.length", "4");

// Set the number of failed password change attempts (incorrect current 
// password) before the user is automatically logged out.
System.setProperty("authentication.maxFailedPasswordChangeAttempts", "4");

// Set the number of maximum allowed login attempts before the user 
// account is locked and login is prohibited.
System.setProperty("authentication.maxFailedLoginAttempts", "4");
```