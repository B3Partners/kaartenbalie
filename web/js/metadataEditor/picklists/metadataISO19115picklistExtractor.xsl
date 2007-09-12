<?xml version="1.0" encoding="UTF-8"?>
<!-- 
Invoer voor deze file moet de file "ML_gmxCodelists.xml" zijn.
"ML_gmxCodelists.xml" is onderdeel van de ISO 19115 geografische metadata standaard.
-->
<x:stylesheet
				xmlns:gmx="http://www.isotc211.org/2005/gmx"
				xmlns:gml="http://www.opengis.net/gml" 
				xmlns:x="http://www.w3.org/1999/XSL/Transform"
				xmlns:xsl="anything" 
				version="1.0"
				>
				<!-- de waarde van xmlns:xsl mag alles zijn --> 
				
<x:output method="xml" indent="yes"/>

<x:namespace-alias stylesheet-prefix="xsl" result-prefix="x"/>

<!--
everything between the x:template tags
is outputted to the new stylesheet
-->

	<x:template match="/">
		<xsl:stylesheet version="1.0">
			<x:apply-templates select="gmx:CT_CodelistCatalogue/gmx:codelistItem"/>
		</xsl:stylesheet>
	</x:template>
	
	<x:template match="versionNumber">
	
	</x:template>
	
	<x:template match="versionDate">
	
	</x:template>
   
	<x:template match="language">
	
	</x:template>

	<x:template match="characterSet">
	
	</x:template>
  
	<x:template match="codelistItem">
		<x:apply-templates select="gmx:ML_CodeListDictionary | gmx:CodeListDictionary"/>
	</x:template>
	
	<x:template match="gmx:ML_CodeListDictionary | gmx:CodeListDictionary">
		<x:variable name="picklistId" select="concat('picklist_', @gml:id)"/>
		<xsl:template name="{$picklistId}">
			<select id="{$picklistId}" name="{$picklistId}" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
				<option value="default">[huidige waarde]</option>
				<x:apply-templates select="gmx:codeEntry"/>
			</select>
		</xsl:template>
	</x:template>
	
	<x:template match="gmx:codeEntry">
		<x:apply-templates select="gmx:ML_CodeDefinition | gmx:CodeDefinition"/>
	</x:template>
	
	<x:template match="gmx:ML_CodeDefinition | gmx:CodeDefinition">
		<x:variable name="optionId" select="@gml:id"/>
		<x:variable name="optionDescription" select="gml:description"/>
		<option value="{$optionId}" onmouseover="picklistItemHover(this, '{$optionDescription}')">
			<x:value-of select="gml:identifier"/>
		</option>
	</x:template>
	
	
  
</x:stylesheet>