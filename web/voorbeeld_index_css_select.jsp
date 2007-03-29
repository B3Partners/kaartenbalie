<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CSS Subpage Selector</title>
        <link href="http://localhost:8084/SubdomainCSSSelector/selectcss?subdomain=${param.subdomain}" type="text/css" rel="stylesheet" />
    </head>
    <body>

    <h1>CSS Subpage Selector</h1>
    
    <div id="test">
        If this block has a yellow border you are on the standard page<br />
        If this block has a green border you are on the green subdomain page<br />
        If this block has a red border you are on the red subdomain page<br />
        If this block has a blue border you are on the blue subdomain page
    </div>
    <div>
        <br /><br />
        Select subdomain below (normally this goes automatically, depending on the subdomain)<br /><br />
        <select onchange="document.location = 'http://localhost:8084/SubdomainCSSSelector/?subdomain=' + this[this.selectedIndex].value">
            <option value="-1" selected="selected">---</option>
            <option value="">Yellow (Standard)</option>
            <option value="green">Green (Subdomain)</option>
            <option value="red">Red (Subdomain)</option>
            <option value="blue">Blue (Subdomain)</option>
        </select>
    </div>
    
    </body>
</html>
