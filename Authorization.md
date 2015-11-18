# The Authorization module #

The application foundation currently has an authentication module. Where there is need for
authentication, there is often also need for authorization. To quickly sum up the terminology,
authentication is where the application makes sure the user is who he claims to be (this can
be achieved in different ways, such as with certificates or the traditional log in model with a
username and a password). Authorization is where the application evaluates if the user has
access to a specific resource (for example a view) and based on the user's access rights, the
application either grants or denies access to the resource. This module defines an API for
using permissions in your application.

The authorization module has role-based access control (RBAC). Different roles are created to
reflect the different job functions in an application. Roles are defined permissions to perform
actiond or to access resources. A user is never assigned permissions directly, all permissions
are assigned to roles and users are assigned roles.


### Example ###
If you are familiar with role-based access control, you can skip this part and continue reading
about the design.

Example, an e-commerce application might have such roles as Visitor, Customer, Partner and
Administrator. The Visitor role is assigned to all unauthenticated users. A Visitor may view
products, but cannot complete an order. Authenticated users are assigned the Customer role.
The Customer role allows the user to complete a purchase. Some loyal customers might be assigned
the Partner role, which would grant them discounted prices. Now we have hidden a resource
(discount price) behind a role. A normal customer would not be able to see the discount price,
but a Partner would. An administrator can easily assign roles to users, for example, he can add
the Partner role to any existing user, who thus would become both a Customer and a Partner.

The benefit of assigning permissions directly to roles instead of users can be seen when a
modification is made to the permission settings. Consider a case where the administrator of the
e-commerce application wants to grant free delivery of all products to all his loyal customers.
Now the administrator can assign the Partner role the permission to free deliveries, this
permission would automatically by applied to all loyal customers. Without the roles, the
administrator would have had to individually assign free delivery to all his loyal customers.

### Design ###

The core of the module are the `PermissionManager`, `Role` and `Resource`
interfaces. The `Role` interface would typically be implemented by a class which can be
persisted into some sort of a storage. In some cases, you actually want to assign permissions
directly to a user, this can easily be done by implementing the `Role` interface in your
user class.

The `Resource` interface doesn't have a default implementations, since resources are always
application specific. Typical resources in an application could be views and/or data (entities).

