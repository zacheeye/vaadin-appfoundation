package org.vaadin.appfoundation.i18n;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import org.vaadin.appfoundation.authorization.Permissions;

import com.apple.eawt.Application;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * A utility class for keeping track of the used locale instance for each
 * application
 * 
 * @author Kim
 * 
 */
public class Lang implements Serializable {

    private static final long serialVersionUID = -217583268167388861L;

    private Locale locale = Locale.getDefault();

    private UI ui;

    /**
     * Constructor. Takes as parameter the application instance.
     * 
     * @param ui
     *            Application instance.
     */
    public Lang(UI ui) {
        this.ui = ui;
        VaadinSession session = ui.getSession();
        String id = ui.getId();
        session.setAttribute(id + "-lang", this);
    }

    /**
     * Get the current locale for the application
     * 
     * @return Locale
     */
    public static Locale getLocale() {
        return getCurrent().locale;
    }

    /**
     * Set the current locale for the application
     * 
     * @param locale
     *            Locale
     */
    public static void setLocale(Locale locale) {
        getCurrent().locale = locale;
    }

    /**
     * Get the translated message for the locale set in Lang
     * 
     * @param identifier
     *            Key for the translation message
     * @param params
     *            Parameters for the translation message
     * @return Translated message string
     */
    public static String getMessage(String identifier, Object... params) {
        return InternationalizationService.getMessage(
                getLocale().getLanguage(), identifier, params);
    }


    /**
     * Initializes the {@link Lang} for the given {@link Application}
     * 
     * @param ui
     */
    public static void initialize(UI ui) {
        if (ui == null) {
            throw new IllegalArgumentException("Application may not be null");
        }
        new Lang(ui);
    }
    
	private static Lang getCurrent() {
		UI ui = UI.getCurrent();
		Lang current = (Lang) ui.getSession().getAttribute(
				ui.getId() + "-lang");
		return current;
	}


}
