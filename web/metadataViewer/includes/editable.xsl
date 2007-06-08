<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Created: 1/2005 Eric Compas
		 Updated: 2/1/05 Eric Compas - modify to make sections, menu, and elements
									   more dynamic and intuitive
									 - add option to have section start collapsed
									   
		 Description:
			These templates create the bulk of the HTML code for an editable
			stylesheet by taking information about each element or section
			and building nested DIV and SPAN tags containing all of the
			dynamic HTML calls. Javascript and VBScript code is stored
			elsewhere. Start and ending of each function closes and opens
			a hidden tag holding elided XML (what's not exposed by the editor).
	
		 Dependencies (for this XSL):
			functions.xsl = some basic XML functions
	
		 Dependencies (of resulting HTML page):
			(These dependencies should all be handled by the calling stylesheet)
			* several images (plus, minus, images)
			* VBScript code for handling dhtml
	-->
	
	<!-- XSL script and function library -->
	<xsl:import href="functions.xsl"/>
	
	<!-- ================ -->
	<!-- GLOBAL VARIABLES -->
	<!-- ================ -->
	
	<!-- change these variable to alter -->
	<!-- the text in popup menus -->
	<!-- and links to the stylesheet images -->
	
	<!-- self reference text ('this' for JavaScript, 'me' for VBScript -->
	<xsl:variable name="SELF_REF">this</xsl:variable>
	
	<!-- default text for elements with no default value specified -->
	<xsl:variable name="GLOBAL_DEFAULT">Click here to edit text.</xsl:variable>
	
	<!-- add/delete elements/sections menu text -->
	<xsl:variable name="ADD_ELEMENT_ABOVE_TEXT">Insert element above</xsl:variable>
	<xsl:variable name="ADD_ELEMENT_BELOW_TEXT">Insert element below</xsl:variable>
	<xsl:variable name="DELETE_ELEMENT_TEXT">Delete this element</xsl:variable>
	<xsl:variable name="ADD_SECTION_ABOVE_TEXT">Insert section above</xsl:variable>
	<xsl:variable name="ADD_SECTION_BELOW_TEXT">Insert section below</xsl:variable>
	<xsl:variable name="DELETE_SECTION_TEXT">Delete this section</xsl:variable>
	<xsl:variable name="ADD_CHILD_TEXT">Insert new child</xsl:variable>
	
	<!-- expand/collapse section and menu image paths -->
	<xsl:variable name="PLUS_IMAGE">images/xp_plus.gif</xsl:variable>
	<xsl:variable name="MINUS_IMAGE">images/xp_minus.gif</xsl:variable>
	<xsl:variable name="MENU_IMAGE">images/arrow.gif</xsl:variable>
	<xsl:variable name="MENU_TOOLTIP">Click for options</xsl:variable>
	
	<!-- ================================================= -->
	<!-- TEMPLATE FUNCTIONS FOR SINGLE-ELEMENT STYLESHEETS -->
	<!-- ================================================= -->

	<xsl:template name="edit_element">
		<!-- Description: creates an editable element for use in single element/static -->
		<!-- editable stylesheets. Usually used in place of <xsl:value-of select="."/> -->
		<xsl:param name="theNodes"/>   <!-- the node to process, make editable -->
		<xsl:element name="span">
			<xsl:attribute name="class">unchanged_value</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:call-template name="dynamic_path">
					<xsl:with-param name="theParmNodes" select="$theNodes"/>
				</xsl:call-template>
			</xsl:attribute>
				<!-- JavaScript self-reference -->
			<xsl:attribute name="onClick">startEdit(this)</xsl:attribute>
			<xsl:attribute name="changed">false</xsl:attribute>
			<xsl:value-of select="$theNodes"/>
		</xsl:element>
	</xsl:template>
	
	<!-- ================================================ -->
	<!-- TEMPLATE FUNCTIONS FOR FULL XML EDIT STYLESHEETS -->
	<!-- ================================================ -->

	<!-- TEMPLATE: for single elements that displayed but not editable -->
	<xsl:template name="noedit_element">
		<xsl:param name="element_title"/>
		<xsl:param name="element_path"/>
		<xsl:param name="default_value"/>
		<xsl:param name="optionality"/>
		<xsl:param name="help_text"/>
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<xsl:attribute name="class"><xsl:value-of select="$optionality"/></xsl:attribute>
		<p class="single_element">
			<xsl:value-of select="$element_title"/>:
			<xsl:element name="span">
				<xsl:attribute name="class">noedit</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<!-- display current value -->
						<xsl:value-of select="."/>		
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default  -->
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
		</p>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>
	
	
	<!-- TEMPLATE: for non-repeating (single) text element -->
	<xsl:template name="single_element">
		<xsl:param name="element_title"/>
		<xsl:param name="element_path"/>
		<xsl:param name="default_value"/>
		<xsl:param name="optionality"/>
		<xsl:param name="help_text"/>
		<xsl:param name="picklist" />
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<p class="single_element">
			<xsl:value-of select="$element_title"/>:
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="default"><xsl:value-of select="$default_value"/></xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:attribute name="Sync"><xsl:value-of select="@Sync"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>		
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
		</p>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>
	
   <xsl:template name="single_Belement">
		<xsl:param name="element_title"/>
		<xsl:param name="element_path"/>
		<xsl:param name="default_value"/>
		<xsl:param name="optionality"/>
		<xsl:param name="help_text"/>
		<xsl:param name="picklist" />
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<p class="single_Belement">
			<xsl:value-of select="$element_title"/>:
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="default"><xsl:value-of select="$default_value"/></xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:attribute name="Sync"><xsl:value-of select="@Sync"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>		
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
		</p>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>	
	
	<!-- TEMPLATE: for non-repeating (single) text element -->
	<xsl:template name="single_Melement">
		<xsl:param name="element_title"/>
		<xsl:param name="element_path"/>
		<xsl:param name="default_value"/>
		<xsl:param name="optionality"/>
		<xsl:param name="help_text"/>
		<xsl:param name="picklist" />
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<p class="single_Melement">
			<xsl:value-of select="$element_title"/>:
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="default"><xsl:value-of select="$default_value"/></xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:attribute name="Sync"><xsl:value-of select="@Sync"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>		
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
		</p>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>
	
	<!-- TEMPLATE: for repeating text element -->
	<!-- (for repeating elements called for each element,
		  template detects first and last elements to both
		  open and close <UL> span and element title)-->
	<xsl:template name="repeat_element">
		<xsl:param name="element_title" />
		<xsl:param name="element_path" />
		<xsl:param name="default_value" />
		<xsl:param name="optionality" />
		<xsl:param name="help_text" />
		<xsl:param name="picklist" />
		
		<!-- at beginning of repeating element -->
		<!--xsl:choose-->
		<xsl:if test="count(preceding-sibling::*[name(current()) = name()]) = 0"> <!-- any of the same preceding? -->
			<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
			<p class="repeat_element"><xsl:value-of select="$element_title"/></p>
			<xsl:text disable-output-escaping="yes"><![CDATA[<ul class="repeat">]]></xsl:text>
		</xsl:if>
				<!--/xsl:choose-->		
		<!-- display each repeating element -->
		<li>
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<!-- element has non-blank value, use it -->
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
			<!-- add popup menu for repeat element options -->
			<span onclick="ShowMenu({$SELF_REF})">
				<xsl:element name="img">
					<xsl:attribute name="src"><xsl:value-of select="$MENU_IMAGE"/></xsl:attribute>
					<xsl:attribute name="title"><xsl:value-of select="$MENU_TOOLTIP"/></xsl:attribute>
				</xsl:element>
				<ul class="menu" onmouseleave="HideMenu({$SELF_REF})">
					<li class="menuaddabove">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', true)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_ABOVE_TEXT"/>
						</xsl:element>
					</li>
					<li class="menuaddbelow">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', false)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_BELOW_TEXT"/>
						</xsl:element>
					</li>
					<li class="menudelete">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">deleteListItem(<xsl:value-of select="$SELF_REF"/>)</xsl:attribute>
							<xsl:value-of select="$DELETE_ELEMENT_TEXT"/>
						</xsl:element>
					</li>
				</ul>
			</span>
		</li>
	
		<!-- at end of repeating element -->
		<xsl:if test="count(following-sibling::*[name(current()) = name()]) = 0"> 
			<xsl:text disable-output-escaping="yes"><![CDATA[</ul>]]></xsl:text>
			<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
		</xsl:if>
		
	</xsl:template>
			
	<!-- TEMPLATE: for repeating Bold text element -->
	<!-- (for repeating Bold elements called for each element,
		  template detects first and last elements to both
		  open and close <UL> span and element title)-->
	<xsl:template name="repeat_Belement">
		<xsl:param name="element_title" />
		<xsl:param name="element_path" />
		<xsl:param name="default_value" />
		<xsl:param name="optionality" />
		<xsl:param name="help_text" />
		<xsl:param name="picklist" />
		
		<!-- at beginning of repeating element -->
		<!--xsl:choose-->
		<xsl:if test="count(preceding-sibling::*[name(current()) = name()]) = 0"> <!-- any of the same preceding? -->
			<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
			<p class="repeat_Belement"><xsl:value-of select="$element_title"/></p>
			<xsl:text disable-output-escaping="yes"><![CDATA[<ul class="repeat">]]></xsl:text>
		</xsl:if>
				<!--/xsl:choose-->		
		<!-- display each repeating element -->
		<li>
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<!-- element has non-blank value, use it -->
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
			<!-- add popup menu for repeat element options -->
			<span onclick="ShowMenu({$SELF_REF})">
				<xsl:element name="img">
					<xsl:attribute name="src"><xsl:value-of select="$MENU_IMAGE"/></xsl:attribute>
					<xsl:attribute name="title"><xsl:value-of select="$MENU_TOOLTIP"/></xsl:attribute>
				</xsl:element>
				<ul class="menu" onmouseleave="HideMenu({$SELF_REF})">
					<li class="menuaddabove">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', true)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_ABOVE_TEXT"/>
						</xsl:element>
					</li>
					<li class="menuaddbelow">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', false)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_BELOW_TEXT"/>
						</xsl:element>
					</li>
					<li class="menudelete">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">deleteListItem(<xsl:value-of select="$SELF_REF"/>)</xsl:attribute>
							<xsl:value-of select="$DELETE_ELEMENT_TEXT"/>
						</xsl:element>
					</li>
				</ul>
			</span>
		</li>
	
		<!-- at end of repeating element -->
		<xsl:if test="count(following-sibling::*[name(current()) = name()]) = 0"> 
			<xsl:text disable-output-escaping="yes"><![CDATA[</ul>]]></xsl:text>
			<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
		</xsl:if>
		
	</xsl:template>

	<!-- TEMPLATE: for repeating Mandatory text element -->
	<!-- (for repeating Mandatory elements called for each element,
		  template detects first and last elements to both
		  open and close <UL> span and element title)-->	
	<xsl:template name="repeat_Melement">
		<xsl:param name="element_title" />
		<xsl:param name="element_path" />
		<xsl:param name="default_value" />
		<xsl:param name="optionality" />
		<xsl:param name="help_text" />
		<xsl:param name="picklist" />
		
		<!-- at beginning of repeating element -->
		<!--xsl:choose-->
		<xsl:if test="count(preceding-sibling::*[name(current()) = name()]) = 0"> <!-- any of the same preceding? -->
			<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
			<p class="repeat_Melement"><xsl:value-of select="$element_title"/></p>
			<xsl:text disable-output-escaping="yes"><![CDATA[<ul class="repeat">]]></xsl:text>
		</xsl:if>
				<!--/xsl:choose-->		
		<!-- display each repeating element -->
		<li>
			<xsl:element name="span">
				<xsl:attribute name="name"><xsl:value-of select="$element_path"/></xsl:attribute>
				<xsl:attribute name="title"><xsl:value-of select="$help_text"/></xsl:attribute>
				<xsl:attribute name="onclick">startEdit(this)</xsl:attribute>
				<xsl:attribute name="optionality"><xsl:value-of select="$optionality" /></xsl:attribute>
				<xsl:if test="$picklist != ''">
					<xsl:attribute name="picklist"><xsl:value-of select="$picklist"/></xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="normalize-space(.)">
						<!-- element has non-blank value, use it -->
						<xsl:attribute name="class">unchanged_value</xsl:attribute>
						<xsl:attribute name="changed">false</xsl:attribute>
						<xsl:value-of select="."/>
					</xsl:when>
					<xsl:otherwise>
						<!-- set value to default, set changed to true  -->
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
			<!-- add popup menu for repeat element options -->
			<span onclick="ShowMenu({$SELF_REF})">
				<xsl:element name="img">
					<xsl:attribute name="src"><xsl:value-of select="$MENU_IMAGE"/></xsl:attribute>
					<xsl:attribute name="title"><xsl:value-of select="$MENU_TOOLTIP"/></xsl:attribute>
				</xsl:element>
				<ul class="menu" onmouseleave="HideMenu({$SELF_REF})">
					<li class="menuaddabove">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', true)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_ABOVE_TEXT"/>
						</xsl:element>
					</li>
					<li class="menuaddbelow">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">addListItem(<xsl:value-of select="$SELF_REF"/>,'<xsl:value-of select="$element_path"/>',50,'<xsl:value-of select="$default_value"/>','<xsl:value-of select="$picklist"/>', false)</xsl:attribute>
							<xsl:value-of select="$ADD_ELEMENT_BELOW_TEXT"/>
						</xsl:element>
					</li>
					<li class="menudelete">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">deleteListItem(<xsl:value-of select="$SELF_REF"/>)</xsl:attribute>
							<xsl:value-of select="$DELETE_ELEMENT_TEXT"/>
						</xsl:element>
					</li>
				</ul>
			</span>
		</li>
	
		<!-- at end of repeating element -->
		<xsl:if test="count(following-sibling::*[name(current()) = name()]) = 0"> 
			<xsl:text disable-output-escaping="yes"><![CDATA[</ul>]]></xsl:text>
			<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
		</xsl:if>
		
	</xsl:template>
	
	<!-- TEMPLATE: for non-repeating (single) section or node -->
	<xsl:template name="single_section">
		<xsl:param name="section_name"/>
		<xsl:param name="section_title"/>
		<xsl:param name="section_Stitle"/>
		<xsl:param name="section_path"/>
		<xsl:param name="optionality"/>
		<xsl:param name="expanding"/>
		<xsl:param name="expanded"/>
		
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<div class="folder" onmouseenter="ShowBox({$SELF_REF})" onmouseleave="HideBox({$SELF_REF})">
			<!-- expanding/collapsing section or not -->
			<xsl:choose>
				<xsl:when test="$expanding = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img src="{$MINUS_IMAGE}" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$PLUS_IMAGE}" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						<span class="section_title"><xsl:value-of select="$section_title"/></span>
						<span class="section_Stitle"><xsl:value-of select="$section_Stitle"/></span>
						<span class="section_path"> (<xsl:value-of select="$section_path"/>)</span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<span class="plus_minus">-</span>
					<span class="section_title"><xsl:value-of select="$section_title"/></span>
					<span class="section_Stitle"><xsl:value-of select="$section_Stitle"/></span>
					</xsl:otherwise>
			</xsl:choose>
			
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<span class="hiddenXML"><xsl:text disable-output-escaping="yes"><![CDATA[<!-- ]]></xsl:text>
					<xsl:choose>
						<!-- valid local name? -->
						<xsl:when test="local-name()">
							<xsl:element name="{local-name()}">
								<xsl:apply-templates/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<!-- error, missing local-name, display message -->
							<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
							<h2 class="error">Transform error: Local name is blank</h2>
							<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
							<p class="error">name() = <xsl:value-of select="name()"/></p>
							<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				<xsl:text disable-output-escaping="yes"><![CDATA[ -->]]></xsl:text></span>
			</xsl:element>
		</div>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>
	
	<!-- TEMPLATE: for non-repeating (single) MANDATORY section or node -->
	<xsl:template name="single_Msection">
		<xsl:param name="section_name"/>
		<xsl:param name="section_title"/>
		<xsl:param name="section_Mtitle"/>
		<xsl:param name="section_path"/>
		<xsl:param name="optionality"/>
		<xsl:param name="expanding"/>
		<xsl:param name="expanded"/>
		
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<div class="folder" onmouseenter="ShowBox({$SELF_REF})" onmouseleave="HideBox({$SELF_REF})">
			<!-- expanding/collapsing section or not -->
			<xsl:choose>
				<xsl:when test="$expanding = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img src="{$MINUS_IMAGE}" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$PLUS_IMAGE}" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
						<span class="section_Mtitle"><xsl:value-of select="$section_Mtitle"/></span>
						<span class="section_path"> (<xsl:value-of select="$section_path"/>)</span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<span class="plus_minus">-</span>
					<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
					<span class="section_Mtitle"><xsl:value-of select="$section_Mtitle"/></span>
					</xsl:otherwise>
			</xsl:choose>
			
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<span class="hiddenXML"><xsl:text disable-output-escaping="yes"><![CDATA[<!-- ]]></xsl:text>
					<xsl:choose>
						<!-- valid local name? -->
						<xsl:when test="local-name()">
							<xsl:element name="{local-name()}">
								<xsl:apply-templates/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<!-- error, missing local-name, display message -->
							<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
							<h2 class="error">Transform error: Local name is blank</h2>
							<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
							<p class="error">name() = <xsl:value-of select="name()"/></p>
							<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				<xsl:text disable-output-escaping="yes"><![CDATA[ -->]]></xsl:text></span>
			</xsl:element>
		</div>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>

	<!-- TEMPLATE: for non-repeating (single) FAQ section or node -->
	<xsl:template name="single_Fsection">
		<xsl:param name="section_name"/>
		<xsl:param name="section_title"/>
		<xsl:param name="section_Ftitle"/>
		<xsl:param name="section_path"/>
		<xsl:param name="optionality"/>
		<xsl:param name="expanding"/>
		<xsl:param name="expanded"/>
		
		<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		<div class="folder" onmouseenter="ShowBox({$SELF_REF})" onmouseleave="HideBox({$SELF_REF})">
			<!-- expanding/collapsing section or not -->
			<xsl:choose>
				<xsl:when test="$expanding = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img src="{$MINUS_IMAGE}" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$PLUS_IMAGE}" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
						<span class="section_Ftitle"><xsl:value-of select="$section_Ftitle"/></span>
						<span class="section_path"> (<xsl:value-of select="$section_path"/>)</span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<span class="plus_minus">-</span>
					<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
					<span class="section_Ftitle"><xsl:value-of select="$section_Ftitle"/></span>
					</xsl:otherwise>
			</xsl:choose>
			
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<span class="hiddenXML"><xsl:text disable-output-escaping="yes"><![CDATA[<!-- ]]></xsl:text>
					<xsl:choose>
						<!-- valid local name? -->
						<xsl:when test="local-name()">
							<xsl:element name="{local-name()}">
								<xsl:apply-templates/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<!-- error, missing local-name, display message -->
							<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
							<h2 class="error">Transform error: Local name is blank</h2>
							<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
							<p class="error">name() = <xsl:value-of select="name()"/></p>
							<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				<xsl:text disable-output-escaping="yes"><![CDATA[ -->]]></xsl:text></span>
			</xsl:element>
		</div>
		<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
	</xsl:template>

	<!-- TEMPLATE: for repeating section or node -->
	<xsl:template name="repeat_section">
		<xsl:param name="section_list" />
		<xsl:param name="section_title"/>
		<xsl:param name="section_path"/>
		<xsl:param name="optionality"/>
		<xsl:param name="expanding"/>
		<xsl:param name="expanded"/>
		<xsl:param name="add_child"/>
		
		<!-- at beginning of repeating section -->
		<xsl:if test="count(preceding-sibling::*[name(current()) = name()]) = 0"> 
			<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		</xsl:if>
			
		<!-- display each repeating section -->
		<div class="folder" section_path="{$section_path}" id="{$section_path}"
			 onmouseenter="ShowBox({$SELF_REF})" onmouseleave="HideBox({$SELF_REF})">
			<!-- expanding/collapsing section or not -->
			<xsl:choose>
				<xsl:when test="$expanding = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img src="{$MINUS_IMAGE}" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$PLUS_IMAGE}" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						<span class="section_title"><xsl:value-of select="$section_title"/></span>
						<span class="section_path">
							<xsl:text> (</xsl:text>
							<xsl:choose>
								<xsl:when test="contains($section_path,'/')">
									<xsl:call-template name="lastIndexOf">
										<xsl:with-param name="string">
											<xsl:value-of select="$section_path"/>
										</xsl:with-param>
										<xsl:with-param name="char">/</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$section_path"/>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>)</xsl:text>
						</span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<span class="section_title"><xsl:value-of select="$section_title"/></span>
					<span class="section_path"> (<xsl:value-of select="$section_path"/>)</span>
				</xsl:otherwise>
			</xsl:choose>
			
			<!-- add popup menu for repeat section options -->
			<span onclick="ShowMenu({$SELF_REF})">
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
			
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<span class="hiddenXML">
					<xsl:text disable-output-escaping="yes"><![CDATA[<!-- ]]></xsl:text>
					<xsl:choose>
							<!-- valid local name? -->
							<xsl:when test="local-name()">
								<xsl:element name="{local-name()}">
									<xsl:apply-templates/>
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<!-- error, missing local-name, display message -->
								<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
								<h2 class="error">Transform error: Local name is blank</h2>
								<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
								<p class="error">name() = <xsl:value-of select="name()"/></p>
								<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					<xsl:text disable-output-escaping="yes"><![CDATA[ -->]]></xsl:text>
				</span>
			</xsl:element>
		</div>
	
		<!-- at end of repeating section -->
		<xsl:if test="count(following-sibling::*[name(current()) = name()]) = 0"> 
			<!-- add final add section input button? -->
			<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
		</xsl:if>
	</xsl:template>	

	<!-- TEMPLATE: for repeating MANDATORY section or node -->
	<xsl:template name="repeat_Msection">
		<xsl:param name="section_list" />
		<xsl:param name="section_title"/>
		<xsl:param name="section_Mtitle"/>
		<xsl:param name="section_path"/>
		<xsl:param name="optionality"/>
		<xsl:param name="expanding"/>
		<xsl:param name="expanded"/>
		<xsl:param name="add_child"/>
		
		<!-- at beginning of repeating section -->
		<xsl:if test="count(preceding-sibling::*[name(current()) = name()]) = 0"> 
			<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
		</xsl:if>
			
		<!-- display each repeating section -->
		<div class="folder" section_path="{$section_path}" id="{$section_path}"
			 onmouseenter="ShowBox({$SELF_REF})" onmouseleave="HideBox({$SELF_REF})">
			<!-- expanding/collapsing section or not -->
			<xsl:choose>
				<xsl:when test="$expanding = 'true'"> 
					<a href="javascript:void(0)" onclick="expandNode(this);return false" class="expand" title="Expand/collapse this section.">
						<!-- start expanded or collapsed? -->
						<xsl:choose>
							<xsl:when test="$expanded = 'true'">
								<img src="{$MINUS_IMAGE}" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$PLUS_IMAGE}" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
						<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
						<span class="section_Mtitle"><xsl:value-of select="$section_Mtitle"/></span>
						<span class="section_path">
							<xsl:text> (</xsl:text>
							<xsl:choose>
								<xsl:when test="contains($section_path,'/')">
									<xsl:call-template name="lastIndexOf">
										<xsl:with-param name="string">
											<xsl:value-of select="$section_path"/>
										</xsl:with-param>
										<xsl:with-param name="char">/</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$section_path"/>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:text>)</xsl:text>
						</span>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<!-- <span class="section_title"><xsl:value-of select="$section_title"/></span> -->
					<span class="section_Mtitle"><xsl:value-of select="$section_Mtitle"/></span>
					<span class="section_path"> (<xsl:value-of select="$section_path"/>)</span>
				</xsl:otherwise>
			</xsl:choose>
			
			<!-- add popup menu for repeat section options -->
			<span onclick="ShowMenu({$SELF_REF})">
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
			
			<!-- add subelements of section within a "content" DIV-->
			<xsl:element name="div">
				<xsl:attribute name="class">content</xsl:attribute>
				<!-- start collapsed? -->
				<xsl:if test="$expanded != 'true'">
					<xsl:attribute name="style">display:none;</xsl:attribute>
				</xsl:if>
				<span class="hiddenXML">
					<xsl:text disable-output-escaping="yes"><![CDATA[<!-- ]]></xsl:text>
					<xsl:choose>
							<!-- valid local name? -->
							<xsl:when test="local-name()">
								<xsl:element name="{local-name()}">
									<xsl:apply-templates/>
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<!-- error, missing local-name, display message -->
								<xsl:text disable-output-escaping="yes"><![CDATA[ --></span>]]></xsl:text>
								<h2 class="error">Transform error: Local name is blank</h2>
								<p class="error">local-name() = <xsl:value-of select="local-name()" /></p>
								<p class="error">name() = <xsl:value-of select="name()"/></p>
								<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					<xsl:text disable-output-escaping="yes"><![CDATA[ -->]]></xsl:text>
				</span>
			</xsl:element>
		</div>
	
		<!-- at end of repeating section -->
		<xsl:if test="count(following-sibling::*[name(current()) = name()]) = 0"> 
			<!-- add final add section input button? -->
			<xsl:text disable-output-escaping="yes"><![CDATA[<span class="hiddenXML"><!-- ]]></xsl:text>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
