<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false"%> 
<html:html>
    <head>
        <title><fmt:message key="index.title"/></title>
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
    </head>
    <script>
        <c:if test="${not empty getMapMadeSuccesfull && getMapMadeSuccesfull== 'gelukt'}">
            window.opener.location.reload();
            window.close();
        </c:if>
    </script>
    <body>
        <div style="margin: 5px;">
            <c:if test="${not empty message}">
                <h2><c:out value="${message}"/></h2>
            </c:if>
            <c:set var="form" value="${wmsUrlCreatorForm.map}"/>
            <html:form action="/wmsUrlCreator">
                <h2><fmt:message key="beheer.kaarten.wmsurlcreator.getcapabilitiesurl"/></h2>
                Vul hieronder de getCapabilitie url in van de service waarvan u een kaart wilt toevoegen.
                <div>                    
                    <html:text size="100" property="getCapabilitiesUrl"/>
                    <html:submit property="getCapabilities">
                        <fmt:message key="beheer.kaarten.wmsurlcreator.getCapabilities"/>
                    </html:submit>
                </div>
                <br><br>
                <!-- toon de layers -->
                <c:if test="${not empty layerList}">
                    <div class="wmscomponentdiv">
                        <h2>Layers:</h2>
                        <div class="listdiv">
                            <c:forEach items="${layerList}" var="l">
                                <html:multibox property="selectedLayers" value="${l}"></html:multibox><c:out value="${l}"/><br/>
                            </c:forEach>                                
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty projectieList}">
                    <div class="wmscomponentdiv">
                        <h2>Projecties:</h2>
                        <div class="listdiv">
                            <html:select property="selectedProjectie">
                                <c:forEach items="${projectieList}" var="p">
                                    <html:option value="${p}"><c:out value="${p}"/></html:option><br/>
                                </c:forEach>
                            </html:select>
                        </div>
                    </div>
                    <div class="wmscomponentdiv">
                        <h2>BBox:</h2>
                        <html:text property="bbox" size="40"/>
                    </div>
                </c:if>
                <div style="clear: both">
                    <html:submit property="getMapUrl">
                        <fmt:message key="beheer.kaarten.wmsurlcreator.getMap"/>
                    </html:submit>
                </div>
                <c:if test="${not empty getMapUrlRequest}">
                    <div class="wmscomponentdiv" style="clear: both;">
                        
                        <h2>GetMap request: </h2>
                        <TEXTAREA id="taGetMap" COLS=80 ROWS=6><c:out value="${getMapUrlRequest}"/>
                        </TEXTAREA>
                        <input type="button" class="knopBreed" onclick="plaatsGetMap()" value="Gebruik GetMap URL"/>
                    </div>
                </c:if>
            </html:form>
        </div>
        
    </body>
</html:html>
