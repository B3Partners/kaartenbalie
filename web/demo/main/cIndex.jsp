<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<% if (getServletConfig().getInitParameter("demoActive").equals("true")) { %>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Kaartenbalie Demo</title>
        </head>
        <body>
            
            <h1>Welkom op de Kaartenbalie Demo pagina</h1>
            Welkom op de demo pagina van de kaartenbalie. Deze pagina is bedoeld om u als gebruiker vertrouwd 
            te maken met de kaartenbalie en in een aantal eenvoudige stappen te laten zien wat kaartenbalie is
            en wat ze kan.
            
            <P>
                <h2>Wat is kaartenbalie</h2>
                Kaartenbalie is een webapplicatie, aangeboden door B3Partners, waarmee u de mogelijkheid heeft 
                om kaartmateriaal dat op verschillende locaties (verschillende webservers) wordt aangeboden, op een
                eenvoudige en snelle manier te combineren. Dit kaartmateriaal dient aan maar een paar eisen te voldoen
                en kaartenbalie is eenvoudig in gebruik om dit materiaal toe te voegen.
            </P>
            
            <P>
                <h2>Hoe gebruikt u kaartenbalie</h2>
                Deze demopagina zal een paar korte registratiestappen uitvoeren zodat u vervolgens de mogelijkheid
                heeft de werking van kaartenbalie te ondervinden. Deze registratie stappen kunt u in de demoversie
                zelf uitvoeren waar het in de officiele versie door een beheerder uitgevoerd wordt.<BR>
                Om u echter een volledig beeld van kaartenbalie te kunnen geven wordt in deze demo een deel van het
                beheer afgestaan zodat u de mogelijkheid heeft om zelf informatie aan kaartenbalie toe te voegen.
                U krijgt in deze demoversie dan ook de mogelijkheid om &eacute;&eacute;n eigen kaartserver toe te
                voegen aan kaartenbalie zodat u ziet hoe uw eigen materiaal gecombineerd wordt met het materiaal
                dat standaard door B3Partners op kaartenbalie aangeboden wordt.
                
                <P>Hieronder staat nog kort hoe u zicht dient te registreren als u gebruik wilt maken van de kaartenbalie demo.</P>
                <UL>
                    <LI>Stap 1: U dient zich te registreren als gebruiker</LI>
                    <LI>Stap 2: U kunt, indien gewenst &eacute;&eacute;n eigen locatie toevoegen aan kaartenbalie, om een betere indruk te krijgen van het functioneren</LI>
                    <LI>Stap 3: U krijgt een samenvatting van uw gegevens</LI>
                    <LI>Stap 4: U kunt werken met de demoviewer</LI>
                </UL>
            </P>
            <P>
                Als u zich al heeft geregistreerd als demogebruiker dan kunt u in het menu rechtstreeks naar de mapviewer
                gaan om de demonstratie van de kaartenbalie te vervolgen.
            </P>
        </body>
    </html>
<% } else { %>
    <h1>Pagina niet aanwezig</h1>
    De pagina die u heeft opgevraagd is niet (meer) toegankelijk in het systeem. Neemt u contact op met de beheerder
    indien u vragen heeft over deze pagina.
<% } %>
