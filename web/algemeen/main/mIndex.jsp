<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div id="nav">
    <center><B>Menu algemeen</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                <li><a href="<html:rewrite page='/beheer/index.do' module='' />">Beheer</a></li>
                <li><a href="<html:rewrite page='/viewer/index.do' module='' />">Viewer</a></li>
                <% if (getServletConfig().getInitParameter("demoActive").equals("true")) { %>
                    <li><a href="<html:rewrite page='/demo/index.do' module='' />">Demo</a></li>
                <% } %>
            </ul>
        </div>
    </p>
</div>