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

<c:set var="form" value="${depositForm.map}"/>

<html:javascript formName="depositForm" staticJavascript="false"/>
<html:form action="/deposit" onsubmit="return validateDepositForm(this)" focus="amount">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="orgId"/>
    <h1><fmt:message key="beheer.deposit.title" /> ${form.orgName}</h1>
    
    <table>
        <tr>
            <td colspan="2" style="height: 30px;" valign="top">
                <strong><fmt:message key="beheer.deposit.rate" />1 ${exchangeRate} <fmt:message key="beheer.deposit.rate" />2</strong>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="beheer.deposit.bedrag" />:
            </td>
            <td>
                <html:text property="amount" size="4" maxlength="4"/> , <html:text property="fraction" size="2" maxlength="2"/> <fmt:message key="beheer.deposit.bedrag.type" />
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="beheer.deposit.kenmerk" />:
            </td>
            <td>
                <html:text property="description" maxlength="12"/> <fmt:message key="beheer.deposit.kenmerk.maxcharacters" />
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="beheer.deposit.betaalmethode" />: 
            </td>
            <td>
                <html:radio property="paymentMethod" value="Ogone"/> Ogone
                <html:radio property="paymentMethod" value="DocData"/> DocData
                <html:radio property="paymentMethod" value="Bibit"/> Bibit
                <html:radio property="paymentMethod" value="iDeal"/> iDeal
            </td>
        </tr>
    </table>
    <html:submit property="save" accesskey="s" styleClass="knop">
        <fmt:message key="button.save"/>
    </html:submit>
    <html:cancel/>
</html:form>

