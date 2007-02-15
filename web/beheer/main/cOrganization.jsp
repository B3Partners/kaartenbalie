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

<html:javascript formName="organizationForm" staticJavascript="false"/>
<html:form action="/organization" onsubmit="return validateOrganizationForm(this)">
    
    <div class="containerdiv" style="float: left; clear: none;">
        <b>Groepstabel:</b>
        <div class="serverRijTitel">
            <div style="width: 50px;">Select</div>
            <div style="width: 700px;">Groepsnaam</div>
        </div>
        <div class="tableContainer" id="tableContainer">          
            <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                <div class="serverRij">
                    <div style="width: 50px;"><html:multibox value="${nOrganization.id}" property="organizationSelected" /></div>
                    <div style="width: 700px;"><a href="organization.do?id=${nOrganization.id}"><c:out value="${nOrganization.name}"/></a></div>
                </div>
            </c:forEach>
        </div>
    </div>
    <html:submit value="Delete organization(s)" property="delete" style="margin-top: 5px; margin-bottom: 5px;"/>
    
    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        <table>
            <tr>
                <td width="300">
                    <table>
                        <tr>
                            <td><B>Vestiging ID:</B></td>
                            <td><html:text property="id" readonly="true" /></td>
                        </tr>
                        <tr>
                            <td><B>Naam organisatie:</B></td>
                            <td><html:text property="name"/></td>
                        </tr>
                        <tr>
                            <td><B>Straat:</B></td>
                            <td><html:text property="organizationStreet"/></td>
                        </tr>
                        <tr>
                            <td><B>Nummer:</B></td>
                            <td><html:text property="organizationNumber"/></td>
                        </tr>
                        <tr>
                            <td><B>Toevoeging:</B></td>
                            <td><html:text property="organizationAddition"/></td>
                        </tr>
                        <tr>
                            <td><B>Postcode:</B></td>
                            <td><html:text property="organizationPostalcode"/></td>
                        </tr>
                        <tr>
                            <td><B>Provincie:</B></td>
                            <td><html:text property="organizationProvince"/></td>
                        </tr>
                        <tr>
                            <td><B>Land:</B></td>
                            <td><html:text property="organizationCountry"/></td>
                        </tr>
                        <tr>
                            <td><B>Postbus:</B></td>
                            <td><html:text property="organizationPostbox"/></td>
                        </tr>
                        <tr>
                            <td><B>Factuuradres:</B></td>
                            <td><html:text property="organizationBillingAddress"/></td>
                        </tr>
                        <tr>
                            <td><B>Bezoekadres:</B></td>
                            <td><html:text property="organizationVisitorsAddress"/></td>
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
                                    <html:submit property="save">
                                        <fmt:message key="button.ok"/>
                                    </html:submit>
                            </center></td>
                        </tr>
                    </table>
                </td>
                <td valign="top">
                    <b>Layer rechten:</B><br /><br />
                    <DIV class=smallTableContainer id=smallTableContainer>            
                        <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
                            <THEAD class=fixedHeader>
                                <TR>
                                    <TH><B>Beschikbare servers</B></TH>
                                </TR>
                            </THEAD>
                            <TBODY class=scrollContent>
                                <c:forEach var="nLayer" varStatus="status" items="${layerlist}">
                                    <TR>
                                        <TD><html:multibox value="${nLayer.id}" property="layerSelected" />
                                        <c:out value="${nLayer.name}"/></TD>
                                    </TR>
                                </c:forEach>
                            </TBODY>
                        </TABLE>
                    </DIV>
                </td>
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