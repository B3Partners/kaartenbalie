<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div class="steps">
    <div class="stepactive">Start</div>
    <div class="step">Registreren</div>
    <div class="step">Kaarten</div>
    <div class="step">Pers. URL</div>   
    <div class="step">Viewer</div>   
</div>

<div id='democontent'>
    <div id="democontentheader">Welkom bij de demoversie van de Kaartenbalie</div>
    <div id="democontenttext">
         <div id="demoheader3">Wat is kaartenbalie</div>
        Kaartenbalie is een webapplicatie, aangeboden door B3Partners, waarmee u de mogelijkheid heeft 
        om kaartmateriaal dat op verschillende locaties (verschillende webservers) wordt aangeboden, op een
        eenvoudige en snelle manier te combineren <b>&eacute;n te beveiligen tegen ongewenst gebruik</b>. 
        Dit kaartmateriaal dient daartoe wel aangeboden te worden als OGC WMS 1.1.1 service.
        
        <div id="demoheader3">Hoe gaat het in zijn werk?</div>
        Als u een nieuwe gebruiker bent dan kunt u op eenvoudige wijze uzelf registreren. 
        <b>Volg hiertoe de stappen boven in dit scherm.</b>
        <br>
        Standaard krijgt u een kaart te zien zoals die door B3Partners wordt aangeboden. Om de kracht van Kaartenbalie
        aan te tonen kunt u daarnaast ook eigen kaartmateriaal toevoegen
        <b>zonder</b> dat anderen van ditzelfde materiaal gebruik kunnen maken.
        
        <div id="demoheader3">Direct naar de GIS-viewer</div>
        Indien u zich al geregistreerd heeft kunt u meteen naar de viewer gaan.
        <p>
        <button onclick="location.href='<html:rewrite page="/registration.do" module="/demo"/>'">Naar Registratie</button>
    </div>
</div>
