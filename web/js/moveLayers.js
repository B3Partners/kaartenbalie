/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */

var selectedLayer;

function checkboxClick(checkbox){
    var orderLayerBox= document.getElementById("orderLayerBox");
    if (checkbox.checked){
        var div = document.createElement("div");
        div.name=checkbox.id;
        div.className="orderLayerClass";
        div.innerHTML=checkbox.value;
        div.onclick=function(){selectLayer(this);};
        orderLayerBox.appendChild(div);
    }else{
        var div=document.getElementsByName(checkbox.id);
        var orderLayers=orderLayerBox.childNodes;
        for (var i=0; i < orderLayers.length; i++){
            if (orderLayers[i].name==checkbox.id){
                orderLayerBox.removeChild(orderLayers[i]);
            }
        }
    }
}
function selectLayer(element){
    if (selectedLayer){
        selectedLayer.className="orderLayerClass";
    }
    element.className="orderLayerClassSelected";
    selectedLayer=element;
}
function switchLayers(element1,element2){
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
                if (i+amount > -1 && i+amount < orderLayers.length){
                    switchLayers(orderLayers[i+amount],orderLayers[i]);
                    selectedLayer=orderLayers[i+amount];
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