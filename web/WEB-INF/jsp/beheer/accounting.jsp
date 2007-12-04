<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div class="containerdiv" style=";">
    <H1>Beheer Accounting</H1>
    
    <fieldset>
        <legend>Account Details</legend>
        <label>Credit Balance :</label>
        <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${balance}" /> credits
        <p>
            <html:submit property="deposit" accesskey="d" styleClass="knop" onclick="bCancel=true">
                Credit verhogen
            </html:submit>
        </p>
    </fieldset>
    
    
    
    <fieldset>
        <legend>Laatste 20 Bijschrijvingen</legend>

        
        
        <div class="transactieRijTitel">
            <div style="width: 120px;">Datum</div>
            <div style="width: 75px;">Methode</div>
            <div style="width: 200px;">Uw kenmerk</div>
            <div style="width: 75px;">Valuta &euro;</div>
            <div style="width: 75px;">Koers</div>
            <div style="width: 100px;">Resultaat</div>
        </div>
        <c:set var="hoogte" value="100" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>
        
        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px">          
            <div class="transactieRij">
                <div style="width: 120px;" class="vakSpeciaal">
                    25-08-1982 13:10
                </div>
                <div style="width: 75px;">
                    iDeal
                </div>                
                <div style="width: 200px;">
                    1234 5678 9123 4567
                </div>
                <div style="width: 75px;">
                    275 &euro;
                </div>                
                <div style="width: 75px;">
                    1:10
                </div>     
                <div style="width: 100px;">
                    2750 c
                </div>     
            </div>
        </div>        
    </fieldset>
    
    <fieldset>
        <legend>Laatste 20 Afschrijvingen -  Gebruik</legend>
        <div class="transactieRijTitel">
            <div style="width: 150px;">Aangemaakt</div>
            <div style="width: 150px;">Verwerkt</div>
            <div style="width: 90px;padding-right:10px;">Credits</div>
            <div style="width: 70px;">Status</div>
            <div style="width: 230px;">Toelichting</div>            
        </div>
        <c:set var="hoogte" value="100" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>
        
        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px">          
            <c:forEach var="tlu" items="${layerUsages}">
                <div class="transactieRij">
                    <div style="width: 150px;" class="vakSpeciaal">
                        <fmt:formatDate  value="${tlu.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                    </div>
                    <div style="width: 150px;" class="vakSpeciaal">
                        <fmt:formatDate  value="${tlu.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                    </div>
                    <div style="width: 90px;text-align:right;padding-right:10px;" class="vakSpeciaal">
                      ${tlu.creditAlteration}
                    </div>
                    <div style="width: 70px;" class="vakSpeciaal">
                        <c:choose>
                            <c:when test="${tlu.status == 0}">
                                Wachtrij
                            </c:when>
                            <c:when test="${tlu.status == 1}">
                                Verwerkt
                            </c:when>
                            <c:when test="${tlu.status == 2}">
                                Geweigerd
                            </c:when>
                        </c:choose>
                       
                    </div>
                    <div style="width: 230px;" class="vakSpeciaal">
                       ${tlu.errorMessage}
                    </div>
                </div>        
            </c:forEach>
        </div>
    </fieldset>
    
    <fieldset>
        <legend>Afschrijvingen Periodiek</legend>
    </fieldset>
    
    
</div>