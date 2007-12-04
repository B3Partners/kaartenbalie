<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu beheerder</B></center><br>
    <p>
        <div id="nav-menu">
            
                <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a>
                <a href="<html:rewrite page='/beheer.do' module='' />"><fmt:message key="beheer.home"/></a>
                <a href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.server"/></a>
                <a href="<html:rewrite page='/beheer/organization.do' module='' />"><fmt:message key="beheer.organization"/></a>
                <a href="<html:rewrite page='/beheer/user.do' module='' />"><fmt:message key="beheer.users"/></a>
                <a href="<html:rewrite page='/beheer/metadata.do' module='' />"><fmt:message key="beheer.metadata"/></a>
        </div>
    </p>
</div>