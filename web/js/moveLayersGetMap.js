//same as the moveLayers.js but with a extra hidden field for sending the order to the action.
var selectedLayer;
function checkboxClick(checkbox){
    var orderLayerBox= document.getElementById("orderLayerBox");
    if (checkbox.checked){
        var div = document.createElement("div");
        div.name=checkbox.id;
        div.className="orderLayerClass";
        div.innerHTML=checkbox.value;
        div.onclick=function(){selectLayer(this);};
        
        //hidden
        var hidden = document.createElement("input");
        hidden.type="hidden";
        hidden.name="sortedLayers";
        hidden.value=checkbox.value;

        orderLayerBox.appendChild(hidden);
        orderLayerBox.appendChild(div);
    }else{
        var div=document.getElementsByName(checkbox.id);
        var orderLayers=orderLayerBox.childNodes;
        for (var i=0; i < orderLayers.length; i++){
            if (orderLayers[i].name==checkbox.id){
                orderLayerBox.removeChild(orderLayers[i]);                
            }
        }

        //hidden
        var hiddenFields=document.getElementsByName("sortedLayers");
        for (var i=0; i < hiddenFields.length; i++){
            if (hiddenFields[i].value==checkbox.value){
                orderLayerBox.removeChild(hiddenFields[i]);
                
            }
        }
    }
}
/*
function createHidden(checkbox){
    var orderLayerBox= document.getElementById("orderLayerBox");
    if (checkbox.checked){
        var hidden = document.createElement("input");
        hidden.type="hidden";
        hidden.name="sortedLayers";
        hidden.value=checkbox.value;
        orderLayerBox.appendChild(hidden);
    }else{
        var hiddenFields=document.getElementsByName("sortedLayers");
        for (var i=0; i < hiddenFields.length; i++){
            if (hiddenFields[i].value==checkbox.value){
                orderLayerBox.removeChild(orderLayers[i]);
            }
        }
    }

} */
function selectLayer(element){
    if (selectedLayer){
        selectedLayer.className="orderLayerClass";
    }
    element.className="orderLayerClassSelected";
    selectedLayer=element;
}

function switchLayers(element1,element2){
    //switch the hidden    
    var hiddenFields=document.getElementsByName("sortedLayers");
    var hiddenElement1;
    var hiddenElement2;    
    for (var i=0; i < hiddenFields.length; i++){
        if (hiddenFields[i].value==element1.innerHTML){            
            hiddenElement1=hiddenFields[i];
        }
        if (hiddenFields[i].value==element2.innerHTML){
            hiddenElement2=hiddenFields[i];
        }
    }
    //do the hidden switch
    var hiddenElementHelp=hiddenElement1.value;
    hiddenElement1.value=hiddenElement2.value;
    hiddenElement2.value=hiddenElementHelp;

    var name=element1.name;
    var className=element1.className;
    var innerHTML=element1.innerHTML;

    element1.name=element2.name;
    element1.className=element2.className;
    element1.innerHTML=element2.innerHTML;

    element2.name=name;
    element2.className=className;
    element2.innerHTML=innerHTML;


}
function moveSelected(amount){
    if (selectedLayer){
        var orderLayerBox= document.getElementById("orderLayerBox");
        var orderLayers=orderLayerBox.childNodes;
        for (var i=0; i < orderLayers.length; i++){
            if (orderLayers[i].name==selectedLayer.name){
                if (i+amount*2 > -1 && i+amount*2 < orderLayers.length){
                    switchLayers(orderLayers[i+amount*2],orderLayers[i]);
                    selectedLayer=orderLayers[i+amount*2];
                    return;
                }
            }
        }      
    }else{
        alert("Selecteer eerst een kaart om te verplaatsen");
    }
}
function moveSelectedDown(){
    moveSelected(1);
}
function moveSelectedUp(){
    moveSelected(-1);
}
//rewrite the getElementsByName function. The function is buggy for ie
document.getElementsByName=function(str){
    str=new String(str);
    var myMatches=new Array();
    var allEls=document.getElementsByTagName("*"),l=allEls.length;
    for(var i=0;i<l;i++)if(allEls[i].name==str || allEls[i].getAttribute("name")==str)myMatches[myMatches.length]=allEls[i];
    return myMatches;
} 