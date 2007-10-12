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
import java.util.Iterator;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.persistence.ManagedPersistence;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ClientRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ServiceProviderRequest;

public class RequestReporting {
    private User user;
    private ClientRequest clientRequest;
    private static final String sprPackage = "nl.b3p.kaartenbalie.core.server.reporting.domain";
    private EntityManager em;
    public RequestReporting() {
        super();
        em = ManagedPersistence.getEntityManager();
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
        em.createQuery("DELETE FROM ClientRequest").executeUpdate();
        tx.commit();

    }

    public void startClientRequest(String clientRequestURI, int bytesReceivedFromUser) throws Exception{

        EntityTransaction tx = em.getTransaction();
        if (tx.isActive() || clientRequest != null) {
            throw new Exception("Cannot start a new clientRequest without ending one first.");
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

    public void addServiceProviderRequest(String sprClassName, Map parameterMap) throws Exception{
        Class sprClass = Class.forName(sprPackage + "." + sprClassName);
        try {

            if (!ServiceProviderRequest.class.isAssignableFrom(sprClass)) {
                throw new Exception(sprClass.getSimpleName() + " is not a member of the ServiceProviderRequest family tree.");
            }

            Object sprObject = sprClass.newInstance();
            Iterator iterParamMap = parameterMap.keySet().iterator();
            while(iterParamMap.hasNext()) {
                String keyValue = (String) iterParamMap.next();
                if (keyValue.equals("Id") || keyValue.equals("ClientRequest")) {
                    throw new NoSuchMethodException("Method for parameterValue " + keyValue + " is not accessible.");
                }
                Object parameterValue = parameterMap.get(keyValue);
                Method sprMethod = sprObject.getClass().getMethod("set" + keyValue,new Class[]{parameterValue.getClass()});
                sprMethod.invoke(sprObject,new Object[]{parameterValue});
            }
            Method methodCR = sprObject.getClass().getMethod("setClientRequest", new Class[]{ClientRequest.class});
            methodCR.invoke(sprObject,new Object[]{clientRequest});

            em.persist(sprObject);
        } catch (NoSuchMethodException ex) {
            Method[] methodList = sprClass.getMethods();
            String validMessage = "Valid parameters are: ";
            for (int i = 0; i < methodList.length; i++) {
                Method method = methodList[i];
                if (method.getName().startsWith("set") && !method.getName().equalsIgnoreCase("setid") && !method.getName().equalsIgnoreCase("setClientRequest")) {
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
            throw new Exception(ex.getMessage() + "\n" + validMessage);
        }
    }

    public void endClientRequest(int bytesSendToUser, long totalResponseTime) throws Exception{
        EntityTransaction tx = em.getTransaction();

        if (!tx.isActive() || clientRequest == null) {
            throw new Exception("Cannot end a clientRequest without starting it first.");
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




    public void getUsers() {
        EntityManager em = ManagedPersistence.getEntityManager();
    }


    public static void main(String [ ] args) throws Exception {

        
        System.out.println(ManagedPersistence.getPersistenceUnitName());
        
        
        System.exit(0);
       // RequestReporting rr = new RequestReporting();
        
        
        /*
        rr.clean();

        for (int i = 0; i< 50; i++) {
            TestThread t = new TestThread();
            t.start();
        }
         */


    }






}
