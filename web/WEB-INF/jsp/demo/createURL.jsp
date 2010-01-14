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
                    alert('<fmt:message key="demo.createUrl.leegveld" />');
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
            alert('<fmt:message key="demo.createUrl.ipadres" />');
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
        document.getElementById("hiddenSaveField").name="save";
        document.getElementById("hiddenSaveField").value="t";
        document.forms[0].submit();
    
    }

</script>

<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/createPersonalURL" onsubmit="return validateUserForm(this)" focus="timeout">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
    <html:hidden property="firstname" />
    <html:hidden property="surname" />
    <html:hidden property="username" />
    <html:hidden property="emailAddress" />
    <html:hidden property="currentAddress" />
    <html:hidden property="organizationName" />
    <html:hidden property="organizationTelephone" />
    
    <html:hidden property="registeredIP" styleId="registeredIP"/>
    <input type="hidden" id="hiddenSaveField"/>
    
    <div class="steps">
        <div class="step"><html:link module="" page="/demo.do"><fmt:message key="demo.steps.start" /></html:link></div>
        <div class="step"><html:link module="/demo" page="/registration.do"><fmt:message key="demo.steps.registeren" /></html:link></div>
        <div class="step"><html:link module="/demo" page="/voegurltoe.do"><fmt:message key="demo.steps.kaarten" /></html:link></div>
        <div class="stepactive"><fmt:message key="demo.steps.persurl" /></div>   
        <div class="step"><fmt:message key="demo.steps.viewer" /></div>   
    </div>
    <div id='democontent'>
        <div id="democontentheader"><fmt:message key="demo.createUrl.title" /></div>
        <fmt:message key="demo.createUrl.body" />
        <c:choose>
            <c:when test="${form.map.personalURL != null}">
                <b><c:out value="${form.map.personalURL}"/></b>
            </c:when>
            <c:otherwise>
                <fmt:message key="demo.createUrl.geenurl" />
            </c:otherwise>
        </c:choose>
        <p>
            <fmt:message key="demo.createUrl.gisviewer" />
        </p>
    </div>
    
    <div id="serverDetails" class="containerdiv" style="clear: left; height: 250px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass">
                    <table>
                        <tr>
                            <td colspan="2">
                                <strong><fmt:message key="demo.createUrl.geldigheidsdatum" /></strong>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="viewer.persoonlijkeurl.timeout"/>:</td>
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
                            <td valign="top">
                                <fmt:message key="viewer.persoonlijkeurl.registeredip"/>:
                            </td>
                            <td valign="top">
                                <div id='ipDiv' class='ipDiv' style="margin: 0px; padding: 0px; margin-left: -3px; float: left;">
                                    <table id='iptable' style="margin: 0px; padding: 0px;">
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                                <input style="float: left;" type="button" onClick='addRow(); return false' value="Voeg IP adres toe"/>
                            </td>
                        </tr>            
                        <tr>
                            <td><fmt:message key="viewer.persoonlijkeurl.createdurl"/>:</td>
                            <td><html:text property="personalURL" styleId="personalURL" styleClass="readOnly" readonly="true" size="100" /></td>
                        </tr>
                    </table>
                    
                    <br /><div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>                    
                        <input type="button" class="knop" onclick="javascript: doCustomSubmit()" name="save" value="<fmt:message key="button.update"/>"/>
                    </div>
                    
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="registeredIP" />
                <html:hidden property="personalURL" />
                <html:hidden property="timeout" />
                <fmt:message key="demo.createUrl.geavanceerd" />
                <html:submit property="edit" accesskey="n" styleClass="knop" onclick="bCancel=true">
                    <fmt:message key="button.edit"/>
                </html:submit>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
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