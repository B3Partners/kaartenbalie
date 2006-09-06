<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>
   <form action="j_security_check" method='post'>
       <div class="item">Inlognaam:</div>
       <div class="value"><input type="text" name="j_username"></div>
       <div class="item">Wachtwoord:</div>
       <div class="value"><input type="password" name="j_password"></div>
       <div class="buttonbox">
           <input type="button" name="login" value="login" >
       </div>
   </form> 
