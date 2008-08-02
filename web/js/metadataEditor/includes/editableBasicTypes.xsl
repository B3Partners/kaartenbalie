<?xml version="1.0" encoding="UTF-8"?>
<!--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet 
    version="1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    >
    <!-- EvdP: geeft basic types weer van ISO 19139. Doet nog niks fancy ermee. -->
    <xsl:template match="gco:TypeName">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:MemberName">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Measure">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Length">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Angle">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Scale">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Distance">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:CharacterString">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Boolean">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:LocalName">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:ScopedName">
        <xsl:value-of select="."/>
    </xsl:template>
	
    <!-- Numeric types -->
    <xsl:template match="gco:Date">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Decimal">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Real">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Integer">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:UnlimitedInteger">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Record">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:RecordType">
        <xsl:value-of select="."/>
    </xsl:template>
    <xsl:template match="gco:Binary">
        <xsl:value-of select="."/>
    </xsl:template>
</xsl:stylesheet>
