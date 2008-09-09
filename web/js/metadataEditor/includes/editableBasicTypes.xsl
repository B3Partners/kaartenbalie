<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:gco="http://www.isotc211.org/2005/gco"
                                        xmlns:gml="http://www.opengis.net/gml"    

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
        
	<xsl:template match="gml:timePosition">
		<xsl:value-of select="."/>
	</xsl:template>
	
</xsl:stylesheet>
