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

        /* soms is selectedLayers geen array maar zit hier gelijk de selected layer in */
        if (selectedLayers != undefined && selectedLayers.checked) {
            selectedLayersStr += selectedLayers.value+",";
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

function itemClick(item) {
    var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);
    treeview_toggleItemChildren(DOMItemId);
}

getIEVersionNumber = function() {
    var ua = navigator.userAgent;
    var MSIEOffset = ua.indexOf("MSIE ");
    if (MSIEOffset == -1) {
        return -1;
    } else {
        return parseFloat(ua.substring(MSIEOffset + 5, ua.indexOf(";", MSIEOffset)));
    }
}

var ieVersion = getIEVersionNumber();

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
        if (ieVersion <= 7 && ieVersion != -1) {
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
        d.onclick= function(){
            setAllTrue(this);
        };
        d.selecteditem=item;
        d.innerHTML="&nbsp;Alles";
        container.appendChild(d);
    }
}

function setAllTrue(element){
    setAll(element,true);
    element.onclick= function(){
        setAllFalse(this);
    };
    element.innerHTML="&nbsp;Niets";
}
function setAllFalse(element){
    setAll(element,false);
    element.onclick= function(){
        setAllTrue(this);
    };
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