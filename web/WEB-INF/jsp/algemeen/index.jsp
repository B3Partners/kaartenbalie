<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<H1>Welkom bij de kaartenbalie</H1>
Op deze site vindt u de webviewer die u in staat stelt om door een grote hoeveelheid kaarten te navigeren en hier
de gewenste data uit op te halen.
Naast de webviewer is er ook een beheergedeelte aanwezig waarmee gebruikersaccounts en rechten ingesteld kunnen worden.
<p/>
<c:if test="${pageContext.request.remoteUser != null}">
    Ingelogd als: <c:out value="${pageContext.request.remoteUser}"/> | <html:link page="/logout.do" module="">Uitloggen</html:link>
</c:if>    

