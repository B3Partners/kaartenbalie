<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xlink="http://www.w3.org/1999/xlink"					
    xmlns:gmd="http://www.isotc211.org/2005/gmd"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gml="http://www.opengis.net/gml"    
    exclude-result-prefixes="gmd gco xlink"
>
    
    <!-- template library to use for making element editable -->
    <xsl:include href="editableElements.xsl"/>
    <!-- template library to use for showing basic types of ISO 19139 -->
    <xsl:include href="editableBasicTypes.xsl"/>
    
    <xsl:output	
        doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN"
        method="xml" omit-xml-declaration="no" indent="yes"
    />
    
    <!-- 
			Overweging: ISO nummers zijn in principe algemeen en kunnen worden hergebruikt. 
			In de huidige implementatie ga ik er van uit dat de de ISO nummers niet worden hergebruikt.
			De titel is namelijk specifiek.
	-->
    
    <!-- ISO 2 Metadata ID MD_Metadata.fileIdentifier -->
    <!-- ISO 3 Metadata taal MD_Metadata.language -->
    <xsl:template match="gmd:MD_Metadata/gmd:language/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Metadata taal</xsl:with-param>
            <xsl:with-param name="picklist">picklist_LanguageCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 4 Metadata karakterset MD_Metadata.characterSet Codelijst: MD_CharacterSetCode (B.5.10)-->
    <xsl:template match="gmd:MD_Metadata/gmd:characterSet/gmd:MD_CharacterSetCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Metadata karakterset</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_CharacterSetCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
    <xsl:template match="gmd:MD_Metadata/gmd:dataQualityInfo//gmd:level/gmd:MD_ScopeCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Metadata hiërarchieniveau</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_ScopeCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName -->
    <xsl:template match="gmd:MD_Metadata/gmd:hierarchyLevelName/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Beschrijving hiërarchisch niveau</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 376 Naam organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.organisationName -->
    <!-- ISO 397 URL metadata organisatie MD_Metadata.contact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage-->
    <!-- ISO 379 Rol organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
    <!-- TODO: ?alle? nodes (mogelijk) werkend met xlink -->
    <xsl:template match="gmd:MD_Metadata/gmd:contact">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Organisatie verantwoordelijk voor metadata</xsl:with-param>
                <xsl:with-param name="repeatable" select="'true'"/>
                <xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:contact[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
            </div>
        </div>
    </xsl:template>
    
    <!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
    <xsl:template match="gmd:dateStamp/gco:Date">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Metadata datum</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
    <xsl:template match="gmd:metadataStandardName/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Metadatastandaard naam</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
    <xsl:template match="gmd:metadataStandardVersion/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Versie metadatastandaard naam</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
    <xsl:template match="gmd:code/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Code referentiesysteem</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace-->
    <xsl:template match="gmd:codeSpace/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Verantwoordelijke organisatie voor namespace referentiesysteemm</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 360 Dataset titel MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.title-->
    <xsl:template match="gmd:title/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Dataset titel</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- datasetReferentie 394+395 -->
    <xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Dataset referentie</xsl:with-param>
                <xsl:with-param name="repeatable" select="'true'"/>
                <xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date[<xsl:value-of select="position()"/>]/gmd:CI_Date</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select="gmd:CI_Date"/>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="gmd:CI_Date">
        <xsl:apply-templates select="gmd:date/gco:Date"/>
        <xsl:apply-templates select="gmd:dateType/gmd:CI_DateTypeCode"/>		
    </xsl:template>
    <!-- ISO 394 Dataset referentie datum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.date-->
    <xsl:template match="gmd:date/gco:Date">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Dataset referentie datum</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 395 Creatie-, publicatie-, of wijzigingsdatum MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.date>CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2)-->
    <xsl:template match="gmd:dateType/gmd:CI_DateTypeCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Creatie-, publicatie-, of wijzigingsdatum</xsl:with-param>
            <xsl:with-param name="picklist">picklist_CI_DateTypeCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
    <xsl:template match="gmd:abstract/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Samenvatting</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
    <xsl:template match="gmd:status/gmd:MD_ProgressCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Status</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_ProgressCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
    <!-- ISO 397 URL organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.contactInfo>CI_Contract.onlineResource>CI_OnlineResource.linkage-->
    <!-- ISO 379 Rol organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5)-->
    <xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Dataset verantwoordelijke organisatie</xsl:with-param>
                <xsl:with-param name="repeatable" select="'true'"/>
                <xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
            </div>
        </div>
    </xsl:template>
    
    <!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
    <xsl:template match="gmd:keyword/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Trefwoorden</xsl:with-param>
            <xsl:with-param name="repeatable" select="'true'"/>			
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
    <xsl:template match="gmd:useLimitation/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Gebruiksbeperkingen</xsl:with-param>
             <xsl:with-param name="repeatable" select="'false'"/>
       </xsl:call-template>
    </xsl:template>
    <!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessContraints Codelijst MD_RestrictionCode (B.5.24)-->
    <xsl:template match="gmd:accessConstraints/gmd:MD_RestrictionCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">(juridische) toegangsrestricties</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_RestrictionCode</xsl:with-param>
             <xsl:with-param name="repeatable" select="'true'"/>
       </xsl:call-template>
    </xsl:template>
    <!-- ISO 37 Ruimtelijk schema MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
    <xsl:template match="gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Ruimtelijk schema</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_SpatialRepresentationTypeCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 57 Toepassingsschaal MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
    <xsl:template match="gmd:denominator/gco:Integer">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Toepassingsschaal</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 39 Dataset taal MD_Metadata.identificationInfo>MD_DataIdentification.language ISO 639-2 -->
    <xsl:template match="gmd:identificationInfo//gmd:language/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Dataset taal</xsl:with-param>
            <xsl:with-param name="picklist">picklist_LanguageCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 40 Dataset karakterset MD_Metadata.identificationInfo>MD_DataIdentification.characterSet Codelijst: MD_CharacterSetCode (B.5.10) -->
    <xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet/gmd:MD_CharacterSetCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Dataset karakterset</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_CharacterSetCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
    <xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Thema's</xsl:with-param>
            <xsl:with-param name="picklist">picklist_MD_TopicCategoryCode</xsl:with-param>
            <xsl:with-param name="repeatable" select="'true'"/>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Minimum x</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Maximum x</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Minimum y</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Maximum y</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Begindatum</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Einddatum</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
    <!-- ISO 397 URL organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.contactInfo>CI_Contact.onlineResource>CI_OnlineResource.linkage -->
    <!-- ISO 379 Rol organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.role Codelijst: CI_RoleCode (B.5.5) -->
    <xsl:template match="gmd:distributionInfo//gmd:distributor">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Distribuerende organisatie</xsl:with-param>
                <xsl:with-param name="repeatable" select="'true'"/>
                <xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:distributor[<xsl:value-of select="position()"/>]/gmd:MD_Distributor/gmd:distributorContact/gmd:CI_ResponsibleParty</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select=".//gmd:distributorContact/gmd:CI_ResponsibleParty"/>
            </div>
        </div>
    </xsl:template>
    
    <!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
    <xsl:template match="gmd:lineage//gmd:statement/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Algemene beschrijving herkomst</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- deze worden 3x gebruikt voor: distributor, pointOfContact, ResponsibleParty ! -->
    <!-- ISO nr. 376 -->
    <xsl:template match="gmd:organisationName/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Naam organisatie</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO nr. 379 -->
    <xsl:template match="gmd:role/gmd:CI_RoleCode">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Rol organisatie</xsl:with-param>
            <xsl:with-param name="picklist">picklist_CI_RoleCode</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO nr. 397 -->
    <xsl:template name="link">
        <xsl:call-template name="element">
            <xsl:with-param name="title">URL organisatie</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <!-- ISO nr. 397 -->
    <xsl:template match="gmd:linkage/gmd:URL">
        <xsl:call-template name="element">
            <xsl:with-param name="title">URL organisatie</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="gmd:CI_ResponsibleParty">
        <xsl:apply-templates select="gmd:organisationName"/>
        <xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage"/>
        <xsl:apply-templates select="gmd:role"/>		
    </xsl:template>
    
    
    <!-- Omgrenzende rechthoek (is vervangen door polygon in ISO19139 !?! of polygon bestaat nu er naast) -->
    <xsl:template name="omgrenzendeRechthoek">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Omgrenzende rechthoek</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select="gmd:westBoundLongitude/gco:Decimal"/>
                <xsl:apply-templates select="gmd:eastBoundLongitude/gco:Decimal"/>
                <xsl:apply-templates select="gmd:southBoundLatitude/gco:Decimal"/>							
                <xsl:apply-templates select="gmd:northBoundLatitude/gco:Decimal"/>
            </div>
        </div>
    </xsl:template>
    <xsl:template name="temporeleDekking">
        <div class="section">
            <xsl:call-template name="section-title">
                <xsl:with-param name="title">Temporele dekking</xsl:with-param>
            </xsl:call-template>
            <div class="section-content">
                <xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition"/>
                <xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition"/>
            </div>
        </div>
    </xsl:template>
    
    <!-- ISO nr. 137 
	<xsl:template match="gmd:dataQualityInfo//gmd:report//gmd:DQ_QuantitativeAttributeAccuracy//gmd:result//gmd:DQ_QuantitativeResult//gmd:value/gco:Record">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>-->

    
</xsl:stylesheet>

