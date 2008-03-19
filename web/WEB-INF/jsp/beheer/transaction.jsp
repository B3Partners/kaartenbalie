<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<h1>Transactie</h1>

<c:if test="${not empty transaction}">
    
    <fieldset class="reportingFieldset">
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
        
        <p>
            <c:choose>
                <c:when  test="${type == 'TransactionLayerUsage'}">
                    <table id="transactionTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                        <thead>
                            <tr class="serverRijTitel">
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
                    <script type="text/javascript">
                        Table.stripe(document.getElementById('transactionTable'), 'table_alternate_tr');
                    </script>
                </c:when>
                <c:when  test="${type == 'TransactionPaymentDeposit'}">
                    <label>Koers : </label>1:${transaction.txExchangeRate}<br/>
                    <label>Valuta &euro; : </label><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${transaction.billingAmount}"/><br/>
                </c:when>            
            </c:choose>
        </p>
    </fieldset>
    
</c:if>
<p>
    <button onclick="location.href = '<html:rewrite page="/accounting.do"/>'">Terug</button>
</p>