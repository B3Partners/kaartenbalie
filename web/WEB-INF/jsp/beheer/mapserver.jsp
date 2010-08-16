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
        <H1><fmt:message key="beheer.mapserver.title" /></H1>
        
        <c:choose>
            <c:when test="${!empty mapfiles}">
                <c:set var="hoogte" value="${(fn:length(mapfiles) * 28) + 28}" />
                <c:if test="${hoogte > 400}">
                    <c:set var="hoogte" value="400" />
                </c:if>
                <div class="scroll" style="height: ${hoogte}px; width: 840px;">
                    <table id="server_table" class="tablesorter">
                        <thead>
                            <tr>
                                <th style="width: 19%;"><fmt:message key="beheer.mapserver.table.titel" /></th>
                                <th style="width: 69%;"><fmt:message key="beheer.mapserver.table.url" /></th>
                                <th style="width: 6%;"><fmt:message key="beheer.mapserver.table.wms" /></th>
                                <th style="width: 6%;"><fmt:message key="beheer.mapserver.table.wfs" /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nMapfile" varStatus="status" items="${mapfiles}">
                                <c:set var="id_selected" value='' />
                                <c:if test="${nMapfile['id'] == mainid}"><c:set var="id_selected" value='selected' /></c:if>
                                <tr onmouseover="showLabel('${nMapfile["id"]}');" onmouseout="hideLabel('${nMapfile["id"]}');">
                                    <td>
                                        <c:out value='${nMapfile["wms_title"]}'/>
                                        <input type="hidden" name="selected" value="${id_selected}" />
                                    </td>
                                    <td>
                                        <c:out value='${nMapfile["wms_onlineresource"]}'/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test='${empty nMapfile["kb_wms"]}'>
                                                <html:link page='/server.do?add=t&url=${nMapfile["encoded_url"]}&givenName=${nMapfile["encoded_title"]}'>
                                                    <fmt:message key="beheer.mapserver.table.addmapfile" />
                                                </html:link>
                                            </c:when>
                                            <c:otherwise>
                                                <html:link page='/server.do?edit=t&id=${nMapfile["kb_wms"]}'>
                                                    <fmt:message key="beheer.mapserver.table.editmapfile" />
                                                </html:link>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test='${empty nMapfile["kb_wfs"]}'>
                                                <html:link page='/wfsserver.do?add=t&url=${nMapfile["encoded_url"]}&givenName=${nMapfile["encoded_title"]}'>
                                                    <fmt:message key="beheer.mapserver.table.addmapfile" />
                                                </html:link>
                                            </c:when>
                                            <c:otherwise>
                                                <html:link page='/wfsserver.do?edit=t&id=${nMapfile["kb_wfs"]}'>
                                                    <fmt:message key="beheer.mapserver.table.editmapfile" />
                                                </html:link>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:forEach var="nMapfile" varStatus="status" items="${mapfiles}">
                <div id="infoLabel${nMapfile["id"]}" class="infoLabelClass">
                    <strong><fmt:message key="beheer.mapserver.infolabel.id" />:</strong> <c:out value='${nMapfile["id"]}'/><br />
                    <strong><fmt:message key="beheer.mapserver.infolabel.srs" />:</strong> <c:out value='${nMapfile["wms_srs"]}'/><br />
                    <strong><fmt:message key="beheer.mapserver.infolabel.bestand" />:</strong> <c:out value='${nMapfile["map"]}'/><br />
                    <strong><fmt:message key="beheer.mapserver.table.url" />:</strong> <c:out value='${nMapfile["wms_onlineresource"]}'/><br />
                </div>
                </c:forEach>
                <script type="text/javascript">
                    tablepager(
                        'server_table',
                        '930',
                        '14'
                    );
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.mapserver.geenbeschikbaar" />
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 500px;" class="containerdiv">
        <div class="serverDetailsClass">
            <table>
                <tr>
                    <td colspan="2">
                        <fmt:message key="beheer.mapserver.uploaden" />
                    </td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.mapserver.mapbestand" />:</B></td>
                    <td><html:file size="80" property="mapFile"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.mapserver.overschrijven" />:</B></td>
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