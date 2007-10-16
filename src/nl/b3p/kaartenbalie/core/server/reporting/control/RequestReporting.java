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
import nl.b3p.kaartenbalie.core.server.persistence.ManagedPersistence;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ClientRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.RequestOperation;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ServiceProviderRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RequestReporting {
    private User user;
    private ClientRequest clientRequest;
    private EntityManager em;
    private static List usServiceProviderRequest;
    private static List usRequestOperation;
    private long reportStopwatch = 0;
    private long lapTimer;
    private static boolean outputLaptimer = false;
    
    static {
        usServiceProviderRequest = new ArrayList();
        usServiceProviderRequest.add(new String("setClientRequest"));
        usServiceProviderRequest.add(new String("setId"));
        
        usRequestOperation = new ArrayList();
        usRequestOperation.add(new String("setClientRequest"));
        usRequestOperation.add(new String("setId"));
    }
    
    public RequestReporting() {
        em = ManagedPersistence.createEntityManager();
    }
    public RequestReporting(User user) {
        this();
        if (user != null) {
            
            this.user = (User) em.find(User.class, user.getId());
        }
        
    }
    
    public void clean() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createQuery("DELETE FROM ServiceProviderRequest").executeUpdate();
        em.createQuery("DELETE FROM RequestOperation").executeUpdate();
        em.createQuery("DELETE FROM ClientRequest").executeUpdate();
        tx.commit();
        
    }
    
    public void startClientRequest(String clientRequestURI, int bytesReceivedFromUser) {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive() || clientRequest != null) {
            throw new Error("Cannot start a new clientRequest without ending one first.");
        }
        tx.begin();
        try {
            clientRequest = new ClientRequest();
            clientRequest.setClientRequestURI(clientRequestURI);
            clientRequest.setBytesReceivedFromUser(new Integer(bytesReceivedFromUser));
            em.persist(clientRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addServiceProviderRequest(Class sprClass, Map parameterMap){
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = RequestReporting.createPOJOByReflection(ServiceProviderRequest.class, sprClass, parameterMap, overriddenParameters, usServiceProviderRequest);
        em.persist(reflectedObject);
    }
    
    public void addRequestOperation(Class rqoClass, Map parameterMap){
        Map overriddenParameters = new HashMap();
        overriddenParameters.put("setClientRequest", clientRequest);
        Object reflectedObject = RequestReporting.createPOJOByReflection(RequestOperation.class, rqoClass, parameterMap, overriddenParameters, usRequestOperation);
        em.persist(reflectedObject);
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
            String validMessage = "Valid parameters are: ";
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
            ex.printStackTrace();
            throw new Error(ex.getMessage() + "\n" + validMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void endClientRequest(int bytesSendToUser, long totalResponseTime) {
        EntityTransaction tx = em.getTransaction();
        
        if (!tx.isActive() || clientRequest == null) {
            throw new Error("Cannot end a clientRequest without starting it first.");
        }
        
        try {
            clientRequest.setBytesSendToUser(new Integer(bytesSendToUser));
            clientRequest.setTotalResponseTime(new Long(totalResponseTime));
            tx.commit();
            clientRequest = null;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }
    }
    
    
    
    
    public static void main(String [ ] args) throws Exception {
        
        
        
        
    }

    
    
    
    
    
    
    
}
