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
        Welkom op deze pagina. Deze pagina is bedoeld om u als gebruiker vertrouwd te maken met de kaartenbalie.
        Door middel van een paar eenvoudige stappen zult u zien hoe kaartenbalie eenvoudig te gebruiken is.

        <P>
        <h2>Wat is kaartenbalie</h2>
        Kaartenbalie is een online webservice, aangeboden door B3Partners, waarmee u de mogelijkheid heeft 
        om kaartmateriaal dat op verschillende locaties (verschillende webservers) wordt aangeboden op een
        eenvoudige en snelle manier te combineren.
        </P>

        <P>
        <h2>Hoe gebruikt u kaartenbalie</h2>
        Deze demo pagina zal u stap voor stap door een proces heen loodsen zodat u de verschillende aspecten
        van kaartenbalie zult zien. Een groot deel van de stappen wijzen zichzelf, maar zullen daar waar nodig
        toegelicht worden.

        <P>Kaartenbalie bestaat daarom uit een viertal stappen zodat u snel ziet hoe kaartenbalie functioneert:</P>
        <UL>
            <LI>Stap 1: U dient zich te registreren als gebruiker</LI>
            <LI>Stap 2: U kunt, indien gewenst &egrave;&egrave;n eigen locatie toevoegen aan kaartenbalie, om een betere indruk te krijgen van het functioneren</LI>
            <LI>Stap 3: U krijgt een samenvatting van uw gegevens</LI>
            <LI>Stap 4: U kunt werken met de demoviewer</LI>
        </UL>
        </P>
        <html:form action="/registration">
            <html:submit value="Volgende"/>
        </html:form>


        </body>
    </html>
<% } else { %>
    <h1>Pagina niet aanwezig</h1>
    De pagina die u heeft opgevraagd is niet (meer) toegankelijk in het systeem. Neemt u contact op met de beheerder
    indien u vragen heeft over deze pagina.
<% } %>
