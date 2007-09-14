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
	Preprocessor die alle nodes kopieert en ontbrekende nodes toevoegd om de mogelijkheid te bieden deze te editen in de stylesheet.
	De templates voor het toevoegen van nodes bevindt zich in "metadataEditPreprocessorTemplates.xsl".
	-->

	<!--get template that 'knows' how to add elements-->
	<xsl:include href="metadataEditPreprocessorTemplates.xsl"/>
	
	
	
	<!-- default template: copy all nodes and attributes-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="gmd:MD_Metadata">
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:language"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:characterSet)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-characterSet"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:characterSet"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:contact"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:dateStamp)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dateStamp"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:dateStamp"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:metadataStandardName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:metadataStandardName"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:metadataStandardVersion)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardVersion"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:metadataStandardVersion"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:referenceSystemInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:referenceSystemInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(gmd:identificationInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-identificationInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:identificationInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:language) 
														and not(self::gmd:contact) 
														and not(self::gmd:characterSet) 
														and not(self::gmd:dateStamp) 
														and not(self::gmd:metadataStandardName)
														and not(self::gmd:metadataStandardVersion)
														and not(self::gmd:referenceSystemInfo)
														and not(self::gmd:identificationInfo)
														]"/>
			
		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:language">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:characterSet">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_CharacterSetCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_CharacterSetCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_CharacterSetCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:MD_CharacterSetCode) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:contact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_ResponsibleParty) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:dateStamp">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:Date) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:metadataStandardName">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:metadataStandardVersion">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:referenceSystemInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ReferenceSystem)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ReferenceSystem"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_ReferenceSystem"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:MD_ReferenceSystem) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:MD_ReferenceSystem">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:referenceSystemIdentifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:referenceSystemIdentifier"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:referenceSystemIdentifier) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:referenceSystemIdentifier">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:RS_Identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-RS_Identifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:RS_Identifier"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:RS_Identifier) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:RS_Identifier">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:code)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-code"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:code"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:code) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:code">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:identificationInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_DataIdentification)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_DataIdentification"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_DataIdentification"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:MD_DataIdentification) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:MD_DataIdentification">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:citation"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:abstract)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-abstract"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:abstract"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:status)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-status"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:status"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:pointOfContact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-pointOfContact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:pointOfContact"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:language"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:spatialRepresentationType)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-spatialRepresentationType"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:spatialRepresentationType"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:topicCategory)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-topicCategory"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:topicCategory"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:citation) 
														and not(self::gmd:abstract) 
														and not(self::gmd:status) 
														and not(self::gmd:pointOfContact) 
														and not(self::gmd:language) 
														and not(self::gmd:spatialRepresentationType) 
														and not(self::gmd:topicCategory) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:citation">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Citation"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_Citation) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:CI_Citation">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:title)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-title"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:title"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:title) 
														and not(self::gmd:date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:title">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:CI_Citation/gmd:date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_Date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:CI_Date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-inner-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:CI_Date/gmd:date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:Date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:abstract">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:status">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_ProgressCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ProgressCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:MD_ProgressCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:MD_ProgressCode) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:pointOfContact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_ResponsibleParty) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:CI_ResponsibleParty">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:organisationName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-organisationName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:organisationName"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:role)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-role"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:role"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(gmd:contactInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contactInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:contactInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:organisationName) 
														and not(self::gmd:role) 
														and not(self::gmd:contactInfo) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="gmd:organisationName">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gco:CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gco:CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gco:CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:role">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_RoleCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_RoleCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_RoleCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_RoleCode) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:contactInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_Contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_Contact"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_Contact) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:CI_Contact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:onlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-onlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:onlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:onlineResource) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:onlineResource">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:CI_OnlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_OnlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:CI_OnlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:CI_OnlineResource) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:CI_OnlineResource">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:linkage)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-linkage"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:linkage"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:linkage) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:linkage">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:URL)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-URL"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:URL"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:URL) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:spatialRepresentationType">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_SpatialRepresentationTypeCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_SpatialRepresentationTypeCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_SpatialRepresentationTypeCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:MD_SpatialRepresentationTypeCode) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="gmd:topicCategory">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(gmd:MD_TopicCategoryCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_TopicCategoryCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="gmd:MD_TopicCategoryCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::gmd:MD_TopicCategoryCode) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
</xsl:stylesheet>
