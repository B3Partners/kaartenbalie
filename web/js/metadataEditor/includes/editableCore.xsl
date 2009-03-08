<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gfc="http://www.isotc211.org/2005/gfc" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" exclude-result-prefixes="gmd gco xlink">
	<!-- template library to use for making element editable -->
	<xsl:include href="editableElements.xsl"/>
	<!-- template library to use for showing basic types of ISO 19139 -->
	<xsl:include href="editableBasicTypes.xsl"/>
	<xsl:output doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN" method="xml" omit-xml-declaration="no" indent="yes"/>
	<!-- 
		Onderstaande templates zijn specifiek voor de ISO nummers
		Door de match nader te specificeren wordt zeker gesteld dat het
		juiste element gevonden wordt.
	-->
	<!-- ISO 2 Metadata ID MD_Metadata.fileIdentifier -->
	<xsl:template match="gmd:MD_Metadata/gmd:fileIdentifier/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata ID</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 2 Metadata ID'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 3 language -->
	<xsl:template match="gmd:language/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">taal</xsl:with-param>
			<xsl:with-param name="picklist">picklist_LanguageCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 3 taal [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 5 Metadata ID MD_Metadata.parentIdentifier -->
	<xsl:template match="gmd:MD_Metadata/gmd:parentIdentifier/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Parent ID</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 5 Parent ID Verplicht als er een dataset met hogere hiërarchie bestaat'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 6 Metadata hiërarchieniveau MD_Metadata.hierarchyLevel Codelijst: MD_ScopeCode (B.5.25) -->
	<xsl:template match="gmd:MD_Metadata/gmd:hierarchyLevel/gmd:MD_ScopeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Hiërarchieniveau</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ScopeCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 6 Hiërarchieniveau [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 7 Beschrijving hiërarchisch niveau MD_Metadata.hierarchyLevelName 
    <xsl:template match="gmd:MD_Metadata/gmd:hierarchyLevelName/gco:CharacterString">
        <xsl:call-template name="element">
            <xsl:with-param name="title">Beschrijving hiërarchisch niveau</xsl:with-param>
			<xsl:with-param name="readonly" select="$readonly"/>
        </xsl:call-template>
    </xsl:template> -->
	<!-- ISO 9 Metadata datum MD_Metadata.dateStamp -->
	<xsl:template match="gmd:dateStamp/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata datum</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 9 Metadata datum'"/>
			<xsl:with-param name="default-value" select="'Klik om te bewerken [jjjj-mm-dd]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 10 Metadatastandaard naam MD_Metadata.metadataStandardName -->
	<xsl:template match="gmd:metadataStandardName/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata standaard naam</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 10 Metadata standaard naam'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 11 Versie metadatastandaard naam  MD_Metadata.metadataStandardVersion -->
	<xsl:template match="gmd:metadataStandardVersion/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata standaard versie</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 11 Metadata standaard versie'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 25 Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.abstract -->
	<xsl:template match="gmd:abstract/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Samenvatting</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 25 Samenvatting'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 26 Doel van de vervaardiging Samenvatting MD_Metadata.identificationInfo>MD_DataIdentification.purpose -->
	<xsl:template match="gmd:purpose/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Doel van de vervaardiging</xsl:with-param>
			<xsl:with-param name="optionality" select="'optional'"/>
			<xsl:with-param name="help-text" select="'ISO 26 Doel van de vervaardiging'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 28 Status MD_Metadata.identificationInfo>MD_DataIdentification.status Codelijst MD_ProgressCode (B.5.23)-->
	<xsl:template match="gmd:status/gmd:MD_ProgressCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Status</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ProgressCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 28 Status [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 37 Ruimtelijk schema 
		MD_Metadata.identificationInfo>MD_DataIdentification.spatialRepresentationType Codelijst: MD_SpatialRepresentation TypeCode (B.5.26) -->
	<xsl:template match="gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Ruimtelijk schema</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_SpatialRepresentationTypeCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'optional'"/>
			<xsl:with-param name="help-text" select="'ISO 37 Ruimtelijk schema [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 40 karakterset characterSet Codelijst: MD_CharacterSetCode (B.5.10)-->
	<xsl:template match="gmd:characterSet/gmd:MD_CharacterSetCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">karakterset</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_CharacterSetCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'optional'"/>
			<xsl:with-param name="help-text" select="'ISO 40 karakterset [keuzelijst] Optioneel aangezien zowel utf 8 als utf16 voldoen aan de ISO/IEC 10646-1 standaard.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 41 Thema's MD_Metadata.identificationInfo>MD_DataIdentification.topicCategory Enumeratie: MD_TopicCategoryCode (B.5.27) -->
	<xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Thema's</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_TopicCategoryCode</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 41 Themas [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 53 + 55 +394 +395 Keywords and thesaurus -->
	<xsl:template name="keywordsThesaurus">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Trefwoorden en thesaurus</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:identificationInfo//gmd:descriptiveKeywords//gmd:keyword"/>
				<xsl:apply-templates select="gmd:identificationInfo//gmd:descriptiveKeywords//gmd:thesaurusName//gmd:title"/>
				<xsl:apply-templates select="gmd:identificationInfo//gmd:descriptiveKeywords//gmd:thesaurusName//gmd:date/gmd:CI_Date"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 53 Trefwoorden MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.keyword-->
	<xsl:template match="gmd:keyword/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Trefwoorden</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 53 Trefwoorden'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 55 MD_Metadata.identificationInfo>MD_DataIdentification.descriptiveKeywords>MD_Keywords.thesaurusName>CI_Citation.title -->
	<xsl:template match="gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Thesaurus</xsl:with-param>
			<xsl:with-param name="repeatable" select="'false'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 55 Thesaurus Verplicht als een keyword uit een thesaurus afkomstig is (voor INSPIRE datasets verplicht), bijbehorende datum en type dan ook verplicht.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 57 Toepassingsschaal 
		MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.equivalentScale>MD_RepresentativeFraction.denominator -->
	<xsl:template match="gmd:denominator/gco:Integer">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Toepassingsschaal</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO  57 Toepassingsschaal Verplicht als er een toepassingsschaal gespecificeerd kan worden (multipliciteit 2)'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 61 Resolution 
		MD_Metadata.identificationInfo>MD_DataIdentification.spatialResolution>MD_Resolution.distance -->
	<xsl:template match="gmd:distance/gco:Distance">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Resolutie</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 61 Resolutie Verplicht als er een resolutie gespecificeerd kan worden (multipliciteit 2)'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 68 Gebruiksbeperkingen MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_Constraints.useLimitation-->
	<xsl:template match="gmd:useLimitation/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Gebruiksbeperkingen</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 68 Gebruiksbeperkingen Verplicht om op zijn minst één van de vier elementen (juridische) toegangsrestricties, overige beperkingen, veiligheidsrestricties of gebruiksbeperkingen op te nemen.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 70 (Juridische) toegangsrestricties MD_Metadata.identificationInfo>MD_DataIdentification.resourceConstraints>MD_LegalConstraints.accessConstraints Codelijst MD_RestrictionCode (B.5.24)-->
	<xsl:template match="gmd:accessConstraints/gmd:MD_RestrictionCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">(juridische) toegangsrestricties</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_RestrictionCode</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 70 (juridische) toegangsrestricties [keuzelijst] Verplicht om op zijn minst één van de vier elementen (juridische) toegangsrestricties, overige beperkingen, veiligheidsrestricties of gebruiksbeperkingen op te nemen.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 72 resourceConstraints>MD_LegalConstraints.otherConstraints -->
	<xsl:template match="gmd:otherConstraints/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Overige beperkingen</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 72 Overige beperkingen Verplicht om op zijn minst één van de vier elementen (juridische) toegangsrestricties, overige beperkingen, veiligheidsrestricties of gebruiksbeperkingen op te nemen.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 74 resourceConstraints>MD_LegalConstraints.classification Codelijst MD_ClassificationCode (B.5.11)-->
	<xsl:template match="gmd:classification/gmd:MD_ClassificationCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Veiligheidsrestricties</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ClassificationCode</xsl:with-param>
			<xsl:with-param name="repeatable" select="'false'"/>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 74 Veiligheidsrestricties [keuzelijst] Verplicht om op zijn minst één van de vier elementen (juridische) toegangsrestricties, overige beperkingen, veiligheidsrestricties of gebruiksbeperkingen op te nemen.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 83 Algemene beschrijving herkomst MD_Metadata.dataQualityInfo>DQ_DataQuality.lineage>LI_Lineage.statement -->
	<xsl:template match="gmd:lineage//gmd:statement/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Algemene beschrijving herkomst</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 83 Algemene beschrijving herkomst'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 130 Specificatie 
			MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.specification>CI_Citation.title -->
	<xsl:template match="gmd:result//gmd:specification//gmd:title/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Specificatie</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 130 Specificatie Verplicht als de dataset een INSPIRE bron is of als de informatie is gemodelleerd volgens een specifiek informatie model.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 131 Verklaring 
			MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.explanation -->
	<xsl:template match="gmd:result//gmd:explanation/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Verklaring</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 131 Verklaring Verplicht als de dataset een INSPIRE bron is of als de informatie is gemodelleerd volgens een specifiek informatie model.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 132 Conformiteitsindicatie met de specificatie 
			MD_Metadata.dataQualityInfo>DQ_DataQuality.report>DQ_DomainConsistency.result>DQ_ConformanceResult.pass -->
	<xsl:template match="gmd:result//gmd:pass/gco:Boolean">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Conformiteitsindicatie</xsl:with-param>
			<xsl:with-param name="picklist">picklist_Boolean</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 132 Conformiteitsindicatie met de specificatie [keuzelijst] Verplicht als de dataset een INSPIRE bron is of als de informatie is gemodelleerd volgens een specifiek informatie model.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 137 
	<xsl:template match="gmd:dataQualityInfo//gmd:report//gmd:DQ_QuantitativeAttributeAccuracy//gmd:result//gmd:DQ_QuantitativeResult//gmd:value/gco:Record">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Geometrische nauwkeurigheid</xsl:with-param>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>-->
	<!-- ISO 139 Niveau kwaliteitsbeschrijving 
			MD_Metadata.dataQualityInfo>DQ_DataQuality.scope>DQ_Scope.level Codelijst: MD_ScopeCode (B.5.25)-->
	<xsl:template match="gmd:scope//gmd:level/gmd:MD_ScopeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Niveau kwaliteitsbeschrijving</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ScopeCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 139 Niveau kwaliteitsbeschrijving [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 207 Code referentiesysteem  MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.code EPSG codes-->
	<xsl:template match="gmd:referenceSystemIdentifier//gmd:code/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Code referentiesysteem</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 207 Code referentiesysteem'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem MD_Metadata.referenceSystemInfo>MD_ReferenceSystem.referenceSystemIdentifier>RS_Identifier.codeSpace-->
	<xsl:template match="gmd:referenceSystemIdentifier//gmd:codeSpace/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Verantwoordelijke organisatie voor namespace referentiesysteemm</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 208.1 Verantwoordelijke organisatie voor namespace referentiesysteem'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 344-347 omgrenzende rechthoek -->
	<xsl:template name="omgrenzendeRechthoek">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Omgrenzende rechthoek</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:westBoundLongitude/gco:Decimal"/>
				<xsl:apply-templates select="gmd:eastBoundLongitude/gco:Decimal"/>
				<xsl:apply-templates select="gmd:southBoundLatitude/gco:Decimal"/>
				<xsl:apply-templates select="gmd:northBoundLatitude/gco:Decimal"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 344 Minimum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.westBoundLongitude -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Minimum x</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 344 Minimum x-coördinaat De bounding box wordt in plaats van in WGS 84 weergegeven in ETRS 89 om aan te sluiten bij INSPIRE'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 345 Maximum x-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.eastBoundLongitude -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Maximum x</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 345 Maximum x-coördinaat De bounding box wordt in plaats van in WGS 84 weergegeven in ETRS 89 om aan te sluiten bij INSPIRE'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 346 Minimum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.southBoundLatitude -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Minimum y</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 346 Minimum y-coördinaat De bounding box wordt in plaats van in WGS 84 weergegeven in ETRS 89 om aan te sluiten bij INSPIRE'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 347 Maximum y-coördinaat MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.geographicElement>EX_GeographicBoundingBox.northBoundLatitude -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Maximum y</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 347 Maximum y-coördinaat De bounding box wordt in plaats van in WGS 84 weergegeven in ETRS 89 om aan te sluiten bij INSPIRE'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 351 Temporele dekking - BeginDatum/einddatum MD_Metadata.identificationInfo>MD_DataIdentification.extent>EX_Extent.temporalElement>EX_TemporalExtent.extent TM_Primitive(B.4.5) -->
	<xsl:template name="temporeleDekking">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Temporele dekking</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition"/>
				<xsl:apply-templates select="gmd:extent/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:begin/gml:TimeInstant/gml:timePosition">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Begindatum</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 351 Temporele dekking - begindatum'"/>
			<xsl:with-param name="default-value" select="'Klik om te bewerken [jjjj-mm-dd]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_TemporalExtent/gmd:extent/gml:TimePeriod/gml:end/gml:TimeInstant/gml:timePosition">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Einddatum</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 351 Temporele dekking - einddatum'"/>
			<xsl:with-param name="default-value" select="'Klik om te bewerken [jjjj-mm-dd]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 360 titel citation>title -->
	<xsl:template match="gmd:citation//gmd:title/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">titel</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 360 titel'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 361 Alternatieve titel citation>alternateTitle -->
	<xsl:template match="gmd:citation//gmd:alternateTitle/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">alternatieve titel</xsl:with-param>
			<xsl:with-param name="optionality" select="'optional'"/>
			<xsl:with-param name="help-text" select="'ISO 361 Alternatieve titel'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 365 MD_Metadata.identificationInfo>MD_DataIdentification.citation>CI_Citation.identifier>MD_Identifier.code -->
	<xsl:template match="gmd:identifier//gmd:code/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Unieke Identifier</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 365 Unieke Identifier'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 376 Naam organisatie metadata MD_Metadata.contact>CI_ResponsibleParty.organisationName -->
	<xsl:template match="gmd:MD_Metadata/gmd:contact">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Organisatie verantwoordelijk voor metadata [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:contact[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 376 Naam organisatie MD_Metadata.identificationInfo>MD_DataIdentification.pointOfContact>CI_ResponsibleParty.organisationName-->
	<xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Dataset verantwoordelijke organisatie [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 376 Naam distribuerende organisatie MD_Metadata.distributionInfo>MD_Distribution.distributor>MD_Distributor.distributorContact>CI_ResponsibleParty.organisationName -->
	<xsl:template match="gmd:distributionInfo//gmd:distributor">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Distribuerende organisatie [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:distributor[<xsl:value-of select="position()"/>]/gmd:MD_Distributor</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:MD_Distributor/gmd:distributorContact/gmd:CI_ResponsibleParty"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 394+395 dataset datum en referentie  -->
	<xsl:template match="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Dataset datum en referentie [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date[<xsl:value-of select="position()"/>]/gmd:CI_Date</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:CI_Date"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 397 URL dataset MD_Metadata.distributionInfo>MD_Distribution.transferOptions>MD_DigitalTransferOptions.online>CI_OnlineResource.linkage -->
	<xsl:template match="gmd:distributionInfo//gmd:transferOptions">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">URL dataset [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions[<xsl:value-of select="position()"/>]/gmd:MD_DigitalTransferOptions</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:MD_DigitalTransferOptions/gmd:online/gmd:CI_OnlineResource/gmd:linkage"/>
				<xsl:apply-templates select="gmd:MD_DigitalTransferOptions/gmd:online/gmd:CI_OnlineResource/gmd:protocol"/>
				<xsl:apply-templates select="gmd:MD_DigitalTransferOptions/gmd:online/gmd:CI_OnlineResource/gmd:name"/>
			</div>
		</div>
	</xsl:template>
	<!-- ISO 398 Protocol-->
	<xsl:template match="gmd:protocol/gmd:SV_ServiceType">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Protocol</xsl:with-param>
			<xsl:with-param name="picklist">picklist_SV_ServiceType</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 398 protocol [keuzelijst] Verplicht als er een URL is opgegeven.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 400 Naam -->
	<xsl:template match="gmd:name/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">naam</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 400 naam Verplicht gesteld voor de OGC:WMS, OGC:WFS en OGC:WCS.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- 
		Onderstaande templates worden op meerdere plaatsen gebruikt.
		De titels moeten algemeen blijven
	-->
	<!-- deze worden 3x gebruikt voor: distributor, pointOfContact, ResponsibleParty ! -->
	<!-- ISO 376 + 379 + 381 - 386 + 397 -->
	<xsl:template match="gmd:CI_ResponsibleParty">
		<xsl:apply-templates select="gmd:organisationName"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:deliveryPoint"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:city"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:administrativeArea"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:postalCode"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:country"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:electronicMailAddress"/>
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage"/>
		<xsl:apply-templates select="gmd:role"/>
	</xsl:template>
	<!-- ISO 376 naam organisatie -->
	<xsl:template match="gmd:organisationName/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">naam</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 376 naam organisatie'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 379 rol organisatie -->
	<xsl:template match="gmd:role/gmd:CI_RoleCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">rol</xsl:with-param>
			<xsl:with-param name="picklist">picklist_CI_RoleCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 379 Rol organisatie [keuzelijst]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 381 -->
	<xsl:template match="gmd:deliveryPoint/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">adres</xsl:with-param>
			<xsl:with-param name="optionality">optional</xsl:with-param>
			<xsl:with-param name="help-text" select="'ISO 381 adres'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 382 -->
	<xsl:template match="gmd:city/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">plaats</xsl:with-param>
			<xsl:with-param name="optionality">optional</xsl:with-param>
			<xsl:with-param name="help-text" select="'ISO 382 plaats'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 383 -->
	<xsl:template match="gmd:administrativeArea/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">provincie</xsl:with-param>
			<xsl:with-param name="optionality">optional</xsl:with-param>
			<xsl:with-param name="help-text" select="'ISO 383 provincie'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 384 -->
	<xsl:template match="gmd:postalCode/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">postcode</xsl:with-param>
			<xsl:with-param name="optionality">optional</xsl:with-param>
			<xsl:with-param name="help-text" select="'ISO 384 postcode'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 385 -->
	<xsl:template match="gmd:country/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">land</xsl:with-param>
			<xsl:with-param name="optionality">optional</xsl:with-param>
			<xsl:with-param name="help-text" select="'ISO 385 land'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 386 -->
	<xsl:template match="gmd:electronicMailAddress/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">e-mail</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'ISO 386 e-mail'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO nr. 397 -->
	<xsl:template match="gmd:linkage/gmd:URL">
		<xsl:call-template name="element">
			<xsl:with-param name="title">URL</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 397 link'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- einde ISO 381 - 386 + 397 -->
	<!-- ISO 394-395 CI_Date-->
	<xsl:template match="gmd:CI_Date">
		<xsl:apply-templates select="gmd:date/gco:Date"/>
		<xsl:apply-templates select="gmd:dateType/gmd:CI_DateTypeCode"/>
	</xsl:template>
	<!-- ISO 394 CI_Date.date-->
	<xsl:template match="gmd:date/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">datum</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 394 Datum Verplicht indien element waarvoor datum geldt verplicht is.'"/>
			<xsl:with-param name="default-value" select="'Klik om te bewerken [jjjj-mm-dd]'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ISO 395 CI_Date.dateType Codelijst: CI_DateTypeCode (B.5.2)-->
	<xsl:template match="gmd:dateType/gmd:CI_DateTypeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">datumtype</xsl:with-param>
			<xsl:with-param name="picklist">picklist_CI_DateTypeCode</xsl:with-param>
			<xsl:with-param name="optionality" select="'conditional'"/>
			<xsl:with-param name="help-text" select="'ISO 395 Datumtype [keuzelijst] Verplicht indien element waarvoor datum geldt verplicht is.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<!-- einde ISO 394-395 CI_Date-->
	<!-- ISO 19110 elementen -->
	<xsl:template match="gmx:name/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Name</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B1.1 name for this feature catalogue'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gmx:scope/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Scope</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B1.2 subject domain(s) of feature types defined in this feature catalogue'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gmx:versionNumber/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Version Number</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B1.4 version number of this feature catalogue, which may include both a major version number or letter and a sequence of minor release numbers or letters, such as “3.2.4a.” The format of this attribute may differ between cataloguing authorities.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gmx:versionDate/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Version Date</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="default-value" select="'Klik om te bewerken [jjjj-mm-dd]'"/>
			<xsl:with-param name="help-text" select="'B1.5 effective date of this feature catalogue.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gfc:producer">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Responsible organization</xsl:with-param>
				<xsl:with-param name="repeatable" select="'false'"/>
				<xsl:with-param name="section-path">/gfc:FC_FeatureCatalogue/gfc:producer[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="gfc:featureType">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Feature Type [<xsl:value-of select="position()"/>]</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gfc:FC_FeatureCatalogue/gfc:featureType[<xsl:value-of select="position()"/>]/gfc:FC_FeatureType</xsl:with-param>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>
			<div class="section-content">
		<xsl:apply-templates select="gfc:FC_FeatureType/gfc:typeName"/>
		<xsl:apply-templates select="gfc:FC_FeatureType/gfc:definition"/>
				<xsl:call-template name="cocs">
					<xsl:with-param name="object" select="."/>
					<xsl:with-param name="ftpos" select="position()"/>
				</xsl:call-template>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template name="cocs">
		<xsl:param name="object"/>
		<xsl:param name="ftpos"/>
		<xsl:for-each select="$object/gfc:FC_FeatureType/gfc:carrierOfCharacteristics">
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Property [<xsl:value-of select="$ftpos"/>.<xsl:value-of select="position()"/>]
					</xsl:with-param>
					<xsl:with-param name="repeatable" select="'true'"/>
					<xsl:with-param name="section-path">/gfc:FC_FeatureCatalogue/gfc:featureType[<xsl:value-of select="$ftpos"/>]/gfc:FC_FeatureType/gfc:carrierOfCharacteristics[<xsl:value-of select="position()"/>]/gfc:FC_FeatureAttribute</xsl:with-param>
					<xsl:with-param name="readonly" select="$readonly"/>
				</xsl:call-template>
				<div class="section-content">
					<xsl:apply-templates select="gfc:FC_FeatureAttribute"/>
				</div>
			</div>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="gfc:typeName/gco:LocalName">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Type Name</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B2.1 text string that uniquely identifies this feature type within the feature catalogue that contains this feature type'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gfc:memberName">
		<xsl:call-template name="element">
			<xsl:with-param name="title">naam</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B4.1 member name that locates this member within a feature type'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="gfc:definition/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Definition</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="help-text" select="'B2.2/B4.2 definition in a natural language.'"/>
			<xsl:with-param name="readonly" select="$readonly"/>
		</xsl:call-template>
	</xsl:template>	
	<xsl:template match="gfc:cardinality"></xsl:template>
</xsl:stylesheet>
