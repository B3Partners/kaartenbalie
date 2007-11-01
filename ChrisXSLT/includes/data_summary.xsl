<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

	
	
	<!-- This is were the summing starts -->
	<xsl:template match="summary">
		<fieldset id="summary">
			<legend>Summary</legend>
			<xsl:apply-templates select="ServerTransferOperation"/>
			<xsl:apply-templates select="CombineImagesOperation"/>
			<xsl:apply-templates select="ClientXFerOperation"/>
			<xsl:apply-templates select="RequestOperation"/>
		</fieldset>	
	</xsl:template>

	<xsl:template match="ServerTransferOperation">
		<h1>Server (Multi) Transfer Operation</h1>
		<hr/>
		<label>Server Transfers Operations</label><xsl:value-of select="@hits"/> operations<br/>
		<label>Average Time</label><xsl:value-of select="format-number(averageDuration,$dispMs)"/><br/>
	</xsl:template>

	
	<xsl:template match="CombineImagesOperation">
		<h1>Combine Images Operation</h1>
		<hr/>
		<label>Images Combined</label><xsl:value-of select="@hits"/> operations<br/>
		<label>Average Time</label><xsl:value-of select="format-number(averageDuration,$dispMs)"/><br/>
	</xsl:template>
	
	<xsl:template match="ClientXFerOperation">
		<h1>Client DataTransfer Operation</h1>
		<hr/>

		<label>Total Transfers</label><xsl:value-of select="@hits"/> transfers<br/>
		<label>Data Transfered</label><xsl:apply-templates select="dataSize"/><br/>
		<label>Average ResponseTime</label><xsl:value-of select="format-number(averageDuration,$dispMs)"/>/transfer<br/>
		<xsl:variable name="avDownloadKiloBytes"><xsl:value-of select="(dataSize div @hits) div 1024"/></xsl:variable>
		<label>Average DL Size</label><xsl:value-of select="format-number($avDownloadKiloBytes,$dispKb)"/><br/>

		<xsl:variable name="kbms"><xsl:value-of select="($avDownloadKiloBytes div averageDuration) * 1000"/></xsl:variable>
		<label>Average DL Speed</label><xsl:value-of select="format-number($kbms,$dispKb)"/>/s<br/>
	</xsl:template>
	
	<xsl:template match="dataSize">
		<xsl:value-of select="format-number(. * $bToMb,$dispMb)"/>
	</xsl:template>
	
	
	<xsl:template match="RequestOperation">
		<h1>Request Operation</h1>
		<hr/>
		<label>total Requests</label><xsl:value-of select="@hits"/> requests<br/>		
		<label>total Bytes Received</label><xsl:apply-templates select="bytesReceivedFromUser"/><br/>
		<label>total Bytes Send</label><xsl:apply-templates select="bytesSendToUser"/><br/>
		
		<label>Average Request Duration</label><xsl:value-of select="format-number(averageDuration,$dispMs)"/><br/>
		<xsl:variable name="tXfer"><xsl:value-of select="(bytesSendToUser + bytesReceivedFromUser) "/></xsl:variable>
		<label>total Send/Received</label><xsl:value-of select="format-number($tXfer* $bToMb,$dispMb)"/><br/>
		
		
		<label>Data per session</label><xsl:value-of select="format-number(($tXfer div @hits) * $bToKb,$dispKb)"/>/request<br/>
	</xsl:template>
	
	<xsl:template match="bytesReceivedFromUser">
		
		<xsl:value-of select="format-number(. * $bToMb,$dispMb)"/>
		
	</xsl:template>
	<xsl:template match="bytesSendToUser">
		<xsl:value-of select="format-number(. * $bToMb,$dispMb)"/>
	</xsl:template>

</xsl:stylesheet>

