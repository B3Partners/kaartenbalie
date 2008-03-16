<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<!-- Scripts and settings for the calendar function -->
<html:link href="calendar-brown" title="summer" />

<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
    </script>

<script type="text/javascript">
    var count=0;
    var iplist="${form.map.registeredIP}";
    function addRow(evalue) 
    {        
        if (!document.getElementsByTagName) {
            return;
        }
        
        var tbl = document.getElementById('iptable');
        var elements = document.getElementsByName('regip');        
        if (elements.length>0){
            for(i = 0; i < elements.length; i++) {
                if (elements[i].value.length <= 0) {
                    alert('Veld is leeg');
                    return;
                }
            }
        }
        var tBodiesObj = document.getElementById('iptable').tBodies[0];    
        row = document.createElement("TR");
        cell1 = document.createElement("TD");
        count++;
        var newTextField = document.createElement('input');
        newTextField.type = "text";
        newTextField.id = "regip"+count;
        newTextField.name = "regip";
        
        
        var newButton = document.createElement('img');
        newButton.src="<html:rewrite page='/images/icons/table_delete.gif' module='' />";
        newButton.onclick= new Function("removeRow(this); return false;")
        newButton.style.cursor = 'pointer';
        newButton.alt = 'Remove';
        newButton.style.width = '20px';
        
        cell1.appendChild(newTextField);
        cell1.innerHTML += ' &nbsp; ';
        cell1.appendChild(newButton);
        cell1.vAlign = 'middle';
        row.appendChild(cell1);
        tBodiesObj.appendChild(row);  
        //als object is aangemaakt het eventueel meegegeven ipadres invullen
        if (evalue && evalue.length>0){            
            document.getElementById("regip"+count).value=evalue;
        }
        
        updateDiv(); 
    }
    
    function removeRow(buttonClicked) {
        var parent = buttonClicked;
        while (parent.tagName != 'TR') {
            parent = parent.parentNode;
        }
        
        var tbl = parent.parentNode;
        
        var lastRow = tbl.rows.length;
        if (lastRow > 1) { 
            tbl.removeChild( parent );
            count--;
        } else {
        alert('U dient minimaal een IP adres op te geven!');
    }
    
    updateDiv();
}

function updateDiv() {
    var objDiv = document.getElementById("ipDiv");
    if(count > 4)
        objDiv.style.height = '130px';
    else
        objDiv.style.height = count * 32 + 'px';
    objDiv.scrollTop = objDiv.scrollHeight;
}

function doCustomSubmit(){
    var ipadresses="";
    for(i = 0; i <= count; i++){
        var element=document.getElementById("regip"+i);            
        if(element && element.value.length>0){
            var val=element.value;
            var val=val.replace(",",".");
            if (ipadresses.length>0){
                ipadresses+=",";
            }
            ipadresses+=val;
        }
    }
    if (ipadresses.length>0){
        document.getElementById("registeredIP").value=ipadresses;
    }
    document.getElementById("hiddenSaveField").name="save";
    document.getElementById("hiddenSaveField").value="t";
    document.forms[0].submit();
    
}

</script>

<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/user" onsubmit="return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="personalURL" />
    <html:hidden property="timeout" />
    <html:hidden property="registeredIP" styleId="registeredIP"/>
    <input type="hidden" id="hiddenSaveField"/>
    
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
    
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 440px;" class="containerdiv">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass"> 
                    <table>
                        <tr>
                            <td>
                                <table>
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
                                        <td><B>
                                                <fmt:message key="viewer.persoonlijkeurl.timeout"/>:
                                        </B></td>
                                        <td>
                                            <html:text property="timeout" styleId="cal_date"/> &nbsp;
                                            <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                                                 style="cursor: pointer; border: 1px solid red;" 
                                                 title="Date selector"
                                                 alt="Date selector"
                                                 onmouseover="this.style.background='red';" 
                                                 onmouseout="this.style.background=''"
                                                 onClick="cal.select(document.getElementById('cal_date'),'cal-button','yyyy-MM-dd', document.getElementById('cal_date').value); return false;"
                                                 name="cal-button"
                                                 />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top"><B>
                                                <fmt:message key="viewer.persoonlijkeurl.registeredip"/>:
                                        </B></td>
                                        <td valign="top">
                                            <div id='ipDiv' class='ipDiv' style="margin: 0px; padding: 0px; margin-left: -3px; float: left;">
                                                <table id='iptable' style="margin: 0px; padding: 0px;">
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <input style="float: left;" type="button" onClick='addRow(); return false' value="Voeg toe"/>
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
                        
                        <tr>
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td><b><fmt:message key="viewer.persoonlijkeurl.createdurl"/>:</b></td>
                                    </tr>
                                    <tr>
                                        <td><html:text property="personalURL" styleId="personalURL" styleClass="readOnly" readonly="true" size="115" /></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    
                    <div class="knoppen">
                        <c:if test="${empty mainid}">
                            <input type="button" class="knop" onclick="javascript: doCustomSubmit()" name="save" value="<fmt:message key="button.save"/>"/>
                        </c:if>
                        <c:if test="${not empty mainid}">
                            <input type="button" class="knop" onclick="javascript: doCustomSubmit()" name="save" value="<fmt:message key="button.update"/>"/>
                            <html:submit property="delete" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                <fmt:message key="button.remove"/>
                            </html:submit>
                        </c:if>
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
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
<script type="text/javascript">
   <c:if test="${action != 'list'}">
       if (iplist!=null && iplist.length>0){        
            var tokens=iplist.split(",");        
            for (var b=0;b < tokens.length; b++){            
                addRow(tokens[b]);
            }
        }
   </c:if>
</script>