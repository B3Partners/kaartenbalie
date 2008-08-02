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
package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionPaymentDeposit;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class DepositAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(DepositAction.class);
    protected static final String BACK = "back";

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward cancelled(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(FAILURE);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        /*
         * Alle gegevens voor de betaling.
         */
        Integer amount = (Integer) dynaForm.get("amount");
        Integer fraction = (Integer) dynaForm.get("fraction");
        String description = dynaForm.getString("description");
        String paymentMethod = dynaForm.getString("paymentMethod");
        BigDecimal billing = new BigDecimal(integersToDouble(amount, fraction));
        Integer exchangeRate = TransactionPaymentDeposit.getExchangeRate();
        if (billing.doubleValue() <= 0) {
            log.error("Amount cannot be less then or equal to zero!");
            throw new Exception("Amount cannot be less then or equal to zero!");
        }

        /*
         * Start de transactie
         */
        StringBuffer tdesc = new StringBuffer();
        if (description != null) {
            tdesc.append(description);
        }
        if (paymentMethod != null) {
            tdesc.append("/");
            tdesc.append(paymentMethod);
        }
        if (exchangeRate != null) {
            tdesc.append("/1:");
            tdesc.append(exchangeRate);
        }
        if (tdesc.length() > 32) {
            tdesc = new StringBuffer(tdesc.substring(0, 32));
        }
        Organization organization = getOrganization(dynaForm, request);
        AccountManager am = AccountManager.getAccountManager(organization.getId());
        TransactionPaymentDeposit tpd = (TransactionPaymentDeposit) am.prepareTransaction(
                TransactionPaymentDeposit.class, tdesc.toString());
        /*
         * Prijs, koers, conversie.
         */
        tpd.setBillingAmount(billing);
        BigDecimal creditAlt = billing.multiply(new BigDecimal(exchangeRate.intValue()));
        tpd.setCreditAlteration(creditAlt);
        tpd.setTxExchangeRate(exchangeRate);
        am.commitTransaction(tpd, (User) request.getUserPrincipal());

        ActionRedirect redirect = new ActionRedirect(mapping.findForward(BACK));
        redirect.addParameter("selectedOrganization", organization.getId().toString());
        return redirect;
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        request.setAttribute("exchangeRate", TransactionPaymentDeposit.getExchangeRate());

        Organization organization = getOrganization(form, request);
        form.set("orgName", organization.getName());
    }

    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request) {

        EntityManager em = getEntityManager();
        Organization organization = null;
        Integer id = getID(dynaForm);

        if (id == null) {
            User principalUser = (User) request.getUserPrincipal();
            if (principalUser == null) {
                return null;
            }
            User user = (User) em.find(User.class, principalUser.getId());
            if (user == null) {
                return null;
            }
            organization = user.getOrganization();
        } else {
            organization = (Organization) em.find(Organization.class, id);
        }

        return organization;
    }

    private Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("orgId"));
    }

    private static double integersToDouble(Integer amount, Integer fraction) throws Exception {
        if (amount == null && fraction == null) {
            log.error("Amount and fraction cannot both be null");
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
