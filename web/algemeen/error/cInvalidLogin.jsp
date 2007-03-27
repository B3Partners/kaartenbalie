<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
Wachtwoord en/of gebruikersnaam zijn onjuist.<p>
Probeer het nogmaals:
<form action="j_security_check" method='post'>
    <div class="item"><fmt:message key="algemeen.username"/>:</div>
    <div class="value"><input type="text" name="j_username"></div>
    <div class="item"><fmt:message key="algemeen.password"/>:</div>
    <div class="value"><input type="password" name="j_password"></div>
    <html:submit property="login" styleClass="knop">
        <fmt:message key="button.login"/>
    </html:submit>
</form> 
