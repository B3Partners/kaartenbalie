<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>


<c:choose>
    <c:when test="${not empty id}">
        <jsp:include page="/WEB-INF/jsp/inc_calendar.jsp" flush="true"/>
        <div class="tabcollection" id="pricingCollection">
            <div id="tabs">
                <ul>
                    <li id="pricing" onclick="displayTabBySource(this);">Index</li>
                    <li id="details" onclick="displayTabBySource(this);">Details</li>
                    <li id="new"  onclick="displayTabBySource(this);">Nieuwe Prijsbepaling</li>
                </ul>
            </div>
            <div id="sheets" style="height:450px;">
                <div id="pricing" class="sheet">  
                    <label>Service Provider :</label> ${spName} <br/>
                    <label style="margin-bottom:10px;">Kaartlaag :</label> <b>${lName}</b><br/>
                    <h1>Samenvatting</h1>
                    <c:if test="${summary == true}">
                        <table style="padding:0px;margin:0px;border-collapse: collapse;border:1px Solid Black;" class="">
                            <thead>
                                <tr>
                                    <th colspan="2">&nbsp;</th>
                                    <th colspan="3" style="border-left: 1px Solid Black; border-right: 1px Solid Black;">Per Request</th>
                                </tr>
                                <tr>
                                    <th style="width:40px;">Serv.</th>
                                    <th style="width:200px;">Operation/Methode</th>
                                    <th style="border-left: 1px Solid Black;width:60px;">Prijs</th>
                                    <th style="width:70px;">tCalc</th>
                                    <th style="border-right: 1px Solid Black;width:60px;">Via</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="dataRow" items="${tableData}">
                                    <tr>
                                        <td style="text-align:right;">
                                            <c:choose>
                                                <c:when test="${dataRow[0] == 'WMS'}"><img src="../images/icons/wms.gif" alt="WMS"></c:when>
                                                <c:when test="${dataRow[0] == 'WFS'}"><img src="../images/icons/wfs.gif" alt="WFS"></c:when>
                                                <c:otherwise><img src="../images/icons/all.gif" alt="All"></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${dataRow[1]}</td>
                                        <td style="border-left: 1px Solid Black;">
                                            <c:choose>
                                                <c:when test="${dataRow[2].layerIsFree}">
                                                    Gratis
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${dataRow[2].layerPrice}" minFractionDigits="2"  maxFractionDigits="2"/> c
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>± ${dataRow[2].calculationTime}ms</td>
                                        <td style="border-right: 1px Solid Black;">
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
                    </c:if>
                    <p>
                        <input type="checkbox" onchange="location.href='editpricing.do?id=${id}&summary=' + this.checked;" ${summary == true ? 'checked':''}>Samenvatting ophalen</input>
                    </p>
                    <button onclick="parent.showPopup(1000,700,'Transactie Details','pricingtestcalc.do?test=submit&id=${id}');">Proefberekening Maken</button>
                </div>
                <div id="details" class="sheet" style="height:450px;">  
                    <div style="background-color:white;border:1px Solid Black;">
                        <table style="width:100%;padding:0px;margin:0px;border-collapse: collapse;" class="pricingTable">
                            <tr>
                                <thead>
                                    <th>planType</th>
                                    <th>Service/Methode</th>
                                    <th>Aangemaakt/Verwijderd</th>
                                    <th>Geldig vanaf</th>
                                    <th>Verloopt</th>
                                    <th>Schaalbereik</th>
                                    <th>Tarief</th>
                                    <th>[d]</th>                            
                                </thead>
                            </tr>
                            
                            <c:forEach var="layerPricing" items="${layerPricings}">
                                <c:set var="rowstyle" scope="page" value="${not empty layerPricing.deletionDate ? 'deleted': activePricing.id == layerPricing.id ? 'active':'normal'}"/>
                                <tr>
                                    <tbody>
                                        <td class="${rowstyle}">
                                            <c:choose>
                                                <c:when test="${layerPricing.planType == 1}">
                                                    Per Opvraag
                                                </c:when>
                                                <c:otherwise>
                                                    Onbekend
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}">
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
                                        <td class="${rowstyle}">
                                            <fmt:formatDate pattern="yyyy-MM-dd @ HH:mm:ss" value="${layerPricing.creationDate}"/><br/>
                                            <fmt:formatDate pattern="yyyy-MM-dd @ HH:mm:ss" value="${layerPricing.deletionDate}"/>                                              
                                        </td>
                                        <td class="${rowstyle}">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validFrom}">
                                                    <fmt:formatDate pattern="yyyy-MM-dd" value="${layerPricing.validFrom}"/>        
                                                </c:when>
                                                <c:otherwise>
                                                    Altijd
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.validUntil}">
                                                    <fmt:formatDate pattern="yyyy-MM-dd" value="${layerPricing.validUntil}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    Nooit
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="${rowstyle}">
                                            <c:choose>
                                                <c:when test="${not empty layerPricing.projection}">
                                                    ${layerPricing.projection} <br/><c:out default="0" value="${layerPricing.minScale}"/> &lt;-&gt; <c:out default="&#8734;" value="${layerPricing.maxScale}" escapeXml="false"/>        
                                                </c:when>
                                                <c:otherwise>n/a</c:otherwise>
                                            </c:choose>
                                            
                                        </td>                                            
                                        <td class="${rowstyle}">
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
                                                [<a href="editpricing.do?delete=submit&pricingid=${layerPricing.id}&id=${id}">D</a>]    
                                            </c:if>
                                        </td>                            
                                    </tbody>                    
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
                <div id="new" class="sheet">            
                    <html:form action="/editpricing.do?id=${id}" focus="startDate" onsubmit="return validateReportingForm(this)">
                        <label>Type :</label>
                        <html:select property="planType">
                            <html:option value="1">Per Opvraag</html:option>
                        </html:select>
                        <br/>
                        <label>Geldig vanaf :</label><html:text styleClass="validFrom" property="validFrom" styleId="validFrom"/>
                        <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
                            <jsp:param name="elementStyleId" value="validFrom"/>
                        </jsp:include>
                        <br/>
                        <label>Geldig tot en met :</label><html:text styleClass="validUntil" property="validUntil" styleId="validUntil"/>
                        <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
                            <jsp:param name="elementStyleId" value="validUntil"/>
                        </jsp:include>
                        <br/>   
                        <label>Tarief :</label><html:text styleId="unitPrice" property="unitPrice"/><html:checkbox styleId="layerIsFree" property="layerIsFree" onclick="unitPriceState(this.checked);"/> Kaart is gratis!<br/>
                        <script type="text/javascript">
                               function unitPriceState(state){
                                    var unitPrice = document.getElementById('unitPrice');
                                    unitPrice.disabled = state;
                                    if (state == true) {
                                        unitPrice.value = '';
                                    }
                                }
                        </script>
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
                        <div id="selectScale" style="display:none;">
                            <label>Schaalbereik :</label>    
                            <html:text property="minScale" style="width:100px;"/> van/tot <html:text property="maxScale" style="width:100px;"/><br/>
                        </div><br/>
                        <label>Service & Methode :</label> ${pricingForm.map.service} ${pricingForm.map.operationWMS}<br/>
                        
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
        Selecteer een kaartlaag!
    </c:otherwise>
    
</c:choose>


