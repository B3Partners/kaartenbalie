<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">
<c:set var="form" value="${accountingForm.map}"/>

<H1><fmt:message key="beheer.accounting.title" /></H1>
<html:form action="/accounting" focus="selectedOrganization">
    <html:select property="selectedOrganization" onchange="submit();">
        <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
            <html:option value="${nOrganization.id}">
            ${nOrganization.name}
            </html:option>
        </c:forEach>
    </html:select> 
    <fmt:message key="beheer.accounting.startregel" />
    <html:select property="firstResult" onchange="submit();">
        <html:option value="0">1</html:option>
        <html:option value="19">20</html:option>
        <html:option value="99">100</html:option>
        <html:option value="999">1000</html:option>
        <html:option value="9999">10000</html:option>
    </html:select>
    <fmt:message key="beheer.accounting.regelsperpagina" />
    <html:select property="listMax" onchange="submit();">
        <html:option value="20">20</html:option>
        <html:option value="50">50</html:option>
        <html:option value="100">100</html:option>
        <html:option value="500">500</html:option>
    </html:select>     
</html:form>
<div class="tabcollection" id="accountCollection" style="margin-bottom: 15px; margin-top: 15px;">
    <div id="tabs">
        <ul id="tabul" style="width: 650px;">
            <li id="Withdrawals" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;"><fmt:message key="beheer.accounting.laatsteafboekingen" /></a></li>            
            <li id="Deposits" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;"><fmt:message key="beheer.accounting.laatstebijboekingen" /></a></li>
            <li id="AccountDetails" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;"><fmt:message key="beheer.accounting.accountdetails" /></a></li>
        </ul>
    </div>
    <script type="text/javascript">Nifty("ul#tabul a","medium transparent top");</script>
    <div id="sheets" style="height:500px;">
        <div id="AccountDetails" class="sheet">
            <fmt:message key="beheer.accounting.creditoverzicht" />:
            <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${balance}" /> credits
            <p>
                <html:link page="/deposit.do?orgId=${form.selectedOrganization}" module="/beheer">
                    <fmt:message key="beheer.accounting.creditsaanschaffen" />
                </html:link>
            </p>
        </div>
        <div id="Deposits" class="sheet">
            <div style="padding-right: 20px;">
                <table id="depositTable" style="width: 95%; padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="headerRijTitel">
                            <th><fmt:message key="beheer.accounting.aangemaakt" /></th>
                            <th><fmt:message key="beheer.accounting.verwerkt" /></th>
                            <th><fmt:message key="beheer.accounting.valuta" /></th>
                            <th><fmt:message key="beheer.accounting.omschrijving" /></th>
                            <th><fmt:message key="beheer.accounting.resultaat" /></th>
                            <th><fmt:message key="beheer.accounting.status" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tpd" items="${paymentDeposits}">
                            <tr>
                                <td>
                                    <a href="<html:rewrite page="/transaction.do?transaction=submit&id=${tpd.id}"/>" onclick="">
                                        <fmt:formatDate  value="${tpd.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatDate  value="${tpd.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.billingAmount}" />
                                </td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    ${tpd.description}
                                </td>                    
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.creditAlteration}" /> c
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tpd.status == 0}">
                                            <fmt:message key="beheer.accounting.status.wachtrij" />
                                        </c:when>
                                        <c:when test="${tpd.status == 1}">
                                            <fmt:message key="beheer.accounting.status.verwerkt" />
                                        </c:when>
                                        <c:when test="${tpd.status == 2}">
                                            <fmt:message key="beheer.accounting.status.geweigerd" />
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>   
                    </tbody>
                </table>
                <script type="text/javascript">
                    Table.stripe(document.getElementById('depositTable'), 'table_alternate_tr');
                </script>
            </div>
        </div>
        <div id="Withdrawals" class="sheet">
            <div style="padding-right: 20px;">
                <table id="withdrawlTable" style="width: 95%; padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="headerRijTitel">
                            <th><fmt:message key="beheer.accounting.withdrawal.aangemaakt" /></th>
                            <th><fmt:message key="beheer.accounting.withdrawal.verwerkt" /></th>
                            <th>&nbsp;</th>
                            <th style="padding-right: 10px;"><fmt:message key="beheer.accounting.withdrawal.credits" /></th>
                            <th><fmt:message key="beheer.accounting.withdrawal.status" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tlu" items="${layerUsages}">
                            <tr>
                                <td>
                                    <a href="<html:rewrite page="/transaction.do?transaction=submit&id=${tlu.id}"/>">
                                        <fmt:formatDate  value="${tlu.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatDate  value="${tlu.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td>&nbsp;</td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tlu.creditAlteration}"/> c
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tlu.status == 0}">
                                            <fmt:message key="beheer.accounting.status.wachtrij" />
                                        </c:when>
                                        <c:when test="${tlu.status == 1}">
                                            <fmt:message key="beheer.accounting.status.verwerkt" />
                                        </c:when>
                                        <c:when test="${tlu.status == 2}">
                                            <fmt:message key="beheer.accounting.status.geweigerd" />
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <script type="text/javascript">
                    Table.stripe(document.getElementById('withdrawlTable'), 'table_alternate_tr');
                </script>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript" type="text/javascript">
    window.onLoad = registerCollection('accountCollection', 'AccountDetails');
</script>