<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html:javascript formName="registrationForm" staticJavascript="false"/>

ID is : <c:out value="${id}"/>
<c:choose>
    <c:when test="${not empty id}">
        
        <h1>Registratie pagina</h1>
        Voordat u gebruik kunt maken van de Kaartenbalie Demo (maar ook bij de officiele versie van Kaartenbalie)
        geldt dat u zich eerst moet registreren of laten registreren. De reden dat u dit dient te doen is omdat u
        op die manier een inlognaam en wachtwoord aangeleverd krijgt waarmee alleen u toegang heeft tot het materiaal
        dat u op kaartenbalie aangeboden heeft.
        Op deze manier blijft het materiaal van u beschermd voor ongenode gasten. Tevens geldt dat u ook geen toegang
        heeft tot kaartmateriaal van anderen. Met andere woorden: kaartenbalie garandeerd de veiligheid van de gegevens
        door deze gegevens persoonlijk te maken en persoonlijk te houden.
        Indien er kaarten zijn die wel voor een groter publiek toegankelijk moeten zijn, dan kan dit met behulp van 
        rechten opgelost worden.
        
        <h2>Uw gegegevens</h2>
        
        <table>
            <tr>
                <td><B>Naam:</B>&nbsp;<c:out value="${firstname}"/>&nbsp;<c:out value="${lastname}"/></td>
            </tr>
            <tr>
                <td><B>Email:</B>&nbsp;<c:out value="${emailAddress}"/></td>
            </tr>
            <tr>
                <td><B>Opgegeven gebruikersnaam:</B>&nbsp;<c:out value="${username}"/></td>
            </tr>
            <tr>
                <td><B>Persoonlijke URL:</B>&nbsp;<c:out value="${personalURL}"/></td>
            </tr>
            
            
            <tr>
                <td><B>Naam organisatie:</B>&nbsp;<c:out value="${name}"/></td>
            </tr>
            <tr>
                <td><B>Straat:</B>&nbsp;<c:out value="${organizationStreet}"/>&nbsp;
                <c:out value="${organizationNumber}"/>&nbsp;<c:out value="${organizationAddition}"/></td>
            </tr>
            
            <tr>
                <td><B>Plaats:</B>&nbsp;<c:out value="${organizationProvince}"/></td>
            </tr>
            
            
            <tr>
                <td><B>Telefoon nr.:</B>&nbsp;<c:out value="${organizationTelephone}"/></td>
            </tr>    
            
            
        </table>
        Klik nu hier om verder te gaan.
        
    </c:when>
    <c:otherwise>
        <html:form action="/registration" onsubmit="return validateRegistrationForm(this)">
            <h1>Registratie pagina</h1>
            Voordat u gebruik kunt maken van de Kaartenbalie Demo (maar ook bij de officiele versie van Kaartenbalie)
            geldt dat u zich eerst moet registreren of laten registreren. De reden dat u dit dient te doen is omdat u
            op die manier een inlognaam en wachtwoord aangeleverd krijgt waarmee alleen u toegang heeft tot het materiaal
            dat u op kaartenbalie aangeboden heeft.
            Op deze manier blijft het materiaal van u beschermd voor ongenode gasten. Tevens geldt dat u ook geen toegang
            heeft tot kaartmateriaal van anderen. Met andere woorden: kaartenbalie garandeerd de veiligheid van de gegevens
            door deze gegevens persoonlijk te maken en persoonlijk te houden.
            Indien er kaarten zijn die wel voor een groter publiek toegankelijk moeten zijn, dan kan dit met behulp van 
            rechten opgelost worden.
            
            <h2>Uw gegegevens</h2>
            
            <table>
                <tr>
                    <td><B>Voornaam:</B></td>
                    <td><html:text property="firstname"/></td>
                    <td><B>Achternaam:</B></td>
                    <td><html:text property="lastname"/></td>
                </tr>
                <tr>
                    <td><B>Email:</B></td>
                    <td><html:text property="emailAddress"/></td>
                </tr>
                <tr>
                    <td><B>Gewenste gebruikersnaam:</B></td>
                    <td><html:text property="username"/></td>
                </tr>
                <tr>
                    <td><B>Gewenst wachtwoord:</B></td>
                    <td><html:text property="password"/></td>
                </tr>
                
                
                <tr>
                    <td><B>Naam organisatie:</B></td>
                    <td><html:text property="name"/></td>
                </tr>
                <tr>
                    <td><B>Straat:</B></td>
                    <td><html:text property="organizationStreet"/></td>
                    <td><B>Nummer:</B></td>
                    <td><html:text property="organizationNumber"/></td>
                    <td><B>Toevoeging:</B></td>
                    <td><html:text property="organizationAddition"/></td>
                </tr>
                
                <tr>
                    <td><B>Plaats:</B></td>
                    <td><html:text property="organizationProvince"/></td>
                    <td><B>Land:</B></td>
                    <td><html:text property="organizationCountry"/></td>
                </tr>
                
                <tr>
                    <td><B>Postbus:</B></td>
                    <td><html:text property="organizationPostbox"/></td>
                </tr>
                
                <tr>
                    <td><B>Telefoon nr.:</B></td>
                    <td><html:text property="organizationTelephone"/></td>
                </tr>
                <tr>
                    <td><B>Fax nr.:</B></td>
                    <td><html:text property="organizationFax"/></td>
                </tr>     
                
                <tr>
                    <td colspan="0"><center>
                            <html:reset  value="Delete values" />
                            <html:submit value="Add/Change User" property="save"/>
                    </center></td>
                </tr>
            </table>
        </html:form>
    </c:otherwise>
</c:choose>




