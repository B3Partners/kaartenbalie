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

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="content_body">
    <h1><fmt:message key="algemeen.metadata.title" /></h1>
    <div class="mdContainerDiv">
        <div class="containerdivFloat">
            <p>
                <fmt:message key="algemeen.metadata.deel1" />
            </p>
            <p>
                <fmt:message key="algemeen.metadata.deel2" />
            </p>
            <p>
                <fmt:message key="algemeen.metadata.deel3" />
            </p>
            <p>
                <fmt:message key="algemeen.metadata.deel4" />
            </p>
        </div>
        <c:set var="link">
            <html:rewrite page="/editmetadata.do?edit=t"/>
        </c:set>
        <iframe src="${link}" frameborder="0" id="metadataIframe" name="metadataIframe" class="mdIframe" width="630" height="450"></iframe>
    </div>	
</div>
