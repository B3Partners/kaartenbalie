<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<H1>Welkom bij de kaartenbalie</H1>
<div id="inleiding" style="float: left;">
    Veel organisaties hebben informatie welke perfect via een kaart(laag) aan de 
    gebruiker gecommuniceerd kan worden. Vaak zal de intern beschikbare informatie 
    niet voldoende zijn om een complete kaart op te bouwen. Men wil graag een luchtfoto 
    als ondergrond toevoegen of juist de Grootschalige Basis Kaart Nederland (GBKN). 
    Deze, en nog vele andere nuttige kaartlagen, worden door andere organisaties 
    onderhouden en, soms tegen betaling, beschikbaar gesteld. Het is natuurlijk 
    mogelijk deze externe kaarten allemaal te kopen en op de eigen server te zetten, 
    maar een betere oplossing is Kaartenbalie.
</div>
<div>
    <dl>
        <dt>Beheer</dt>
        <dd>Toevoegen van kaartlagen/servers, metadata en prijzen,
            toevoegen van organisaties, gebruikers en een budget voor het ophalen 
            van betaalde kaarten, rapportages van het gebruik.
        </dd>
        <dt>Persoonlijke pagina</dt>
        <dd>Wachtwoord en ipadres aanpassen en uw personelijke url opvragen
            of een volledige kaartadres genereren.
        </dd>
        <dt>Demo</dt>
        <dd>Nieuwe gebruikers kunnen via de demo kennismaken van de mogelijkheden van
            B3P Kaartenbalie.
        </dd>
        <dt>Viewer</dt>
        <dd>Als u al een inlog heeft kunt u direct naar de viewer: 
            <html:link page="/viewer.do" module="">Link naar Viewer</html:link>
        </dd>
    </dl>
</div>

<div>
    <c:if test="${pageContext.request.remoteUser != null}">
        Ingelogd als: <c:out value="${pageContext.request.remoteUser}"/> | <html:link page="/logout.do" module="">Uitloggen</html:link>
    </c:if>    
</div>
