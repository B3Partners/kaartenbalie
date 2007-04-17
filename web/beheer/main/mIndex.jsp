<%@include file="/templates/taglibs.jsp" %>           

<div id="nav">
    <center><B>Menu beheerder</B></center>
    <p>
        <div id="nav-menu">
            <ul>
                <li><a href="<html:rewrite page='/index.do' module='' />">Home</a></li>
                <li><a href="<html:rewrite page='/beheer/index.do' module='' />">Beheer home</a></li>
                <li><a href="<html:rewrite page='/beheer/server.do' module='' />">Beheer servers</a></li>
                <li><a href="<html:rewrite page='/beheer/organization.do' module='' />">Beheer organisaties</a></li>
                <li><a href="<html:rewrite page='/beheer/user.do' module='' />">Beheer gebruikers</a></li>
            </ul>
        </div>
    </p>
</div>
