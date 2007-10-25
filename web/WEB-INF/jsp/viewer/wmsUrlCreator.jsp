<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${wmsUrlCreatorForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<c:set var="persUrl" value="${form.map.personalUrl}"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/moveLayersGetMap.js' module='' />"></script>

<html:javascript formName="wmsUrlCreatorForm" staticJavascript="false"/>
<html:form action="/wmsUrlCreator" onsubmit="return validateWmsUrlCreatorForm(this)" focus="height">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="personalUrl"/>
    
    
    <c:choose>
        <c:when test="${not empty persUrl}">
            <div class="containerdiv" style="float: left; clear: none; margin-bottom: 15px;">
                <H1>Persoonlijke GetMap URL</H1>
                Op deze pagina kunt u een persoonlijke GetMap URL maken die gebaseerd is op uw Persoonlijke URL.
                U kunt deze GetMap URL gebruiken voor viewers die het GetCapabilities commando niet ondersteunen.
            </div>    
                
                <!-- <b>Layer rechten:</b><br/> --><br />
                <div id="treeContainerLarge" style="height: 240px;">
                    <div class="treeHolderLarge" style="height: 240px;">
                        <div id="tree"></div>
                    </div>
                </div>
                <div style="float: left;width: 200px; height: 240px;">
                    <div class="getMapLabelValue">
                        <div class="getMapLabel"><fmt:message key="beheer.getmapurl.projecties"/>:</div>
                        <c:if test="${not empty projectieList}">
                            <div class="getMapValue">
                                <html:select property="selectedProjectie">
                                    <c:forEach items="${projectieList}" var="p">
                                        <html:option value="${p}"><c:out value="${p}"/></html:option><br/>
                                    </c:forEach>
                                </html:select>
                            </div>
                        </c:if>
                    </div>
                    <div class="getMapLabel"><fmt:message key="beheer.getmapurl.height"/>:</div>
                    <div class="getMapValue"><html:text property="height"/></div>
                    
                    <div class="getMapLabel"><fmt:message key="beheer.getmapurl.width"/>:</div>
                    <div class="getMapValue"><html:text property="width"/></div>
                    <div class="getMapLabel"><fmt:message key="beheer.getmapurl.bbox"/>:</div>
                    <div class="getMapValue"><html:text property="bbox" size="40"/></div>
                    <div class="getMapLabel"><fmt:message key="beheer.getmapurl.format"/>:</div>
                    
                    <c:if test="${not empty formatList}">
                        <div class="getMapValue">
                            <html:select property="selectedFormat">
                                <c:forEach items="${formatList}" var="f">
                                    <html:option value="${f}"><c:out value="${f}"/></html:option><br/>
                                </c:forEach>
                            </html:select>
                        </div>
                    </c:if>
                </div>
                <%-- Layer volgorde aanpassen. Dit werkt alleen nog niet. Onderste rij is met de URL. --%>
                <div class="volgordeBox">    
                    <br /><b>Volgorde</b><br>
                    Bepaal hieronder de volgorde van de kaarten.
                    <br />Selecteer een kaart en verplaats het met de knoppen.
                    <input type="button" value="/\" onclick="javascript: moveSelectedUp()"/>
                    <input type="button" value="\/" onclick="javascript: moveSelectedDown()"/>
                    <div id="orderLayerBox" class="orderLayerBox"></div>
                </div>
                Persoonlijke GetMap URL:<BR>
                <html:textarea property="defaultGetMap" styleClass="readOnly" styleId="defaultGetMap" readonly="true" cols="85" rows="5" />
                
                <div style="clear: both; height: 0px;"></div>
                <html:submit property="getMapUrl">
                    <fmt:message key="beheer.kaarten.wmsurlcreator.getMap"/>
                </html:submit>
                
                
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
                            vink.onclick=function(){checkboxClick(this);};
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
                        "expandAll": true
                    });  
                        
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
                   

                </c:if>
                </script>
            
        </c:when>
        <c:otherwise>
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
            </div>
        </c:otherwise>
    </c:choose> 
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>