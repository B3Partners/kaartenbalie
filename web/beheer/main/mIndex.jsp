<%@include file="/templates/taglibs.jsp" %>           

<div id="nav">
    <center><B>Menu beheerder</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a></li>
                <li><a href="<html:rewrite page='/beheer/index.do' module='' />"><fmt:message key="beheer.home"/></a></li>
                <li><a href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.server"/></a></li>
                <li><a href="<html:rewrite page='/beheer/organization.do' module='' />"><fmt:message key="beheer.organization"/></a></li>
                <li><a href="<html:rewrite page='/beheer/user.do' module='' />"><fmt:message key="beheer.users"/></a></li>
                <%-- <li><a href="<html:rewrite page='/beheer/metadata.do' module='' />"><fmt:message key="beheer.metadata"/></a></li> --%>
            </ul>
        </div>
    </p>
</div>