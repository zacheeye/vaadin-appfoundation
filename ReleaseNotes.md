# Release notes #

This page contains the release notes and upgrade instructions since version 1.1.

## Version 1.3.5 ##

This release contains only three changes: one bug fix, one improvement and one design change.

A bug report was made that AppFoundation's authorization module was not compatible with Oracle XE. The problem was due to
the word "resource" being a keyword in the Oracle database, hence, the "resource" column in PermissionEntity was renamed
to "perm\_resource". If you are using the PermissionEntity, please make sure that you update the column name "resource" to
"perm\_resource" in your database. With this fix made, the AppFoundation should be completely compatible with Oracle XE
(or at least all tests pass when XE is used as the test database).

Another more major improvement was made due to a very valid comment from a community member - the i18n module only supports
the TMX format, but what if the user wasn't to provide the translation messages in another format? In this case, the
user wanted to use Java property files. A new interface, `TranslationSource`, is introduced. Instead of giving
a File object to the InternationalizationServlet's loadTranslations() method, you should now give a TranslationSource
object as the method parameter. The TranslationSource's implementing class is responsible of implementing the logic which
fetches the actual translation messages from the wanted sources, this means that you can now have your translation messages in a
Java property file (or for that matter, any other format you wish) as long as you just make your own implementation of the
TranslationSource interface. AppFoundation contains a default implementation of TranslationSource for reading TMX files, this
class is called `TmxSourceReader`. Note that this change does not break existing application, because the old loadTranslation
method which takes a File as its input is now depricated, but its implementation is changed so that it is a wrapper method
for the new interface.

The last change was made to the view module. The `ViewItem`'s `getView()` method returned null, if the both the
view instance was null and the view factory instance was null (this happens, for example, when you use a string as
the view id but you do not explicitly set the item a view instance nor a view factory). This is a bit problematic, since
it will most likely cause a NullPointerException somewhere later on in the application and tracing it back to the original
source may not be the quickest task. Hence, the newest version will automatically throw a NullPointerException directly
from the getView() method if both the view and factory instances are null.


## Version 1.3.4 ##

This release has been about expanding existing APIs and making the already available tools a bit more easy to use for the
developer. Both the i18n and persistence modules have received updates, changes are explained in more detail below.

### New i18n form ###
The i18n module has already contained the @FieldTranslation annotation feature and with the help of the TranslationUtil
helper class, one can bind the translations to a form. This process has been made more simple with the new I18nForm. The
I18nForm is basically a normal form, except that it looks for the @FieldTranslation annotation in the properties of
the given POJO. Basically, all you need for do is to initialize the form with the POJOs class as the constructor's
parameter.

```
I18nForm form = new I18nForm(YourPojo.class);
BeanItem<YourPojo> item = new BeanItem<YourPojo>(new YourPojo());
form.setItemDataSource(item);
```

### Expansions to the persistence module's API ###
The facade interface has received three new methods. Both `list()` method have received two new variants in which
both you can specify a subset of the resultset to be returned. Both methods take two extra parameters, a start index
and an amount of results you wish to be returned.

The second change is that there is now a `getFieldValues()` method. With this method, you can fetch a particular
field's values of all POJOs from the database without fetching the actual POJO instances. For example, if you have
a POJO called Person, you could fetch the first name of all Persons with getFieldValues method. The method would return
a list of strings containing all first names.

## Version 1.3.3 ##
This release comes with two new features, one to the i18n module and one to the view module.

### URI fragment parameters ###
The ViewHandler now supports the usage of URI fragment parameters. This means that you can now add string parameters directly
to your URL and the parameters will be forwarded to the view's `activate()` method.

For example, consider the URL http://some.site/yourapp#yourview/foo/bar , when calling the URL above, the ViewHandler would
activate the view that has been assigned the URI fragment "yourview". When the view gets activated, the `activate()` method
gets as parameters the strings "foo" and "bar".

### Adding new languages to your translations file ###
Previously, the FillXml tool which helps you maintain your translation files was only able to stubs for missing translation
units (tuid). The tool has now been improved to support adding of missing language translations. This means, that if you
have an existing translations file, but you wish to add support for a new language, all you need to do is to call the
`updateTranslations()` method with your new language, and the tool will automatically create stubs for your translations,
even for existing translation units.


## Version 1.3.2 ##
### Bug fixes ###
This version includes a bug fix to the NullPointerException which may occur in the Permissions class if it is used in the same
HTTP request as in which it was initialized.

### Improvements ###
The view module got some changes for this release. A `View` interface was added to replace the `AbstractView`. The
`AbstractView` class remains and it implements the `View` interface, so your existing code will work from that perspective.
However, the other interfaces which previously had references to the `AbstractView` have now been replaced with references
to the `View` interface. This means that when you drop in the new jar, your project will go red. The fix to this is relatively
simple, in most places, just search and replace `AbstractView` references with `View` references.

