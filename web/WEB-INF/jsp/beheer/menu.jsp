<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu beheerder</B></center><br>
    <p>
        <div id="nav-menu">
            
            <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a>
            <a href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.server"/></a>
            <a href="<html:rewrite page='/beheer/wfsserver.do' module='' />"><fmt:message key="beheer.wfsserver"/></a>
            <c:if test="${menuParameters.metadata == true}">
                <a href="<html:rewrite page='/beheer/metadata.do' module='' />"><fmt:message key="beheer.metadata"/></a>
            </c:if>
            <c:if test="${menuParameters.pricing == true}">
                <a href="<html:rewrite page='/beheer/pricing.do' module='' />"><fmt:message key="beheer.pricing"/></a>                        
            </c:if>
            <a href="<html:rewrite page='/beheer/organization.do' module='' />"><fmt:message key="beheer.organization"/></a>
            <c:if test="${menuParameters.accounting == true}">
                <a href="<html:rewrite page='/beheer/accounting.do' module='' />"><fmt:message key="beheer.accounting"/></a>
            </c:if>
            <c:if test="${menuParameters.reporting == true}">
                <a href="<html:rewrite page='/beheer/reporting.do' module='' />"><fmt:message key="beheer.reporting"/></a>     
            </c:if>
            <a href="<html:rewrite page='/beheer/user.do' module='' />"><fmt:message key="beheer.users"/></a>
        </div>
    </p>
</div>