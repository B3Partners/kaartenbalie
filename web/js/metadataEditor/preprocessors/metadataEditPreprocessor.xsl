<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gfc="http://www.isotc211.org/2005/gfc" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml">
	<!--
						exclude-result-prefixes="gmd"
	-->
	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	Adapted: Chris van Lith
	
	Beschrijving stylesheet:
	Preprocessor die alle nodes kopieert en ontbrekende nodes toevoegd om de mogelijkheid te bieden deze te editen in de stylesheet.
	De templates voor het toevoegen van nodes bevindt zich in "metadataEditPreprocessorTemplates.xsl".
	-->
	<!--get template that 'knows' how to add elements-->
	<xsl:include href="metadataEditPreprocessorTemplates.xsl"/>
	<!-- default template: copy all nodes and attributes-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="metadata">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Metadata)">
					<xsl:call-template name="add-MD_Metadata"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="gmd:MD_Metadata"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:FC_FeatureCatalogue)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_FC_FeatureCatalogue"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:FC_FeatureCatalogue"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Metadata) 
                                 and not(self::gfc:FC_FeatureCatalogue) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Metadata">
		<xsl:copy>
			<!-- ISO 2 Metadata ID MD_Metadata.fileIdentifier -->
			<xsl:choose>
				<xsl:when test="not(gmd:fileIdentifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-fileIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:fileIdentifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 3 Metadata taal MD_Metadata.language -->
			<xsl:choose>
				<xsl:when test="not(gmd:language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:language"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 4 Metadata karakterset MD_Metadata.characterSet Codelijst: MD_CharacterSetCode (B.5.10)-->
			<xsl:choose>
				<xsl:when test="not(gmd:characterSet)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-characterSet"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:characterSet"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 5 Metadata ID MD_Metadata.parentIdentifier -->
			<xsl:choose>
				<xsl:when test="not(gmd:fileIdentifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-parentIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:parentIdentifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
			<xsl:choose>
				<xsl:when test="not(gmd:hierarchyLevel)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-hierarchyLevel"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:hierarchyLevel"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName -->
			<xsl:choose>
				<xsl:when test="not(gmd:hierarchyLevelName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-hierarchyLevelName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:hierarchyLevelName"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:contact"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
			<xsl:choose>
				<xsl:when test="not(gmd:dateStamp)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dateStamp"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:dateStamp"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
			<xsl:choose>
				<xsl:when test="not(gmd:metadataStandardName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:metadataStandardName"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
			<xsl:choose>
				<xsl:when test="not(gmd:metadataStandardVersion)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardVersion"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:metadataStandardVersion"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:referenceSystemInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:referenceSystemInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:identificationInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-identificationInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:identificationInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:distributionInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-distributionInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:distributionInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:dataQualityInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dataQualityInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:dataQualityInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:fileIdentifier)
                                 and not(self::gmd:parentIdentifier)
                                 and not(self::gmd:language)
                                 and not(self::gmd:contact) 
                                 and not(self::gmd:characterSet) 
                                 and not(self::gmd:hierarchyLevelName) 
                                 and not(self::gmd:hierarchyLevel)
                                 and not(self::gmd:dateStamp) 
                                 and not(self::gmd:metadataStandardName)
                                 and not(self::gmd:metadataStandardVersion)
                                 and not(self::gmd:referenceSystemInfo)
                                 and not(self::gmd:identificationInfo)
                                 and not(self::gmd:distributionInfo)
                                 and not(self::gmd:dataQualityInfo)
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:fileIdentifier">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString)
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:parentIdentifier">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString)
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:language">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:characterSet">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_CharacterSetCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_CharacterSetCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_CharacterSetCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_CharacterSetCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:hierarchyLevel">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ScopeCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ScopeCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_ScopeCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_ScopeCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:contact">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_ResponsibleParty) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:dateStamp">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Date"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Date) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:metadataStandardName">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:metadataStandardVersion">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:referenceSystemInfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ReferenceSystem)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ReferenceSystem"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_ReferenceSystem"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_ReferenceSystem) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_ReferenceSystem">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:referenceSystemIdentifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:referenceSystemIdentifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:referenceSystemIdentifier) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:referenceSystemIdentifier">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:RS_Identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-RS_Identifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:RS_Identifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:RS_Identifier) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:RS_Identifier">
		<!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
		<!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace -->
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:code)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-code"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:code"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:code)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-codeSpace"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:codeSpace"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:code) 
                                 and not(self::gmd:codeSpace) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:code">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:codeSpace">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:identificationInfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_DataIdentification)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_DataIdentification"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_DataIdentification"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_DataIdentification) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_DataIdentification">
		<!-- ISO 360 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.title-->
		<!-- ISO 394 Dataset referentie datum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.date-->
		<!-- ISO 395 Creatie-, publicatie-, of wijzigingsdatum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2) -->
		<!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
		<!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
		<!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
		<!-- ISO 397 URL organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.contactInfo>CI_Contract.onlineResource>CI_OnlineResource.linkage-->
		<!-- ISO 379 Rol organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5)-->
		<!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
		<!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
		<!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessConstraints Codelijst MD_RestrictionCode (B.5.24)-->
		<!-- ISO 37 Ruimtelijk schema MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
		<!-- ISO 57 Toepassingsschaal MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
		<!-- ISO 39 Dataset taal MD_Metadata.identificationInfo>MD_DataIdentification.language ISO 639-2 -->
		<!-- ISO 40 Dataset karakterset MD_Metadata.identificationInfo>MD_DataIdentification.characterSet Codelijst: MD_CharacterSetCode (B.5.10) -->
		<!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
		<!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
		<!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
		<!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
		<!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
		<!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:characterSet)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-characterSet"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:characterSet"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:citation"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:abstract)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-abstract"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:abstract"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:purpose)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-purpose"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:purpose"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:status)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-status"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:status"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:pointOfContact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-pointOfContact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:pointOfContact"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:descriptiveKeywords)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-descriptiveKeywords"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:descriptiveKeywords"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:resourceConstraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-resourceConstraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:resourceConstraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:spatialRepresentationType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-spatialRepresentationType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:spatialRepresentationType"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:spatialResolution)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-spatialResolution"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:spatialResolution"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:language"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:topicCategory)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-topicCategory"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:topicCategory"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:extent)">
					<!--Child element missing, create it -->
					<xsl:call-template name="add-extent"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it -->
					<xsl:apply-templates select="gmd:extent"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node -->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:citation) 
                                 and not(self::gmd:characterSet) 
                                 and not(self::gmd:abstract) 
                                 and not(self::gmd:purpose) 
                                 and not(self::gmd:status) 
                                 and not(self::gmd:pointOfContact) 
                                 and not(self::gmd:language) 
                                 and not(self::gmd:spatialRepresentationType) 
                                 and not(self::gmd:spatialResolution)
                                 and not(self::gmd:topicCategory) 
                                 and not(self::gmd:resourceConstraints) 														
                                 and not(self::gmd:descriptiveKeywords) 														
                                 and not(self::gmd:extent) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_DataIdentification/gmd:extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:EX_Extent)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-EX_Extent"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:EX_Extent"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:EX_Extent) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:EX_Extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:geographicElement)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-geographicElement"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:geographicElement"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:temporalElement)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-temporalElement"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:temporalElement"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:geographicElement) 
                                 and not(self::gmd:temporalElement) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:temporalElement">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:EX_TemporalExtent)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-EX_TemporalExtent"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:EX_TemporalExtent"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:EX_TemporalExtent) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:EX_TemporalExtent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:extent)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-EX_TemporalExtent-extent"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it TODO -->
					<xsl:apply-templates select="gmd:extent"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:extent) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:EX_TemporalExtent/gmd:extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gml:TimePeriod)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-TimePeriod"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gml:TimePeriod"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gml:TimePeriod) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gml:TimePeriod">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gml:begin)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-beginPosition"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gml:begin"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gml:end)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-endPosition"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gml:end"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gml:begin) 
                                 and not(self::gml:end) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:geographicElement">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:EX_GeographicBoundingBox)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-EX_GeographicBoundingBox"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:EX_GeographicBoundingBox"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:EX_GeographicBoundingBox) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:EX_GeographicBoundingBox">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:westBoundLongitude)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-westBoundLongitude"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:westBoundLongitude"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:eastBoundLongitude)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-eastBoundLongitude"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:eastBoundLongitude"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:southBoundLatitude)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-southBoundLatitude"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:southBoundLatitude"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:northBoundLatitude)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-northBoundLatitude"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:northBoundLatitude"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:westBoundLongitude) 
                                 and not(self::gmd:eastBoundLongitude) 
                                 and not(self::gmd:southBoundLatitude) 
                                 and not(self::gmd:northBoundLatitude) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:citation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Citation"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Citation) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Citation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:title)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-title"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:title"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:title)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-alternateTitle"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:alternateTitle"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:date"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-identifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:identifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:title) 
                                 and not(self::gmd:alternateTitle) 
                                 and not(self::gmd:date) 
                                 and not(self::gmd:identifier)
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:title">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:alternateTitle">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Citation/gmd:date">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Date"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Date) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Date">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-inner-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:date"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:dateType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dateType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:dateType"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:date) 
                                 and not(self::gmd:dateType) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Date/gmd:date">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Date"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Date) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Date/gmd:dateType">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_DateTypeCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_DateTypeCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_DateTypeCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_DateTypeCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Date/gmd:dateTypeCode">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Citation/gmd:identifier">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Identifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Identifier"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Identifier) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Identifier">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-identifier_code"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:code"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:code) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:code">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:abstract">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:purpose">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:status">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ProgressCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ProgressCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:MD_ProgressCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:MD_ProgressCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:pointOfContact">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_ResponsibleParty) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_ResponsibleParty">
		<!-- ISO 376 Naam organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.organisationName -->
		<!-- v1.2 ISO 386 e-mail  MD_Metadata.contact>CI_ResponsibleParty.contactInfo>CI_Contact.address>CI_Address.electronicMailAddress -->
		<!-- ISO 397 URL metadata organisatie MD_Metadata.contact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage-->
		<!-- ISO 379 Rol organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:organisationName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-organisationName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:organisationName"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:role)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-role"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:role"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:contactInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contactInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:contactInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:organisationName) 
                                 and not(self::gmd:role) 
                                 and not(self::gmd:contactInfo) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:organisationName">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:role">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_RoleCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_RoleCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_RoleCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_RoleCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:contactInfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Contact"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Contact) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Contact">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:onlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-onlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:onlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:address)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-address"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:address"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:onlineResource) 
                                  and not(self::gmd:address) 
           ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:onlineResource">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_OnlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_OnlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_OnlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_OnlineResource) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_OnlineResource">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:linkage)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-linkage"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:linkage"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:protocol)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-protocol"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:protocol"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:name)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-name"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:name"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:linkage) 
                                 and not(self::gmd:protocol) 
                                 and not(self::gmd:name) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:linkage">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:URL)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-URL"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:URL"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:URL) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:protocol">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:SV_ServiceType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-SV_ServiceType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:SV_ServiceType"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:SV_ServiceType) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:name">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:address">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Address)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Address"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Address"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Address) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:CI_Address">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:deliveryPoint)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-deliveryPoint"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:deliveryPoint"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:city)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-city"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:city"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:administrativeArea)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-administrativeArea"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:administrativeArea"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:postalCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-postalCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:postalCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:country)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-country"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:country"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:electronicMailAddress)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-electronicMailAddress"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:electronicMailAddress"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:deliveryPoint) 
                                 and not(self::gmd:city) 
                                 and not(self::gmd:administrativeArea) 
                                 and not(self::gmd:postalCode) 
                                 and not(self::gmd:country) 
                                 and not(self::gmd:electronicMailAddress) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:deliveryPoint">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:city">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:administrativeArea">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:postalCode">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:country">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:electronicMailAddress">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:spatialRepresentationType">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_SpatialRepresentationTypeCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_SpatialRepresentationTypeCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_SpatialRepresentationTypeCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_SpatialRepresentationTypeCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:spatialResolution">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Resolution)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Resolution"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Resolution"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Resolution) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Resolution">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:equivalentScale)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-equivalentScale"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:equivalentScale"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:distance)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-distance"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:distance"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:equivalentScale) 
                                 and not(self::gmd:distance) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:distance">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Distance)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Distance"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Distance"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Distance) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:equivalentScale">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_RepresentativeFraction)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_RepresentativeFraction"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_RepresentativeFraction"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_RepresentativeFraction) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_RepresentativeFraction">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:denominator)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-denominator"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:denominator"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:denominator) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:denominator">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Integer)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Integer"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Integer"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Integer) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:topicCategory">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_TopicCategoryCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_TopicCategoryCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_TopicCategoryCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_TopicCategoryCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:descriptiveKeywords">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Keywords)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Keywords"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Keywords"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Keywords) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Keywords">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:keyword)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-keyword"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:keyword"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:thesaurusName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-thesaurusName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:thesaurusName"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:keyword) 
                                 and not(self::gmd:thesaurusName)
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:keyword">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:thesaurusName">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Citation"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Citation) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:resourceConstraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Constraints) and not(../gmd:resourceConstraints/gmd:MD_Constraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Constraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Constraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_LegalConstraints) and not(../gmd:resourceConstraints/gmd:MD_LegalConstraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_LegalConstraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_LegalConstraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_SecurityConstraints) and not(../gmd:resourceConstraints/gmd:MD_SecurityConstraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_SecurityConstraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_SecurityConstraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                     not(self::gmd:MD_LegalConstraints) 
                                     and not(self::gmd:MD_Constraints) 
                                     and not(self::gmd:MD_SecurityConstraints) 
                ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Constraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:useLimitation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-useLimitation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:useLimitation"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:useLimitation) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:useLimitation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_LegalConstraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:accessConstraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-accessConstraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:accessConstraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:otherConstraints)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-otherConstraints"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:otherConstraints"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:accessConstraints) 
                                 and not(self::gmd:otherConstraints) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:accessConstraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_RestrictionCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_RestrictionCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_RestrictionCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_RestrictionCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:otherConstraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_SecurityConstraints">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:classification)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-classification"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:classification"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:classification) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:classification">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ClassificationCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ClassificationCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_ClassificationCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_ClassificationCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:distributionInfo">
		<!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
		<!-- ISO 397 URL organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage -->
		<!-- ISO 379 Rol organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Distribution)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Distribution"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Distribution"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Distribution) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Distribution">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:distributor)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-distributor"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:distributor"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:transferOptions)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-transferOptions"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:transferOptions"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:distributor) 
                                 and not(self::gmd:transferOptions) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:distributor">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_Distributor)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_Distributor"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_Distributor"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_Distributor) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_Distributor">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:distributorContact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-distributorContact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:distributorContact"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:distributorContact) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:distributorContact">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_ResponsibleParty) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:transferOptions">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_DigitalTransferOptions)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_DigitalTransferOptions"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_DigitalTransferOptions"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_DigitalTransferOptions) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:MD_DigitalTransferOptions">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:online)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-online"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:online"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:online) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:online">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_OnlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_OnlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_OnlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_OnlineResource) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:dataQualityInfo">
		<!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:DQ_DataQuality)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-DQ_DataQuality"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:DQ_DataQuality"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:DQ_DataQuality) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:DQ_DataQuality">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:scope)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-scope"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:scope"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:report)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-report"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:report"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:lineage)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-lineage"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:lineage"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:scope) 
                                 and not(self::gmd:lineage) 
                                 and not(self::gmd:report) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:scope">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:DQ_Scope)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-DQ_Scope"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:DQ_Scope"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:DQ_Scope) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:DQ_Scope">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:level)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-level"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:level"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:level) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:level">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ScopeCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ScopeCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_ScopeCode"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:MD_ScopeCode) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:report">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:DQ_DomainConsistency)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-DQ_DomainConsistency"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:DQ_DomainConsistency"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:DQ_DomainConsistency) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:DQ_DomainConsistency">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:result)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-result"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:result"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:result) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:result">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:DQ_ConformanceResult)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-DQ_ConformanceResult"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:DQ_ConformanceResult"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:DQ_ConformanceResult) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:DQ_ConformanceResult">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:pass)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-pass"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:pass"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:explanation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-explanation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:explanation"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmd:specification)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-specification"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:specification"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:pass) 
                                 and not(self::gmd:explanation) 
                                 and not(self::gmd:specification) 
           ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:pass">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:explanation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:specification">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Citation"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_Citation) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:lineage">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:LI_Lineage)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-LI_Lineage"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:LI_Lineage"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:LI_Lineage) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmd:LI_Lineage">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:statement)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-statement"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:statement"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:statement) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<!-- ISO 19110 Feature Catalogue elements -->
	<xsl:template match="gfc:FC_FeatureCatalogue">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmx:name)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gmx_name"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmx:name"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmx:scope)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gmx_scope"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmx:scope"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmx:versionNumber)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gmx_versionNumber"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmx:versionNumber"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gmx:versionDate)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gmx_versionDate"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmx:versionDate"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:producer)">
					<xsl:call-template name="add-gfc_producer"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="gfc:producer"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:featureType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_featureType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:featureType"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmx:name) 
                                 and not(self::gmx:scope) 
                                 and not(self::gmx:versionNumber) 
                                 and not(self::gmx:versionDate) 
                                 and not(self::gfc:producer) 
                                 and not(self::gfc:featureType) 
           ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmx:name">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmx:scope">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmx:versionNumber">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gmx:versionDate">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Date"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Date) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<!-- TODO mix-up of namespaces -->
	<xsl:template match="gfc:producer">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gmd:CI_ResponsibleParty) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<!-- TODO mix-up of namespaces -->
	<xsl:template match="gfc:featureType">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gfc:FC_FeatureType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_FC_FeatureType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:FC_FeatureType"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gfc:FC_FeatureType) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:FC_FeatureType">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gfc:typeName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_typeName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:typeName"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:definition)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_definition"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:definition"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:isAbstract)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_isAbstract"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:isAbstract"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:carrierOfCharacteristics)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_carrierOfCharacteristics"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:carrierOfCharacteristics"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gfc:typeName) 
                                 and not(self::gfc:definition) 
                                 and not(self::gfc:isAbstract) 
                                 and not(self::gfc:carrierOfCharacteristics) 
          ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:typeName">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:LocalName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-LocalName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:LocalName"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:LocalName) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:definition">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:CharacterString) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:isAbstract">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Boolean)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Boolean"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Boolean"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Boolean) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:carrierOfCharacteristics">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gfc:FC_FeatureAttribute)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_FC_FeatureAttribute"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:FC_FeatureAttribute"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gfc:FC_FeatureAttribute) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:FC_FeatureAttribute">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gfc:memberName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_memberName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:memberName"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:definition)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_definition"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:definition"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:cardinality)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_cardinality"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:cardinality"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gfc:valueType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-gfc_valueType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gfc:valueType"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gfc:memberName) 
                                 and  not(self::gfc:definition) 
                                 and   not(self::gfc:cardinality) 
                                 and  not(self::gfc:valueType) 
           ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:memberName">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:LocalName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-LocalName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:LocalName"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:LocalName) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:cardinality">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(gco:Integer)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Integer"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Integer"/>
				</xsl:otherwise>
			</xsl:choose>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
                                 not(self::gco:Integer) 
            ]"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="gfc:valueType">
		<xsl:copy>
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
