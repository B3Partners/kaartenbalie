<?xml version="1.0" encoding="UTF-16"?>
<xsl:stylesheet					
					version="1.0"
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xlink="http://www.w3.org/1999/xlink"					
					xmlns:gco="http://www.isotc211.org/2005/gco"
					exclude-result-prefixes="gmd"
					>

	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	
	Beschrijving stylesheet:
	Bevat templates die ontbrekende nodes toevoegen om de mogelijkheid te bieden deze te editen in de stylesheet.
	-->

	<xsl:template name="add-MD_Metadata">
		<xsl:element name="gmd:MD_Metadata">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-language"/>
			<xsl:call-template name="add-contact"/>
			<xsl:call-template name="add-characterSet"/>
			<xsl:call-template name="add-dateStamp"/>
			<xsl:call-template name="add-metadataStandardName"/>
			<xsl:call-template name="add-metadataStandardVersion"/>
			<xsl:call-template name="add-referenceSystemInfo"/>
			<xsl:call-template name="add-identificationInfo"/>
			<!--<xsl:call-template name="add-distributionInfo"/>
			<xsl:call-template name="add-dataQualityInfo"/>-->
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-language">
		<xsl:element name="gmd:language">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-characterSet">
		<xsl:element name="gmd:characterSet">
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
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-metadataStandardVersion">
		<xsl:element name="gmd:metadataStandardVersion">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
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
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-code">
		<xsl:element name="gmd:code">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
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
			<xsl:call-template name="add-topicCategory"/>
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
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-inner-date">
		<xsl:element name="gmd:date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
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
			<xsl:call-template name="add-CharacterString"/>
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
	
	<xsl:template name="add-topicCategory">
		<xsl:element name="gmd:topicCategory">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_TopicCategoryCode"/>
		</xsl:element>
	</xsl:template>
	
	
	
	
	<xsl:template name="add-CharacterString">
		<xsl:element name="gco:CharacterString"></xsl:element>
	</xsl:template>
	
	<xsl:template name="add-Date">
		<xsl:element name="gco:Date"></xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_RoleCode">
		<xsl:element name="gmd:CI_RoleCode">
			<!-- TODO: finish! -->
			<xsl:attribute name="gmd:codeList"></xsl:attribute>
			<xsl:attribute name="gmd:codeListValue"></xsl:attribute>			
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-MD_TopicCategoryCode">
		<xsl:element name="gmd:MD_TopicCategoryCode">
			<!-- TODO: finish! -->
			<xsl:attribute name="gmd:codeList"></xsl:attribute>
			<xsl:attribute name="gmd:codeListValue"></xsl:attribute>			
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-URL">
		<xsl:element name="gmd:URL"></xsl:element>
	</xsl:template>
	
	
</xsl:stylesheet>
