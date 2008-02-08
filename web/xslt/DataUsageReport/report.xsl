<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:variable name="td_natural">lbz</xsl:variable>
	<xsl:variable name="td_variance">dg</xsl:variable>
	<xsl:variable name="dispMb">0.00 MB</xsl:variable>
	<xsl:variable name="dispKb">0.00 KB</xsl:variable>
	<xsl:variable name="dispMs">0.0 ms</xsl:variable>
	<xsl:variable name="bToKb" select="1 div 1024"/>
	<xsl:variable name="bToMb" select="1 div 1024 div 1024"/>
	
	<xsl:template match="/report">

		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="http://localhost:8084/kaartenbalie/xslt/DataUsageReport/css/b3p_global.css"/>
				<link rel="stylesheet" type="text/css" href="http://localhost:8084/kaartenbalie/xslt/DataUsageReport/css/b3p_report.css"/>
			</head>
			<body>
				<fieldset id="report" >
					<legend>Report</legend>
					<label>id:</label><xsl:value-of select="@id"/><br/>
					<label>date:</label><xsl:value-of select="@date"/><br/>
					<label>ProcessingTime:</label><xsl:value-of select="@processingTime"/>ms<br/>
					<xsl:apply-templates select="vars"/>
					<xsl:apply-templates select="data"/>
				</fieldset>
			</body>
		</html>
	</xsl:template>
	
	<xsl:include href="includes/vars.xsl"/>
	<xsl:include href="includes/data.xsl"/>
</xsl:stylesheet>