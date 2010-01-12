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
<tiles:importAttribute/>

<c:set var="form" value="${pricingForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="id" value="${form.map.id}"/>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<script type="text/javascript">
    function confirmDeletion(lpid, lid) {
        if (confirm("Wilt u deze regel wissen?"))
            location.href="<html:rewrite page='/wfseditpricing.do' module='/beheer' />?delete=t&pricingid=" + lpid + "&id=" + lid;
            return false;
    }
</script>

<c:choose>
    <c:when test="${not empty id}">
    
        <div id="calDiv" style="position:absolute; visibility:hidden; background-color:white;"></div>
        <script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
        <link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
        <script type="text/javascript">
            var cal = new CalendarPopup("calDiv");
            cal.setCssPrefix("calcss_");
        </script>
        
        <div class="tabcollection" id="pricingCollection">
            <div id="tabs">
                <ul id="tabul">
                    <li id="pricing" onclick="displayTabBySource(this);"><a href="#"><fmt:message key="beheer.wfseditpricing.overzicht"/></a></li>
                    <li id="details" onclick="displayTabBySource(this);"><a href="#"><fmt:message key="beheer.wfseditpricing.details"/></a></li>
                    <li id="new"  onclick="displayTabBySource(this);"><a href="#"><fmt:message key="beheer.wfseditpricing.nieuweprijsbepaling"/></a></li>
                </ul>
            </div>
            <script type="text/javascript">Nifty("ul#tabul a","medium transparent top");</script>
            <div id="sheets">
                <div id="pricing" class="sheet">  
                    <label><fmt:message key="beheer.wfseditpricing.serviceprovider"/> :</label> ${spName} <br/>
                    <label style="margin-bottom:10px;"><fmt:message key="beheer.wfseditpricing.kaartlaag"/> :</label> <b>${lName}</b><br/><br />
                    <h1><fmt:message key="beheer.wfseditpricing.title"/></h1>
                    <table id="summaryTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                        <thead>
                            <tr class="serverRijTitel">
                                <th colspan="2">&nbsp;</th>
                                <th colspan="3"><fmt:message key="beheer.wfseditpricing.table.perrequest"/></th>
                            </tr>
                            <tr class="serverRijTitel">
                                <th style="width:40px;"><fmt:message key="beheer.wfseditpricing.table.server"/></th>
                                <th style="width:200px;"><fmt:message key="beheer.wfseditpricing.table.operatie"/></th>
                                <th style="width:60px;"><fmt:message key="beheer.wfseditpricing.table.prijs"/></th>
                                <th style="width:70px;"><fmt:message key="beheer.wfseditpricing.table.calc"/></th>
                                <th style="width:60px;"><fmt:message key="beheer.wfseditpricing.table.via"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="dataRow" items="${tableData}">
                                <tr>
                                    <td style="text-align:left;">
                                        <c:choose>
                                            <c:when test="${dataRow[0] == 'WMS'}"><img src="../images/icons/wms.gif" alt="WMS"></c:when>
                                            <c:when test="${dataRow[0] == 'WFS'}"><img src="../images/icons/wfs.gif" alt="WFS"></c:when>
                                            <c:otherwise><img src="../images/icons/all.gif" alt="All"></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${dataRow[1]}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${dataRow[2].layerIsFree}">
                                                <fmt:message key="beheer.wfseditpricing.table.prijs.gratis"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatNumber value="${dataRow[2].layerPrice}" minFractionDigits="2"  maxFractionDigits="2"/> c
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>&plusmn; ${dataRow[2].calculationTime}ms</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${dataRow[2].method == '-1'}"><img src="../images/icons/blocked.gif" alt="Kaart geblokkeerd."></c:when>
                                            <c:when test="${dataRow[2].method == '0'}"><img src="../images/icons/owner.gif" alt="Prijsinformatie via eigen prijsbepalingen"></c:when>
                                            <c:when test="${dataRow[2].method == '1'}"><img src="../images/icons/parent.gif" alt="WFS"></c:when>
                                            <c:when test="${dataRow[2].method == '2'}"><img src="../images/icons/childs.gif" alt="WFS"></c:when>                                        
                                            <c:otherwise><img src="../images/icons/na.gif" alt="All"></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <script type="text/javascript">
                        Table.stripe(document.getElementById('summaryTable'), 'table_alternate_tr');
                    </script>
                    <p></p>
                    <button onclick="location.href = '<html:rewrite page="/wfspricingtest.do?id=${id}"/>'" module="/beheer"><fmt:message key="beheer.wfseditpricing.proefberekening"/></button>
                </div>
                <div id="details" class="sheet">  
                    <div>
                        <table id="planTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                            <thead>
                                <tr class="serverRijTitel">
                                    <th><fmt:message key="beheer.wfseditpricing.type"/></th>
                                    <th><fmt:message key="beheer.wfseditpricing.servicemethode"/></th>
                                    <th><fmt:message key="beheer.wfseditpricing.geldigvanaf"/></th>
                                    <th><fmt:message key="beheer.wfseditpricing.verloopt"/></th>
                                    <th><fmt:message key="beheer.wfseditpricing.schaalbereik"/></th>
                                    <th><fmt:message key="beheer.wfseditpricing.tarief"/></th>
                                    <th></th>                            
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="layerPricing" items="${layerPricings}">
                                    <c:set var="rowstyle" scope="page" value="${not empty layerPricing.deletionDate ? 'deleted': activePricing.id == layerPricing.id ? 'active':'normal'}"/>
                                    <tr>
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${layerPricing.planType == 1}">
                                                    <fmt:message key="beheer.wfseditpricing.plantype.peropvraag"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="beheer.wfseditpricing.plantype.onbekend"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${layerPricing.service == 'WMS'}"><img src="../images/icons/wms.gif" alt="WMS"></c:when>
                                                <c:when test="${layerPricing.service == 'WFS'}"><img src="../images/icons/wfs.gif" alt="WFS"></c:when>
                                                <c:otherwise><img src="../images/icons/all.gif" alt="All"></c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.operation}">${layerPricing.operation}</c:when>
                                                <c:otherwise><fmt:message key="beheer.wfseditpricing.operatie.alle"/></c:otherwise>
                                            </c:choose>
                                        </td>                                        
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validFrom}">
                                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${layerPricing.validFrom}"/>        
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="beheer.wfseditpricing.geldigvanaf.altijd"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validUntil}">
                                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${layerPricing.validUntil}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:message key="beheer.wfseditpricing.verloopt.nooit"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.projection}">
                                                ${layerPricing.projection} <br/><c:out default="0" value="${layerPricing.minScale}"/> &lt;-&gt; <c:out default="&#8734;" value="${layerPricing.maxScale}" escapeXml="false"/>        
                                                </c:when>
                                                <c:otherwise>n/a</c:otherwise>
                                            </c:choose>
                                            
                                        </td>                                            
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${layerPricing.layerIsFree == true}">
                                                    <fmt:message key="beheer.wfseditpricing.table.prijs.gratis"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${layerPricing.unitPrice}" minFractionDigits="2"  maxFractionDigits="2"/>        
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}">
                                            <c:if test="${empty layerPricing.deletionDate}">
                                                <a href="#" onclick="return confirmDeletion(${layerPricing.id}, ${id});"><img src="../images/icons/page_delete.gif" alt="Delete" style="border: 0px none;"></a>    
                                            </c:if>
                                        </td>                            
                                    </tr>
                                    <div id="infoLabel${layerPricing.id}" class="infoLabelClass">
                                        <strong><fmt:message key="beheer.wfseditpricing.type"/>: </strong><c:choose><c:when test="${layerPricing.planType == 1}">Per Opvraag</c:when><c:otherwise>Onbekend</c:otherwise></c:choose><br />
                                        <strong><fmt:message key="beheer.wfseditpricing.service"/>: </strong>${layerPricing.service}<br />
                                        <strong><fmt:message key="beheer.wfseditpricing.methode"/>: </strong><c:choose><c:when test="${not empty layerPricing.operation}">${layerPricing.operation}</c:when><c:otherwise>Alle</c:otherwise></c:choose><br />
                                        <strong><fmt:message key="beheer.wfseditpricing.aangemaakt"/>: </strong><fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${layerPricing.creationDate}"/><br />
                                        <strong><fmt:message key="beheer.wfseditpricing.verwijderd"/>: </strong><fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${layerPricing.deletionDate}"/>
                                    </div>
                                </c:forEach>
                            </tbody>
                        </table>
                        <script type="text/javascript">
                            Table.stripe(document.getElementById('planTable'), 'table_alternate_tr');
                        </script>
                    </div>
                </div>
                <div id="new" class="sheet">            
                    <html:javascript formName="pricingForm" staticJavascript="false"/>
                    <html:form action="/wfseditpricing" onsubmit="return validatePricingForm(this)" focus="unitPrice">
                        <html:hidden property="action"/>
                        <html:hidden property="alt_action"/>
                        <html:hidden property="id"/>
                        <table>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.type"/> :</label>
                                    <html:select property="planType">
                                        <html:option value="1"><fmt:message key="beheer.wfseditpricing.plantype.peropvraag"/></html:option>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.geldigvanaf"/> :</label><html:text property="validFrom" styleId="validFrom"/>
                                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button1"
                                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                                         title="Date selector"
                                         alt="Date selector"
                                         onmouseover="this.style.background='red';" 
                                         onmouseout="this.style.background=''"
                                         onClick="cal.select(document.getElementById('validFrom'),'cal-button1','yyyy-MM-dd', document.getElementById('validFrom').value); return false;"
                                         name="cal-button1"
                                         />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.geldigtotenmet"/> :</label><html:text styleClass="validUntil" property="validUntil" styleId="validUntil"/>
                                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button2"
                                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                                         title="Date selector"
                                         alt="Date selector"
                                         onmouseover="this.style.background='red';" 
                                         onmouseout="this.style.background=''"
                                         onClick="cal.select(document.getElementById('validUntil'),'cal-button2','yyyy-MM-dd', document.getElementById('validUntil').value); return false;"
                                         name="cal-button2"
                                         />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.tarief"/> :</label>
                                    <html:text styleId="unitPrice" property="unitPrice"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.projectie"/> :</label>
                                    <script type="text/javascript">
                                        function setScaleVisible(state){
                                            var selectScale = document.getElementById('selectScale');
                                            if (state == true) {
                                                selectScale.style.display = "block";
                                            } else {
                                                selectScale.style.display = "none";
                                            }
                                        }
                                    </script>                        
                                    <html:select styleId="" property="projection" onchange="setScaleVisible(this.value != '');">
                                        <html:option value=""><fmt:message key="beheer.wfseditpricing.projectie.unspecified"/></html:option>
                                        <c:forEach var="projectionString" items="${projections}">
                                            <html:option value="${projectionString}" >${projectionString}</html:option>
                                        </c:forEach>
                                    </html:select>
                                    <div id="selectScale" style="display:none; margin-top: 4px;">
                                        <label><fmt:message key="beheer.wfseditpricing.schaalbereik"/> :</label>    
                                        <html:text property="minScale" style="width:100px;"/> van/tot <html:text property="maxScale" style="width:100px;"/><br/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label><fmt:message key="beheer.wfseditpricing.servicemethode"/> :</label> ${pricingForm.map.service} ${pricingForm.map.operationWMS} ${pricingForm.map.operationWFS}
                                </td>
                            </tr>
                        </table>
                        
                        <table style="width:300px;display:on;">
                            <!--<tr>
                                <td style="width:70px;">
                                    <html:radio property="service" onclick="document.getElementById('operationWMS').disabled = true;document.getElementById('operationWFS').disabled = true;" value="GLOBAL">GLOBAL</html:radio>
                                </td>
                            </tr>!-->
                            <!--<tr>
                                <td style="width:70px;">
                                    <html:radio property="service" onclick="document.getElementById('operationWMS').disabled = false;document.getElementById('operationWFS').disabled = true;" value="WMS">WMS</html:radio>
                                </td>
                                <td>
                                    <html:select styleId="operationWMS" property="operationWMS" disabled="${pricingForm.map.service != 'WMS'}">
                                        <html:option value="">Alle</html:option>
                                        <c:forEach var="request" items="${wmsRequests}">
                                            <html:option value="${request}"/>
                                        </c:forEach>
                                    </html:select>
                                </td>
                            </tr>!-->
                            <tr>
                                <td style="width:70px;">
                                    <html:radio disabled="false" onclick="document.getElementById('operationWMS').disabled = true;document.getElementById('operationWFS').disabled = false;" property="service" value="WFS">WFS</html:radio>
                                </td>
                                <td>
                                    <html:select styleId="operationWFS" property="operationWFS" disabled="${pricingForm.map.service != 'WFS'}">
                                        <!--<html:option value="">Alle</html:option>!-->
                                        <c:forEach var="request" items="${wfsRequests}">
                                            <html:option value="${request}"/>
                                        </c:forEach>                            
                                    </html:select> 
                                </td>
                            </tr>
                        </table>
                        <html:submit property="save" styleClass="submit"><fmt:message key="beheer.wfseditpricing.voegtoe"/></html:submit>
                        
                    </html:form>
                </div>
                
            </div>
        </div>
        <script language="JavaScript" type="text/javascript">
            window.onLoad = registerCollection('pricingCollection', 'details');
        </script>
    </c:when>
    <c:otherwise>
        <fmt:message key="beheer.wfseditpricing.selecteerkaartlaag"/>
    </c:otherwise>
    
</c:choose>


