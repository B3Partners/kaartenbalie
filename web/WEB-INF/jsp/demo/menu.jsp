<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu Demo</B></center><br>
    <p>
        <div id="nav-menu">
            <!--
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                <li><a href="<html:rewrite page='/demo.do' module='' />"><fmt:message key="demo.demo"/></a></li>
                <li><a href="<html:rewrite page='/demo/registration.do' module='' />"><fmt:message key="demo.registration"/></a></li>
                <li><a href="<html:rewrite page='/demo/mapviewer.do?layers=17_basis_nl*16_bebouwdekom_nl' module='' />"><fmt:message key="demo.mapviewer"/></a></li>                    
                <li><a href="<html:rewrite page='/demo/showProfile.do' module='' />"><fmt:message key="demo.profile"/></a></li>
                <li></li>
                <li><a href="http://www.b3p.nl/b3partners/webPages.do?pageid=203015">B3Partners Website</a></li>
            </ul>
            -->
            
                <a href="<html:rewrite page='/index.do' module='' />">Home</a>
                <a href="<html:rewrite page='/demo.do' module='' />"><fmt:message key="demo.demo"/></a>
                <a href="<html:rewrite page='/demo/registration.do' module='' />"><fmt:message key="demo.registration"/></a>
                <a href="<html:rewrite page='/demo/mapviewer.do?layers=17_basis_nl*16_bebouwdekom_nl' module='' />"><fmt:message key="demo.mapviewer"/></a>                    
                <a href="<html:rewrite page='/demo/showProfile.do' module='' />"><fmt:message key="demo.profile"/></a>
                
                <a href="http://www.b3p.nl/b3partners/webPages.do?pageid=203015">B3Partners Website</a>
            
        </div>
    </p>
</div>
