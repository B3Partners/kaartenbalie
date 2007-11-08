/*
 * TestThread.java
 *
 * Created on October 1, 2007, 3:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.util.HashMap;
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetFeatureInfoRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;

/**
 *
 * @author Chris Kramer
 */
public class TestThread extends Thread {
    
    private int records = 5;
    private User user;
    public TestThread(int records, User user) {
        this.records = records;
        this.user = user;
    }
    
    public void run() {
        try {
            long totalTime = 0;
            
            DataMonitoring rr = new DataMonitoring(user, user.getOrganization());
            for (int i = 0; i< records; i++) {
                
                long startTime = System.currentTimeMillis();
                rr.startClientRequest("http://www.google.nl",1024, System.currentTimeMillis());
                
                
                Map paramMap = new HashMap();
                paramMap.put("BytesSend", new Long(4096));
                paramMap.put("BytesReceived", new Long(4096*8));
                paramMap.put("ResponseStatus", new Integer(404));
                paramMap.put("ProviderRequestURI", new String("www.google.nl"));
                rr.addServiceProviderRequest(WMSGetFeatureInfoRequest.class, paramMap);
                paramMap.put("WmsVersion", new String("1.0.0"));
                //paramMap.put("ClientRequest", new Integer(100));
                rr.addServiceProviderRequest(WMSGetMapRequest.class, paramMap);
                rr.endClientRequest(2048,0);
                long endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
            }
            System.out.println("Total time for " + records + " is " + (totalTime /1000) + " seconds, thats " + (totalTime / records) +  " ms per record.");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
