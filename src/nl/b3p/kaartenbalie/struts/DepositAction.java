/*
 * AccountingAction.java
 *
 * Created on November 19, 2007, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionPaymentDeposit;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Chris Kramer
 */
public class DepositAction extends KaartenbalieCrudAction {
    
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
         * Alle gegevens voor de betaling.
         */
        Integer amount = (Integer) dynaForm.get("amount");
        Integer fraction = (Integer) dynaForm.get("fraction");
        String description = dynaForm.getString("description");
        String paymentMethod = dynaForm.getString("paymentMethod");
        BigDecimal billing  = new BigDecimal(integersToDouble(amount, fraction));
        Integer exchangeRate = TransactionPaymentDeposit.getExchangeRate();
        if (billing.doubleValue() <= 0) {
            throw new Exception("Amount cannot be less then or equal to zero!");
        }
        
        /*
         * Start de transactie
         */
        Organization organization = getOrganization(request);
        AccountManager am = AccountManager.getAccountManager(organization.getId());
        TransactionPaymentDeposit tpd = (TransactionPaymentDeposit) am.prepareTransaction(
                TransactionPaymentDeposit.class,
                "KB koers 1:" + exchangeRate.toString() + " " + description);
        /*
         * Prijs, koers, conversie.
         */
        tpd.setBillingAmount(billing);
        BigDecimal creditAlt = billing.multiply(new BigDecimal(exchangeRate.intValue()));
        tpd.setCreditAlteration(creditAlt);
        tpd.setTxExchangeRate(exchangeRate);
        am.commitTransaction(tpd, (User) request.getUserPrincipal());
        return super.create(mapping, dynaForm, request, response);
    }
    
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        request.setAttribute("exchangeRate", TransactionPaymentDeposit.getExchangeRate());
    }
    
    private Organization getOrganization(HttpServletRequest request) {
        EntityManager em = getEntityManager();
        User principalUser = (User) request.getUserPrincipal();
        User user = (User) em.find(User.class, principalUser.getId());
        return user.getOrganization();
    }
    
    private static double integersToDouble(Integer amount, Integer fraction) throws Exception{
        if (amount == null && fraction == null) {
            throw new Exception("Amount and fraction cannot both be null");
        }
        if (amount == null) {
            amount = new Integer(0);
        }
        if (fraction == null) {
            fraction = new Integer(0);
        }
        String strDouble = amount.toString() + "." + fraction.toString();
        return Double.parseDouble(strDouble);
    }
}
