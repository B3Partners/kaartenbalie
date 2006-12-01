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
<body>
    <H1>Persoonlijke URL cre&euml;ren</H1>
<table>
    <html:form action="/createPersonalURL">
        <tr>
        <td>Gebruikersnaam:</td>
        <td><html:text property="username"  /></td>
        </tr>
        <tr>
        <td>Wachtwoord:</td>
        <td><html:password property="password" /></td>
        </tr>
        <tr>
        <td>Gewenste timeout:</td>
        <td><html:text property="timeout"/></td>
        </tr>
        <tr>
        <td>Geregisteerd IP adres:</td> 
        <td><html:text property="registeredIP" readonly="true" /></td>
        </tr>
        <tr>
        <td>Persoonlijk gecre&euml;erde URL:</td>
        <td><html:text property="personalURL" readonly="true" size="100" /></td>
        </tr>
        <tr>
        <td><html:submit value="Create personal URL" property="save"/></td>
        </tr>
</html:form>
</table>
</body>