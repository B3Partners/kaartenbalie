<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- 	
		Auteur: Erik van de Pol. B3Partners.

		Deze file bevat de core templates voor fullXML mode editing. Maar met meer vrijheid en diversiteit dan in
		"editable.xsl". Geschikt voor het invoeren van extra (niet editable) tekst en bijvoorbeeld een tab-layout.
		Gedeeltes van de code zijn gebaseerd op "editable.xsl" van NPS Metadata Editor 1.1

		Dependencies (for this XSL):
			functions.xsl = some basic XML functions
	
		 Dependencies (of resulting HTML page):
			(These dependencies should all be handled by the calling stylesheet)
			* several images (plus, minus, images)
			* VBScript code for handling dhtml
	-->
	
	<!-- XSL script and function library -->
	<!--<xsl:import href="functions.xsl"/>-->
	<xsl:include href="functions.xsl"/>	
	
	<!-- self reference text ('this' for JavaScript, 'me' for VBScript -->
	<xsl:variable name="SELF_REF">this</xsl:variable>

	<!-- default text for elements with no default value specified -->
	<xsl:variable name="GLOBAL_DEFAULT">Klik hier om deze tekst te bewerken.</xsl:variable>

	<!-- add/delete elements/sections menu text -->
	<xsl:variable name="ADD_ELEMENT_ABOVE_TEXT">Voeg element hierboven toe</xsl:variable>
	<xsl:variable name="ADD_ELEMENT_BELOW_TEXT">Voeg element hieronder toe</xsl:variable>
	<xsl:variable name="DELETE_ELEMENT_TEXT">Verwijder dit element</xsl:variable>
	<xsl:variable name="ADD_SECTION_ABOVE_TEXT">Voeg sectie hierboven toe</xsl:variable>
	<xsl:variable name="ADD_SECTION_BELOW_TEXT">Voeg sectie hieronder toe</xsl:variable>
	<xsl:variable name="DELETE_SECTION_TEXT">Verwijder deze sectie</xsl:variable>
	<xsl:variable name="ADD_CHILD_TEXT">Voeg nieuw kind toe</xsl:variable>
	
	<!-- expand/collapse section and menu image paths -->
	<xsl:variable name="PLUS_IMAGE">images/xp_plus.gif</xsl:variable>
	<xsl:variable name="MINUS_IMAGE">images/xp_minus.gif</xsl:variable>
	<xsl:variable name="MENU_IMAGE">images/arrow.gif</xsl:variable>
	<xsl:variable name="MENU_TOOLTIP">Klik om opties te zien</xsl:variable>
	
	
	
	
	<!-- ============================================ -->
	<!-- TEMPLATE FUNCTIONS FOR FULLXML STYLESHEETS -->
	<!-- ============================================ -->


	<!-- TEMPLATE: voor een element dat verandert kan worden door de gebruiker. Het kiest per default het huidige pad als element dat geëdit kan worden -->
	<xsl:template name="edit_element">
		<xsl:param name="element_title"/>
		<xsl:param name="element_path" select="."/>
		<xsl:param name="default_value"/>
		<xsl:param name="optionality" select="'optional'"/><!-- 'mandatory' of 'optional' of leeg (= mandatory). Mandatory wordt altijd opgeslagen -->
		<xsl:param name="help_text"/>
		<xsl:param name="picklist"/>
		<xsl:param name="trailing_text"/>

		<xsl:variable name="class">
			<xsl:choose>
				<xsl:when test="$optionality = 'mandatory'">
					<xsl:text>single_Melement</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>single_Belement</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<div class="{$class}">
			<xsl:if test="$element_title != ''">
				<xsl:value-of select="$element_title"/>:
			</xsl:if>
			<xsl:element name="span">
				<xsl:attribute name="name">
					<!-- <xsl:call-template name="dynamic_path"> -->
					<xsl:call-template name="full_path">					
						<xsl:with-param name="theParmNodes" select="$element_path"/>
					</xsl:call-template>
				</xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="default"><xsl:value-of select="$default_value"/></xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:attribute name="Sync"><xsl:value-of select="@Sync"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space($element_path)">
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="$element_path"/>		
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true; ?Niet doen: default values niet opslaan dus? inderdaad-->
						<xsl:attribute name="class">default_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:choose>
							<xsl:when test="$default_value != ''">
								<xsl:value-of select="$default_value"/>
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
		<xsl:param name="section_Mtitle"/>
		<xsl:param name="section_path" select="."/>
		<xsl:param name="expanding" select="'true'"/>
		<xsl:param name="expanded" select="'true'"/>
		<xsl:param name="repeatable" select="'false'"/>
		<xsl:param name="add_child"/>
		

		<!-- display each repeating section -->
		<div class="folder" section_path="{$section_path}" id="{$section_path}">


			<xsl:call-template name="section-title">
				<xsl:with-param name="section_Mtitle" select="$section_Mtitle"/>
				<xsl:with-param name="expanding" select="$expanding"/>
				<xsl:with-param name="expanded" select="$expanded"/>
			</xsl:call-template>

			
			<!-- add popup menu for repeat section options -->
			<xsl:if test="$repeatable = 'true' ">
				<xsl:variable name="imgclass">
					<xsl:choose>
						<xsl:when test="$expanding = 'true'">
							<xsl:text>menuimg</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text></xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<span class="{$imgclass}" onclick="ShowMenu({$SELF_REF})">
					<xsl:element name="img">
						<xsl:attribute name="src"><xsl:value-of select="$MENU_IMAGE"/></xsl:attribute>
						<xsl:attribute name="title"><xsl:value-of select="$MENU_TOOLTIP"/></xsl:attribute>
					</xsl:element>
					<ul class="menu" onmouseleave="HideMenu({$SELF_REF})">
						<li class="menuaddabove">
							<xsl:element name="a">
								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
								<xsl:attribute name="onclick">addSection(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$section_path"/>',true)</xsl:attribute>
								<xsl:value-of select="$ADD_SECTION_ABOVE_TEXT"/>
							</xsl:element>
						</li>
						<li class="menuaddbelow">
							<xsl:element name="a">
								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
								<xsl:attribute name="onclick">addSection(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$section_path"/>',false)</xsl:attribute>
								<xsl:value-of select="$ADD_SECTION_BELOW_TEXT"/>
							</xsl:element>
						</li>
						<li class="menudelete">
							<xsl:element name="a">
								<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
								<xsl:attribute name="onclick">deleteSection(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$section_path"/>')</xsl:attribute>
								<xsl:value-of select="$DELETE_SECTION_TEXT"/>
							</xsl:element>
						</li>
						<!-- add menu option for add child? -->
						<xsl:if test="$add_child = 'true'">
							<li class="menuaddchild">
								<xsl:element name="a">
									<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
									<xsl:attribute name="onclick">addChild(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$section_path"/>')</xsl:attribute>
									<xsl:value-of select="$ADD_CHILD_TEXT"/>
								</xsl:element>
							</li>
						</xsl:if>
					</ul>
				</span>
			</xsl:if>
	
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none</xsl:attribute>
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
	Gebruik in plaats van "section" "section-title" in combinatie met de twee templates daaronder ("section-content-open" en "section-content-close")
	om custom content toe te voegen aan de sectie (of in de praktijk vaak om de volgorde van de content-tags te veranderen).
	Template "section" verkrijgt zijn content door <xsl:apply-templates/> aan te roepen.
	-->
	<xsl:template name="section-title">
		<xsl:param name="section_Mtitle"/>
		<xsl:param name="expanding" select="'true'"/>
		<xsl:param name="expanded" select="'true'"/>
		
		<xsl:choose>
			<xsl:when test="$expanding = 'true'"> 
				<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
					<!-- start expanded or collapsed? -->
					<xsl:choose>
						<xsl:when test="$expanded = 'true'">
							<img src="{$MINUS_IMAGE}" border="0px"/>
						</xsl:when>
						<xsl:otherwise>
							<img src="{$PLUS_IMAGE}" border="0px"/>
						</xsl:otherwise>
					</xsl:choose>

					<span class="section_Mtitle" onmouseover="section_MtitleHover(this)"><xsl:value-of select="$section_Mtitle"/></span>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<span class="section_Mtitle"><xsl:value-of select="$section_Mtitle"/></span>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>
	

	<!-- TEMPLATE: geeft een separator weer in fullXML mode -->
	<xsl:template name="separator">
		<div class="separator">_________________</div>
	</xsl:template>
	
	<xsl:template name="get-br">
		<br/>
	</xsl:template>
	
	<!-- TEMPLATE: geeft een anchor weer in fullXML mode -->
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
	
	<!-- TEMPLATE: geeft een mailto-anchor weer in fullXML mode -->
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
