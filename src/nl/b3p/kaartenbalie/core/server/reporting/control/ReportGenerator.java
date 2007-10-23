/*
 * ReportGenerator.java
 *
 * Created on October 23, 2007, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class ReportGenerator {
    
    /** Creates a new instance of ReportGenerator */
    private EntityManager em;
    public ReportGenerator() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    
    
}
