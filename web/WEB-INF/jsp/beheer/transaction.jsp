<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:if test="${not empty transaction}">
    
    <fieldset>
        <legend>Details voor transactie</legend>
        <label>Type :</label>${type}<br/>
        <label>Datum aangemaakt :</label><fmt:formatDate  value="${transaction.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/><br/>
        <label>Datum verwerkt :</label><fmt:formatDate  value="${transaction.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/><br/>
        <label>Totaal credits :</label><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${transaction.creditAlteration}"/><br/>
        <label>Omschrijving :</label>${transaction.description}<br/>
        <label>Status : </label>
        <c:choose>
            <c:when test="${transaction.status == 0}">
                Wachtrij
            </c:when>
            <c:when test="${transaction.status == 1}">
                Verwerkt
            </c:when>
            <c:when test="${transaction.status == 2}">
                Geweigerd
            </c:when>
        </c:choose>
        <br/>
        <c:if test="${not empty transaction.errorMessage}">
            <label>Foutmelding : </label>
            <div>${transaction.errorMessage}</div>
            <br/>
        </c:if>
        <hr>
        <c:choose>
            <c:when  test="${type == 'TransactionLayerUsage'}">
                <table style="width:100%;">
                    <thead>
                        <tr>
                            <th style="width:150px;vertical-align:top;">Layer</th>
                            <th>Tarief</th>
                            <th>Opbouw v/d kosten</th>
                        </tr>
                    </thead>
                    <c:forEach var="lum" items="${LayerUsageMutations}">
                        <tr>
                            <td style="vertical-align:top;">${lum.layer.name}</td>
                            <td style="vertical-align:top;"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${lum.layerCosts}"/></td>
                            <td>${lum.description}</td>
                        </tr>
                    </c:forEach>
                </table>                
            </c:when>
            <c:when  test="${type == 'TransactionPaymentDeposit'}">
                <label>Koers : </label>1:${transaction.txExchangeRate}<br/>
                <label>Valuta &euro; : </label><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${transaction.billingAmount}"/><br/>
            </c:when>            
            
        </c:choose>
    
    </fieldset>
    
    
</c:if>
