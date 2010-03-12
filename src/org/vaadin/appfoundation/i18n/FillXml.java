package org.vaadin.appfoundation.i18n;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.NodeFactory;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * This is a helper class for maintaining the translations.xml file. This file
 * will look for any translations defined in the SystemMsg-file but which have
 * not yet been added to the translations.xml. This class will then create stubs
 * for those missing translations.
 * 
 * @author Kim
 * 
 */
public class FillXml {

    /**
     * Adds stubs for missing translations in the translations file.
     * 
     * @param file
     *            File containing the translations
     * @param languages
     *            All available languages
     * @param identifiers
     *            All used identifiers
     * @throws IOException
     * @throws ParsingException
     * @throws ValidityException
     */
    public static void updateTranslations(File file, String[] languages,
            List<String> identifiers) throws ValidityException,
            ParsingException, IOException {
        // Check that we are not getting any null parameters
        if (file == null || languages == null || identifiers == null) {
            throw new IllegalArgumentException("All parameters must be set");
        }

        // Make sure the file exists
        if (file != null && !file.exists()) {
            throw new FileNotFoundException();
        }
        Builder builder = new Builder();
        // A list of all identifiers already existing in the file
        List<String> ids = new ArrayList<String>();

        // Read the file with the parser
        Document document = builder.build(file);

        // Get the root element
        Element root = document.getRootElement();

        // Get the tu-elements
        Elements tu = root.getChildElements("body").get(0).getChildElements(
                "tu");

        // Loop through all tu-elements and fetch the identifiers
        for (int i = 0; i < tu.size(); i++) {
            String id = tu.get(i).getAttributeValue("tuid");
            // Add the id to the list
            ids.add(id);
        }

        NodeFactory nf = new NodeFactory();

        // Loop through all know identifiers
        for (String id : identifiers) {
            // If the identifier didn't exist in the file, then we need to
            // create a stub for it
            if (!ids.contains(id)) {

                // Add a new tu-element
                Element element = nf.startMakingElement("tu", null);
                // Set the id
                element.addAttribute(new Attribute("tuid", id));

                // Loop through all our languages
                for (String lang : languages) {
                    // Add the tuv-elements for each language
                    Element e = nf.startMakingElement("tuv", null);
                    e.addAttribute(new Attribute("lang", lang));

                    Element seg = nf.startMakingElement("seg", null);
                    seg.addAttribute(new Attribute("value", "TODO"));
                    e.appendChild(seg);
                    element.appendChild(e);
                }
                root.getChildElements("body").get(0).appendChild(element);
            }
        }

        // Make our XML file compatible with the servlet.
        String xml = root.toXML().replaceAll("<seg value=\"TODO\" />",
                "<seg>TODO</seg>");

        FileOutputStream fileoutstream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fileoutstream, "UTF-8");
        writer.write(xml);
        writer.close();
    }
}
