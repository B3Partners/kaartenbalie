<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xlink="http://www.w3.org/1999/xlink"					
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:gco="http://www.isotc211.org/2005/gco"
					exclude-result-prefixes="gmd gco"
					>

	<!-- This parameter must be set by the browser -->
	<xsl:param name="basePath"/>

	<!-- template library to use for making element editable -->
	<xsl:include href="includes/editableElements.xsl"/>
	<!-- template library to use for showing basic types of ISO 19139 -->
	<!--<xsl:include href="includes/editableBasicTypes.xsl"/>-->

	<xsl:output	
					doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
					doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN"
					method="xml" omit-xml-declaration="no" indent="yes"
					/>
	
				
				

	<!--
	Auteur: Erik van de Pol. B3Partners.

	Beschrijving stylesheet:
	In het algemeen geldt dat voor elke property (waarde/xml-node/xml-tag) een apart template is gemaakt.
	-->

	
	<xsl:template match="/">
		<xsl:call-template name="MD_Metadata"/>
	</xsl:template>


	<xsl:template name="MD_Metadata">
		<div id="editDocRoot" changed="false">
			<div>
				<table id="main-menu" width="300" height="30" cellspacing="0" cellpadding="0">
					<tr>
						<td id="overzicht-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor een overzicht van de dataset">Overzicht</td>
						<td id="attributen-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de attributen van de dataset">Attributen</td>
						<td id="specificaties-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de specificaties van de dataset">Specificaties</td>
					</tr>
				</table>
			</div>
			
			<div id="properties-box">
				<xsl:call-template name="overzicht-tab"/>
				<xsl:call-template name="attributen-tab"/>
				<xsl:call-template name="specificaties-tab"/>
			</div>
			
		</div>
	</xsl:template>
	
	<!-- ============== -->
	<!-- TAB DEFINITIONS -->
	<!-- ============== -->

	<xsl:template name="overzicht-tab">
		<div id="overzicht" class="tab-definition" style="display:block">
		
			<!--
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:language/gco:CharacterString"/>
			<xsl:variable name="language">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:language/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$language = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Metadata taal</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/language/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:language/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:characterSet/gco:CharacterString"/>
			<xsl:variable name="characterSet">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:characterSet/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$characterSet = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Metadata karakterset</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/characterSet/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:characterSet/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dateStamp/gco:Date"/>
			<xsl:variable name="dateStamp">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dateStamp/gco:Date"/>
			</xsl:variable>
			<xsl:if test="$dateStamp = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Metadata wijzigingsdatum</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/dateStamp/gco:Date</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:dateStamp/gco:Date"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardName/gco:CharacterString"/>
			<xsl:variable name="metadataStandardName">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardName/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$metadataStandardName = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Naam metadata standaard</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/metadataStandardName/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:metadataStandardName/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardVersion/gco:CharacterString"/>
			<xsl:variable name="metadataStandardVersion">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardVersion/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$metadataStandardVersion = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Versie metadata standaard</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/metadataStandardVersion/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:metadataStandardVersion/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString"/>
			<xsl:variable name="abstract">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$abstract = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Samenvatting</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/abstract/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/gmd:MD_ProgressCode"/>
			<xsl:variable name="status">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/gmd:MD_ProgressCode"/>
			</xsl:variable>
			<xsl:if test="$status = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Status</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/status/MD_ProgressCode</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/gmd:MD_ProgressCode"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString"/>
			<xsl:variable name="title">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$title = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Dataset titel</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/citation/CI_Citation/title/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date"/>
			<xsl:variable name="date">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date"/>
			</xsl:variable>
			<xsl:if test="$date = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Dataset referentie datum</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/citation/CI_Citation/date/CI_Date/date/gco:Date</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/gco:CharacterString"/>
			<xsl:variable name="ds-language">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/gco:CharacterString"/>
			</xsl:variable>
			<xsl:if test="$ds-language = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Dataset taal</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/language/gco:CharacterString</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/gco:CharacterString"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode"/>
			<xsl:variable name="topicCategory">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode"/>
			</xsl:variable>
			<xsl:if test="$topicCategory = '' ">
				<xsl:call-template name="element">
					<xsl:with-param name="title">Thema's</xsl:with-param>
					<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/topicCategory/MD_TopicCategoryCode</xsl:with-param>
					<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/gmd:MD_TopicCategoryCode"/>
					<xsl:with-param name="force-default" select="'true'"/>
				</xsl:call-template>
			</xsl:if>
			
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Metadata verantwoordelijke organisatie</xsl:with-param>
				</xsl:call-template>
				<div class="content">
				
					<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString"/>
					<xsl:variable name="organisationName">
						<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString"/>
					</xsl:variable>
					<xsl:if test="$organisationName = '' ">
						<xsl:call-template name="element">
							<xsl:with-param name="title">Naam organisatie metadata</xsl:with-param>
							<xsl:with-param name="path">/MD_Metadata/contact/CI_ResponsibleParty/organisationName/gco:CharacterString</xsl:with-param>
							<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString"/>
							<xsl:with-param name="force-default" select="'true'"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:role/gmd:CI_RoleCode"/>
					<xsl:variable name="role">
						<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:role/gmd:CI_RoleCode"/>
					</xsl:variable>
					<xsl:if test="$role = '' ">
						<xsl:call-template name="element">
							<xsl:with-param name="title">Rol organisatie metadata</xsl:with-param>
							<xsl:with-param name="path">/MD_Metadata/contact/CI_ResponsibleParty/role/CI_RoleCode</xsl:with-param>
							<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:contact/gmd:CI_ResponsibleParty/gmd:role/gmd:CI_RoleCode"/>
							<xsl:with-param name="force-default" select="'true'"/>
						</xsl:call-template>
					</xsl:if>
					
					<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage/gmd:URL"/>
					<xsl:variable name="linkage">
						<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage/gmd:URL"/>
					</xsl:variable>
					<xsl:if test="$linkage = '' ">
						<xsl:call-template name="element">
							<xsl:with-param name="title">URL metadata organisatie</xsl:with-param>
							<xsl:with-param name="path">/MD_Metadata/identificationInfo/MD_DataIdentification/pointOfContact/CI_ResponsibleParty/contactInfo/CI_Contact/onlineResource/CI_OnlineResource/linkage/URL</xsl:with-param>
							<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage/gmd:URL"/>
							<xsl:with-param name="force-default" select="'true'"/>
						</xsl:call-template>
					</xsl:if>
					
				</div>
			</div>
			<xsl:call-template name="br"/>	
			
			<div class="section">
				<xsl:call-template name="section-title">
					<xsl:with-param name="title">Referentie systeem</xsl:with-param>
				</xsl:call-template>
				<div class="content">
					<xsl:apply-templates select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem//gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code/gco:CharacterString"/>
					<xsl:variable name="linkage">
						<xsl:apply-templates select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem//gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code/gco:CharacterString"/>
					</xsl:variable>
					<xsl:if test="$linkage = '' ">
						<xsl:call-template name="element">
							<xsl:with-param name="title">Code referentiesysteem</xsl:with-param>
							<xsl:with-param name="path">/MD_Metadata/referenceSystemInfo/MD_ReferenceSystem/referenceSystemIdentifier/RS_Identifier/code/gco:CharacterString</xsl:with-param>
							<xsl:with-param name="value" select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem//gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code/gco:CharacterString"/>
							<xsl:with-param name="force-default" select="'true'"/>
						</xsl:call-template>
					</xsl:if>

				</div>
			</div>
			<xsl:call-template name="br"/>-->
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:language"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:characterSet"/>

			<xsl:call-template name="metadataVerantwoordelijkeOrganisatie"/>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dateStamp"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardName"/>			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardVersion"/>
			
			<xsl:call-template name="referentieSysteem"/>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date"/>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status"/>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:descriptiveKeywords//gmd:keyword"/>			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:useLimitation"/>
			<!--<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:accessConstraints"/>-->
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:characterSet"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory"/>

			<xsl:call-template name="omgrenzendeRechthoek"/>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dataQualityInfo//gmd:scope//gmd:level"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dataQualityInfo//gmd:DQ_QuantitativeResult//gmd:value"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:lineage//gmd:statement"/>

		</div>
	</xsl:template>
	
	<xsl:template name="attributen-tab">
		<div id="attributen" class="tab-definition" style="display:block">

		</div>
	</xsl:template>
	
	<xsl:template name="specificaties-tab">
		<div id="specificaties" class="tab-definition" style="display:block">

		</div>
	</xsl:template>
	

	<!-- ============ -->
	<!-- TAB CONTENT -->
	<!-- ============ -->

	

	<!-- OVERZICHT TAB -->

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
	<xsl:template match="gmd:MD_Metadata/gmd:characterSet/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Metadata karakterset</xsl:with-param>
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
	<xsl:template match="gmd:status/gco:MD_ProgressCode"><!-- TODO: picklist maken. Documentatie over inhoud picklist? -->
		<xsl:call-template name="element">
			<xsl:with-param name="title">Status</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr.37 -->
	<xsl:template match="gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Ruimtelijk schema</xsl:with-param>
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
	<xsl:template match="gmd:identificationInfo//gmd:topicCategory/gmd:MD_TopicCategoryCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Thema's</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 53 -->
	<!-- TODO: meerdere trefwoorden netjes weergeven -->
	<xsl:template match="gmd:keyword">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Trefwoorden</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 68 -->
	<xsl:template match="gmd:useLimitation">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Gebruiksbeperkingen</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 70  bestaat niet??????-->
	<xsl:template match="gmd:accessConstraints">
		<xsl:call-template name="element">
			<xsl:with-param name="title">(Juridische) toegangsrestricties</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
		<!-- (Niet helemaal ISO nr. 13/186referentieSysteem) -->
	<xsl:template name="referentieSysteem">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Referentie systeem</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem//gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code"/>
				<!--<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_RespParty/gmd:pointOfContact/gmd:role"/>-->
			</div>
		</div>
		<xsl:call-template name="br"/>		
	</xsl:template>

	<!-- ISO nr. 207 -->
	<xsl:template match="gmd:code/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Code referentiesysteem</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 208.1 ????? Onvindbaar -->
	<!--<xsl:template match="gmd:Title">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>-->
	
	<!-- (Niet helemaal ISO nr. 8) -->
	<!-- via contact (met xlink; uitzoeken hoe dat precies moet) of identificationInfo te bereiken -->
	<xsl:template name="metadataVerantwoordelijkeOrganisatie">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Metadata verantwoordelijke organisatie</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:organisationName"/>
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:role"/>		
				<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:onlineResource/gmd:CI_OnlineResource/gmd:linkage"/>
			</div>
		</div>
		<xsl:call-template name="br"/>
	</xsl:template>

	<!-- ISO nr. 360 --> <!-- gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/-->
	<xsl:template match="gmd:title/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 376 -->
	<xsl:template match="gmd:organisationName/gco:CharacterString">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Naam organisatie metadata</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 379 -->
	<xsl:template match="gmd:role/gmd:CI_RoleCode">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Rol organisatie metadata</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 362/394 -->
	<xsl:template match="gmd:date/gco:Date">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Dataset referentie datum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 397 -->
	<xsl:template match="gmd:linkage/gmd:URL">
		<xsl:call-template name="element">
			<xsl:with-param name="title">URL metadata organisatie</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- Omgrenzende rechthoek (is vervangen door polygon in ISO19139 !?!)-->
	<xsl:template name="omgrenzendeRechthoek">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Omgrenzende rechthoek</xsl:with-param>
			</xsl:call-template>
			<div class="content">
			
			</div>
		</div>
		<xsl:call-template name="br"/>
	</xsl:template>

	<!-- ISO nr. 139 -->
	<xsl:template match="gmd:level">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Data kwaliteitsniveau</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 137 -->
	<xsl:template match="gmd:DQ_QuantitativeResult//gmd:value">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 83 -->
	<!--<xsl:template match="gmd:lineage//gmd:statement">
		<xsl:call-template name="element">
			<xsl:with-param name="title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>-->
	

</xsl:stylesheet>
