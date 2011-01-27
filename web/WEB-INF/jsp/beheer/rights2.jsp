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

<script type="text/javascript" src="<html:rewrite page='/dwr/interface/JRightsSupport.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/dwr/engine.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/json2.js' module='' />"></script>


<html:form action="/rights2">
    <html:hidden property="id" />
    <html:hidden property="type" />

    <H1><fmt:message key="beheer.rights2.title" /></H1>
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

    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        <div class="serverDetailsClass">
            <table>
                <tr>
                    <td valign="top" style="width: 360px; padding-right: 20px;">
                        <b><fmt:message key="beheer.rights2.set" />:&nbsp;</b>
                        <html:select property="orgId" onchange="changeOrganization();">
                            <c:forEach var="orgItem" items="${organizationlist}">
                                <html:option value="${orgItem.id}">
                                    <c:out value="${orgItem.name}"/>
                                </html:option>
                            </c:forEach>
                        </html:select>&nbsp;
                        <div class="treeHolderWide" id="layerContainer" style="width: 360px;"></div>
                    </td>
                    <td valign="top" style="width: 380px;">
                        <b><fmt:message key="beheer.rights2.rechten" />:&nbsp;</b>
                        <html:button property="save" onclick="submitRightsForm();" accesskey="s" styleClass="knop">
                            <fmt:message key="button.update"/>
                        </html:button>
                        <div class="treeHolderWide" id="tree" style="width: 380px;"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>

</html:form>

<script type="text/javascript">
    //    $j(document).ready(function() {
    //        findValidLayers();
    //    });

    function collectFormParams() {
        var params = new Object();
        var orgid = document.forms[0].orgId.value;
        var spid = document.forms[0].id.value;
        var sptype = document.forms[0].type.value;
        var selectedLayersStr = "";
        if(document.forms[0].selectedLayers) {
            var selectedLayers = document.forms[0].selectedLayers;
            for (var i=0; i < selectedLayers.length; i++) {
               if(selectedLayers[i].checked) selectedLayersStr += selectedLayers[i].value+",";
            }
        }
        params["orgId"]=orgid;
        params["id"]=spid;
        params["type"]=sptype;
        params["selectedLayers"]=selectedLayersStr;
        return params;
    }

    function changeService(spid, type) {
        document.forms[0].id.value = spid;
        document.forms[0].type.value = type;
        var params = collectFormParams();
        JRightsSupport.getRightsTree(params, createRightsTree);
        JRightsSupport.getValidLayers(params, handleValidLayers);
    }

    function changeOrganization() {
        var params = collectFormParams();
        JRightsSupport.getRightsTree(params, createRightsTree);
        JRightsSupport.getValidLayers(params, handleValidLayers);
    }

    function submitRightsForm() {
        var params = collectFormParams();
        JRightsSupport.submitRightsForm(params, handleValidLayers);

        alert("De rechten zijn succesvol opgeslagen.");

        return false;
    }

    function handleValidLayers(validlayers) {
        // Create container
        var layerContainer = $('<div></div>').attr({
            "class": "layersContainer"
        });
        //layerContainer.append("Kaartlagen");

        var layerList = $('<ol></ol>');
 
        // Create table content
        if(validlayers) {
            $.each(validlayers, function(index, layer) {
                var li = $('<li></li>');
                li.html(layer);
                 layerList.append(li);
            });
        }
        layerContainer.append(layerList);

        $('#layerContainer').html('').append(layerContainer);
    }

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

    function itemClick(item) {
        var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);
        treeview_toggleItemChildren(DOMItemId);
    }

    function createLabel(container, item) {
        var div = document.createElement("div");
        var label = document.createElement("label");

        div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
        if(div.className == 'serviceproviderLabel') {
            currentParent = container.id;
        }

        var vink;
        if (item.id && item.type == "layer") {           
            /* Voor IE7 */
            if (navigator.appName=="Microsoft Internet Explorer") {
                var vinkStr = '<input type="checkbox" id="' + item.id + '"';

                if (item.visible == "true") {
                    vinkStr += ' checked="checked"';
                }
                
                vinkStr += ' name="selectedLayers"';
                vinkStr += '>';

                vink = document.createElement(vinkStr);

                vink.value = item.id;
                vink.className = "layerVink " + currentParent;
                vink.layerType = item.type;
            } else {

                vink = document.createElement("input");
                
                if (item.visible == "true") {
                    vink.checked = true;
                }

                vink.type = "checkbox";
                vink.value=item.id;
                vink.name="selectedLayers";
                vink.id=item.id;
                vink.layerType=item.type;
                vink.className="layerVink " + currentParent;
            }
            
            label.appendChild(vink);
        }

        div.onclick = function() {
            itemClick(item);
        };
        div.appendChild(document.createTextNode(item.name));
        label.title=item.id;
        label.appendChild(div);
        label.style.whiteSpace = 'nowrap';
        label.style.width = 'auto';
        container.appendChild(label);
        if (item.children) {
            var d=document.createElement("a");
            d.href="#";
            d.onclick= function(){setAllTrue(this);};
            d.selecteditem=item;
            d.innerHTML="&nbsp;Alles";
            container.appendChild(d);
        }
    }

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

</script>
