<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="containerdiv" style="height: 500px;">
    <h1>B3P Metadata Editor (metadatastandaard kernset voor geografie 1.1)</h1>
    <div class="mdContainerDiv">
        <div id="tree" class="containerdivFloat"></div>
        
        <iframe frameborder="0" id="metadataIframe" name="metadataIframe" class="mdIframe" width="630" height="450"></iframe>
    </div>	
</div>

<div id="groupDetails" style="margin-top: 10px; height: 10px;" class="containerdiv">
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
            div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
            div.style.height = '18px';
            if(item.type == "layer") {
                var popupLink = document.createElement("a");
                popupLink.innerHTML = item.name;
                popupLink.href='editmetadata.do?edit=submit&id=' + item.id;
                popupLink.target='metadataIframe';
                div.appendChild(popupLink);
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
