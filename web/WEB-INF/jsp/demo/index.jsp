<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2007-2011 B3Partners BV.

This program is distributed under the terms
of the GNU General Public License.

You should have received a copy of the GNU General Public License
along with this software. If not, see http://www.gnu.org/licenses/gpl.html

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div class="steps">
    <div class="stepactive"><fmt:message key="demo.steps.start" /></div>
    <div class="step"><fmt:message key="demo.steps.registeren" /></div>
    <div class="step"><fmt:message key="demo.steps.kaarten" /></div>
    <div class="step"><fmt:message key="demo.steps.persurl" /></div>   
    <div class="step"><fmt:message key="demo.steps.viewer" /></div>   
</div>

<div id='democontent'>
    <div id="democontentheader"><fmt:message key="demo.index.title" /></div>
    <div id="democontenttext">
        <div id="demoheader3"><fmt:message key="demo.index.header1" /></div>
        <fmt:message key="demo.index.body1" />
        
        <div id="demoheader3"><fmt:message key="demo.index.header2" /></div>
        <fmt:message key="demo.index.body2" />
        
        <div id="demoheader3"><fmt:message key="demo.index.header3" /></div>
        <fmt:message key="demo.index.body3" />
        <button onclick="location.href='<html:rewrite page="/registration.do" module="/demo"/>'"><fmt:message key="demo.index.registratie" /></button>
    </div>
</div>
