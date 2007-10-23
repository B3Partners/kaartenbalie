/*
 * ClientRequest.java
 *
 * Created on September 27, 2007, 4:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.print.Doc;
import nl.b3p.kaartenbalie.core.server.reporting.control.RequestReporting;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClientRequest implements XMLElement {
    
    private Integer id;
    private Date timeStamp;
    private String clientRequestURI;
    private Set serviceProviderRequests;
    private Set requestOperations;
    
    
    public ClientRequest() {
        setTimeStamp(new Date());
        setServiceProviderRequests(new HashSet());
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
   
    
    public Set getServiceProviderRequests() {
        return serviceProviderRequests;
    }
    
    public void setServiceProviderRequests(Set serviceProviderRequests) {
        this.serviceProviderRequests = serviceProviderRequests;
    }
    
    
    public String getClientRequestURI() {
        return clientRequestURI;
    }
    
    public void setClientRequestURI(String clientRequestURI) {
        this.clientRequestURI = clientRequestURI;
    }
    
    public Set getRequestOperations() {
        return requestOperations;
    }
    
    public void setRequestOperations(Set requestOperations) {
        this.requestOperations = requestOperations;
    }
    
    public Element toElement(Document doc, Element rootElement) {
        Element cre = doc.createElement("ClientRequest");
                cre.setAttribute("id", id.toString());
        cre.setAttribute("timeStamp", "" + timeStamp.getTime());

        //cre.appendChild(RequestReporting.createElement(doc, "bytesReceivedFromUser", bytesReceivedFromUser.toString()));
        //cre.appendChild(RequestReporting.createElement(doc, "bytesSendToUser", bytesSendToUser.toString()));        
        //cre.appendChild(RequestReporting.createElement(doc, "totalResponseTime", totalResponseTime.toString()));        
        cre.appendChild(RequestReporting.createElement(doc, "clientRequestURI", clientRequestURI.toString()));                
        //cre.appendChild(RequestReporting.createElement(doc, "bytesSendToUser", bytesSendToUser.toString()));        
        
        
        rootElement.appendChild(cre);
        
        
        Iterator i = serviceProviderRequests.iterator();
        while(i.hasNext()) {
            ServiceProviderRequest spr = (ServiceProviderRequest) i.next();
            spr.toElement(doc,cre);
            
        }
        
        return rootElement;
        
    }
    

    
    
    
   
    
    
}
