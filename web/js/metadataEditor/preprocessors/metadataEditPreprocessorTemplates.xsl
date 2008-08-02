<?xml version="1.0" encoding="UTF-8"?>
<!--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet					
    version="1.0"
    xmlns:gmd="http://www.isotc211.org/2005/gmd"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink"					
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gml="http://www.opengis.net/gml/3.2"
>
    
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
    <xsl:variable name="FILL_DEFAULTS">true
    </xsl:variable>
    <xsl:template name="add-MD_Metadata">
        <xsl:element name="gmd:MD_Metadata">
            <!--Add sub-elements and sections-->
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
    <xsl:template name="add-language">
        <xsl:element name="gmd:language">
            <!--Add sub-elements and sections-->
            <xsl:choose>
                <xsl:when test="$FILL_DEFAULTS">
                    <xsl:element name="gco:CharacterString">dut
                    </xsl:element>
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
                    <xsl:element name="gco:CharacterString">ISO 19115:2003
                    </xsl:element>
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
                    <xsl:element name="gco:CharacterString">Nederlandse metadatastandaard voor geografie 1.1
                    </xsl:element>
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
                    <xsl:element name="gco:CharacterString">28992
                    </xsl:element>
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
                    <xsl:element name="gco:CharacterString">EPSG
                    </xsl:element>
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
            <xsl:call-template name="add-status"/>
            <xsl:call-template name="add-pointOfContact"/>
            <xsl:call-template name="add-language"/>
            <xsl:call-template name="add-characterSet"/>
            <xsl:call-template name="add-spatialRepresentationType"/>
            <xsl:call-template name="add-spatialresolution"/>
            <xsl:call-template name="add-topicCategory"/>
            <xsl:call-template name="add-descriptiveKeywords"/>
            <xsl:call-template name="add-resourceConstraints"/>
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
            <xsl:call-template name="add-date"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-title">
        <xsl:element name="gmd:title">
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
    <xsl:template name="add-CI_DateTypeCode">
        <xsl:element name="gmd:CI_DateTypeCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
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
            <xsl:call-template name="add-role"/>
            <xsl:call-template name="add-contactInfo"/>
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
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-linkage">
        <xsl:element name="gmd:linkage">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-URL"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-spatialRepresentationType">
        <xsl:element name="gmd:spatialRepresentationType">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-MD_SpatialRepresentationTypeCode"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-spatialresolution">
        <xsl:element name="gmd:spatialresolution">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-MD_Resolution"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_Resolution">
        <xsl:element name="gmd:MD_Resolution">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-equivalentScale"/>
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
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-keyword">
        <xsl:element name="gmd:keyword">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-CharacterString"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-resourceConstraints">
        <xsl:element name="gmd:resourceConstraints">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-MD_Constraints"/>
            <xsl:call-template name="add-MD_LegalConstraints"/>
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
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-accessConstraints">
        <xsl:element name="gmd:accessContraints">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-MD_RestrictionCode"/>
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
                    <xsl:element name="gco:Decimal">3.3
                    </xsl:element>
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
                    <xsl:element name="gco:Decimal">7.2
                    </xsl:element>
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
                    <xsl:element name="gco:Decimal">50.5
                    </xsl:element>
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
                    <xsl:element name="gco:Decimal">53.5
                    </xsl:element>
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
            <xsl:call-template name="add-TimeInstant-Before"/>
            <xsl:call-template name="add-TimeInstant-After"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-TimeInstant-After">
        <xsl:element name="gmd:TimeInstant">
            <xsl:element name="gmd:timePosition">
                <xsl:attribute name="gmd:frame">#ISO-8601
                </xsl:attribute>
                <xsl:attribute name="gmd:indeterminatePosition">after
                </xsl:attribute>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-TimeInstant-Before">
        <xsl:element name="gmd:TimeInstant">
            <xsl:element name="gmd:timePosition">
                <xsl:attribute name="gmd:frame">#ISO-8601
                </xsl:attribute>
                <xsl:attribute name="gmd:indeterminatePosition">before
                </xsl:attribute>
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
            <xsl:call-template name="add-lineage"/>
            <xsl:call-template name="add-report"/>
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
    <xsl:template name="add-report">
        <xsl:element name="gmd:report">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-DQ_QuantitativeAttributeAccuracy"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-DQ_QuantitativeAttributeAccuracy">
        <xsl:element name="gmd:DQ_QuantitativeAttributeAccuracy">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-result"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-result">
        <xsl:element name="gmd:result">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-DQ_QuantitativeResult"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-DQ_QuantitativeResult">
        <xsl:element name="gmd:DQ_QuantitativeResult">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-value"/>
            <xsl:call-template name="add-valueUnit"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-value">
        <xsl:element name="gmd:value">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-Record"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-valueUnit">
        <xsl:element name="gmd:valueUnit">
            <!--Add sub-elements and sections-->
            <xsl:call-template name="add-UnitDefinition"/>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-CharacterString">
        <xsl:element name="gco:CharacterString">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-Date">
        <xsl:element name="gco:Date">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-Decimal">
        <xsl:element name="gco:Decimal">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-Integer">
        <xsl:element name="gco:Integer">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-URL">
        <xsl:element name="gmd:URL">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-Record">
        <xsl:element name="gco:Record">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-UnitDefinition">
        <!-- TODO: default waarde invullen! -->
        <xsl:element name="gml:UnitDefinition">
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_CharacterSetCode">
        <xsl:element name="gmd:MD_CharacterSetCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
            <xsl:if test="$FILL_DEFAULTS">
                <xsl:value-of select="'utf8'"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_ProgressCode">
        <xsl:element name="gmd:MD_ProgressCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
            <xsl:if test="$FILL_DEFAULTS">
                <xsl:value-of select="'compleet'"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-CI_RoleCode">
        <xsl:element name="gmd:CI_RoleCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_SpatialRepresentationTypeCode">
        <xsl:element name="gmd:MD_SpatialRepresentationTypeCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_TopicCategoryCode">
        <xsl:element name="gmd:MD_TopicCategoryCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_ScopeCode">
        <xsl:element name="gmd:MD_ScopeCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
            <xsl:if test="$FILL_DEFAULTS">
                <xsl:value-of select="'dataset'"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>
    <xsl:template name="add-MD_RestrictionCode">
        <xsl:element name="gmd:MD_RestrictionCode">
            <xsl:attribute name="gmd:codeList">
            </xsl:attribute>
            <xsl:attribute name="gmd:codeListValue">
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
