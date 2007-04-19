<%@include file="/templates/taglibs.jsp" %>

<c:set var="form" value="${wmsUrlCreatorForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<html:javascript formName="wmsUrlCreatorForm" staticJavascript="false"/>
<html:form action="/wmsUrlCreator" onsubmit="return validateWmsUrlCreatorForm(this)">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    
    <div class="containerdiv" style="float: left; clear: none;">
    <H1>Persoonlijke GetMap URL</H1>
    <P>
        Op deze pagina kunt u een persoonlijke GetMap URL maken die gebaseerd is op uw Persoonlijke URL.
        Indien u nopg geen persoonlijke URL heeft, dient u deze eerst aan te maken alvorens u een persoonlijke
        GetMap URL kunt maken.
        Met behulp van de persoonlijke GetMap URL kunt u op eenvoudige wijze er voor zorgen dat u bij het
        gebruik van een externe viewer altijd een bepaalde beginsituatie creeert die altijd gelijk is tot
        u deze URL wijzigt.
    </P>
    
    <table border="1px">
        <%-- bovenste rij, met de layers en de instellingen --%>
        <tr>
            <td>
                <%-- linker kolom met de layers --%>
                <table>
                    <tr>
                        <td>
                            <b>Layer rechten:</b><br>
                            <div id="treeContainerSmall">
                                <div class="treeHolderSmall">
                                    <div id="tree"></div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
            <td>
                <%-- rechter kolom met de instellingen --%>
                <table>
                    <tr><td><b><fmt:message key="beheer.getmapurl.projecties"/>:</b></td></tr>
                    <tr><td>
                            <c:if test="${not empty projectieList}">
                                <html:select property="selectedProjectie">
                                    <c:forEach items="${projectieList}" var="p">
                                        <html:option value="${p}"><c:out value="${p}"/></html:option><br/>
                                    </c:forEach>
                                </html:select>
                            </c:if>
                    </td></tr>
                    <tr><td><b><fmt:message key="beheer.getmapurl.height"/>:</b></td></tr>
                    <tr><td><html:text property="height"/></td></tr>
                    <tr><td><b><fmt:message key="beheer.getmapurl.width"/>:</b></td></tr>
                    <tr><td><html:text property="width"/></td></tr>
                    <tr><td><b><fmt:message key="beheer.getmapurl.bbox"/>:</b></td></tr>
                    <tr><td><html:text property="bbox" size="40"/></td></tr>
                    <tr><td><b><fmt:message key="beheer.getmapurl.format"/>:</b></td></tr>
                    <tr><td>
                            <c:if test="${not empty formatList}">
                                <html:select property="selectedFormat">
                                    <c:forEach items="${formatList}" var="f">
                                        <html:option value="${f}"><c:out value="${f}"/></html:option><br/>
                                    </c:forEach>
                                </html:select>
                            </c:if>
                    </td></tr>
                </table>
            </td>
        </tr>
        <%-- onderste rij, met de URL--%>
        <tr >
            <td colspan="0" >
                Persoonlijke GetMap URL:<BR>
                <html:textarea property="defaultGetMap" styleClass="readOnly" styleId="defaultGetMap" readonly="true" cols="75" rows="5" />
            </td>
        </tr>
    </table>
    
    <div style="clear: both">
        <html:submit property="getMapUrl">
            <fmt:message key="beheer.kaarten.wmsurlcreator.getMap"/>
        </html:submit>
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
            //var vink= document.createElement("html:multibox");
            //als het item kinderen heeft dan geen vink toevoegen.
            if (!item.children){
                vink.type="checkbox";
                
                vink.value=item.id;
                //vink.name=item.id;
                vink.name="selectedLayers";
                //vink.property="selectedLayers";
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
                "collapsed": "../images/treeview/plus.gif",
                "expanded": "../images/treeview/minus.gif",
                "leaf": "../images/treeview/leaft.gif"
            },
            "saveExpandedState": true,
            "saveScrollState": true,
            "expandAll": false
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