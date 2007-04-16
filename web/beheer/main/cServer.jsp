<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="form" value="${serverForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<html:javascript formName="serverForm" staticJavascript="false"/>
<html:form action="/server" onsubmit="return validateServerForm(this)" focus="givenName">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
    <html:messages id="message" message="true" >
        <div id="error">
            <c:out value="${message}" escapeXml="false"/>
        </div>
    </html:messages>
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer Servers</H1>
        
        <b>Server tabel:</b>
        <div class="serverRijTitel">
            <div style="width: 100px;">Naam</div>
            <div style="width: 400px;">URL</div>
            <div style="width: 150px;">Datum laatste update</div>
        </div>
        
        <div class="tableContainer" id="tableContainer"> 
            <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                <div class="serverRij">
                    <div style="width: 100px;" class="vakSpeciaal" title="<c:out value="${nServiceProvider.givenName}"/>">
                        <html:link page="/server.do?edit=submit&id=${nServiceProvider.id}">
                            <c:out value="${nServiceProvider.givenName}"/>
                        </html:link>
                    </div>
                    <div style="width: 400px;" title="<c:out value="${nServiceProvider.url}"/>">
                        <c:out value="${nServiceProvider.url}"/>
                    </div>
                    <div style="width: 150px;" class="vakSpeciaal" title="<c:out value="${nServiceProvider.updatedDate}"/>">
                        <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
                    </div>                    
                </div>
            </c:forEach>
        </div>
    </div>
    
    <c:if test="${action != 'list'}">
        <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px;">
            <table>
                <c:choose>
                    <c:when test="${not empty mainid}">
                        <tr>
                            <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                            <td><html:text property="givenName" readonly="true" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                            <td><html:text property="url" size="75" readonly="true" /></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                            <td><html:text property="givenName" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                            <td><html:text property="url" size="75" /></td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${action != 'list'}">
            <div class="knoppen">
                <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                    <fmt:message key="button.cancel"/>
                </html:cancel>
                <c:if test="${empty mainid}">
                    <html:submit property="save" accesskey="s" styleClass="knop">
                        <fmt:message key="button.save"/>
                    </html:submit>
                </c:if>
                <c:if test="${not empty mainid}">
                    <html:submit property="save" accesskey="s" styleClass="knop">
                        <fmt:message key="button.update"/>
                    </html:submit>
                    <html:submit property="delete" accesskey="d" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.remove"/>
                    </html:submit>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="knoppen">
                <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                    <fmt:message key="button.new"/>
                </html:submit>
            </div>
        </c:otherwise>
    </c:choose>
</html:form>