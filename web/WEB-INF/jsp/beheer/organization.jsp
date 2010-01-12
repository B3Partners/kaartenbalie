<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${organizationForm.map}"/>
<c:set var="action" value="${form.action}"/>
<c:set var="mainid" value="${form.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<html:javascript formName="organizationForm" staticJavascript="false"/>
<html:form action="/organization" onsubmit="return validateOrganizationForm(this)" focus="name">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
 
    <div class="containerdiv" style="float: left; clear: none;">
        <H1><fmt:message key="beheer.organization.title" /></H1>

        <c:choose>
            <c:when test="${!empty organizationlist}">
                <c:set var="hoogte" value="${(fn:length(organizationlist) * 28) + 28}" />
                <c:if test="${hoogte > 400}">
                    <c:set var="hoogte" value="400" />
                </c:if>
                <div class="scroll" style="height: ${hoogte}px; width: 840px;">
                    <table id="server_table" class="tablesorter">
                        <thead>
                            <tr>
                                <th style="width: 30%;" id="sort_col1"><fmt:message key="beheer.organization.table.naam" /></th>
                                <th style="width: 37%;" id="sort_col2"><fmt:message key="beheer.organization.table.adres" /></th>
                                <th style="width: 19%;" id="sort_col3"><fmt:message key="beheer.organization.table.plaats" /></th>
                                <th style="width: 15%;" id="sort_col4"><fmt:message key="beheer.organization.table.telefoon" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                                <c:set var="id_selected" value='' />
                                <c:if test="${nOrganization.id == mainid}"><c:set var="id_selected" value=' id="regel_selected"' /></c:if>
                                <tr onmouseover="showLabel(${nOrganization.id})" onmouseout="hideLabel(${nOrganization.id});"${id_selected}>
                                    <td style="width: 30%;">
                                        <div style="width: 100%; overflow: hidden;">
                                            <html:link page="/organization.do?edit=submit&id=${nOrganization.id}">
                                                <c:out value="${nOrganization.name}"/>
                                            </html:link>
                                        </div>
                                    </td>

                                    <td style="width: 37%;">
                                        <div style="width: 100%; overflow: hidden;"><c:out value="${nOrganization.street}"/>&nbsp;<c:out value="${nOrganization.number}"/><c:out value="${nOrganization.addition}"/></div>
                                    </td>
                                    <td style="width: 19%;">
                                        <div style="width: 100%; overflow: hidden;"><c:out value="${nOrganization.province}"/></div>
                                    </td>
                                    <td style="width: 15%;">
                                        <div style="width: 100%; overflow: hidden;"><c:out value="${nOrganization.telephone}"/></div>
                                    </td>
                                </tr>
                                <div id="infoLabel${nOrganization.id}" class="infoLabelClass">
                                    <strong><fmt:message key="beheer.organization.infolabel.naam" />:</strong> <c:out value="${nOrganization.name}"/><br />
                                    <strong><fmt:message key="beheer.organization.table.adres" />:</strong> <c:out value="${nOrganization.street}"/>&nbsp;<c:out value="${nOrganization.number}"/><c:out value="${nOrganization.addition}"/><br />
                                    <strong><fmt:message key="beheer.organizationPostalcode" />:</strong> <c:out value="${nOrganization.postalcode}"/><br />
                                    <strong><fmt:message key="beheer.organization.table.plaats" />:</strong> <c:out value="${nOrganization.province}"/><br />
                                    <strong><fmt:message key="beheer.organizationCountry" />:</strong> <c:out value="${nOrganization.country}"/><br />
                                    <strong><fmt:message key="beheer.organization.table.telefoon" />:</strong> <c:out value="${nOrganization.telephone}"/>
                                </div>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <script type="text/javascript">
                    if(document.getElementById('regel_selected')) {
                        $("#regel_selected").addClass('selected');
                        if(${hoogte} == 400) $(".scroll").scrollTop(($("#regel_selected").position().top - $("#regel_selected").parent().position().top));
                    }
                    $("#server_table").tablesorter({
                        widgets: ['zebra', 'hoverRows', 'fixedHeaders'],
                        sortList: [[0,0]],
                        textExtraction: linkExtract
                    });
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.organization.geenbeschikbaar" />
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 500px;" class="containerdiv">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass">
                    <table>
                        <tr>
                            <td><B><fmt:message key="beheer.name"/>:</B></td>
                            <td><html:text property="name" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationStreet"/>:</B></td>
                            <td><html:text property="street" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationNumber"/>:</B></td>
                            <td><html:text property="number" size="5" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationToevoeging"/>:</B></td>
                            <td><html:text property="addition" size="5" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationPostalcode"/>:</B></td>
                            <td><html:text property="postalcode" size="15" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationProvince"/>:</B></td>
                            <td><html:text property="province" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationCountry"/>:</B></td>
                            <td><html:text property="country" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationPostbox"/>:</B></td>
                            <td><html:text property="postbox" size="15" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationFacturationAddress"/>:</B></td>
                            <td><html:text property="billingAddress" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationVisitorAddress"/>:</B></td>
                            <td><html:text property="visitorsAddress" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationTelephone"/>:</B></td>
                            <td><html:text property="telephone" size="25" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationFaxnumber"/>:</B></td>
                            <td><html:text property="fax" size="25" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationBbox"/>:</B></td>
                            <td><html:text property="bbox" size="50" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationCode"/>:</B></td>
                            <td><html:text property="code" size="25" /></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.allowAccountingLayers"/>:</B></td>
                            <td><html:checkbox property="allow" /></td>
                        </tr>
                    </table>

                    <div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <c:choose>
                            <c:when test="${save || delete}">
                                <html:submit property="confirm" accesskey="o" styleClass="knop" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                                    <fmt:message key="button.ok"/>
                                </html:submit>
                            </c:when>
                            <c:when test="${not empty mainid}">
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.update"/>
                                </html:submit>
                                <html:submit property="deleteConfirm" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                    <fmt:message key="button.remove"/>
                                </html:submit>
                            </c:when>
                            <c:otherwise>
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.save"/>
                                </html:submit>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="name"/>
                <div class="knoppen">
                    <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.new"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>

</html:form>
