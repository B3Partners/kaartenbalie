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
<c:set var="form" value="${createPersonalURLForm.map}"/>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar-brown.css' module='' />" title="calendar-brown" />
    <H1>Persoonlijke URL</H1>
    <P>
    U kunt door middel van deze pagina een persoonlijke URL aanmaken.
    Deze URL stelt u in staat om de verschillende kaarten ook op te vragen met behulp van een externe viewer.
    </P>
        <c:if test="${not empty message}">
            <div id="error">
            <h3><c:out value="${message}"/></h3>
            </div>
        </c:if>
        
        <c:if test="${not empty warning}">
        <div id="warning">
            <h3><c:out value="${warning}"/></h3>
        </div>
    </c:if>
    
<table>
    <html:javascript formName="createPersonalURLForm" staticJavascript="false"/>
    <html:form action="/createPersonalURL" onsubmit="return validateCreatePersonalURLForm(this)">
        <tr>
        <td>&nbsp;</td>
        </tr>
        <tr>
        <td><fmt:message key="viewer.persoonlijkeurl.username"/>:</td>
        <td><html:text property="username" readonly="true" styleClass="readOnly" /></td>
        </tr>
        <c:if test="${empty changePassword}">
            <tr>
                <td><fmt:message key="viewer.persoonlijkeurl.password"/>:</td>
                <td>
                    <html:password property="password" readonly="true" styleClass="readOnly"/>
                    <html:submit property="create" styleClass="knop">
                        <fmt:message key="button.changepwd"/>
                    </html:submit>
                </td>
            </tr>
        </c:if>
        <c:if test="${!empty changePassword}">
            <tr>
                <td><fmt:message key="viewer.persoonlijkeurl.oudpw"/>:</td>
                <td><html:password property="password"/></td>
            </tr>
            <tr>
                <td><fmt:message key="viewer.persoonlijkeurl.nieuwpw"/>:</td>
                <td><html:password property="newpassword" /></td>
            </tr>
            <tr>
                <td><fmt:message key="viewer.persoonlijkeurl.retypepw"/>:</td>
                <td><html:password property="newpasswordretyped" /></td>
            </tr>
        </c:if>
        <tr>
            <td><fmt:message key="viewer.persoonlijkeurl.timeout"/>:</td>
            <td>
                
                <html:text property="timeout" styleId="cal_date"/> &nbsp;
                
                <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
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
                     //Calendar.setDate(myDate);
                </script>
                
            </td>
        </tr>
        <tr>
        <td><fmt:message key="viewer.persoonlijkeurl.registeredip"/>:</td> 
        <td><html:text property="registeredIP"/></td>
        </tr>
        <tr>
        <td><fmt:message key="viewer.persoonlijkeurl.createdurl"/>:</td>
        <td><html:text property="personalURL" styleId="personalURL" styleClass="readOnly" readonly="true" size="100" /></td>
        </tr>
        <tr>
        <td>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <html:submit property="save" styleClass="knop">
                    <fmt:message key="button.create"/>
                </html:submit>
            </td>
        </tr>
        <c:if test="${not empty form.personalURL}">
            <tr>
                <td>Default getMap:</td>
                <td><html:textarea property="defaultGetMap" styleClass="readOnly" styleId="defaultGetMap" readonly="true" cols="75" rows="5" /></td>
            </tr>
        <tr>                
                <td>
                    <input id="openWMSHelpButton" class="knop" type="button" onclick="javascript: openWMSHelp()" value="<fmt:message key='button.createDefaultgetMap'/>"/>
                </td>
            </tr>
        </c:if>
</html:form>
</table>
    <script>
        function openWMSHelp(){
            var elementGetMap= document.getElementById("defaultGetMap");
            var elementPUrl= document.getElementById("personalURL");
            var value="wmsUrlCreator.do"
            if (elementGetMap && elementGetMap.value.length > 0){
                value+="?getMap="+encodeURIComponent(elementGetMap.value);
            }
            if (elementPUrl && elementPUrl.value.length > 0){
                if (value.indexOf('?')<0){
                    value+="?";
                }else{
                    value+="&";
                }
                value+="pUrl="+encodeURIComponent(elementPUrl.value);
            }
            window.open(value,"GetMapBuilder","width=700,height=600,resizable=yes,scrollbars=yes");
        }
        function putWmsGetMap(value){
            document.getElementById("defaultGetMap").value=value;
        }

        
    </script>
</body>