Note that the `View` is not a component, so you cannot add a view class directly to a layout (as you could do with the
`AbstractView`), this means that you have to cast the view to a component before adding it to a layout.

Example
```
public void activate(View view) {
    if (!(view instanceof Component)) {
        throw new IllegalArgumentException("View must be a component");
    }
    getContent().removeAllComponents();
    getContent().addComponent((Component) view);
}
```

### New features ###
The authorization module's `PermissionManager` has received three new methods, `removePermission()`, `removeAllPermission()`
and `removeAllPermissions()`.

Sometimes you might want to remove defined access rights. By this I mean, you don't **deny** access,
you **remove** assigned access rights completely, whether it be a deny or allow right. There are three methods
available for removing access. The first method `removePermission()` which takes three input
parameters: role, action and resource. The method removes any access rights defined for the given
role for the given action in the given resource.

The second method is called `removeAllPermission()` which takes to parameters: role and
resource. This method removes access rights set with `allowAll()` or `denyAll()`
methods. The removal of access rights is only applied for the given role for the given resource.

The third method is called `removeAllPermissions()`. It as well takes as parameter a role
and a resource. This method removes **all** access rights set for the given role in the given resource,
whether it be a normal deny or allow access right or a denyAll/allowAll.


## Version 1.3.1 ##

There had been a packaging problem with the 1.3.0 release, that problem has been fixed in this release. Sorry for the inconvenience.

## Version 1.3 ##

This version has focused on improving the inbuilt security features but also few other changes have been made. The most significant changes has been in the authentication module, which has gotten a bunch of new features explained below.

### New features, authentication module ###
#### Protection against brute force attacks ####

The authentication module now contains protection against brute force attacks. If a user tries to log in but gets the password wrong five times in a row (number of allowed failed attempts can be configured), the user account will be locked and you can no longer log in with it unless the account is unlocked.

Developers often forget another entry point for brute force attacks - the password change form. Password change forms often require the user to give his current password before the new password is applied. If the user forgets to log out and leaves the computer, then an attacker can use the user's session and perform a brute force attack using the password change form - unless, of course, the developer has remembered block this entry point as well. The `UserUtil` class contains the method `changePassword()`. This method keeps track of failed password change attempts. If the user gets his current password wrong five times in a row, then the user is automatically logged out from the application (note that the account will not be locked).

The application foundation cannot know how you have implemented the showing of a login form / logging out of a user, hence, when the password change fails five times, the `SessionHandler`'s `logout()` method will be called and a `LogoutEvent` is dispatched. This event can be caught by implementing the `LogoutListener` interface and registering the listener to the `SessionHandler`. You should implement this interface and upon a logout, change the content of your application to the login form (or which ever approach you are using).

A couple of new exceptions were also added, for example, the `AuthenticationUtil.authenticate()` method now throws a `AccountLockedException` if the user account has been locked. The `UserUtil` will also throw a `PasswordRequirementException` in those places where a new password is being defined (change password, registering). The `PasswordRequirementException` is thrown if the given password does not fulfill the password policies set (see below).

#### Inbuilt password rules ####

The authentication module now contains more password rules which can be applied for users. Previously, one have been able to define a minimum length for a password. Now you can also define that a password must contain lower-case letters, upper-case letters, number and/or special characters. The usage of these password rules is explained below in the configuration section of this release note.

It can be user-friendly to give the user feedback about these password policies in the graphical user interface. Hence, you can call the `PasswordUtil.getValidators()` method which will return a list of Vaadin validators of all active password rules, these validators can be applied directly to Vaadin fields.

### New features, view module ###

#### Deactivation of views ####
The view module has received a counterpart for the activation of views - namely, deactivation of views. It works just as activating of views, you call `ViewHandler.deactivateView(...)` to deactivate a view. An event about the deactivation is sent to the dispatch listeners (who can cancel the deactivation if necessary) after which the parent view is told to deactivate the view. Finally, the view's `deactivated()` method will be called.

This new event of the views brought some changes to the API of the `DispatchEventListener` interface. The `preDispatch` and `postDispatch` methods are depricated (the will still work) and are replaced with the `preActivation` and `postActivation` methods. Additionally, `preDeactivation` and `postDeactivation` methods were added for the deactivation process.

#### A default implementation for the ViewContainer interface ####
The view module has received a new class, the `SimpleViewContainer`. The `SimpleViewContainer` is a default implementation of the `ViewContainer` interface. The `SimpleViewContainer` is a normal Vaadin `CustomComponent` with a panel. When a view is activated, any existing views are removed from the panel and the activated view is added to the panel. When a view is deactivated, then it is simply removed from the panel. This implementation of the `ViewContainer` comes handy when you don't need any special features for changing views in your application. The `SimpleViewContainer` extends the `AbstractView`, so it can be used as a view itself.

