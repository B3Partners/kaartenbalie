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

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<!-- Scripts and settings for the calendar function -->
<html:link href="calendar-brown" title="summer" />

<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white;"></div>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
</script>

<script type="text/javascript">
    var count=0;
    var maxcount=0;
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
            alert('<fmt:message key="beheer.user.ipalert"/>');
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
    
        if (count>maxcount)
            maxcount=count;
    }

    function collectIps(){
        var ipadresses="";
        for(i = 0; i <= maxcount; i++){
            var element=document.getElementById("regip"+i);            
            if(element && element.value.length>0){
                var val=element.value;
                val=val.replace(",",".");
                if (ipadresses.length>0){
                    ipadresses+=",";
                }
                ipadresses+=val;
            }
        }
        if (ipadresses.length>0){
            document.getElementById("registeredIP").value=ipadresses;
        }
    }


</script>

<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/user" onsubmit="collectIps(); return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="registeredIP" styleId="registeredIP"/>

    <div class="containerdiv" style="float: left; clear: none;">
        <H1><fmt:message key="beheer.user.title"/></H1>

        <c:choose>
            <c:when test="${!empty userlist}">
                <c:set var="hoogte" value="${(fn:length(userlist) * 28) + 28}" />
                <c:if test="${hoogte > 400}">
                    <c:set var="hoogte" value="400" />
                </c:if>
                <div class="scroll" style="height: ${hoogte}px; width: 840px;">
                    <table id="server_table" class="tablesorter">
                        <thead>
                            <tr>
                                <th style="width: 24%;" id="sort_col1"><fmt:message key="beheer.userUsername"/></th>
                                <th style="width: 24%;" id="sort_col2"><fmt:message key="beheer.userSurname"/></th>
                                <th style="width: 38%;" id="sort_col3"><fmt:message key="beheer.user.table.rollen"/></th>
                                <th style="width: 15%;" id="sort_col4"><fmt:message key="beheer.user.table.timeout"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nUser" varStatus="status" items="${userlist}">
                                <c:set var="id_selected" value='' />
                                <c:if test="${nUser.id == mainid}"><c:set var="id_selected" value=' id="regel_selected"' /></c:if>
                                <tr onmouseover="showLabel(${nUser.id})" onmouseout="hideLabel(${nUser.id});"${regel_selected}>
                                    <td style="width: 24%;">
                                        <div style="width: 100%; overflow: hidden;">
                                            <html:link page="/user.do?edit=submit&id=${nUser.id}">
                                                <c:out value="${nUser.username}"/>
                                            </html:link>
                                        </div>
                                    </td>
                                    <td style="width: 24%;">
                                        <div style="width: 100%; overflow: hidden;"><c:out value="${nUser.surname}"/></div>
                                    </td>
                                    <td style="width: 38%;">
                                        <div style="width: 100%; overflow: hidden;">
                                            <c:forEach var="nRole" varStatus="status" items="${nUser.roles}">
                                                <c:out value="${nRole.role}" /><c:if test="${!status.last}">,</c:if>
                                            </c:forEach>
                                        </div>
                                    </td>
                                    <td style="width: 15%;">
                                        <div style="width: 100%; overflow: hidden;"><fmt:formatDate pattern="dd-MM-yyyy" value="${nUser.timeout}" /></div>
                                    </td>
                                </tr>
                            <div id="infoLabel${nUser.id}" class="infoLabelClass">
                                <strong><fmt:message key="beheer.userUsername"/>:</strong> ${nUser.username}<br />
                                <strong><fmt:message key="beheer.user.infolabel.naam"/>:</strong> ${nUser.firstName} ${nUser.surname}<br />
                                <strong><fmt:message key="beheer.userEmail"/>:</strong> ${nUser.emailAddress}<br />
                                <strong><fmt:message key="beheer.userOrganization"/>:</strong> ${nUser.mainOrganization.name}<br />
                                <strong><fmt:message key="beheer.user.infolabel.rollen"/>:</strong>
                                <c:forEach var="nRole" varStatus="status" items="${nUser.roles}">
                                    <c:out value="${nRole.role}" /><c:if test="${!status.last}">,</c:if>
                                </c:forEach>
                            </div>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <script type="text/javascript">
                    if(document.getElementById('regel_selected')) {
                        $("#regel_selected").addClass('selected');
                        if(${hoogte} == 400) $(".scroll").scrollTop(($("#regel_selected").position().top - $("#regel_selected").parent().position().top));
                    }
                    $("#server_table").tablesorter({
                        widgets: ['zebra', 'hoverRows', 'fixedHeaders'],
                        sortList: [[0,0]],
                        headers: {
                            3: {
                                sorter:'dutchdates'
                            }
                        },
                        textExtraction: linkExtract
                    });
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.user.geenwmsservices"/>
            </c:otherwise>
        </c:choose>
    </div>

    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 440px;" class="containerdiv">
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
                                            </B>
                                            <input type="button" onClick='addRow(); return false' value="Voeg toe"/>
                                        </td>
                                        <td valign="top">
                                            <div id='ipDiv' class='ipDiv' style="margin: 0px; padding: 0px; margin-left: -3px; float: left;">
                                                <table id='iptable' style="margin: 0px; padding: 0px;">
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>            
                                </table>
                            </td>

                            <td valign="top" style="padding-left: 20px;">
                                <table cellpadding="0px;">
                                    <tr>
                                        <td><B><fmt:message key="beheer.userOrganization"/>:</B></td>
                                        <td>
                                            <html:select property="mainOrganization">
                                                <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                                                    <html:option value="${nOrganization.id}">
                                                        ${nOrganization.name}
                                                    </html:option>
                                                </c:forEach>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <c:forEach var="nOrg" varStatus="status" items="${organizationlist}">
                                        <tr>
                                            <td><html:multibox value="${nOrg.id}" property="orgSelected" /></td>
                                            <td><c:out value="${nOrg.name}" /></td>
                                        </tr>
                                    </c:forEach>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr><td><B><fmt:message key="beheer.userRole"/>:</B></td></tr>
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