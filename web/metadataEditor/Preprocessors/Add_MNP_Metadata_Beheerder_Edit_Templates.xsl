<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	
	Beschrijving stylesheet:
	Bevat templates die ontbrekende nodes toevoegen om de mogelijkheid te bieden deze te editen in de stylesheet.
	-->

	<!--TEMPLATE: add_metadata--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_metadata">
		<xsl:element name="metadata"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_dataset_description"></xsl:call-template>
			<xsl:call-template name="add_ESRI_NL"></xsl:call-template>
			<xsl:call-template name="add_NPS_Info"></xsl:call-template>
			<xsl:call-template name="add_idinfo"></xsl:call-template>
			<xsl:call-template name="add_distinfo"></xsl:call-template>
			<xsl:call-template name="add_metainfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_NPS_Info--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_NPS_Info">
		<xsl:element name="NPS_Info"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_MetaPurp"></xsl:call-template>
			<xsl:call-template name="add_NPS_Unit"></xsl:call-template>
			<xsl:call-template name="add_DatStore"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_MetaPurp--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_MetaPurp">
		<xsl:element name="MetaPurp"></xsl:element>
	</xsl:template><!--TEMPLATE: add_NPS_Unit--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_NPS_Unit">
		<xsl:element name="NPS_Unit"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_UnitCode"></xsl:call-template>
			<xsl:call-template name="add_UnitType"></xsl:call-template>
			<xsl:call-template name="add_Org_Code"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_UnitCode--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_UnitCode">
		<xsl:element name="UnitCode"></xsl:element>
	</xsl:template><!--TEMPLATE: add_UnitType--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_UnitType">
		<xsl:element name="UnitType"></xsl:element>
	</xsl:template><!--TEMPLATE: add_Org_Code--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_Org_Code">
		<xsl:element name="Org_Code"></xsl:element>
	</xsl:template><!--TEMPLATE: add_DatStore--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_DatStore">
		<xsl:element name="DatStore"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_Category"></xsl:call-template>
			<xsl:call-template name="add_DataSite"></xsl:call-template>
			<xsl:call-template name="add_DSteward"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_Category--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_Category">
		<xsl:element name="Category"></xsl:element>
	</xsl:template><!--TEMPLATE: add_DataSite--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_DataSite">
		<xsl:element name="DataSite"></xsl:element>
	</xsl:template><!--TEMPLATE: add_DSteward--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_DSteward">
		<xsl:element name="DSteward"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_cntinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_cntinfo">
		<xsl:element name="cntinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntperp"></xsl:call-template>
			<xsl:call-template name="add_cntorgp"></xsl:call-template>
			<xsl:call-template name="add_cntpos"></xsl:call-template>
			<xsl:call-template name="add_cntaddr"></xsl:call-template>
			<xsl:call-template name="add_cntvoice"></xsl:call-template>
			<xsl:call-template name="add_cnttdd"></xsl:call-template>
			<xsl:call-template name="add_cntfax"></xsl:call-template>
			<xsl:call-template name="add_cntemail"></xsl:call-template>
			<xsl:call-template name="add_hours"></xsl:call-template>
			<xsl:call-template name="add_cntinst"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_cntperp--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_cntperp">
		<xsl:element name="cntperp"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntper"></xsl:call-template>
			<xsl:call-template name="add_cntorg"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_cntorgp--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_cntorgp">
		<xsl:element name="cntorgp"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntorg"></xsl:call-template>
			<xsl:call-template name="add_cntper"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_cntper--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntper">
		<xsl:element name="cntper"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntorg--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntorg">
		<xsl:element name="cntorg"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntpos--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntpos">
		<xsl:element name="cntpos"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntaddr--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_cntaddr">
		<xsl:element name="cntaddr"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_addrtype"></xsl:call-template>
			<xsl:call-template name="add_address"></xsl:call-template>
			<xsl:call-template name="add_city"></xsl:call-template>
			<xsl:call-template name="add_state"></xsl:call-template>
			<xsl:call-template name="add_postal"></xsl:call-template>
			<xsl:call-template name="add_country"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_addrtype--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_addrtype">
		<xsl:element name="addrtype"></xsl:element>
	</xsl:template><!--TEMPLATE: add_address--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_address">
		<xsl:element name="address"></xsl:element>
	</xsl:template><!--TEMPLATE: add_city--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_city">
		<xsl:element name="city"></xsl:element>
	</xsl:template><!--TEMPLATE: add_state--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_state">
		<xsl:element name="state"></xsl:element>
	</xsl:template><!--TEMPLATE: add_postal--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_postal">
		<xsl:element name="postal"></xsl:element>
	</xsl:template><!--TEMPLATE: add_country--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_country">
		<xsl:element name="country"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntvoice--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntvoice">
		<xsl:element name="cntvoice"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cnttdd--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cnttdd">
		<xsl:element name="cnttdd"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntfax--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntfax">
		<xsl:element name="cntfax"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntemail--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntemail">
		<xsl:element name="cntemail"></xsl:element>
	</xsl:template><!--TEMPLATE: add_hours--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_hours">
		<xsl:element name="hours"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cntinst--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cntinst">
		<xsl:element name="cntinst"></xsl:element>
	</xsl:template><!--TEMPLATE: add_idinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_idinfo">
		<xsl:element name="idinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_citation"></xsl:call-template>
			<xsl:call-template name="add_descript"></xsl:call-template>
			<xsl:call-template name="add_timeperd"></xsl:call-template>
			<xsl:call-template name="add_status"></xsl:call-template>
			<xsl:call-template name="add_spdom"></xsl:call-template>
			<xsl:call-template name="add_keywords"></xsl:call-template>
			<xsl:call-template name="add_accconst"></xsl:call-template>
			<xsl:call-template name="add_useconst"></xsl:call-template>
			<xsl:call-template name="add_ptcontac"></xsl:call-template>
			<xsl:call-template name="add_browse"></xsl:call-template>
			<xsl:call-template name="add_crossref"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_citation--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_citation">
		<xsl:element name="citation"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_citeinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_citeinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_citeinfo">
		<xsl:element name="citeinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_origin"></xsl:call-template>
			<xsl:call-template name="add_pubdate"></xsl:call-template>
			<xsl:call-template name="add_pubtime"></xsl:call-template>
			<xsl:call-template name="add_title"></xsl:call-template>
			<xsl:call-template name="add_edition"></xsl:call-template>
			<xsl:call-template name="add_serinfo"></xsl:call-template>
			<xsl:call-template name="add_pubinfo"></xsl:call-template>
			<xsl:call-template name="add_othercit"></xsl:call-template>
			<xsl:call-template name="add_onlink"></xsl:call-template>
			<xsl:call-template name="add_BibKeyID"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_origin--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_origin">
		<xsl:element name="origin"></xsl:element>
	</xsl:template><!--TEMPLATE: add_pubdate--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_pubdate">
		<xsl:element name="pubdate"></xsl:element>
	</xsl:template><!--TEMPLATE: add_pubtime--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_pubtime">
		<xsl:element name="pubtime"></xsl:element>
	</xsl:template><!--TEMPLATE: add_title--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_title">
		<xsl:element name="title"></xsl:element>
	</xsl:template><!--TEMPLATE: add_edition--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_edition">
		<xsl:element name="edition"></xsl:element>
	</xsl:template><!--TEMPLATE: add_serinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_serinfo">
		<xsl:element name="serinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_sername"></xsl:call-template>
			<xsl:call-template name="add_issue"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_sername--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_sername">
		<xsl:element name="sername"></xsl:element>
	</xsl:template><!--TEMPLATE: add_issue--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_issue">
		<xsl:element name="issue"></xsl:element>
	</xsl:template><!--TEMPLATE: add_pubinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_pubinfo">
		<xsl:element name="pubinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_pubplace"></xsl:call-template>
			<xsl:call-template name="add_publish"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_pubplace--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_pubplace">
		<xsl:element name="pubplace"></xsl:element>
	</xsl:template><!--TEMPLATE: add_publish--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_publish">
		<xsl:element name="publish"></xsl:element>
	</xsl:template><!--TEMPLATE: add_othercit--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_othercit">
		<xsl:element name="othercit"></xsl:element>
	</xsl:template><!--TEMPLATE: add_onlink--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_onlink">
		<xsl:element name="onlink"></xsl:element>
	</xsl:template><!--TEMPLATE: add_BibKeyID--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_BibKeyID">
		<xsl:element name="BibKeyID"></xsl:element>
	</xsl:template><!--TEMPLATE: add_descript--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_descript">
		<xsl:element name="descript"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_abstract"></xsl:call-template>
			<xsl:call-template name="add_purpose"></xsl:call-template>
			<xsl:call-template name="add_supplinf"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_abstract--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_abstract">
		<xsl:element name="abstract"></xsl:element>
	</xsl:template><!--TEMPLATE: add_purpose--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_purpose">
		<xsl:element name="purpose"></xsl:element>
	</xsl:template><!--TEMPLATE: add_supplinf--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_supplinf">
		<xsl:element name="supplinf"></xsl:element>
	</xsl:template><!--TEMPLATE: add_timeperd--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_timeperd">
		<xsl:element name="timeperd"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_timeinfo"></xsl:call-template>
			<xsl:call-template name="add_current"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_timeinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_timeinfo">
		<xsl:element name="timeinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_sngdate"></xsl:call-template>
			<xsl:call-template name="add_rngdates"></xsl:call-template>
			<xsl:call-template name="add_mdattim"></xsl:call-template>			
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_sngdate--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_sngdate">
		<xsl:element name="sngdate"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_caldate"></xsl:call-template>
			<xsl:call-template name="add_time"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_caldate--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_caldate">
		<xsl:element name="caldate"></xsl:element>
	</xsl:template><!--TEMPLATE: add_time--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_time">
		<xsl:element name="time"></xsl:element>
	</xsl:template><!--TEMPLATE: add_rngdates--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_rngdates">
		<xsl:element name="rngdates"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_begdate"></xsl:call-template>
			<xsl:call-template name="add_enddate"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_begdate--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_begdate">
		<xsl:element name="begdate"></xsl:element>
	</xsl:template><!--TEMPLATE: add_enddate--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_enddate">
		<xsl:element name="enddate"></xsl:element>
	</xsl:template><!--TEMPLATE: add_mdattim--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_mdattim">
		<xsl:element name="mdattim"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_sngdate"></xsl:call-template>
			<xsl:call-template name="add_sngdate"></xsl:call-template>			
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_current--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_current">
		<xsl:element name="current"></xsl:element>
	</xsl:template><!--TEMPLATE: add_status--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_status">
		<xsl:element name="status"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_progress"></xsl:call-template>
			<xsl:call-template name="add_update"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_progress--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_progress">
		<xsl:element name="progress"></xsl:element>
	</xsl:template><!--TEMPLATE: add_update--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_update">
		<xsl:element name="update"></xsl:element>
	</xsl:template><!--TEMPLATE: add_spdom--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_spdom">
		<xsl:element name="spdom"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_bounding"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_bounding--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_bounding">
		<xsl:element name="bounding"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_westbc"></xsl:call-template>
			<xsl:call-template name="add_eastbc"></xsl:call-template>
			<xsl:call-template name="add_northbc"></xsl:call-template>
			<xsl:call-template name="add_southbc"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_westbc--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_westbc">
		<xsl:element name="westbc"></xsl:element>
	</xsl:template><!--TEMPLATE: add_eastbc--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_eastbc">
		<xsl:element name="eastbc"></xsl:element>
	</xsl:template><!--TEMPLATE: add_northbc--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_northbc">
		<xsl:element name="northbc"></xsl:element>
	</xsl:template><!--TEMPLATE: add_southbc--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_southbc">
		<xsl:element name="southbc"></xsl:element>
	</xsl:template><!--TEMPLATE: add_keywords--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_keywords">
		<xsl:element name="keywords"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_theme"></xsl:call-template>
			<xsl:call-template name="add_place"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_theme--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_theme">
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cat_themekt"></xsl:call-template>
			<xsl:call-template name="add_cat_themekey"></xsl:call-template>
		</xsl:element>
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_iso_themekt"></xsl:call-template>
			<xsl:call-template name="add_iso_themekey"></xsl:call-template>
		</xsl:element>
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_themekt"></xsl:call-template>
			<xsl:call-template name="add_themekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_simple_theme--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_simple_theme">
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_themekt"></xsl:call-template>
			<xsl:call-template name="add_themekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_cat_theme--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_cat_theme">
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cat_themekt"></xsl:call-template>
			<xsl:call-template name="add_cat_themekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_iso_theme--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_iso_theme">
		<xsl:element name="theme"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_iso_themekt"></xsl:call-template>
			<xsl:call-template name="add_iso_themekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_cat_themekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cat_themekt">
		<xsl:element name="themekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_cat_themekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_cat_themekey">
		<xsl:element name="themekey">
		<xsl:attribute name="picklist"></xsl:attribute>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_iso_themekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_iso_themekt">
		<xsl:element name="themekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_iso_themekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_iso_themekey">
		<xsl:element name="themekey">
		<xsl:attribute name="picklist"></xsl:attribute>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_themekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_themekt">
		<xsl:element name="themekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_themekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_themekey">
		<xsl:element name="themekey"></xsl:element>
	</xsl:template><!--TEMPLATE: add_place--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_place">
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_name_placekt"></xsl:call-template>
			<xsl:call-template name="add_name_placekey"></xsl:call-template>
		</xsl:element>
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_code_placekt"></xsl:call-template>
			<xsl:call-template name="add_code_placekey"></xsl:call-template>
		</xsl:element>
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_placekt"></xsl:call-template>
			<xsl:call-template name="add_placekey"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_simple_place--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_simple_place">
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_placekt"></xsl:call-template>
			<xsl:call-template name="add_placekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_name_place--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_name_place">
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_name_placekt"></xsl:call-template>
			<xsl:call-template name="add_name_placekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_code_place--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_code_place">
		<xsl:element name="place"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_code_placekt"></xsl:call-template>
			<xsl:call-template name="add_code_placekey"></xsl:call-template>
		</xsl:element>		
	</xsl:template><!--TEMPLATE: add_name_placekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_name_placekt">
		<xsl:element name="placekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_name_placekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_name_placekey">
		<xsl:element name="placekey">
		<xsl:attribute name="picklist"></xsl:attribute>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_code_placekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_code_placekt">
		<xsl:element name="placekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_code_placekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_code_placekey">
		<xsl:element name="placekey">
		<xsl:attribute name="picklist"></xsl:attribute>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_placekt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_placekt">
		<xsl:element name="placekt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_placekey--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_placekey">
		<xsl:element name="placekey"></xsl:element>
	</xsl:template><!--TEMPLATE: add_accconst--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_accconst">
		<xsl:element name="accconst"></xsl:element>
	</xsl:template><!--TEMPLATE: add_useconst--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_useconst">
		<xsl:element name="useconst"></xsl:element>
	</xsl:template><!--TEMPLATE: add_ptcontac--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_ptcontac">
		<xsl:element name="ptcontac"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_browse--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_browse">
		<xsl:element name="browse"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_browsen"></xsl:call-template>
			<xsl:call-template name="add_browsed"></xsl:call-template>
			<xsl:call-template name="add_browset"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_browsen--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_browsen">
		<xsl:element name="browsen"></xsl:element>
	</xsl:template><!--TEMPLATE: add_browsed--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_browsed">
		<xsl:element name="browsed"></xsl:element>
	</xsl:template><!--TEMPLATE: add_browset--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_browset">
		<xsl:element name="browset"></xsl:element>
	</xsl:template><!--TEMPLATE: add_crossref--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_crossref">
		<xsl:element name="crossref"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_citeinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_distinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_distinfo">
		<xsl:element name="distinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_distrib"></xsl:call-template>
			<xsl:call-template name="add_distliab"></xsl:call-template>
			<xsl:call-template name="add_stdorder"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_distrib--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_distrib">
		<xsl:element name="distrib"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_distliab--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_distliab">
		<xsl:element name="distliab"></xsl:element>
	</xsl:template><!--TEMPLATE: add_stdorder--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_stdorder">
		<xsl:element name="stdorder"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_digform"></xsl:call-template>
			<xsl:call-template name="add_fees"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_digform--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_digform">
		<xsl:element name="digform"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_digtinfo"></xsl:call-template>
			<xsl:call-template name="add_digtopt"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_digtinfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_digtinfo">
		<xsl:element name="digtinfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_formname"></xsl:call-template>
			<xsl:call-template name="add_transize"></xsl:call-template>
			<xsl:call-template name="add_dssize"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_formname--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_formname">
		<xsl:element name="formname"></xsl:element>
	</xsl:template><!--TEMPLATE: add_transize--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_transize">
		<xsl:element name="transize"></xsl:element>
	</xsl:template><!--TEMPLATE: add_dssize--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_dssize">
		<xsl:element name="dssize"></xsl:element>
	</xsl:template><!--TEMPLATE: add_digtopt--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_digtopt">
		<xsl:element name="digtopt"><!--Add sub-elements and sections-->
		   <xsl:call-template name="add_onlinopt"></xsl:call-template>
		   <xsl:call-template name="add_offoptn"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_onlinopt--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_onlinopt">
		<xsl:element name="onlinopt"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_computer"></xsl:call-template>
			<xsl:call-template name="add_accinstr"></xsl:call-template>
			<xsl:call-template name="add_oncomp"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_computer--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_computer">
		<xsl:element name="computer"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_networka"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_networka--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_networka">
		<xsl:element name="networka"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_networkr"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_networkr--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_networkr">
		<xsl:element name="networkr"></xsl:element>
	</xsl:template><!--TEMPLATE: add_accinstr--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_accinstr">
		<xsl:element name="accinstr"></xsl:element>
	</xsl:template><!--TEMPLATE: add_oncomp--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_oncomp">
		<xsl:element name="oncomp"></xsl:element>
	</xsl:template><!--TEMPLATE: add_offoptn--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_offoptn">
		<xsl:element name="offoptn"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_offmedia"></xsl:call-template>
			<xsl:call-template name="add_reccap"></xsl:call-template>
			<xsl:call-template name="add_recfmt"></xsl:call-template>
			<xsl:call-template name="add_compat"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_offmedia--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_offmedia">
		<xsl:element name="offmedia"></xsl:element>
	</xsl:template><!--TEMPLATE: add_reccap--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_reccap">
		<xsl:element name="reccap"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_recden"></xsl:call-template>
			<xsl:call-template name="add_recdenu"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_recden--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_recden">
		<xsl:element name="recden"></xsl:element>
	</xsl:template><!--TEMPLATE: add_recdenu--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_recdenu">
		<xsl:element name="recdenu"></xsl:element>
	</xsl:template><!--TEMPLATE: add_recfmt--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_recfmt">
		<xsl:element name="recfmt"></xsl:element>
	</xsl:template><!--TEMPLATE: add_compat--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_compat">
		<xsl:element name="compat"></xsl:element>
	</xsl:template><!--TEMPLATE: add_fees--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_fees">
		<xsl:element name="fees"></xsl:element>
	</xsl:template><!--TEMPLATE: add_metainfo--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_metainfo">
		<xsl:element name="metainfo"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_metd"></xsl:call-template>
			<xsl:call-template name="add_metrd"></xsl:call-template>
			<xsl:call-template name="add_metc"></xsl:call-template>
			<xsl:call-template name="add_metstdn"></xsl:call-template>
			<xsl:call-template name="add_metstdv"></xsl:call-template>
			<xsl:call-template name="add_metextns"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_metd--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_metd">
		<xsl:element name="metd"></xsl:element>
	</xsl:template><!--TEMPLATE: add_metrd--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_metrd">
		<xsl:element name="metrd"></xsl:element>
	</xsl:template><!--TEMPLATE: add_metc--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_metc">
		<xsl:element name="metc"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_cntinfo"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_metstdn--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_metstdn">
		<xsl:element name="metstdn"></xsl:element>
	</xsl:template><!--TEMPLATE: add_metstdv--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_metstdv">
		<xsl:element name="metstdv"></xsl:element>
	</xsl:template><!--TEMPLATE: add_metextns--><!--Desc: adds a blank copy of this section and all it's child elements and sections-->
	<xsl:template name="add_metextns">
		<xsl:element name="metextns"><!--Add sub-elements and sections-->
			<xsl:call-template name="add_onlink"></xsl:call-template>
			<xsl:call-template name="add_metprof"></xsl:call-template>
		</xsl:element>
	</xsl:template><!--TEMPLATE: add_metprof--><!--Desc: adds a blank copy of this element-->
	<xsl:template name="add_metprof">
		<xsl:element name="metprof"></xsl:element>
	</xsl:template>
	
	
	
	<xsl:template name="add_dataset_description">
		<xsl:element name="dataset_description">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_identification"></xsl:call-template>
			<xsl:call-template name="add_metadata_reference"></xsl:call-template>
			<xsl:call-template name="add_overview"></xsl:call-template>
			<xsl:call-template name="add_spatial_reference_system"></xsl:call-template>
			<xsl:call-template name="add_administrative_metadata"></xsl:call-template>
			<xsl:call-template name="add_geokey_database"></xsl:call-template>
			<xsl:call-template name="add_quality_element"></xsl:call-template>
			<xsl:call-template name="add_planar_extent"></xsl:call-template>
			<xsl:call-template name="add_vertical_extent"></xsl:call-template>
			<xsl:call-template name="add_temporal_extent"></xsl:call-template>
			<xsl:call-template name="add_geokey_dataset"></xsl:call-template>
			<xsl:call-template name="add_data_definition"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_identification">
		<xsl:element name="identification">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_alternative_title"></xsl:call-template>
			<xsl:call-template name="add_dataset_title"></xsl:call-template>
			<xsl:call-template name="add_abbreviated_title"></xsl:call-template>
			<xsl:call-template name="add_version"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_spatial_reference_system">
		<xsl:element name="spatial_reference_system">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_direct_positioning_system"></xsl:call-template>
			<xsl:call-template name="add_indirect_positioning_system"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_direct_positioning_system">
		<xsl:element name="direct_positioning_system">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_identifier"></xsl:call-template>
			<xsl:call-template name="add_geodetic_datum_name"></xsl:call-template>
			<xsl:call-template name="add_map_projection_name"></xsl:call-template>
			<xsl:call-template name="add_geodetic_ellipsoid_name"></xsl:call-template>
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_vertical_datum_name"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_indirect_positioning_system">
		<xsl:element name="indirect_positioning_system">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_location_class_name"></xsl:call-template>
			<xsl:call-template name="add_reference_date"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_metadata_reference">
		<xsl:element name="metadata_reference">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_entry_date"></xsl:call-template>
			<xsl:call-template name="add_last_update_date"></xsl:call-template>
			<xsl:call-template name="add_future_review_date"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_administrative_metadata">
		<xsl:element name="administrative_metadata">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_distribution"></xsl:call-template>
			<xsl:call-template name="add_point_of_contacts_role"></xsl:call-template>
			<xsl:call-template name="add_organisation_role"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_distribution">
		<xsl:element name="distribution">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_on_line_access"></xsl:call-template>
			<xsl:call-template name="add_restrictions_on_use"></xsl:call-template>
			<xsl:call-template name="add_distribution_ESRI_NL"></xsl:call-template>
			<xsl:call-template name="add_copyright_owners"></xsl:call-template>
			<xsl:call-template name="add_unit_of_distribution"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_on_line_access">
		<xsl:element name="on_line_access">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_url"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_distribution_ESRI_NL">
		<xsl:element name="ESRI_NL">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_restrictions_on_use_link"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_copyright_owners">
		<xsl:element name="copyright_owners">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_organisation"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_point_of_contacts_role">
		<xsl:element name="point_of_contacts_role">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_description"></xsl:call-template>
			<xsl:call-template name="add_point_of_contact"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_point_of_contact">
		<xsl:element name="point_of_contact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_point_of_contact_name"></xsl:call-template>
			<xsl:call-template name="add_organisation_name"></xsl:call-template>
			<xsl:call-template name="add_phone"></xsl:call-template>
			<xsl:call-template name="add_fax"></xsl:call-template>
			<xsl:call-template name="add_email"></xsl:call-template>
			<xsl:call-template name="add_www_home_page"></xsl:call-template>
			<xsl:call-template name="add_point_of_contact_address"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_point_of_contact_address">
		<xsl:element name="point_of_contact_address">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_street"></xsl:call-template>
			<xsl:call-template name="add_zip"></xsl:call-template>
			<xsl:call-template name="add_city"></xsl:call-template>
			<xsl:call-template name="add_country"></xsl:call-template>
			<xsl:call-template name="add_visiting_address"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_organisation_role">
		<xsl:element name="organisation_role">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_description"></xsl:call-template>
			<xsl:call-template name="add_organisation"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_organisation">
		<xsl:element name="organisation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_abbreviated_organisation_name"></xsl:call-template>
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_phone"></xsl:call-template>
			<xsl:call-template name="add_fax"></xsl:call-template>
			<xsl:call-template name="add_email"></xsl:call-template>
			<xsl:call-template name="add_www_home_page"></xsl:call-template>
			<xsl:call-template name="add_organisation_address"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_organisation_address">
		<xsl:element name="organisation_address">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_street"></xsl:call-template>
			<xsl:call-template name="add_zip"></xsl:call-template>
			<xsl:call-template name="add_city"></xsl:call-template>
			<xsl:call-template name="add_country"></xsl:call-template>
			<xsl:call-template name="add_visiting_address"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_visiting_address">
		<xsl:element name="visiting_address">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_street"></xsl:call-template>
			<xsl:call-template name="add_zip"></xsl:call-template>
			<xsl:call-template name="add_city"></xsl:call-template>
			<xsl:call-template name="add_country"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_geokey_database">
		<xsl:element name="geokey_database">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_description"></xsl:call-template>
			<xsl:call-template name="add_id"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_overview">
		<xsl:element name="overview">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_summary"></xsl:call-template>
			<xsl:call-template name="add_producer_organisation_name"></xsl:call-template>
			<xsl:call-template name="add_purpose_of_production"></xsl:call-template>
			<xsl:call-template name="add_intented_application_scale"></xsl:call-template>
			<xsl:call-template name="add_spatial_schema_type"></xsl:call-template>
			<xsl:call-template name="add_dataset_language"></xsl:call-template>
			<xsl:call-template name="add_potential_usage"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_spatial_schema_type">
		<xsl:element name="spatial_schema_type">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_spatial_schema_type_id"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_dataset_language">
		<xsl:element name="dataset_language">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_language"></xsl:call-template>
			<xsl:call-template name="add_characterset"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_quality_element">
		<xsl:element name="quality_element">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_lineage"></xsl:call-template>
			<xsl:call-template name="add_quality_text"></xsl:call-template>
			<xsl:call-template name="add_quality_parameters"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_lineage">
		<xsl:element name="lineage">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_production"></xsl:call-template>
			<xsl:call-template name="add_source_and_processes"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_production">
		<xsl:element name="production">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_producer_organisation"></xsl:call-template>
			<xsl:call-template name="add_method"></xsl:call-template>
			<xsl:call-template name="add_date_of_production"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_date_of_production">
		<xsl:element name="date_of_production">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_date"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_source_and_processes">
		<xsl:element name="source_and_processes">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_source_history"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_source_history">
		<xsl:element name="source_history">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_source_history_organisation"></xsl:call-template>
			<xsl:call-template name="add_method"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_quality_parameters">
		<xsl:element name="quality_parameters">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_quality_indicator_set"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_quality_indicator_set">
		<xsl:element name="quality_indicator_set">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_quality_measure_set"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_quality_measure_set">
		<xsl:element name="quality_measure_set">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_name"></xsl:call-template>
			<xsl:call-template name="add_description"></xsl:call-template>
			<xsl:call-template name="add_measure_result"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_measure_result">
		<xsl:element name="measure_result">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_value"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_planar_extent">
		<xsl:element name="planar_extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_extent_status"></xsl:call-template>
			<xsl:call-template name="add_extent_date"></xsl:call-template>
			<xsl:call-template name="add_geographic_area"></xsl:call-template>
			<xsl:call-template name="add_bounding_quadrangle"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_geographic_area">
		<xsl:element name="geographic_area">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_area_name"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_bounding_quadrangle">
		<xsl:element name="bounding_quadrangle">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_min_horizontal"></xsl:call-template>
			<xsl:call-template name="add_max_horizontal"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_min_horizontal">
		<xsl:element name="min_horizontal">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_position"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_max_horizontal">
		<xsl:element name="max_horizontal">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_position"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_position">
		<xsl:element name="position">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_x"></xsl:call-template>
			<xsl:call-template name="add_y"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_vertical_extent">
		<xsl:element name="vertical_extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_extent_status"></xsl:call-template>
			<xsl:call-template name="add_extent_date"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_temporal_extent">
		<xsl:element name="temporal_extent">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_extent_status"></xsl:call-template>
			<xsl:call-template name="add_extent_date"></xsl:call-template>
			<xsl:call-template name="add_temporal_extent_description"></xsl:call-template>
			<xsl:call-template name="add_period_range_details"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_period_range_details">
		<xsl:element name="period_range_details">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_period_start_date"></xsl:call-template>
			<xsl:call-template name="add_period_end_date"></xsl:call-template>
			<xsl:call-template name="add_period_range_date_qualifier"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_data_definition">
		<xsl:element name="data_definition">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_application_schema_description"></xsl:call-template>
			<xsl:call-template name="add_object_type"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_application_schema_description">
		<xsl:element name="application_schema_description">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_is_identified_by"></xsl:call-template>
			<xsl:call-template name="add_is_described_by"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_object_type">
		<xsl:element name="object_type">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_object_type_name"></xsl:call-template>
			<xsl:call-template name="add_object_type_definition"></xsl:call-template>
			<xsl:call-template name="add_attribute_type"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_attribute_type">
		<xsl:element name="attribute_type">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_attribute_type_name"></xsl:call-template>
			<xsl:call-template name="add_attribute_type_definition"></xsl:call-template>
			<xsl:call-template name="add_attribute_type_ESRI_NL"></xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="add_attribute_type_ESRI_NL">
		<xsl:element name="ESRI_NL">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_attribute_type_alias"></xsl:call-template>
			<xsl:call-template name="add_attribute_type_type"></xsl:call-template>
			<xsl:call-template name="add_attribute_type_width"></xsl:call-template>
			<xsl:call-template name="add_attribute_type_precision"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_geokey_dataset">
		<xsl:element name="geokey_dataset">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_aard_observatie"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_aard_observatie">
		<xsl:element name="aard_observatie">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_image_size"></xsl:call-template>
			<xsl:call-template name="add_extra_information"></xsl:call-template>
			<xsl:call-template name="add_tide"></xsl:call-template>
			<xsl:call-template name="add_sensor_band"></xsl:call-template>
			<xsl:call-template name="add_recording_time"></xsl:call-template>
			<xsl:call-template name="add_pixel_size"></xsl:call-template>
			<xsl:call-template name="add_pixel_value"></xsl:call-template>
			<xsl:call-template name="add_preprocessing"></xsl:call-template>
			<xsl:call-template name="add_radiometric_resolution"></xsl:call-template>
			<xsl:call-template name="add_scene"></xsl:call-template>
			<xsl:call-template name="add_sensor_speed"></xsl:call-template>
			<xsl:call-template name="add_track"></xsl:call-template>
			<xsl:call-template name="add_source_name"></xsl:call-template>
			<xsl:call-template name="add_sensor_name"></xsl:call-template>
			<xsl:call-template name="add_flying_height"></xsl:call-template>
			<xsl:call-template name="add_weather"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	

	<xsl:template name="add_ESRI_NL">
		<xsl:element name="ESRI_NL">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_overzicht"></xsl:call-template>
			<xsl:call-template name="add_documentatie"></xsl:call-template>
			<xsl:call-template name="add_identificatie"></xsl:call-template>
			<xsl:call-template name="add_velden"></xsl:call-template>
			<xsl:call-template name="add_kwaliteit"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_overzicht">
		<xsl:element name="overzicht">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_set2D3D"></xsl:call-template>
			<xsl:call-template name="add_gegevens_hoofdgroep"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_identificatie">
		<xsl:element name="identificatie">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_invuller"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_velden">
		<xsl:element name="velden">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_CEN_ingevuld"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_documentatie">
		<xsl:element name="documentatie">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_document"></xsl:call-template>
			<xsl:call-template name="add_document_link"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add_kwaliteit">
		<xsl:element name="kwaliteit">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add_kwaliteitscontrole"></xsl:call-template>
		</xsl:element>
	</xsl:template>
	
	<!-- sommige elementen hieronder worden met hun defaults aangemaakt -->
	<xsl:template name="add_alternative_title">
		<xsl:element name="alternative_title"></xsl:element>
	</xsl:template>
	<xsl:template name="add_dataset_title">
		<xsl:element name="dataset_title"></xsl:element>
	</xsl:template>
	<xsl:template name="add_abbreviated_title">
		<xsl:element name="abbreviated_title"></xsl:element>
	</xsl:template>
	<xsl:template name="add_version">
		<xsl:element name="version"></xsl:element>
	</xsl:template>
	<xsl:template name="add_identifier">
		<xsl:element name="identifier"></xsl:element>
	</xsl:template>
	<xsl:template name="add_geodetic_datum_name">
		<xsl:element name="geodetic_datum_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_map_projection_name">
		<xsl:element name="map_projection_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_geodetic_ellipsoid_name">
		<xsl:element name="geodetic_ellipsoid_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_name">
		<xsl:element name="name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_vertical_datum_name">
		<xsl:element name="vertical_datum_name">NAP</xsl:element>
	</xsl:template>
	<xsl:template name="add_location_class_name">
		<xsl:element name="location_class_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_reference_date">
		<xsl:element name="reference_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_entry_date">
		<xsl:element name="entry_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_last_update_date">
		<xsl:element name="last_update_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_future_review_date">
		<xsl:element name="future_review_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_url">
		<xsl:element name="url"></xsl:element>
	</xsl:template>
	<xsl:template name="add_restrictions_on_use">
		<xsl:element name="restrictions_on_use">Alleen MNP: met bronvermelding</xsl:element>
	</xsl:template>
	<xsl:template name="add_restrictions_on_use_link">
		<xsl:element name="restrictions_on_use_link"></xsl:element>
	</xsl:template>
	<xsl:template name="add_unit_of_distribution">
		<xsl:element name="unit_of_distribution"></xsl:element>
	</xsl:template>
	<xsl:template name="add_description">
		<xsl:element name="description"></xsl:element>
	</xsl:template>
	<xsl:template name="add_point_of_contact_name">
		<xsl:element name="point_of_contact_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_organisation_name">
		<xsl:element name="organisation_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_phone">
		<xsl:element name="phone"></xsl:element>
	</xsl:template>
	<xsl:template name="add_email">
		<xsl:element name="email"></xsl:element>
	</xsl:template>
	<xsl:template name="add_abbreviated_organisation_name">
		<xsl:element name="abbreviated_organisation_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_fax">
		<xsl:element name="fax"></xsl:element>
	</xsl:template>
	<xsl:template name="add_www_home_page">
		<xsl:element name="www_home_page"></xsl:element>
	</xsl:template>
	<xsl:template name="add_street">
		<xsl:element name="street"></xsl:element>
	</xsl:template>
	<xsl:template name="add_zip">
		<xsl:element name="zip"></xsl:element>
	</xsl:template>
	<xsl:template name="add_id">
		<xsl:element name="id"></xsl:element>
	</xsl:template>
	<xsl:template name="add_summary">
		<xsl:element name="summary"></xsl:element>
	</xsl:template>
	<xsl:template name="add_producer_organisation_name">
		<xsl:element name="producer_organisation_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_purpose_of_production">
		<xsl:element name="purpose_of_production"></xsl:element>
	</xsl:template>
	<xsl:template name="add_intented_application_scale">
		<xsl:element name="intented_application_scale"></xsl:element>
	</xsl:template>
	<xsl:template name="add_spatial_schema_type_id">
		<xsl:element name="spatial_schema_type_id"></xsl:element>
	</xsl:template>
	<xsl:template name="add_language">
		<xsl:element name="language">Nederlands</xsl:element>
	</xsl:template>
	<xsl:template name="add_characterset">
		<xsl:element name="characterset">ISO 8859-1</xsl:element>
	</xsl:template>
	<xsl:template name="add_potential_usage">
		<xsl:element name="potential_usage"></xsl:element>
	</xsl:template>
	<xsl:template name="add_producer_organisation">
		<xsl:element name="producer_organisation"></xsl:element>
	</xsl:template>
	<xsl:template name="add_method">
		<xsl:element name="method"></xsl:element>
	</xsl:template>
	<xsl:template name="add_date">
		<xsl:element name="date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_source_history_organisation">
		<xsl:element name="organisation"></xsl:element>
	</xsl:template>
	<xsl:template name="add_quality_text">
		<xsl:element name="quality_text"></xsl:element>
	</xsl:template>
	<xsl:template name="add_value">
		<xsl:element name="value"></xsl:element>
	</xsl:template>
	<xsl:template name="add_extent_status">
		<xsl:element name="extent_status">Actueel</xsl:element>
	</xsl:template>
	<xsl:template name="add_extent_date">
		<xsl:element name="extent_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_area_name">
		<xsl:element name="area_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_temporal_extent_description">
		<xsl:element name="temporal_extent_description"></xsl:element>
	</xsl:template>
	<xsl:template name="add_period_start_date">
		<xsl:element name="period_start_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_period_end_date">
		<xsl:element name="period_end_date"></xsl:element>
	</xsl:template>
	<xsl:template name="add_period_range_date_qualifier">
		<xsl:element name="period_range_date_qualifier"></xsl:element>
	</xsl:template>
	<xsl:template name="add_is_identified_by">
		<xsl:element name="is_identified_by"></xsl:element>
	</xsl:template>
	<xsl:template name="add_is_described_by">
		<xsl:element name="is_described_by"></xsl:element>
	</xsl:template>
	<xsl:template name="add_object_type_name">
		<xsl:element name="object_type_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_object_type_definition">
		<xsl:element name="object_type_definition"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_name">
		<xsl:element name="attribute_type_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_definition">
		<xsl:element name="attribute_type_definition"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_alias">
		<xsl:element name="attribute_type_alias"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_type">
		<xsl:element name="attribute_type_type"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_width">
		<xsl:element name="attribute_type_width"></xsl:element>
	</xsl:template>
	<xsl:template name="add_attribute_type_precision">
		<xsl:element name="attribute_type_precision"></xsl:element>
	</xsl:template>
	<xsl:template name="add_set2D3D">
		<xsl:element name="set2D3D"></xsl:element>
	</xsl:template>
	<xsl:template name="add_gegevens_hoofdgroep">
		<xsl:element name="gegevens_hoofdgroep"></xsl:element>
	</xsl:template>
	<xsl:template name="add_invuller">
		<xsl:element name="invuller"></xsl:element>
	</xsl:template>
	<xsl:template name="add_CEN_ingevuld">
		<xsl:element name="CEN_ingevuld"></xsl:element>
	</xsl:template>
	<xsl:template name="add_kwaliteitscontrole">
		<xsl:element name="kwaliteitscontrole"></xsl:element>
	</xsl:template>
	<xsl:template name="add_document">
		<xsl:element name="document"></xsl:element>
	</xsl:template>
	<xsl:template name="add_document_link">
		<xsl:element name="document_link"></xsl:element>
	</xsl:template>
	<xsl:template name="add_natvform">
		<xsl:element name="natvform"></xsl:element>
	</xsl:template>
	<xsl:template name="add_x">
		<xsl:element name="x"></xsl:element>
	</xsl:template>
	<xsl:template name="add_y">
		<xsl:element name="y"></xsl:element>
	</xsl:template>
	<xsl:template name="add_minimum_height_value">
		<xsl:element name="minimum_height_value"></xsl:element>
	</xsl:template>
	<xsl:template name="add_maximum_height_value">
		<xsl:element name="maximum_height_value"></xsl:element>
	</xsl:template>
	<xsl:template name="add_image_size">
		<xsl:element name="image_size"></xsl:element>
	</xsl:template>
	<xsl:template name="add_extra_information">
		<xsl:element name="extra_information"></xsl:element>
	</xsl:template>
	<xsl:template name="add_tide">
		<xsl:element name="tide"></xsl:element>
	</xsl:template>
	<xsl:template name="add_sensor_band">
		<xsl:element name="sensor_band"></xsl:element>
	</xsl:template>
	<xsl:template name="add_recording_time">
		<xsl:element name="recording_time"></xsl:element>
	</xsl:template>
	<xsl:template name="add_pixel_size">
		<xsl:element name="pixel_size"></xsl:element>
	</xsl:template>
	<xsl:template name="add_pixel_value">
		<xsl:element name="pixel_value"></xsl:element>
	</xsl:template>
	<xsl:template name="add_preprocessing">
		<xsl:element name="preprocessing"></xsl:element>
	</xsl:template>
	<xsl:template name="add_radiometric_resolution">
		<xsl:element name="radiometric_resolution"></xsl:element>
	</xsl:template>
	<xsl:template name="add_scene">
		<xsl:element name="scene"></xsl:element>
	</xsl:template>
	<xsl:template name="add_sensor_speed">
		<xsl:element name="sensor_speed"></xsl:element>
	</xsl:template>
	<xsl:template name="add_track">
		<xsl:element name="track"></xsl:element>
	</xsl:template>
	<xsl:template name="add_source_name">
		<xsl:element name="source_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_sensor_name">
		<xsl:element name="sensor_name"></xsl:element>
	</xsl:template>
	<xsl:template name="add_flying_height">
		<xsl:element name="flying_height"></xsl:element>
	</xsl:template>
	<xsl:template name="add_weather">
		<xsl:element name="weather"></xsl:element>
	</xsl:template>
	
	
</xsl:stylesheet>
