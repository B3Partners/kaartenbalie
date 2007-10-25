<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<H1>Welkom op de Persoonlijke Pagina</H1>
<p>Hier kunt u een persoonlijke URL samenstellen zodat u via een externe viewer 
    het kaartmateriaal van kaartenbalie kunt bekijken. Deze samengestelde url heeft 
    een beveiligingssysteem dat als vervanging dient voor het inloggen. Hiermee
    kunt u dus iedere viewer gebruiken die de open WMS standaard ondersteunt.
    Natuurlijk kunt u ook onze viewer gebruiken.
</p>
<p>
    Onder Profiel kunt u uw wachtwoord en emailadres aanpassen. Tenslotte kunt u
    een GetMap URL gegenereren voor viewers die het GetCapabilities commando niet
    ondersteunen.
</p>
<p>
    Indien u zich al geregistreerd heeft kunt u met behulp van de onderstaande 
    button meteen naar de viewer.
</p>
<html:button  property="viewer" onclick="javascript:window.location.href='/gisviewer/viewer.do'">
    <fmt:message key="button.toviewer"/>
</html:button>
