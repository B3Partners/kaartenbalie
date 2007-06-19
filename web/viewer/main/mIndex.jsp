<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div id="nav">
    <center><B>Menu viewer</B></center><br>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="viewer.home"/></a></li>
                <li><a href="<html:rewrite page='/viewer/index.do' module='' />"><fmt:message key="viewer.viewer"/></a></li>
                <li><a href="<html:rewrite page='/viewer/mapviewer.do?layers=17_basis_nl*16_bebouwdekom_nl' module='' />"><fmt:message key="viewer.mapviewer"/></a></li>
                <li><a href="<html:rewrite page='/viewer/changeProfile.do' module='' />">Profiel</a></li>
                <li><a href="<html:rewrite page='/viewer/createPersonalURL.do' module='' />"><fmt:message key="viewer.persoonlijkeurl"/></a></li>
                <li><a href="<html:rewrite page='/viewer/wmsUrlCreator.do' module='' />"><fmt:message key="viewer.getmapurl"/></a></li>
            </ul>
        </div>
    </p>
</div>