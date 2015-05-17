<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="text"/>
	<xsl:template match="/root">
		<xsl:text>Hierbij ontvangt u als bijlage het metadata bestand, dat u via de metadata editor van B3P Kaartenbalie heeft aangemaakt.</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>U kunt dit bestand inladen in een metadata repository welke het Nederlandse profiel op ISO19115:2003/ISO19139 ondersteunt. Indien bepaalde elementen niet herkend worden door uw systeem, dan is dit waarschijnlijk niet compatibel. Indien u meent dat dit metadata bestand niet correct is, gelieve contact op te nemen met B3Partners door op deze mail te antwoorden.</xsl:text>
                <xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>U heeft de volgende gegevens opgegeven:</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:apply-templates select="company"/>
		<xsl:apply-templates select="fullname"/>
		<xsl:apply-templates select="address"/>
		<xsl:apply-templates select="zipcode"/>
		<xsl:apply-templates select="city"/>
		<xsl:apply-templates select="country"/>
		<xsl:apply-templates select="phone"/>
		<xsl:apply-templates select="fax"/>
		<xsl:apply-templates select="to"/>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>Met vriendelijke groeten</xsl:text>
		<xsl:text>&#10;</xsl:text>
		<xsl:text>B3Partners BV</xsl:text>
	</xsl:template>
	<xsl:template match="fullname">
		<xsl:text>Naam: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="company">
		<xsl:text>Bedrijf: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="address">
		<xsl:text>Adres: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="zipcode">
		<xsl:text>Postcode: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="city">
		<xsl:text>Woonplaats: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="country">
		<xsl:text>Land: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="phone">
		<xsl:text>Telefoon: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="fax">
		<xsl:text>Fax: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="to">
		<xsl:text>Emailadres: </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
</xsl:stylesheet>
