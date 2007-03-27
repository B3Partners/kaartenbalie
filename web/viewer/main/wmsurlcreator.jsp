<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false"%> 


<html:html>
    <head>
        <title><fmt:message key="kaartenbalie.title"/></title>
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
    </head>
    <script>
        <c:if test="${not empty getMapMade}">
            //window.opener.location.reload();
            window.opener.putWmsGetMap('${getMapMade}');
            window.close();
        </c:if>
    </script>
    <body>
        <div style="margin: 5px;">
            <c:if test="${not empty message}">
                <h2><c:out value="${message}"/></h2>
            </c:if>
            <c:set var="form" value="${wmsUrlCreatorForm.map}"/>
            <html:form action="/wmsUrlCreator" >
                <html:hidden property="personalUrl"/>
                <!-- toon de layers -->
                <c:if test="${not empty layerList}">
                    <div class="wmscomponentdiv">
                        <div><b><fmt:message key="beheer.getmapurl.layers"/>:</b></div>
                        <div class="listdiv">
                            <c:forEach items="${layerList}" var="l">
                                <html:multibox property="selectedLayers" value="${l.uniqueName}"></html:multibox><c:out value="${l.name}"/><br/>
                            </c:forEach>                                
                        </div>
                    </div>
                </c:if>
                <div class="wmscomponentdiv">
                <c:if test="${not empty projectieList}">                    
                    <div><b><fmt:message key="beheer.getmapurl.projecties"/>:</b></div>
                    <html:select property="selectedProjectie">
                        <c:forEach items="${projectieList}" var="p">
                            <html:option value="${p}"><c:out value="${p}"/></html:option><br/>
                        </c:forEach>
                    </html:select>
                </c:if>
                <div><b><fmt:message key="beheer.getmapurl.height"/>:</b></div>
                <html:text property="height"/>
                <div><b><fmt:message key="beheer.getmapurl.width"/>:</b></div>
                <html:text property="width"/>
                <div><b><fmt:message key="beheer.getmapurl.bbox"/>:</b></div>
                <html:text property="bbox" size="40"/>
                <c:if test="${not empty formatList}">
                    <div><b><fmt:message key="beheer.getmapurl.format"/>:</b></div>
                    <html:select property="selectedFormat">
                        <c:forEach items="${formatList}" var="f">
                            <html:option value="${f}"><c:out value="${f}"/></html:option><br/>
                        </c:forEach>
                    </html:select>
                </c:if>
                </div>
                
                <div style="clear: both">
                    <html:submit property="getMapUrl">
                        <fmt:message key="beheer.kaarten.wmsurlcreator.getMap"/>
                    </html:submit>
                </div>
            </html:form>
        </div>
        
    </body>
</html:html>
