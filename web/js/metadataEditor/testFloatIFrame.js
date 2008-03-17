function openMetadataInIFrame(url) {
	var editMetadataRoot = document.getElementById("editMetadataRoot");
	var editMetadataIFrame = document.createElement("iframe");
	editMetadataIFrame.src = url;

	editMetadataIFrame.setAttribute("frameborder", "0");

	editMetadataIFrame.scrolling = "no";
	editMetadataRoot.innerHTML = "";
	editMetadataRoot.appendChild(editMetadataIFrame);
}