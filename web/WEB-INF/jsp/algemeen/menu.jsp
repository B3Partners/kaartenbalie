<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu algemeen</B></center><br>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a></li>
                <li><a href="<html:rewrite page='/beheer.do' module='' />"><fmt:message key="algemeen.beheer"/></a></li>
                <li><a href="<html:rewrite page='/viewer.do' module='' />"><fmt:message key="algemeen.viewer"/></a></li>
                <c:if test="${false}">
                    <li><a href="<html:rewrite page='/demo.do' module='' />"><fmt:message key="algemeen.demo"/></a></li>
                </c:if>
            </ul>
        </div>
    </p>
</div>