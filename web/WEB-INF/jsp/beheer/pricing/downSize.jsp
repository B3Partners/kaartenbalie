<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<c:if test="${not empty downsize}">

    <table style="width:100%;border:1px Solid Black;border-collapse:collapse">
        <tr>
            <thead>
                <th style="width:300px;">Niveau's:
                    <a href="pricingdownsize.do?downsize=submit&id=${id}&level=0&details=${details}">Enkel</a>                    
                    <c:choose>
                        <c:when test="${level > 0 }">
                            <a href="pricingdownsize.do?downsize=submit&id=${id}&level=${level-1}&details=${details}">Minder</a>
                        </c:when>
                        <c:otherwise>
                            
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${level >= 0 || empty level}">
                            <a href="pricingdownsize.do?downsize=submit&id=${id}&level=${level+1}&details=${details}">Meer</a>                    
                        </c:when>
                        <c:otherwise>
                           
                        </c:otherwise>
                    </c:choose>
                    <a href="pricingdownsize.do?downsize=submit&id=${id}&level=-1&details=${details}">Alle</a>    
                    Opties: <a href="pricingdownsize.do?downsize=submit&id=${id}&level=${level}&details=${!details}">Details aan/uit</a>    
                </th>
                <th style="width:100px;" colspan="2">Geldigheid</th>
                <th style="width:50px;text-align:center;" colspan="2">Tarief</th>
            </thead>
        </tr>
        <tr>
            <thead style="border-bottom:2px Solid Black;">
                <th style="width:300px;">Laag</th>
                <th style="width:50px;">Van</th>
                <th style="width:50px;">Tot</th>
                <th style="width:50px;">Bruto</th>
                <th style="width:50px;">Netto</th>
                
            </thead>
        </tr>
        <c:set var="childWrapperElement" scope="request" value="${downsize}"/>
        <jsp:include page="/WEB-INF/jsp/beheer/pricing/downSizeChild.jsp">
            <jsp:param name="padding" value="${0}"/>
        </jsp:include>
    </table>
    
</c:if>







