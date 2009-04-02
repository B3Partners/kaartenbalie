<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:include href="usage.xsl"/>
	
	<xsl:template match="/">
		<xsl:call-template name="start">
			<xsl:with-param name="service">WFS</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
