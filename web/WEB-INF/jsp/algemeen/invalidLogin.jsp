<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
Wachtwoord en/of gebruikersnaam zijn onjuist.<p>
Probeer het nogmaals:
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
<script type="text/javascript" language="JavaScript">
  <!--
  var focusControl = document.forms[0].elements["j_username"];
  focusControl.focus();
  // -->
</script>
