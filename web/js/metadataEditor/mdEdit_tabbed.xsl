<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gfc="http://www.isotc211.org/2005/gfc" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" exclude-result-prefixes="gmd gco">
	<!-- This parameter must be set by the browser -->
	<xsl:param name="basePath"/>
	<xsl:param name="readonly" select="'false'"/>
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
				<div>
					<table id="menu" width="300" height="30" cellspacing="0" cellpadding="0">
						<tr>
							<td id="overzicht-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor een overzicht van de dataset">Overzicht</td>
							<td id="specificaties-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de specificaties van de dataset">Specificaties</td>
							<td id="attributen-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de attributen van de dataset">Attributen</td>
						</tr>
					</table>
				</div>
				<xsl:variable name="background-class-name">
					<xsl:call-template name="get-background-class-name"/>
				</xsl:variable>
				<div id="properties-box" class="{$background-class-name}">
					<xsl:call-template name="elements"/>
				</div>
			</div>
			<div class="hidden">
				<xsl:call-template name="picklists"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template name="elements">
		<xsl:for-each select="//gmd:MD_Metadata">
			<div id="overzicht" class="tab-definition" style="display:block">
				<div class="section">
					<xsl:call-template name="section-title">
						<xsl:with-param name="title" select="'Overzicht'"/>
						<xsl:with-param name="expandable" select="'false'"/>
					</xsl:call-template>
					<div class="section-content">
						<!-- Titel van de bron ISO 360 (groter lettertype, gecentreerd)  -->
						<!-- ISO 360 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.title-->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title"/>
						<!-- Alternatieve titel ISO 361 (normaal lettertype, gecentreerd) -->
						<!-- ISO 361 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.alternateTitle-->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:alternateTitle"/>
						<!-- Thumbnail  


-->
						<!-- Samenvatting ISO 25  -->
						<!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract"/>
						<!-- Doel van vervaardiging ISO 26  -->
						<!-- ISO 26 Doel van de vervaardiging MD_Metadata.identificationInfo>MD_DataIdentification.purpose -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:purpose"/>
						<!-- Algemene beschrijving herkomst  ISO 83  -->
						<!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
						<xsl:apply-templates select="gmd:dataQualityInfo//gmd:lineage//gmd:statement"/>
						<!-- Toepassingsschaal ISO 57  -->
						<!-- ISO 57 Toepassingsschaal 
						MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator"/>
						<!-- Status ISO 28  -->
						<!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status"/>
						<!-- Trefwoorden ISO 53 (mogen kommagescheiden weergegeven worden)  -->
						<!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
						<!-- ISO 55 MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.thesaurusName>CI_Citation.title -->
						<!-- ISO 394  + 395 MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.thesaurusName>CI_Citation.date>CI_Date.date -->
						<xsl:call-template name="keywordsThesaurus"/>
						<!-- Onderwerp ISO 41  -->
						<!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory"/>
						<!-- Naam en URL Aanvullende documentatie ISO 274 (ISO 397, 398, 400 en 402) (klikbaar. Vooral gebruikt voor links naar websites en documenten op ons lokale netwerk)  -->
						<!-- ISO 397 URL MD_Metadata.distributionInfo>MD_Distribution.transferOptions>MD_DigitalTransferOptions.online>CI_OnlineResource.linkage-->
						<!-- ISO 398 Protocol MD_Metadata.distributionInfo>MD_Distribution.transferOptions>MD_DigitalTransferOptions.online>CI_OnlineResource.protocol:  codelijst SV_ServiceType -->
						<!-- ISO 400 Naam  MD_Metadata.distributionInfo>MD_Distribution.transferOptions>MD_DigitalTransferOptions.online>CI_OnlineResource.name -->
						<xsl:apply-templates select="gmd:distributionInfo//gmd:transferOptions"/>
						<!-- Verantwoordelijke organisatie bron ISO 29, daaronder ingesprongen: -->         
<!-- Overige info uit blok Verantwoordelijke organisatie bron (inklapbaar)  -->
						<!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
						<!-- ISO 397 URL organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.contactInfo>CI_Contract.onlineResource>CI_OnlineResource.linkage-->
						<!-- ISO 379 Rol organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5)-->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact"/>
						<!-- Verantwoordelijke organisatie metadata ISO 8, daaronder ingesprongen: -->         
