/*
 * ReportThreadTemplate.java
 *
 * Created on November 2, 2007, 9:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.util.Map;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ReportTemplate;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread{
    
    
    protected ReportGenerator reportGenerator;
    protected Map parameters;
    protected User user;
    protected EntityManager em;
    protected ReportTemplate reportTemplate;
    protected int threadState;


    
    public ReportThreadTemplate() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    protected void notifyDone() {
        em.close();
        reportGenerator.notifyDone(this);
    }
    protected void notifyBreak(Exception exception) {
        em.close();
        reportGenerator.notifyBreak(this, exception);
    }
    public abstract void setParameters(Map parameters) throws Exception;
    public void setUser(User user) {
        this.user = user;
    }
    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }
    
    
    
}
