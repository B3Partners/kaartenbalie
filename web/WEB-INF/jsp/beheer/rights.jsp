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
<c:set var="mainid" value="${form.id}"/>
<c:set var="orgName" value="${form.name}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<html:form action="/organization">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="name" />

    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer Rechten</H1>

        <table style="width: 740px;" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
            <thead>
                <tr class="serverRijTitel" id="topRij">
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:0}); sortTable(this);" width="200"><div>Naam organisatie</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:1}); sortTable(this);" width="250"><div>Adres</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:2}); sortTable(this);" width="125"><div>Plaats</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:3}); sortTable(this);" width="100"><div>Telefoon</div></td>
                </tr>
            </thead>
        </table>

        <c:set var="hoogte" value="${(fn:length(organizationlist) * 21)}" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>

        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px; width: 770px;">          
            <table id="server_table" class="table-autosort table-stripeclass:table_alternate_tr" width="740" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
                <tbody>
                    <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">

                        <tr class="serverRij" onmouseover="showLabel(${nOrganization.id})" onmouseout="hideLabel(${nOrganization.id});">
                            <td width="200">
                                <div style="width: 190px; overflow: hidden;">
                                    <html:link page="/organization.do?editRights=submit&id=${nOrganization.id}">
                                        <c:out value="${nOrganization.name}"/>
                                    </html:link>
                                </div>
                            </td>

                            <td style="width: 250px;">
                                <c:out value="${nOrganization.street}"/>&nbsp;<c:out value="${nOrganization.number}"/><c:out value="${nOrganization.addition}"/>
                            </td>
                            <td style="width: 125px;">
                                <c:out value="${nOrganization.province}"/>
                            </td>
                            <td style="width: 100px;">
                                <c:out value="${nOrganization.telephone}"/>
                            </td>
                        </tr>
                    <div id="infoLabel${nOrganization.id}" class="infoLabelClass">
                        <strong>Naam:</strong> <c:out value="${nOrganization.name}"/><br />
                        <strong>Adres:</strong> <c:out value="${nOrganization.street}"/>&nbsp;<c:out value="${nOrganization.number}"/><c:out value="${nOrganization.addition}"/><br />
                        <strong>Postcode:</strong> <c:out value="${nOrganization.postalcode}"/><br />
                        <strong>Plaats:</strong> <c:out value="${nOrganization.province}"/><br />
                        <strong>Land:</strong> <c:out value="${nOrganization.country}"/><br />
                        <strong>Telefoon:</strong> <c:out value="${nOrganization.telephone}"/>
                    </div>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script type="text/javascript">
        var server_table = document.getElementById('server_table');
        Table.stripe(server_table, 'table_alternate_tr');
        Table.sort(server_table, {sorttype:Sort['alphanumeric'], col:0});
    </script>

    <c:if test="${not empty mainid}">
        <div id="groupDetails" style="clear: left; padding-top: 15px; height: 500px;" class="containerdiv">
            <div class="serverDetailsClass">
                <table >
                    <tr>
                        <td valign="top" rowspan="0" style="padding-left: 40px;">
                            <b>Rechten <c:out value="${orgName}"/>:&nbsp;</b>
                            <html:submit property="saveRights" accesskey="s" styleClass="knop">
                                <fmt:message key="button.update"/>
                            </html:submit>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" rowspan="0" style="padding-left: 40px;">
                            <div id="treeContainerWide">
                                <div class="treeHolderWide">
                                    <div id="tree"></div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </c:if>

    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>

    <script type="text/javascript">
        <c:if test = "${not empty layerList && not empty mainid}">
        
            var root = ${layerList};
            function itemClick(item) {
                var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);
                treeview_toggleItemChildren(DOMItemId);
            }
        
            function createLabel(container, item) {
                var div = document.createElement("div");
                var vink= document.createElement("input");
                var label= document.createElement("label");

                div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
                if(div.className == 'serviceproviderLabel') {
                    currentParent = container.id;
                }
            
                if (item.id && item.type == "layer") {
                    vink.type="checkbox";
                    vink.value=item.id;
                    vink.name="selectedLayers";
                    vink.id=item.id;
                    vink.layerType=item.type;
                    vink.className="layerVink " + currentParent;
                    container.appendChild(vink);
                }
            
                div.onclick = function() {
                    itemClick(item);
                };
                div.appendChild(document.createTextNode(item.name));
                label.title=item.id;
                label.appendChild(div);
                container.appendChild(label);
                if (item.children){
                    var d=document.createElement("a");
                    d.href="#";
                    d.onclick= function(){setAllTrue(this);};
                    d.selecteditem=item;
                    d.innerHTML="&nbsp;Alles";
                    container.appendChild(d);
                }
            }

            treeview_create({
                "id": "tree",
                "root": root,
                "rootChildrenAsRoots": true,
                "itemLabelCreatorFunction": createLabel,
                "toggleImages": {
                    "collapsed": "<html:rewrite page='/images/treeview/plus.gif' module=''/>",
                    "expanded": "<html:rewrite page='/images/treeview/minus.gif' module=''/>",
                    "leaf": "<html:rewrite page='/images/treeview/leaft.gif' module=''/>"
                },
                "saveExpandedState": true,
                "saveScrollState": true,
                "expandAll": false
            });
        
            function setAllTrue(element){
                setAll(element,true);
                element.onclick= function(){setAllFalse(this);};
                element.innerHTML="&nbsp;Niets";
            }
            function setAllFalse(element){
                setAll(element,false);
                element.onclick= function(){setAllTrue(this);};
                element.innerHTML="&nbsp;Alles";
            }
        
            function setAll(element,checked){
                var item=element.selecteditem;
                if(item && item.children){
                    setAllChilds(item.children,checked);
                }
            
            }
            function setAllChilds(children,checked){
                for(var i=0; i < children.length; i++){
                    var element=document.getElementById(children[i].id);
                    if(element){
                        if (checked && element.checked){
                        }else{
                            element.checked=checked;
                        
                        }
                    }
                    if (children[i].children){
                        setAllChilds(children[i].children,checked);
                    }
                }
            }
            //TODO werkt nog niet
            function anyChildChecked(root){
                var children = null;
                if(root && root.children)
                    children = root.children;
                else
                    return false;
                for(var i=0; i < children.length; i++){
                    var element=document.getElementById(children[i].id);
                    if(element){
                        if (element.checked){
                            return true;
                        }
                    }
                    if (children[i].children){
                        if (anyChildChecked(children[i]))
                            return true;
                    }
                }
                return false;
            }

            //check the selected layers
            <c:if test="${not empty checkedLayers}">
                var layerstring="${checkedLayers}";
                var layers=layerstring.split(",");
                for (var i=0; i < layers.length; i++){
                    var element = document.getElementById(layers[i]);
                    if (element){
                        element.checked=true;
                    }
                }
            </c:if>
        
        </c:if>
    </script>
</html:form>
