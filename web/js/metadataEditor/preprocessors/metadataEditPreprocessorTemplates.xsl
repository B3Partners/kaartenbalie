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
	Bevat templates die ontbrekende nodes toevoegen om de mogelijkheid te bieden deze te editen in de stylesheet.
	-->
	<!-- do we want default values filled in, templates must be prepared for this-->
	<xsl:variable name="FILL_DEFAULTS">true</xsl:variable>
	<xsl:template name="add-MD_Metadata">
		<xsl:element name="gmd:MD_Metadata">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-fileIdentifier"/>
			<xsl:call-template name="add-parentIdentifier"/>
			<xsl:call-template name="add-language"/>
			<xsl:call-template name="add-contact"/>
			<xsl:call-template name="add-characterSet"/>
			<xsl:call-template name="add-hierarchyLevel"/>
			<xsl:call-template name="add-hierarchyLevelName"/>
			<xsl:call-template name="add-dateStamp"/>
			<xsl:call-template name="add-metadataStandardName"/>
			<xsl:call-template name="add-metadataStandardVersion"/>
			<xsl:call-template name="add-referenceSystemInfo"/>
			<xsl:call-template name="add-identificationInfo"/>
			<xsl:call-template name="add-distributionInfo"/>
			<xsl:call-template name="add-dataQualityInfo"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-fileIdentifier">
		<xsl:element name="gmd:fileIdentifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-parentIdentifier">
		<xsl:element name="gmd:parentIdentifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-language">
		<xsl:element name="gmd:language">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:CharacterString">nld</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-characterSet">
		<xsl:element name="gmd:characterSet">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_CharacterSetCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-hierarchyLevel">
		<xsl:element name="gmd:hierarchyLevel">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ScopeCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-hierarchyLevelName">
		<xsl:element name="gmd:hierarchyLevelName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-contact">
		<xsl:element name="gmd:contact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-dateStamp">
		<xsl:element name="gmd:dateStamp">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-metadataStandardName">
		<xsl:element name="gmd:metadataStandardName">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:CharacterString">ISO 19115:2003</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-metadataStandardVersion">
		<xsl:element name="gmd:metadataStandardVersion">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:CharacterString">Nederlandse metadatastandaard voor geografie 1.2</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-referenceSystemInfo">
		<xsl:element name="gmd:referenceSystemInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ReferenceSystem"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_ReferenceSystem">
		<xsl:element name="gmd:MD_ReferenceSystem">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-referenceSystemIdentifier"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-referenceSystemIdentifier">
		<xsl:element name="gmd:referenceSystemIdentifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-RS_Identifier"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-RS_Identifier">
		<xsl:element name="gmd:RS_Identifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-code"/>
			<xsl:call-template name="add-codeSpace"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-code">
		<xsl:element name="gmd:code">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:CharacterString">28992</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-codeSpace">
		<xsl:element name="gmd:codeSpace">
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:CharacterString">EPSG</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-identificationInfo">
		<xsl:element name="gmd:identificationInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_DataIdentification"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_DataIdentification">
		<xsl:element name="gmd:MD_DataIdentification">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-citation"/>
			<xsl:call-template name="add-abstract"/>
			<xsl:call-template name="add-purpose"/>
			<xsl:call-template name="add-status"/>
			<xsl:call-template name="add-pointOfContact"/>
			<xsl:call-template name="add-descriptiveKeywords"/>
			<xsl:call-template name="add-resourceConstraints"/>
			<xsl:call-template name="add-spatialRepresentationType"/>
			<xsl:call-template name="add-spatialResolution"/>
			<xsl:call-template name="add-language"/>
			<xsl:call-template name="add-characterSet"/>
			<xsl:call-template name="add-topicCategory"/>
			<xsl:call-template name="add-extent"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-citation">
		<xsl:element name="gmd:citation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Citation"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_Citation">
		<xsl:element name="gmd:CI_Citation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-title"/>
			<xsl:call-template name="add-alternateTitle"/>
			<xsl:call-template name="add-date"/>
			<xsl:call-template name="add-identifier"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-title">
		<xsl:element name="gmd:title">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-alternateTitle">
		<xsl:element name="gmd:alternateTitle">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-date">
		<xsl:element name="gmd:date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Date"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_Date">
		<xsl:element name="gmd:CI_Date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-inner-date"/>
			<xsl:call-template name="add-dateType"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-inner-date">
		<xsl:element name="gmd:date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-dateType">
		<xsl:element name="gmd:dateType">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_DateTypeCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-identifier">
		<xsl:element name="gmd:identifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Identifier"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Identifier">
		<xsl:element name="gmd:MD_Identifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-identifier_code"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-identifier_code">
		<xsl:element name="gmd:code">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-purpose">
		<xsl:element name="gmd:purpose">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-abstract">
		<xsl:element name="gmd:abstract">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-status">
		<xsl:element name="gmd:status">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ProgressCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-pointOfContact">
		<xsl:element name="gmd:pointOfContact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_ResponsibleParty">
		<xsl:element name="gmd:CI_ResponsibleParty">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-organisationName"/>
			<xsl:call-template name="add-contactInfo"/>
			<xsl:call-template name="add-role"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-organisationName">
		<xsl:element name="gmd:organisationName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-role">
		<xsl:element name="gmd:role">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_RoleCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-contactInfo">
		<xsl:element name="gmd:contactInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Contact"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_Contact">
		<xsl:element name="gmd:CI_Contact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-onlineResource"/>
			<xsl:call-template name="add-address"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-onlineResource">
		<xsl:element name="gmd:onlineResource">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_OnlineResource"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_OnlineResource">
		<xsl:element name="gmd:CI_OnlineResource">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-linkage"/>
			<xsl:call-template name="add-protocol"/>
			<xsl:call-template name="add-name"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-linkage">
		<xsl:element name="gmd:linkage">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-URL"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-protocol">
		<xsl:element name="gmd:protocol">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-SV_ServiceType"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-name">
		<xsl:element name="gmd:name">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-address">
		<xsl:element name="gmd:address">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Address"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_Address">
		<xsl:element name="gmd:CI_Address">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-deliveryPoint"/>
			<xsl:call-template name="add-city"/>
			<xsl:call-template name="add-administrativeArea"/>
			<xsl:call-template name="add-postalCode"/>
			<xsl:call-template name="add-country"/>
			<xsl:call-template name="add-electronicMailAddress"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-deliveryPoint">
		<xsl:element name="gmd:deliveryPoint">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-city">
		<xsl:element name="gmd:city">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-administrativeArea">
		<xsl:element name="gmd:administrativeArea">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-postalCode">
		<xsl:element name="gmd:postalCode">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-country">
		<xsl:element name="gmd:country">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-electronicMailAddress">
		<xsl:element name="gmd:electronicMailAddress">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-spatialRepresentationType">
		<xsl:element name="gmd:spatialRepresentationType">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_SpatialRepresentationTypeCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-spatialResolution">
		<xsl:element name="gmd:spatialResolution">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Resolution"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Resolution">
		<xsl:element name="gmd:MD_Resolution">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-equivalentScale"/>
			<xsl:call-template name="add-distance"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-distance">
		<xsl:element name="gmd:distance">
			<!--Add sub-elements and sections-->
			<!-- TODO volgens ISO TS 1903-->
			<xsl:call-template name="add-Distance"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-equivalentScale">
		<xsl:element name="gmd:equivalentScale">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_RepresentativeFraction"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_RepresentativeFraction">
		<xsl:element name="gmd:MD_RepresentativeFraction">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-denominator"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-denominator">
		<xsl:element name="gmd:denominator">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Integer"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-topicCategory">
		<xsl:element name="gmd:topicCategory">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_TopicCategoryCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-descriptiveKeywords">
		<xsl:element name="gmd:descriptiveKeywords">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Keywords"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Keywords">
		<xsl:element name="gmd:MD_Keywords">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-keyword"/>
			<xsl:call-template name="add-thesaurusName"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-keyword">
		<xsl:element name="gmd:keyword">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-thesaurusName">
		<xsl:element name="gmd:thesaurusName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Citation"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-resourceConstraints">
		<xsl:element name="gmd:resourceConstraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Constraints"/>
			<xsl:call-template name="add-MD_LegalConstraints"/>
			<xsl:call-template name="add-MD_SecurityConstraints"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Constraints">
		<xsl:element name="gmd:MD_Constraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-useLimitation"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-useLimitation">
		<xsl:element name="gmd:useLimitation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_LegalConstraints">
		<xsl:element name="gmd:MD_LegalConstraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-accessConstraints"/>
			<xsl:call-template name="add-otherConstraints"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-accessConstraints">
		<xsl:element name="gmd:accessConstraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_RestrictionCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-otherConstraints">
		<xsl:element name="gmd:otherConstraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_SecurityConstraints">
		<xsl:element name="gmd:MD_SecurityConstraints">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-classification"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-classification">
		<xsl:element name="gmd:classification">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ClassificationCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-extent">
		<xsl:element name="gmd:extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-EX_Extent"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-EX_Extent">
		<xsl:element name="gmd:EX_Extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-geographicElement"/>
			<xsl:call-template name="add-temporalElement"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-geographicElement">
		<xsl:element name="gmd:geographicElement">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-EX_GeographicBoundingBox"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-EX_GeographicBoundingBox">
		<xsl:element name="gmd:EX_GeographicBoundingBox">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-westBoundLongitude"/>
			<xsl:call-template name="add-eastBoundLongitude"/>
			<xsl:call-template name="add-southBoundLatitude"/>
			<xsl:call-template name="add-northBoundLatitude"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-westBoundLongitude">
		<xsl:element name="gmd:westBoundLongitude">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:Decimal">3.3</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-Decimal"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-eastBoundLongitude">
		<xsl:element name="gmd:eastBoundLongitude">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:Decimal">7.2</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-Decimal"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-southBoundLatitude">
		<xsl:element name="gmd:southBoundLatitude">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:Decimal">50.5</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-Decimal"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-northBoundLatitude">
		<xsl:element name="gmd:northBoundLatitude">
			<!--Add sub-elements and sections-->
			<xsl:choose>
				<xsl:when test="$FILL_DEFAULTS">
					<xsl:element name="gco:Decimal">53.5</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="add-Decimal"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-temporalElement">
		<xsl:element name="gmd:temporalElement">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-EX_TemporalExtent"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-EX_TemporalExtent">
		<xsl:element name="gmd:EX_TemporalExtent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-EX_TemporalExtent-extent"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-EX_TemporalExtent-extent">
		<xsl:element name="gmd:extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-TimePeriod"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-TimePeriod">
		<xsl:element name="gml:TimePeriod">
			<xsl:attribute name="gml:id">extent</xsl:attribute>
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-beginPosition"/>
			<xsl:call-template name="add-endPosition"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-beginPosition">
		<xsl:element name="gml:begin">
			<xsl:element name="gml:TimeInstant">
				<xsl:attribute name="gml:id">t11</xsl:attribute>
				<xsl:element name="gml:timePosition"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-endPosition">
		<xsl:element name="gml:end">
			<xsl:element name="gml:TimeInstant">
				<xsl:attribute name="gml:id">t12</xsl:attribute>
				<xsl:element name="gml:timePosition"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-distributionInfo">
		<xsl:element name="gmd:distributionInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Distribution"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Distribution">
		<xsl:element name="gmd:MD_Distribution">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-distributor"/>
			<xsl:call-template name="add-transferOptions"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-distributor">
		<xsl:element name="gmd:distributor">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_Distributor"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_Distributor">
		<xsl:element name="gmd:MD_Distributor">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-distributorContact"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-distributorContact">
		<xsl:element name="gmd:distributorContact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-transferOptions">
		<xsl:element name="gmd:transferOptions">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_DigitalTransferOptions"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_DigitalTransferOptions">
		<xsl:element name="gmd:MD_DigitalTransferOptions">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-online"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-online">
		<xsl:element name="gmd:online">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_OnlineResource"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-dataQualityInfo">
		<xsl:element name="gmd:dataQualityInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-DQ_DataQuality"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-DQ_DataQuality">
		<xsl:element name="gmd:DQ_DataQuality">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-scope"/>
			<xsl:call-template name="add-report"/>
			<xsl:call-template name="add-lineage"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-scope">
		<xsl:element name="gmd:scope">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-DQ_Scope"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-DQ_Scope">
		<xsl:element name="gmd:DQ_Scope">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-level"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-level">
		<xsl:element name="gmd:level">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ScopeCode"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-report">
		<xsl:element name="gmd:report">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-DQ_DomainConsistency"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-DQ_DomainConsistency">
		<xsl:element name="gmd:DQ_DomainConsistency">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-result"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-result">
		<xsl:element name="gmd:result">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-DQ_ConformanceResult"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-DQ_ConformanceResult">
		<xsl:element name="gmd:DQ_ConformanceResult">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-pass"/>
			<xsl:call-template name="add-explanation"/>
			<xsl:call-template name="add-specification"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-pass">
		<xsl:element name="gmd:pass">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Boolean"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-explanation">
		<xsl:element name="gmd:explanation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-specification">
		<xsl:element name="gmd:specification">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Citation"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-lineage">
		<xsl:element name="gmd:lineage">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-LI_Lineage"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-LI_Lineage">
		<xsl:element name="gmd:LI_Lineage">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-statement"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-statement">
		<xsl:element name="gmd:statement">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CharacterString">
		<xsl:element name="gco:CharacterString"/>
	</xsl:template>
	<xsl:template name="add-LocalName">
		<xsl:element name="gco:LocalName"/>
	</xsl:template>
	<xsl:template name="add-Date">
		<xsl:element name="gco:Date"/>
	</xsl:template>
	<xsl:template name="add-Decimal">
		<xsl:element name="gco:Decimal"/>
	</xsl:template>
	<xsl:template name="add-Integer">
		<xsl:element name="gco:Integer"/>
	</xsl:template>
	<xsl:template name="add-Boolean">
		<xsl:element name="gco:Boolean"/>
	</xsl:template>
	<xsl:template name="add-Distance">
		<xsl:element name="gco:Distance">
			<xsl:attribute name="uom">meters</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-URL">
		<xsl:element name="gmd:URL"/>
	</xsl:template>
	<xsl:template name="add-CI_DateTypeCode">
		<xsl:element name="gmd:CI_DateTypeCode">
			<xsl:attribute name="codeList">#CI_DateTypeCode</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_CharacterSetCode">
		<xsl:element name="gmd:MD_CharacterSetCode">
			<xsl:attribute name="codeList">#MD_CharacterSetCode</xsl:attribute>
			<xsl:attribute name="codeListValue"><xsl:if test="$FILL_DEFAULTS"><xsl:value-of select="'utf8'"/></xsl:if></xsl:attribute>
			<xsl:if test="$FILL_DEFAULTS">
				<xsl:value-of select="'utf8'"/>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_ProgressCode">
		<xsl:element name="gmd:MD_ProgressCode">
			<xsl:attribute name="codeList">#MD_ProgressCode</xsl:attribute>
			<xsl:attribute name="codeListValue"><xsl:if test="$FILL_DEFAULTS"><xsl:value-of select="'completed'"/></xsl:if></xsl:attribute>
			<xsl:if test="$FILL_DEFAULTS">
				<xsl:value-of select="'compleet'"/>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-CI_RoleCode">
		<xsl:element name="gmd:CI_RoleCode">
			<xsl:attribute name="codeList">#CI_RoleCode</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_SpatialRepresentationTypeCode">
		<xsl:element name="gmd:MD_SpatialRepresentationTypeCode">
			<xsl:attribute name="codeList">#MD_SpatialRepresentationTypeCode</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_TopicCategoryCode">
		<xsl:element name="gmd:MD_TopicCategoryCode"/>
		<!-- TODO characterstring enumeration -->
	</xsl:template>
	<xsl:template name="add-MD_ScopeCode">
		<xsl:element name="gmd:MD_ScopeCode">
			<xsl:attribute name="codeList">#MD_ScopeCode</xsl:attribute>
			<xsl:attribute name="codeListValue"><xsl:if test="$FILL_DEFAULTS"><xsl:value-of select="'dataset'"/></xsl:if></xsl:attribute>
			<xsl:if test="$FILL_DEFAULTS">
				<xsl:value-of select="'dataset'"/>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_RestrictionCode">
		<xsl:element name="gmd:MD_RestrictionCode">
			<xsl:attribute name="codeList">#MD_RestrictionCode</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-MD_ClassificationCode">
		<xsl:element name="gmd:MD_ClassificationCode">
			<xsl:attribute name="codeList">#MD_ClassificationCode</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-SV_ServiceType">
		<xsl:element name="gmd:SV_ServiceType">
			<xsl:attribute name="codeList">#SV_ServiceType</xsl:attribute>
			<xsl:attribute name="codeListValue"/>
		</xsl:element>
	</xsl:template>
	<!-- Feature catalogue ISO 19110 -->
	<xsl:template name="add-gfc_FC_FeatureCatalogue">
		<xsl:element name="gfc:FC_FeatureCatalogue">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-gmx_name"/>
			<xsl:call-template name="add-gmx_scope"/>
			<xsl:call-template name="add-gmx_versionNumber"/>
			<xsl:call-template name="add-gmx_versionDate"/>
			<xsl:call-template name="add-gfc_producer"/>
			<xsl:call-template name="add-gfc_featureType"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gmx_name">
		<xsl:element name="gmx:name">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gmx_scope">
		<xsl:element name="gmx:scope">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gmx_versionNumber">
		<xsl:element name="gmx:versionNumber">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gmx_versionDate">
		<xsl:element name="gmx:versionDate">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_producer">
		<xsl:element name="gfc:producer">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_featureType">
		<xsl:element name="gfc:featureType">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-gfc_FC_FeatureType"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_FC_FeatureType">
		<xsl:element name="gfc:FC_FeatureType">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-gfc_typeName"/>
			<xsl:call-template name="add-gfc_definition"/>
			<xsl:call-template name="add-gfc_isAbstract"/>
			<xsl:call-template name="add-gfc_carrierOfCharacteristics"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_typeName">
		<xsl:element name="gfc:typeName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-LocalName"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_definition">
		<xsl:element name="gfc:definition">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_isAbstract">
		<xsl:element name="gfc:isAbstract">
			<!--Add sub-elements and sections-->
			<xsl:element name="gco:Boolean">
				<xsl:value-of select="'FALSE'"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_carrierOfCharacteristics">
		<xsl:element name="gfc:carrierOfCharacteristics">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-gfc_FC_FeatureAttribute"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_FC_FeatureAttribute">
		<xsl:element name="gfc:FC_FeatureAttribute">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-gfc_memberName"/>
			<xsl:call-template name="add-gfc_definition"/>
			<xsl:call-template name="add-gfc_cardinality"/>
			<xsl:call-template name="add-gfc_valueType"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_memberName">
		<xsl:element name="gfc:memberName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-LocalName"/>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_cardinality">
		<xsl:element name="gfc:cardinality">
			<!--Add sub-elements and sections-->
			<xsl:element name="gco:Integer">
				<xsl:value-of select="'1'"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template name="add-gfc_valueType">
		<xsl:element name="gfc:valueType">
			<!--Add sub-elements and sections-->
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
