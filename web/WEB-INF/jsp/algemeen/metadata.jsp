<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

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
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="containerdiv" style="height: 500px;">
    <h1>B3P Metadata Editor (metadatastandaard kernset voor geografie 1.2)</h1>
    
    <div class="mdContainerDiv">
        <div class="containerdivFloat">
            <p>
                U kunt deze metadata editor gratis gebruiken vanaf deze website.
                Nadat u de gegevens heeft ingevuld kunt u het xml-bestand naar u toe
                mailen.
            </p>
            <p>
                Deze metadata editor is conform metadatastandaard kernset voor geografie 1.2 en
                implementeert het Nederlandse profiel op ISO19115:2003/ ISO19139
            </p>
            <p>
                Een aantal velden hebben een vooringevulde waarde. Ook deze velden
                kunt u aanpassen.
            </p>
            <p>
                Indien u deze editor in uw eigen omgeving wilt toepassen, kunt u 
                contact opnemen met B3Partners BV: 
                <a href="mailto:info@b3partners.nl">info@b3partners.nl</a>
            </p>
        </div>
        <c:set var="link">
            <html:rewrite page="/editmetadata.do?edit=t"/>
        </c:set>
        <iframe src="${link}" frameborder="0" id="metadataIframe" name="metadataIframe" class="mdIframe" width="630" height="450"></iframe>
    </div>	
</div>


