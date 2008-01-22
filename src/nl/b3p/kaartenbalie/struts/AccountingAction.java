/*
 * AccountingAction.java
 *
 * Created on November 19, 2007, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionPaymentDeposit;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Chris Kramer
 */
public class AccountingAction extends KaartenbalieCrudAction {
    
    protected static final String TRANSACTION                        = "transaction";
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(TRANSACTION);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.accounting.transaction.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.accounting.transaction.succes");
        map.put(TRANSACTION, crudProp);
        return map;
    }
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public ActionForward transaction(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idString = request.getParameter("id");
        request.setAttribute("id", idString);
        if (idString != null && idString.length() > 0) {
            Integer transactionId = new Integer(Integer.parseInt(idString));
            Organization organization = getOrganization(request);
            EntityManager em = getEntityManager();
            Transaction transaction = (Transaction) em.createQuery(
                    "FROM Transaction AS ta " +
                    "WHERE ta.id = :transactionId AND ta.account.organization.id = :organizationId ")
                    .setParameter("transactionId", transactionId)
                    .setParameter("organizationId", organization.getId())
                    .getSingleResult();
            request.setAttribute("transaction",transaction);
            
            List layerUsageMutations = em.createQuery(
                    "FROM LayerUsageMutation AS lum " +
                    "WHERE lum.transactionLayerUsage.id = :transactionId")
                    .setParameter("transactionId", transaction.getId())
                    .getResultList();
            request.setAttribute("LayerUsageMutations", layerUsageMutations );
            
            request.setAttribute("type",transaction.getClass().getSimpleName());
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        Organization organization = getOrganization(request);
        AccountManager am = AccountManager.getAccountManager(organization.getId());
        request.setAttribute("balance", new Double(am.getBalance()));
        request.setAttribute("layerUsages", am.getTransactions(20, TransactionLayerUsage.class));
        request.setAttribute("paymentDeposits", am.getTransactions(20, TransactionPaymentDeposit.class));
        
    }
    
    private Organization getOrganization(HttpServletRequest request) {
        EntityManager em = getEntityManager();
        User principalUser = (User) request.getUserPrincipal();
        User user = (User) em.find(User.class, principalUser.getId());
        return user.getOrganization();
    }
    
    
    
    
}
