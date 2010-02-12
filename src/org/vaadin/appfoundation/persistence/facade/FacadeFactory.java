package org.vaadin.appfoundation.persistence.facade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating and managing facades in the application.
 * 
 * @author Kim
 * 
 */
public class FacadeFactory implements Serializable {

    private static final long serialVersionUID = -7409448835737552324L;

    private static Map<String, IFacade> facades = new HashMap<String, IFacade>();

    private static IFacade defaultFacade;

    /**
     * Register a new JPAFacade to the application.
     * 
     * @param name
     *            The persistence-unit name in the persistence.xml file.
     * @param isDefault
     *            Should this facade be the default facade to be used in the
     *            application.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void registerFacade(String name, boolean isDefault)
            throws InstantiationException, IllegalAccessException {
        registerFacade(JPAFacade.class, name, isDefault);
    }

    /**
     * Register a new facade to the application.
     * 
     * @param facade
     *            The class of the facade implementation
     * @param name
     *            The persistence-unit name in the persistence.xml file.
     * @param isDefault
     *            Should this facade be the default facade to be used in the
     *            application.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void registerFacade(Class<? extends IFacade> facade,
            String name, boolean isDefault) throws InstantiationException,
            IllegalAccessException {
        // Check if there already exists a facade with this name.
        if (facades.containsKey(name)) {
            // If it exists, then close the facade.
            facades.get(name).close();
        }

        // Create a new instance of the facade
        IFacade facadeImpl = facade.newInstance();

        // Initialize the facade
        facadeImpl.init(name);

        // Put the facade in our static map
        facades.put(name, facadeImpl);

        // Should this facade instance be used as the default facade in the
        // application?
        if (isDefault) {
            defaultFacade = facadeImpl;
        }
    }

    /**
     * Returns the default facade of the application.
     * 
     * @return The default facade instance
     */
    public static IFacade getFacade() {
        return defaultFacade;
    }

    /**
     * Get the facade for a specific configuration
     * 
     * @param name
     *            Persistence-unit name (defined in the persistence.xml)
     * @return The facade instance for the given name
     */
    public static IFacade getFacade(String name) {
        return facades.get(name);
    }

    /**
     * Removes a facade from the factory
     * 
     * @param name
     *            Persistence-unit name (defined in the persistence.xml)
     */
    public static void removeFacade(String name) {
        if (facades.containsKey(name)) {
            IFacade facade = facades.get(name);
            facade.kill();
            facades.remove(name);
        }
    }

}
