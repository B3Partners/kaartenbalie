<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"					
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:gco="http://www.isotc211.org/2005/gco"
					exclude-result-prefixes="gco gmd"
					>

	<xsl:output	
					doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
					doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN"
					method="xml" omit-xml-declaration="no" indent="yes"
					/>
	
	<!-- This parameter is set by the browser -->
	<xsl:param name="basePath"/>
				
				
	<!-- template library to use for making element editable -->
	<xsl:include href="includes/editableElements.xsl"/>
	<!-- template library to use for showing basic types of ISO 19139 -->
	<xsl:include href="includes/editableBasicTypes.xsl"/>
				

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
			<xsl:value-of select="$basePath"/>
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
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:language"/>			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:characterSet"/>
			
			<xsl:call-template name="metadataVerantwoordelijkeOrganisatie"/>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact//gmd:CI_OnlineResource//gmd:linkage"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:dateStamp"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardName"/>			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:metadataStandardVersion"/>
			
			<xsl:call-template name="referentieSysteem"/>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:title"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:date"/>

			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:abstract"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:status"/>
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:descriptiveKeywords//gmd:keyword"/>			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:useLimitation"/>
			<!--<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:accessConstraints"/>-->
			
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:language"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:characterSet"/>
			<xsl:apply-templates select="/gmd:MD_Metadata/gmd:identificationInfo//gmd:topicCategory"/>

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
	<xsl:template match="gmd:MD_Metadata/gmd:language">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Metadata taal</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 4 -->
	<xsl:template match="gmd:MD_Metadata/gmd:characterSet">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Metadata karakterset</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 9 -->
	<xsl:template match="gmd:MD_Metadata/gmd:dateStamp">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Metadata wijzigingsdatum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 10 -->
	<xsl:template match="gmd:metadataStandardName">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naam metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 11 -->
	<xsl:template match="gmd:metadataStandardVersion">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Versie metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 25 -->
	<xsl:template match="gmd:abstract">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Samenvatting</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 28 -->
	<xsl:template match="gmd:status">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Status</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr.37 -->
	<xsl:template match="gmd:spatialRepresentationType/gmd:MD_SpatialRepresentationTypeCode">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Ruimtelijk schema</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 39 -->
	<xsl:template match="gmd:identificationInfo//gmd:language">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset taal</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 40 -->
	<xsl:template match="gmd:identificationInfo//gmd:characterSet">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset karakterset</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 41 -->
	<xsl:template match="gmd:identificationInfo//gmd:topicCategory">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Thema's</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 53 -->
	<!-- TODO: meerdere trefwoorden netjes weergeven -->
	<xsl:template match="gmd:keyword">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Trefwoorden</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 68 -->
	<xsl:template match="gmd:useLimitation">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Gebruiksbeperkingen</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 70  bestaat niet??????-->
	<xsl:template match="gmd:accessConstraints">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">(Juridische) toegangsrestricties</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
		<!-- (Niet helemaal ISO nr. 13/186referentieSysteem) -->
	<xsl:template name="referentieSysteem">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Referentie systeem</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="/gmd:MD_Metadata//gmd:MD_ReferenceSystem//gmd:referenceSystemIdentifier//gmd:code"/>
				<!--<xsl:apply-templates select="/gmd:MD_Metadata/gmd:contact/gmd:CI_RespParty/gmd:pointOfContact/gmd:role"/>-->
			</div>
		</div>
		<xsl:call-template name="get-br"/>		
	</xsl:template>

	<!-- ISO nr. 207 -->
	<xsl:template match="gmd:code">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Code referentiesysteem</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- ISO nr. 208.1 ????? Onvindbaar -->
	<!--<xsl:template match="gmd:Title">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>-->
	
	<!-- (Niet helemaal ISO nr. 8) -->
	<xsl:template name="metadataVerantwoordelijkeOrganisatie">
		<div class="section">
			<xsl:call-template name="section-title">
				<xsl:with-param name="title">Metadata verantwoordelijke organisatie</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="/gmd:MD_Metadata//gmd:contact//gmd:CI_ResponsibleParty//gmd:organisationName"/>
				<xsl:apply-templates select="/gmd:MD_Metadata//gmd:contact//gmd:CI_ResponsibleParty//gmd:role"/>		
			</div>
		</div>
		<xsl:call-template name="get-br"/>		
	</xsl:template>

	<!-- ISO nr. 360 -->
	<xsl:template match="gmd:title">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 376 -->
	<xsl:template match="gmd:organisationName">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naam organisatie metadata</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 379 -->
	<xsl:template match="gmd:role">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Rol organisatie metadata</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 362/394 -->
	<xsl:template match="gmd:date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset referentie datum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 397 -->
	<xsl:template match="gmd:linkage">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">URL metadata organisatie</xsl:with-param>
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
		<xsl:call-template name="get-br"/>
	</xsl:template>

	<!-- ISO nr. 139 -->
	<xsl:template match="gmd:level">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Data kwaliteitsniveau</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 137 -->
	<xsl:template match="gmd:DQ_QuantitativeResult//gmd:value">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- ISO nr. 83 -->
	<xsl:template match="gmd:lineage//gmd:statement">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>


</xsl:stylesheet>
