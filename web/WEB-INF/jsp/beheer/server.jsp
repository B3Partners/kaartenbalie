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

<c:set var="form" value="${serverForm.map}"/>
<c:set var="action" value="${form.action}"/>
<c:set var="mainid" value="${form.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<html:javascript formName="serverForm" staticJavascript="false"/>

<html:form action="/${form.serverType}server" onsubmit="return validateServerForm(this)" focus="givenName">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="serverType" />

    <div class="containerdiv" style="float: left; clear: none;">
        <c:choose>
            <c:when test="${form.serverType == 'wfs'}">
                <H1>Beheer WFS Servers</H1>
                <a href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.server"/></a><p>
                </c:when>
                <c:otherwise>
                <H1>Beheer WMS Servers</H1>
                <a href="<html:rewrite page='/beheer/wfsserver.do?serverType=wfs' module='' />"><fmt:message key="beheer.wfsserver"/></a><p>
                </c:otherwise>
            </c:choose>
        <table style="width: 740px;" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
            <thead>
                <tr class="serverRijTitel" id="topRij">
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:0}); sortTable(this);" width="350"><div>Naam</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:1}); sortTable(this);" width="250"><div>Afkorting</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['date'], col:2}); sortTable(this);" width="140"><div>Datum laatste update</div></td>
                </tr>
            </thead>
        </table>

        <c:set var="hoogte" value="${(fn:length(serviceproviderlist) * 21)}" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>

        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px; width: 770px;"> 
            <table id="server_table" class="table-autosort table-stripeclass:table_alternate_tr" width="740" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
                <tbody>
                    <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                        <tr class="serverRij" onmouseover="showLabel(${nServiceProvider.id})" onmouseout="hideLabel(${nServiceProvider.id});">
                            <td width="350">
                                <div style="width: 340px; overflow: hidden;">
                                    <html:link page="/${form.serverType}server.do?edit=submit&id=${nServiceProvider.id}&serverType=${form.serverType}">
                                        <c:out value="${nServiceProvider.givenName}"/>
                                    </html:link>
                                </div>
                            </td>
                            <td width="250">
                                <div style="width: 240px; overflow: hidden;"><c:out value="${nServiceProvider.abbr}"/></div>
                            </td>
                            <td width="140">
                                <div style="width: 130px; overflow: hidden;"><fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/></div>
                            </td>
                        </tr>
                    <div id="infoLabel${nServiceProvider.id}" class="infoLabelClass">
                        <strong>Naam:</strong> ${nServiceProvider.givenName}<br />
                        <strong>Afkorting:</strong> ${nServiceProvider.abbr}<br />
                        <strong>URL:</strong> ${nServiceProvider.url}<br />
                        <strong>Datum laatste update:</strong> <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
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

    <div id="mapFiles" class="containerdiv" style="clear: left; padding-top: 15px; height: 150px;">

    </div>

    <script type="text/javascript">
        setOnload(initTreeView);

        var options = {};

        // Copied from Utils.js from M.L.
        function setOnload(func) {
            /* Dit zou eventueel ook kunnen met window.addEventListener/attachEvent,
             * maar dan verschilt natuurlijk de volgorde van meerdere events tussen IE/Firefox
             */

            var oldOnload = window.onload;
            window.onload = function() {
                if(oldOnload) {
                    oldOnload();
                }
                func();
            }
        }
        // \

        function initTreeView() {
            var root = ${mapfiles};
            options.id = "mapFiles";
            options.root = root;
            options.itemLabelCreatorFunction = createLabel,
            options.rootChildrenAsRoots = true,
            options.toggleImages = {
                "collapsed": "<html:rewrite page="/images/treeview/plus.gif" module=""/>",
                "expanded": "<html:rewrite page="/images/treeview/minus.gif" module=""/>",
                "leaf": "<html:rewrite page="/images/treeview/kaart.gif" module=""/>"
            };

            if(options.root.children.length == 0){
                options.containerNode = document.getElementById(options.id);
                options.containerNode.innerHTML = "Niets gevonden";
            }else{
                treeview_create(options);
            }
        }



        function createLabel(container, item) {
            // Label text
            container.className = "node";

            var a = document.createElement("a");
            a.href = "#";
            a.onclick = function() {
                treeItemClick(item);
            };
            a.appendChild(document.createTextNode(item.title));

            container.appendChild(a);
        }

        function treeItemClick(item) {
            if(item.isChild){
                alert( item.title + ' selected');
            }else{
                treeview_expandItemParents(options.id, item.id);
            }
        }
    </script>



    <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px; height: 150px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass"> 
                    <table>
                        <tr>
                            <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                            <td><html:text property="givenName" size="35" maxlength="60"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serviceProviderAbbr"/>:</B></td>
                            <td><html:text property="abbr" size="16" maxlength="10"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                            <td><html:text property="url" size="75" /></td>
                        </tr>
                    </table>

                    <br />
                    <div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <c:choose>
                            <c:when test="${save || delete}">
                                <html:submit property="confirm" accesskey="o" styleClass="knop" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                                    <fmt:message key="button.ok"/>
                                </html:submit>
                            </c:when>
                            <c:when test="${not empty mainid}">
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.update"/>
                                </html:submit>
                                <html:submit property="deleteConfirm" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                    <fmt:message key="button.remove"/>
                                </html:submit>
                            </c:when>
                            <c:otherwise>
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.save"/>
                                </html:submit>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="givenName"/>
                <div class="knoppen">
                    <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.new"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>