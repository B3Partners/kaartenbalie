<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="vars">
            <h1>Periode</h1>
            <label>Van :</label><xsl:value-of select="period/start"/><br/>
            <label>Tot :</label><xsl:value-of select="period/end"/><br/>
            <h1>Organisatie:</h1>
            
            <xsl:apply-templates select="company"/>
            <label>Naam :</label><xsl:value-of select="company/name"/><br/>			
	</xsl:template>
</xsl:stylesheet>

