/*
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Metadata Editor.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
*/
// 3/8/07 Erik van de Pol

// Loosely based on "generic_dhtml.vbs" by Eric Compas (2/3/05).
// Rewritten in javascript and working in most W3C compliant browsers.


// some global settings

// section highlighting - border && background shading 
// colors and flags (set to false to not use)
var SHADE_COLOR = "#FFFBDF";
var USE_SHADE = false;
var BORDER_COLOR = "#ccc";
var USE_BORDER = true;

// menu image offset (distance away from image that's
// clicked on to show menu)
var MENU_X_OFFSET = 3;
var MENU_Y_OFFSET = 3;

var openedMenuNode = null;


// Show popup window that's embedded in code
function showMenu(event) {
	var element = getTarget(event);
	element = element.parentNode;
	//debug("showMenu");

	while (element.tagName.toLowerCase() != "span" && element.tagName.toLowerCase() != "html")
		element = element.parentNode;
	
	//current value
	var list;
	var listFound = false;
	for (var i = 0; i < element.childNodes.length; i++) {		
		list = element.childNodes[i];
		if (list.nodeType == Node.ELEMENT_NODE && list.tagName.toLowerCase() == "ul") {
			listFound = true;
			break;
		}
	}

	if (!listFound) {
		alert("Cannot locate menu to display.");
		return;
	}

	// get image to use for positioning
	var image;
	var imageFound = false;
	for (var i = 0; i < element.childNodes.length; i++) {		
		image = element.childNodes[i];
		if (image.nodeType == Node.ELEMENT_NODE && image.tagName.toLowerCase() == "img") {
			imageFound = true;
			break;
		}
	}

	if (!imageFound) {
		alert("Cannot locate menu image.");
		return;
	}
	
	// show/hide menu
	if (isMenuShowing(list))
		hideMenu(list);
	else
		_showMenu(list);
	
	stopPropagation(event);
}

function _showMenu(element) {
	//debug("show");
	hideMenu(openedMenuNode);
	element.style.display = "inline";
	openedMenuNode = element;
}

function hideMenu(element) {
	//debug("hide");
	if (element != null)
		element.style.display = "none";
	openedMenuNode = null;
}

function isMenuShowing(element) {
	//debug("element.style.display: " + element.style.display);
	return element.style.display !== "none";
}

// Description:
//   Expand/collapse the contents of the specified folder (div tag)
//   Assumes:
//  1) that script is called by A tag
//  2) that DIV to be shown or hidden is first DIV child of 
//     anchor's parent
//  3) that plus and minus images (if images are used) are
//     defined somewhere in the document with id= "minus_img"
//     and "plus_img"
//
// Arguments:
//   pElem = reference to the object calling the routine
function expandNode(pElem) {
	// get elements (anchor and child div)
	var pFolderAnchor = pElem.parentNode.getElementsByTagName("a")[0];
	var pChildDiv = pElem.parentNode.parentNode.getElementsByTagName("div")[1];
	if (pFolderAnchor == null || pChildDiv == null) {
		// error encountered;
		alert("Error encountered expanding/collapsing this section.");
		return;
	}

	// switch folder content display or hide
	var sDisplay = pChildDiv.style.display;
	if (sDisplay == "none") {
		pChildDiv.style.display = "block";
	}
	else {
		pChildDiv.style.display = "none";
	}

	// switch plus/minus text or plus/minus images

	// get first img child (must be + or - image)
	var pCurrentIMG;
	//for (var childIndex in pFolderAnchor.childNodes) {
	for (var i = 0; i < pFolderAnchor.childNodes.length; i++) {
		pCurrentIMG = pFolderAnchor.childNodes[i];
		if (pCurrentIMG.nodeType == 1 && pCurrentIMG.tagName.toLowerCase() == "img") {
			// switch images
			var pNewIMG;			
			if (sDisplay == "none") {
				pNewIMG = document.getElementById("minus_img").cloneNode(true);
			}
			else {
				pNewIMG = document.getElementById("plus_img").cloneNode(true);
			}

			if (pNewIMG == null) {
				alert("No image to switch found");
				return;
			}
			pFolderAnchor.replaceChild(pNewIMG, pCurrentIMG);
			break;
		}
	}

}


// Description:
//   Expand/collapse all the folder (div tags) in the current document.
//   Expands/collapses div tags with class="folder"
//
// Arguments:
//   expand = true, expands all
//          = false, collapses all
function expandAll(expand) {
	// get all div elements in document and loop through them
	var pDIVElements = document.getElementsByTagName("div");

	for (var childIndex in pDIVElements) {
		var pElem = pDIVElements[childIndex];
		if (pElem.className == "folder") {
			// current div element's anchor and content div
			pFolderAnchor = pElem.getElementsByTagName("a")[0];
			pChildDiv = pElem.getElementsByTagName("div")[0];
			if (pFolderAnchor != null && pChildDiv != null) {
				// switch folder content display or hide
				if (expand)
					pChildDiv.style.display = "block";
				else
					pChildDiv.style.display = "none";

				// switch plus/minus text or plus/minus images

				// image first child?
				if (pFolderAnchor.childNodes[0].tagName == "IMG") {
					pCurrentIMG = pFolderAnchor.getElementsByTagName("img")[0];
					
					// switch images
					if (expand)
						pNewIMG = document.getElementById("minus_img").cloneNode(true);
					else
						pNewIMG = document.getElementById("plus_img").cloneNode(true);

					pFolderAnchor.replaceChild(pNewIMG, pCurrentIMG);
				}
				else {
					// switch text
					if (expand)
						sSymbol = "-";
					else
						sSymbol = "+";

					pFolderAnchor.childNodes[0].innerText = sSymbol;
				}
			}
			else {
				// error encountered - skip this folder
			}
		}
	}
}