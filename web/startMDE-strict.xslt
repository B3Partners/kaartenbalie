<?xml version="1.0" encoding="UTF-8"?>
<!--
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Metadata Editor.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output doctype-public="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-system="-//W3C//DTD XHTML 1.0 Strict//EN" method="html" omit-xml-declaration="yes" indent="yes"/>
	<xsl:param name="baseURL" select="'file:///*baseURLPlaceholder*/'"/>
	<!-- xsl:param name="baseURL" select="'file:///C:/dev/nb6-projects/b3p-metadata-editor/'"/ -->
	<xsl:param name="basePath">
		<xsl:value-of select="$baseURL"/>
		<xsl:value-of select="'js/metadataEditor/'"/>
	</xsl:param>
	<xsl:param name="readonly" select="'false'"/>
	<xsl:template match="/">
		<xsl:variable name="metadataXML">
			<xsl:apply-templates mode="copy"/>
		</xsl:variable>
		<xsl:element name="html">
			<xsl:attribute name="xhtml">true</xsl:attribute>
			<xsl:element name="head">
				<xsl:element name="meta">
					<xsl:attribute name="http-equip">pragma</xsl:attribute>
					<xsl:attribute name="content">no-cache</xsl:attribute>
				</xsl:element>
				<xsl:element name="title">B3P Metadata Editor </xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:text>
						var escapedMetadataXML = "</xsl:text>
					<xsl:value-of select="$metadataXML"/>
					<xsl:text>";
					</xsl:text>
					<xsl:text>
						var baseFullPath ="</xsl:text>
					<xsl:value-of select="$basePath"/>
					<xsl:text>";
					</xsl:text>
					<xsl:text>
						var viewMode ="</xsl:text>
					<xsl:value-of select="$readonly"/>
					<xsl:text>";
					</xsl:text>
					<xsl:text>
						//var debugMode = true;
						var debugMode = false;
							
						var metadataXML = "";
						// path to root of ISO metadata element MD_Metadata, required for add/delete menu
						var pathToRoot = "/";
						var basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;MD_Metadata xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gml=\"http://www.opengis.net/gml\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd ./ISO19139_2005-10-08/gmd/gmd.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" /&gt;";
								
						var mainXslFullPath = baseFullPath + "mdEdit_strict.xsl";
						var preprocessorXslFullPath = baseFullPath + "preprocessors/metadataEditPreprocessor.xsl";			
						var PLUS_IMAGE = baseFullPath + "images/xp_plus.gif";
						var MINUS_IMAGE = baseFullPath + "images/xp_minus.gif";
						var MENU_IMAGE = baseFullPath + "images/arrow.gif";
								
						function loadMetadata() 
						{ 
							// if no metadata is present we start the editor with all elements empty
							if (trim(escapedMetadataXML) == "") {
								metadataXML = basicMetadataXML.unescapeHTML();
							} else if (escapedMetadataXML.indexOf("&lt;MD_Metadata") == -1) {
								if (confirm("Onleesbare metadata gevonden (metadata tag ontbreekt), overschrijven?")) {
									metadataXML = basicMetadataXML.unescapeHTML();
								} else {
									alert("U kunt deze metadata niet bewerken of inzien, het bestand is ongewijzigd!");
								}
							} else {
								metadataXML = escapedMetadataXML.unescapeHTML();
							}

							transformXml();

						} 
						
						function saveMetadata() 
						{ 
						
							var ta = document.getElementById('newxml');
							ta.value = xmlDoc.xml;
							// alert("xmlDoc.xml bevat nieuw xml: " + xmlDoc.xml); // xmlDoc.xml;
							
							var root = document.getElementById("edit-doc-root");
							var tc = root.getAttribute('changed');
							var ic = document.getElementById('ischanged');
							ic.value = tc;
							// alert("changed: " + ic.value); 
						} 
						
						
						function pMarkAsUnChanged()
						{
						// changed op false zetten, aannemen dat save gelukt is.
							var root = document.getElementById("edit-doc-root");
							root.setAttribute('changed', false);
						}
						
				
					</xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/StringBuffer.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/crossBrowser.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/metadataEdit.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/metadataEditBrowser.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/generic_dhtml.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/generic_edit.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/nczXMLDOMWrapper.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/XML.Transformer.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="script">
					<xsl:attribute name="type">text/javascript</xsl:attribute>
					<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>includes/scriptaculous-js-1.7.0/lib/prototype.js</xsl:text></xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="link">
					<xsl:attribute name="type">text/css</xsl:attribute>
					<xsl:attribute name="href"><xsl:value-of select="$basePath"/><xsl:text>../../styles/metadataEdit.css</xsl:text></xsl:attribute>
					<xsl:attribute name="rel">stylesheet</xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
				<xsl:element name="link">
					<xsl:attribute name="type">text/css</xsl:attribute>
					<xsl:attribute name="href"><xsl:value-of select="$basePath"/><xsl:text>../../styles/metadataEdit-ie.css</xsl:text></xsl:attribute>
					<xsl:attribute name="rel">stylesheet</xsl:attribute>
					<xsl:text> </xsl:text>
				</xsl:element>
			</xsl:element>
			<xsl:element name="body">
				<xsl:attribute name="onload">loadMetadata();</xsl:attribute>
				<xsl:element name="h1">B3P Metadata Editor</xsl:element>
				<xsl:element name="form">
					<xsl:element name="div">
						<xsl:attribute name="id">saveButton</xsl:attribute>
						<xsl:attribute name="onclick">saveMetadata();</xsl:attribute>
					</xsl:element>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">newxml</xsl:attribute>
						<xsl:attribute name="id">newxml</xsl:attribute>
					</xsl:element>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">ischanged</xsl:attribute>
						<xsl:attribute name="id">ischanged</xsl:attribute>
					</xsl:element>
					<xsl:element name="input">
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="id">MarkAsUnChanged</xsl:attribute>
						<xsl:attribute name="onclick">pMarkAsUnChanged();</xsl:attribute>
					</xsl:element>
					<hr/>
					<xsl:element name="div">
						<xsl:attribute name="id">write-root</xsl:attribute>
						<xsl:attribute name="onclick">click();</xsl:attribute>
						<xsl:text> </xsl:text>
					</xsl:element>
				</xsl:element>
				<hr/>
				<xsl:element name="div">
					<xsl:attribute name="class">hidden</xsl:attribute>
					<xsl:element name="img">
						<xsl:attribute name="id">plus_img</xsl:attribute>
						<xsl:attribute name="class">plus-minus</xsl:attribute>
						<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>images/xp_plus.gif</xsl:text></xsl:attribute>
						<xsl:text> </xsl:text>
					</xsl:element>
					<xsl:element name="img">
						<xsl:attribute name="id">minus_img</xsl:attribute>
						<xsl:attribute name="class">plus-minus</xsl:attribute>
						<xsl:attribute name="src"><xsl:value-of select="$basePath"/><xsl:text>images/xp_minus.gif</xsl:text></xsl:attribute>
						<xsl:text> </xsl:text>
					</xsl:element>
				</xsl:element>
				<xsl:text>Copyright 2008 B3Partners BV, Licensed under the terms of the GPLv3</xsl:text>
				<hr/>
			</xsl:element>
			<!-- another head: prevents IE cache bug/feature -->
			<xsl:element name="head">
				<xsl:element name="meta">
					<xsl:attribute name="http-equip">pragma</xsl:attribute>
					<xsl:attribute name="content">no-cache</xsl:attribute>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*" mode="copy">
		<xsl:choose>
			<xsl:when test="name() = 'MD_Metadata' or name() = 'gmd:MD_Metadata' ">
				<xsl:text>&lt;</xsl:text>
				<xsl:value-of select="name()"/>
				<xsl:text> xmlns=\&quot;http://www.isotc211.org/2005/gmd\&quot; xmlns:gmd=\&quot;http://www.isotc211.org/2005/gmd\&quot; xmlns:gco=\&quot;http://www.isotc211.org/2005/gco\&quot; xmlns:gml=\&quot;http://www.opengis.net/gml\&quot; xmlns:xlink=\&quot;http://www.w3.org/1999/xlink\&quot; xmlns:xsi=\&quot;http://www.w3.org/2001/XMLSchema-instance\&quot;</xsl:text>
				<xsl:text>&gt;</xsl:text>
				<xsl:apply-templates mode="copy"/>
				<xsl:text>&lt;/</xsl:text>
				<xsl:value-of select="name()"/>
				<xsl:text>&gt;</xsl:text>
			</xsl:when>
			<xsl:when test="namespace-uri() = '' or namespace-uri() = 'http://www.isotc211.org/2005/gmd' or namespace-uri() = 'http://www.isotc211.org/2005/gco' or namespace-uri() = 'http://www.opengis.net/gml' or namespace-uri() = 'http://www.w3.org/1999/xlink'">
				<xsl:text>&lt;</xsl:text>
				<xsl:value-of select="name()"/>
				<xsl:apply-templates select="@*" mode="copy"/>
				<xsl:text>&gt;</xsl:text>
				<xsl:apply-templates mode="copy"/>
				<xsl:text>&lt;/</xsl:text>
				<xsl:value-of select="name()"/>
				<xsl:text>&gt;</xsl:text>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="@*" mode="copy">
		<xsl:text> </xsl:text>
		<xsl:value-of select="name()"/>
		<xsl:text>=\&quot;</xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>\&quot;</xsl:text>
	</xsl:template>
	<xsl:template match="comment()" mode="copy">
		<xsl:text>&lt;!--</xsl:text>
		<xsl:call-template name="replace-substring">
			<xsl:with-param name="original" select="."/>
			<xsl:with-param name="substring" select="'&quot;'"/>
			<xsl:with-param name="replacement" select="'\&quot;'"/>
		</xsl:call-template>
		<xsl:text>--&gt;</xsl:text>
	</xsl:template>
	<xsl:template match="text() " mode="copy">
		<xsl:variable name="temps4">
			<xsl:variable name="temps3">
				<xsl:variable name="temps2">
					<xsl:variable name="temps1">
						<xsl:call-template name="replace-substring">
							<xsl:with-param name="original" select="normalize-space(.)"/>
							<xsl:with-param name="substring" select="'\'"/>
							<xsl:with-param name="replacement" select="'\\'"/>
						</xsl:call-template>
					</xsl:variable>
					<xsl:call-template name="replace-substring">
						<xsl:with-param name="original" select="$temps1"/>
						<xsl:with-param name="substring" select="'&gt;'"/>
						<xsl:with-param name="replacement" select="'&amp;gt;'"/>
					</xsl:call-template>
				</xsl:variable>
				<xsl:call-template name="replace-substring">
					<xsl:with-param name="original" select="$temps2"/>
					<xsl:with-param name="substring" select="'&lt;'"/>
					<xsl:with-param name="replacement" select="'&amp;lt;'"/>
				</xsl:call-template>
			</xsl:variable>
			<xsl:call-template name="replace-substring">
				<xsl:with-param name="original" select="$temps3"/>
				<xsl:with-param name="substring" select="'&amp;'"/>
				<xsl:with-param name="replacement" select="'&amp;amp;'"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:call-template name="replace-substring">
			<xsl:with-param name="original" select="$temps4"/>
			<xsl:with-param name="substring" select="'&quot;'"/>
			<xsl:with-param name="replacement" select="'\&quot;'"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="processing-instruction() " mode="copy">
		<xsl:text>&lt;?</xsl:text>
		<xsl:value-of select="name()"/>
		<xsl:text> </xsl:text>
		<xsl:call-template name="replace-substring">
			<xsl:with-param name="original" select="."/>
			<xsl:with-param name="substring" select="'&quot;'"/>
			<xsl:with-param name="replacement" select="'\&quot;'"/>
		</xsl:call-template>
		<xsl:text>?&gt;</xsl:text>
	</xsl:template>
	<xsl:template name="replace-substring">
		<xsl:param name="original"/>
		<xsl:param name="substring"/>
		<xsl:param name="replacement" select="''"/>
		<xsl:variable name="first">
			<xsl:choose>
				<xsl:when test="contains($original, $substring)">
					<xsl:value-of select="substring-before($original, $substring)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$original"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="middle">
			<xsl:choose>
				<xsl:when test="contains($original, $substring)">
					<xsl:value-of select="$replacement"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="last">
			<xsl:choose>
				<xsl:when test="contains($original, $substring)">
					<xsl:choose>
						<xsl:when test="contains(substring-after($original, $substring), 
                                   $substring)">
							<xsl:call-template name="replace-substring">
								<xsl:with-param name="original">
									<xsl:value-of select="substring-after($original, $substring)"/>
								</xsl:with-param>
								<xsl:with-param name="substring">
									<xsl:value-of select="$substring"/>
								</xsl:with-param>
								<xsl:with-param name="replacement">
									<xsl:value-of select="$replacement"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="substring-after($original, $substring)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="concat($first, $middle, $last)"/>
	</xsl:template>
</xsl:stylesheet>
