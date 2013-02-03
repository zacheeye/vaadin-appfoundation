package org.vaadin.appfoundation.test.i18n;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.i18n.InternationalizationService;
import org.vaadin.appfoundation.i18n.Lang;
import org.vaadin.appfoundation.i18n.TmxSourceReader;
import org.vaadin.appfoundation.test.MockUI;

public class LangTest {

    private MockUI application;
    private Lang lang = null;

    @Before
    public void setUp() {
        // Create a new instance of the MockApplication
        application = new MockUI();
        // Initialize the Lang class with the MockApplication
        lang = new Lang(application);
    }

    @After
    public void tearDown() {
        lang = null;
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

        InternationalizationService.loadTranslations(new TmxSourceReader(all));
        Lang.setLocale(Locale.ENGLISH);
        assertEquals("Foo bar bar bar", Lang.getMessage("TEST", "bar", "bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeWithNullApplication() {
        Lang.initialize(null);

    }

    @Test
    public void initialize() {
//        MockContext context = (MockContext) application.getContext();
//        assertEquals(0, context.getListeners().size());
//        Lang.initialize(application);
//        assertEquals(1, context.getListeners().size());
//        assertEquals(Lang.class, context.getListeners().get(0).getClass());
    }
}
