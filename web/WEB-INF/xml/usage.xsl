<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output version="1.0" encoding="ISO-8859-1" omit-xml-declaration="no" indent="yes" media-type="text/html"/>
	<xsl:decimal-format decimal-separator="," grouping-separator="." name="MyFormat" NaN="&#160;" infinity="&#160;"/>
	<xsl:decimal-format name="CalcFormat" NaN="0"/>
	<xsl:template name="dateformat">
		<xsl:param name="date"/>
		<xsl:value-of select="substring($date,9,2)"/>
		<xsl:value-of select="substring($date,5,4)"/>
		<xsl:value-of select="substring($date,1,4)"/>
	</xsl:template>
	<xsl:variable name="styles">
body {
	margin: 10px;
	padding: 0;
	font-family: Arial, Helvetica, sans-serif;
	color: #000000;
	background-color: #FFFFFF;
   }
   
.pagebreak {
	page-break-before: always;
	color: #000000;
} 

table { 
	empty-cells: show; 
}

td {
	font-size: 12px;
	vertical-align: top;
}
table {
	empty-cells: show;
}
h1 {
	font-size: 22px;
	color: #990033;
	font-weight: bold;
	padding-bottom: 15px;
	padding-top: 0px;
	margin:0;
}
h2 {
	font-size: 16px;
	color: #246780;
	font-weight: bold;
	padding-bottom: 5px;
	padding-top: 10px;
	margin:0;
}
.number {
text-align: right;
white-space: nowrap;
}
	</xsl:variable>
	<xsl:template match="/">
		<html>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
			<head>
				<style>
					<xsl:value-of select="$styles"/>
				</style>
				<title>Kaartenbalie Performance Rapport</title>
			</head>
			<body>
				<h1>Kaartenbalie Performance Rapport</h1>
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="parameters">
		<br/>
		<fieldset id="parameters">
			<legend>Raport metadata</legend>
			<table>
				<tbody>
					<tr>
						<td>Rapport ID</td>
						<td>
							<xsl:value-of select="id"/>
						</td>
					</tr>
					<tr>
						<td>Organizatie</td>
						<td>
							<xsl:value-of select="organization"/>
						</td>
					</tr>
					<tr>
						<td>Rapporttijd</td>
						<td>
							<xsl:value-of select="time-stamp"/>
						</td>
					</tr>
					<tr>
						<td>Productieduur</td>
						<td>
							<xsl:value-of select="processing-time"/>
							<xsl:text> ms</xsl:text>
						</td>
					</tr>
					<tr>
						<td>Begindatum</td>
						<td>
							<xsl:value-of select="date-start"/>
						</td>
					</tr>
					<tr>
						<td>Einddatum</td>
						<td>
							<xsl:value-of select="date-end"/>
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</xsl:template>
	<xsl:template match="request-summary">
		<xsl:for-each select="type-summary">
			<br/>
			<fieldset id="type-summary">
				<legend>Performance samenvatting voor  type: <xsl:value-of select="type"/>
				</legend>
				<table>
					<tbody>
						<tr>
							<td>Aantal requests</td>
							<td>
								<xsl:value-of select="count"/>
							</td>
						</tr>
						<tr>
							<td>Gemiddelde responsetijd</td>
							<td>
								<xsl:value-of select="duration-avg"/>
								<xsl:text> ms</xsl:text>
							</td>
						</tr>
						<tr>
							<td>Maximale responsetijd</td>
							<td>
								<xsl:value-of select="duration-max"/>
								<xsl:text> ms</xsl:text>
							</td>
						</tr>
						<tr>
							<td>Totale grootte plaatjes</td>
							<td>
								<xsl:value-of select="data-size-sum"/>
								<xsl:text> B</xsl:text>
							</td>
						</tr>
						<tr>
							<td>Aantal Bytes ontvangen</td>
							<td>
								<xsl:value-of select="bytes-received-sum"/>
								<xsl:text> B</xsl:text>
							</td>
						</tr>
						<tr>
							<td>Aantal Bytes verzonden</td>
							<td>
								<xsl:value-of select="bytes-sent-sum"/>
								<xsl:text> B</xsl:text>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="service-providers">
		<br/>
		<fieldset id="service-provider">
			<legend>Performance samenvatting per serviceprovider</legend>
			<table>
				<thead>
					<tr>
						<td>Naam</td>
						<td>Aantal requests</td>
						<td>Gemiddelde responsetijd (ms)</td>
						<td>Maximale responsetijd (ms)</td>
						<td>Aantal Bytes ontvangen</td>
						<td>Aantal Bytes verzonden</td>
					</tr>
				</thead>
				<tbody>
					<xsl:for-each select="service-provider">
						<tr>
							<td>
								<xsl:value-of select="name"/>
							</td>
							<td>
								<xsl:value-of select="count"/>
							</td>
							<td>
								<xsl:value-of select="duration-avg"/>
							</td>
							<td>
								<xsl:value-of select="duration-max"/>
							</td>
							<td>
								<xsl:value-of select="bytes-received-sum"/>
							</td>
							<td>
								<xsl:value-of select="bytes-sent-sum"/>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</fieldset>
	</xsl:template>
	<xsl:template match="request-load">
		<br/>
		<fieldset id="hourly-load">
			<legend>Performance per uur</legend>
			<table>
				<thead>
					<tr>
						<td>Datum</td>
						<td>Uur</td>
						<td>Aantal requests</td>
						<td>Gemiddelde responsetijd (ms)</td>
						<td>Maximale responsetijd (ms)</td>
						<td>Aantal Bytes ontvangen</td>
						<td>Aantal Bytes verzonden</td>
					</tr>
				</thead>
				<tbody>
					<xsl:for-each select="hourly-load">
						<tr>
							<td>
								<xsl:value-of select="date"/>
							</td>
							<td>
								<xsl:value-of select="hour"/>
							</td>
							<td>
								<xsl:value-of select="count"/>
							</td>
							<td>
								<xsl:value-of select="duration-avg"/>
							</td>
							<td>
								<xsl:value-of select="duration-max"/>
							</td>
							<td>
								<xsl:value-of select="bytes-received-sum"/>
							</td>
							<td>
								<xsl:value-of select="bytes-sent-sum"/>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</fieldset>
	</xsl:template>
	<xsl:template match="response-frequency">
		<br/>
		<fieldset id="response-time">
			<legend>Response frequentie distributie</legend>
			<table>
				<thead>
					<tr>
						<td>Min. Responsetijd</td>
						<td>Max. Responsetijd</td>
						<td>Aantal requests</td>
					</tr>
				</thead>
				<tbody>
					<xsl:for-each select="response-time[type= 'REQUEST']">
						<tr>
							<td>
								<xsl:value-of select="duration-low"/>
							</td>
							<td>
								<xsl:choose>
									<xsl:when test="number(duration-high) &lt; 0">-</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="duration-high"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								<xsl:value-of select="count"/>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</fieldset>
	</xsl:template>
</xsl:stylesheet>
