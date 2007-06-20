<%@include file="/templates/taglibs.jsp" %>

<c:set var="form" value="${registrationForm}"/>
<html:javascript formName="registrationForm" staticJavascript="false"/>

<html:form action="/voegurltoe">
    <html:hidden property="id" />
    
    <div id='democontent'>
    <div id="democontentheader">Registratie pagina</div>
    <div id="democontenttext">
    
    <div id="tooltipBox" 
         onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
         onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
         style="z-index:5000;position:absolute;cursor:pointer;">
    </div>
    
    Uw registratie is gelukt. Hieronder vindt u een overzicht van uw gegevens zoals u deze tijdens de
    registratiestap heeft opgegeven. Daarnaast vindt u hier meteen uw persoonlijke URL. Hiermee kunt
    u het kaartmateriaal ook via uw eigen GIS viewer opvragen.
    
    <div id="demoheader3">Uw gegegevens</div>
    <table>
        <tr>
            <td><B><fmt:message key="beheer.userFirstname"/>:</B>&nbsp;<c:out value="${form.map.firstname}"/>&nbsp;<c:out value="${form.map.surname}"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="beheer.userEmail"/>:</B>&nbsp;<c:out value="${form.map.emailAddress}"/></td>
        </tr>        
        <tr>
            <td><B><fmt:message key="beheer.userUsername"/>:</B>&nbsp;<c:out value="${form.map.username}"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="viewer.persoonlijkeurl"/>:</B>&nbsp;<c:out value="${form.map.personalURL}"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="beheer.name"/>:</B>&nbsp;<c:out value="${form.map.organizationName}"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="beheer.organizationTelephone"/>:</B>&nbsp;<c:out value="${form.map.organizationTelephone}"/></td>
        </tr>
    </table>
    
    <div id="demoheader3">De volgende stap</div>
    Kaartenbalie biedt u de mogelijheid om naast het kaartmateriaal dat standaard aangeboden wordt, zelf ook
    kaartmateriaal toe te voegen aan de Kaartenbalie, om zo een beter idee te krijgen van de werking van de 
    Kaartenbalie. Indien u geen kaart toe wilt voegen kunt u ook direct naar de viewer gaan door op de button
    <b>Naar de viewer</b> te klikken. Anders kunt u met de button <b>Voeg kaart toe</b> zelf een WMS server toevoegen
    met eigen kaartmateriaal.<br><br>
    
    <input type="button" onclick="javascript:window.location.href='voegurltoe.do?userid=${form.map.id}'" value="<fmt:message key="button.addmap"/>">
    <input type="button" onclick="javascript:window.location.href='<html:rewrite page='/demo/mapviewer.do?layers=17_basis_nl*16_bebouwdekom_nl' module='' />'" value="<fmt:message key="button.toviewer"/>" >
    <br>
</html:form>
