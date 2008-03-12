<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<html:form action="/deposit.do" onsubmit="return validateReportingForm(this)">
    <fieldset class="reportingFieldset">
        <html:hidden property="action"/>
        <html:hidden property="alt_action"/>
        <legend>Uw credits verhogen</legend>
        
        <table>
            <tr>
                <td style="height: 30px;" valign="top">
                    <strong><label>Huidige koers :</label>1:${exchangeRate} (1 euro voor ${exchangeRate} credits)</strong>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Gewenste bedrag:</label><html:text property="amount" size="4" maxlength="4"/> , <html:text property="fraction" size="2" maxlength="2"/> (euro)
                </td>
            </tr>
            <tr>
                <td>
                    <label>Eigen kenmerk:</label><html:text property="description" maxlength="12"/> (max 12 karakters)
                </td>
            </tr>
            <tr>
                <td>
                    <label>Betaalmethode :</label><html:radio property="paymentMethod" value="iDeal"/> iDeal
                </td>
            </tr>
            <tr>
                <td>
                    <label>&nbsp;</label><html:radio property="paymentMethod" value="creditCard" disabled="true"/> CreditCard
                </td>
            </tr>
        </table>
        <html:submit property="create" accesskey="s" styleClass="knop">
            <fmt:message key="button.save"/>
        </html:submit>
        <button onclick="location.href='accounting.do'; return false;">Annuleren</button>
    </fieldset>
</html:form>

