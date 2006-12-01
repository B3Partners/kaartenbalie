<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div id="nav">
    <center><B>Menu viewer</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                <li><a href="<html:rewrite page='/viewer/index.do' module='' />">Viewer</a></li>
                <li><a href="<html:rewrite page='/viewer/mapviewer.do' module='' />">Mapviewer</a></li>
                <li><a href="<html:rewrite page='/viewer/createPersonalURL.do' module='' />">Cre&euml;er Personal URL</a></li>
            </ul>
        </div>
    </p>
</div>