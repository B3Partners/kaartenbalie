<%@include file="/templates/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu algemeen</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a></li>
                <li><a href="<html:rewrite page='/beheer/index.do' module='' />"><fmt:message key="algemeen.beheer"/></a></li>
                <li><a href="<html:rewrite page='/viewer/index.do' module='' />"><fmt:message key="algemeen.viewer"/></a></li>
                <c:if test="${DemoActive == true}">
                    <li><a href="<html:rewrite page='/demo/index.do' module='' />"><fmt:message key="algemeen.demo"/></a></li>
                </c:if>
            </ul>
        </div>
    </p>
</div>