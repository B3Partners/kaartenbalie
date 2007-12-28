<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<c:set var="padding"><%= request.getParameter("padding") %></c:set>
<c:set scope="page" var="childWrapper" value="${childWrapperElement}"/>
<tr style="border-bottom:1px Solid #AAA;border-top:1px Solid #AAA;">
    <th colspan="3" style="padding-left:${padding}px;"><a href="#" onclick="parent.navigate('pricingframe','editpricing.do?edit=submit&id=${childWrapper.layer.id}');"/>${childWrapper.layer.title}</a></th>
    <th style="text-align:right;padding-right:10px;"><fmt:formatNumber value="${childWrapper.layerPriceGross}" minFractionDigits="2"  maxFractionDigits="2"/></th>
    <th style="text-align:right;padding-right:10px;"><fmt:formatNumber value="${childWrapper.layerPriceNet}" minFractionDigits="2"  maxFractionDigits="2"/></th>
</tr>
<c:forEach var="pricingPlan" items="${childWrapper.pricingPlans}">
    <tr>
        <td style="text-align:left;padding-left:${padding}px;"><fmt:formatDate pattern="yyyy-MM-dd" value="${pricingPlan.creationDate}"/></td>
        <td style="">
            <c:choose>
                <c:when test="${not empty pricingPlan.validFrom}">
                    <fmt:formatDate pattern="yyyy-MM-dd" value="${pricingPlan.validFrom}"/>        
                </c:when>
                <c:otherwise>
                    Altijd
                </c:otherwise>
            </c:choose>
        </td>
        <td style="">
            <c:choose>
                <c:when test="${not empty pricingPlan.validUntil}">
                    <fmt:formatDate pattern="yyyy-MM-dd" value="${pricingPlan.validUntil}"/>
                </c:when>
                <c:otherwise>
                    Nooit
                </c:otherwise>
            </c:choose>
        </td>        
        <td style="text-align:right;padding-right:10px;">
            <font style="color:${pricingPlan.unitPrice < 0 ? '#F00':''};">
                <fmt:formatNumber value="${pricingPlan.unitPrice}" minFractionDigits="2"  maxFractionDigits="2"/>
            </font>
        </td>
    </tr>
    
</c:forEach>  
<tr style="border-bottom:1px Solid #AAA;border-top:1px Solid #AAA;">
    <td colspan="4"></td>
</tr>

<c:forEach var="childWrapper" items="${childWrapper.childWrappers}">
    <c:set var="childWrapperElement" scope="request" value="${childWrapper}"/>
    <jsp:include page="/WEB-INF/jsp/beheer/pricing/downSizeChild.jsp">
        <jsp:param name="padding" value="${padding + 20}"/>
    </jsp:include>
</c:forEach>    



