<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu algemeen</B></center><br>
    <p>
        <div id="nav-menu">
                <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a>
                <a href="<html:rewrite page='/beheer.do' module='' />"><fmt:message key="algemeen.beheer"/></a>
                <a href="<html:rewrite page='/viewer.do' module='' />"><fmt:message key="algemeen.viewer"/></a>
                <c:if test="${false}">
                    <a href="<html:rewrite page='/demo.do' module='' />"><fmt:message key="algemeen.demo"/></a>
                </c:if>
            
        </div>
    </p>
</div>