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
import nl.b3p.kaartenbalie.core.server.reporting.domain.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.WMSRequest;

/**
 *
 * @author Chris Kramer
 */
public class TestThread extends Thread {
    
    
    
    public TestThread() {
    }
    
    public void run() {
        try {
            long totalTime = 0;
            int records = 100;
            RequestReporting rr = new RequestReporting(null);
            /*
            for (int i = 0; i< records; i++) {
                
                long startTime = System.currentTimeMillis();
                rr.startClientRequest("http://www.google.nl",1024);
                
                
                Map paramMap = new HashMap();
                paramMap.put("BytesSend", new Integer(4096));
                paramMap.put("BytesReceived", new Integer(4096*8));
                paramMap.put("ResponseStatus", new Integer(404));
                paramMap.put("ProviderRequestURI", new String("www.google.nl"));
                rr.addServiceProviderRequest("ServiceProviderRequest", paramMap);
                paramMap.put("WmsVersion", new String("1.0.0"));
                //paramMap.put("ClientRequest", new Integer(100));
                rr.addServiceProviderRequest("WMSRequest", paramMap);
                rr.endClientRequest(2048,0);
                long endTime = System.currentTimeMillis();
                //System.out.println(i + ":  Duration:" + (endTime - startTime));
                totalTime += (endTime - startTime);
            }
            System.out.println("Totaal time for " + records + " records: " + totalTime + "ms, Average = " + (totalTime / records) + "ms");
             **/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
