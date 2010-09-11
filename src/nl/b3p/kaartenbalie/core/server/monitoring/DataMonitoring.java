/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.core.server.monitoring;

import java.util.Iterator;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataMonitoring {

    private static final Log log = LogFactory.getLog(DataMonitoring.class);
    private User user;
    private Organization organization;
    private ClientRequest clientRequest;
    private long operationStartTime;
    private Operation requestOperation;
    /*
     * Basically use this boolean to enable or disable the logging mechanism.
     */
    private static boolean enableMonitoring = false;

    public static void setEnableMonitoring(boolean state) {
        enableMonitoring = state;
    }

    public DataMonitoring() {
    }

    public void setUserAndOrganization(User user, Organization organization) {
        this.user = user;
        this.organization = organization;
    }

    /*
     * This is your init for to create a new RequestReport. Call it whenever you feel like starting a new
     * clientreport! You cannot make multiple reports at once though.
     */
    public void startClientRequest(String clientRequestURI, int bytesReceivedFromUser, long operationStartTime, String clientIp, String method) {
        if (!isEnableMonitoring()) {
            return;
        }
        this.operationStartTime = operationStartTime;
        requestOperation = new Operation();
        requestOperation.setType(Operation.REQUEST);
        requestOperation.setMsSinceRequestStart(new Long(getMSSinceStart()));
        requestOperation.setBytesReceivedFromUser(new Integer(bytesReceivedFromUser));

        clientRequest = new ClientRequest();
        clientRequest.setClientRequestURI(clientRequestURI);
        clientRequest.setMethod(method);
        clientRequest.setClientIp(clientIp);
    }

    public long getMSSinceStart() {
        return (System.currentTimeMillis() - operationStartTime);
    }
    /*
     * The function addServiceProvider request makes use of the reflection method described below. It is used to log
     * every call to a ServiceProvider. It should contain a parameterMap that matches the setters of the specified
     * sprClass.
     */

    public void addServiceProviderRequest(ServiceProviderRequest s) throws Exception {
        if (!isEnableMonitoring()) {
            return;
        }
        if (clientRequest==null) {
            return;
        }
        clientRequest.getServiceProviderRequests().add(s);

    }

    public void addRequestOperation(Operation o) throws Exception {
        if (!isEnableMonitoring()) {
            return;
        }
        if (clientRequest==null) {
            return;
        }
        clientRequest.getRequestOperations().add(o);
    }


    /*
     * This is your final statement in logging call. It will clean up your clientRequest and commit all the logged calls to
     * the database. After this you can restart your clientRequest without creating a new RequestReporting.
     */
    public void setClientRequestException(Exception ex) {
        if (clientRequest==null) {
            return;
        }
        clientRequest.setExceptionClass(ex.getClass());
        clientRequest.setExceptionMessage(ex.getMessage());
    }

    public void endClientRequest(String service, String operation, int bytesSentToUser, long totalResponseTime) {
        /*
        if (!isEnableMonitoring()) {
            return;
        }
        if (clientRequest==null) {
            return;
        }

        try {
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            requestOperation.setDuration(new Long(totalResponseTime));
            requestOperation.setBytesSentToUser(new Integer(bytesSentToUser));

            this.addRequestOperation(requestOperation);
            clientRequest.setService(service);
            clientRequest.setOperation(operation);
            clientRequest.setUser(user);
            clientRequest.setOrganization(organization);
            //Now Persist...
            Iterator iterRO = clientRequest.getRequestOperations().iterator();
            while (iterRO.hasNext()) {
                em.persist(iterRO.next());
            }
            Iterator iterSPR = clientRequest.getServiceProviderRequests().iterator();
            while (iterSPR.hasNext()) {
                em.persist(iterSPR.next());
            }
            em.persist(clientRequest);
            em.flush();
        } catch (Exception e) {
            log.error("", e);
        }
        */
        clientRequest = null;
    }

    public static Element createElement(Document doc, String createElementName, String textContent) {
        Element tmpElement = doc.createElement(createElementName);
        tmpElement.setTextContent(textContent);
        return tmpElement;
    }

    public static boolean isEnableMonitoring() {
        return enableMonitoring;
    }
}
