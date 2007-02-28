<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<div id="nav">
    <center><B>Menu algemeen</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                <li><a href="<html:rewrite page='/beheer/index.do' module='' />">Beheer</a></li>
                <li><a href="<html:rewrite page='/viewer/index.do' module='' />">Viewer</a></li>
                <li><a href="<html:rewrite page='/viewer/mapviewer.do' module='' />">Mapviewer</a></li>
                <% if (getServletConfig().getInitParameter("demoActive").equals("true")) { %>
                    <li><a href="<html:rewrite page='/demo/index.do' module='' />">Demo</a></li>
                <% } %>
            </ul>
        </div>
    </p>
</div>
