<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	
	<xsl:template match="data">
		<fieldset id="data">
			<legend>Results</legend>
			<xsl:apply-templates select="details"/>
		</fieldset>

	</xsl:template>
	

	<xsl:include href="data_details.xsl"/>
</xsl:stylesheet>

