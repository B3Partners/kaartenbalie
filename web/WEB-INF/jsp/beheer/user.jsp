<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>


<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/user" onsubmit="return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="personalURL" />
    <html:hidden property="registeredIP" />
    <html:hidden property="timeout" />
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer Gebruikers</H1>
        
        <table style="width: 740px;" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
            <thead>
                <tr class="serverRijTitel" id="topRij">
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:0}); sortTable(this);" width="160"><div>Gebruikersnaam</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:1}); sortTable(this);" width="160"><div>Voornaam</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:2}); sortTable(this);" width="160"><div>Achternaam</div></td>
                    <td class="serverRijTitel table-sortable" onclick="Table.sort(server_table, {sorttype:Sort['ignorecase'], col:3}); sortTable(this);" width="160"><div>E-mailadres</div></td>
                </tr>
            </thead>
        </table>
        
        <c:set var="hoogte" value="${(fn:length(userlist) * 21)}" />
        <c:if test="${hoogte > 230}">
            <c:set var="hoogte" value="230" />
        </c:if>
        
        <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px; width: 770px;">     
            <table id="server_table" class="table-autosort table-stripeclass:table_alternate_tr" width="740" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
                <tbody>
                    <c:forEach var="nUser" varStatus="status" items="${userlist}">
                        <tr class="serverRij" onmouseover="showLabel(${nUser.id})" onmouseout="hideLabel(${nUser.id});">
                            <td width="160">
                                <div style="width: 150px; overflow: hidden;">
                                    <html:link page="/user.do?edit=submit&id=${nUser.id}">
                                        <c:out value="${nUser.username}"/>
                                    </html:link>
                                </div>
                            </td>
                            <td width="160">
                                <div style="width: 150px; overflow: hidden;"><c:out value="${nUser.firstName}"/></div>
                            </td>
                            <td width="160">
                                <div style="width: 150px; overflow: hidden;"><c:out value="${nUser.surname}"/></div>
                            </td>
                            <td width="160">
                                <div style="width: 150px; overflow: hidden;"><c:out value="${nUser.emailAddress}"/></div>
                            </td>
                        </tr>
                        <div id="infoLabel${nUser.id}" class="infoLabelClass">
                            <strong>Gebruikersnaam:</strong> ${nUser.username}<br />
                            <strong>Naam:</strong> ${nUser.firstName} ${nUser.surname}<br />
                            <strong>E-mailadres:</strong> ${nUser.emailAddress}<br />
                            <strong>Organisatie:</strong> ${nUser.organization.name}<br />
                            <strong>Rollen:</strong> 
                            <c:forEach var="nRole" varStatus="status" items="${nUser.userroles}">
                                <c:out value="${nRole.role}" /><c:if test="${!status.last}">,</c:if>
                            </c:forEach>
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
    
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 250px;" class="containerdiv">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass"> 
                    <table>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userFirstname"/>:</B></td>
                                        <td><html:text property="firstname"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userSurname"/>:</B></td>
                                        <td><html:text property="surname"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userEmail"/>:</B></td>
                                        <td><html:text property="emailAddress"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userUsername"/>:</B></td>
                                        <td><html:text property="username"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userPassword"/>:</B></td>
                                        <td><html:password property="password"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.repeatpassword"/>:</B></td>
                                        <td><html:password property="repeatpassword"/></td>
                                    </tr>
                                    <tr>
                                        <td><B><fmt:message key="beheer.userOrganization"/>:<B></td>
                                        <td>
                                            <html:select property="selectedOrganization">
                                                <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                                                    <html:option value="${nOrganization.id}">
                                                        ${nOrganization.name}
                                                    </html:option>
                                                </c:forEach>
                                            </html:select>     
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            
                            
                            <td valign="top" style="padding-left: 20px;">
                                <table cellpadding="0px;">
                                    <B><fmt:message key="beheer.userRole"/>:</B>
                                    <c:forEach var="nRole" varStatus="status" items="${userrolelist}">
                                        <tr>
                                            <td><html:multibox value="${nRole.id}" property="roleSelected" /></td>
                                            <td><c:out value="${nRole.role}" /></td>
                                        </tr>
                                    </c:forEach>
                                    
                                </table>
                            </td>
                        </tr>
                    </table>
                    
                    <div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <c:if test="${empty mainid}">
                            <html:submit property="save" accesskey="s" styleClass="knop">
                                <fmt:message key="button.save"/>
                            </html:submit>
                        </c:if>
                        <c:if test="${not empty mainid}">
                            <html:submit property="save" accesskey="s" styleClass="knop">
                                <fmt:message key="button.update"/>
                            </html:submit>
                            <html:submit property="delete" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                <fmt:message key="button.remove"/>
                            </html:submit>
                        </c:if>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="firstname"/>
                <div class="knoppen">
                    <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.new"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</html:form>