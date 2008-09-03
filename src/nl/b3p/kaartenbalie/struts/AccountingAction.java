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

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionPaymentDeposit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Chris Kramer
 */
public class AccountingAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(AccountingAction.class);
    protected static final String TRANSACTION = "transaction";

    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(TRANSACTION);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(FAILURE);
        map.put(TRANSACTION, crudProp);
        return map;
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward transaction(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idString = request.getParameter("id");
        request.setAttribute("id", idString);
        if (idString != null && idString.length() > 0) {
            Integer transactionId = new Integer(Integer.parseInt(idString));
            Organization organization = getOrganization(dynaForm, request);
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager();
            Transaction transaction = (Transaction) em.createQuery(
                    "FROM Transaction AS ta " +
                    "WHERE ta.id = :transactionId AND ta.account.organization.id = :organizationId ").setParameter("transactionId", transactionId).setParameter("organizationId", organization.getId()).getSingleResult();
            request.setAttribute("transaction", transaction);

            List layerPriceCompositions = em.createQuery(
                    "FROM LayerPriceComposition AS lpc " +
                    "WHERE lpc.transactionLayerUsage.id = :transactionId").setParameter("transactionId", transaction.getId()).getResultList();
            request.setAttribute("layerPriceCompositions", layerPriceCompositions);

            request.setAttribute("type", transaction.getClass().getSimpleName());
        }
        return unspecified(mapping, dynaForm, request, response);
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);

        Organization organization = getOrganization(form, request);
        form.set("selectedOrganization", FormUtils.IntegerToString(organization.getId()));

        int firstResult = FormUtils.StringToInt(form.getString("firstResult"));
        int listMax = FormUtils.StringToInt(form.getString("listMax"));
        if (firstResult == 0 && listMax == 0) {
            firstResult = 0;
            listMax = 20;
            form.set("firstResult", Integer.toString(firstResult));
            form.set("listMax", Integer.toString(listMax));
        }

        AccountManager am = AccountManager.getAccountManager(organization.getId());
        request.setAttribute("balance", new Double(am.getBalance()));
        request.setAttribute("layerUsages", am.getTransactions(firstResult, listMax, TransactionLayerUsage.class));
        request.setAttribute("paymentDeposits", am.getTransactions(firstResult, listMax, TransactionPaymentDeposit.class));

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List organizationlist = em.createQuery("from Organization order by name").getResultList();
        request.setAttribute("organizationlist", organizationlist);
    }

    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {

        log.debug("Getting entity manager ......");
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
        return FormUtils.StringToInteger(dynaForm.getString("selectedOrganization"));
    }
}
