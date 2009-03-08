<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 					
					version="1.0"
					xmlns="http://www.w3.org/1999/xhtml"
					xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
					>
<!--
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography

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



	<!-- 	
		Auteur: Erik van de Pol. B3Partners.

		Dependencies (for this XSL):
			functions.xsl = some basic XML functions
	
		 Dependencies (of resulting HTML page):
			(These dependencies should all be handled by the calling stylesheet)
			* Javascript code for handling dhtml
	-->
	
	<!-- XSL script and function library -->
	<!--<xsl:import href="functions.xsl"/>-->
	<xsl:include href="functions.xsl"/>	
	
	<!-- self reference text ('this' for JavaScript, 'me' for VBScript -->
	<xsl:variable name="SELF_REF">this</xsl:variable>

	<!-- default text for elements with no default value specified -->
	<xsl:variable name="GLOBAL_DEFAULT">Klik om te bewerken.</xsl:variable>

	<!-- add/delete elements/sections menu text -->
	<xsl:variable name="ADD_ELEMENT_ABOVE_TEXT">Voeg element hierboven toe</xsl:variable>
	<xsl:variable name="ADD_ELEMENT_BELOW_TEXT">Voeg element hieronder toe</xsl:variable>
	<xsl:variable name="DELETE_ELEMENT_TEXT">Verwijder dit element</xsl:variable>
	<xsl:variable name="ADD_SECTION_ABOVE_TEXT">Voeg sectie hierboven toe</xsl:variable>
	<xsl:variable name="ADD_SECTION_BELOW_TEXT">Voeg sectie hieronder toe</xsl:variable>
	<xsl:variable name="DELETE_SECTION_TEXT">Verwijder deze sectie</xsl:variable>
	<xsl:variable name="ADD_CHILD_TEXT">Voeg nieuw kind toe</xsl:variable>
	
	<!-- expand/collapse section and menu image paths -->
	<xsl:variable name="EXPAND_TEXT">Klap deze sectie in/uit</xsl:variable>
	
	<xsl:variable name="PLUS_IMAGE"><xsl:value-of select="$basePath"/>images/xp_plus.gif</xsl:variable>
	<xsl:variable name="MINUS_IMAGE"><xsl:value-of select="$basePath"/>images/xp_minus.gif</xsl:variable>
	<xsl:variable name="MENU_IMAGE"><xsl:value-of select="$basePath"/>images/arrow.gif</xsl:variable>
	
	<xsl:variable name="MENU_TOOLTIP">Klik om opties te zien</xsl:variable>
	
	<!-- ============================================ -->
	<!-- TEMPLATE FUNCTIONS FOR FULLXML STYLESHEETS -->
	<!-- ============================================ -->


	<!-- TEMPLATE: voor een element dat verandert kan worden door de gebruiker. Het kiest per default het huidige pad als element dat geëdit kan worden -->
	<xsl:template name="element">
		<xsl:param name="title"/> <!-- verplicht voor mooie weergave -->
		<xsl:param name="picklist"/>
		<xsl:param name="path" select="."/>
		<xsl:param name="default-value"/>
		<xsl:param name="optionality" select="'optional'"/><!-- 'conditional' of 'mandatory' of 'optional' of leeg (= optional). Mandatory wordt altijd opgeslagen -->
		<xsl:param name="repeatable" select="'false'"/>
		<xsl:param name="readonly" select="'false'"/>
		<xsl:param name="help-text"/>
		<xsl:param name="field-type"/><!-- 'text' of 'date' of leeg (= text) (nog verder uitbreidbaar, zie generic_edit.js startEdit -->
		
		<xsl:variable name="class">
			<xsl:choose>
				<xsl:when test="$optionality = 'mandatory' and $readonly = 'false'">
					<xsl:text>key-mandatory</xsl:text>
				</xsl:when>
				<xsl:when test="$optionality = 'conditional' and $readonly = 'false'">
					<xsl:text>key-conditional</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>key</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<div class="'element'">
			<xsl:if test="$title != ''">
				<!-- strange layout (in this xslt-file) to get whitespace right -->
				<span class="{$class}"><xsl:value-of select="$title"/><xsl:if test="$repeatable = 'true' and $readonly = 'false'">
						<xsl:call-template name="repeatable-menu">
							<xsl:with-param name="path">
								<xsl:call-template name="full-path"/>					
							</xsl:with-param>
							<xsl:with-param name="type" select="'element'"/>
						</xsl:call-template>
					</xsl:if>: <!-- whitespace issue -->
				</span>				
			</xsl:if>
			<xsl:element name="span">
				<xsl:attribute name="fullPath">
					<xsl:call-template name="full-path">					
						<xsl:with-param name="theParmNodes" select="$path"/>
					</xsl:call-template>
				</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help-text"/></xsl:attribute>
				<xsl:attribute name="default"><xsl:value-of select="$default-value"/></xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:if test="$readonly = 'false'">
					<xsl:attribute name="onclick">startEdit(event)</xsl:attribute>
				</xsl:if>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:attribute name="field-type"><xsl:value-of select="$field-type"/></xsl:attribute>
				<xsl:choose>
					<!-- check of de inhoud van $path leeg is -->
					<xsl:when test="normalize-space($path)">
						<xsl:attribute name="class">unchanged-value</xsl:attribute>
						
						<!-- Possible TODO: (for later) uitkijken: types moeten hieruit gedestilleerd worden -->
						<!--<xsl:value-of select="$element_path"/>-->
						<xsl:value-of select="normalize-space($path)"/>
						<!--<xsl:apply-templates select=".//"/> -->
						
					</xsl:when>
					<xsl:when test="@codeListValue != '' ">
						<xsl:attribute name="class">unchanged-value</xsl:attribute>
						<xsl:value-of select="normalize-space(@codeListValue)"/>
					</xsl:when>							
					<xsl:otherwise>
						<!-- set value to default, set changed to true; ?Niet doen: default values niet opslaan dus? inderdaad-->
						<xsl:attribute name="class">default-value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:choose>
							<xsl:when test="$readonly = 'true'">
								<xsl:value-of select="'-'"/>
							</xsl:when>
							<xsl:when test="$default-value != ''">
								<xsl:value-of select="$default-value"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$GLOBAL_DEFAULT"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
		</div>

	</xsl:template>
	
	
	
	<!-- TEMPLATE: for (repeating) section -->
	<xsl:template name="section">
		<xsl:param name="title"/>
		<xsl:param name="section-path" select="."/>
		<xsl:param name="expanded" select="'true'"/>
		<xsl:param name="repeatable" select="'false'"/>
		<xsl:param name="readonly" select="'false'"/>
		

		<!-- display each repeating section -->
		<div class="section" section-path="{$section-path}" id="{$section-path}">

			<xsl:call-template name="section-title">
				<xsl:with-param name="title" select="$title"/>
				<xsl:with-param name="section-path" select="$section-path"/>
				<xsl:with-param name="expanded" select="$expanded"/>
				<xsl:with-param name="repeatable" select="$repeatable"/>
				<xsl:with-param name="readonly" select="$readonly"/>
			</xsl:call-template>

			<!-- add subelements of section within a "section-content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">section-content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<!-- valid local name? -->
					<xsl:when test="local-name()">
						<xsl:element name="{local-name()}">
							<xsl:apply-templates/>
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<!-- error, missing local-name, display message -->

						<h2 class="error">Transform error: Local name is blank</h2>
						<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
						<p class="error">name() = <xsl:value-of select="name()"/></p>

					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>

		</div>
	

	</xsl:template>
	


	<!-- TEMPLATE: for section-title -->
	<!-- 
	Gebruik in plaats van "section" "section-title" in combinatie met een <div class="content"/> met daarin de content van de section
	om custom content toe te voegen aan de sectie (of in de praktijk vaak om de volgorde van de content-tags te veranderen).
	Template "section" verkrijgt zijn content door <xsl:apply-templates/> aan te roepen.
	-->
	<xsl:template name="section-title">
		<xsl:param name="title"/>
		<xsl:param name="section-path" select="."/>
		<xsl:param name="expanded" select="'true'"/>
		<xsl:param name="expandable" select="'true'"/>		
		<xsl:param name="repeatable" select="'false'"/>
		<xsl:param name="readonly" select="'false'"/>
		
		<div class="section-header">
			<xsl:choose>
				<xsl:when test="$expandable = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false;" class="expandable" title="{$EXPAND_TEXT}">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img class="plus-minus" src="{$MINUS_IMAGE}"/>
							</xsl:when>
							<xsl:otherwise>
								<img class="plus-minus" src="{$PLUS_IMAGE}"/>
							</xsl:otherwise>
						</xsl:choose>

						<span class="section-title" onmouseover="sectionTitleHover(this);"><xsl:value-of select="$title"/></span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<span class="section-title"><xsl:value-of select="$title"/></span>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:if test="$repeatable = 'true'  and $readonly = 'false'">
				<xsl:call-template name="repeatable-menu">
					<xsl:with-param name="path" select="$section-path"/>
					<xsl:with-param name="type" select="'section'"/>
				</xsl:call-template>
			</xsl:if>
		</div>

	</xsl:template>
	
	
	<xsl:template name="repeatable-menu">
		<xsl:param name="path" select="."/>
		<xsl:param name="type" select="'element'"/><!-- allowed values: 'element' or 'section'-->
		
		<!-- add popup menu for repeat section options -->

		<xsl:variable name="jsFunction">
			<xsl:choose>
				<xsl:when test="$type = 'element'">
					<xsl:text>Element</xsl:text>
				</xsl:when>
				<xsl:when test="$type = 'section'">
					<xsl:text>Section</xsl:text>
				</xsl:when>				
				<xsl:otherwise>
					<h2 class="error">Argument 'type' not valid. It must be either 'element' or 'section' (both with quotes)</h2>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	
		<xsl:variable name="menu-img-class">
			<xsl:choose>
				<xsl:when test="$type = 'element'">
					<xsl:text>menu-img-element</xsl:text>
				</xsl:when>
				<xsl:when test="$type = 'section'">
					<xsl:text>menu-img-section</xsl:text>
				</xsl:when>				
				<xsl:otherwise>
					<h2 class="error">Argument 'type' not valid. It must be either 'element' or 'section' (both with quotes)</h2>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
	
		<span onclick="showMenu(event);">
			<xsl:element name="img">
				<xsl:attribute name="src"><xsl:value-of select="$MENU_IMAGE"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$MENU_TOOLTIP"/></xsl:attribute>
				<xsl:attribute name="onmouseover">menuImgHover(this);</xsl:attribute>
				<xsl:attribute name="class"><xsl:value-of select="$menu-img-class"/></xsl:attribute>
			</xsl:element>
			<ul class="menu" style="display: none;">
				<li class="menuaddabove">
					<xsl:element name="a">
						<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
						<xsl:attribute name="onclick">add<xsl:value-of select="$jsFunction"/>(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$path"/>',true)</xsl:attribute>
						<xsl:choose>
							<xsl:when test="$type = 'element'">
								<xsl:value-of select="$ADD_ELEMENT_ABOVE_TEXT"/>
							</xsl:when>
							<xsl:when test="$type = 'section'">
								<xsl:value-of select="$ADD_SECTION_ABOVE_TEXT"/>
							</xsl:when>
						</xsl:choose>
					</xsl:element>
				</li>
				<li class="menuaddbelow">
					<xsl:element name="a">
						<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
						<xsl:attribute name="onclick">add<xsl:value-of select="$jsFunction"/>(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$path"/>',false)</xsl:attribute>
						<xsl:choose>
							<xsl:when test="$type = 'element'">
								<xsl:value-of select="$ADD_ELEMENT_BELOW_TEXT"/>
							</xsl:when>
							<xsl:when test="$type = 'section'">
								<xsl:value-of select="$ADD_SECTION_BELOW_TEXT"/>
							</xsl:when>
						</xsl:choose>
					</xsl:element>
				</li>
				<li class="menudelete">
					<xsl:element name="a">
						<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
						<xsl:attribute name="onclick">delete<xsl:value-of select="$jsFunction"/>(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$path"/>')</xsl:attribute>
						<xsl:choose>
							<xsl:when test="$type = 'element'">
								<xsl:value-of select="$DELETE_ELEMENT_TEXT"/>
							</xsl:when>
							<xsl:when test="$type = 'section'">
								<xsl:value-of select="$DELETE_SECTION_TEXT"/>
							</xsl:when>
						</xsl:choose>
					</xsl:element>
				</li>
			</ul>
		</span>

	</xsl:template>
	
	
	<!-- TEMPLATE: geeft een anchor weer -->
	<xsl:template name="anchor">
		<xsl:param name="href" select="."/>
		<xsl:param name="name-shown">no name</xsl:param>
		<xsl:param name="target">viewer</xsl:param>

		<xsl:element name="a">
			<!--<xsl:attribute name="xmlns">http://www.w3.org/1999/xhtml</xsl:attribute>-->
			<xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute>
			<xsl:attribute name="target"><xsl:value-of select="$target"/></xsl:attribute>
			<xsl:value-of select="$name-shown"/>
		</xsl:element>

	</xsl:template>
	
	<!-- TEMPLATE: geeft een mailto-anchor weer -->
	<xsl:template name="mailtoAnchor">
		<xsl:param name="email" select="."/>
		<xsl:param name="name-shown">no name</xsl:param>

		<xsl:element name="a">
			<!--<xsl:attribute name="xmlns">http://www.w3.org/1999/xhtml</xsl:attribute>-->
			<xsl:attribute name="href">mailto:<xsl:value-of select="$email"/></xsl:attribute>
			<xsl:value-of select="$name-shown"/>
		</xsl:element>

	</xsl:template>
	

</xsl:stylesheet>
