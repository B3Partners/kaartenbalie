<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<c:set var="calImage" scope="page"><html:rewrite page='/images/siteImages/calendar_image.gif' module='' /></c:set>

<c:set var="element"><%= request.getParameter("elementStyleId") %></c:set>

<img src="${calImage}" id="cal-button"
     style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
     title="Date selector"
     alt="Date selector"
     onmouseover="this.style.background='red';" 
     onmouseout="this.style.background=''"
     onClick="cal.select(document.getElementById('${element}'),'cal-button','yyyy-MM-dd', document.getElementById('${element}').value); return false;"
     name="cal-button"
/>