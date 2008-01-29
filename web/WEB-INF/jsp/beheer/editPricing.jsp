<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>


<c:choose>
    <c:when test="${not empty id}">
        <jsp:include page="/WEB-INF/jsp/inc_calendar.jsp" flush="true"/>
        <fieldset style="margin:0px;padding-top:0px;">
            <legend>Prijsinformatie</legend>
            <label>Service Provider :</label> ${spName}<br/>
            <label style="margin-bottom:10px;">Kaartlaag :</label> ${lName}<br/>
            <div style="background-color:white;border:1px Solid Black;width:550px;">
                <table style="width:550px;padding:0px;margin:0px;border-collapse: collapse;">
                    <tr>
                        <th style="width:150px;">Type</th>
                        <th style="width:200px;">Tarief deze laag</th>
                        <th style="width:200px;">Uiteindelijk tarief</th>
                    </tr>
                    <tr>
                        <th style="width:150px;">per Request</th>
                        <td style="width:200px;"><fmt:formatNumber value="${priceRequestSingle}" minFractionDigits="2"  maxFractionDigits="2"/></td>
                        <td style="width:200px;"><fmt:formatNumber value="${priceRequestCascade}" minFractionDigits="2" maxFractionDigits="2"/> [<a href="#" onclick="parent.showPopup(800,600,'Herleidbare Informatie','pricingdownsize.do?downsize=submit&id=${id}&level=-1');">Herleiden</a>]</td>                        
                    </tr>
                </table>
            </div>
        </fieldset>
        
        <div class="tabcollection" id="pricingCollection">
            <div id="tabs">
                <ul>
                    <li id="details" onclick="displayTabBySource(this);">Details</li>
                    <li id="new"  onclick="displayTabBySource(this);">Nieuwe Prijsbepaling</li>
                    <li id="help"  onclick="displayTabBySource(this);">Uitleg</li>
                </ul>
            </div>
            <div id="sheets" style="height:300px;">
                <div id="help" class="sheet">  
                    <b>layerPricing Aggregatie</b><br/>
                    <c:choose>
                        <c:when test="${aggregateLayerPricings == true}">
                            <p>
                                Kaartenbalie maakt gebruik van aggregatie voor het berekenen van de layerprijzen.<br/>
                                Dit wil dus zeggen dan wanneer een prijs wordt opgevraagd, deze prijs opgebouwd kan
                                zijn uit meerdere layerprijzen. Alle layerprijzen die aan de voorwaarden voldoen worden
                                bij elkaar opgeteld. De som van deze optelling is de prijs.
                            </p>
                        </c:when>
                        <c:otherwise>
                            <p>
                                Kaartenbalie maakt <b>geen</b> gebruik van aggregatie. <br/>
                                De nieuwe layerprijs die aan de voorwaarden voldoet bepaalt de uiteindelijke prijs van de kaart.
                            </p>
                        </c:otherwise>
                    </c:choose>
                    
                    <b>Legenda Detail pagina</b>
                    <table style="width:100%;border:1px Solid Black;">
                        <thead>
                            <tr>
                                <th>Kolommen</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${aggregateLayerPricings == false}">
                                <tr>
                                    <td class="active">
                                        Hiermee worde de actieve layerPricing aangegeven voor dit moment.
                                        Het kan zijn dat op een andere datum een andere layerPricing actief is.
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="">
                                    Met deze kleur worden alle andere layerPricings aangegeven.
                                </td>
                            </tr> 
                            <tr>
                                <td class="deleted">
                                    Dit is de kleur voor layerPricings die zijn verwijders.
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
                <div id="details" class="sheet" style="height:280px;">  
                    <div style="background-color:white;border:1px Solid Black;">
                        
                        
                        <table style="width:100%;padding:0px;margin:0px;border-collapse: collapse;">
                            <tr>
                                <thead>
                                    <th>planType</th>
                                    <th>Aangemaakt</th>
                                    <th>Geldig vanaf</th>
                                    <th>Verloopt</th>
                                    <th>Verwijderd</th>
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
                                            <fmt:formatDate pattern="yyyy-MM-dd @ HH:mm:ss" value="${layerPricing.creationDate}"/>        
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
                                            <fmt:formatDate pattern="yyyy-MM-dd @ HH:mm:ss" value="${layerPricing.deletionDate}"/>        
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
                               function unitPriceState(state)
                                {
                                    var unitPrice = document.getElementById('unitPrice');
                                    unitPrice.disabled = state;
                                    if (state == true) {
                                        unitPrice.value = '';
                                    }
                                }
                        </script>
                        <ul>
                            <li>
                                <b>Geldig vanaf :</b> Indien leeg, regel is altijd geldig. Anders begin van de dag (0:00:00u).
                            </li>
                            <li>
                                <b>Geldig tot en met :</b> Indien leeg, regel verloopt nooit. Anders einde van de dag (23:59:59u).
                            </li>
                            <li>
                                <b>Tarief :</b> Indien leeg, 0,00 credits. 
                            </li>
                            <li>
                                <b>Kaart is gratis :</b> Indien aangevinkt, de kaart is in de aangegeven periode gratis. (NB: Andere regels hebben geen invloed meer zolang de kaart gratis is.)
                            </li>
                        </ul>
                        <html:submit property="save" styleClass="submit">Voeg toe</html:submit>
                        
                    </html:form>
                </div>
                
            </div>
        </div>
        <script language="JavaScript" type="text/javascript">
            window.onLoad = registerCollection('pricingCollection', 'details');
        </script>
    </c:when>
    <c:otherwise>
        Selecteer een kaartlaag!
    </c:otherwise>
    
</c:choose>


