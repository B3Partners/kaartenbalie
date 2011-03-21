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
                <H1><fmt:message key="viewer.wmsurlCreator.title" /></H1>
                <fmt:message key="viewer.wmsurlCreator.body" />
            </div>    
            
            <!-- <b>Layer rechten:</b><br/> --><br />
            <div id="treeContainerLarge" style="height: 270px;">
                <div class="treeHolderLarge" style="height: 270px;">
                    <div id="tree"></div>
                </div>
            </div>
            <div style="float: left; width: 300px; height: 240px; margin-left: 15px; margin-top: 0px;" class="serverDetailsClass">
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
            <div class="volgordeBox" style="width: 748px;">    
                <br /><b><fmt:message key="viewer.wmsurlCreator.volgorde" /></b><br>
                <fmt:message key="viewer.wmsurlCreator.volgorde.text" />
                <input type="button" value="/\" onclick="javascript: moveSelectedUp()"/>
                <input type="button" value="\/" onclick="javascript: moveSelectedDown()"/>
                <div id="orderLayerBox" class="orderLayerBox" style="width: 748px;"></div>
            </div>
            <fmt:message key="viewer.wmsurlCreator.persurl" /><BR>
            <html:textarea property="defaultGetMap" styleClass="readOnly" styleId="defaultGetMap" readonly="true" cols="85" rows="5" style="width: 748px;" />
            
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
                        div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
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
                            "collapsed": "../images/treeview/plus.gif",
                            "expanded": "../images/treeview/minus.gif",
                            "leaf": "../images/treeview/leaft.gif"
                        },
                        "saveExpandedState": true,
                        "saveScrollState": true,
                        "expandAll": false
                    });  
                        
                    function setAllTrue(element){
                        setAll(element,true);
                        element.onclick= function(){setAllFalse(this);};
                        element.innerHTML="<fmt:message key="viewer.wmsurlCreator.niets" />";
                    }
                    function setAllFalse(element){
                        setAll(element,false);
                        element.onclick= function(){setAllTrue(this);};
                        element.innerHTML="<fmt:message key="viewer.wmsurlCreator.alles" />";
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
                                checkboxClick(element);
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
                <H1><fmt:message key="viewer.wmsurlCreator.title" /></H1>
                <P>
                    <fmt:message key="viewer.wmsurlCreator.body2" />
                </P>
            </div>
        </c:otherwise>
    </c:choose> 
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>