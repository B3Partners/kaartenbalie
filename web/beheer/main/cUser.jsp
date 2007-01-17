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





<table>
    <tr>
    <td valign="top"><b>Gebruikers tabel:</b>
    <DIV class=tableContainer id=tableContainer>
        <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
            <THEAD class=fixedHeader>
            <TR>
            <TH>Select</TH>
            <TH>Voornaam</TH>
            <TH>Achternaam</TH>
            <TH>Gebruikersnaam</TH>
            <TH>Email adres</TH>
            </TR>
            </THEAD>
            <TBODY class=scrollContent>            
            <c:forEach var="nUser" varStatus="status" items="${userlist}">
                <TR>
                    <TD><html:multibox value="${nUser.id}" property="userSelected" /></TD>
                    <TD><c:out value="${nUser.firstName}"/></TD>
                    <TD><c:out value="${nUser.lastName}"/></TD>
                    <TD><a href="user.do?id=${nUser.id}"><c:out value="${nUser.username}"/></a></TD>
                    <TD><c:out value="${nUser.emailAddress}"/></TD>
                </TR>
            </c:forEach>
            </TBODY>
        </TABLE>
    </DIV>
    <html:submit value="Add/Change User" property="delete"/>
    </td>
    <td width="33%" align="right">
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
    </td>
    </tr>
    <tr>
    <td><b>Administratieve meldingen</b>
    <DIV class=tableContainer id=tableContainer2 style="top: auto">
        <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
            <THEAD class=fixedHeader>
            <TR> 
            <TH><B>Administratieve data</B></TH>
            </TR>
            </THEAD>
            <TBODY class=scrollContent>
            <TR><TD>Foutmeldingen</TD></TR>
            <TR><TD>Error code 21: fout in bestand.... bla bla bla</TD></TR>
            <TR><TD>Gevonden locaties in de buurt:</TD></TR>
            <TR><TD>Locatie 1: buurtsuper</TD></TR>
            <TR><TD>Locatie 2: bouwput</TD></TR>
            <TR><TD>Gebruiker 'nando' bestaat al</TD></TR>
            <TR><TD>Ongeldig postcode/adres gegeven</TD></TR>
            <TR><TD>Gebruiker 'nando' opgeslagen in het system</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            <TR><TD>Nando</TD></TR>
            </TBODY>
        </TABLE>
    </DIV>
    </td>
    <td>comments</td>
    </tr>
</table>
</html:form>