<!-- Overige info uit blok Verantwoordelijke organisatie metadata (inklapbaar)  -->
						<!-- ISO 376 Naam organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.organisationName -->
						<!-- ISO 397 URL metadata organisatie MD_Metadata.contact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage-->
						<!-- ISO 379 Rol organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
						<xsl:apply-templates select="gmd:contact"/>
						<!-- Gebruiksbeperkingen ISO 68  -->
						<!-- Juridische toegangsrestricties ISO 70 -->
						<!-- Veiligheidsrestricties ISO 74  -->
						<!-- Overige beperkingen ISO 72  -->
						<div class="section">
							<xsl:call-template name="section-title">
								<xsl:with-param name="title">Beperkingen</xsl:with-param>
							</xsl:call-template>
							<div class="section-content">
								<!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessConstraints Codelijst MD_RestrictionCode (B.5.24)-->
								<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:accessConstraints"/>
								<!-- ISO 72 Overige beperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.otherConstraints-->
								<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_LegalConstraints/gmd:otherConstraints"/>
								<!-- ISO 74 Veiligheidsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_SecurityConstraints.classification Codelijst MD_ClassificationCode (B.5.11)-->
								<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_SecurityConstraints/gmd:classification"/>
								<!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
								<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="specificaties" class="tab-definition" style="display:block">
				<div class="section">
					<xsl:call-template name="section-title">
						<xsl:with-param name="title" select="'Specificaties'"/>
						<xsl:with-param name="expandable" select="'false'"/>
					</xsl:call-template>
					<div class="section-content">
						<!-- Projectie ISO 207   -->
						<!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
						<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code"/>
						<!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace-->
						<xsl:apply-templates select="gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:codeSpace"/>
						<!-- Extent ISO 344-347  -->
						<!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
						<!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
						<!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
						<!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
						<!-- Als het goed is, is er maar één sectie hiervan. Maar als er meerdere zijn printen we ze op een mooie manier -->
						<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox">
							<xsl:call-template name="omgrenzendeRechthoek"/>
						</xsl:for-each>
						<!-- Temporele dekking ISO 351   -->
						<!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
						<xsl:for-each select="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent">
							<xsl:call-template name="temporeleDekking"/>
						</xsl:for-each>
						<!-- Overig aanwezige contacts wanneer aanwezig zoals distributeur ISO 272 + overige info uit bijbehorend blok contactinfo (inklapbaar)   -->
						<!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
						<!-- ISO 397 URL organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage -->
						<!-- ISO 379 Rol organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) - - >
					<xsl:apply-templates select="gmd:distributionInfo//gmd:distributor"/> -->
						<!-- Daaronder de overige verplichte velden in de volgorde die voor jullie makkelijk is.  -->
						<!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
						<xsl:apply-templates select="gmd:hierarchyLevel"/>
						<!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName 
					<xsl:apply-templates select="gmd:hierarchyLevelName"/> -->
						<!-- ISO 365 MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.identifier>MD_Identifier.code -->
						<xsl:apply-templates select="gmd:identificationInfo//gmd:citation//gmd:identifier/gmd:MD_Identifier/gmd:code"/>
						<!-- ISO 39 Dataset taal MD_Metadata.identificationInfo>MD_DataIdentification.language ISO 639-2 -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language"/>
						<!-- ISO 40 Dataset karakterset MD_Metadata.identificationInfo>MD_DataIdentification.characterSet Codelijst: MD_CharacterSetCode (B.5.10) - - >
					<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet"/> -->
						<!-- ISO 394 Dataset referentie datum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.date-->
						<!-- ISO 395 Creatie-, publicatie-, of wijzigingsdatum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2)-->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date"/>
						<!-- ISO 139 Niveau kwaliteitsbeschrijving 
						MD_Metadata.dataQualityInfo>DQ_DataQuality.scope>DQ_Scope.level -->
						<xsl:apply-templates select="gmd:dataQualityInfo//gmd:scope//gmd:level"/>
						<!-- ISO 61 Resolutie
						 MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.distance -->
						<xsl:apply-templates select="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution/gmd:MD_Resolution/gmd:distance"/>
						<!-- ISO 37 Ruimtelijk schema MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
						<xsl:apply-templates select="gmd:identificationInfo//gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode"/>
						<div class="section">
							<xsl:call-template name="section-title">
								<xsl:with-param name="title">Specificatie kwaliteit</xsl:with-param>
							</xsl:call-template>
							<div class="section-content">
								<!-- ISO 132 Conformiteitsindicatie met de specificatie 
						MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.pass -->
								<xsl:apply-templates select="gmd:dataQualityInfo//gmd:report//gmd:result//gmd:pass"/>
								<!-- ISO 131 Verklaring 
						MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.explanation -->
								<xsl:apply-templates select="gmd:dataQualityInfo//gmd:report//gmd:result//gmd:explanation"/>
								<!-- ISO 130 Specificatie 
						MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.specification>CI_Citation.title -->
								<xsl:apply-templates select="gmd:dataQualityInfo//gmd:report//gmd:result//gmd:specification//gmd:title"/>
								<!-- ISO 394 + 395 Specificatie datum 
				MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.specification>CI_Citation.date>CI_Date -->
								<xsl:apply-templates select="gmd:dataQualityInfo//gmd:report//gmd:result//gmd:specification//gmd:date/gmd:CI_Date"/>
							</div>
						</div>
						<!-- ISO 2 Metadata ID MD_Metadata.fileIdentifier -->
						<xsl:apply-templates select="gmd:fileIdentifier"/>
						<!-- ISO 5 Parent ID MD_Metadata.parentIdentifier -->
						<xsl:apply-templates select="gmd:parentIdentifier"/>
						<!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
						<xsl:apply-templates select="gmd:dateStamp"/>
						<!-- ISO 3 Metadata taal MD_Metadata.language -->
						<xsl:apply-templates select="gmd:language"/>
						<!-- ISO 4 Metadata karakterset MD_Metadata.characterSet Codelijst: MD_CharacterSetCode (B.5.10) - - >
					<xsl:apply-templates select="gmd:characterSet"/> -->
						<!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
						<xsl:apply-templates select="gmd:metadataStandardName"/>
						<!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
						<xsl:apply-templates select="gmd:metadataStandardVersion"/>
					</div>
				</div>
			</div>
		</xsl:for-each>
		<xsl:for-each select="//gfc:FC_FeatureCatalogue">
			<div id="attributen" class="tab-definition" style="display:block">
			<!-- Attribuutnaam en attribuutdefinitie op dezelfde wijze als in de huidige stylesheet. Opnemen in xml volgens hoofdstuk 8 van het Nederlands profiel. -->
			<div class="section">
				<xsl:call-template name="section-title">
						<xsl:with-param name="title" select="'Attributen'"/>
						<xsl:with-param name="expandable" select="'false'"/>
				</xsl:call-template>
				<div class="section-content">
					<!-- ISO 19110 elementen -->
					<!-- B1.1 name for this feature catalogue -->
					<xsl:apply-templates select="gmx:name"/>
					<!-- B1.2 subject domain(s) of feature types defined in this feature catalogue -->
					<xsl:apply-templates select="gmx:scope"/>
					<!-- B1.4 version number of this feature catalogue, which may include both a major version number or letter and 
						a sequence of minor release numbers or letters, such as “3.2.4a.” The format of this attribute may differ between cataloguing authorities.  -->
					<xsl:apply-templates select="gmx:versionNumber"/>
					<!-- B1.5 effective date of this feature catalogue. -->
					<xsl:apply-templates select="gmx:versionDate"/>
					<!-- B1.6 producer -->
					<xsl:apply-templates select="gfc:producer"/>
					<!-- B1.8 feature types -->
					<xsl:apply-templates select="gfc:featureType"/>
				</div>
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
		<!-- MD_ClassificationCode (B.5.11) -->
		<xsl:call-template name="picklist_MD_ClassificationCode"/>
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
		<!-- SV_ServiceType: eigen lijst -->
		<xsl:call-template name="picklist_SV_ServiceType"/>
		<!-- unused
		<xsl:call-template name="picklist_CI_OnLineFunctionCode"/>
		<xsl:call-template name="picklist_CI_PresentationFormCode"/>
		<xsl:call-template name="picklist_DQ_EvaluationMethodTypeCode"/>
		<xsl:call-template name="picklist_DS_AssociationTypeCode"/>
		<xsl:call-template name="picklist_DS_InitiativeTypeCode"/>
		<xsl:call-template name="picklist_MD_CellGeometryCode"/>
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
	<!-- TODO plaatjes en xpath voor selectie goed zetten -->
	<xsl:template name="get-background-class-name">
		<xsl:choose>
			<xsl:when test="/metadata/dataset_description/geokey_database/description[. = 'basisdata Nederland']">
				<xsl:text>achtergrond-nederland</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
