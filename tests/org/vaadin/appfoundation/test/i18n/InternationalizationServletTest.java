package org.vaadin.appfoundation.test.i18n;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.i18n.InternationalizationService;
import org.vaadin.appfoundation.i18n.TmxSourceReader;
import org.vaadin.appfoundation.i18n.TranslationSource;

public class InternationalizationServletTest {

    @Before
    public void setUp() {
        InternationalizationService.clear();
    }

    @Test
    public void loadAndGetMessage() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        TranslationSource all = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(all);

        assertEquals("test1", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void loadFromFile() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        File all = new File(url.getFile());

        InternationalizationService.loadTranslations(all);

        assertEquals("test1", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));
    }

    @Test
    public void loadFromMultipleFiles() {
        URL url = InternationalizationServletTest.class
                .getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.fi.xml");
        TranslationSource fi = new TmxSourceReader(new File(url.getFile()));

        url = InternationalizationServletTest.class
                .getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.en.xml");
        TranslationSource en = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(fi);
        InternationalizationService.loadTranslations(en);

        assertEquals("Name", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("Nimi", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));
    }

    @Test
    public void overrideTranslations() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        TranslationSource all = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(all);

        url = InternationalizationServletTest.class
                .getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.en.xml");
        TranslationSource en = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(en, false);

        assertEquals("test1", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));

        en = new TmxSourceReader(new File(url.getFile()));
        InternationalizationService.loadTranslations(en, true);
        assertEquals("Name", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void overrideFromFile() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        TranslationSource all = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(all);

        url = InternationalizationServletTest.class
                .getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.en.xml");
        TranslationSource en = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(en, false);

        assertEquals("test1", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));

        InternationalizationService.loadTranslations(new File(url.getFile()),
                true);
        assertEquals("Name", InternationalizationService.getMessage("en",
                "GENERIC_NAME"));
        assertEquals("testi1", InternationalizationService.getMessage("fi",
                "GENERIC_NAME"));
    }

    @Test
    public void getMessage() {
        URL url = InternationalizationServletTest.class.getClassLoader()
                .getResource(
                        "org/vaadin/appfoundation/test/i18n/translations.xml");
        TranslationSource all = new TmxSourceReader(new File(url.getFile()));

        InternationalizationService.loadTranslations(all);

        assertEquals("", InternationalizationService.getMessage("DOESNTEXIST",
                "DOESNEXIST"));
        assertEquals("", InternationalizationService.getMessage("en",
                "DOESNEXIST"));
        assertEquals("Foo {0} bar {1}", InternationalizationService.getMessage(
                "en", "TEST"));
        assertEquals("Foo Hello bar world", InternationalizationService
                .getMessage("en", "TEST", "Hello", "world"));

    }

    @SuppressWarnings("deprecation")
    @Test(expected = IllegalArgumentException.class)
    public void invalidFileNull() {
        InternationalizationService.loadTranslations((File) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSourceNull() {
        InternationalizationService.loadTranslations((TranslationSource) null);
    }

    @SuppressWarnings("deprecation")
    @Test(expected = IllegalArgumentException.class)
    public void invalidFileDoesntExist() {
        InternationalizationService.loadTranslations(new File("foobar.xml"));

    }

    @SuppressWarnings("deprecation")
    @Test(expected = IllegalArgumentException.class)
    public void invalidFileFolder() {
        InternationalizationService.loadTranslations(new File("org/"));
    }
}
