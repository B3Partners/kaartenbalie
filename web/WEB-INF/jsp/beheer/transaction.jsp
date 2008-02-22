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
                            <th>Type</th>
                            <th>Aantal</th>
                            <th>Projectie</th>
                            <th>Schaal</th>
                            <th>Service</th>
                            <th>Dienst</th>
                            <th>Prijs</th>                            
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
                                        Gratis
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${lpc.layerPrice}"/>        
                                    </c:otherwise>
                                </c:choose>
                            </td>                                
                                                    
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
