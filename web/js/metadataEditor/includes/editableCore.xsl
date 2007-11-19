<?xml version="1.0" encoding="UTF-16"?>
<xsl:stylesheet
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xlink="http://www.w3.org/1999/xlink"					
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:gco="http://www.isotc211.org/2005/gco"
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

	<!-- ISO nr. 3 -->
	<xsl:template match="gmd:MD_Metadata/gmd:language/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata taal</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 4 -->
	<xsl:template match="gmd:MD_Metadata/gmd:characterSet/gmd:MD_CharacterSetCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata karakterset</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_CharacterSetCode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 9 -->
	<xsl:template match="gmd:MD_Metadata/gmd:dateStamp/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata wijzigingsdatum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 10 -->
	<xsl:template match="gmd:metadataStandardName/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Naam metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 11 -->
	<xsl:template match="gmd:metadataStandardVersion/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Versie metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 25 -->
	<xsl:template match="gmd:abstract/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Samenvatting</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 28 -->
	<xsl:template match="gmd:status/gmd:MD_ProgressCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Status</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ProgressCode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr.37 -->
	<xsl:template match="gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Ruimtelijk schema</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_SpatialRepresentationTypeCode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 39 -->
	<xsl:template match="gmd:identificationInfo//gmd:language/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset taal</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 40 -->
	<xsl:template match="gmd:identificationInfo//gmd:characterSet">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset karakterset</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 41 -->
	<xsl:template match="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Thema</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_TopicCategoryCode</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>
			<!--<xsl:with-param name="path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory</xsl:with-param>-->
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 53 -->
	<!-- TODO: meerdere trefwoorden netjes weergeven -->
	<xsl:template match="gmd:keyword">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Trefwoorden</xsl:with-param>
			<xsl:with-param name="repeatable" select="'true'"/>			
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 68 -->
	<xsl:template match="gmd:useLimitation">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Gebruiksbeperkingen</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 70  bestaat niet?????? is vervangen door de volgende, lijkt me -->
	<xsl:template match="gmd:MD_SecurityConstraints/gmd:classification/gmd:MD_ClassificationCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">(Juridische) toegangsrestricties</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ClassificationCode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
		<!-- (Niet helemaal ISO nr. 13/186 referentieSysteem) -->
	<xsl:template name="referentieSysteem">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Referentie systeem</xsl:with-param>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier</xsl:with-param>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code"/>
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem/gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:codeSpace"/>
			</div>
		</div>
	</xsl:template>

	<!-- ISO nr. 207 -->
	<xsl:template match="gmd:code/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Code referentiesysteem</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 208.1 ? -->
	<xsl:template match="gmd:codeSpace/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">ID namespace referentiesysteem</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
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

	<xsl:template match="gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Organisatie verantwoordelijk voor dataset</xsl:with-param>
				<xsl:with-param name="repeatable" select="'true'"/>
				<xsl:with-param name="section-path">/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[<xsl:value-of select="position()"/>]/gmd:CI_ResponsibleParty</xsl:with-param>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="gmd:MD_Metadata/gmd:distributionInfo//gmd:distributor">
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
	
	<xsl:template match="gmd:CI_ResponsibleParty">
		<xsl:apply-templates select="gmd:organisationName"/>
		<xsl:apply-templates select="gmd:role"/>		
		<xsl:apply-templates select="gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage"/>
	</xsl:template>

	<!-- ISO nr. 360 --> <!-- gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/-->
	<xsl:template match="gmd:title/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 362/394 -->
	<xsl:template match="gmd:date/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset referentie datum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

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

	<!-- Omgrenzende rechthoek (is vervangen door polygon in ISO19139 !?! of polygon bestaat nu er naast)-->
	<xsl:template name="omgrenzendeRechthoek">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Omgrenzende rechthoek</xsl:with-param>
			</xsl:call-template>
			<div class="section-content">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal"/>
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal"/>
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal"/>							
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal"/>
			</div>
		</div>
	</xsl:template>

	<!-- ISO nr. 139 -->
	<xsl:template match="gmd:dataQualityInfo//gmd:level/gmd:MD_ScopeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Data kwaliteitsniveau</xsl:with-param>
			<xsl:with-param name="picklist">picklist_MD_ScopeCode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 137 -->
	<xsl:template match="gmd:dataQualityInfo//gmd:DQ_QuantitativeResult//gmd:value">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 83 -->
	<xsl:template match="gmd:lineage//gmd:statement/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Algemene beschrijving herkomst</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 344 -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Minimum x</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 345 -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Maximum x</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 346 -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Minimum y</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 347 -->
	<xsl:template match="gmd:identificationInfo//gmd:extent//gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Maximum y</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>
	
