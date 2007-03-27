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
<SCRIPT type=text/javascript>
        <!--
        
        function toggleDiv(divid) {
            var divEl = document.getElementById(divid);
            if(divEl.style.display == 'none') divEl.style.display = 'block';
            else divEl.style.display = 'none';
        }
        
/* http://www.alistapart.com/articles/zebratables/ */
function removeClassName (elem, className) {
	elem.className = elem.className.replace(className, "").trim();
}

function addCSSClass (elem, className) {
	removeClassName (elem, className);
	elem.className = (elem.className + " " + className).trim();
}

String.prototype.trim = function() {
	return this.replace( /^\s+|\s+$/, "" );
}

function stripedTable() {
	if (document.getElementById && document.getElementsByTagName) {  
		var allTables = document.getElementsByTagName('table');
		if (!allTables) { return; }

		for (var i = 0; i < allTables.length; i++) {
			if (allTables[i].className.match(/[\w\s ]*scrollTable[\w\s ]*/)) {
				var trs = allTables[i].getElementsByTagName("tr");
				for (var j = 0; j < trs.length; j++) {
					removeClassName(trs[j], 'alternateRow');
					addCSSClass(trs[j], 'normalRow');
				}
				for (var k = 0; k < trs.length; k += 2) {
					removeClassName(trs[k], 'normalRow');
					addCSSClass(trs[k], 'alternateRow');
				}
			}
		}
	}
}

window.onload = function() { stripedTable(); }
        -->
        </SCRIPT>
<html:javascript formName="serverForm" staticJavascript="false"/>
<html:form action="/server" onsubmit="return validateServerForm(this)">
    <c:if test="${not empty message}">
        <div id="error">
            <h3><c:out value="${message}"/></h3>
        </div>
    </c:if>
    
    <div class="containerdiv" style="float: left; clear: none;">
        <b>Server tabel:</b>
        <div class="serverRijTitel">
            <div style="width: 50px;">Select</div>
            <div style="width: 100px;">Naam</div>
            <div style="width: 315px;">URL</div>
            <div style="width: 150px;">Datum aanmaak</div>
            <div style="width: 100px;">Is geupdate</div>
        </div>
        <div class="tableContainer" id="tableContainer">         
            <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                <div class="serverRij">
                    <div style="width: 50px;">
                        <html:multibox value="${nServiceProvider.id}" property="serviceProviderSelected" />
                    </div>
                    <div style="width: 100px;" class="vakSpeciaal" title="<c:out value="${nServiceProvider.givenName}"/>">
                        <a href="server.do?id=${nServiceProvider.id}"><c:out value="${nServiceProvider.givenName}"/></a>
                    </div>
                    <div style="width: 315px;" title="<c:out value="${nServiceProvider.url}"/>">
                        <c:out value="${nServiceProvider.url}"/>
                    </div>
                    <div style="width: 150px;" class="vakSpeciaal" title="<c:out value="${nServiceProvider.updatedDate}"/>">
                        <c:out value="${nServiceProvider.updatedDate}"/>
                    </div>
                    <div style="width: 100px;" title="<c:out value="${nServiceProvider.reviewed}"/>">
                        <c:out value="${nServiceProvider.reviewed}"/>
                    </div>                    
                </div>
            </c:forEach>
        </div>
    </div>
    <c:if test="${empty showFields && empty selectedId}">
        <html:submit property="delete" style="margin-top: 5px; margin-bottom: 5px;">
            <fmt:message key="button.delete"/>
        </html:submit>
        <html:submit property="create" style="margin-top: 5px; margin-bottom: 5px;">
            <fmt:message key="button.new"/>
        </html:submit>
    </c:if>
    
    <c:if test="${not empty showFields || not empty selectedId}">
        <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px;">
            <table>
                <tr>
                    <td><B><fmt:message key="beheer.serverID"/>:</B></td>
                    <td><html:text property="serverid" readonly="true" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                    <td><html:text property="serviceProviderGivenName" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                    <td><html:text property="serviceProviderUrl" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.serverUpdate"/>:</B></td>
                    <td><html:text property="serviceProviderUpdatedDate" readonly="true" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.serverIsUpdated"/>:</B></td>
                    <td><html:text property="serviceProviderReviewed" readonly="true" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.service"/>:</B></td>
                    <td>
                        <select>
                            <option>WMS</option>
                        </select>
                    </td>
                </tr>
                
                </td>
                </tr>
                <tr>
                    <td colspan="0"><center>
                            <html:submit property="cancel" onclick="bCancel=true">
                                <fmt:message key="button.cancel"/>
                            </html:submit>
                            <html:submit property="save" >
                                <fmt:message key="button.ok"/>
                            </html:submit>
                    </center></td>
                </tr>
            </table>
        </div>
    </c:if>
    
    <br /><br /><a href="#" onclick="toggleDiv('adminContainer');">Toon/verberg administratieve meldingen</a><br />&nbsp;<br />&nbsp;
    <div id="adminContainer" class="containerdiv" style="clear: left; display: none;">
        <b>Administratieve meldingen</b>
        <DIV class=tableContainer id=tableContainer2 style="top: auto">
            <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
                <THEAD class=fixedHeader>
                    <TR> 
                        <TH><B>Administratieve data</B></TH>
                    </TR>
                </THEAD>
                <TBODY class=scrollContent>
                    <TR><TD></TD></TR>
                </TBODY>
            </TABLE>
        </DIV>
    </div>
</html:form>