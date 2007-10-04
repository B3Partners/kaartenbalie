<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${organizationForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<html:javascript formName="organizationForm" staticJavascript="false"/>
<html:form action="/organization" onsubmit="return validateOrganizationForm(this)" focus="name">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer Organisaties</H1>
        
        <b>Organisatie tabel:</b>
        <div class="serverRijTitel">
            <div style="width: 200px;">Naam organisatie</div>
            <div style="width: 250px;">Adres</div>
            <div style="width: 125px;">Plaats</div>
            <div style="width: 100px;">Telefoon</div>
        </div>
        
        <c:set var="hoogte" value="${(fn:length(organizationlist) * 21)}" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>
        
        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px">          
            <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                <div class="serverRij">
                    <div style="width: 200px;" class="vakSpeciaal" title="<c:out value="${nOrganization.name}"/>">
                        <html:link page="/organization.do?edit=submit&id=${nOrganization.id}">
                            <c:out value="${nOrganization.name}"/>
                        </html:link>
                    </div>
                    <div style="width: 250px;" title="<c:out value="${nOrganization.street}"/>">
                        <c:out value="${nOrganization.street}"/>&nbsp;<c:out value="${nOrganization.number}"/><c:out value="${nOrganization.addition}"/>
                    </div>
                    <div style="width: 125px;" class="vakSpeciaal" title="<c:out value="${nOrganization.province}"/>">
                        <c:out value="${nOrganization.province}"/>
                    </div>
                    <div style="width: 100px;" title="<c:out value="${nOrganization.telephone}"/>">
                        <c:out value="${nOrganization.telephone}"/>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    
    <c:choose>
        <c:when test="${action != 'list'}">
            <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
                <table >
                    <tr>
                        <td width="300" valign="top">
                            <table>
                                <tr>
                                    <td><B><fmt:message key="beheer.name"/>:</B></td>
                                    <td><html:text property="name"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationStreet"/>:</B></td>
                                    <td><html:text property="street"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationNumber"/>:</B></td>
                                    <td><html:text property="number"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationToevoeging"/>:</B></td>
                                    <td><html:text property="addition"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationPostalcode"/>:</B></td>
                                    <td><html:text property="postalcode"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationProvince"/>:</B></td>
                                    <td><html:text property="province"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationCountry"/>:</B></td>
                                    <td><html:text property="country"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationPostbox"/>:</B></td>
                                    <td><html:text property="postbox"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationFacturationAddress"/>:</B></td>
                                    <td><html:text property="billingAddress"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationVisitorAddress"/>:</B></td>
                                    <td><html:text property="visitorsAddress"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationTelephone"/>:</B></td>
                                    <td><html:text property="telephone"/></td>
                                </tr>
                                <tr>
                                    <td><B><fmt:message key="beheer.organizationFaxnumber"/>:</B></td>
                                    <td><html:text property="fax" /></td>
                                </tr>
                                
                            </table>
                        </td>
                        <td valign="top" rowspan="0">
                            <b>Layer rechten:</b><br/><br/>
                            <div id="treeContainerLarge">
                                <div class="treeHolderLarge">
                                    <div id="tree"></div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
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
            
            if (item.id && item.type != "serviceprovider") {
                vink.type="checkbox";
                vink.value=item.id;
                vink.name="selectedLayers";
                vink.id=item.id;
                vink.layerType=item.type;
                vink.className="layerVink";
                container.appendChild(vink);
            }
            
            div.className = item.type == "serviceprovider" ? "serviceproviderLabel" : "layerLabel";
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
                d.innerHTML=" Selecteer alles";
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
        function reloadLayers(){
            var layersString="";
            var orderLayerBox= document.getElementById("orderLayerBox");
            var orderLayers=orderLayerBox.childNodes;
            for (var i=0; i < orderLayers.length; i++){
                if(layersString.length==0){
                    layersString+=orderLayers[i].name;
                }else{
                    layersString+=","+orderLayers[i].name;
                }
            }                
            //haal de extent op van de mainmap en plaats die in de request
            var e=flamingo.call("mainMap", "getCurrentExtent");
            window.location.href="mapviewer.do?layers="+layersString+"&extent="+e.minx + "," + e.miny + "," + e.maxx + "," + e.maxy;
        } 
        
        function setAllTrue(element){
            setAll(element,true);
            element.onclick= function(){setAllFalse(this);};
            element.innerHTML=" Deselecteer alles";
        }
        function setAllFalse(element){
            setAll(element,false);
            element.onclick= function(){setAllTrue(this);};
            element.innerHTML=" Selecteer alles";
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