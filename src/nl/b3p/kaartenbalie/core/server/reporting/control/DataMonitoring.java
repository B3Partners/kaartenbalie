/*
 * ClientReporting.java
 *
 * Created on September 27, 2007, 4:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class DataMonitoring {
    private User user;
    private Organization organization;
    private ClientRequest clientRequest;
    private EntityManager em;
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
    private DataMonitoring() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    /*
     * This should be the default call to create a requestReporting object.
     */
    public DataMonitoring(User user, Organization organization) {
        this();
        if (user == null) {
            throw new Error("User is required for proper DataMonitoring...");
        }
        if (organization == null) {
            throw new Error("Organization is required for proper DataMonitoring...");
        }
        this.user = user;
        this.organization = organization;
        
    }
    
    /*
     * For testing purposes only. This function will be removed in the future
     */
    public void clean() {
        if (transactionActive()) {
            throw new Error("Cannot run the clean command when a reporting is in progress.. ");
        }
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //em.createQuery("DELETE FROM ServiceProviderRequest").executeUpdate();
        //em.flush();
        em.createQuery("DELETE FROM RequestOperation").executeUpdate();
        //em.flush();
        //em.createQuery("DELETE FROM ClientRequest").executeUpdate();
        em.flush();
        tx.commit();
        
    }
    
    /*
     * This is your init for to create a new RequestReport. Call it whenever you feel like starting a new
     * clientreport! You cannot make multiple reports at once though.
     */
    public void startClientRequest(String clientRequestURI, int bytesReceivedFromUser, long operationStartTime) {
        if (!enableMonitoring) return;
        EntityTransaction tx = em.getTransaction();
        if (transactionActive()) {
            throw new Error("Cannot start a new clientRequest without ending one first.");
        }
        tx.begin();
        try {
            this.operationStartTime = operationStartTime;
            tRequestOperationMap = new HashMap();
            tRequestOperationMap.put("MsSinceRequestStart", new Long(getMSSinceStart()));
            tRequestOperationMap.put("BytesReceivedFromUser", new Integer(bytesReceivedFromUser));
            clientRequest = new ClientRequest();
            clientRequest.setClientRequestURI(clientRequestURI);
            clientRequest.setUser(user);
            clientRequest.setOrganization(organization);
            em.persist(clientRequest);
        } catch (Exception e) {
            //TODO Error Handling... Should never happend though..
        }
    }
    
    public long getMSSinceStart() {
        return (System.currentTimeMillis() - operationStartTime);
    }
    /*
     * The function addServiceProvider request makes use of the reflection method described below. It is used to log
     * every call to a ServiceProvider. It should contain a parameterMap that matches the setters of the specified
     * sprClass.
     */
    public void addServiceProviderRequest(Class sprClass, Map parameterMap){
        if (!enableMonitoring) return;
        
        if (!transactionActive()) {
            throw new Error("Please start a clientrequest before attempting to log ServiceProviderRequests");
        }
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = DataMonitoring.createPOJOByReflection(ServiceProviderRequest.class, sprClass, parameterMap, overriddenParameters, usServiceProviderRequest);
        em.persist(reflectedObject);
    }
    
    /*
     * The function addRequestOperation request makes use of the reflection method described below. It is used to log
     * every call to a RequestOperation. It should contain a parameterMap that matches the setters of the specified
     * rqoClass.
     */
    public void addRequestOperation(Class rqoClass, Map parameterMap){
        if (!enableMonitoring) return;
        if (!transactionActive()) {
            throw new Error("Please start a clientrequest before attempting to log RequestOperations");
        }
        
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = DataMonitoring.createPOJOByReflection(Operation.class, rqoClass, parameterMap, overriddenParameters, usRequestOperation);
        em.persist(reflectedObject);
    }
    
    /*
     * This is a small function which will check wether a transaction is in a valid state in combination with a
     * clientRequest. It is used in different perspectives all throughout this class.
     */
    private boolean transactionActive() {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive() && clientRequest == null) {
            throw new Error("Transaction is active, but there is no clientrequest..");
        } else if (!tx.isActive() && clientRequest != null) {
            throw new Error("There is a clientRequest, but the transaction is not active!");
        }
        return (tx.isActive() && clientRequest != null);
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
    private static Object createPOJOByReflection(Class classFamily, Class targetObjectClass, Map parameterMap, Map overriddenParameters, List unsupportedMethods)  {
        try {
            if (!classFamily.isAssignableFrom(targetObjectClass)) {
                throw new Exception(targetObjectClass.getSimpleName() + " is not a member of the " + classFamily.getSimpleName() + " family tree.");
            }
            
            Object refObject = targetObjectClass.newInstance();
            if (parameterMap != null) {
                Iterator iterParamMap = parameterMap.keySet().iterator();
                while(iterParamMap.hasNext()) {
                    String keyValue = (String) iterParamMap.next();
                    if (unsupportedMethods.contains("set" + keyValue)) {
                        throw new NoSuchMethodException("Method 'set" + keyValue + "' for parameterValue " + keyValue + " is not supported.");
                    }
                    Object parameterValue = parameterMap.get(keyValue);
                    if (parameterValue != null) {
                        Method refMethod = refObject.getClass().getMethod("set" + keyValue,new Class[]{parameterValue.getClass()});
                        refMethod.invoke(refObject,new Object[]{parameterValue});
                    }
                }
            }
            if (overriddenParameters != null) {
                Iterator iterParamMap = overriddenParameters.keySet().iterator();
                while(iterParamMap.hasNext()) {
                    String keyValue = (String) iterParamMap.next();
                    Object parameterValue = overriddenParameters.get(keyValue);
                    if (parameterValue != null) {
                        Method refMethod = refObject.getClass().getMethod(keyValue,new Class[]{parameterValue.getClass()});
                        refMethod.invoke(refObject,new Object[]{parameterValue});
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
                    methodName = methodName.substring(3,methodName.length());
                    Class[] parameterTypes = method.getParameterTypes();
                    validMessage += methodName + "(";
                    for (int j = 0; j < parameterTypes.length; j++) {
                        validMessage += parameterTypes[j].getName() + ",";
                    }
                    validMessage = validMessage.substring(0,validMessage.length()-1) + ") \n";
                }
            }
            throw new Error(ex.getMessage() + "\n" + validMessage);
        } catch (Exception e) {
            
            //TODO Error Handling..
        }
        return null;
    }
    
    /*
     * This is your final statement in logging call. It will clean up your clientRequest and commit all the logged calls to
     * the database. After this you can restart your clientRequest without creating a new RequestReporting.
     */
    public void endClientRequest(int bytesSendToUser, long totalResponseTime) {
        if (!enableMonitoring) return;
        EntityTransaction tx = em.getTransaction();
        
        if (!transactionActive()) {
            throw new Error("Cannot end a clientRequest without starting it first.");
        }
        
        try {
            
            tRequestOperationMap.put("Duration", new Long(totalResponseTime));
            tRequestOperationMap.put("BytesSendToUser", new Integer(bytesSendToUser));
            this.addRequestOperation(RequestOperation.class, tRequestOperationMap);
            
            tx.commit();
            clientRequest = null;
            tRequestOperationMap = null;
        } catch (Exception e) {
            //TODO Error Handling...
            tx.rollback();
        }
    }
    
    
    public static Element createElement(Document doc, String createElementName, String textContent) {
        Element tmpElement = doc.createElement(createElementName);
        tmpElement.setTextContent(textContent);
        return tmpElement;
    }
    
    public static void main(String [] args) throws Exception {
        
     /*
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        EntityManager em = MyEMFDatabase.createEntityManager();
        DataMonitoring dm = new DataMonitoring((User) em.find(User.class, new Integer(1)));
        dm.clean();
      */
        Integer integer = new Integer(null);
        
    }
    
    
}