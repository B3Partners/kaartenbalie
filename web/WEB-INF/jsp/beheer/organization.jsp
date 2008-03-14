<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${organizationForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

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
        <H1>Beheer Organisaties</H1>
        
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
                                    <html:link page="/organization.do?edit=submit&id=${nOrganization.id}">
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
    
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 500px;" class="containerdiv">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass">
                    <table >
                        <tr>
                            <td width="330" valign="top">
                                <table>
                                    <tr>
                                        <td><B><fmt:message key="beheer.name"/>:</B></td>
                                        <td><html:text property="name" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationStreet"/>:</B></td>
                                        <td><html:text property="street" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationNumber"/>:</B></td>
                                        <td><html:text property="number" size="5" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationToevoeging"/>:</B></td>
                                        <td><html:text property="addition" size="8" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationPostalcode"/>:</B></td>
                                        <td><html:text property="postalcode" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationProvince"/>:</B></td>
                                        <td><html:text property="province" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationCountry"/>:</B></td>
                                        <td><html:text property="country" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationPostbox"/>:</B></td>
                                        <td><html:text property="postbox" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationFacturationAddress"/>:</B></td>
                                        <td><html:text property="billingAddress" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationVisitorAddress"/>:</B></td>
                                        <td><html:text property="visitorsAddress" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationTelephone"/>:</B></td>
                                        <td><html:text property="telephone" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationFaxnumber"/>:</B></td>
                                        <td><html:text property="fax" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationBbox"/>:</B></td>
                                        <td><html:text property="bbox" size="22" /></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.organizationCode"/>:</B></td>
                                        <td><html:text property="code" size="22" /></td>
                                    </tr>
                                </table>
                            </td>
                            <td valign="top" rowspan="0" style="padding-left: 40px;">
                                <b>Layer rechten:</b><br/><br/>
                                <div id="treeContainerLarge">
                                    <div class="treeHolderLarge">
                                        <div id="tree"></div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                    
                    <div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <c:if test="${empty mainid}">
                            <html:submit property="save" accesskey="s" styleClass="knop">
                                <fmt:message key="button.save"/>
                            </html:submit>
                        </c:if>
                        <c:if test="${not empty mainid}">
                            <html:submit property="save" accesskey="s" styleClass="knop">
                                <fmt:message key="button.update"/>
                            </html:submit>
                            <html:submit property="delete" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                <fmt:message key="button.remove"/>
                            </html:submit>
                        </c:if>
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
    
    <script type="text/javascript">
    <c:if test = "${not empty layerList}">
        
        var root = ${layerList};
        function itemClick(item) {
            var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);        
            treeview_toggleItemChildren(DOMItemId);
        }
        
        function createLabel(container, item) {
            var div = document.createElement("div");
            var vink= document.createElement("input");
            
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
            container.appendChild(div);
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
                var element=document.getElementById(layers[i]);
                if (element){
                    element.checked=true;
                }
            }
        </c:if>
        
        function mainMap_onUpdateProgress(){
            setMapInfo();
        }
        function setMapInfo(){
            var e=flamingo.call("mainMap", "getCurrentExtent");
            var s=flamingo.call("mainMap", "getCurrentScale");
            
            var x=Math.round((e.minx+e.maxx)/2);
            var y=Math.round((e.miny+e.maxy)/2);
            document.getElementById("currentScale").innerHTML="Schaal= "+s;
            document.getElementById("currentCoordinates").innerHTML="X= "+x+ " Y= "+y;
        }
        
    </c:if>
    </script>
</html:form>
