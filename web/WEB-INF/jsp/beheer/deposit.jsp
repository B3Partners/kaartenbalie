<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${depositForm.map}"/>

<html:javascript formName="depositForm" staticJavascript="false"/>
<html:form action="/deposit" onsubmit="return validateDepositForm(this)" focus="amount">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="orgId"/>
    <h1>Credits kopen voor ${form.orgName}</h1>
    
    <table>
        <tr>
            <td colspan="2" style="height: 30px;" valign="top">
                <strong>Huidige koers: 1 euro voor ${exchangeRate} credits</strong>
            </td>
        </tr>
        <tr>
            <td>
                Gewenste bedrag:
            </td>
           <td>
                <html:text property="amount" size="4" maxlength="4"/> , <html:text property="fraction" size="2" maxlength="2"/> (euro)
            </td>
        </tr>
        <tr>
            <td>
                Eigen kenmerk:
            </td>
           <td>
                <html:text property="description" maxlength="12"/> (max 12 karakters)
            </td>
        </tr>
        <tr>
            <td>
                Betaalmethode: 
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

