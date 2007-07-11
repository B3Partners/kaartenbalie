<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
				version="1.0"
				xmlns="http://www.w3.org/1999/xhtml"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				>

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

	<!-- template library to use for making element editable -->
	<xsl:include href="includes/editable_extension.xsl"/>
	<!-- template library to use for showing basic types of ISO 19139 -->
	<xsl:include href="includes/editableBasicTypes.xsl"/>	
	<!-- picklists -->
	<xsl:include href="Picklists/Metadata_Beheerder_Picklists.xsl"/>
	
	<xsl:template match="/MD_Metadata">
		<div id="editDocRoot" changed="false" onmousemove="mouseEvent()">
		
			<div>
				<table id="main-menu" width="300" height="30" cellspacing="0" cellpadding="0">
					<tr>
						<td id="overzicht-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor een overzicht van de dataset">Overzicht</td>
						<td id="attributen-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de attributen van de dataset">Attributen</td>
						<td id="specificaties-tab" class="tab-unselected" onmouseover="tabHover(this)" onclick="changeTab(this)" title="Klik hier voor informatie over de specificaties van de dataset">Specificaties</td>
					</tr>
				</table>
			</div>
			
			<xsl:variable name="background-class-name">
				<xsl:call-template name="get-background-class-name"/>
			</xsl:variable>

			<div id="properties-box" class="{$background-class-name}">
				<xsl:call-template name="overzicht-tab"/>
				<xsl:call-template name="attributen-tab"/>
				<xsl:call-template name="specificaties-tab"/>
			</div>
			
			<!-- oude picklists. NPS legacy -->
			<div class="hidden" id="picklists">
				<!-- hide picklists for use by picklist Javascript code-->
				<xsl:call-template name="intented_application_scale"/>
				<xsl:call-template name="language"/>
				<xsl:call-template name="producer_organisation"/>
				<xsl:call-template name="gegevens_hoofdgroep"/>
				<xsl:call-template name="spatial_schema_type_id"/>
				<xsl:call-template name="method"/>
				<xsl:call-template name="is_described_by"/>
				<xsl:call-template name="indirect_positioning_system_name"/>
				<xsl:call-template name="extent_status"/>
				<xsl:call-template name="point_of_contacts_role"/>
				<xsl:call-template name="organisation_role"/>
				<xsl:call-template name="restrictions_on_use"/>
				<xsl:call-template name="background"/>
			</div>
			
			<!-- images en pad naar sheet verplaatst naar omringende jsp -->
			
		</div>
	</xsl:template>
	
	<!-- ============== -->
	<!-- TAB DEFINITIONS -->
	<!-- ============== -->

	<xsl:template name="overzicht-tab">
		<div id="overzicht" class="tab-definition" style="display:block">
			<xsl:apply-templates select="/MD_Metadata/MD_Identification/citation/Title"/>
			<xsl:apply-templates select="/MD_Metadata/dateStamp"/>
			<xsl:apply-templates select="/MD_Metadata/metadataStandardName"/>			
			<xsl:apply-templates select="/MD_Metadata/metadataStandardVersion"/>
		</div>
	</xsl:template>
	
	<xsl:template name="attributen-tab">
		<div id="attributen" class="tab-definition" style="display:block">
			<xsl:apply-templates select="/metadata/dataset_description/data_definition/object_type"/>
		</div>
	</xsl:template>
	
	<xsl:template name="specificaties-tab">
		<div id="specificaties" class="tab-definition" style="display:block">
			<xsl:apply-templates select="/metadata/dataset_description/geokey_database/description"/>
			
			<xsl:apply-templates select="/metadata/dataset_description/spatial_reference_system/direct_positioning_system/map_projection_name"/>
			<xsl:apply-templates select="/metadata/dataset_description/planar_extent"/>
			<xsl:call-template name="get-br"/>
			<xsl:apply-templates select="/metadata/dataset_description/vertical_extent"/>
			<xsl:call-template name="get-br"/>
			<xsl:apply-templates select="/metadata/dataset_description/temporal_extent"/>
			<xsl:call-template name="separator"/>
			
			<xsl:apply-templates select="/metadata/dataset_description/quality_element"/>
			<xsl:call-template name="separator"/>
			
			<xsl:apply-templates select="/metadata/ESRI_NL"/>
		</div>
	</xsl:template>
	
	
	<!-- ============ -->
	<!-- TAB CONTENT -->
	<!-- ============ -->

	

	<!-- OVERZICHT TAB -->

	<xsl:template match="/MD_Metadata/MD_Identification/citation/Title">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Dataset titel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="/MD_Metadata/dateStamp">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Metadata wijzigingsdatum</xsl:with-param>
			<!-- mandatory of niet? opzoeken in xsd -->
			<!--<xsl:with-param name="optionality" select="'mandatory'"/>-->
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="/MD_Metadata/metadataStandardName">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naam metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="/MD_Metadata/metadataStandardVersion">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Versie metadata standaard</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	
	
	
	
	
	<xsl:template match="metadata/dataset_description/administrative_metadata/organisation_role">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Organisatie'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/administrative_metadata/organisation_role'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'true'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="administrative_metadata//organisation">
		<xsl:apply-templates select="abbreviated_organisation_name"/>
		<xsl:apply-templates select="name"/>
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Adresgegevens</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="phone"/>
				<xsl:apply-templates select="fax"/>
				<xsl:apply-templates select="email"/>
				<xsl:apply-templates select="www_home_page"/>
				<xsl:apply-templates select="organisation_address"/>
			</div>
		</div>
	</xsl:template>
	
	
	<xsl:template match="organisation_role/organisation/abbreviated_organisation_name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Verkorte naam organisatie</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="organisation_role/description">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Rol organisatie</xsl:with-param>
			<xsl:with-param name="picklist">organisation_role</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="organisation_role/organisation/name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Volledige naam organisatie</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/administrative_metadata/point_of_contacts_role">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Contactpersoon'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/administrative_metadata/point_of_contacts_role'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'true'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="point_of_contact">
		<xsl:apply-templates select="point_of_contact_name"/>
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Adresgegevens</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="phone"/>
				<xsl:apply-templates select="fax"/>
				<xsl:apply-templates select="email"/>
				<xsl:apply-templates select="www"/>
				<xsl:apply-templates select="point_of_contact_address"/>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/administrative_metadata/point_of_contacts_role/point_of_contact/point_of_contact_name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Contactpersoon</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/administrative_metadata/point_of_contacts_role/description">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Rol contactpersoon</xsl:with-param>
			<xsl:with-param name="picklist">point_of_contacts_role</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- Address -->
	<xsl:template match="point_of_contact_address">
		<xsl:call-template name="address"/>
	</xsl:template>

	<xsl:template match="organisation_address">
		<xsl:call-template name="address"/>
	</xsl:template>

	<xsl:template name="address">
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Postadres</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:call-template name="_address"/>
				<xsl:apply-templates select="visiting_address"/>
			</div>
		</div>				
	</xsl:template>
	
	<xsl:template match="visiting_address">
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Bezoekadres</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:call-template name="_address"/>
			</div>
		</div>				
	</xsl:template>
	
	<xsl:template name="_address">
		<xsl:apply-templates select="street"/>
		<xsl:apply-templates select="zip"/>
		<xsl:apply-templates select="city"/>
		<xsl:apply-templates select="country"/>
	</xsl:template>
	
	<xsl:template match="street">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Straat</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="zip">
		<xsl:variable name="zip_name">
			<xsl:if test="local-name(../.) = 'visiting_address'">Postcode</xsl:if>
			<xsl:if test="local-name(../.) != 'visiting_address'">Postbus</xsl:if>
		</xsl:variable>
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title" select="$zip_name"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="administrative_metadata//city">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Stad</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="administrative_metadata//country">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Land</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="phone">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Telefoonnummer</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="fax">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Faxnummer</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="email">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Emailadres</xsl:with-param>
			<xsl:with-param name="trailing_text"></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="mailtoAnchor">
			<xsl:with-param name="email"><xsl:value-of select="."/></xsl:with-param>
			<xsl:with-param name="name-shown">Schrijf email</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="www">
		<xsl:call-template name="www_home_page"/>
	</xsl:template>
	
	<xsl:template match="www_home_page" name="www_home_page">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Internetadres</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="anchor">
			<xsl:with-param name="href"><xsl:value-of select="."/></xsl:with-param>
			<xsl:with-param name="name-shown">Open webpagina</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	

	<xsl:template match="metadata/dataset_description/administrative_metadata/distribution">
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Distributie</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="restrictions_on_use"/>		
				<xsl:apply-templates select="copyright_owners"/>		
				<!-- <xsl:apply-templates select="unit_of_distribution"/>	-->
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="restrictions_on_use">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Juridische gebruiksbeperkingen</xsl:with-param>
			<xsl:with-param name="picklist">restrictions_on_use</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="copyright_owners">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Eigenaar copyright</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	



	<!-- ATTRIBUTEN TAB -->

	<xsl:template match="metadata/dataset_description/data_definition/object_type">
		<xsl:call-template name="section-title">
			<xsl:with-param name="section_Mtitle">Attributen</xsl:with-param>
		</xsl:call-template>
		<div class="content">
			<xsl:apply-templates select="attribute_type"/>
		</div>
    </xsl:template>
    
	<xsl:template match="metadata/dataset_description/data_definition/object_type/attribute_type">
		<xsl:call-template name="section-title">
			<xsl:with-param name="section_Mtitle">Attribuut</xsl:with-param>
		</xsl:call-template>
		<div class="content">
			<xsl:apply-templates select="attribute_type_name"/>
			<xsl:apply-templates select="attribute_type_definition"/>			
		</div>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/data_definition/object_type/attribute_type/attribute_type_name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naam</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/data_definition/object_type/attribute_type/attribute_type_definition">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Definitie</xsl:with-param>
		</xsl:call-template>
	</xsl:template>



	<!-- SPECIFICATIES TAB -->

	<xsl:template match="metadata/dataset_description/geokey_database/description">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Achtergrond</xsl:with-param>
			<xsl:with-param name="picklist">background</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="map_projection_name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Projectie</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- planar_extent -->
	<xsl:template match="metadata/dataset_description/planar_extent">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Horizontale dekking'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/planar_extent'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="extent_status">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Status van de dekking</xsl:with-param>
			<xsl:with-param name="picklist">extent_status</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="extent_date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Actualiteit van de dekking</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/planar_extent/geographic_area">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/planar_extent/geographic_area/area_name">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naam geografisch gebied</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/planar_extent/bounding_quadrangle">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Omgrenzende rechthoek'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/planar_extent/bounding_quadrangle'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="min_horizontal">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="max_horizontal">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="min_horizontal/position">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="max_horizontal/position">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="min_horizontal/position/x">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Minimum x</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="min_horizontal/position/y">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Minimum y</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="max_horizontal/position/x">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Maximum x</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="max_horizontal/position/y">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Maximum y</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- vertical_extent -->
	<xsl:template match="metadata/dataset_description/vertical_extent">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Verticale dekking'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/vertical_extent'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="minimum_height_value">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Minimale hoogte</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="maximum_height_value">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Maximale hoogte</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- temporal_extent -->
	<xsl:template match="metadata/dataset_description/temporal_extent">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Temporele dekking'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/temporal_extent'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/temporal_extent/period_range_details">
		<div class="content">
			<xsl:apply-templates select="period_start_date"/>
			<xsl:apply-templates select="period_end_date"/>		
			<xsl:apply-templates select="period_range_date_qualifier"/>				
		</div>
	</xsl:template>

	<xsl:template match="period_start_date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Van</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="period_end_date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Tot</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="period_range_date_qualifier">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Specificatie van de periode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- quality_element -->
	<xsl:template match="metadata/dataset_description/quality_element">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Kwaliteit (CEN4)'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/quality_element'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="quality_parameters">
		<xsl:apply-templates select="quality_indicator_set/quality_measure_set/measure_result/value"/>
	</xsl:template>
	
	<xsl:template match="quality_parameters/quality_indicator_set/quality_measure_set/measure_result/value">
		<xsl:variable name="title">
			<xsl:if test="../../../../name = 'Positional Accuracy'">
				<xsl:text>Geometrische nauwkeurigheid</xsl:text>
			</xsl:if>
			<xsl:if test="../../../../name = 'Completeness'">
				<xsl:text>Volledigheid</xsl:text>
			</xsl:if>
		</xsl:variable>
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title" select="$title"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="quality_text">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Kwaliteitsbeschrijving</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage/production">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Dataset herkomst'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/quality_element/lineage/production'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
		<xsl:call-template name="get-br"/>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage/production/producer_organisation">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Organisatie</xsl:with-param>
			<xsl:with-param name="picklist">producer_organisation</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/quality_element/lineage/production/method">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Inwinningsmethode</xsl:with-param>
			<xsl:with-param name="picklist">method</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/quality_element/lineage/production/date_of_production">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage/production/date_of_production/date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Inwinningsdatum</xsl:with-param>
			<xsl:with-param name="optionality" select="'mandatory'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage/source_and_processes">
		<xsl:apply-templates/>
		<xsl:call-template name="get-br"/>
	</xsl:template>
	
	<xsl:template match="metadata/dataset_description/quality_element/lineage/source_and_processes/source_history">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Uitgevoerde GIS-bewerking'"/>
			<xsl:with-param name="section_path" select="'metadata/dataset_description/quality_element/lineage/source_and_processes'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'true'"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/quality_element/lineage/source_and_processes/source_history/organisation">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Producent</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="metadata/dataset_description/quality_element/lineage/source_and_processes/source_history/method">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Bewerking</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	

	<!-- ESRI_NL -->
	
	<!-- Kwaliteit CEN3 -->
	<xsl:template match="metadata/ESRI_NL">
		<xsl:apply-templates select="kwaliteit"/>
		<xsl:apply-templates select="documentatie"/>
		<!--<xsl:apply-templates select="@*|node()[not(self::kwaliteit) and not(self::documentatie)]"/>-->
	</xsl:template>

	<xsl:template match="metadata/ESRI_NL/kwaliteit">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Kwaliteit (CEN3)'"/>
			<xsl:with-param name="section_path" select="'metadata/ESRI_NL/kwaliteit'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
		<xsl:call-template name="separator"/>
	</xsl:template>
	
	<xsl:template match="/metadata/ESRI_NL/kwaliteit/kwaliteitsbeschrijving">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Kwaliteitsbeschrijving</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/betrouwbaarheid">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Betrouwbaarheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/kwaliteitscontrole">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Kwaliteitscontrole uitgevoerd</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/nauwkeurigheid">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Geometrische nauwkeurigheid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/precisie">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Precisie'"/>
			<xsl:with-param name="section_path" select="'metadata/ESRI_NL/kwaliteit/precisie'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/precisie/systematische_fout">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Systematische fout</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/precisie/standaarddeviatie_xy">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Standaarddeviatie in x- en y-richting</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/precisie/standaarddeviatie_z">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Standaarddeviatie in z-richting</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/volledigheid">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Volledigheid'"/>
			<xsl:with-param name="section_path" select="'metadata/ESRI_NL/kwaliteit/volledigheid'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'false'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/volledigheid/aantal_objecten">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Naar aantal objecten</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
		
	<xsl:template match="metadata/ESRI_NL/kwaliteit/volledigheid/kenmerken_objecten">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">T.a.v. aantal kenmerken</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
		
	<xsl:template match="metadata/ESRI_NL/kwaliteit/volledigheid/geografische_extent">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Geografische extent</xsl:with-param>
		</xsl:call-template>
	</xsl:template>	
		
	<xsl:template match="metadata/ESRI_NL/kwaliteit/inwinningsmethode">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Inwinningsmethode</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="metadata/ESRI_NL/kwaliteit/inwinningsmethode/bijhoudingsbeleid">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Bijhoudingsbeleid</xsl:with-param>
		</xsl:call-template>
	</xsl:template>


	<!-- Documentatie -->
	<xsl:template match="metadata/ESRI_NL/documentatie">
		<xsl:call-template name="section">
			<xsl:with-param name="section_Mtitle" select="'Aanvullende documentatie'"/>
			<xsl:with-param name="section_path" select="'metadata/ESRI_NL/documentatie'"/>
			<xsl:with-param name="optionality" select="'mandatory'"/>
			<xsl:with-param name="expanding" select="'true'"/>
			<xsl:with-param name="expanded" select="'true'"/>
			<xsl:with-param name="repeatable" select="'true'"/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="document">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Beschrijving</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="document_link">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Link</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="anchor">
			<xsl:with-param name="name-shown">Volg link</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	

	<!-- tijd van saven kan hierin opgeslagen worden via vbscript code "stop_edit"; dus bij het veranderen van een waarde-->
	<!-- Actualiteit -->
	<!--
	<xsl:template match="metadata/dataset_description/metadata_reference">
		<div class="folder">
			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle">Actualiteit van de metadata</xsl:with-param>
			</xsl:call-template>
			<div class="content">
				<xsl:apply-templates select="entry_date"/>
				<xsl:apply-templates select="last_update_date"/>
			</div>
		</div>
		<xsl:call-template name="separator"/>
	</xsl:template>
	
	<xsl:template match="entry_date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Invoerdatum</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="last_update_date">
		<xsl:call-template name="edit_element">
			<xsl:with-param name="element_title">Laatste herziening</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	-->

	
</xsl:stylesheet>
