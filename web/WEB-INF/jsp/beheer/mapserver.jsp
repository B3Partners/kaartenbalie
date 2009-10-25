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

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<html:form action="/mapserver" focus="mapFile" enctype="multipart/form-data">

    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beschikbare Mapserver WMS/WFS Services</H1>

        <table style="width: 800px;" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
            <thead>
                <tr class="serverRijTitel" id="topRij">
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:0}); sortTable(this);" width="150"><div>Titel</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:1}); sortTable(this);" width="550"><div>Url</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:2}); sortTable(this);" width="50"><div>WMS</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:3}); sortTable(this);" width="50"><div>WFS</div></td>
                </tr>
            </thead>
        </table>

        <c:set var="hoogte" value="${(fn:length(mapfiles) * 25)}" />
        <c:if test="${hoogte > 400}">
            <c:set var="hoogte" value="400" />
        </c:if>

        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px; width: 820px;">
            <table id="server_table" class="table-autosort table-stripeclass:table_alternate_tr" width="800" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
                <tbody>
                    <c:forEach var="nMapfile" varStatus="status" items="${mapfiles}">
                        <tr class="serverRij" onmouseover="showLabel('${nMapfile["id"]}');" onmouseout="hideLabel('${nMapfile["id"]}');">
                            <td width="150">
                                <div style="width: 150px; overflow: hidden;">
                                    <c:out value='${nMapfile["wms_title"]}'/>
                                </div>
                            </td>
                            <td style="width: 550px;">
                                <div style="width: 550px; overflow: hidden;">
                                    <c:out value='${nMapfile["wms_onlineresource"]}'/>
                                </div>
                            </td>
                            <td style="width: 50px;">
                                <c:choose>
                                    <c:when test='${empty nMapfile["kb_wms"]}'>
                                        <html:link page='/server.do?add=t&url=${nMapfile["encoded_url"]}&givenName=${nMapfile["encoded_title"]}'>
                                            <c:out value='Add'/>
                                        </html:link>
                                    </c:when>
                                    <c:otherwise>
                                        <html:link page='/server.do?edit=t&id=${nMapfile["kb_wms"]}'>
                                            <c:out value='Edit'/>
                                        </html:link>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td style="width: 50px;">
                                <c:choose>
                                    <c:when test='${empty nMapfile["kb_wfs"]}'>
                                        <html:link page='/wfsserver.do?add=t&url=${nMapfile["encoded_url"]}&givenName=${nMapfile["encoded_title"]}'>
                                            <c:out value='Add'/>
                                        </html:link>
                                    </c:when>
                                    <c:otherwise>
                                        <html:link page='/wfsserver.do?edit=t&id=${nMapfile["kb_wfs"]}'>
                                            <c:out value='Edit'/>
                                        </html:link>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    <div id="infoLabel${nMapfile["id"]}" class="infoLabelClass">
                        <strong>ID:</strong> <c:out value='${nMapfile["id"]}'/><br />
                        <strong>SRS:</strong> <c:out value='${nMapfile["wms_srs"]}'/><br />
                        <strong>Bestand:</strong> <c:out value='${nMapfile["map"]}'/><br />
                        <strong>URL:</strong> <c:out value='${nMapfile["wms_onlineresource"]}'/><br />
                    </div>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script type="text/javascript">
        var server_table = document.getElementById('server_table');
        Table.stripe(server_table, 'table_alternate_tr');
        Table.sort(server_table, {sorttype:Sort['alphanumeric'], col:0});
    </script>

    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 500px;" class="containerdiv">
        <div class="serverDetailsClass">
            <table>
                <tr>
                    <td colspan="2">
                        U kunt hier een UMN Mapserver map-bestand uploaden<br/>
                        Indien het bestand een correcte inhoud heeft, wordt het in bovenstaande lijst zichtbaar.
                    </td>
                </tr>
                <tr>
                    <td><B>Map bestand:</B></td>
                    <td><html:file size="80" property="mapFile"/></td>
                </tr>
                <tr>
                    <td><B>Overschrijven:</B></td>
                    <td><html:checkbox property="overwrite"/></td>
                </tr>
            </table>
            <div class="knoppen">
                <html:submit property="save" accesskey="s" styleClass="knop">
                    <fmt:message key="button.save"/>
                </html:submit>
            </div>
        </div>
    </div>


    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>

