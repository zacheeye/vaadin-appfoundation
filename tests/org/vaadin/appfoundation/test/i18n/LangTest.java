package org.vaadin.appfoundation.test.i18n;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.i18n.InternationalizationServlet;
import org.vaadin.appfoundation.i18n.Lang;
import org.vaadin.appfoundation.test.MockApplication;

public class LangTest {

    private MockApplication application;

    @Before
    public void setUp() {
        // Create a new instance of the MockApplication
        application = new MockApplication();
        // Initialize the Lang class with the MockApplication
        new Lang(application);
    }

    @Test
    public void getAndSetLocale() {
        assertEquals(Locale.getDefault(), Lang.getLocale());
        Lang.setLocale(Locale.CANADA);
        assertEquals(Locale.CANADA, Lang.getLocale());
        Lang.setLocale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, Lang.getLocale());
    }

    @Test
    public void getMessage() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        File all = new File(url.getFile());

        InternationalizationServlet.loadTranslations(all);
        Lang.setLocale(Locale.ENGLISH);
        assertEquals("Foo bar bar bar", Lang.getMessage("TEST", "bar", "bar"));
    }
}
