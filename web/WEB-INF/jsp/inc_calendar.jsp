<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!-- Scripts and settings for the calendar function -->
<html:link href="calendar-brown" title="summer" />
<c:set var="calSrc" scope="page"><html:rewrite page='/js/calendar/CalendarPopup.js' module='' /></c:set>
<c:set var="calStyle" scope="page"><html:rewrite page='/styles/calendar/calendar-style.css' module='' /></c:set>
<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
<script language="JavaScript" type="text/javascript" src="${calSrc}"></script>
<link rel="stylesheet" type="text/css" media="all" href="${calStyle}" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
</script>