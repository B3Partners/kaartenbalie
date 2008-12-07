<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" exclude-result-prefixes="gmd gco">
	<xsl:output version="1.0" encoding="ISO-8859-1" doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN" omit-xml-declaration="yes" indent="yes" media-type="text/html"/>
	<xsl:template match="/">
		<xsl:for-each select="//gmd:MD_Metadata">
			<html>
				<head>
					<title>
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title"/>
					</title>
				</head>
				<body>
					<!-- ISO 3 Metadata taal MD_Metadata.language -->
					<xsl:apply-templates select="gmd:language"/>
					<br/>
					<!-- ISO 4 Metadata karakterset MD_Metadata.characterSet Codelijst: MD_CharacterSetCode (B.5.10)-->
					<xsl:apply-templates select="gmd:characterSet"/>
					<br/>
					<!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
					<xsl:apply-templates select="gmd:dataQualityInfo//gmd:scope//gmd:level"/>
					<br/>
					<!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName -->
					<xsl:apply-templates select="gmd:hierarchyLevelName"/>
					<br/>
					<!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
					<xsl:apply-templates select="gmd:dateStamp"/>
					<br/>
					<!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
					<xsl:apply-templates select="gmd:metadataStandardName"/>
					<br/>
					<!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
					<xsl:apply-templates select="gmd:metadataStandardVersion"/>
					<br/>
					<!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
					<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code"/>
					<br/>
					<!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace-->
					<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:codeSpace"/>
					<br/>
					<!-- ISO 360 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.title-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title"/>
					<br/>
					<!-- ISO 394 Dataset referentie datum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.date-->
					<!-- ISO 395 Creatie-, publicatie-, of wijzigingsdatum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date"/>
					<br/>
					<!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract"/>
					<br/>
					<!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status"/>
					<br/>
					<!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
					<!-- ISO 397 URL organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.contactInfo>CI_Contract.onlineResource>CI_OnlineResource.linkage-->
					<!-- ISO 379 Rol organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact"/>
					<br/>
					<!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
					<xsl:apply-templates select="gmd:identificationInfo//gmd:descriptiveKeywords//gmd:keyword"/>
					<br/>
					<!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation"/>
					<br/>
					<!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessConstraints Codelijst MD_RestrictionCode (B.5.24)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:accessConstraints"/>
					<br/>
					<!-- ISO 37 Ruimtelijk schema MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
					<xsl:apply-templates select="gmd:identificationInfo//gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode"/>
					<br/>
					<!-- ISO 57 Toepassingsschaal MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator"/>
					<br/>
					<!-- ISO 39 Dataset taal MD_Metadata.identificationInfo>MD_DataIdentification.language ISO 639-2 -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language"/>
					<br/>
					<!-- ISO 40 Dataset karakterset MD_Metadata.identificationInfo>MD_DataIdentification.characterSet Codelijst: MD_CharacterSetCode (B.5.10) -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet"/>
					<br/>
					<!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory"/>
					<br/>
					<!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
					<!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
					<!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
					<!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
					<!-- Als het goed is, is er maar één sectie hiervan. Maar als er meerdere zijn printen we ze op een mooie manier -->
					<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox">
						<xsl:call-template name="omgrenzendeRechthoek"/>
					</xsl:for-each>
					<br/>
					<!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
					<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent">
						<xsl:call-template name="temporeleDekking"/>
					</xsl:for-each>
					<br/>
					<!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
					<!-- ISO 397 URL organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage -->
					<!-- ISO 379 Rol organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
					<xsl:apply-templates select="gmd:distributionInfo//gmd:distributor"/>
					<br/>
					<!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
					<xsl:apply-templates select="gmd:dataQualityInfo//gmd:lineage//gmd:statement"/>
					<br/>
				</body>
			</html>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="omgrenzendeRechthoek">
		<strong>Omgrenzende rechthoek</strong>
		<table>
				<tr>
					<td><xsl:apply-templates select="gmd:westBoundLongitude/gco:Decimal"/></td>
					<td><xsl:apply-templates select="gmd:eastBoundLongitude/gco:Decimal"/></td>
					<td><xsl:apply-templates select="gmd:southBoundLatitude/gco:Decimal"/></td>
					<td><xsl:apply-templates select="gmd:northBoundLatitude/gco:Decimal"/></td>
				</tr>
		</table>
	</xsl:template>
	<xsl:template name="temporeleDekking">
		<strong>Temporele dekking</strong>
		<table>
				<tr>
					<td><xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition"/></td>
					<td><xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition"/></td>
				</tr>
		</table>
	</xsl:template>
	<xsl:template match="gco:TypeName">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:MemberName">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Measure">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Length">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Angle">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Scale">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Distance">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:CharacterString">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Boolean">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:LocalName">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:ScopedName">
		<xsl:value-of select="."/>
	</xsl:template>
	<!-- Numeric types -->
	<xsl:template match="gco:Date">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Decimal">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Real">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Integer">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:UnlimitedInteger">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Record">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:RecordType">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gco:Binary">
		<xsl:value-of select="."/>
	</xsl:template>
	<xsl:template match="gml:timePosition">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
