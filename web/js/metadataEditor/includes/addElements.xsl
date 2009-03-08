<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xlink="http://www.w3.org/1999/xlink"					
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:gco="http://www.isotc211.org/2005/gco"
					exclude-result-prefixes="gmd gco xlink"
					>
<!--
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Metadata Editor.

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

	<!-- This parameter must be set by the browser -->
	<xsl:param name="basePath"/>
	
	<!-- template library to use for making element editable -->
	<xsl:include href="editableCore.xsl"/>	

	<xsl:output	
					doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
					doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN"
					method="xml" omit-xml-declaration="no" indent="yes"
					/>
					
	<xsl:template match="/">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>
	
	
				
</xsl:stylesheet>