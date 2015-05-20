package nl.b3p.kaartenbalie.service.requesthandler;

import general.KaartenbalieTestCase;
import nl.b3p.servletTestAPI.InputStreamStub;
import nl.b3p.servletTestAPI.HttpServletResponseStub;
import nl.b3p.servletTestAPI.HttpServletRequestStub;
import nl.b3p.servletTestAPI.ByteArrayOutputStreamStub;
import nl.b3p.testStreet.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.ogc.utils.OGCRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class DataWrapperTest extends KaartenbalieTestCase{
    private HttpServletRequestStub request;
    private HttpServletResponseStub response;
    private DataWrapper wrapper;
    
    private String contentType = "text/html";
    private int bufferSize  = 2048;
    private String charactorEncoding    = "UTF-8";
    private int contentLength = 4096;
    private Locale locale;
    
    public DataWrapperTest(String name){
        super(name);        
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.request     = new HttpServletRequestStub();
        this.response    = new HttpServletResponseStub();
        
        this.wrapper     = new DataWrapper(this.request,this.response);
        this.locale      = new Locale("nl");
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.request    = null;
        this.response   = null;
        this.wrapper    = null;
        this.locale     = null;
    }
    
    /**
     * Test of setContentType method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetContentType() {
        this.wrapper.setContentType(contentType);
        
        assertStringEquals(this.contentType, this.wrapper.getContentType());
    }

    /**
     * Test of getContentType method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetContentType() {
        assertStringEquals(this.response.getContentType(),this.wrapper.getContentType());
    }

    /**
     * Test of getErrorContentType method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetErrorContentType() {
        assertNull(this.wrapper.getErrorContentType());
        
        this.wrapper.setErrorContentType("application/xml");
        assertStringEquals("application/xml",this.wrapper.getErrorContentType());
    }

    /**
     * Test of setErrorContentType method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetErrorContentType() {
        this.wrapper.setErrorContentType(contentType);
        
        assertStringEquals(this.contentType, this.wrapper.getErrorContentType());
    }

    /**
     * Test of getBufferSize method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetBufferSize() {
        assertEquals(this.response.getBufferSize(),this.wrapper.getBufferSize());
    }

    /**
     * Test of setBufferSize method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetBufferSize() {
        this.wrapper.setBufferSize(bufferSize);
        
        assertEquals(this.bufferSize,this.wrapper.getBufferSize());
    }

    /**
     * Test of getCharacterEncoding method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetCharacterEncoding() {
        assertStringEquals(this.response.getCharacterEncoding(),this.wrapper.getCharacterEncoding());
    }

    /**
     * Test of setCharacterEncoding method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetCharacterEncoding() {
        this.wrapper.setCharacterEncoding(charactorEncoding);
        
        assertStringEquals(this.charactorEncoding,this.wrapper.getCharacterEncoding());
    }

    /**
     * Test of getContentLength method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetContentLength() {
        assertEquals(0,this.wrapper.getContentLength());
    }
    
    /**
     * Test of correct reading from the content length of class DataWrapper
     * Every int takes 4 bytes.
     */
    public void testDataWrapperTest_SetContentLength(){
        ByteArrayOutputStreamStub stub  = new ByteArrayOutputStreamStub();
        int end = this.contentLength/4;
        for( int i=1; i<= end; i++){
            stub.write(34);
        }
        try {
            this.wrapper.write(stub);
            
            assertEquals(this.contentLength,this.wrapper.getContentLength());
        } 
        catch (IOException ex) {
            fail(ex.getLocalizedMessage());
            
            assertTrue(false);
        }
    }

    /**
     * Test of setDateHeader method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetDateHeader() {
        Date date   = new Date();
        long dateTime    = date.getTime();
        this.wrapper.setDateHeader("modified", dateTime);
        
        assertEquals(dateTime,this.response.getDateHeader("modified"));
    }

    /**
     * Test of setHeader method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetHeader() {
        String value = "NL";
        this.wrapper.setHeader("language", value);
        
        assertStringEquals(value,this.response.getheader("language"));
    }

    /**
     * Test of setIntHeader method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetIntHeader() {
        this.wrapper.setIntHeader("size", this.bufferSize);
        
        assertEquals(this.bufferSize,this.response.getIntHeader("size"));
    }

    /**
     * Test of getLocale method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetLocale() {
        assertEquals(this.locale,this.wrapper.getLocale());
    }

    /**
     * Test of setLocale method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetLocale() {
        Locale test = new Locale("en-us");
        this.wrapper.setLocale(test);
        
        assertEquals(test,this.wrapper.getLocale());
        assertEquals(test,this.response.getLocale());
    }

    /**
     * Test of setStatus method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetStatus(){
        this.wrapper.setStatus(404);
        assertEquals(404,this.response.getStatus());
    }

    /**
     * Test of getOutputStream method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetOutputStream() {
        try {
            assertEquals(this.response.getOutputStream(true),this.wrapper.getOutputStream());
        }
        catch (Exception ex) {
            fail(ex.getLocalizedMessage());
            
            assertTrue(false);
        }
    }

    /**
     * Test of write method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_WriteInputStream() {
        InputStreamStub stream  = new InputStreamStub();
        ArrayList<Byte> content = new ArrayList<Byte>();
        content.add((byte) 300); content.add((byte) 100); content.add((byte) 5);
        stream.setContent(content);
        
        try {
            this.wrapper.write(stream);
            
            assertTrue(true);
        }
        catch(IOException e){
            fail(e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }

    /**
     * Test of write method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_WriteOutputStream() {
        ByteArrayOutputStreamStub stub  = new ByteArrayOutputStreamStub();
        stub.write(30); stub.write(1); stub.write(100);
        
        try {
            this.wrapper.write(stub);
            
            assertTrue(true);
        } 
        catch (IOException ex) {
            fail(ex.getLocalizedMessage());
            
            assertTrue(false);
        }
    }

    /**
     * Test of getLayeringParameterMap method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetLayeringParameterMap() {
        Map map = new HashMap();
        
        assertEquals(map,this.wrapper.getLayeringParameterMap());
    }

    /**
     * Test of getRequestReporting method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetRequestReporting() {
        assertNull(this.wrapper.getRequestReporting());
    }

    /**
     * Test of setRequestReporting method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetRequestReporting() {
        DataMonitoring reporting = new DataMonitoring();
        
        this.wrapper.setRequestReporting(reporting);
        
        assertEquals(reporting,this.wrapper.getRequestReporting());
    }

    /**
     * Test of getOperation method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetOperation() {
        assertNull(this.wrapper.getOperation());
    }

    /**
     * Test of setOperation method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetOperation() {
        String operation    = "JUnit operation";
        
        this.wrapper.setOperation(operation);
        
        assertStringEquals(operation,this.wrapper.getOperation());
    }

    /**
     * Test of getOgcrequest method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetOgcrequest() {
        assertNull(this.wrapper.getOgcrequest());
    }

    /**
     * Test of setOgcrequest method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetOgcrequest() {
        OGCRequest OGrequest  = new OGCRequest();
        
        this.wrapper.setOgcrequest(OGrequest);
        assertEquals(OGrequest,this.wrapper.getOgcrequest());
    }

    /**
     * Test of getService method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetService() {
        assertNull(this.wrapper.getService());
    }

    /**
     * Test of setService method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_SetService() {
        String service  = "JUnit service";
        
        this.wrapper.setService(service);
        assertStringEquals(service,this.wrapper.getService());
    }

    /**
     * Test of getRequest method, of class DataWrapper.
     */
    @Test
    public void testDataWrapperTest_GetRequest() {
        assertEquals(this.request,this.wrapper.getRequest());
    }
}
