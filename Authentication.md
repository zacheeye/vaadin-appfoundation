# Authentication #

## Introduction ##

The authentication module provides a simple implementation for authenticating users in a Vaadin application. The module does not only provide authentication, but also the session management of logged in users. It also provides a set of helpful utility classes for commonly executed operations.

## Architecture ##

The authentication module relies on a database backend and hence depends on the persistence module. The module contains one data class, called User. The User class is a JPA entity class which contains a few commonly used fields, such as username, password, name and email address. The number of the fields can be easily expanded by extending the User class in your own implementation of a User entity object.

The core of the module consists of two utility classes, one for the actual authentication (AuthenticationUtil) and one for session management (SessionHandler). The module also provides two other utility classes (PasswordUtil and UserUtil) for commonly performed operations.

All the classes contain static methods with a slight exception of the SessionHandler class. The SessionHandler class keeps track of the currently inlogged user in the application and allows the the application to access the User object in a static manner. Because basically all applications must support having multiple users inlogged at the same time, we need a way to keep track of several users mutt still be able to access them statically. For this purpose, the SessionHandler class uses the TreadLocal pattern. The application needs to update the TreadLocal variable for each HTTP request and to make this task as easy as possible for the user, the SessionHandler implements the TransactionListener interface. This means that the developer only needs to create an instance of the SessionHandler class in the application's init() method and register the instance as a transaction listener, the SessionHandler class will take care of the rest.

## Importing the authentication module to your project ##

The authentication module depends on the persistence module, so make sure you've imported and configured it correctly for your project.

Next you will need to add the jar for the authentication module to you WEB-INF/lib folder. Just drop the prebuilt package into the lib-folder.

## Configuring the authentication module ##
The only dependency of the authentication module is the persistence module. Hence, make sure you've entered the User class to your persistence.xml file as described in the persistence module's documentation.

You will also need to register the SessionHandler to your application, this is usually done in the application's init method.

```
public class YourApplication extends Application {
	@Override
	public void init() {
	        SessionHandler.initialize(this);
	    
		...
	}

}
```

The authentication module stores the users' passwords in a hashed format in the database. The hash uses a salt value to avoid rainbow attacks. It is recommended that you specify your own application specific hash value for the module. This is done by calling `PasswordUtil.setProperties(properties)` where properties is your configuration. The salt value for the password should be defined in the `password.salt`-property. Note that the properties are stored in a static variable and once it is set, it cannot be changed. This means the properties can be defined only once for your JVM. It is recommended that the properties are set for example in your application's context listener. If the properties are not set, then a default salt value will be used for the passwords.

## Usage ##
### SessionHandler ###
There are basically only two static methods you should use in the SessionHandler. To get the currently inlogged user's User object, you can call `SessionHandler.get();`. To logout a user, simply call `SessionHandler.logout();`. SessionHandler also contains a static `setUser(user)` method, but in normal situations the developer should never need to call himself.

### AuthenticationUtil ###
The authentication utility class contains only one method, `authenticate(username, password)`. This method tries to authenticate the user with the given credentials. If the authentication is successful, the AuthenticationUtil will automatically register the User object to the SessionHandler and return the authenticated user's `User` object. If the authentication fails, an exception is thrown for each appropriate error situation. See the [javadoc](https://vaadin-appfoundation.googlecode.com/hg/javadoc/org/vaadin/appfoundation/authentication/util/AuthenticationUtil.html) for more details.

### PasswordUtil ###
The password utility class contains three static methods. The `setProperties(properties)` method is used for defining a password salt as described earlier in this documentation.

The `verifyPassword(user, password)` compares the unhashed `password` string against the password defined in the `user` object (which is an instance of User). The method returns true if the password is correct and false if they are a mismatch.

The `generateHashedPassword(password)` creates a hashed version of the plain text password provided as the method's input parameter. This method is needed for example when you want to change the password of a user. The User object contains the hashed version of a password, so when you set a new password for a user, you should call `user.setPassword(PasswordUtil.generateHashedPassword(password));`, otherwise the clear text password is stored in the database and the authentication will not work (as it expects the password to be hashed).

### UserUtil ###
The user utility class in not exactly a part of the authentication or session management processes. The class is more of a collection of commonly used methods for controlling the user objects in the application. The class contains methods for changing a user's password, storing a user profile, registering new users etc. Please view the [javadoc](https://vaadin-appfoundation.googlecode.com/hg/javadoc/org/vaadin/appfoundation/authentication/util/UserUtil.html) for a more detailed information about the available methods.