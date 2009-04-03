<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gfc="http://www.isotc211.org/2005/gfc" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml">
	<!--
						exclude-result-prefixes="gmd"
	-->
	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Chris van Lith
	
	Beschrijving stylesheet:
	Bevat defaults voor nieuwe metadata tags en paden naar esri metadata tbv synchroniser.
	-->
	<!-- do we want to synchronise with esri tags, templates must be prepared for this-->
	<xsl:variable name="SYNCHRONISE_ESRI">false</xsl:variable>
	<!-- default templates -->
	<xsl:template name="add-fileIdentifier-default">
		<xsl:element name="gco:CharacterString">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-MetaID-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>nld</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-dateStamp-default">
		<xsl:element name="gco:Date">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="dateformat">
						<xsl:with-param name="date">
							<xsl:call-template name="add-ModDate-esri"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-language-default">
		<xsl:element name="gco:CharacterString">nld</xsl:element>
	</xsl:template>
	<xsl:template name="add-metadataStandardName-default">
		<xsl:element name="gco:CharacterString">ISO 19115:2003</xsl:element>
	</xsl:template>
	<xsl:template name="add-metadataStandardVersion-default">
		<xsl:element name="gco:CharacterString">Nederlandse metadatastandaard voor geografie 1.2</xsl:element>
	</xsl:template>
	<xsl:template name="add-code-default">
		<xsl:element name="gco:CharacterString">28992</xsl:element>
	</xsl:template>
	<xsl:template name="add-codeSpace-default">
		<xsl:element name="gco:CharacterString">EPSG</xsl:element>
	</xsl:template>
	<xsl:template name="add-citation-title-default">
		<xsl:element name="gco:CharacterString">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-resTitle-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Geen titel</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-westBoundLongitude-default">
		<xsl:element name="gco:Decimal">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-westBL-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>3.3</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-eastBoundLongitude-default">
		<xsl:element name="gco:Decimal">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-eastBL-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>7.2</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-southBoundLatitude-default">
		<xsl:element name="gco:Decimal">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-southBL-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>50.5</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-northBoundLatitude-default">
		<xsl:element name="gco:Decimal">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-northBL-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>53.5</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_CharacterSetCode-default">
		<xsl:attribute name="codeListValue"><xsl:value-of select="'utf8'"/></xsl:attribute>
		<xsl:value-of select="'utf8'"/>
	</xsl:template>
	<xsl:template name="add-MD_ProgressCode-default">
		<xsl:attribute name="codeListValue"><xsl:value-of select="'completed'"/></xsl:attribute>
		<xsl:value-of select="'compleet'"/>
	</xsl:template>
	<xsl:template name="add-MD_ScopeCode-default">
		<xsl:attribute name="codeList">#MD_ScopeCode</xsl:attribute>
		<xsl:attribute name="codeListValue"><xsl:value-of select="'dataset'"/></xsl:attribute>
		<xsl:value-of select="'dataset'"/>
	</xsl:template>
	<xsl:template name="add-transferOptions-linkage-default">
		<xsl:element name="gmd:URL">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-linkage-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-transferOptions-protocol-default">
		<xsl:element name="gmd:SV_ServiceType">
			<xsl:attribute name="codeList">#SV_ServiceType</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:attribute name="codeListValue"><xsl:value-of select="'download'"/></xsl:attribute>
					<xsl:value-of select="'download'"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="codeListValue"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-transferOptions-name-default">
		<xsl:element name="gco:CharacterString">
			<xsl:choose>
				<xsl:when test="$SYNCHRONISE_ESRI">
					<xsl:call-template name="add-resTitle-esri"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Onbekend</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!-- paden naar esri gegevens tbv synchroniser -->
	<xsl:template name="add-MetaID-esri">
		<xsl:value-of select="/metadata/Esri/MetaID"/>
		<!-- {B518C789-937A-4552-B1CA-2D2CC230C719} -->
	</xsl:template>
	<xsl:template name="add-CreaDate-esri">
		<xsl:value-of select="/metadata/Esri/CreaDate"/>
		<!-- 20090331 -->
	</xsl:template>
	<xsl:template name="add-CreaTime-esri">
		<xsl:value-of select="/metadata/Esri/CreaTime"/>
		<!-- 10294700 -->
	</xsl:template>
	<xsl:template name="add-ModDate-esri">
		<xsl:value-of select="/metadata/Esri/ModDate"/>
		<!-- 20090331 -->
	</xsl:template>
	<xsl:template name="add-ModTime-esri">
		<xsl:value-of select="/metadata/Esri/ModTime"/>
		<!-- 10294700 -->
	</xsl:template>
	<xsl:template name="add-resTitle-esri">
		<xsl:value-of select="/metadata/dataIdInfo/idCitation/resTitle"/>
		<!-- test_mde -->
	</xsl:template>
	<xsl:template name="add-GeoBndBox-westBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/westBL"/>
		<!-- 77236 -->
	</xsl:template>
	<xsl:template name="add-GeoBndBox-eastBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/eastBL"/>
		<!-- 140441 -->
	</xsl:template>
	<xsl:template name="add-GeoBndBox-northBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/northBL"/>
		<!-- 426024 -->
	</xsl:template>
	<xsl:template name="add-GeoBndBox-southBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/dataExt/geoEle/GeoBndBox/southBL"/>
		<!-- 381457 -->
	</xsl:template>
	<xsl:template name="add-westBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/geoBox/westBL"/>
		<!-- 4.25958 -->
	</xsl:template>
	<xsl:template name="add-eastBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/geoBox/eastBL"/>
		<!-- 5.178165 -->
	</xsl:template>
	<xsl:template name="add-northBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/geoBox/northBL"/>
		<!-- 51.823564 -->
	</xsl:template>
	<xsl:template name="add-southBL-esri">
		<xsl:value-of select="/metadata/dataIdInfo/geoBox/southBL"/>
		<!-- 51.417794 -->
	</xsl:template>
	<xsl:template name="add-mdHrLvName-esri">
		<xsl:value-of select="/metadata/mdHrLvName"/>
		<!-- dataset -->
	</xsl:template>
	<xsl:template name="add-linkage-esri">
		<xsl:value-of select="/metadata/distInfo/distributor/distorTran/onLineSrc/linkage"/>
		<!-- file://\\GWLT2\D$\projdata\b3p_mde\testdata\test_mde.shp -->
	</xsl:template>
	<xsl:template name="add-protocol-esri">
		<xsl:value-of select="/metadata/distInfo/distributor/distorTran/onLineSrc/protocol"/>
		<!-- Local Area Network -->
	</xsl:template>
	<xsl:template name="add-natvform-esri">
		<xsl:value-of select="/metadata/idinfo/natvform"/>
		<!-- Shapefile -->
	</xsl:template>
	<xsl:template name="dateformat">
		<xsl:param name="date"/>
		<xsl:if test="string-length($date) &gt; 0">
			<xsl:value-of select="substring($date,1,4)"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="substring($date,5,2)"/>
			<xsl:text>-</xsl:text>
			<xsl:value-of select="substring($date,7,2)"/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
