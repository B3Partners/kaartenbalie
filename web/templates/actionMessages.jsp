<%@include file="/templates/taglibs.jsp" %> 

<div class="messages"> 
    <html:messages id="message" message="true" >
        <div id="error">
            <c:out value="${message}" escapeXml="false"/>
        </div>
    </html:messages> 
</div> 

