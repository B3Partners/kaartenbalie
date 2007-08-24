<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<!--
	Auteur: Erik van de Pol. B3Partners.
	
	Beschrijving stylesheet:
	Preprocessor die alle nodes kopieert en ontbrekende nodes toevoegd om de mogelijkheid te bieden deze te editen in de stylesheet.
	De templates voor het toevoegen van nodes bevindt zich in "Add_MNP_Metadata_Beheerder_Edit_Templates.xsl".
	-->

	<!--get template that 'knows' how to add elements-->
	<xsl:include href="metadataEditPreprocessorTemplates.xsl"/>
	
	
	<!-- default template: copy all nodes and attributes-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>


	<!--root template-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>


	<xsl:template match="MD_Metadata">
	
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="language"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(characterSet)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-characterSet"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="characterSet"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="contact"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(dateStamp)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dateStamp"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="dateStamp"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(metadataStandardName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="metadataStandardName"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(metadataStandardVersion)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-metadataStandardVersion"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="metadataStandardVersion"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(referenceSystemInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="referenceSystemInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(identificationInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-identificationInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="identificationInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(distributionInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-distributionInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="distributionInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="not(dataQualityInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-dataQualityInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="dataQualityInfo"/>
				</xsl:otherwise>
			</xsl:choose>

			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::language) 
														and not(self::contact) 
														and not(self::characterSet) 
														and not(self::dateStamp) 
														and not(self::metadataStandardName)
														and not(self::metadataStandardVersion)
														and not(self::referenceSystemInfo)
														and not(self::identificationInfo)
														and not(self::distributionInfo)																																										
														and not(self::dataQualityInfo)																																										
														]"/>
			
		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="language">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="characterSet">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="contact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_ResponsibleParty) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="dateStamp">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::Date) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="metadataStandardName">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="metadataStandardVersion">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="referenceSystemInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(MD_ReferenceSystem)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_ReferenceSystem"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="MD_ReferenceSystem"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::MD_ReferenceSystem) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="MD_ReferenceSystem">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(referenceSystemIdentifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-referenceSystemIdentifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="referenceSystemIdentifier"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::referenceSystemIdentifier) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="referenceSystemIdentifier">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(RS_Identifier)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-RS_Identifier"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="RS_Identifier"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::RS_Identifier) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="RS_Identifier">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(code)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-code"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="code"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::code) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="code">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="identificationInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(MD_DataIdentification)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_DataIdentification"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="MD_DataIdentification"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::MD_DataIdentification) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="MD_DataIdentification">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="citation"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(abstract)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-abstract"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="abstract"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(status)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-status"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="status"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(pointOfContact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-pointOfContact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="pointOfContact"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(language)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-language"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="language"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(topicCategory)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-topicCategory"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="topicCategory"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::citation) 
														and not(self::abstract) 
														and not(self::status) 
														and not(self::pointOfContact) 
														and not(self::language) 
														and not(self::topicCategory) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="citation">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_Citation)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Citation"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_Citation"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_Citation) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="CI_Citation">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(title)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-title"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="title"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::title) 
														and not(self::date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="title">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="CI_Citation/date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_Date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="CI_Date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-inner-date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="CI_Date/date">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(Date)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-Date"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="Date"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::Date) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="abstract">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="status">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="pointOfContact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_ResponsibleParty)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_ResponsibleParty"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_ResponsibleParty"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_ResponsibleParty) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="CI_ResponsibleParty">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(organisationName)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-organisationName"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="organisationName"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(role)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-role"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="role"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:choose>
				<xsl:when test="not(contactInfo)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-contactInfo"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="contactInfo"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::organisationName) 
														and not(self::role) 
														and not(self::contactInfo) 
														]"/>

		</xsl:copy>
	
	</xsl:template>


	<xsl:template match="organisationName">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CharacterString)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CharacterString"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CharacterString"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CharacterString) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="role">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_RoleCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_RoleCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_RoleCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_RoleCode) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="contactInfo">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_Contact)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_Contact"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_Contact"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_Contact) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="CI_Contact">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(onlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-onlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="onlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::onlineResource) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="onlineResource">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(CI_OnlineResource)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-CI_OnlineResource"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="CI_OnlineResource"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::CI_OnlineResource) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="CI_OnlineResource">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(linkage)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-linkage"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="linkage"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::linkage) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="linkage">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(URL)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-URL"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="URL"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::URL) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	<xsl:template match="topicCategory">
		
		<xsl:copy>
		
			<xsl:choose>
				<xsl:when test="not(MD_TopicCategoryCode)">
					<!--Child element missing, create it-->
					<xsl:call-template name="add-MD_TopicCategoryCode"/>
				</xsl:when>
				<xsl:otherwise>
					<!--Child element exists, copy it-->
					<xsl:apply-templates select="MD_TopicCategoryCode"/>
				</xsl:otherwise>
			</xsl:choose>
			
			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[
														not(self::MD_TopicCategoryCode) 
														]"/>

		</xsl:copy>
		
	</xsl:template>
	
	
	
	
	


</xsl:stylesheet>
