<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:include href="C:\dev_erik\Kaartenbalie\web\js\metadataEditor\preprocessors\metadataEditPreprocessorTemplates.xsl"/>
	<xsl:template match="/root/MD_Metadata/identificationInfo/MD_DataIdentification/pointOfContact">
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
