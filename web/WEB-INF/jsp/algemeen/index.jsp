<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<H1>Welkom bij de kaartenbalie</H1>
<div id="inleiding">
    <h2>Inleiding Kaartenbalie</h2>
    Veel organisaties hebben informatie welke perfect via een kaart(laag) aan de 
    gebruiker gecommuniceerd kan worden. Vaak zal de intern beschikbare informatie 
    niet voldoende zijn om een complete kaart op te bouwen. Men wil graag een luchtfoto 
    als ondergrond toevoegen of juist de Grootschalige Basis Kaart Nederland (GBKN). 
    Deze, en nog vele andere nuttige kaartlagen, worden door andere organisaties 
    onderhouden en, soms tegen betaling, beschikbaar gesteld. Het is natuurlijk 
    mogelijk deze externe kaarten allemaal te kopen en op de eigen server te zetten, 
    maar een betere oplossing is Kaartenbalie.
</div>
<!--
<p>
    De jarenlange ervaring met internet GIS-toepassingen heeft B3Partners vertaald 
    in een nieuw concept: Kaartenbalie.
</p>
<p>
    Veel organisaties hebben informatie welke perfect via een kaart(laag) aan de 
    gebruiker gecommuniceerd kan worden. Vaak zal de intern beschikbare informatie 
    niet voldoende zijn om een complete kaart op te bouwen. Men wil graag een luchtfoto 
    als ondergrond toevoegen of juist de Grootschalige Basis Kaart Nederland (GBKN). 
    Deze, en nog vele andere nuttige kaartlagen, worden door andere organisaties 
    onderhouden en, soms tegen betaling, beschikbaar gesteld. Het is natuurlijk 
    mogelijk deze externe kaarten allemaal te kopen en op de eigen server te zetten, 
    maar een betere oplossing is Kaartenbalie.
</p>
<p>
    Kaartenbalie zorgt ervoor dat kaarten/kaartlagen, welke via verschillende 
    bronnen (intern en extern) beschikbaar zijn, via &eacute;&eacute;n&nbsp;electronische 
    balie opgevraagd kunnen worden. Kaartenbalie gebruikt de OpenGIS WMS systematiek, zodat 
    alle gangbare GIS viewers van bijvoorbeeld ESRI of Bentley de kaarten kunnen opvragen en 
    opnemen in hun projecten.
</p>
<p>
    Kaartenbalie kan intern worden ingezet om externe kaarten beschikbaar te stellen, 
    maar Kaartenbalie kan ook via internet kaartmateriaal aan burgers of klanten ter 
    beschikking stellen. Via een rechtensysteem kan de beheerder vastleggen welke 
    kaarten beschikbaar zijn voor anoniem gebruik en welke kaarten alleen door een 
    speciale groep van internetgebruikers gezien mogen worden; men denke hierbij aan 
    brandweer/politie bij gemeentelijke kaarten of klanten bij commerciele organisaties.
</p>
<p>
    B3Partners heeft een <html:link page="/demo.do" module="">Demo</html:link> opgezet 
    waarmee u zelf kunt ervaren hoe makkelijk Kaartenbalie u in staat stelt uw 
    eigen GIS beveiligd via het web te ontsluiten, eventueel gecombineerd met 
    informatie van anderen (in dit geval B3Partners). De demo maakt gebruik de 
    Flamingo viewer. Hiermee kunt u de kaarten bekijken, in- en uitzoemen, 
    verschuiven en objectinformatie op vragen.
</p>
<p>
    Als u al een gebruikersaccount heeft kunt u hier via de 
    <html:link page="/viewer.do" module="">Persoonlijke Pagina</html:link> uw gegevens
    aanpassen en uw persoonlijke URL aanmaken voor gebruik in externe viewers.
</p>
<p>
    Tenslotte kunnen beheerders via <html:link page="/beheer.do" module="">Beheer</html:link>
    gebruikersaccounts en rechten instellen. Ook kunnen hier nieuwe kaarten aan
    Kaartenbalie worden toegevoegd en de rechten per organisatie hierop ingesteld worden.
    Tenslotte wordt het gebruik van het kaartmateriaal per organisatie gerapporteerd.
<p/>
<p>
    Indien u zich al geregistreerd heeft kunt u met behulp van de onderstaande button meteen naar de viewer.
</p>
<html:button  property="viewer" onclick="javascript:window.location.href='/gisviewer/viewer.do'">
    <fmt:message key="button.toviewer"/>
</html:button>
-->
<p>
    <c:if test="${pageContext.request.remoteUser != null}">
        Ingelogd als: <c:out value="${pageContext.request.remoteUser}"/> | <html:link page="/logout.do" module="">Uitloggen</html:link>
    </c:if>    
</p>
