<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<html:form action="/deposit.do" focus="" onsubmit="return validateReportingForm(this)">
    <fieldset>
        <html:hidden property="action"/>
        <html:hidden property="alt_action"/>
        <legend>Uw credits verhogen</legend>
        <label>Huidige koers :</label>1:${exchangeRate} (1 euro voor ${exchangeRate} credits)<br/>
        <hr>
        <label>Gewenste bedrag:</label><html:text property="amount" size="4" maxlength="4"/>,<html:text property="fraction" size="2" maxlength="2"/> (euro)<br/> 
        <label>Eigen kenmerk:</label><html:text property="description" maxlength="12"/> (max 12 karakters)<br/> 
        <label>Betaalmethode :</label><html:radio property="paymentMethod" value="iDeal"/> iDeal<br/>
        <label>&nbsp;</label><html:radio property="paymentMethod" value="creditCard" disabled="true"/> CreditCard<br/>
        <html:submit property="create" accesskey="s" styleClass="knop">
            <fmt:message key="button.save"/>
        </html:submit>
        
    </fieldset>
</html:form>

