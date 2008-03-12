<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

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
        document.getElementById("hiddenSaveField").value="s";
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
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Persoonlijke URL</H1>
        <P>
            Op deze pagina kunt u:
            <ul>
                <li>een reeds aangemaakt persoonlijke URL opvragen of </li>
                <li>deze persoonlijke URL wijzigen of</li>
                <li>u kunt een nieuwe URL maken, indien u er nog geen een aangemaakt had.</li>
            </ul>
        </P>        
        <p>
            <strong>LET OP: uw persoonlijke url verandert als u geldigheidsdatum aanpast.</strong>
        </p>
        <P>
            De URL stelt u in staat om op eenvoudige wijze met andere viewers 
            dan de intern meegeleverde viewer gebruik te maken van de Kaartenbalie. 
            Hieronder vindt u de huidige gegevens zoals deze in de applicatie bekend zijn. 
        </P>        
        <P>
            Via volgende link kunt u de voor u geldende WMS GetCapabilities ophalen:
            <html:link page="/wms/?REQUEST=GetCapabilities&VERSION=1.1.1&SERVICE=WMS" module="" target="_blank">GetCapabilities</html:link>
        </P>        
        
        <H2>Huidige gegevens:</H2>
        <table>
            <tr>
                <td>Naam:</td>
                <td><c:out value="${form.map.firstname}"/>&nbsp;<c:out value="${form.map.surname}"/></td>
            </tr>
            <tr>
                <td>Gebruikersnaam:</td>
                <td><c:out value="${form.map.username}"/></td>
            </tr>
            <tr>
                <td>Email adres:</td>
                <td><c:out value="${form.map.emailAddress}"/></td>
            </tr>
            
            <c:choose>
                <c:when test="${form.map.personalURL != null}">
                    <tr>
                        <td>Pers. URL:</td>
                        <td><c:out value="${form.map.personalURL}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td>Pers. URL:</td>
                        <td>Nog geen URL aangemaakt.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            
            <tr>
                <td>Huidig IP adres:</td>
                <td><c:out value="${form.map.currentAddress}"/></td>
            </tr>
        </table>
    </div>
    
    <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px; height: 250px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass">
                    <html:hidden property="registeredIP" styleId="registeredIP"/>
                    <input type="hidden" id="hiddenSaveField"/>
                    <table>
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
                                IP adres:
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
                <div class="knoppen">
                    <html:submit property="edit" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.edit"/>
                    </html:submit>
                </div>
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