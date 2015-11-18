# Introduction #
This project's goal is to provide a simple and lightweight foundation for Vaadin application. The project consist of individual modules which are designed to be used separately or in combination with other modules. Minimization of dependencies to other modules and third party libraries have been one of the primary goals with the module designs.

# Modules #

The first release of the application foundation contains four modules

## Persistence ##

Most applications, even though how simple, often need some kind of persistence of data. The persistence module's purpose is to provide a simple JPA-based persistence for Vaadin applications, without the developer having to worry to much about such terms EntityManagers? or session management.

Dependencies
  * EclipseLink

## Authentication ##

The authentication module provides a simple implementation for authenticating users in a Vaadin application. The module does not only provide authentication, but also the session management of logged in users. It also provides a set of helpful utility classes for commonly executed operations.

Dependencies
  * The persistence module

## Internationalization ##

Often applications need to be written for a multilingual audience. Therefore we must have a way to easily make translations of our applications. Even though you know that your application will not need any translation, it is still a good practice to keep the natural language strings away from your application code. The internationalization (i18n) module will help you accomplish that task.

Dependencies
  * XOM XML parser

## View handling ##
When your application grows, the complexity of your user interface code grows as well. At some point, you might want to create cross-references between different UI components. If your user interface hasn't been designed for such operations, building the required logic afterward can be a tedious task. The view module provides a simple way to activate and cross-reference different parts of the application's user interface.

Dependencies
  * None
