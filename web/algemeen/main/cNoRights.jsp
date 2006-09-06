<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
Wachtwoord en/of gebruikersnaam zijn onjuist. Of u heeft geen rechten op dit deel.<br>
Probeer het nogmaals:
<form action="j_security_check" method='post'>
   <div class="item">Inlognaam:</div>
   <div class="value"><input type="text" name="j_username"></div>
   <div class="item">Wachtwoord:</div>
   <div class="value"><input type="password" name="j_password"></div>
   <div class="buttonbox">
       <input type="submit" name="login" value="login" >
   </div>
</form> 