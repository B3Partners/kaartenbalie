/*
 * AccountingAction.java
 *
 * Created on November 19, 2007, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Chris Kramer
 */
public class AccountingAction extends KaartenbalieCrudAction {
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        EntityManager em = getEntityManager();
        User principalUser = (User) request.getUserPrincipal();
        User user = (User) em.find(User.class, principalUser.getId());
        Organization organization = user.getOrganization();
        
        AccountManager am = AccountManager.getAccountManager(organization.getId());
        request.setAttribute("balance", new Double(am.getBalance()));
        request.setAttribute("layerUsages", am.getTransactions(20, TransactionLayerUsage.class));
        request.setAttribute("paymentDeposits", am.getTransactions(20, TransactionLayerUsage.class));
        this.createLists(dynaForm, request);
        return mapping.findForward(SUCCESS);
    }
    
    
    
}
