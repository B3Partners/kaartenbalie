<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<H1>Welkom bij de kaartenbalie</H1>
<table>
    <tr><td valign="top">
            <div id="inleiding" style="float: left;">
                <p>
                    U werkt met een groot aantal kaarten. U heeft zelf kaarten en u wilt
                    deze informatie verrijken met kaarten van externen: luchtfoto's, 
                    wegenkaarten of mogelijk de Grootschalige Basis Kaart Nederland (GBKN).
                    Het is natuurlijk mogelijk deze kaarten allemaal te kopen en 
                    op de eigen server te zetten, maar een betere oplossing is Kaartenbalie.
                </p>
                <p>
                    Kaartenbalie maakt het mogelijk om kaarten afkomstig van meerdere
                    bronnen als een geheel te raadplegen. U kunt metadata toevoegen,
                    prijzen instellen en het gebruik monitoren.
                </p>
                <p>
                    Neemt u contact op met B3Partners voor meer informatie: 
                    <a href="mailto:info@b3partners.nl">info@b3partners.nl</a> of kijk
                    op de website van <a href="www.b3partners.nl">B3Partners</a>.
                </p>
            </div>
            
        </td><td valign="top">
            &nbsp;&nbsp;
        </td><td valign="top">
            <dl>
                <dt><b>Beheer</b></dt>
                <dd>Toevoegen van kaartlagen/servers, metadata en prijzen,
                    toevoegen van organisaties, gebruikers en een budget voor het ophalen 
                    van betaalde kaarten, rapportages van het gebruik.
                </dd>
                <dt><b>Persoonlijke pagina</b></dt>
                <dd>Wachtwoord en ipadres aanpassen en uw personelijke url opvragen
                    of een volledige kaartadres genereren.
                </dd>
                <dt><b>Metadata Editor</b></dt>
                <dd>Metadata editor conform kernset voor geografie 1.1 (Nederlands profiel op ISO19115:2003/ ISO19139)
                </dd>
                <dt><b>Demo</b></dt>
                <dd>Nieuwe gebruikers kunnen via de demo kennismaken van de mogelijkheden van
                    B3P Kaartenbalie.
                </dd>
                <dt><b>Viewer</b></dt>
                <dd>Als u al een inlog heeft kunt u direct naar de <a href="/gisviewer/viewer.do">viewer</a>.
                </dd>
            </dl>
            
    </td></tr>
</table>

<div>
    <c:if test="${pageContext.request.remoteUser != null}">
        Ingelogd als: <c:out value="${pageContext.request.remoteUser}"/> | <html:link page="/logout.do" module="">Uitloggen</html:link>
    </c:if>    
</div>
