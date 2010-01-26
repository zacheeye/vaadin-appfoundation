package org.vaadin.appfoundation.persistence;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

/**
 * A default persistence context listener which will start up the "default"
 * persistence unit in the facade factory.
 * 
 * @author Kim
 * 
 */
public class DefaultPersistenceContextListener implements
        ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {
        // Do nothing

    }

    public void contextInitialized(ServletContextEvent arg0) {
        // Registers the peristence unit "default" to the facade factory and
        // sets this as the default facade.
        FacadeFactory.registerFacade("default", true);
    }

}
