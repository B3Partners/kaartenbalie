<%@include file="/templates/taglibs.jsp" %>

<div id="bodycontent">
    <div id="banner">
        <!-- BEGIN header -->
        <tiles:get name='top'/>
        <!-- END header -->
    </div>
    <div id="balk">
        <div>
            <a href="<html:rewrite page='/login.do' module='' />">Login</a>
        </div>
    </div>
    <div id="nav">
        <!-- BEGIN menu -->
        <tiles:get name='menu'/>
        <!-- END menu -->
    </div>
    <div id="content">
        <!-- BEGIN content -->		
        <tiles:insert definition="common.actionMessages"/>
        <tiles:get name='content'/>
        <!-- END content -->
    </div>
    <div id="footer">
        <DIV id=footertext>
            <!-- BEGIN footer-->
            <tiles:get name='footer'/>
            <!-- END footer-->
        </DIV>
    </div>
</div>