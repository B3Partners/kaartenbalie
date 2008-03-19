<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">
<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>

<c:choose>
    <c:when test="${not empty id}">
        
        <div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
        <script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
        <link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
        <script type="text/javascript">
            var cal = new CalendarPopup("calDiv");
            cal.setCssPrefix("calcss_");
        </script>
        
        <div class="tabcollection" id="pricingCollection">
            <div id="tabs">
                <ul id="tabul">
                    <li id="pricing" onclick="displayTabBySource(this);"><a href="#">Index</a></li>
                    <li id="details" onclick="displayTabBySource(this);"><a href="#">Details</a></li>
                    <li id="new"  onclick="displayTabBySource(this);"><a href="#">Nieuwe Prijsbepaling</a></li>
                </ul>
            </div>
            <script type="text/javascript">Nifty("ul#tabul a","medium transparent top");</script>
            <div id="sheets">
                <div id="pricing" class="sheet">  
                    <label>Service Provider :</label> ${spName} <br/>
                    <label style="margin-bottom:10px;">Kaartlaag :</label> <b>${lName}</b><br/><br />
                    <h1>Samenvatting</h1>
                    <c:if test="${summary == true}">
                        <table id="summaryTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                            <thead>
                                <tr class="serverRijTitel">
                                    <th colspan="2">&nbsp;</th>
                                    <th colspan="3">Per Request</th>
                                </tr>
                                <tr class="serverRijTitel">
                                    <th style="width:40px;">Serv.</th>
                                    <th style="width:200px;">Operation/Methode</th>
                                    <th style="width:60px;">Prijs</th>
                                    <th style="width:70px;">tCalc</th>
                                    <th style="width:60px;">Via</th>
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
                                                    Gratis
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
                    </c:if>
                    <p>
                        <input type="checkbox" onchange="location.href='editpricing.do?id=${id}&summary=' + this.checked;" ${summary == true ? 'checked':''}>Samenvatting ophalen</input>
                    </p>
                    <button onclick="location.href = '<html:rewrite page="/pricingtestcalc.do?test=submit&id=${id}"/>'">Proefberekening Maken</button>
                </div>
                <div id="details" class="sheet">  
                    <div>
                        <table id="planTable" style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
                            <thead>
                                <tr class="serverRijTitel">
                                    <th>Type</th>
                                    <th>Service / Methode</th>
                                    <th>Geldig vanaf</th>
                                    <th>Verloopt</th>
                                    <th>Schaalbereik</th>
                                    <th>Tarief</th>
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
                                                    Per Opvraag
                                                </c:when>
                                                <c:otherwise>
                                                    Onbekend
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
                                                <c:otherwise>Alle</c:otherwise>
                                            </c:choose>
                                        </td>                                        
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validFrom}">
                                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${layerPricing.validFrom}"/>        
                                                </c:when>
                                                <c:otherwise>
                                                    Altijd
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}" onmouseover="showLabel(${layerPricing.id})" onmouseout="hideLabel(${layerPricing.id});">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validUntil}">
                                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${layerPricing.validUntil}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    Nooit
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
                                                    Gratis
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${layerPricing.unitPrice}" minFractionDigits="2"  maxFractionDigits="2"/>        
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}">
                                            <c:if test="${empty layerPricing.deletionDate}">
                                                <a href="editpricing.do?delete=submit&pricingid=${layerPricing.id}&id=${id}"><img src="../images/icons/page_delete.gif" alt="Delete" style="border: 0px none;"></a>    
                                            </c:if>
                                        </td>                            
                                    </tr>
                                    <div id="infoLabel${layerPricing.id}" class="infoLabelClass">
                                        <strong>Type: </strong><c:choose><c:when test="${layerPricing.planType == 1}">Per Opvraag</c:when><c:otherwise>Onbekend</c:otherwise></c:choose><br />
                                        <strong>Service: </strong>${layerPricing.service}<br />
                                        <strong>Methode: </strong><c:choose><c:when test="${not empty layerPricing.operation}">${layerPricing.operation}</c:when><c:otherwise>Alle</c:otherwise></c:choose><br />
                                        <strong>Aangemaakt: </strong><fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${layerPricing.creationDate}"/><br />
                                        <strong>Verwijderd: </strong><fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${layerPricing.deletionDate}"/>
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
                    <html:form action="/editpricing.do?id=${id}" onsubmit="return validateReportingForm(this)">
                        <table>
                            <tr>
                                <td>
                                    <label>Type :</label>
                                    <html:select property="planType">
                                        <html:option value="1">Per Opvraag</html:option>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Geldig vanaf :</label><html:text styleClass="validFrom" property="validFrom" styleId="validFrom"/>
                                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                                         title="Date selector"
                                         alt="Date selector"
                                         onmouseover="this.style.background='red';" 
                                         onmouseout="this.style.background=''"
                                         onClick="cal.select(document.getElementById('validFrom'),'cal-button','yyyy-MM-dd', document.getElementById('validFrom').value); return false;"
                                         name="cal-button"
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Geldig tot en met :</label><html:text styleClass="validUntil" property="validUntil" styleId="validUntil"/>
                                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                                         title="Date selector"
                                         alt="Date selector"
                                         onmouseover="this.style.background='red';" 
                                         onmouseout="this.style.background=''"
                                         onClick="cal.select(document.getElementById('validUntil'),'cal-button','yyyy-MM-dd', document.getElementById('validUntil').value); return false;"
                                         name="cal-button"
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Tarief :</label>
                                    <html:text styleId="unitPrice" property="unitPrice"/>
                                    <html:checkbox styleId="layerIsFree" property="layerIsFree" onclick="unitPriceState(this.checked);"/> Kaart is gratis!
                                    <script type="text/javascript">
                                           function unitPriceState(state){
                                                var unitPrice = document.getElementById('unitPrice');
                                                unitPrice.disabled = state;
                                                if (state == true) {
                                                    unitPrice.value = '';
                                                }
                                            }
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Projectie :</label>
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
                                        <html:option value="">Unspecified</html:option>
                                        <c:forEach var="projectionString" items="${projections}">
                                            <html:option value="${projectionString}" >${projectionString}</html:option>
                                        </c:forEach>
                                    </html:select>
                                    <div id="selectScale" style="display:none; margin-top: 4px;">
                                        <label>Schaalbereik :</label>    
                                        <html:text property="minScale" style="width:100px;"/> van/tot <html:text property="maxScale" style="width:100px;"/><br/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Service & Methode :</label> ${pricingForm.map.service} ${pricingForm.map.operationWMS}
                                </td>
                            </tr>
                        </table>
                                               
                        <table style="width:300px;display:none;">
                            <tr>
                                <td style="width:70px;">
                                    <html:radio property="service" onclick="document.getElementById('operationWMS').disabled = true;document.getElementById('operationWFS').disabled = true;" value="GLOBAL">GLOBAL</html:radio>
                                </td>
                            </tr>
                            <tr>
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
                            </tr>
                            <tr>
                                <td style="width:70px;">
                                    <html:radio disabled="true" onclick="document.getElementById('operationWMS').disabled = true;document.getElementById('operationWFS').disabled = false;" property="service" value="WFS">WFS</html:radio>
                                </td>
                                <td>
                                    <html:select styleId="operationWFS" property="operationWFS" disabled="${pricingForm.map.service != 'WFS'}">
                                        <html:option value="">Alle</html:option>
                                        <c:forEach var="request" items="${wfsRequests}">
                                            <html:option value="${request}"/>
                                        </c:forEach>                            
                                    </html:select> 
                                </td>
                            </tr>
                        </table>
                        <html:submit property="save" styleClass="submit">Voeg toe</html:submit>
                        
                    </html:form>
                </div>
                
            </div>
        </div>
        <script language="JavaScript" type="text/javascript">
            window.onLoad = registerCollection('pricingCollection', 'details', '${gotoTab}');
        </script>
    </c:when>
    <c:otherwise>
        Selecteer een kaartlaag
    </c:otherwise>
    
</c:choose>


