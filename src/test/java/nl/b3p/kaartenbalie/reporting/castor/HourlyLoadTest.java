package nl.b3p.kaartenbalie.reporting.castor;

import general.KaartenbalieTestCase;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import nl.b3p.servletAPI.ContentHandlerStub;
import nl.b3p.servletAPI.InputStreamReaderStub;
import nl.b3p.servletAPI.InputStreamStub;
import nl.b3p.servletAPI.OutputStreamStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class HourlyLoadTest extends KaartenbalieTestCase {

    private HourlyLoad instance;

    public HourlyLoadTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.instance = new HourlyLoad();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        this.instance = null;
    }

    /**
     * Test of deleteBytesReceivedSum method, of class HourlyLoad.
     */
    @Test
    public void testDeleteBytesReceivedSum() {
        this.instance.setBytesReceivedSum(100);
        this.instance.deleteBytesReceivedSum();
        assertFalse(this.instance.hasBytesReceivedSum());
    }

    /**
     * Test of deleteBytesSentSum method, of class HourlyLoad.
     */
    @Test
    public void testDeleteBytesSentSum() {
        this.instance.setBytesSentSum(100);
        this.instance.deleteBytesSentSum();
        assertFalse(this.instance.hasBytesSentSum());
    }

    /**
     * Test of deleteCount method, of class HourlyLoad.
     */
    @Test
    public void testDeleteCount() {
        this.instance.setCount(100);
        this.instance.deleteCount();
        assertFalse(this.instance.hasCount());
    }

    /**
     * Test of deleteDurationAvg method, of class HourlyLoad.
     */
    @Test
    public void testDeleteDurationAvg() {
        this.instance.setDurationAvg(100);
        this.instance.deleteDurationAvg();
        assertFalse(this.instance.hasDurationAvg());
    }

    /**
     * Test of deleteDurationMax method, of class HourlyLoad.
     */
    @Test
    public void testDeleteDurationMax() {
        this.instance.setDurationMax(100);
        this.instance.deleteDurationMax();
        assertFalse(this.instance.hasDurationMax());
    }

    /**
     * Test of deleteHour method, of class HourlyLoad.
     */
    @Test
    public void testDeleteHour() {
        this.instance.setHour(100);
        this.instance.deleteHour();
        assertFalse(this.instance.hasHour());
    }

    /**
     * Test of getBytesReceivedSum method, of class HourlyLoad.
     */
    @Test
    public void testGetBytesReceivedSum() {
        int received = 100;

        assertEquals(0, this.instance.getBytesReceivedSum());
        this.instance.setBytesReceivedSum(received);
        assertEquals(received, this.instance.getBytesReceivedSum());
        assertTrue(this.instance.hasBytesReceivedSum());
    }

    /**
     * Test of getBytesSentSum method, of class HourlyLoad.
     */
    @Test
    public void testGetBytesSentSum() {
        int sent = 100;

        assertEquals(0, this.instance.getBytesSentSum());
        this.instance.setBytesSentSum(sent);
        assertEquals(sent, this.instance.getBytesSentSum());
        assertTrue(this.instance.hasBytesSentSum());
    }

    /**
     * Test of getCount method, of class HourlyLoad.
     */
    @Test
    public void testGetCount() {
        int count = 100;

        assertEquals(0, this.instance.getCount());
        this.instance.setCount(100);
        assertEquals(count, this.instance.getCount());
        assertTrue(this.instance.hasCount());
    }

    /**
     * Test of getDate method, of class HourlyLoad.
     */
    @Test
    public void testGetDate() {
        org.exolab.castor.types.Date date = new org.exolab.castor.types.Date();

        assertNull(this.instance.getDate());
        this.instance.setDate(date);
        assertEquals(date, this.instance.getDate());
    }

    /**
     * Test of getDurationAvg method, of class HourlyLoad.
     */
    @Test
    public void testGetDurationAvg() {
        int duration = 100;

        assertEquals(0, this.instance.getDurationAvg());
        this.instance.setDurationAvg(duration);
        assertEquals(duration, this.instance.getDurationAvg());
        assertTrue(this.instance.hasDurationAvg());
    }

    /**
     * Test of getDurationMax method, of class HourlyLoad.
     */
    @Test
    public void testGetDurationMax() {
        int duration = 100;

        assertEquals(0, this.instance.getDurationMax());
        this.instance.setDurationMax(duration);
        assertEquals(duration, this.instance.getDurationMax());
        assertTrue(this.instance.hasDurationMax());
    }

    /**
     * Test of getHour method, of class HourlyLoad.
     */
    @Test
    public void testGetHour() {
        int hour = 100;

        assertEquals(0, this.instance.getHour());
        this.instance.setHour(hour);
        assertEquals(hour, this.instance.getHour());
        assertTrue(this.instance.hasHour());
    }

    /**
     * Test of hasBytesReceivedSum method, of class HourlyLoad.
     */
    @Test
    public void testHasBytesReceivedSum() {
        assertFalse(this.instance.hasBytesReceivedSum());
        this.instance.setBytesReceivedSum(100);
        assertTrue(this.instance.hasBytesReceivedSum());
    }

    /**
     * Test of hasBytesSentSum method, of class HourlyLoad.
     */
    @Test
    public void testHasBytesSentSum() {
        assertFalse(this.instance.hasBytesSentSum());
        this.instance.setBytesSentSum(100);
        assertTrue(this.instance.hasBytesSentSum());
    }

    /**
     * Test of hasCount method, of class HourlyLoad.
     */
    @Test
    public void testHasCount() {
        assertFalse(this.instance.hasCount());
        this.instance.setCount(100);
        assertTrue(this.instance.hasCount());
    }

    /**
     * Test of hasDurationAvg method, of class HourlyLoad.
     */
    @Test
    public void testHasDurationAvg() {
        assertFalse(this.instance.hasDurationAvg());
        this.instance.setDurationAvg(100);
        assertTrue(this.instance.hasDurationAvg());
    }

    /**
     * Test of hasDurationMax method, of class HourlyLoad.
     */
    @Test
    public void testHasDurationMax() {
        assertFalse(this.instance.hasDurationMax());
        this.instance.setDurationMax(100);
        assertTrue(this.instance.hasDurationMax());
    }

    /**
     * Test of hasHour method, of class HourlyLoad.
     */
    @Test
    public void testHasHour() {
        assertFalse(this.instance.hasHour());
        this.instance.setHour(100);
        assertTrue(this.instance.hasHour());
    }

    /**
     * Test of isValid method, of class HourlyLoad.
     */
    @Test
    public void testIsValid() {
        assertFalse(this.instance.isValid());
        this.makeValid();
        assertTrue(this.instance.isValid());
    }

    /**
     * Test of marshal method, of class HourlyLoad.
     */
    @Test
    public void testMarshal_Writer() {
        try {
            java.io.Writer out = new OutputStreamWriter(new OutputStreamStub());
            this.makeValid();
            this.instance.marshal(out);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of marshal method, of class HourlyLoad.
     */
    @Test
    public void testMarshal_ContentHandler() {
        try {
            org.xml.sax.ContentHandler handler = new ContentHandlerStub();
            this.makeValid();
            this.instance.marshal(handler);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of setBytesReceivedSum method, of class HourlyLoad.
     */
    @Test
    public void testSetBytesReceivedSum() {
        int received = 100;
        this.instance.setBytesReceivedSum(received);
        assertEquals(received, this.instance.getBytesReceivedSum());
        assertTrue(this.instance.hasBytesReceivedSum());
    }

    /**
     * Test of setBytesSentSum method, of class HourlyLoad.
     */
    @Test
    public void testSetBytesSentSum() {
        int sent = 100;
        this.instance.setBytesSentSum(sent);
        assertEquals(sent, this.instance.getBytesSentSum());
        assertTrue(this.instance.hasBytesSentSum());
    }

    /**
     * Test of setCount method, of class HourlyLoad.
     */
    @Test
    public void testSetCount() {
        int count = 100;
        this.instance.setCount(count);
        assertEquals(count, this.instance.getCount());
        assertTrue(this.instance.hasCount());
    }

    /**
     * Test of setDate method, of class HourlyLoad.
     */
    @Test
    public void testSetDate() {
        org.exolab.castor.types.Date date = new org.exolab.castor.types.Date();
        this.instance.setDate(date);
        assertEquals(date, this.instance.getDate());
    }

    /**
     * Test of setDurationAvg method, of class HourlyLoad.
     */
    @Test
    public void testSetDurationAvg() {
        int duration = 100;
        this.instance.setDurationAvg(duration);
        assertEquals(duration, this.instance.getDurationAvg());
        assertTrue(this.instance.hasDurationAvg());
    }

    /**
     * Test of setDurationMax method, of class HourlyLoad.
     */
    @Test
    public void testSetDurationMax() {
        int duration = 100;
        this.instance.setDurationMax(duration);
        assertEquals(duration, this.instance.getDurationMax());
        assertTrue(this.instance.hasDurationMax());
    }

    /**
     * Test of setHour method, of class HourlyLoad.
     */
    @Test
    public void testSetHour() {
        int hour = 100;
        this.instance.setHour(hour);
        assertEquals(hour, this.instance.getHour());
        assertTrue(this.instance.hasHour());
    }

    /**
     * Test of unmarshal method, of class HourlyLoad.
     */
    @Test
    public void testUnmarshal() {
        try {
            InputStreamStub stream = new InputStreamStub();
            ArrayList<Byte> content = new ArrayList<Byte>();
            for (int i = 0; i < 15; i++) {
                content.add((byte) (i * 5 + 10));
            }
            stream.setContent(content);
            InputStreamReader reader = new InputStreamReaderStub((InputStream) stream);
            HourlyLoad.unmarshal(reader);
        } catch (Exception e) {
            String message = e.getLocalizedMessage();
            if (message.contains("Parsing Error : Content is not allowed in prolog")) {
                assertTrue(true);
            } else {
                fail("Exception :" + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Test of validate method, of class HourlyLoad.
     */
    @Test
    public void testValidate() {
        try {
            this.instance.validate();
            fail("Function should throw a exception");
        } catch (Exception e) {
            assertTrue(true);
        }

        try {
            this.makeValid();

            this.instance.validate();
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Makes the current HoulyLoad instance valid
     */
    private void makeValid() {
        org.exolab.castor.types.Date date = new org.exolab.castor.types.Date();
        this.instance.setDate(date);
        this.instance.setHour(100);
        this.instance.setCount(3);
        this.instance.setDurationAvg(80);
        this.instance.setDurationMax(100);
        this.instance.setBytesReceivedSum(10);
        this.instance.setBytesSentSum(1000);
    }
}
