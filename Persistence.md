# Persistence #

## Introduction ##
Most applications, even though how simple, often need some kind of persistence of data. The persistence module's purpose is to provide a simple JPA-based persistence for Vaadin applications, without the developer having to worry to much about such terms EntityManagers? or session management.

Vaadin applications have statefull user interfaces and JPA wasn't exactly designed to be used in these kinds of application. There are quite a few pitfalls when using JPA based persistence in a Vaadin application and this persistence module tries to manage and hide those pitfalls from the user.

## Architecture ##

The persistence module is built by using two different design patters, the facade pattern and the factory pattern. The facade pattern's idea is to abstract the implementation of something away from the developer and provide a simple API. In this case, the our facade hides the logic of JPA's EntityManager and EntityManagerFactory.

The module contains an interface called IFacade, which defines the API for communicating with the database facade. The module also provides a default implementation of JPA based persistence. This default implementation is found in the JPAFacade class which implements the IFacade interface. If you want, you can make your own facade implementation, for example if you want to use Hibernate or maybe even some other type of persistence which doesn't rely on JPA.

The factory pattern's purpose is to create object instances without exposing the instantiation logic. In the persistence module we have a static class called FacadeFactory, whose purpose is, as the name implies, create instances of our facades. The developer should register his persistence unit (explained later in this documentation) to the FacadeFactory. The FacadeFactory will then create an instance of the given facade and store it in a static map. When the user requests for a facade from the FacadeFactory, the factory won't create a new instance of the facade, but rather take the facade instance from the static map and provide it to the user. This means, that the same facade instance will be shared among all the application users. This won't be a problem, since the JPA session is closed after every request, on the contrary, it will save on our resources.

