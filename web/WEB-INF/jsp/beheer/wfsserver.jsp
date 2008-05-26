<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${wfsServerForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<html:javascript formName="wfsServerForm" staticJavascript="false"/>
<html:form action="/wfsserver" onsubmit="return validateWfsServerForm(this)" focus="givenName">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer WFS Servers</H1>
        
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
                                    <html:link page="/wfsserver.do?edit=submit&id=${nServiceProvider.id}">
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
    
    <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px; height: 150px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass"> 
                    <table>
                        <tr>
                            <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                            <td><html:text property="givenName" size="35" maxlength="20"/></td>
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