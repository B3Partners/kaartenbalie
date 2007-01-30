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
   
<html:link href="calendar-brown" title="summer" />

<!-- main calendar program -->
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar.js' module='' />"></script>

<!-- language for the calendar -->
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar-en.js' module='' />"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
   adding a calendar a matter of 1 or 2 lines of code. -->
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar-setup.js' module='' />"></script>

<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar-brown.css' module='' />" title="calendar-brown" />

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
            <td><html:text property="timeout" styleId="cal_date" readonly="true"/> &nbsp;
                <img src="<html:rewrite page='/viewer/images/calendar_image.gif' module='' />" id="cal-button"
                     style="cursor: pointer; border: 1px solid red;" 
                     title="Date selector"
                     onmouseover="this.style.background='red';" 
                     onmouseout="this.style.background=''" />
                <script type="text/javascript">
                    Calendar.setup({
                      inputField        : "cal_date",
                      button            : "cal-button",
                      align             : "Tr",
                      singleClick       : true
                    });
                    
                     var myDate = new Date();
                     myDate.setDate(myDate.getDate()+7);
                     Calendar.setDate(myDate);
                </script>
            </td>
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