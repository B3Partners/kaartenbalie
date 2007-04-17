<%@include file="/templates/taglibs.jsp" %>

<c:if test="${!empty focus}"> 
<script type="text/JavaScript"> 
<!-- 
    function initFocus() { 
       try { 
           document.forms[0]['${focus}'].focus(); 
       } catch(e) { 
       } 
    } 
     
    if(window.addEventListener) { 
        window.addEventListener("load", initFocus, false); 
    } else { 
        window.attachEvent("onload", initFocus, false); 
    } 
// -->    
</script> 
</c:if> 
