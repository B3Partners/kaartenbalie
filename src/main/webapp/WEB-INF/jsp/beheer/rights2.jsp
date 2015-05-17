<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2007-2011 B3Partners BV.

This program is distributed under the terms
of the GNU General Public License.

You should have received a copy of the GNU General Public License
along with this software. If not, see http://www.gnu.org/licenses/gpl.html

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<script type="text/javascript" src="<html:rewrite page='/dwr/engine.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/dwr/interface/JRightsSupport.js' module='' />"></script>

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/json2.js' module='' />"></script>


<html:form action="/rights2">
    <html:hidden property="id" />
    <html:hidden property="type" />

    <H1><fmt:message key="beheer.rights2.title" /></H1>
    <div class="containerdiv">
        <c:choose>
            <c:when test="${!empty serviceproviderlist}">
                <table id="rights2_table" class="dataTable">
                    <thead>
                        <tr>
                            <th style="width: 10%;"><fmt:message key="beheer.rights2.table.type" /></th>
                            <th style="width: 50%;"><fmt:message key="beheer.rights2.table.naam" /></th>
                            <th style="width: 20%;"><fmt:message key="beheer.rights2.table.afkorting" /></th>
                            <th style="width: 20%;"><fmt:message key="beheer.rights2.table.datumupdate" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                            <tr>
                                <td>
                                    <c:out value="${nServiceProvider.type}"/>
                                </td>
                                <td>
                                    <a href="#" onclick="changeService('${nServiceProvider.id}', '${nServiceProvider.type}');">
                                        <c:out value="${nServiceProvider.givenName}"/>
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${nServiceProvider.abbr}"/>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.rights2.geenbeschikbaar" />
            </c:otherwise>
        </c:choose>
    </div>

    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        <div class="serverDetailsClass">
            <table>
                <tr>
                    <td valign="top" style="width: 360px; padding-right: 20px;">
                        <b><fmt:message key="beheer.rights2.groups" />:</b>
                        <html:select property="orgId" onchange="changeOrganization();">
                            <c:forEach var="orgItem" items="${organizationlist}">
                                <html:option value="${orgItem.id}">
                                    <c:out value="${orgItem.name}"/>
                                </html:option>
                            </c:forEach>
                        </html:select>
                    </td>
                    <td valign="top" style="width: 380px;">
                        <html:button property="save" onclick="submitRightsForm();" accesskey="s" styleClass="knop">
                            <fmt:message key="button.update"/>
                        </html:button>
                    </td>
                </tr>                
                <tr>
                    <td valign="top" style="width: 360px; padding-right: 20px;">
                        <b><fmt:message key="beheer.rights2.set" />:&nbsp;</b>
                        <div class="treeHolderWide" id="layerContainer" style="width: 360px;"></div>
                    </td>
                    <td valign="top" style="width: 380px;">
                        <b><fmt:message key="beheer.rights2.rechten" />:&nbsp;</b>                      
                        <div class="treeHolderWide" id="tree" style="width: 380px;"></div>
                    </td>
                </tr>                
            </table>
        </div>
    </div>

</html:form>

<script type="text/javascript">
function createRightsTree(rtree) {
    $("#tree").html('');
    var treeobj = JSON.parse(rtree);

    treeview_create({
        "id": "tree",
        "root": treeobj,
        "rootChildrenAsRoots": true,
        "itemLabelCreatorFunction": createLabel,
        "toggleImages": {
            "collapsed": "<html:rewrite page='/images/treeview/plus.gif' module=''/>",
            "expanded": "<html:rewrite page='/images/treeview/minus.gif' module=''/>",
            "leaf": "<html:rewrite page='/images/treeview/leaft.gif' module=''/>"
        },
        "saveExpandedState": false,
        "saveScrollState": false,
        "expandAll": true
    });
}
</script>

<script type="text/javascript" src="<html:rewrite page='/js/rights.js' module='' />"></script>