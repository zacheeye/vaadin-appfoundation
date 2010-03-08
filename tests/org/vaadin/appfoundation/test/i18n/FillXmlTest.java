package org.vaadin.appfoundation.test.i18n;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FillXmlTest {

    private File file = null;

    @Before
    public void setUp() {
        // create a random file
        file = new File(UUID.randomUUID().toString());
        try {
            file.createNewFile();

            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<tmx version=\"1.4\"><body></body></tmx>";
            FileOutputStream fileoutstream = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(fileoutstream, "UTF-8");
            writer.write(xml);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void updateFile1() {

    }

}
