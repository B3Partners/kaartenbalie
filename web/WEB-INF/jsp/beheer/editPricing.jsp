<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>


<c:choose>
    <c:when test="${not empty id}">
        <jsp:include page="/WEB-INF/jsp/inc_calendar.jsp" flush="true"/>
        <fieldset style="margin:0px;padding-top:0px;">
            <legend>Prijsbepaling</legend>
            <label>Service Provider :</label> ${spName}<br/>
            <label style="margin-bottom:10px;">Kaartlaag :</label> ${lName}<br/>
            <label style="text-align:left;margin-bottom:10px;">Prijsgegevens :</label><br/>
            <div style="background-color:white;border:1px Solid Black;">
                <table style="width:100%;padding:0px;margin:0px;border-collapse: collapse;">
                    <tr>
                        <thead>
                            <th>planType</th>
                            <th>Aangemaakt</th>
                            <th>Geldig vanaf</th>
                            <th>Verloopt</th>
                            <th>Tarief</th>
                            <th>[d]</th>                            
                        </thead>
                    </tr>
                    
                    <c:forEach var="layerPricing" items="${layerPricings}">
                        <tr>
                            <tbody>
                                <td>
                                    <c:choose>
                                        <c:when test="${layerPricing.planType == 1}">
                                            Per Opvraag
                                        </c:when>
                                        <c:otherwise>
                                            Onbekend
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="yyyy-MM-dd" value="${layerPricing.creationDate}"/>        
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty layerPricing.validFrom}">
                                            <fmt:formatDate pattern="yyyy-MM-dd" value="${layerPricing.validFrom}"/>        
                                        </c:when>
                                        <c:otherwise>
                                            Altijd
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty layerPricing.validUntil}">
                                            <fmt:formatDate pattern="yyyy-MM-dd" value="${layerPricing.validUntil}"/>
                                        </c:when>
                                        <c:otherwise>
                                            Nooit
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatNumber value="${layerPricing.unitPrice}" minFractionDigits="2"  maxFractionDigits="2"/></td>
                                <td>[<a href="editpricing.do?delete=submit&pricingid=${layerPricing.id}&id=${id}">D</a>]</td>                            
                            </tbody>                    
                        </tr>
                    </c:forEach>
                </table>
            </div>
            
            <label style="text-align:left;margin-bottom:10px;padding-top:10px;">Leidt tot:</label><br/>
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
        <c:if test="${not empty downsize}">
            <fieldset>
                <legend>Tarief herleiding</legend>
                <c:set var="downSizeElement" value="${downsize}" scope="request"/>
                <jsp:include page="/WEB-INF/jsp/beheer/pricing/downSizeRoot.jsp"/>
            </fieldset>
        </c:if>
        <html:form action="/editpricing.do?id=${id}" focus="startDate" onsubmit="return validateReportingForm(this)">
            <fieldset style="margin:0px;padding-top:0px;">
                <legend>Nieuwe Prijsbepaling</legend>
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
                        if (state == true)
                        {
                            unitPrice.value = '';
                        }
                    }
                </script>
                <html:submit property="save" styleClass="submit">Voeg toe</html:submit>
            </fieldset>
        </html:form>
    </c:when>
    <c:otherwise>
        Selecteer een kaartlaag!
    </c:otherwise>
    
</c:choose>


