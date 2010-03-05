package org.vaadin.appfoundation.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * This servlet reads the xml file of all translations and loads them into a
 * map.
 * 
 * @author Kim
 * 
 */
public class InternationalizationServlet extends HttpServlet {

    private static final long serialVersionUID = 6849398292635918231L;

    // <lang, <id, message>>
    private static Map<String, Map<String, String>> translations = new HashMap<String, Map<String, String>>();

    /**
     * Load a translation file into memory. Any duplicated translations will be
     * discarded.
     * 
     * @param file
     *            The File object of the translation file
     */
    public static void loadTranslations(File file) {
        loadTranslations(file, false);
    }

    /**
     * Load a translation file into memory.
     * 
     * @param file
     *            The File object of the translation file
     * @param force
     *            If a translation already exists in-memory, should the new
     *            translation override the existing translation
     */
    public static void loadTranslations(File file, boolean force) {
        // Make sure the input parameter is valid
        if (file == null || !file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Translation did not exist");
        }

        // Create an XML builder
        Builder builder = new Builder();
        try {
            // Read the translations.xml file to the Document
            Document document = builder.build(new FileInputStream(file));
            Element root = document.getRootElement();
            // Search for all "tu"-elements
            Elements tu = root.getChildElements("body").get(0)
                    .getChildElements("tu");

            // Loop through the tu-elements. Each tu-element is a translation
            // for a single localized string
            for (int i = 0; i < tu.size(); i++) {
                // Get the key for this translation string
                String identifier = tu.get(i).getAttributeValue("tuid");

                // Get the tuv-elements. Each tuv-element is the actual
                // translation for one language
                Elements tuv = tu.get(i).getChildElements("tuv");
                for (int j = 0; j < tuv.size(); j++) {
                    // Get the language
                    String language = tuv.get(j).getAttributeValue("lang");
                    // Get the translated message
                    String message = tuv.get(j).getChildElements("seg").get(0)
                            .getValue();
                    // Add the translation to our map
                    addMessage(language, identifier, message, force);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a translated message to the translations map
     * 
     * @param language
     *            Language of the translation
     * @param identifier
     *            Key string for the translation message
     * @param message
     *            The translated message
     * @param force
     *            If a translation already exists in-memory, should the new
     *            translation override the existing translation
     */
    private static void addMessage(String language, String identifier,
            String message, boolean force) {
        Map<String, String> messages = null;
        // Check if there are any existing translations for this language
        if (!translations.containsKey(language)) {
            // No translations existed for this language, so create a new map
            // for it
            messages = new HashMap<String, String>();
            translations.put(language, messages);
        } else {
            // Use the existing map for this language's translations
            messages = translations.get(language);
        }
        // Add the translation message to this language's translation map. If
        // force is true, then override any previous translations.
        if (!messages.containsKey(identifier) || force) {
            messages.put(identifier, message);
        }
    }

    /**
     * Get the translated message for a specific language
     * 
     * @param language
     *            Language for which we want the translation
     * @param identifier
     *            Key for the translation message
     * @param params
     *            Parameters for the translation message
     * @return Translated message string
     */
    public static String getMessage(String language, String identifier,
            Object... params) {
        if (!translations.containsKey(language)) {
            return "";
        } else {
            // Get the raw translation message
            Map<String, String> messages = translations.get(language);
            String msg = messages.containsKey(identifier) ? messages
                    .get(identifier) : "";

            // Check if any parameters are defined
            if (params != null) {
                // Replace the placeholders in the raw message with the given
                // parameters
                for (int i = 0; i < params.length; i++) {
                    String value = String.valueOf(params[i]);
                    msg = msg.replace("{" + i + "}", value);
                }
            }

            return msg;
        }
    }
}
