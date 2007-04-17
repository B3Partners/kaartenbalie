<%@include file="/templates/taglibs.jsp" %>

<c:set var="focus" value="j_username" scope="request"/> 
<tiles:insert definition="common.setFocus"/> 

<form action="j_security_check" method='post' >
    <div class="item">
        <fmt:message key="algemeen.username"/>:
    </div>
    <div class="value">
        <input type="text" name="j_username">
    </div>
    <div class="item">
        <fmt:message key="algemeen.password"/>:
    </div>
    <div class="value">
        <input type="password" name="j_password">
    </div>
    <html:submit property="login" styleClass="knop">
        <fmt:message key="button.login"/>
    </html:submit>
</form>
