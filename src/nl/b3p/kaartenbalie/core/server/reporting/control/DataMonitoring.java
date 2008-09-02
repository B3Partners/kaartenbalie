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
package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.operations.RequestOperation;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ClientRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.operations.Operation;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ServiceProviderRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataMonitoring {

    private static final Log log = LogFactory.getLog(DataMonitoring.class);
    private User user;
    private Organization organization;
    private ClientRequest clientRequest;
    private static List usServiceProviderRequest;
    private static List usRequestOperation;
    private long operationStartTime;
    private Map tRequestOperationMap;
    /*
     * Basically use this boolean to enable or disable the logging mechanism.
     */
    private static boolean enableMonitoring = false;
    /* These are static sets used for the reflectionfunction. It makes sure that it isnt possible to the Id and client
     * requestvalues for the ServiceProviderRequests and RequestOperations.
     */
    

    static {
        usServiceProviderRequest = new ArrayList();
        usServiceProviderRequest.add(new String("setClientRequest"));
        usServiceProviderRequest.add(new String("setId"));

        usRequestOperation = new ArrayList();
        usRequestOperation.add(new String("setClientRequest"));
        usRequestOperation.add(new String("setId"));
    }

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
        tRequestOperationMap = new HashMap();
        tRequestOperationMap.put("MsSinceRequestStart", new Long(getMSSinceStart()));
        tRequestOperationMap.put("BytesReceivedFromUser", new Integer(bytesReceivedFromUser));
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

    public void addServiceProviderRequest(Class sprClass, Map parameterMap) throws Exception {
        if (!isEnableMonitoring()) {
            return;
        }
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = DataMonitoring.createPOJOByReflection(ServiceProviderRequest.class, sprClass, parameterMap, overriddenParameters, usServiceProviderRequest);
        clientRequest.getServiceProviderRequests().add(reflectedObject);

    }

    /*
     * The function addRequestOperation request makes use of the reflection method described below. It is used to log
     * every call to a RequestOperation. It should contain a parameterMap that matches the setters of the specified
     * rqoClass.
     */
    public void addRequestOperation(Class rqoClass, Map parameterMap) throws Exception {
        if (!isEnableMonitoring()) {
            return;
        }
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = DataMonitoring.createPOJOByReflection(Operation.class, rqoClass, parameterMap, overriddenParameters, usRequestOperation);
        clientRequest.getRequestOperations().add(reflectedObject);
    }

    /*
     * This is a POJO reflection method. It is required to ensure the right usage of the controller classes. For our reporting
     * system we need to store objects that tell something about an event. The problem is, that these are pojo's that extend
     * other pojo and therefore it is impossible to create them all with just one function. The alternative was to create
     * the pojo in the external function itself and then give it to the controller. Alas this does not fit controller logic.
     * What does it do?
     * The function craetePOJOByReflection contains five parameters.
     * Class classFamily; this is the class that the target class should inherit.
     * Class targetObjectClass; this is the class that will be instantiated and returned.
     * Map parameterMap<String, Object>; this will fire the setters named after the keyvalue and give the object as its
     * parameter.
     * Map overriddenParameters<String, Object>; this will fire the functions that match the keyvalue with the given object
     * as its parameter.
     * List unsupportedMethods<String>; this is a safety map. If the parameterMap contains a keyvalue that matches an
     * unsupportedMethod, an Exception will be thrown. Use this to protect your class from setting things like the Id
     * and relations.
     */
    private static Object createPOJOByReflection(Class classFamily, Class targetObjectClass, Map parameterMap, Map overriddenParameters, List unsupportedMethods) throws Exception {
        try {
            if (!classFamily.isAssignableFrom(targetObjectClass)) {
                log.error(targetObjectClass.getSimpleName() + " is not a member of the " + classFamily.getSimpleName() + " family tree.");
                throw new Exception(targetObjectClass.getSimpleName() + " is not a member of the " + classFamily.getSimpleName() + " family tree.");
            }

            Object refObject = targetObjectClass.newInstance();
            if (parameterMap != null) {
                Iterator iterParamMap = parameterMap.keySet().iterator();
                while (iterParamMap.hasNext()) {
                    String keyValue = (String) iterParamMap.next();
                    if (unsupportedMethods.contains("set" + keyValue)) {
                        throw new NoSuchMethodException("Method 'set" + keyValue + "' for parameterValue " + keyValue + " is not supported.");
                    }
                    Object parameterValue = parameterMap.get(keyValue);
                    if (parameterValue != null) {
                        Method refMethod = refObject.getClass().getMethod("set" + keyValue, new Class[]{parameterValue.getClass()});
                        refMethod.invoke(refObject, new Object[]{parameterValue});
                    }
                }
            }
            if (overriddenParameters != null) {
                Iterator iterParamMap = overriddenParameters.keySet().iterator();
                while (iterParamMap.hasNext()) {
                    String keyValue = (String) iterParamMap.next();
                    Object parameterValue = overriddenParameters.get(keyValue);
                    if (parameterValue != null) {
                        Method refMethod = refObject.getClass().getMethod(keyValue, new Class[]{parameterValue.getClass()});
                        refMethod.invoke(refObject, new Object[]{parameterValue});
                    }
                }
            }
            return refObject;
        } catch (NoSuchMethodException ex) {
            Method[] methodList = targetObjectClass.getMethods();
            String validMessage = "Valid parameters are: \n";
            for (int i = 0; i < methodList.length; i++) {
                Method method = methodList[i];
                if (method.getName().startsWith("set") &&
                        (unsupportedMethods == null || (unsupportedMethods != null && !unsupportedMethods.contains(method.getName())))) {
                    String methodName = method.getName();
                    methodName = methodName.substring(3, methodName.length());
                    Class[] parameterTypes = method.getParameterTypes();
                    validMessage += methodName + "(";
                    for (int j = 0; j < parameterTypes.length; j++) {
                        validMessage += parameterTypes[j].getName() + ",";
                    }
                    validMessage = validMessage.substring(0, validMessage.length() - 1) + ") \n";
                }
            }
            throw new Exception(ex.getMessage() + "\n" + validMessage);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /*
     * This is your final statement in logging call. It will clean up your clientRequest and commit all the logged calls to
     * the database. After this you can restart your clientRequest without creating a new RequestReporting.
     */
    public void setClientRequestException(Exception ex) {
        clientRequest.setExceptionClass(ex.getClass());
        clientRequest.setExceptionMessage(ex.getMessage());
    }

    public void endClientRequest(String service, String operation, int bytesSendToUser, long totalResponseTime) {
        if (!isEnableMonitoring()) {
            return;
        }
        try {
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            tRequestOperationMap.put("Duration", new Long(totalResponseTime));
            tRequestOperationMap.put("BytesSendToUser", new Integer(bytesSendToUser));
            this.addRequestOperation(RequestOperation.class, tRequestOperationMap);
            clientRequest.setService(service);
            clientRequest.setOperation(operation);
            clientRequest.setUser(user);
            clientRequest.setOrganization(organization);
            //Now Persist...
            Iterator iterRO = clientRequest.getRequestOperations().iterator();
            em.persist(clientRequest);
            while (iterRO.hasNext()) {
                em.persist(iterRO.next());
            }
            em.flush();
            Iterator iterSPR = clientRequest.getServiceProviderRequests().iterator();
            while (iterSPR.hasNext()) {
                em.persist(iterSPR.next());
            }
            em.flush();
        } catch (Exception e) {
            log.error("", e);
        }
        clientRequest = null;
        tRequestOperationMap = null;
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
