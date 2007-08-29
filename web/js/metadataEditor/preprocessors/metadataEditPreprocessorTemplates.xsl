<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet					
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					xmlns:xlink="http://www.w3.org/1999/xlink"					
					xmlns:gmd="http://www.isotc211.org/2005/gmd"
					xmlns:gco="http://www.isotc211.org/2005/gco"
					>

	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	
	Beschrijving stylesheet:
	Bevat templates die ontbrekende nodes toevoegen om de mogelijkheid te bieden deze te editen in de stylesheet.
	-->

	<xsl:template name="add-MD_Metadata">
		<xsl:element name="MD_Metadata">
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
		<xsl:element name="language">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-characterSet">
		<xsl:element name="characterSet">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-contact">
		<xsl:element name="contact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-dateStamp">
		<xsl:element name="dateStamp">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-metadataStandardName">
		<xsl:element name="metadataStandardName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-metadataStandardVersion">
		<xsl:element name="metadataStandardVersion">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-referenceSystemInfo">
		<xsl:element name="referenceSystemInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_ReferenceSystem"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-MD_ReferenceSystem">
		<xsl:element name="MD_ReferenceSystem">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-referenceSystemIdentifier"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-referenceSystemIdentifier">
		<xsl:element name="referenceSystemIdentifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-RS_Identifier"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-RS_Identifier">
		<xsl:element name="RS_Identifier">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-code"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-code">
		<xsl:element name="code">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-identificationInfo">
		<xsl:element name="identificationInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_DataIdentification"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-MD_DataIdentification">
		<xsl:element name="MD_DataIdentification">
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
		<xsl:element name="citation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Citation"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_Citation">
		<xsl:element name="CI_Citation">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-title"/>
			<xsl:call-template name="add-date"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-title">
		<xsl:element name="title">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-date">
		<xsl:element name="date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Date"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_Date">
		<xsl:element name="CI_Date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-inner-date"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-inner-date">
		<xsl:element name="date">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-Date"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-abstract">
		<xsl:element name="abstract">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-status">
		<xsl:element name="status">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-pointOfContact">
		<xsl:element name="pointOfContact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_ResponsibleParty"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_ResponsibleParty">
		<xsl:element name="CI_ResponsibleParty">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-organisationName"/>
			<xsl:call-template name="add-role"/>
			<xsl:call-template name="add-contactInfo"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-organisationName">
		<xsl:element name="organisationName">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CharacterString"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-role">
		<xsl:element name="role">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_RoleCode"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-contactInfo">
		<xsl:element name="contactInfo">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_Contact"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_Contact">
		<xsl:element name="CI_Contact">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-onlineResource"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-onlineResource">
		<xsl:element name="onlineResource">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-CI_OnlineResource"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-CI_OnlineResource">
		<xsl:element name="CI_OnlineResource">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-linkage"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-linkage">
		<xsl:element name="linkage">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-URL"/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-topicCategory">
		<xsl:element name="topicCategory">
			<!--Add sub-elements and sections-->
			<xsl:call-template name="add-MD_TopicCategoryCode"/>
		</xsl:element>
	</xsl:template>
	
	
	
	
	<xsl:template name="add-CharacterString">
		<xsl:element name="gco:CharacterString"/>
	</xsl:template>
	
	<xsl:template name="add-Date">
		<xsl:element name="gco:Date"/>
	</xsl:template>
	
	<xsl:template name="add-CI_RoleCode">
		<xsl:element name="CI_RoleCode">
			<!-- TODO: finish! -->
			<xsl:attribute name="codeList"></xsl:attribute>
			<xsl:attribute name="codeListValue"></xsl:attribute>			
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-MD_TopicCategoryCode">
		<xsl:element name="MD_TopicCategoryCode">
			<!-- TODO: finish! -->
			<xsl:attribute name="codeList"></xsl:attribute>
			<xsl:attribute name="codeListValue"></xsl:attribute>			
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="add-URL">
		<xsl:element name="URL"/>
	</xsl:template>
	
	
</xsl:stylesheet>
