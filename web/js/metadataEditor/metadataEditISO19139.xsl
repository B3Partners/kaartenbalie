<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" exclude-result-prefixes="gmd gco">
	<!-- This parameter must be set by the browser -->
	<xsl:param name="basePath"/>
	<!-- template library to use for making element editable -->
	<xsl:include href="picklists/metadataPicklists.xsl"/>
	<xsl:include href="includes/editableCore.xsl"/>
	<xsl:output doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN" method="xml" omit-xml-declaration="no" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	Adapted: Chris van Lith

	Beschrijving stylesheet:
	In het algemeen geldt dat voor elke property (waarde/xml-node/xml-tag) een apart template is gemaakt.
	-->
	<xsl:template match="/">
		<xsl:call-template name="theMetadata"/>
	</xsl:template>
	<xsl:template match="node()[@xlink:href]">
		<xsl:apply-templates select="id(@xlink:href)"/>
	</xsl:template>
	<xsl:template name="theMetadata">
		<div id="edit-doc-root" changed="false">
			<div id="properties-box">
				<xsl:call-template name="elements"/>
			</div>
			<div class="hidden">
				<xsl:call-template name="picklists"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template name="elements">
		<xsl:for-each select="//gmd:MD_Metadata">
			<!-- ISO 2 Metadata ID MD_Metadata.fileIdentifier -->
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Metadata: taal, karakterset en hierarchie</xsl:with-param>
				</xsl:call-template>
				<div class="section-content">
					<!-- ISO 3 Metadata taal MD_Metadata.language -->
					<xsl:apply-templates select="gmd:language"/>
					<!-- ISO 4 Metadata karakterset MD_Metadata.characterSet Codelijst: MD_CharacterSetCode (B.5.10)-->
					<xsl:apply-templates select="gmd:characterSet"/>
					<!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
					<xsl:apply-templates select="gmd:dataQualityInfo//gmd:scope//gmd:level"/>
					<!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName -->
					<xsl:apply-templates select="gmd:hierarchyLevelName"/>
				</div>
			</div>
			<!-- ISO 376 Naam organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.organisationName -->
			<!-- ISO 397 URL metadata organisatie MD_Metadata.contact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage-->
			<!-- ISO 379 Rol organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
			<xsl:apply-templates select="/*/*/gmd:contact"/>
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Metadata: datum, standaard en referentie</xsl:with-param>
				</xsl:call-template>
				<div class="section-content">
					<!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
					<xsl:apply-templates select="gmd:dateStamp"/>
					<!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
					<xsl:apply-templates select="gmd:metadataStandardName"/>
					<!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
					<xsl:apply-templates select="gmd:metadataStandardVersion"/>
					<!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
					<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code"/>
					<!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace-->
					<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:codeSpace"/>
				</div>
			</div>
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Dataset</xsl:with-param>
				</xsl:call-template>
				<div class="section-content">
					<!-- ISO 360 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.title-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title"/>
					<!-- ISO 394 Dataset referentie datum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.date-->
					<!-- ISO 395 Creatie-, publicatie-, of wijzigingsdatum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date"/>
					<!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract"/>
					<!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status"/>
					<!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
					<!-- ISO 397 URL organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.contactInfo>CI_Contract.onlineResource>CI_OnlineResource.linkage-->
					<!-- ISO 379 Rol organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact"/>
					<!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
					<xsl:apply-templates select="gmd:identificationInfo//gmd:descriptiveKeywords//gmd:keyword"/>
					<!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation"/>
					<!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessConstraints Codelijst MD_RestrictionCode (B.5.24)-->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:accessConstraints"/>
					<!-- ISO 37 Ruimtelijk schema MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
					<xsl:apply-templates select="gmd:identificationInfo//gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode"/>
					<!-- ISO 57 Toepassingsschaal MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator"/>
					<!-- ISO 39 Dataset taal MD_Metadata.identificationInfo>MD_DataIdentification.language ISO 639-2 -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language"/>
					<!-- ISO 40 Dataset karakterset MD_Metadata.identificationInfo>MD_DataIdentification.characterSet Codelijst: MD_CharacterSetCode (B.5.10) -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet"/>
					<!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory"/>
					<!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
					<!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
					<!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
					<!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
					<!-- Als het goed is, is er maar één sectie hiervan. Maar als er meerdere zijn printen we ze op een mooie manier -->
					<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox">
						<xsl:call-template name="omgrenzendeRechthoek"/>
					</xsl:for-each>
					<!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
					<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent">
						<xsl:call-template name="temporeleDekking"/>
					</xsl:for-each>
					<!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
					<!-- ISO 397 URL organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage -->
					<!-- ISO 379 Rol organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
					<xsl:apply-templates select="gmd:distributionInfo//gmd:distributor"/>
					<!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
					<xsl:apply-templates select="gmd:dataQualityInfo//gmd:lineage//gmd:statement"/>
				</div>
			</div>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="picklists">
		<!-- CI_DateTypeCode (B.5.2)-->
		<xsl:call-template name="picklist_CI_DateTypeCode"/>
		<!-- CI_RoleCode (B.5.5)-->
		<xsl:call-template name="picklist_CI_RoleCode"/>
		<!-- MD_CharacterSetCode (B.5.10) -->
		<xsl:call-template name="picklist_MD_CharacterSetCode"/>
		<!-- MD_ProgressCode (B.5.23)-->
		<xsl:call-template name="picklist_MD_ProgressCode"/>
		<!-- MD_RestrictionCode (B.5.24)-->
		<xsl:call-template name="picklist_MD_RestrictionCode"/>
		<!-- MD_ScopeCode (B.5.25) -->
		<xsl:call-template name="picklist_MD_ScopeCode"/>
		<!-- MD_SpatialRepresentation TypeCode (B.5.26) -->
		<xsl:call-template name="picklist_MD_SpatialRepresentationTypeCode"/>
		<!-- MD_TopicCategoryCode (B.5.27) -->
		<xsl:call-template name="picklist_MD_TopicCategoryCode"/>
		<!-- languageCode: eigen lijst -->
		<xsl:call-template name="picklist_LanguageCode"/>
		<!-- unused
		<xsl:call-template name="picklist_CI_OnLineFunctionCode"/>
		<xsl:call-template name="picklist_CI_PresentationFormCode"/>
		<xsl:call-template name="picklist_DQ_EvaluationMethodTypeCode"/>
		<xsl:call-template name="picklist_DS_AssociationTypeCode"/>
		<xsl:call-template name="picklist_DS_InitiativeTypeCode"/>
		<xsl:call-template name="picklist_MD_CellGeometryCode"/>
		<xsl:call-template name="picklist_MD_ClassificationCode"/>
		<xsl:call-template name="picklist_MD_CoverageContentTypeCode"/>
		<xsl:call-template name="picklist_MD_DatatypeCode"/>
		<xsl:call-template name="picklist_MD_DimensionNameTypeCode"/>
		<xsl:call-template name="picklist_MD_GeometricObjectTypeCode"/>
		<xsl:call-template name="picklist_MD_ImagingConditionCode"/>
		<xsl:call-template name="picklist_MD_KeywordTypeCode"/>
		<xsl:call-template name="picklist_MD_MaintenanceFrequencyCode"/>
		<xsl:call-template name="picklist_MD_MediumFormatCode"/>
		<xsl:call-template name="picklist_MD_MediumNameCode"/>
		<xsl:call-template name="picklist_MD_ObligationCode"/>
		<xsl:call-template name="picklist_MD_PixelOrientationCode"/>
		<xsl:call-template name="picklist_MD_TopologyLevelCode"/>
		-->
	</xsl:template>
</xsl:stylesheet>
