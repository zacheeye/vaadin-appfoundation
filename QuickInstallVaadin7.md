# Quick install #

This is a quick step-by-step instruction on how to create a Vaadin application which uses the
application foundation package.

1) Create a new Vaadin project

2) Add the appfoundation.jar to your WEB-INF/lib -folder

3) Go to http://www.eclipse.org/eclipselink/ and download the newest installer package of
EclipseLink. Unzip the package and copy the eclipselink.jar file from the jlib-folder and all
the jar-files in the jlib/jpa-folder to your projects WEB-INF/lib folder.

4) Go to http://www.xom.nu/ and dowlad the XOM XML parser. Once you downloaded the parser,
include the xom.jar package in your application's WEB-INF/lib -folder.

5) Go to http://hsqldb.org/ and download the newest version of the hsqldb. Include the hsqldb.jar
in your WEB-INF/lib -folder.

_Note: This step depends on which database your are going to use. Check the peristence module's
documentation for more details_

6) Create a META-INF -folder in your src-folder.

7) Create a persistence.xml -file in the META-INF folder you just created. The example
application's persistence file looks like this:
```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="1.0"
        xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">


       <persistence-unit name="default">
                <provider>
                        org.eclipse.persistence.jpa.PersistenceProvider
                </provider>
                <class>org.vaadin.appfoundation.authentication.data.User</class>
                <class>org.vaadin.appfoundation.example.data.Message</class>
                <exclude-unlisted-classes>false</exclude-unlisted-classes>
                <properties>
                        <property name="eclipselink.logging.level" value="OFF" />
                        <property name="eclipselink.jdbc.driver" value="org.hsqldb.jdbcDriver" />
                        <property name="eclipselink.jdbc.url" value="jdbc:hsqldb:mem:example" />
                        <property name="eclipselink.jdbc.user" value="sa" />
                        <property name="eclipselink.jdbc.password" value="" />
                        <property name="eclipselink.target-database"
                                value="org.eclipse.persistence.platform.database.HSQLPlatform" />
                        <property name="eclipselink.ddl-generation" value="drop-and-create-tables" />
                        <property name="eclipselink.ddl-generation.output-mode"
                                value="database" />
                        <property name="eclipselink.orm.throw.exceptions" value="true" />
                </properties>

        </persistence-unit>
</persistence>
```

8) Create a context listener for your application in which you register your facade and set a
password salt value for the authentication module.
```
@Override
public void contextInitialized(ServletContextEvent arg0) {
    try {
	// Register facade
	FacadeFactory.registerFacade("default", true);

	// Set the salt for passwords
	Properties prop = new Properties();
	prop.setProperty("password.salt", "pfew4‰‰#fawef@53424fsd");
	PasswordUtil.setProperties(prop);

	// Set the properties for the UserUtil
	prop.setProperty("password.length.min", "4");
	prop.setProperty("username.length.min", "4");
	
	UserUtil.setProperties(prop);
  ....
}
```

9) Register the ViewHandler, Lang, PermissionManager and the SessionHandler to your application in your UI's
init() method.
```
@Override
public void init(VaadinRequest request) {
    SessionHandler.initialize(this);
    ViewHandler.initialize(this);
    Lang.initialize(this);
    Permissions.initialize(this, new JPAPermissionManager());
    ...
}
```
10) Modify your web.xml file to register the ContextListener<listener>
  <listener-class>
    org.vaadin.appfoundation.example.YourContextListener
  </listener-class>
</listener>


11) Create your translation files and load them into the i18n servlet. A good place to do this 
is in your application's context listener.

{{{
@Override
public void contextInitialized(ServletContextEvent arg0) {
    ...
	File file = new File(path);
	InternationalizationService.loadTranslations(file);
    ...
}
}}}

You should now have everything you need configured to be able to use the application foundation library in its full force!```