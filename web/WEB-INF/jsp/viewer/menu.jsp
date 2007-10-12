<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu viewer</B></center><br>
    <p>
        <div id="nav-menu">
            <!-- <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="viewer.home"/></a></li>
                <li><a href="<html:rewrite page='/viewer.do' module='' />"><fmt:message key="viewer.viewer"/></a></li>
                <li><a href="<html:rewrite page='/viewer/mapviewer.do'module=''/>"><fmt:message key="viewer.mapviewer"/></a></li>
                <li><a href="<html:rewrite page='/viewer/changeProfile.do'module=''/>">Profiel</a></li>
                <li><a href="<html:rewrite page='/viewer/createPersonalURL.do'module=''/>"><fmt:message key="viewer.persoonlijkeurl"/></a></li>
                <li><a href="<html:rewrite page='/viewer/wmsUrlCreator.do'module=''/>"><fmt:message key="viewer.getmapurl"/></a></li>
            </ul> -->
            
                <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="viewer.home"/></a>
                <a href="<html:rewrite page='/viewer.do' module='' />"><fmt:message key="viewer.viewer"/></a>
                <a href="<html:rewrite page='/viewer/mapviewer.do'module=''/>"><fmt:message key="viewer.mapviewer"/></a>
                <a href="<html:rewrite page='/viewer/changeProfile.do'module=''/>">Profiel</a>
                <a href="<html:rewrite page='/viewer/createPersonalURL.do'module=''/>"><fmt:message key="viewer.persoonlijkeurl"/></a>
                <a href="<html:rewrite page='/viewer/wmsUrlCreator.do'module=''/>"><fmt:message key="viewer.getmapurl"/></a>
            
        </div>
    </p>
</div>