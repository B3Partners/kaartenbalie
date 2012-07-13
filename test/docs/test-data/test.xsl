<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="string">
		<span style="font-weight:bold">
		    <xsl:apply-templates/>
		</span>
	</xsl:template>
</xsl:stylesheet>
