<%@include file="/templates/taglibs.jsp" %>

<c:if test="${DemoActive == true}">
    <div id="nav">
        <center><B>Menu Demo</B></center>
        <p>
            <div id="nav-menu">
                <ul>
                    <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                    <li><a href="<html:rewrite page='/demo/index.do' module='' />"><fmt:message key="demo.demo"/></a></li>
                    <li><a href="<html:rewrite page='/demo/registration.do' module='' />"><fmt:message key="demo.registration"/></a></li>
                    <li><a href="<html:rewrite page='/demo/mapviewer.do' module='' />"><fmt:message key="demo.mapviewer"/></a></li>                    
                    <li><a href="<html:rewrite page='/demo/showProfile.do' module='' />"><fmt:message key="demo.profile"/></a></li>
                </ul>
            </div>
        </p>
    </div>
</c:if>