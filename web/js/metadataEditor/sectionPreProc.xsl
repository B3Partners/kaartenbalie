<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gmd="http://www.isotc211.org/2005/gmd" exclude-result-prefixes="gmd">
	<xsl:output method="xml" indent="yes"/>
	<xsl:include href="preprocessors/metadataEditPreprocessorTemplates.xsl"/>
	<xsl:template match="gmd:MD_Metadata/gmd:contact">
		<xsl:copy>
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
