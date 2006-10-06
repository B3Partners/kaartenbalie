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


<table>
    <tr>
    <td>table 1
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
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            <TR>
            <TD><INPUT TYPE=CHECKBOX NAME="name"></TD>
            <TD>Nando</TD>
            <TD>de Goeij</TD>
            <TD>nandodegoeij</TD>
            <TD>degoeij@orange.nl</TD></TR>
            </TBODY>
        </TABLE>
    </DIV>
    </td>
    <td>table 2
    <DIV class=smallTableContainer id=smallTableContainer>
        <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
            <THEAD class=fixedHeader>
            <TR>
            <TH><B>Layer data</B></TH>
            </TR>
            </THEAD>
            <TBODY class=scrollContent>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            <TR><TD>Tree structure komt hier</TD></TR>
            </TBODY>
        </TABLE>
    </DIV>
    </td>
    </tr>
    <tr>
    <td> table 3
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
   
   

<c:forEach var="nUser" varStatus="status" items="${userlist}">
   <a href="user.do?id=${nUser.id}"><c:out value="${nUser.lastName}, ${nUser.firstName}"/></a><br>
</c:forEach>

<html:form action="/user">
    Lastname:   <html:text property="lastname"/><br>
    Firstname: <html:text property="firstname"/><br>
    Email: <html:text property="email"/><br>
    Username: <html:text property="username"/><br>
    Password: <html:text property="password"/><br>
</html:form>
    
    
