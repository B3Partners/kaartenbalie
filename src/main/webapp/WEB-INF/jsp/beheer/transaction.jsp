<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2007-2011 B3Partners BV.

This program is distributed under the terms
of the GNU General Public License.

You should have received a copy of the GNU General Public License
along with this software. If not, see http://www.gnu.org/licenses/gpl.html

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<h1><fmt:message key="beheer.transaction.title"/></h1>

<c:if test="${not empty transaction}">

    <fieldset class="reportingFieldset">
        <legend><fmt:message key="beheer.transaction.details"/></legend>
        <label><fmt:message key="beheer.transaction.type"/> :</label>${type}<br/>
        <label><fmt:message key="beheer.transaction.created"/> :</label><fmt:formatDate  value="${transaction.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/><br/>
        <label><fmt:message key="beheer.transaction.edited"/> :</label><fmt:formatDate  value="${transaction.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/><br/>
        <label><fmt:message key="beheer.transaction.credits"/> :</label><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${transaction.creditAlteration}"/><br/>
        <label><fmt:message key="beheer.transaction.description"/> :</label>${transaction.description}<br/>
        <label><fmt:message key="beheer.transaction.status"/> : </label>
        <c:choose>
            <c:when test="${transaction.status == 0}">
                <fmt:message key="beheer.transaction.status.wachtrij"/>
            </c:when>
            <c:when test="${transaction.status == 1}">
                <fmt:message key="beheer.transaction.status.verwerkt"/>
            </c:when>
            <c:when test="${transaction.status == 2}">
                <fmt:message key="beheer.transaction.status.geweigerd"/>
            </c:when>
        </c:choose>
        <br/>
        <c:if test="${not empty transaction.errorMessage}">
            <label><fmt:message key="beheer.transaction.errorMessage"/> : </label>
            <div>${transaction.errorMessage}</div>
            <br/>
        </c:if>
        
        <p>
            <c:choose>
                <c:when  test="${type == 'TransactionLayerUsage'}">
                    <table id="transactionTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                        <thead>
                            <tr class="serverRijTitel">
                                <th style="width:150px;vertical-align:top;"><fmt:message key="beheer.transaction.table.layer"/></th>
                                <th><fmt:message key="beheer.transaction.table.type"/></th>
                                <th><fmt:message key="beheer.transaction.table.aantal"/></th>
                                <th><fmt:message key="beheer.transaction.table.projectie"/></th>
                                <th><fmt:message key="beheer.transaction.table.schaal"/></th>
                                <th><fmt:message key="beheer.transaction.table.service"/></th>
                                <th><fmt:message key="beheer.transaction.table.dienst"/></th>
                                <th><fmt:message key="beheer.transaction.table.prijs"/></th>                            
                            </tr>
                        </thead>
                        <c:forEach var="lpc" items="${layerPriceCompositions}">
                            <tr>
                                <td style="vertical-align:top;">${lpc.serverProviderPrefix}_${lpc.layerName}</td>
                                <td style="vertical-align:top;">${lpc.planType}</td>
                                <td style="vertical-align:top;">${lpc.units}</td>
                                <td style="vertical-align:top;">${lpc.projection}</td>                            
                                <td style="vertical-align:top;">${lpc.scale}</td>
                                <td style="vertical-align:top;">${lpc.service}</td>
                                <td style="vertical-align:top;">${lpc.operation}</td>
                                <td style="vertical-align:top;">
                                    <c:choose>
                                        <c:when test="${lpc.layerIsFree == true}">
                                            <fmt:message key="beheer.transaction.table.prijs.gratis"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${lpc.layerPrice}"/>        
                                        </c:otherwise>
                                    </c:choose>
                                </td>                                
                                
                            </tr>
                        </c:forEach>
                    </table>
                    <script type="text/javascript">
                        Table.stripe(document.getElementById('transactionTable'), 'table_alternate_tr');
                    </script>
                </c:when>
                <c:when  test="${type == 'TransactionPaymentDeposit'}">
                    <label><fmt:message key="beheer.transaction.koers"/> : </label>1:${transaction.txExchangeRate}<br/>
                    <label><fmt:message key="beheer.transaction.valuta"/> : </label><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${transaction.billingAmount}"/><br/>
                </c:when>            
            </c:choose>
        </p>
    </fieldset>
    
</c:if>
<p>
    <button onclick="location.href = '<html:rewrite page="/accounting.do"/>'"><fmt:message key="beheer.transaction.terug"/></button>
</p>