<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div id="bodycontent">
    <div id='top'>
        <!-- BEGIN header -->
        <tiles:get name='top'/>
        <!-- END header -->
    </div>
    <div id='menu'>
        <!-- BEGIN menu -->
        <tiles:get name='menu'/>
        <!-- END menu -->
    </div>
    <div id='content'>
        <!-- BEGIN content -->						
        <tiles:get name='content'/>
        <!-- END content -->
    </div>
    <div id='footer'>
        <!-- BEGIN footer-->
        <tiles:get name='footer'/>
        <!-- END footer-->
    </div>
</div>
