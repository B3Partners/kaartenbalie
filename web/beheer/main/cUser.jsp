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
<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/user" onsubmit="return validateUserForm(this)">
    <c:if test="${not empty message}">
        <div id="error">
            <h3><c:out value="${message}"/></h3>
        </div>
    </c:if>
    
    <div class="containerdiv" style="float: left; clear: none;">
        <b>Gebruikers tabel:</b>
        <div class="serverRijTitel">
            <div style="width: 50px;">Select</div>
            <div style="width: 160px;">Voornaam</div>
            <div style="width: 160px;">Achternaam</div>
            <div style="width: 160px;">Gebruikersnaam</div>
            <div style="width: 160px;">E-mailadres</div>
        </div>
        <DIV class="tableContainer" id="tableContainer">          
            <c:forEach var="nUser" varStatus="status" items="${userlist}">
                <div class="serverRij">    
                    <div style="width: 50px;"><html:multibox value="${nUser.id}" property="userSelected" /></div>
                    <div style="width: 160px;" class="vakSpeciaal" title="<c:out value="${nUser.firstName}"/>"><c:out value="${nUser.firstName}"/></div>
                    <div style="width: 160px;" title="<c:out value="${nUser.lastName}"/>"><c:out value="${nUser.lastName}"/></div>
                    <div style="width: 160px;" class="vakSpeciaal" title="<c:out value="${nUser.username}"/>"><a href="user.do?id=${nUser.id}"><c:out value="${nUser.username}"/></a></div>
                    <div style="width: 160px;" title="<c:out value="${nUser.emailAddress}"/>"><c:out value="${nUser.emailAddress}"/></div>
                </div>    
            </c:forEach>
        </DIV>
    </div>
    <html:submit value="Delete selected user(s)" property="delete"  style="margin-top: 5px; margin-bottom: 5px;"/>
    
    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        <table>
            <tr>
                <td><B>ID:</B></td>
                <td><html:text property="id" readonly="true" /></td>
            </tr>
            <tr>
                <td><B>Voornaam:</B></td>
                <td><html:text property="firstname"/></td>
            </tr>
            <tr>
                <td><B>Achternaam:</B></td>
                <td><html:text property="lastname"/></td>
            </tr>
            <tr>
                <td><B>Email:</B></td>
                <td><html:text property="emailAddress"/></td>
            </tr>
            <tr>
                <td><B>Gebruikersnaam:</B></td>
                <td><html:text property="username"/></td>
            </tr>
            <tr>
                <td><B>Wachtwoord:</B></td>
                <td><html:text property="password"/></td>
            </tr>
            <tr>
                <td><B>Organisatie:</B></td>
                <td>
                    <html:select property="selectedOrganization">
                        <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                            <html:option value="${nOrganization.id}">${nOrganization.name}</html:option>
                        </c:forEach>
                    </html:select>     
                </td>
            </tr>
            <tr>
                <td><B>Roles:</B></td>
                <td>
                    <html:select property="selectedRole">
                        <html:option value="beheerder">beheerder</html:option>
                        <html:option value="gebruiker">gebruiker</html:option>
                        <html:option value="demogebruiker">demogebruiker</html:option>
                    </html:select>     
                </td>
            </tr>
            <tr>
                <td colspan="0"><center>
                        <html:reset  value="Delete values" />
                        <html:submit value="Add/Change User" property="save"/>
                </center></td>
            </tr>
        </table>
    </div>
    
    <br /><br /><a href="#" onclick="toggleDiv('adminContainer');">Toon/verberg administratieve meldingen</a><br />&nbsp;<br />&nbsp;
    <div id="adminContainer" style="clear: left; display: none;" class="containerdiv">
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