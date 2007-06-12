<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
Auteur: Erik van de Pol. B3Partners.

Beschrijving stylesheet:
Bevat de picklists van nodes die geëdit kunnen worden. Niet elke node hoeft een picklist te hebben.
De inhoud van de picklist kan makkelijk aangepast worden kijkend naar de andere voorbeelden. Ook kun je op deze manier nieuwe
picklists toevoegen.
Een referentie hiernaar moet dan wel in "MNP_Metadata_Beheerder_Edit.xsl" toegevoegd worden Zowel bovenin als bij het element zelf.
-->

	<xsl:template name="intented_application_scale">
		<select id="intented_application_scale" name="intented_application_scale" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="1:10.000">1:10.000</option>
			<option value="1:100.000">1:100.000</option>
			<option value="1:1000">1:1000</option>
			<option value="1:25.000">1:25.000</option>
			<option value="1:250.00">1:250.00</option>
			<option value="1:2500">1:2500</option>
			<option value="1:50.000">1:50.000</option>
			<option value="1:500">1:500</option>
			<option value="A1">A1</option>
			<option value="A4">A4</option>
			<option value="A5">A5</option>
		</select>
	</xsl:template>
	
	<xsl:template name="language">
		<select id="language" name="language" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Nederlands">Nederlands</option>
			<option value="Duits">Duits</option>
			<option value="Engels">Engels</option>
			<option value="Frans">Frans</option>
			<option value="Anders">Anders</option>
		</select>
	</xsl:template>
	
	<xsl:template name="producer_organisation">
		<select id="producer_organisation" name="producer_organisation" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="ABF Research">ABF Research</option>
			<option value="Alterra">Alterra</option>
			<option value="Bridgis B.V.">Bridgis B.V.</option>
			<option value="Centraal Bureau voor de Statistiek (CBS)">Centraal Bureau voor de Statistiek (CBS)</option>
			<option value="Dienst Landelijk Gebied (DLG)">Dienst Landelijk Gebied (DLG)</option>
			<option value="directoraat-generaal Ruimte (DG Ruimte)">directoraat-generaal Ruimte (DG Ruimte)</option>
			<option value="Geodan BV">Geodan BV</option>
			<option value="IKC Natuurbeheer">IKC Natuurbeheer</option>
			<option value="Kadata B.V.">Kadata B.V.</option>
			<option value="Milieu- en Natuurplanbureau (MNP)">Milieu- en Natuurplanbureau (MNP)</option>
			<option value="Ministerie van landbouw, natuurbeheer en visserij (LNV)">Ministerie van landbouw, natuurbeheer en visserij (LNV)</option>
			<option value="Ministerie van Verkeer en Waterstaat, Directoraat-Generaal Luchtvaart (DGL)">Ministerie van Verkeer en Waterstaat, Directoraat-Generaal Luchtvaart (DGL)</option>
			<option value="Nationaal Lucht- en Ruimtevaartlaboratorium (NLR)">Nationaal Lucht- en Ruimtevaartlaboratorium (NLR)</option>
			<option value="Nirov">Nirov</option>
			<option value="Onbekend">Onbekend</option>
			<option value="Rijksinstituut voor Integraal Zoetwaterbeheer en Afvalwaterbehandeling (RIZA)">Rijksinstituut voor Integraal Zoetwaterbeheer en Afvalwaterbehandeling (RIZA)</option>
			<option value="Rijksinstituut voor Volksgezondheid en Milieu (RIVM)">Rijksinstituut voor Volksgezondheid en Milieu (RIVM)</option>
			<option value="Rijksinstituut voor Volksgezondheid en Milieu, sector Milieurisico's en Externe Veiligheid (RIVM-MEV)">Rijksinstituut voor Volksgezondheid en Milieu, sector Milieurisico's en Externe Veiligheid (RIVM-MEV)</option>
			<option value="Rijkswaterstaat, Adviesdienst Geo-Informatie en ICT (RWS-AGI)">Rijkswaterstaat, Adviesdienst Geo-Informatie en ICT (RWS-AGI)</option>
			<option value="Rijkswaterstaat, Adviesdienst Verkeer en Vervoer (RWS-AVV)">Rijkswaterstaat, Adviesdienst Verkeer en Vervoer (RWS-AVV)</option>
			<option value="Ruimtelijk Plan Bureau (RPB)">Ruimtelijk Plan Bureau (RPB)</option>
			<option value="Stichting LISA (ETIN adviseurs)">Stichting LISA (ETIN adviseurs)</option>
			<option value="Stichting Recreatie - Kennis- en Innovatiecentrum (KIC)">Stichting Recreatie - Kennis- en Innovatiecentrum (KIC)</option>
			<option value="Stichting Toegepast Onderzoek Waterbeheer (STOWA)">Stichting Toegepast Onderzoek Waterbeheer (STOWA)</option>
			<option value="TNO">TNO</option>
			<option value="Topografische Dienst Kadaster (TDK)">Topografische Dienst Kadaster (TDK)</option>
			<option value="Vereniging natuurmonumenten">Vereniging natuurmonumenten</option>
			<option value="Wegener Direct Marketing">Wegener Direct Marketing</option>
		</select>
	</xsl:template>
	
	<xsl:template name="gegevens_hoofdgroep">
		<select id="gegevens_hoofdgroep" name="gegevens_hoofdgroep" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Arbeid">Arbeid</option>
			<option value="Atmosfeer">Atmosfeer</option>
			<option value="Beleid">Beleid</option>
			<option value="Bevolking">Bevolking</option>
			<option value="Bodem en geologie">Bodem en geologie</option>
			<option value="Bodemgebruik">Bodemgebruik</option>
			<option value="Collecties">Collecties</option>
			<option value="Cultuurhistorie">Cultuurhistorie</option>
			<option value="Gebiedsindelingen">Gebiedsindelingen</option>
			<option value="Hoogtebestanden">Hoogtebestanden</option>
			<option value="Infrastructuur en vervoer">Infrastructuur en vervoer</option>
			<option value="Land">Land</option>
			<option value="Landbouw en visserij">Landbouw en visserij</option>
			<option value="Leefomgeving">Leefomgeving</option>
			<option value="Presentatielagen">Presentatielagen</option>
			<option value="Topografie">Topografie</option>
			<option value="Voorzieningen">Voorzieningen</option>
			<option value="Water">Water</option>
			<option value="Wonen">Wonen</option>
		</select>
	</xsl:template>

	<xsl:template name="spatial_schema_type_id">
		<select id="spatial_schema_type_id" name="spatial_schema_type_id" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Geen topologie (G0)">Geen topologie (G0)</option>
			<option value="Volledige topologie (G1)">Volledige topologie (G1)</option>
			<option value="2D-netwerk (G2)">2D-netwerk (G2)</option>
			<option value="3D-netwerk (G3)">3D-netwerk (G3)</option>
			<option value="3D-netwerk met vlakken (G4)">3D-netwerk met vlakken (G4)</option>
			<option value="Spaghetti (G5)">Spaghetti (G5)</option>
			<option value="TIN (G6)">TIN (G6)</option>
			<option value="Raster of image (G7)">Raster of image (G7)</option>
			<option value="Grid of lattice (G8)">Grid of lattice (G8)</option>
			<option value="Geen ruimtelijk schema">Geen ruimtelijk schema</option>
			<option value="Niet gedefinieerd">Niet gedefinieerd</option>
		</select>
	</xsl:template>
	
	<xsl:template name="method">
		<select id="method" name="method" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Digitaliseren">Digitaliseren</option>
			<option value="Gewaterpaste punten">Gewaterpaste punten</option>
			<option value="GIS-bewerkingen">GIS-bewerkingen</option>
			<option value="Grondradar">Grondradar</option>
			<option value="Hoogtelijnen uit luchtfoto's">Hoogtelijnen uit luchtfoto's</option>
			<option value="Hoogtepunten uit luchtfoto's">Hoogtepunten uit luchtfoto's</option>
			<option value="Laser altimetrie">Laser altimetrie</option>
			<option value="Luchtfoto's">Luchtfoto's</option>
			<option value="Multibeam">Multibeam</option>
			<option value="Remote sensing">Remote sensing</option>
			<option value="Singlebeam echolood">Singlebeam echolood</option>
			<option value="Subbottom profiler">Subbottom profiler</option>
			<option value="Terrestrisch ingemeten punten">Terrestrisch ingemeten punten</option>
		</select>
	</xsl:template>
	
	<xsl:template name="is_described_by">
		<select id="is_described_by" name="is_described_by" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Access">Access</option>
			<option value="ArcGIS">ArcGIS</option>
			<option value="ArcInfo">ArcInfo</option>
			<option value="ArcView">ArcView</option>
			<option value="Dino">Dino</option>
			<option value="Excel">Excel</option>
			<option value="Diverse GIS-software">Diverse GIS-software</option>
		</select>
	</xsl:template>
	
	<xsl:template name="indirect_positioning_system_name">
		<select id="indirect_positioning_system_name" name="indirect_positioning_system_name" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Administratieve eenheden">Administratieve eenheden</option>
			<option value="Adressering">Adressering</option>
			<option value="Kilometrering">Kilometrering</option>
			<option value="Postcode">Postcode</option>
			<option value="Overig">Overig</option>
		</select>
	</xsl:template>
	
	<xsl:template name="extent_status">
		<select id="extent_status" name="extent_status" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Actueel">Actueel</option>
			<option value="Toekomstig">Toekomstig</option>
			<option value="n.v.t.">n.v.t.</option>
		</select>
	</xsl:template>
	
	<xsl:template name="point_of_contacts_role">
		<select id="point_of_contacts_role" name="point_of_contacts_role" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Inhoudelijk contactpersoon">Inhoudelijk contactpersoon</option>
			<option value="Technisch contactpersoon">Technisch contactpersoon</option>
			<option value="Inhoudelijk en Technisch contactpersoon">Inhoudelijk en Technisch contactpersoon</option>
			<option value="Overig">Overig</option>
		</select>
	</xsl:template>
	
	<xsl:template name="organisation_role">
		<select id="organisation_role" name="organisation_role" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Beheerder">Beheerder</option>
			<option value="Distributeur/uitgever">Distributeur/uitgever</option>
			<option value="Eigenaar">Eigenaar</option>
			<option value="Leverancier">Leverancier</option>
			<option value="Opdrachtgever">Opdrachtgever</option>
			<option value="Overig">Overig</option>
		</select>
	</xsl:template>
	
	<xsl:template name="restrictions_on_use">
		<select id="restrictions_on_use" name="restrictions_on_use" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="Geen gebruiksbeperkingen">Geen gebruiksbeperkingen</option>
			<option value="Alleen MNP: met bronvermelding">Alleen MNP: met bronvermelding</option>
			<option value="Alleen RIVM: met bronvermelding">Alleen RIVM: met bronvermelding</option>
			<option value="Beschikbaar voor derden op basis van gebruikersovereenkomst">Beschikbaar voor derden op basis van gebruikersovereenkomst</option>
			<option value="Beschikbaar voor derden in overleg met contactpersoon">Beschikbaar voor derden in overleg met contactpersoon</option>
			<option value="Beslist niet beschikbaar voor derden">Beslist niet beschikbaar voor derden</option>
		</select>
	</xsl:template>
	
	<xsl:template name="background">
		<select id="background" name="background" onchange="pickList(this)" onBlur='pickList(this)' onkeypress="pickListKeyPress(this)" onkeydown="pickListKeyPress(this)">
			<option value="default">[huidige waarde]</option>
			<option value="basisdata Nederland">basisdata Nederland</option>
			<option value="basisdata Europa">basisdata Europa</option>
			<option value="basisdata Wereld">basisdata Wereld</option>
			<option value="projectdata">projectdata</option>
			<option value="webservice">webservice</option>
		</select>
	</xsl:template>
	
	
</xsl:stylesheet>