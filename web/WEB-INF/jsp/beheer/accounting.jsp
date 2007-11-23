<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div class="containerdiv" style="float: left; clear: none;">
    <H1>Beheer Accounting</H1>
    
    <fieldset>
            <legend>Account Details</legend>
            <label>Credit Balance :</label> 100.013 Cr<br/>
             <html:submit property="deposit" accesskey="d" styleClass="knop" onclick="bCancel=true">
                    Credit verhogen
             </html:submit>
    </fieldset>
    
    
    
    <fieldset>
        <legend>Bijschrijvingen</legend>
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
        <legend>Afschrijvingen Gebruik</legend>
    </fieldset>

        <fieldset>
        <legend>Afschrijvingen Periodiek</legend>
    </fieldset>

    
</div>