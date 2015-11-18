# Rules for permission #

## Terminology ##

We have three key terms: role, resource and action. A resource has a set of actions. Roles are granted or denied access to a resource's action, permissions are never set directly to a resource. For example, to grant the role X access to the resource Y's "read" action, call the following method

```
allow(roleX, "read", resourceY);
```

This method invocation can be read as "allow role x to access 'read' in the resource Y". As you can see, permissions are always applied to the resource and action pair. In this document, we notate this pair as resource/action. The example code could hence be summarized as "allow role x access to resourceY/read".

There are two types of permissions, explicit and implicit permissions. We'll start by explaining the difference between these two types of permissions.

### Explicit permissions ###

Explicit permissions are permissions which have been directly set for a role to a specific resource and its action(s). To explicitly grant access, use either the `allow()` or `allowAll()` method. To explicitly deny access, use either the `deny()` or the `denyAll()`} method.

```
// The visitor role is explicitly granted access to the "read" action 
// for the given resource
allow(visitor, "read", resource);

// The visitor role is explicitly denied access to the "write" action 
// for the given resource
deny(visitor, "write", resource);

// The admin role is explicitly granted access to all actions in the
// given resource
allowAll(admin, otherResource);

// The visitor role is explicitly denied access to all actions in the
// given resource
denyAll(visitor, otherResource);
```

### Implicit permissions ###

A role may be granted or denied access implicitly. This means that we haven't explicitly allowed or denied the role the access to a given resource and its action, but the denied or granted access result depends on other permission rules described later in this document.

For example, by default, if a resource/action combination has no explicit permissions set for **any role**, then all roles are granted access. This granting of access is done _implicitly_, since we haven't _explicitly_ told the role that it has access to the requested resource/action combination.

## Rules for permissions ##

  1. If the requested resource/action combination **doesn't have any any explicit permissions** set for **any role**, then all roles are granted access.
  1. If the role has explicitly been allowed access to the resource/action, then access is granted.
  1. If the role has explicitly been denied access to the resource/Action, then access is **not** granted.
  1. If one or more roles are **explicitly granted** access to a resource/action, then all other roles which do not have any explicit permissions set will be **denied** access to the resource/action   this is an implicit denial of access
  1. If one or more roles are **explicitly denied** access to a resource/action, then all other roles which do not have any explicit permissions set will be **granted** access to the resource/action - this is an implicit granting of access.

## Conflicting permissions when multiple roles are involved ##

Consider a case where a user is given a set of roles. When we are checking a user's permissions, we need to loop through all his roles and check which kinds of permissions are applied by each of the roles. Sometimes different roles may have conflicting permissions, for example, one role might say that the user is granted access to the resource while the other implies that the user is not allowed to access the resource. To solve this conflict we need a set of rules.

We start by categorising the rules in the previous section into four different types of results a permission check may return.

  * Explicit allow: The role was explicitly allowed access to the resource/action
  * Explicit deny: The role was explicitly denied access to the resource/action
  * Implicit deny: The role was Implicitly denied access to the resource/action
  * Implicit allow: The role was implicitly allowed access to the resource/action

To solve the permission conflict, the rules should be applied in the following order:

_Note that these rules doesn't take into account sub roles, but only roles at the same level. See next section for rules about hierarchical conflicts._

  1. An explicit allow overrides other permissions, access is always granted
  1. If an explicit allow doesn't exist, then explicit deny will override other permissions, access is denied
  1. If no explicit permissions are set, then implicit permission will be applied (implicit deny will override any possible explicit allows)
  1. If no other permissions are set, implicit allow is applied

## Hierarchical roles and conflicts ##

Roles are hierarchical since a role may contain a set of sub roles. When a role is assigned a set of sub roles, then that role will inherit the permissions of all its sub roles.

We introduce the concept of role hierarchy levels. Let's assume we created a role named [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1). This role is the root of the hierarchy, hence it is to be considered to be on the level one in the hierarchy tree. We then create roles [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2) and [R3](https://code.google.com/p/vaadin-appfoundation/source/detail?r=3). We set both of these roles as [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1)'s sub roles. When viewing from [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1)'s perspective, [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2) and [R3](https://code.google.com/p/vaadin-appfoundation/source/detail?r=3) are now on level two in the hierarchy tree. If we created additional roles, [R4](https://code.google.com/p/vaadin-appfoundation/source/detail?r=4) and [R5](https://code.google.com/p/vaadin-appfoundation/source/detail?r=5) and set these roles as the sub roles of [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2), then [R4](https://code.google.com/p/vaadin-appfoundation/source/detail?r=4) and [R5](https://code.google.com/p/vaadin-appfoundation/source/detail?r=5) will be considered to be on the level three of the role hierarchy tree.

When evaluating the permissions of a user, one must take into account the role's sub roles.

Example: We have two roles, [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1) and [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2). [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2) is a sub role of [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1). We have a resource called ListView and it has the action "read". [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2) is explicitly allowed the access to ListView/read, [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1) is explicitly denied access to ListView/read.

The question is, if a user is given the role [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1), does he or does he not have access to ListView/read? The correct answer is that he does not. The previous section would imply that the user would have access, since explicit allow overrides explicit deny. Those rules only apply to roles in the same level.

The following rules should be applied when determining the correct permissions

  1. If either an explicit allow or deny is found in any level of the hierarchy tree, then that permission should be applied.
  1. If the hierarchy tree contains multiple explicit permissions, then the top most permission (lowest level number) should be applied.
  1. If there are multiple explicit rules defined on the same level, then the rules of the previous section should be applied to determine the correct permission
  1. If no explicit permissions are set, then implicit permissions are applied
  1. An implicit deny **will override all** implicit allows **at any level**

To analyse the example above with the given rules, we can see that even though we had both an explicit deny and an explicit allow, the explicit deny will override the explicit allow, since it was on a higher level in the hierarchy tree. If the user would be directly assigned both the roles [R1](https://code.google.com/p/vaadin-appfoundation/source/detail?r=1) and [R2](https://code.google.com/p/vaadin-appfoundation/source/detail?r=2), then the explicit allow would override the explicit deny, since now both the explicit permissions are on the same level.