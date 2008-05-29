<%@include file="/WEB-INF/jsp/taglibs.jsp" %>


<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="containerDiv" style=";">
    <h1>Beheer WFS Layer prijzen</h1>
    
    <b>Lijst met beschikbare WFS layers:</b>
    
    <div style="height: 600px; margin: 0px; border: 0px Solid Black; padding: 0px;">
        <div id="tree" style="border: 0px none White; float: left; width: 230px; height: 550px; overflow: auto; margin: 5px;">
        </div>
        <div style="border: 0px Solid Black; float: left; margin: 5px; width: 670px; height: 550px;">
            <iframe name="pricingframe" id="pricingframe" src="wfseditpricing.do?edit=submit&id=" style="width: 100%; height: 100%;" frameborder="0"></iframe>
        </div>    
    </div>
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
            div.className = item.type == "serviceprovider" ? "serviceproviderLabel" : "layerLabel";
            div.style.height = '18px';
            if(item.type != "serviceprovider") {
                if (item.layerName != null && item.layerName != '')
                {
                
                    var popupLink = document.createElement("a");
                    popupLink.innerHTML = item.name;
                    popupLink.href='wfseditpricing.do?edit=submit&id=' + item.id;
                    popupLink.target='pricingframe';
                    div.appendChild(popupLink);
                }  else {
                    div.innerHTML = item.name;
                }
            } else {
                div.appendChild(document.createTextNode(item.name));
            }
            container.appendChild(div);
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
    </c:if>
</script>    
