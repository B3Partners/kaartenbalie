<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- Created: 2004 Eric Compas
								   
	 Description:
	 	Various functions for getting full XML paths, etc.
	-->
	
	
	<!-- Description: returns the portion of a 'string' after the last occurence of 'char' -->
	<!-- from Jeni Tennison at URL http://sources.redhat.com/ml/xsl-list/2001-02/msg00831.html -->
	<!-- declare that it takes two parameters - the string and the char -->
	<xsl:template name="lastIndexOf">
	   <xsl:param name="string" />
	   <xsl:param name="char" />
	   <xsl:choose>
		  <!-- if the string contains the character... -->
		  <xsl:when test="contains($string, $char)">
			 <!-- call the template recursively... -->
			 <xsl:call-template name="lastIndexOf">
				<!-- with the string being the string after the character
					 -->
				<xsl:with-param name="string"
								select="substring-after($string, $char)" />
				<!-- and the character being the same as before -->
				<xsl:with-param name="char" select="$char" />
			 </xsl:call-template>
		  </xsl:when>
		  <!-- otherwise, return the value of the string -->
		  <xsl:otherwise><xsl:value-of select="$string" /></xsl:otherwise>
	   </xsl:choose>
	</xsl:template>
	
	<!-- Description: returns the full XPath for passed node-set -->
	<!-- From: http://www.dpawson.co.uk/xsl/sect2/N6077.html -->
	<!-- modified by Eric Compas, 1/25/2005 to make a callable template -->
	<xsl:template name="full_path_old">
	  <xsl:for-each select="ancestor-or-self::*">
		<xsl:value-of select="name()"/>
		<xsl:if test="position()!=last()">
			<xsl:text>/</xsl:text>
		</xsl:if>
	  </xsl:for-each>
	</xsl:template>
	
	<xsl:template name="full-path">
		<xsl:for-each select="ancestor-or-self::*">
			<xsl:text>/</xsl:text>
			<xsl:value-of select="name()"/>
			<xsl:variable name="precedingSiblingsWithSameNodeName" select="count(preceding-sibling::*[name(current()) = name(.)])"/>
			<xsl:if test="$precedingSiblingsWithSameNodeName > -1"><!-- in de praktijk zal "$precedingSiblingsWithSameNodeName > 0" ook wel werken (is misschien iets netter) -->
				<xsl:text>[</xsl:text>
					<xsl:value-of select="1 + $precedingSiblingsWithSameNodeName"/>
				<xsl:text>]</xsl:text>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	

</xsl:stylesheet>