Both the `Role` and `Resource` interfaces are really small. They both have a common
method which needs to be implemented - `getIdentifier()`. The `getIdentifier()`
methods purpose is to return an object which can be used to uniquely identify that specific role
or resource. Typically, the identifier could be an entities primary key or, for example, a view's
class object. The reason for using identifiers is to minimize the need of keeping references
to the actual role or resource objects. The `Role` interface has a few other methods which
need to be implemented. These methods have to do with managing role hierarchies (a role can
be assigned a set of sub-roles, in other words, a role inherits its sub-roles' permissions).

The core the actual authorization process is the `PermissionManager` interface. A
PermissionManager is an object where you define if a role is granted or denied the access to a
resource. The authorization module will come with two default implementations of the
`PermissionHandler`. The first implementation is called `MemoryPermissionManager`,
which stores all its permissions in-memory. Note that the PermissionManager is application
specific, so you'll need to assign all the permissions for each individual application instance.
Alternatively, you could make the MemoryPermissionManager work like a singelton class and
provide the same instance of the object to all the application instances.

The second default implementation of the `PermissionManager` interface is the
`JPAPermissionManager` which stores its permission data into a database. This implementation
relies on the persistence module for data storage, so make sure you've configured the
persistence module if you intend to use this implementation. The advantage with this
implementation is that all the permissions are stored in a database, which means that once
you've once defined a permission, it is then immediately applied to to all application instances.

To see the rules how permissions should be applied, please read the
[Permission rules](http://code.google.com/p/vaadin-appfoundation/wiki/PermissionRules) document.

# Importing the view module to your project #
The authorization module does not have any dependencies**, so all you need to do is to drop the
jar package in your application's WEB-INF/lib -folder and you are all set to go.**

**If you are using the `JPAPermissionManager` implementation, then this module depends on
the persistence module.**

# Configuring the view module #

All you need to do, is to register initialize the `Permissions` class to your application
using the approprtiate `PermissionManager`. This is usually done in the application's
init-method.
```
public class YourApplication extends Application {
        @Override
        public void init() {
                Permissions.initialize(this, new JPAPermissionManager());                           
                ...
        }
}
```


## API usage examples ##

The `PermissionManager` interface contains methods for managing permissions in an
application. The functions of the permission manager can be divided into three categories:
granting permissions, denying permissions and checking permissions. You'll probably want to
access these methods in a static way, for that purpose there is a `Permissions` class
which provides static wrapper methods for the actual `PermissionManager` implementation.

### Granting access ###

The core method for granting access looks like this:

```
public void allow(Role role, String action, Resource resource);
```

The first and third parameters are obvious. The first parameter is the role to whom you want to
grant access, the third parameter is the resource to which you want to grant access. The second
parameter needs a bit more explaining. Sometimes it is not enough to just grant or deny access
to a resource, sometimes you want to have multiple permission definitions for one role-resource
pair. The second parameter allows you to define an action for which the permission is defined
in the resource. To help you understand what this actually means, see the example below.

There is also a allowAll method, which looks like this
```
public void allowAll(Role role, Resource resource);
```

This method works the same way as the `allow()` method, except that it doesn't take an
action parameter. This method applies the allow-permission to all actions in the given resource.

### Denying access ###

Denying access works just like granting access

```
public void deny(Role role, Object action, Resource resource);
```

Why have separate deny and allow methods? Consider a case where you have 20 roles. You have a
resource and you want to grant all but one role the permission to that resource. It is hence
easier to define and maintain the permissions if you just **deny** that one role access to the
resource instead of setting an allowance permission for the other remaining 19 roles. There is
also a `denyAll()` method which corresponds to the functionality of `allowAll()`, except
that it, of course, denies access to all actions in the resource.

### Checking access ###

```
public boolean hasAccess(Role role, Object action, Resource resource)
```

This method is hopefully self-explaining. You ask the `PermissionManager` whether or not the
given role has the right to perform the given action for the given resource.

### Removing access ###

Sometimes you might want to remove defined access rights. By this I mean, you don't **deny** access,
you **remove** assigned access rights, whether it be a deny or allow right. There are three methods
available for removing access. The first method `removePermission()` which takes three input
parameters: role, action and resource. The method removes any access rights defined for the given
role for the given action in the given resource.

The second method is called `removeAllPermission()` which takes to parameters: role and
resource. This method removes access rights set with `allowAll()` or `denyAll()`
methods. The removal of access rights is only applied for the given role for the given resource.

The third method is called `removeAllPermissions()`. It as well takes as parameter a role
and a resource. This method removes **all** access rights set for the given role in the given resource,
whether it be a normal deny or allow access right or a denyAll/allowAll.

### Example ###
Consider that the resource is a view in an RSS feed reader application. We have three sorts of
users: visitors who are only allowed to read the feeds, registered users can read feeds and post
comments to the feeds and finally we have administrators who can modify which RSS feeds are shown
in the application.

Let's identify the different parts of the applications. The roles are Visitor, RegisteredUser
and Administrator. We might have multiple resources, but in this example we'll just focus on
one resource: the NewsFeedView. The

The roles are hierarchical, which means that any RegisteredUser will also have the permissions
of the Visitor role, any Administrator will have all the permissions of RegisteredUser (and
hence also Visitor's permissions).

Before we can assign permissions, we'll first need to identify the actions in the resource. As
mentioned, a visitor can only read feeds, but not comment on them - hence we have two actions:
read and comment. We've now identified the actions, so we're ready to define the permissions

```
// Initialize the PermissionManager, this should usually be done in the application's
// init method
Permissions.initialize(application, new MemoryPermissionManager());

Role visitors = getVisitorRole();
Role regUsers = getRegisteredUsersRole();
Resource newsFeedView = getNewsFeedView();

Permissions.allow(visitors, "read", newsFeedView);
Permissions.allow(regUsers, "comment", newsFeedView);
```

Before opening the news feed view, we can call
```
if(Permissions.hasAccess(usersRoles, "read", newsFeedView)) {
	layout.addComponent(newsFeedView);
}
```

In the actual news feed view, we can check further permissions
```
if(Permissions.hasAccess(usersRoles, "comment", newsFeedView)) {
	layout.addComponent(commentForm);
}
```