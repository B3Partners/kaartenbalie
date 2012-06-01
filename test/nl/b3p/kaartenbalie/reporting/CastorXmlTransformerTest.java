package nl.b3p.kaartenbalie.reporting;

import general.KaartenbalieTestCase;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.xml.transform.stream.StreamSource;
import nl.b3p.servletAPI.OutputStreamStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class CastorXmlTransformerTest extends KaartenbalieTestCase {

    private CastorXmlTransformer instance;

    public CastorXmlTransformerTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        String XslFile = "test.xsl";
        instance = new CastorXmlTransformer(XslFile, XslFile, this.data_dir, this.data_dir);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of getContentTypes method, of class CastorXmlTransformer.
     */
    @Test
    public void testGetContentTypes() {
        Map contentTypes = CastorXmlTransformer.getContentTypes();

        assertTrue(contentTypes.containsKey(CastorXmlTransformer.HTML));
        assertTrue(contentTypes.containsKey(CastorXmlTransformer.XSL));
        assertTrue(contentTypes.containsKey(CastorXmlTransformer.PDF));
        assertTrue(contentTypes.containsKey(CastorXmlTransformer.XML));
    }

    /**
     * Test of createXml method, of class CastorXmlTransformer.
     */
    @Test
    public void testCreateXml() {
        try {
            String rapport = "XML rapport";
            String result = instance.createXml(rapport);
            if (!result.contains("<string>" + rapport + "</string>")) {
                fail("Expected <string>" + rapport + "</string> in output");
            }
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of createHtml method, of class CastorXmlTransformer.
     */
    @Test
    public void testCreateHtml() {
        try {
            String rapport = "HTML rapport";
            String result = instance.createHtml(rapport);
            if (!result.contains("style=\"font-weight:bold\">" + rapport + "</span>")) {
                fail("Expected 'style=\"font-weight:bold\">" + rapport + "</span>' in output ");
            }
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of createXsl method, of class CastorXmlTransformer.
     */
    @Test
    public void testCreateXsl() {
        try {
            String rapport = "Xsl rapport";
            String result = instance.createXsl(rapport);
            if (!result.contains(rapport + "</string>")) {
                fail("Expected " + rapport + "</string> in output.: ");
            }
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of createPdf method, of class CastorXmlTransformer.
     */
    @Test
    public void testCreatePdf() {
        try {
            String rapport = "createPdf rapport";
            ByteArrayOutputStream result = instance.createPdf(rapport);

            assertTrue(true);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of objectToPDF method, of class CastorXmlTransformer.
     */
    @Test
    public void testObjectToPDF() {
        try {
            String rapport = "toPDF rapport";
            OutputStreamStub out = new OutputStreamStub();

            CastorXmlTransformer.objectToPDF(rapport, new StreamSource(this.data_dir + "test.xsl"), out, false);

            assertTrue(true);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }
}
