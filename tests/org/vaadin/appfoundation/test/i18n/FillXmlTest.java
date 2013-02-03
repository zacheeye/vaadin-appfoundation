package org.vaadin.appfoundation.test.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.i18n.FillXml;
import org.vaadin.appfoundation.i18n.InternationalizationService;
import org.vaadin.appfoundation.i18n.TmxSourceReader;

public class FillXmlTest {

    private File file = null;

    @Before
    public void setUp() throws IOException {
        // Clear the servlet's memory
        InternationalizationService.clear();

        // create a random file
        file = new File(UUID.randomUUID().toString());
        if (!file.createNewFile()) {
            fail("Unable to create file");
        }

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                + "<tmx version=\"1.4\"><body></body></tmx>";
        FileOutputStream fileoutstream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fileoutstream, "UTF-8");
        writer.write(xml);
        writer.close();
    }

    @After
    public void tearDown() {
        if (!file.delete()) {
            fail("Unable to delete file");
        }
    }

    @Test
    public void updateFile() throws ValidityException, ParsingException,
            IOException {
        // Create a list of identifiers
        List<String> identifiers = new ArrayList<String>();
        // Only add one identifier to the list
        identifiers.add("TEST");

        // Update the file with the given identifiers and languages
        FillXml.updateTranslations(file, new String[] { "en", "fi" },
                identifiers);
        // Load the newly created file into the servlet
        InternationalizationService.loadTranslations(new TmxSourceReader(file));

        // Check that the TODO messages were added for both the "en" and
        // "fi" languages.
        assertEquals("TODO", InternationalizationService.getMessage("en",
                "TEST"));
        assertEquals("TODO", InternationalizationService.getMessage("fi",
                "TEST"));

        // Check that the "ANOTHER" identifier doesn't exist yet in the file
        assertEquals("", InternationalizationService
                .getMessage("en", "ANOTHER"));

        // Add a new identifier
        identifiers.add("ANOTHER");

        // Update the file with the new list of identifiers
        FillXml.updateTranslations(file, new String[] { "en", "fi" },
                identifiers);
        // Load the new file
        InternationalizationService.loadTranslations(new TmxSourceReader(file));

        // Check that old values exist
        assertEquals("TODO", InternationalizationService.getMessage("en",
                "TEST"));

        // Check that the new value was added
        assertEquals("TODO", InternationalizationService.getMessage("en",
                "ANOTHER"));

    }

    @Test
    public void addNewLanguages() throws ValidityException, ParsingException,
            IOException {
        // Create a list of identifiers
        List<String> identifiers = new ArrayList<String>();
        // Only add one identifier to the list
        identifiers.add("TEST");

        // Update the file with the new list of identifiers
        FillXml.updateTranslations(file, new String[] { "en" }, identifiers);
        // Load the new file
        InternationalizationService.loadTranslations(new TmxSourceReader(file));

        // Check that the new value was added
        assertEquals("TODO", InternationalizationService.getMessage("en",
                "TEST"));

        // The "fi" language shouldn't be added, as it was not in the list
        // of available languages
        assertEquals("", InternationalizationService.getMessage("fi", "TEST"));

        // Now run the same update, but including the "fi" language
        FillXml.updateTranslations(file, new String[] { "en", "fi" },
                identifiers);
        // Load the new file
        InternationalizationService.loadTranslations(new TmxSourceReader(file));

        // Check that the finnish translation was added to the file
        assertEquals("TODO", InternationalizationService.getMessage("fi",
                "TEST"));
    }

    @Test(expected = FileNotFoundException.class)
    public void nonExistingFile() throws IOException, ValidityException,
            ParsingException {
        File file = new File(UUID.randomUUID().toString());
        List<String> identifiers = new ArrayList<String>();
        FillXml.updateTranslations(file, new String[] { "en", "fi" },
                identifiers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullFile() throws ValidityException, ParsingException,
            IOException {
        List<String> identifiers = new ArrayList<String>();
        FillXml.updateTranslations(null, new String[] { "en", "fi" },
                identifiers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullLanguage() throws ValidityException, ParsingException,
            IOException {
        List<String> identifiers = new ArrayList<String>();
        FillXml.updateTranslations(new File(UUID.randomUUID().toString()),
                null, identifiers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullIdentifiers() throws ValidityException, ParsingException,
            IOException {
        List<String> identifiers = null;
        FillXml.updateTranslations(new File(UUID.randomUUID().toString()),
                new String[] { "en", "fi" }, identifiers);
    }

}