You can register multiple facades to the FacadeFactory. This can come handy for example if you are using two different databases in one application or if you want to use two different IFacade implementations (and then you'll probably also be using different databases for the different implementations, although there is nothing that forbids you from using the same database in both implementations).

As what comes to your entity classes, the IFacade expects you that all your entity classes will extends the AbstractPojo class. The AbstractPojo class is a simple superclass which has the primary key field for all entities and a consistency version field for optimistic locking.

## Importing the persistence module to your project ##

The persistence module is built on EclipseLink, so you will need to download the depending libraries and add them to your project. Go to the address http://www.eclipse.org/eclipselink/ and download the newest installer package of EclipseLink. Unzip the package and copy the eclipselink.jar file from the jlib-folder and all the jar-files in the jlib/jpa-folder to your projects WEB-INF/lib folder.

Next you will need to add the jar for the persistence module to you WEB-INF/lib folder. Just drop the prebuilt package to the lib-folder.

## Configuring the persistence module ##

Once you've imported all required libraries, you are ready to configure your persistence module. First you will need to create a persistence.xml-file in your META-INF -folder. (Note! The META-INF folder needs to be in the classpath, easiest way to ensure this is to create the folder right under your src-folder. In other words, your configuration file would be located in src/META-INF/persistence.xml.) The persistence.xml should be configured according to the JPA specification. To get you started, this documentation will provide three different configurations examples in one persistence file.

First example is configured for HSQLDB, which is a good and simple database implementation for testing purposes. The second persistence unit is configured for PostgreSQL and the third for MySQL.
```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="1.0"
	xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">

	<persistence-unit name="default">
		<provider>
			org.eclipse.persistence.jpa.PersistenceProvider
		</provider>
		<class>org.vaadin.example.foundationplayground.TestPojo</class>
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

	<persistence-unit name="postgres">
		<provider>
			org.eclipse.persistence.jpa.PersistenceProvider
		</provider>
		<class>org.vaadin.example.foundationplayground.TestPojo</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
		<properties>
			<property name="eclipselink.logging.level" value="FINE" />
			<property name="eclipselink.jdbc.driver" value="org.postgresql.Driver" />
			<property name="eclipselink.jdbc.url" value="jbdc:postgresql://localhost/example" />
			<property name="eclipselink.jdbc.user" value="postgres" />
			<property name="eclipselink.jdbc.password" value="" />
			<property name="eclipselink.ddl-generation" value="none" />
			<property name="eclipselink.ddl-generation.output-mode"
				value="database" />
			<property name="eclipselink.orm.throw.exceptions" value="true" />
		</properties>

	</persistence-unit>

	<persistence-unit name="mysql">
		<provider>
			org.eclipse.persistence.jpa.PersistenceProvider
		</provider>
		<class>org.vaadin.example.foundationplayground.TestPojo</class>
		<exclude-unlisted-classes>false</exclude-unlisted-classes>
		<properties>
			<property name="eclipselink.logging.level" value="OFF" />
			<property name="eclipselink.jdbc.driver" value="com.mysql.jdbc.Driver" />
			<property name="eclipselink.jdbc.url" value="jdbc:mysql://localhost:3306/example" />
			<property name="eclipselink.jdbc.user" value="mysql" />
			<property name="eclipselink.jdbc.password" value="" />
			<property name="eclipselink.ddl-generation" value="none" />
			<property name="eclipselink.ddl-generation.output-mode"
				value="database" />
			<property name="eclipselink.orm.throw.exceptions" value="true" />
		</properties>
	</persistence-unit>
</persistence>
```

First thing you will need to pay attention to is the persistence unit's name. In this configuration example it has been named "default", "postgres" and "mysql". The second tag you need to pay attention to is the class-tag. You should list all your entity classes in separate class-tags. In this example configuration, our project has only one Entity class called TestPojo.

The properties starting with the name "eclipselink. ..." is where you configure your database specific configurations. These configurations vary depending on the database you are using. In this example configuration, we are using HSQLDB. Pay special attention to the "eclipselink.ddl-generation"-property, since it will determine how your database is set up. This documentation won't go into detail how it should be configured, but as a word of warning, if the field is configured wrong, you may lose all your data in the database!

Your persistence.xml file is now configured. You'll still need one more thing before you can try it out in your project, namely, the hsqldb.jar -file needs to be added to your WEB-INF/lib-folder. The jar can be downloaded from http://hsqldb.org . If you are using some other database, then you'll need to download that specific database's driver and include it in your WEB-INF/lib-folder.

Now you are all set to go. To take your configuration into use, you will need to register your persistence unit to the facade factory. This is done simply by calling ` FacadeFactory.registerFacade("default", true); ` where "default" is your persistence unit's name and "true" defines whether or not this facade instance should be used as the default facade.

A facade should be instantiated **only once** per application (not for each application instance!), hence it is a good idea to create a ServletContextListener, where you register your facade. This way, when you start up your server, the facade will be ready to go once the first users fires up your application.

# Usage examples #

### FacadeFactory ###

Registering a facade
```
FacadeFactory.registerFacade("default");
```

Getting the default facade
```
IFacade facade = FacadeFactory.getFacade();
```

Getting an alternative facade
```
IFacade facade = FacadeFactory.getFacade("mysql");
```

Removing a facade
```
FacadeFactory.removeFacade("mysql");
```

### The IFacade interface ###

Getting a User entity object with the primary key 42
```
FacadeFactory.getFacade().find(User.class, 42);
```

Getting all User entity objects
```
FacadeFactory.getFacade().list(User.class);
```

Custom queries
```
String query = "SELECT u FROM User AS u WHERE u.username=:username";
Map<String, Object> parameters = new HashMap<String, Object>();
parameters.put("username","johndoe");
User user = FacadeFactory.getFacade().find(query, parameters);
```

Note that you will not have control of transactions yourself, but if you need to store several entities in one transaction, then you can use the storeAll-mehtod. Transaction control is not allowed due to the shared facade.
```
List<AbstractPojo> entities = new ArrayList<AbstractPojo>();
entities.add(user);
entities.add(purchaseOrder);

FacadeFactory.getFacade().storeAll(entities);
```


Deleting an entity
```
FacadeFactory.getFacade().delete(product);
```

### How to build your database structure ###
EclipseLink gives you two options in how your database structure should be built. If you take a look at the permissions.xml
file example above, you can see that there is the following parameter
```
<property name="eclipselink.ddl-generation" value="drop-and-create-tables" />
```
This parameter defines what should be done to the database structure when the facade is initialized. The parameter
drop-and-create-tables will drop your entire database and automatically recreate a new and empty database structure. This
particular option can come in handy when you are running tests, but when your application is in a state where it no longer
may lose any data, you should change the parameter to "none".

Note that if you are not using automatically generated database schemas, but instead decide to do the schema yourself, you
should be aware that each table holding pojo information should have the fields "ID" (primary key) and an integer field
called "CONSISTENCYVERSION". Both fields are necessary for the persistence module to work.