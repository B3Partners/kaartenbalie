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
	<xsl:include href="Add_MNP_Metadata_Beheerder_Edit_Templates.xsl"></xsl:include>
	<xsl:variable name="NPSThemes" select="''"/>
	<xsl:variable name="NPSPlaces" select="''"/>

	<!--root template-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

<!--TEMPLATE: check metadata--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="metadata">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(dataset_description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_dataset_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="dataset_description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(ESRI_NL)"><!--Child element missing, create it-->
					<xsl:call-template name="add_ESRI_NL"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="ESRI_NL"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(NPS_Info)"><!--Child element missing, create it-->
					<xsl:call-template name="add_NPS_Info"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="NPS_Info"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="idinfo">
				<!-- Copy it -->
				<xsl:apply-templates select="idinfo"></xsl:apply-templates>
			</xsl:if>
						<xsl:if test="dataqual">
				<!-- Copy it -->
				<xsl:apply-templates select="dataqual"></xsl:apply-templates>
			</xsl:if>
			<xsl:if test="spdoinfo">
				<!-- Copy it -->
				<xsl:apply-templates select="spdoinfo"></xsl:apply-templates>
			</xsl:if>
			<xsl:if test="spref">
				<!-- Copy it -->
				<xsl:apply-templates select="spref"></xsl:apply-templates>
			</xsl:if>
			<xsl:if test="eainfo">
				<!-- Copy it -->
				<xsl:apply-templates select="eainfo"></xsl:apply-templates>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="not(distinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_distinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="distinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metainfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metainfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metainfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::NPS_Info) and not(self::idinfo) and not(self::dataqual) and not(self::spdoinfo) and not(self::spref) and not(self::eainfo) and not(self::distinfo) and not(self::metainfo) and not(self::dataset_description) and not(self::ESRI_NL)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check NPS_Info--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="NPS_Info">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(MetaPurp)"><!--Child element missing, create it-->
					<xsl:call-template name="add_MetaPurp"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="MetaPurp"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(NPS_Unit)"><!--Child element missing, create it-->
					<xsl:call-template name="add_NPS_Unit"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="NPS_Unit"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(DatStore)"><!--Child element missing, create it-->
					<xsl:call-template name="add_DatStore"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="DatStore"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::MetaPurp) and not(self::NPS_Unit) and not(self::DatStore)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check NPS_Unit--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="NPS_Unit">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(UnitCode)"><!--Child element missing, create it-->
					<xsl:call-template name="add_UnitCode"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="UnitCode"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(UnitType)"><!--Child element missing, create it-->
					<xsl:call-template name="add_UnitType"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="UnitType"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(Org_Code)"><!--Child element missing, create it-->
					<xsl:call-template name="add_Org_Code"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="Org_Code"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::UnitCode) and not(self::UnitType) and not(self::Org_Code)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check DatStore--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="DatStore">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(Category)"><!--Child element missing, create it-->
					<xsl:call-template name="add_Category"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="Category"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(DataSite)"><!--Child element missing, create it-->
					<xsl:call-template name="add_DataSite"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="DataSite"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(DSteward)"><!--Child element missing, create it-->
					<xsl:call-template name="add_DSteward"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="DSteward"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::Category) and not(self::DataSite) and not(self::DSteward)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check DSteward--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="DSteward">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check cntinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="(NPS_Info | idinfo | distinfo | metainfo)//cntinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntperp) and not(cntorgp)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntperp"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntperp | cntorgp"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntpos)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntpos"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntpos"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntaddr)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntaddr"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntaddr"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntvoice)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntvoice"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntvoice"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cnttdd)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cnttdd"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cnttdd"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntfax)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntfax"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntfax"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntemail)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntemail"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntemail"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(hours)"><!--Child element missing, create it-->
					<xsl:call-template name="add_hours"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="hours"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntinst)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntinst"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntinst"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntperp) and not(self::cntorgp) and not(self::cntpos) and not(self::cntaddr) and not(self::cntvoice) and not(self::cnttdd) and not(self::cntfax) and not(self::cntemail) and not(self::hours) and not(self::cntinst)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check cntperp--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="cntperp">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntper)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntper"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntper"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntorg)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntorg"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntorg"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntper) and not(self::cntorg)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check cntorgp--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="cntorgp">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntorg)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntorg"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntorg"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(cntper)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntper"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntper"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntper) and not(self::cntorg)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
	<!--TEMPLATE: check cntaddr--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="cntaddr">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(addrtype)"><!--Child element missing, create it-->
					<xsl:call-template name="add_addrtype"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="addrtype"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(address)"><!--Child element missing, create it-->
					<xsl:call-template name="add_address"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="address"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(city)"><!--Child element missing, create it-->
					<xsl:call-template name="add_city"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="city"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(state)"><!--Child element missing, create it-->
					<xsl:call-template name="add_state"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="state"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(postal)"><!--Child element missing, create it-->
					<xsl:call-template name="add_postal"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="postal"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(country)"><!--Child element missing, create it-->
					<xsl:call-template name="add_country"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="country"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::addrtype) and not(self::address) and not(self::city) and not(self::state) and not(self::postal) and not(self::country)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check idinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="idinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(natvform)"><!--Child element missing, create it-->
					<xsl:call-template name="add_natvform"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="natvform"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(citation)"><!--Child element missing, create it-->
					<xsl:call-template name="add_citation"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="citation"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(descript)"><!--Child element missing, create it-->
					<xsl:call-template name="add_descript"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="descript"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(timeperd)"><!--Child element missing, create it-->
					<xsl:call-template name="add_timeperd"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="timeperd"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(status)"><!--Child element missing, create it-->
					<xsl:call-template name="add_status"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="status"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(spdom)"><!--Child element missing, create it-->
					<xsl:call-template name="add_spdom"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="spdom"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(keywords)"><!--Child element missing, create it-->
					<xsl:call-template name="add_keywords"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="keywords"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(accconst)"><!--Child element missing, create it-->
					<xsl:call-template name="add_accconst"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="accconst"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(useconst)"><!--Child element missing, create it-->
					<xsl:call-template name="add_useconst"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="useconst"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(ptcontac)"><!--Child element missing, create it-->
					<xsl:call-template name="add_ptcontac"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="ptcontac"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(browse)"><!--Child element missing, create it-->
					<xsl:call-template name="add_browse"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="browse"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(crossref)"><!--Child element missing, create it-->
					<xsl:call-template name="add_crossref"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="crossref"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::citation) and not(self::descript) and not(self::timeperd) and not(self::status) and not(self::spdom) and not(self::keywords) and not(self::accconst) and not(self::useconst) and not(self::ptcontac) and not(self::browse) and not(self::crossref) and not(self::natvform)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check citation--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="citation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(citeinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_citeinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="citeinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::citeinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check citeinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="(idinfo | distinfo | metainfo)//citeinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(origin)"><!--Child element missing, create it-->
					<xsl:call-template name="add_origin"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="origin"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(pubdate)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pubdate"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pubdate"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(pubtime)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pubtime"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pubtime"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(title)"><!--Child element missing, create it-->
					<xsl:call-template name="add_title"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="title"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(edition)"><!--Child element missing, create it-->
					<xsl:call-template name="add_edition"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="edition"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(serinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_serinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="serinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(pubinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pubinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pubinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(othercit)"><!--Child element missing, create it-->
					<xsl:call-template name="add_othercit"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="othercit"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(onlink)"><!--Child element missing, create it-->
					<xsl:call-template name="add_onlink"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="onlink"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(BibKeyID)"><!--Child element missing, create it-->
					<xsl:call-template name="add_BibKeyID"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="BibKeyID"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::origin) and not(self::pubdate) and not(self::pubtime) and not(self::title) and not(self::edition) and not(self::serinfo) and not(self::pubinfo) and not(self::othercit) and not(self::onlink) and not(self::BibKeyID)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check serinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="serinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(sername)"><!--Child element missing, create it-->
					<xsl:call-template name="add_sername"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="sername"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(issue)"><!--Child element missing, create it-->
					<xsl:call-template name="add_issue"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="issue"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::sername) and not(self::issue)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check pubinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="pubinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(pubplace)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pubplace"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pubplace"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(publish)"><!--Child element missing, create it-->
					<xsl:call-template name="add_publish"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="publish"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::pubplace) and not(self::publish)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check descript--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="descript">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(abstract)"><!--Child element missing, create it-->
					<xsl:call-template name="add_abstract"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="abstract"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(purpose)"><!--Child element missing, create it-->
					<xsl:call-template name="add_purpose"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="purpose"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(supplinf)"><!--Child element missing, create it-->
					<xsl:call-template name="add_supplinf"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="supplinf"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::abstract) and not(self::purpose) and not(self::supplinf)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check timeperd--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="timeperd">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(timeinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_timeinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="timeinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(current)"><!--Child element missing, create it-->
					<xsl:call-template name="add_current"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="current"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::timeinfo) and not(self::current)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check timeinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="timeinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(sngdate)"><!--Child element missing, create it-->
					<xsl:call-template name="add_sngdate"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="sngdate"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(rngdates)"><!--Child element missing, create it-->
					<xsl:call-template name="add_rngdates"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="rngdates"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(mdattim)"><!--Child element missing, create it-->
					<xsl:call-template name="add_mdattim"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="mdattim"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>			<!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::sngdate) and not(self::rngdates) and not(self::mdattim)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check sngdate--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="sngdate">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(caldate)"><!--Child element missing, create it-->
					<xsl:call-template name="add_caldate"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="caldate"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(time)"><!--Child element missing, create it-->
					<xsl:call-template name="add_time"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="time"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::caldate) and not(self::time)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check status--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="status">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(progress)"><!--Child element missing, create it-->
					<xsl:call-template name="add_progress"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="progress"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(update)"><!--Child element missing, create it-->
					<xsl:call-template name="add_update"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="update"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::progress) and not(self::update)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check spdom--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="spdom">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(bounding)"><!--Child element missing, create it-->
					<xsl:call-template name="add_bounding"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="bounding"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::bounding)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check bounding--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="bounding">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(westbc)"><!--Child element missing, create it-->
					<xsl:call-template name="add_westbc"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="westbc"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(eastbc)"><!--Child element missing, create it-->
					<xsl:call-template name="add_eastbc"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="eastbc"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(northbc)"><!--Child element missing, create it-->
					<xsl:call-template name="add_northbc"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="northbc"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(southbc)"><!--Child element missing, create it-->
					<xsl:call-template name="add_southbc"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="southbc"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::westbc) and not(self::eastbc) and not(self::northbc) and not(self::southbc)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check keywords--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="keywords">
		<xsl:copy>	
			<!-- Order of separate choose blocks is significant.  If combined together, missing theme sections -->
			<!-- do not get added correctly to the resulting transform.  -->
			<xsl:choose>
				<xsl:when test="not(theme)"><!--Child element missing, create it-->
					<xsl:call-template name="add_theme"></xsl:call-template>
				</xsl:when>								
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="theme"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Direct evaluation of all keyword thesaurus nodes is not comprehensive, so have -->
			<!-- to resort to this kludge. -->
			<xsl:variable name="NPSThemes">			
			   <xsl:for-each select=".//themekt">
			      <xsl:value-of select="."/>			   
		  	   </xsl:for-each>
		  	</xsl:variable>   
			<!-- Adds NPS theme keywords. -->
			<xsl:choose>
				<xsl:when test="not(contains($NPSThemes,'National Park Service Theme Category Thesaurus')) and $NPSThemes!=''">
					<xsl:call-template name="add_cat_theme"></xsl:call-template>
				</xsl:when>				
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(contains($NPSThemes,'ISO 19115 Topic Category')) and $NPSThemes!=''">
					<xsl:call-template name="add_iso_theme"></xsl:call-template>
				</xsl:when>			
			</xsl:choose>		
			<!-- Adds blank theme keyword section if XML has NPS theme keywords without other theme keywords. -->
			<xsl:choose>			
				<xsl:when test="contains($NPSThemes,'National Park Service Theme Category Thesaurus') and contains($NPSThemes,'ISO 19115 Topic Category') and count(theme)=2">
					<xsl:call-template name="add_simple_theme"></xsl:call-template>
				</xsl:when>				
			</xsl:choose>
			<xsl:choose>			
				<xsl:when test="(contains($NPSThemes,'National Park Service Theme Category Thesaurus') or contains($NPSThemes,'ISO 19115 Topic Category')) and count(theme)=1">
					<xsl:call-template name="add_simple_theme"></xsl:call-template>
				</xsl:when>				
			</xsl:choose>			
			<!-- Order of separate choose blocks is significant.  If combined together, missing place sections -->
			<!-- do not get added correctly to the resulting transform.  -->			
			<xsl:choose>
				<xsl:when test="not(place)"><!--Child element missing, create it-->
					<xsl:call-template name="add_place"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="place"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Direct evaluation of all keyword thesaurus nodes is not comprehensive, so have -->
			<!-- to resort to this kludge. -->
			<xsl:variable name="NPSPlaces">			
			   <xsl:for-each select=".//placekt">
			      <xsl:value-of select="."/>			   
		  	   </xsl:for-each>
		  	</xsl:variable>			
			<!-- Adds NPS place keywords. -->
			<xsl:choose>			
				<xsl:when test="not(contains($NPSPlaces,'National Park System Unit Name Thesaurus')) and $NPSPlaces!=''">
					<xsl:call-template name="add_name_place"></xsl:call-template>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(contains($NPSPlaces,'National Park System Unit Code Thesaurus')) and $NPSPlaces!=''">
					<xsl:call-template name="add_code_place"></xsl:call-template>
				</xsl:when>				
			</xsl:choose>
			<!-- Adds blank place keyword section if XML has NPS place keywords without other place keywords. -->
			<xsl:choose>			
				<xsl:when test="contains($NPSPlaces,'National Park System Unit Name Thesaurus') and contains($NPSPlaces,'National Park System Unit Code Thesaurus') and count(place)=2">
					<xsl:call-template name="add_simple_place"></xsl:call-template>
				</xsl:when>				
			</xsl:choose>
			<xsl:choose>			
				<xsl:when test="(contains($NPSPlaces,'National Park System Unit Name Thesaurus') or contains($NPSPlaces,'National Park System Unit Code Thesaurus')) and count(place)=1">
					<xsl:call-template name="add_simple_place"></xsl:call-template>
				</xsl:when>				
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::theme) and not(self::place)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check theme--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="theme">
		<xsl:variable name="NPSThemes" select="''"/>
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(themekt)"><!--Child element missing, create it-->
					<xsl:call-template name="add_themekt"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="themekt"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(themekey)"><!--Child element missing, create it-->
					<xsl:call-template name="add_themekey"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="themekey"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::themekt) and not(self::themekey)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check place--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="place">
		<xsl:variable name="NPSPlaces" select="''"/>
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(placekt)"><!--Child element missing, create it-->
					<xsl:call-template name="add_placekt"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="placekt"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(placekey)"><!--Child element missing, create it-->
					<xsl:call-template name="add_placekey"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="placekey"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::placekt) and not(self::placekey)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check ptcontac--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="ptcontac">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check browse--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="browse">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(browsen)"><!--Child element missing, create it-->
					<xsl:call-template name="add_browsen"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="browsen"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(browsed)"><!--Child element missing, create it-->
					<xsl:call-template name="add_browsed"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="browsed"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(browset)"><!--Child element missing, create it-->
					<xsl:call-template name="add_browset"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="browset"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::browsen) and not(self::browsed) and not(self::browset)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check crossref--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="crossref">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(citeinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_citeinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="citeinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::citeinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check distinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->	
	<xsl:template match="distinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(distrib)"><!--Child element missing, create it-->
					<xsl:call-template name="add_distrib"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="distrib"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(distliab)"><!--Child element missing, create it-->
					<xsl:call-template name="add_distliab"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="distliab"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(stdorder)"><!--Child element missing, create it-->
					<xsl:call-template name="add_stdorder"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="stdorder"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::distrib) and not(self::distliab) and not(self::stdorder)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check distrib--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="distrib">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check stdorder--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="stdorder">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(digform)"><!--Child element missing, create it-->
					<xsl:call-template name="add_digform"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="digform"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(fees)"><!--Child element missing, create it-->
					<xsl:call-template name="add_fees"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="fees"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::digform) and not(self::fees)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check digform--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="digform">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(digtinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_digtinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="digtinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(digtopt)"><!--Child element missing, create it-->
					<xsl:call-template name="add_digtopt"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="digtopt"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::digtinfo) and not(self::digtopt)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check digtinfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="digtinfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(formname)"><!--Child element missing, create it-->
					<xsl:call-template name="add_formname"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="formname"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(transize)"><!--Child element missing, create it-->
					<xsl:call-template name="add_transize"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="transize"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(dssize)"><!--Child element missing, create it-->
					<xsl:call-template name="add_dssize"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="dssize"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::formname) and not(self::transize) and not(self::dssize)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check digtopt--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="digtopt">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(onlinopt)"><!--Child element missing, create it-->
					<xsl:call-template name="add_onlinopt"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="onlinopt"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(offoptn)"><!--Child element missing, create it-->
					<xsl:call-template name="add_offoptn"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="offoptn"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::onlinopt) and not(self::offoptn)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check onlinopt--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="onlinopt">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(computer)"><!--Child element missing, create it-->
					<xsl:call-template name="add_computer"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="computer"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(accinstr)"><!--Child element missing, create it-->
					<xsl:call-template name="add_accinstr"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="accinstr"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(oncomp)"><!--Child element missing, create it-->
					<xsl:call-template name="add_oncomp"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="oncomp"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::computer) and not(self::accinstr) and not(self::oncomp)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check computer--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="computer">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(networka)"><!--Child element missing, create it-->
					<xsl:call-template name="add_networka"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="networka"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::networka)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check networka--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="networka">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(networkr)"><!--Child element missing, create it-->
					<xsl:call-template name="add_networkr"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="networkr"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::networkr)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check offoptn--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="offoptn">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(offmedia)"><!--Child element missing, create it-->
					<xsl:call-template name="add_offmedia"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="offmedia"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(reccap)"><!--Child element missing, create it-->
					<xsl:call-template name="add_reccap"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="reccap"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(recfmt)"><!--Child element missing, create it-->
					<xsl:call-template name="add_recfmt"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="recfmt"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(compat)"><!--Child element missing, create it-->
					<xsl:call-template name="add_compat"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="compat"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::offmedia) and not(self::reccap) and not(self::recfmt) and not(self::compat)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check reccap--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="offoptn//reccap">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(recden)"><!--Child element missing, create it-->
					<xsl:call-template name="add_recden"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="recden"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(recdenu)"><!--Child element missing, create it-->
					<xsl:call-template name="add_recdenu"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="recdenu"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::recden) and not(self::recdenu)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check metainfo--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="metainfo">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(metd)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metd"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metd"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metrd)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metrd"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metrd"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>			
			<xsl:choose>
				<xsl:when test="not(metc)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metc"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metc"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metstdn)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metstdn"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metstdn"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metstdv)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metstdv"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metstdv"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metextns)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metextns"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metextns"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::metd) and not(self::metrd) and not(self::metc) and not(self::metstdn) and not(self::metstdv) and not(self::metextns)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check metc--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="metc">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(cntinfo)"><!--Child element missing, create it-->
					<xsl:call-template name="add_cntinfo"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="cntinfo"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::cntinfo)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template><!--TEMPLATE: check metextns--><!--Adds missing child elements and sections of this node--><!--in the proper place and order.-->
	<xsl:template match="metainfo//metextns">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(onlink)"><!--Child element missing, create it-->
					<xsl:call-template name="add_onlink"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="onlink"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metprof)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metprof"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metprof"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose><!--Copy everthing else under this node-->
			<xsl:apply-templates select="@*|node()[not(self::onlink) and not(self::metprof)]"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template>


	<!--=====================================================--><!--Default template: copy all nodes, attributes verbatim--><!--=====================================================-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"></xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
	
	
	<xsl:template match="dataset_description">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(identification)"><!--Child element missing, create it-->
					<xsl:call-template name="add_identification"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="identification"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(metadata_reference)"><!--Child element missing, create it-->
					<xsl:call-template name="add_metadata_reference"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="metadata_reference"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(overview)"><!--Child element missing, create it-->
					<xsl:call-template name="add_overview"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="overview"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(spatial_reference_system)"><!--Child element missing, create it-->
					<xsl:call-template name="add_spatial_reference_system"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="spatial_reference_system"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(administrative_metadata)"><!--Child element missing, create it-->
					<xsl:call-template name="add_administrative_metadata"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="administrative_metadata"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(geokey_database)"><!--Child element missing, create it-->
					<xsl:call-template name="add_geokey_database"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="geokey_database"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(quality_element)"><!--Child element missing, create it-->
					<xsl:call-template name="add_quality_element"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="quality_element"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(planar_extent)"><!--Child element missing, create it-->
					<xsl:call-template name="add_planar_extent"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="planar_extent"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(vertical_extent)"><!--Child element missing, create it-->
					<xsl:call-template name="add_vertical_extent"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="vertical_extent"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(temporal_extent)"><!--Child element missing, create it-->
					<xsl:call-template name="add_temporal_extent"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="temporal_extent"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(geokey_dataset)"><!--Child element missing, create it-->
					<xsl:call-template name="add_geokey_dataset"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="geokey_dataset"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(data_definition)"><!--Child element missing, create it-->
					<xsl:call-template name="add_data_definition"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="data_definition"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::identification) and not(self::metadata_reference) and not(self::overview) and not(self::spatial_reference_system) and not(self::administrative_metadata) and not(self::geokey_database) and not(self::quality_element) and not(self::planar_extent) and not(self::vertical_extent) and not(self::temporal_extent) and not(self::geokey_dataset) and not(self::data_definition)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="identification">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(alternative_title)"><!--Child element missing, create it-->
					<xsl:call-template name="add_alternative_title"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="alternative_title"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(dataset_title)"><!--Child element missing, create it-->
					<xsl:call-template name="add_dataset_title"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="dataset_title"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(abbreviated_title)"><!--Child element missing, create it-->
					<xsl:call-template name="add_abbreviated_title"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="abbreviated_title"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(version)"><!--Child element missing, create it-->
					<xsl:call-template name="add_version"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="version"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::alternative_title) and not(self::dataset_title) and not(self::abbreviated_title) and not(self::version)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="spatial_reference_system">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(direct_positioning_system)"><!--Child element missing, create it-->
					<xsl:call-template name="add_direct_positioning_system"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="direct_positioning_system"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(indirect_positioning_system)"><!--Child element missing, create it-->
					<xsl:call-template name="add_indirect_positioning_system"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="indirect_positioning_system"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::direct_positioning_system) and not(self::indirect_positioning_system)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="direct_positioning_system">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(identifier)"><!--Child element missing, create it-->
					<xsl:call-template name="add_identifier"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="identifier"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(geodetic_datum_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_geodetic_datum_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="geodetic_datum_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(map_projection_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_map_projection_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="map_projection_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(geodetic_ellipsoid_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_geodetic_ellipsoid_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="geodetic_ellipsoid_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(vertical_datum_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_vertical_datum_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="vertical_datum_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::identifier) and not(self::geodetic_datum_name) and not(self::map_projection_name) and not(self::geodetic_ellipsoid_name) and not(self::name) and not(self::vertical_datum_name)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="indirect_positioning_system">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(location_class_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_location_class_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="location_class_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(reference_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_reference_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="reference_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::name) and not(self::location_class_name) and not(self::reference_date)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="metadata_reference">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(entry_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_entry_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="entry_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(last_update_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_last_update_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="last_update_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(future_review_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_future_review_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="future_review_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::entry_date) and not(self::last_update_date) and not(self::future_review_date)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="administrative_metadata">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(distribution)"><!--Child element missing, create it-->
					<xsl:call-template name="add_distribution"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="distribution"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(point_of_contacts_role)"><!--Child element missing, create it-->
					<xsl:call-template name="add_point_of_contacts_role"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="point_of_contacts_role"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(organisation_role)"><!--Child element missing, create it-->
					<xsl:call-template name="add_organisation_role"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="organisation_role"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::distribution) and not(self::point_of_contacts_role) and not(self::organisation_role)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="distribution">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(on_line_access)"><!--Child element missing, create it-->
					<xsl:call-template name="add_on_line_access"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="on_line_access"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(restrictions_on_use)"><!--Child element missing, create it-->
					<xsl:call-template name="add_restrictions_on_use"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="restrictions_on_use"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(ESRI_NL)"><!--Child element missing, create it-->
					<xsl:call-template name="add_distribution_ESRI_NL"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="ESRI_NL"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(copyright_owners)"><!--Child element missing, create it-->
					<xsl:call-template name="add_copyright_owners"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="copyright_owners"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(unit_of_distribution)"><!--Child element missing, create it-->
					<xsl:call-template name="add_unit_of_distribution"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="unit_of_distribution"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::on_line_access) and not(self::restrictions_on_use) and not(self::ESRI_NL) and not(self::copyright_owners) and not(self::unit_of_distribution)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="on_line_access">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(url)"><!--Child element missing, create it-->
					<xsl:call-template name="add_url"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="url"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::url)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="distribution/ESRI_NL">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(restrictions_on_use_link)"><!--Child element missing, create it-->
					<xsl:call-template name="add_restrictions_on_use_link"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="restrictions_on_use_link"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::restrictions_on_use_link)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="copyright_owners">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(organisation)"><!--Child element missing, create it-->
					<xsl:call-template name="add_organisation"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="organisation"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::organisation)]"/>
		</xsl:copy>
	</xsl:template>
	
	<!-- EvdP: Alleen voor copyright_owners of ook voor andere organisations?  -> copyright_owners krijgt nu volledige organisatie-->
	<!--<xsl:template match="organisation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(name)">
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::name)]"/>
		</xsl:copy>
	</xsl:template>
	-->
			
	<xsl:template match="point_of_contacts_role">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(point_of_contact)"><!--Child element missing, create it-->
					<xsl:call-template name="add_point_of_contact"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="point_of_contact"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::description) and not(self::point_of_contact)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="point_of_contact">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(point_of_contact_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_point_of_contact_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="point_of_contact_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(phone)"><!--Child element missing, create it-->
					<xsl:call-template name="add_phone"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="phone"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(fax)"><!--Child element missing, create it-->
					<xsl:call-template name="add_fax"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="fax"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(email)"><!--Child element missing, create it-->
					<xsl:call-template name="add_email"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="email"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(www_home_page)"><!--Child element missing, create it-->
					<xsl:call-template name="add_www_home_page"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="www_home_page"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(point_of_contact_address)"><!--Child element missing, create it-->
					<xsl:call-template name="add_point_of_contact_address"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="point_of_contact_address"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::point_of_contact_name) and not(self::phone) and not(self::fax) and not(self::email) and not(self::www_home_page) and not(self::point_of_contact_address)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="organisation_role">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(organisation)"><!--Child element missing, create it-->
					<xsl:call-template name="add_organisation"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="organisation"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::description) and not(self::organisation)]"/>
		</xsl:copy>
	</xsl:template>
			
	<xsl:template match="organisation">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(abbreviated_organisation_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_abbreviated_organisation_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="abbreviated_organisation_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(phone)"><!--Child element missing, create it-->
					<xsl:call-template name="add_phone"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="phone"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(fax)"><!--Child element missing, create it-->
					<xsl:call-template name="add_fax"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="fax"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(email)"><!--Child element missing, create it-->
					<xsl:call-template name="add_email"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="email"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(www_home_page)"><!--Child element missing, create it-->
					<xsl:call-template name="add_www_home_page"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="www_home_page"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(organisation_address)"><!--Child element missing, create it-->
					<xsl:call-template name="add_organisation_address"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="organisation_address"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::abbreviated_organisation_name) and not(self::name) and not(self::phone) and not(self::fax) and not(self::email) and not(self::www_home_page) and not(self::organisation_address)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="organisation_address">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(street)"><!--Child element missing, create it-->
					<xsl:call-template name="add_street"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="street"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(zip)"><!--Child element missing, create it-->
					<xsl:call-template name="add_zip"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="zip"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(city)"><!--Child element missing, create it-->
					<xsl:call-template name="add_city"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="city"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(country)"><!--Child element missing, create it-->
					<xsl:call-template name="add_country"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="country"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(visiting_address)"><!--Child element missing, create it-->
					<xsl:call-template name="add_visiting_address"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="visiting_address"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::street) and not(self::zip) and not(self::city) and not(self::country) and not(self::visiting_address)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="point_of_contact_address">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(street)"><!--Child element missing, create it-->
					<xsl:call-template name="add_street"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="street"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(zip)"><!--Child element missing, create it-->
					<xsl:call-template name="add_zip"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="zip"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(city)"><!--Child element missing, create it-->
					<xsl:call-template name="add_city"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="city"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(country)"><!--Child element missing, create it-->
					<xsl:call-template name="add_country"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="country"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(visiting_address)"><!--Child element missing, create it-->
					<xsl:call-template name="add_visiting_address"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="visiting_address"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::street) and not(self::zip) and not(self::city) and not(self::country) and not(self::visiting_address)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="visiting_address">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(street)"><!--Child element missing, create it-->
					<xsl:call-template name="add_street"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="street"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(zip)"><!--Child element missing, create it-->
					<xsl:call-template name="add_zip"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="zip"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(city)"><!--Child element missing, create it-->
					<xsl:call-template name="add_city"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="city"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(country)"><!--Child element missing, create it-->
					<xsl:call-template name="add_country"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="country"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::street) and not(self::zip) and not(self::city) and not(self::country)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="geokey_database">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(id)"><!--Child element missing, create it-->
					<xsl:call-template name="add_id"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="id"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::description) and not(self::id)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="overview">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(summary)"><!--Child element missing, create it-->
					<xsl:call-template name="add_summary"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="summary"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(producer_organisation_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_producer_organisation_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="producer_organisation_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(purpose_of_production)"><!--Child element missing, create it-->
					<xsl:call-template name="add_purpose_of_production"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="purpose_of_production"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(intented_application_scale)"><!--Child element missing, create it-->
					<xsl:call-template name="add_intented_application_scale"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="intented_application_scale"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(spatial_schema_type)"><!--Child element missing, create it-->
					<xsl:call-template name="add_spatial_schema_type"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="spatial_schema_type"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(dataset_language)"><!--Child element missing, create it-->
					<xsl:call-template name="add_dataset_language"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="dataset_language"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(potential_usage)"><!--Child element missing, create it-->
					<xsl:call-template name="add_potential_usage"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="potential_usage"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::summary) and not(self::producer_organisation_name) and not(self::purpose_of_production) and not(self::intented_application_scale) and not(self::spatial_schema_type) and not(self::dataset_language) and not(self::potential_usage)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="spatial_schema_type">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(spatial_schema_type_id)"><!--Child element missing, create it-->
					<xsl:call-template name="add_spatial_schema_type_id"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="spatial_schema_type_id"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::spatial_schema_type_id)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="dataset_language">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(language)"><!--Child element missing, create it-->
					<xsl:call-template name="add_language"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="language"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(characterset)"><!--Child element missing, create it-->
					<xsl:call-template name="add_characterset"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="characterset"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::language) and not(self::characterset)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="quality_element">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(lineage)"><!--Child element missing, create it-->
					<xsl:call-template name="add_lineage"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="lineage"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(quality_text)"><!--Child element missing, create it-->
					<xsl:call-template name="add_quality_text"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="quality_text"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(quality_parameters)"><!--Child element missing, create it-->
					<xsl:call-template name="add_quality_parameters"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="quality_parameters"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::lineage) and not(self::quality_text) and not(self::quality_parameters)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="lineage">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(production)"><!--Child element missing, create it-->
					<xsl:call-template name="add_production"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="production"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(source_and_processes)"><!--Child element missing, create it-->
					<xsl:call-template name="add_source_and_processes"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="source_and_processes"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::production) and not(self::source_and_processes)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="production">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(producer_organisation)"><!--Child element missing, create it-->
					<xsl:call-template name="add_producer_organisation"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="producer_organisation"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(method)"><!--Child element missing, create it-->
					<xsl:call-template name="add_method"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="method"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(date_of_production)"><!--Child element missing, create it-->
					<xsl:call-template name="add_date_of_production"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="date_of_production"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::producer_organisation) and not(self::method) and not(self::date_of_production)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="date_of_production">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::date)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="source_and_processes">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(source_history)"><!--Child element missing, create it-->
					<xsl:call-template name="add_source_history"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="source_history"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::source_history)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="source_history">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(organisation)"><!--Child element missing, create it-->
					<xsl:call-template name="add_source_history_organisation"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="organisation"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(method)"><!--Child element missing, create it-->
					<xsl:call-template name="add_method"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="method"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::organisation) and not(self::method)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="quality_parameters">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(quality_indicator_set)"><!--Child element missing, create it-->
					<xsl:call-template name="add_quality_indicator_set"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="quality_indicator_set"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::name) and not(self::quality_indicator_set)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="quality_indicator_set">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(quality_measure_set)"><!--Child element missing, create it-->
					<xsl:call-template name="add_quality_measure_set"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="quality_measure_set"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::name) and not(self::quality_measure_set)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="quality_measure_set">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(measure_result)"><!--Child element missing, create it-->
					<xsl:call-template name="add_measure_result"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="measure_result"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::name) and not(self::description) and not(self::measure_result)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="measure_result">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(value)"><!--Child element missing, create it-->
					<xsl:call-template name="add_value"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="value"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::value)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="planar_extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(extent_status)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_status"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_status"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(extent_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(geographic_area)"><!--Child element missing, create it-->
					<xsl:call-template name="add_geographic_area"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="geographic_area"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(bounding_quadrangle)"><!--Child element missing, create it-->
					<xsl:call-template name="add_bounding_quadrangle"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="bounding_quadrangle"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::extent_status) and not(self::extent_date) and not(self::geographic_area) and not(self::bounding_quadrangle)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="geographic_area">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(area_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_area_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="area_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::area_name)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="bounding_quadrangle">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(min_horizontal)"><!--Child element missing, create it-->
					<xsl:call-template name="add_min_horizontal"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="min_horizontal"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(max_horizontal)"><!--Child element missing, create it-->
					<xsl:call-template name="add_max_horizontal"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="max_horizontal"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::min_horizontal) and not(self::max_horizontal)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="min_horizontal">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(position)"><!--Child element missing, create it-->
					<xsl:call-template name="add_position"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="position"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::position)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="max_horizontal">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(position)"><!--Child element missing, create it-->
					<xsl:call-template name="add_position"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="position"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::position)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="position">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(x)"><!--Child element missing, create it-->
					<xsl:call-template name="add_x"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="x"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(y)"><!--Child element missing, create it-->
					<xsl:call-template name="add_y"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="y"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::x) and not(self::y)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="vertical_extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(extent_status)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_status"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_status"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(extent_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(minimum_height_value)"><!--Child element missing, create it-->
					<xsl:call-template name="add_minimum_height_value"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="minimum_height_value"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(maximum_height_value)"><!--Child element missing, create it-->
					<xsl:call-template name="add_maximum_height_value"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="maximum_height_value"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::extent_status) and not(self::extent_date) and not(self::minimum_height_value) and not(self::maximum_height_value)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="temporal_extent">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(extent_status)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_status"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_status"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(extent_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extent_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extent_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(temporal_extent_description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_temporal_extent_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="temporal_extent_description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(period_range_details)"><!--Child element missing, create it-->
					<xsl:call-template name="add_period_range_details"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="period_range_details"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::extent_status) and not(self::extent_date) and not(self::temporal_extent_description) and not(self::period_range_details)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="period_range_details">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(period_start_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_period_start_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="period_start_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(period_end_date)"><!--Child element missing, create it-->
					<xsl:call-template name="add_period_end_date"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="period_end_date"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(period_range_date_qualifier)"><!--Child element missing, create it-->
					<xsl:call-template name="add_period_range_date_qualifier"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="period_range_date_qualifier"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::period_start_date) and not(self::period_end_date) and not(self::period_range_date_qualifier)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="geokey_dataset">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(aard_observatie)"><!--Child element missing, create it-->
					<xsl:call-template name="add_aard_observatie"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="aard_observatie"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::aard_observatie)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="aard_observatie">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(image_size)"><!--Child element missing, create it-->
					<xsl:call-template name="add_image_size"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="image_size"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(extra_information)"><!--Child element missing, create it-->
					<xsl:call-template name="add_extra_information"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="extra_information"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(tide)"><!--Child element missing, create it-->
					<xsl:call-template name="add_tide"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="tide"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(sensor_band)"><!--Child element missing, create it-->
					<xsl:call-template name="add_sensor_band"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="sensor_band"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(recording_time)"><!--Child element missing, create it-->
					<xsl:call-template name="add_recording_time"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="recording_time"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(pixel_size)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pixel_size"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pixel_size"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(pixel_value)"><!--Child element missing, create it-->
					<xsl:call-template name="add_pixel_value"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="pixel_value"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(preprocessing)"><!--Child element missing, create it-->
					<xsl:call-template name="add_preprocessing"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="preprocessing"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(radiometric_resolution)"><!--Child element missing, create it-->
					<xsl:call-template name="add_radiometric_resolution"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="radiometric_resolution"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(scene)"><!--Child element missing, create it-->
					<xsl:call-template name="add_scene"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="scene"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(sensor_speed)"><!--Child element missing, create it-->
					<xsl:call-template name="add_sensor_speed"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="sensor_speed"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(track)"><!--Child element missing, create it-->
					<xsl:call-template name="add_track"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="track"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(source_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_source_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="source_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(sensor_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_sensor_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="sensor_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(flying_height)"><!--Child element missing, create it-->
					<xsl:call-template name="add_flying_height"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="flying_height"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(weather)"><!--Child element missing, create it-->
					<xsl:call-template name="add_weather"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="weather"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::image_size) and not(self::extra_information) and not(self::tide) and not(self::sensor_band) and not(self::recording_time) and not(self::pixel_size) and not(self::pixel_value) and not(self::preprocessing) and not(self::radiometric_resolution) and not(self::scene) and not(self::sensor_speed) and not(self::track) and not(self::source_name) and not(self::sensor_name) and not(self::flying_height) and not(self::weather)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="data_definition">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(application_schema_description)"><!--Child element missing, create it-->
					<xsl:call-template name="add_application_schema_description"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="application_schema_description"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(object_type)"><!--Child element missing, create it-->
					<xsl:call-template name="add_object_type"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="object_type"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::application_schema_description) and not(self::object_type)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="application_schema_description">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(is_identified_by)"><!--Child element missing, create it-->
					<xsl:call-template name="add_is_identified_by"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="is_identified_by"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(is_described_by)"><!--Child element missing, create it-->
					<xsl:call-template name="add_is_described_by"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="is_described_by"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::is_identified_by) and not(self::is_described_by)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="object_type">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(object_type_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_object_type_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="object_type_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(object_type_definition)"><!--Child element missing, create it-->
					<xsl:call-template name="add_object_type_definition"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="object_type_definition"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(attribute_type)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::object_type_name) and not(self::object_type_definition) and not(self::attribute_type)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="attribute_type">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(attribute_type_name)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_name"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_name"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(attribute_type_definition)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_definition"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_definition"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(ESRI_NL)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_ESRI_NL"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="ESRI_NL"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::attribute_type_name) and not(self::attribute_type_definition) and not(self::ESRI_NL)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="attribute_type/ESRI_NL">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(attribute_type_alias)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_alias"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_alias"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(attribute_type_type)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_type"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_type"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(attribute_type_width)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_width"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_width"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(attribute_type_precision)"><!--Child element missing, create it-->
					<xsl:call-template name="add_attribute_type_precision"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="attribute_type_precision"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::attribute_type_alias) and not(self::attribute_type_type) and not(self::attribute_type_width) and not(self::attribute_type_precision)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="metadata/ESRI_NL">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(overzicht)"><!--Child element missing, create it-->
					<xsl:call-template name="add_overzicht"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="overzicht"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(documentatie)"><!--Child element missing, create it-->
					<xsl:call-template name="add_documentatie"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="documentatie"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(identificatie)"><!--Child element missing, create it-->
					<xsl:call-template name="add_identificatie"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="identificatie"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(velden)"><!--Child element missing, create it-->
					<xsl:call-template name="add_velden"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="velden"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(kwaliteit)"><!--Child element missing, create it-->
					<xsl:call-template name="add_kwaliteit"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="kwaliteit"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::overzicht) and not(self::documentatie) and not(self::metadata_reference) and not(self::identificatie) and not(self::velden) and not(self::kwaliteit)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="overzicht">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(set2D3D)"><!--Child element missing, create it-->
					<xsl:call-template name="add_set2D3D"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="set2D3D"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(gegevens_hoofdgroep)"><!--Child element missing, create it-->
					<xsl:call-template name="add_gegevens_hoofdgroep"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="gegevens_hoofdgroep"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::set2D3D) and not(self::gegevens_hoofdgroep)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="identificatie">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(invuller)"><!--Child element missing, create it-->
					<xsl:call-template name="add_invuller"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="invuller"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::invuller)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="velden">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(CEN_ingevuld)"><!--Child element missing, create it-->
					<xsl:call-template name="add_CEN_ingevuld"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="CEN_ingevuld"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::CEN_ingevuld)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="documentatie">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(document)"><!--Child element missing, create it-->
					<xsl:call-template name="add_document"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="document"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(document_link)"><!--Child element missing, create it-->
					<xsl:call-template name="add_document_link"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="document_link"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::document) and not(self::document_link)]"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="kwaliteit">
		<xsl:copy>
			<xsl:choose>
				<xsl:when test="not(kwaliteitscontrole)"><!--Child element missing, create it-->
					<xsl:call-template name="add_kwaliteitscontrole"></xsl:call-template>
				</xsl:when>
				<xsl:otherwise><!--Child element exists, copy it-->
					<xsl:apply-templates select="kwaliteitscontrole"></xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates select="@*|node()[not(self::kwaliteitscontrole)]"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
