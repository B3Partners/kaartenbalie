<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<SCRIPT type=text/javascript>
        <!--
function toggleDiv(divid) {
            var divEl = document.getElementById(divid);
            if(divEl.style.display == 'none') divEl.style.display = 'block';
            else divEl.style.display = 'none';
        }

/* http://www.alistapart.com/articles/zebratables/ */
function removeClassName (elem, className) {
	elem.className = elem.className.replace(className, "").trim();
}

function addCSSClass (elem, className) {
	removeClassName (elem, className);
	elem.className = (elem.className + " " + className).trim();
}

String.prototype.trim = function() {
	return this.replace( /^\s+|\s+$/, "" );
}

function stripedTable() {
	if (document.getElementById && document.getElementsByTagName) {  
		var allTables = document.getElementsByTagName('table');
		if (!allTables) { return; }

		for (var i = 0; i < allTables.length; i++) {
			if (allTables[i].className.match(/[\w\s ]*scrollTable[\w\s ]*/)) {
				var trs = allTables[i].getElementsByTagName("tr");
				for (var j = 0; j < trs.length; j++) {
					removeClassName(trs[j], 'alternateRow');
					addCSSClass(trs[j], 'normalRow');
				}
				for (var k = 0; k < trs.length; k += 2) {
					removeClassName(trs[k], 'normalRow');
					addCSSClass(trs[k], 'alternateRow');
				}
			}
		}
	}
}

window.onload = function() { stripedTable(); }
        -->
        </SCRIPT>

<html:javascript formName="organizationForm" staticJavascript="false"/>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>


<html:form action="/organization" onsubmit="return validateOrganizationForm(this)">
    <c:if test="${not empty message}">
        <div id="error">
            <h3><c:out value="${message}"/></h3>
        </div>
    </c:if>
    
    <div class="containerdiv" style="float: left; clear: none;">
        <b>Groepstabel:</b>
        <div class="serverRijTitel">
            <div style="width: 50px;">Select</div>
            <div style="width: 700px;">Groepsnaam</div>
        </div>
        <div class="tableContainer" id="tableContainer">          
            <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                <div class="serverRij">
                    <div style="width: 50px;"><html:multibox value="${nOrganization.id}" property="organizationSelected" /></div>
                    <div style="width: 700px;"><a href="organization.do?id=${nOrganization.id}"><c:out value="${nOrganization.name}"/></a></div>
                </div>
            </c:forEach>
        </div>
    </div>
    <html:submit value="Delete organization(s)" property="delete" style="margin-top: 5px; margin-bottom: 5px;"/>
    
    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        <table >
            <tr>
                <td width="300">
                    <table>
                        <tr>
                            <td><B>Vestiging ID:</B></td>
                            <td><html:text property="id" readonly="true" /></td>
                        </tr>
                        <tr>
                            <td><B>Naam organisatie:</B></td>
                            <td><html:text property="name"/></td>
                        </tr>
                        <tr>
                            <td><B>Straat:</B></td>
                            <td><html:text property="organizationStreet"/></td>
                        </tr>
                        <tr>
                            <td><B>Nummer:</B></td>
                            <td><html:text property="organizationNumber"/></td>
                        </tr>
                        <tr>
                            <td><B>Toevoeging:</B></td>
                            <td><html:text property="organizationAddition"/></td>
                        </tr>
                        <tr>
                            <td><B>Postcode:</B></td>
                            <td><html:text property="organizationPostalcode"/></td>
                        </tr>
                        <tr>
                            <td><B>Provincie:</B></td>
                            <td><html:text property="organizationProvince"/></td>
                        </tr>
                        <tr>
                            <td><B>Land:</B></td>
                            <td><html:text property="organizationCountry"/></td>
                        </tr>
                        <tr>
                            <td><B>Postbus:</B></td>
                            <td><html:text property="organizationPostbox"/></td>
                        </tr>
                        <tr>
                            <td><B>Factuuradres:</B></td>
                            <td><html:text property="organizationBillingAddress"/></td>
                        </tr>
                        <tr>
                            <td><B>Bezoekadres:</B></td>
                            <td><html:text property="organizationVisitorsAddress"/></td>
                        </tr>
                        <tr>
                            <td><B>Telefoon nr.:</B></td>
                            <td><html:text property="organizationTelephone"/></td>
                        </tr>
                        <tr>
                            <td><B>Fax nr.:</B></td>
                            <td><html:text property="organizationFax" /></td>
                        </tr>
                        
                    </table>
                </td>
                <td valign="top">
                    <b>Layer rechten:</B><br/><br/>
                    <div id="treeContainerSmall">
                        <div class="treeHolderSmall">
                            <div id="tree"></div>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="0"><center>
                        <html:reset  value="Delete values" />
                        <html:submit property="save">
                            <fmt:message key="button.ok"/>
                        </html:submit>
                </center></td>
            </tr>
        </table>
    </div>
    
    <br /><br /><a href="#" onclick="toggleDiv('adminContainer');">Toon/verberg administratieve meldingen</a><br />&nbsp;<br />&nbsp;
    <div id="adminContainer" style="clear: left; display: none;" class="containerdiv">
        <b>Administratieve meldingen</b>
        <DIV class=tableContainer id=tableContainer2 style="top: auto">
            <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
                <THEAD class=fixedHeader>
                    <TR> 
                        <TH><B>Administratieve data</B></TH>
                    </TR>
                </THEAD>
                <TBODY class=scrollContent>
                    <TR><TD></TD></TR>
                </TBODY>
            </TABLE>
        </DIV>
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
                "collapsed": "/kaartenbalie/images/treeview/plus.gif",
                "expanded": "/kaartenbalie/images/treeview/minus.gif",
                "leaf": "/kaartenbalie/images/treeview/leaft.gif"
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