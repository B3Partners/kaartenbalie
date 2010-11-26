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

<c:set var="form" value="${rightsForm.map}"/>
<c:set var="mainid" value="${form.id}"/>
<c:set var="orgId" value="${form.orgId}"/>
<c:set var="orgName" value="${form.orgName}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<html:form action="/rights2">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="type" />

        <H1><fmt:message key="beheer.rights2.title" /></H1>
        <table>
            <tr>
                <td style="color: #196299;">
                    <fmt:message key="beheer.rights2.set" />
                </td>
                <td colspan="3">
                    <html:select property="orgId">
                        <c:forEach var="orgItem" items="${organizationlist}">
                            <html:option value="${orgItem.id}">
                                <c:out value="${orgItem.name}"/>
                            </html:option>
                        </c:forEach>
                    </html:select>&nbsp;
                    <html:submit property="edit" styleClass="knop">
                        <fmt:message key="button.change"/>
                    </html:submit>
                </td>
            </tr>
        </table>
    <div class="containerdiv" style="float: left; clear: none;">
        <c:choose>
            <c:when test="${!empty serviceproviderlist}">
                <div style="height:325px;">
                    <table id="rights2_table" class="tablesorter">
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
                                <c:set var="id_selected" value='' />
                                <c:if test="${nServiceProvider.id == mainid}"><c:set var="id_selected" value='selected' /></c:if>
                                <tr>
                                    <td>
                                        <c:out value="${nServiceProvider.type}"/>
                                    </td>
                                    <td>
                                        <html:link page="/rights2.do?edit=submit&id=${nServiceProvider.id}&type=${nServiceProvider.type}&orgId=${orgId}">
                                            <c:out value="${nServiceProvider.givenName}"/>
                                        </html:link>
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
                </div>

                <script type="text/javascript">
                    tablepager(
                    'rights2_table',
                    '930',
                    '14',
                    false
                );
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.rights2.geenbeschikbaar" />
            </c:otherwise>
        </c:choose>
    </div>

    <c:choose>
        <c:when test="${not empty mainid}">
            <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
                <div class="serverDetailsClass">
                    <table >
                        <tr>
                            <td valign="top" style="padding-left: 40px;">
                                <b><fmt:message key="beheer.rights2.rechten" /> <c:out value="${orgName}"/>:&nbsp;</b>
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.update"/>
                                </html:submit>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" style="padding-left: 40px;">
                                <div id="treeContainerWide">
                                    <div class="treeHolderWide" id="tree">

                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="scrollTOP" style="margin-top: 50px;">

            </div>
        </c:when>
        <c:otherwise>
        </c:otherwise>
    </c:choose>


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
                "expandAll": true
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