### Changes in configuration of the application ###

Previously, the `PasswordUtil` and `UserUtil` had a `setProperties()` method. These methods have now been depricated (though, they still work) and the System properties should be used instead. There has also been some changes in the property namings, although the old names will still work.

For more details about the new configuration, see the [property configuration document](http://code.google.com/p/vaadin-appfoundation/wiki/PropertyConfiguration) in this wiki.

## Version 1.2 ##

This version comes with some improvements to the existing modules and with a completely
new module.  I'll start with the changes to the current modules. The goal with the changes
to the existing module has been to simplify the API and making the code more clean. For example,
the registering of modules have been made easier. Previously, you needed to have a row like
this in your application's init method:

```
getContent().addTransactionListener(new Lang(this));
```

This same line of code have been simplified to one static method call:
```
Lang.initialize(this);
```

The biggest changes in the existing modules are in the authentication module. To simply the
API and making the code more clean, I've had to break the old API slightly. Previously, when
the `AuthenticationUtil.authenticate()` method was called, an enum was returned with an
error code. Likewise, when using the `UserUtil`'s method, error messages were returned.
Using error codes is actually a bad practice and I wanted to get rid of those. In version 1.2
all error code return types were replaced with exceptions. For example, the API for the
`registerUser` method now looks like this

```
/**
 * This method tries to register a new user
 * 
 * @param username
 *            Desired username
 * @param password
 *            Desired password
 * @param verifyPassword
 *            Verification of the desired password
 * @return The created user instance
 * @throws TooShortPasswordException
 *             Thrown if the given password is too short
 * @throws TooShortUsernameException
 *             Thrown i the given username is too short
 * @throws PasswordsDoNotMatchException
 *             Thrown i the password verification fails
 * @throws UsernameExistsException
 *             Thrown i the username already exists
 */
public static User registerUser(String username, String password,
        String verifyPassword) throws TooShortPasswordException,
        TooShortUsernameException, PasswordsDoNotMatchException,
        UsernameExistsException 
```

This means that your project will go red when you update to version 1.2, but rest assured, the
required changes are easy and fast to make.

The biggest change in the application foundation is the introduction of a new module (yay,
new features)! Some of you might have noticed that the repository has contained a package
named `org.vaadin.appfoundation.authorization` since day one, but it hasn't been included
in the released packages. This module is now completed and it is included in the newest release.
The authorization module provides an easy way of restricting users' access to different parts
of your application. The authorization module can be used separated or together with other
modules. For more details, please review the documentation for the authorization module at
http://code.google.com/p/vaadin-appfoundation/wiki/Authorization.

Hope you like the changes, stay tuned for future releases!

## Version 1.1 ##

The version 1.1 comes with many new improvements to the existing four modules. But before
going into the details, I have to mention that since the 1.0.2 release, I've switched to
use Mercurial as my VCS, so if anyone out there is using the SVN trunk, make sure to
switch to the new repository to be able to get the newest changes.

What comes to quality assurance, I've created unit tests for the project. The test suite
currently covers 92.1% of the code, a number I'm quite satisfied in.

**What's new**

Three out of the four modules have received upgrades. The persistence module was slightly
improved with the publishing of the `count()` method. No other major changes were made
to this module.

The i18n module received support for multiple translation files. This means that you can
manage your translations more flexibly, for example have each language in its own
translation file or maybe have a translation file containing generic translations which
can be shared among different applications and then have separate translation files for
application specific translations. I've dropped support for the default translations.xml
file, which means that each application needs to specify which translation files it is
using. More on this subject in the upgrade instructions.

The view module has received two upgrades. Previously, the ViewItem took a Class object of
the view and used it to instantiate the actual view object. Alternatively the developer
was able to directly set the view object to the ViewItem. Sometimes we want to have the
views initialized on request, but still want to have more control on _how_ the view is
initialized, for example, maybe we want to pass some parameters to the constructor. For
this purpose I've introduced the ViewFactory interface. Each ViewItem has a ViewFactory
which is responsible for instantiating the view object. The view module comes with a
default implementation of the interface, called DefaultViewFactory, which instantiates the
view based on a class object.

Another significant improvement in the view module is the support for URIs. You can now
specify a URI for any view, the view is activated when the URI is triggered.

Note that the AbstractView's `content` field was previously protected. This field has now
been changed to private. You can access the field through the `getContent()` and
`setContent()` methods.

**Upgrading from earlier releases**

The i18n module no longer uses the default translations.xml file, but requires each
application to specify which translation files it is using. To load an translation file
into the InternationalizationServlet's memory, call the `loadTranslation()` method in
the servlet, which takes as parameter a `File` object. A good practice would be to load
the translation files in the application's context listener.

Your views will most likely have errors in them, since the `content` field of the
AbstractView was changed to private. To fix these errors, simply replace any `content` field
references with the `getContent()` method.