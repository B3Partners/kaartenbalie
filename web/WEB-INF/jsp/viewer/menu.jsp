<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="nav">
    <center><B>Menu viewer</B></center><br>
    <p>
        <div id="nav-menu">
                <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="viewer.home"/></a>
                <a href="<html:rewrite page='/viewer/createPersonalURL.do'module=''/>"><fmt:message key="viewer.persoonlijkeurl"/></a>
                <a href="<html:rewrite page='/viewer/changeProfile.do'module=''/>"><fmt:message key="viewer.profiel"/></a>
                <a href="<html:rewrite page='/viewer/wmsUrlCreator.do'module=''/>"><fmt:message key="viewer.getmapurl"/></a>
            
        </div>
    </p>
</div>