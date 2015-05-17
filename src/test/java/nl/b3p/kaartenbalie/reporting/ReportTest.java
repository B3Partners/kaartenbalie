package nl.b3p.kaartenbalie.reporting;

import general.KaartenbalieTestCase;
import java.util.Calendar;
import java.util.Date;
import nl.b3p.kaartenbalie.core.server.Organization;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class ReportTest extends KaartenbalieTestCase {
    private Report instance;
    
    public ReportTest(String name) {
        super(name);
    }

    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance = new Report();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
    }

    /**
     * Test of getId method, of class Report.
     */
    @Test
    public void testGetId() {
        assertEquals(null,instance.getId());
    }

    /**
     * Test of setId method, of class Report.
     */
    @Test
    public void testSetId() {
        Integer test    = 12;
        instance.setId(test);
        assertEquals(test,instance.getId());
    }

    /**
     * Test of getReportDate method, of class Report.
     */
    @Test
    public void testGetReportDate() {
        Date dateNow    = new Date();
        assertEquals(dateNow,instance.getReportDate());        
    }

    /**
     * Test of setReportDate method, of class Report.
     */
    @Test
    public void testSetReportDate() {
        Date dateCheck  = this.generateDate();
        
        instance.setReportDate(dateCheck);
        assertEquals(dateCheck,instance.getReportDate());
    }

    /**
     * Test of getProcessingTime method, of class Report.
     */
    @Test
    public void testGetProcessingTime() {
        assertEquals(null,instance.getProcessingTime());
    }

    /**
     * Test of setProcessingTime method, of class Report.
     */
    @Test
    public void testSetProcessingTime() {
        Long time   = Long.MIN_VALUE;
        instance.setProcessingTime(time);
        assertEquals(time,instance.getProcessingTime());
    }

    /**
     * Test of getOrganization method, of class Report.
     */
    @Test
    public void testGetOrganization() {
        assertEquals(null,instance.getOrganization());
    }

    /**
     * Test of setOrganization method, of class Report.
     */
    @Test
    public void testSetOrganization() {
        Organization organization   = new Organization();
        instance.setOrganization(organization);
        assertEquals(organization,instance.getOrganization());
    }

    /**
     * Test of getReportXML method, of class Report.
     */
    @Test
    public void testGetReportXML() {
        assertEquals(null,instance.getReportXML());
    }

    /**
     * Test of setReportXML method, of class Report.
     */
    @Test
    public void testSetReportXML() {
        String report = "report Test";
        instance.setReportXML(report);
        assertStringEquals(report,instance.getReportXML());
    }

    /**
     * Test of getStartDate method, of class Report.
     */
    @Test
    public void testGetStartDate() {
        assertEquals(null,instance.getStartDate());
    }

    /**
     * Test of setStartDate method, of class Report.
     */
    @Test
    public void testSetStartDate() {
        Date dateCheck  = this.generateDate();
        
        instance.setStartDate(dateCheck);
        assertEquals(dateCheck,instance.getStartDate());
    }

    /**
     * Test of getEndDate method, of class Report.
     */
    @Test
    public void testGetEndDate() {
        assertEquals(null,instance.getEndDate());
    }

    /**
     * Test of setEndDate method, of class Report.
     */
    @Test
    public void testSetEndDate() {
        Date dateCheck  = this.generateDate();
        
        instance.setEndDate(dateCheck);
        assertEquals(dateCheck,instance.getEndDate());
    }

    /**
     * Test of getReportMime method, of class Report.
     */
    @Test
    public void testGetReportMime() {
        assertEquals(null,instance.getReportMime());
    }

    /**
     * Test of setReportMime method, of class Report.
     */
    @Test
    public void testSetReportMime() {
        String reportMime   = "Report MIME";
        instance.setReportMime(reportMime);
        assertStringEquals(reportMime,instance.getReportMime());
    }

    /**
     * Test of getName method, of class Report.
     */
    @Test
    public void testGetName() {
        assertEquals(null,instance.getName());
    }

    /**
     * Test of setName method, of class Report.
     */
    @Test
    public void testSetName() {
        String name = "test name";
        instance.setName(name);
        assertStringEquals(name,instance.getName());
    }
}
