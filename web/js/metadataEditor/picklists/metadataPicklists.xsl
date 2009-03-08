<?xml version="1.0" encoding="UTF-16"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by Chris Van Lith (B3Partners BV) -->
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="picklist_CI_DateTypeCode">
		<select id="picklist_CI_DateTypeCode" name="picklist_CI_DateTypeCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="creation" title="Datum waarop de dataset of dataset serie is gecreëerd.">creatie</option>
			<option value="publication" title="Datum waarop de dataset of dataset serie is gepubliceerd.">publicatie</option>
			<option value="revision" title="Datum waarop de dataset of dataset serie is gecontroleerd, verbeterd of is gewijzigd.">revisie</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_CI_RoleCode">
		<select id="picklist_CI_RoleCode" name="picklist_CI_RoleCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="resourceProvider" title="Organisatie die de data verstrekt.">verstrekker</option>
			<option value="custodian" title="Partij die verantwoordelijkheid heeft geaccepteerd en zorg draagt voor het beheer van de data.">beheerder</option>
			<option value="owner" title="Partij die eigenaar is van de data.">eigenaar</option>
			<option value="user" title="Partij die de data gebruikt.">gebruiker</option>
			<option value="distributor" title="Partij die de data verstrekt.">distributeur</option>
			<option value="originator" title="Partij die de data heeft gecreëerd.">maker</option>
			<option value="pointOfContact" title="Partij waarmee contact kan worden opgenomen voor het vergaren van kennis of verstrekking van de data.">contactpunt</option>
			<option value="principalInvestigator" title="Sleutelpartij verantwoordelijk voor verzamelen van data en de uitvoering van onderzoek.">inwinner</option>
			<option value="processor" title="Partij die de data heeft bewerkt, zodanig dat de data is gewijzigd.">bewerker</option>
			<option value="publisher" title="Partij die de data publiceert.">uitgever</option>
			<option value="author" title="Partij die auteur is van de data.">auteur</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_CharacterSetCode">
		<select id="picklist_MD_CharacterSetCode" name="picklist_MD_CharacterSetCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="ucs2" title="16 bits ISO/IEC 10646">ucs2</option>
			<option value="ucs4" title="32 bits ISO/IEC 10646">ucs4</option>
			<option value="utf7" title="7 bits ISO/IEC 10646">utf7</option>
			<option value="utf8" title="8 bits ISO/IEC 10646">utf8</option>
			<option value="utf16" title="16 bits ISO/IEC 10646">utf16</option>
			<option value="8859part1" title="ISO/IEC 8859-1, alphabet latin 1">8859part1</option>
			<option value="8859part2" title="ISO/IEC 8859-2, alphabet latin 2">8859part2</option>
			<option value="8859part3" title="ISO/IEC 8859-3, alphabet latin 3">8859part3</option>
			<option value="8859part4" title="ISO/IEC 8859-4, alphabet latin 4">8859part4</option>
			<option value="8859part5" title="ISO/IEC 8859-5, alphabet latin/cyrillique">8859part5</option>
			<option value="8859part6" title="ISO/IEC 8859-6, alphabet latin/arabe">8859part6</option>
			<option value="8859part7" title="ISO/IEC 8859-7, alphabet latin/grec">8859part7</option>
			<option value="8859part8" title="ISO/IEC 8859-8, alphabet latin/hébreu">8859part8</option>
			<option value="8859part9" title="ISO/IEC 8859-9, alphabet latin 5">8859part9</option>
			<option value="8859part10" title="ISO/IEC 8859-10, alphabet latin 6">8859part10</option>
			<option value="8859part11" title="ISO/IEC 8859-11, alphabet latin/Thaï">8859part11</option>
			<option value="8859part13" title="ISO/IEC 8859-13, alphabet latin 7">8859part13</option>
			<option value="8859part14" title="ISO/IEC 8859-14, alphabet latin 8 (celtique)">8859part14</option>
			<option value="8859part15" title="ISO/IEC 8859-15, alphabet latin 9">8859part15</option>
			<option value="8859part16" title="ISO/IEC 8859-16, alphabet latin 10">8859part16</option>
			<option value="jis" title="Japonais">jis</option>
			<option value="shiftJIS" title="Japonais pour MS-DOS">shiftJIS</option>
			<option value="eucJP" title="Japonais pour UNIX">eucJP</option>
			<option value="usAscii" title="ISO 646 US">usAscii</option>
			<option value="ebcdic" title="IBM">ebcdic</option>
			<option value="eucKR" title="Koréen">eucKR</option>
			<option value="big5" title="Chinois traditionel (Taiwan, Hong Kong, Chine)">big5</option>
			<option value="GB2312" title="Chinois simplifié">GB2312</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ClassificationCode">
		<select id="picklist_MD_ClassificationCode" name="picklist_MD_ClassificationCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="unclassified" title="Beschikbaar voor algemene ontsluiting.">vrij toegankelijk</option>
			<option value="restricted" title="Niet geschikt voor algemene ontsluiting.">niet toegankelijk</option>
			<option value="confidential" title="Beschikbaar voor personen die vertrouwd kan omgaan met de informatie.">vertrouwelijk</option>
			<option value="secret" title="Dient geheim en verborgen te worden gehouden voor iedereen behalve een geselecteerde groep personen.">geheim</option>
			<option value="topSecret" title="Hoogste geheimhouding verplicht.">zeer geheim</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MaintenanceFrequencyCode">
		<select id="picklist_MD_MaintenanceFrequencyCode" name="picklist_MD_MaintenanceFrequencyCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="continual" title="Data wordt herhaaldelijk en vaak geactualiseerd.">continu</option>
			<option value="daily" title="Data wordt elke dag geactualiseerd.">dagelijks</option>
			<option value="weekly" title="Data wordt wekelijks geactualiseerd.">wekelijks</option>
			<option value="fortnightly" title="Data wordt 2-wekelijks geactualiseerd.">2-wekelijks</option>
			<option value="monthly" title="Data wordt maandelijks geactualiseerd.">maandelijks</option>
			<option value="quarterly" title="Data wordt elke kwartaal geactualiseerd.">1 x per kwartaal</option>
			<option value="biannually" title="Data wordt half jaarlijks geactualiseerd.">1 x per half jaar</option>
			<option value="annually" title="Data wordt jaarlijks geactualiseerd.">jaarlijks</option>
			<option value="2annually" title="Data wordt één keer per 2 jaar geactualiseerd.">2-jaarlijks</option>
			<option value="3annually" title="Data wordt één keer per 3 jaar geactualiseerd.">3-jaarlijks</option>
			<option value="4annually" title="Data wordt één keer per 4 jaar geactualiseerd.">4-jaarlijks</option>
			<option value="5annually" title="Data wordt één keer per 5 jaar geactualiseerd.">5-jaarlijks</option>
			<option value="6annually" title="Data wordt één keer per 6 jaar geactualiseerd.">6-jaarlijks</option>
			<option value="7annually" title="Data wordt één keer per 7 jaar geactualiseerd.">7-jaarlijks</option>
			<option value="8annually" title="Data wordt één keer per 8 jaar geactualiseerd.">8-jaarlijks</option>
			<option value="9annually" title="Data wordt één keer per 9 jaar geactualiseerd.">9-jaarlijks</option>
			<option value="10annually" title="Data wordt één keer per 10 jaar geactualiseerd.">10-jaarlijks</option>
			<option value="moreThan10annually" title="Data wordt niet binnen 10 jaar geactualiseerd.">Meer dan 10-jaarlijks</option>
			<option value="asNeeded" title="Data wordt geactualiseerd indien nodig.">indien nodig</option>
			<option value="irregular" title="Data wordt geactualiseerd in intervallen die niet even lang duren.">onregelmatig</option>
			<option value="notPlanned" title="Er zijn geen plannen om de data te actualiseren.">niet gepland</option>
			<option value="unknown" title="Herzieningsfrequentie is niet bekend.">onbekend</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MediumNameCode">
		<select id="picklist_MD_MediumNameCode" name="picklist_MD_MediumNameCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="cdRom " title="Read only optical disk">cdRom</option>
			<option value="dvd " title="Digital versatile disk">dvd</option>
			<option value="dvdRom " title="Digital versatile disk, read only">dvdRom</option>
			<option value="3halfInchFloppy " title="3,5 inch magnetic disk">3halfInchFloppy</option>
			<option value="5quarterInchFloppy " title="5,25 inch magnetic disk">5quarterInchFloppy</option>
			<option value="7trackTape " title="7 track magnetic tape">7trackTape</option>
			<option value="9trackTape " title="9 track magnetic tape">9trackTape</option>
			<option value="3480Cartridge " title="3480 cartridge tape drive">3480Cartridge</option>
			<option value="3490Cartridge " title="3490 cartridge tape drive">3490Cartridge</option>
			<option value="3580Cartridge " title="3580 cartridge tape drive">3580Cartridge</option>
			<option value="4mmCartridgeTape " title="4 millimetre magnetic tape">4mmCartridgeTape</option>
			<option value="8mmCartridgeTape " title="8 millimetre magnetic tape">8mmCartridgeTape</option>
			<option value="1quarterInchCartridgeTape " title="0,25 inch magnetic tape">1quarterInchCartridgeTape</option>
			<option value="digitalLinearTape " title="Half inch cartridge streaming tape
drive">digitalLinearTape</option>
			<option value="onLine " title="Direct computer linkage">onLine</option>
			<option value="satellite " title="Linkage through a satelite
communication system">satellite</option>
			<option value="telephoneLink " title="Communication trough a telephone
network">telephoneLink</option>
			<option value="hardcopy " title="Pamphlet or leaflet giving descriptive information">hardcopy</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ProgressCode">
		<select id="picklist_MD_ProgressCode" name="picklist_MD_ProgressCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="completed" title="Productie van de data is compleet / afgerond.">compleet</option>
			<option value="historicalArchive" title="De data is opgeslagen in een offline opslagmedium.">historisch archief</option>
			<option value="obsolete" title="Data is niet langer relevant.">niet relevant</option>
			<option value="onGoing" title="Data wordt continu geactualiseerd.">continu geactualiseerd</option>
			<option value="planned" title="Datum is al bekend wanneer de data gecreëerd of geactualiseerd moet zijn.">gepland</option>
			<option value="required" title="Data moet nog gegenereerd of geactualiseerd worden.">actualisatie vereist</option>
			<option value="underDevelopment" title="Data wordt momenteel gecreëerd.">in ontwikkeling</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_RestrictionCode">
		<select id="picklist_MD_RestrictionCode" name="picklist_MD_RestrictionCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="copyright" title="Exclusief recht voor publicatie, productie, of verkoop van rechten op een literair, theater, muzikaal of artistiek werk, of op het gebruik van een commerciële druk of label, toegekend bij wet voor een specifieke periode of tijd aan een auteur, componist, artiest of distributeur.">copyright</option>
			<option value="patent" title="Overheid heeft een exclusief recht toegekend om een uitvinding te maken, verkopen, gebruiken of in licentie uit te geven.">patent</option>
			<option value="patentPending" title="Geproduceerde of verkochte informatie wachtend op een patent.">patent in wording</option>
			<option value="trademark" title="Een naam, symbool of ander object om een product te identificeren, wat officieel geregistreerd is en gebruik wettelijk voorbehouden is aan de eigenaar of fabrikant.">merknaam</option>
			<option value="license" title="Formele toestemming of iets te doen.">licentie</option>
			<option value="intellectualProperty Rights" title="Recht op een financieel voordeel van en controle hebben op de distributie een niet tastbaar eigendom wat het resultaat is van creativiteit.">intellectueel eigendom</option>
			<option value="restricted" title="Verbod op distributie en gebruik.">niet toegankelijk</option>
			<option value="otherRestrictions" title="Restrictie niet opgenomen in lijst.">anders</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ScopeCode">
		<select id="picklist_MD_ScopeCode" name="picklist_MD_ScopeCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="dataset" title="Informatie heeft betrekking op de dataset.">dataset</option>
			<option value="series" title="Informatie heeft betrekking op de serie.">series</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_SpatialRepresentationTypeCode">
		<select id="picklist_MD_SpatialRepresentationTypeCode" name="picklist_MD_SpatialRepresentationTypeCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="vector" title="Vector data wordt gebruikt om geografische data te representeren.">vector</option>
			<option value="grid" title="grid data is used to represent geographic data">grid</option>
			<option value="textTable" title="Tekstuele of tabel data wordt gebruikt om geografische data te representeren">textTable</option>
			<option value="tin" title="triangulated irregular network">tin</option>
			<option value="stereoModel" title="3D overzicht wordt gevormd door intersectie van twee kernstralen van twee overlappende beelden.">stereoModel</option>
			<option value="video" title="Scène uit een video opname.">video</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_TopicCategoryCode">
		<select id="picklist_MD_TopicCategoryCode" name="picklist_MD_TopicCategoryCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="farming" title="Houden van dieren en/of verbouwen van planten. Vb: landbouw, irrigatie, ziekten die gewassen aantasten.">landbouw en veeteelt</option>
			<option value="biota" title="Flora en fauna in natuurlijke omgeving. Vb: habitats, ecologie.">biota</option>
			<option value="boundaries" title="Wettelijke gebiedsbeschrijvingen. Vb: politieke en administratieve grenzen.">grenzen</option>
			<option value="climatologyMeteorologyAtmosphere" title="Processen en fenomenen in de atmosfeer. Vb: wolkbedekking, weer, klimaat verandering.">klimatologie, meteorologie, atmosfeer</option>
			<option value="economy" title="Economische activiteiten, condities en werkgelegenheid. Vb: Werkgelegenheid, industrie, toerisme, olie- en gasvelden, bosbouw, visserij.">economie</option>
			<option value="elevation" title="Hoogte boven of onder zeeniveau. Vb: hoogtekaart, DEM, hellingen.">hoogte</option>
			<option value="environment" title="Natuurlijke bronnen, bescherming en beheer. Vb: milieuverontreiniging, landschap, natuurlijke reserves, vuilopslag.">natuur en milieu</option>
			<option value="geoscientificInformation" title="Data die behoort tot een aardwetenschap. Vb: geologie, mineralen, structuur van de aarde, zwaartekrachtskaart, grondstoffen, erosie.">geowetenschappelijke data</option>
			<option value="health" title="Gezondheid(szorg), menselijke ecologie en veiligheid. Vb: ziekten, hygiëne, gezondheidszorg.">gezondheid</option>
			<option value="imageryBaseMapsEarthCover" title="Basiskaarten. Vb: landbedekking, topografische kaarten, foto’s, ongeclassificeerde kaarten.">referentie materiaal aardbedekking</option>
			<option value="intelligenceMilitary" title="Militaire basissen, structuren en activiteiten. Vb: barakken, oefenterreinen, militaire transporten.">militair</option>
			<option value="inlandWaters" title="Binnenwater, drainagesystemen en hun karakteristieken. Vb: Rivieren en gletsjers, dijken, stromen, waterzuiveringsinstallaties, overloopgebieden.">binnenwater</option>
			<option value="location" title="Positionele informatie en services. Vb: adressen, geodetisch netwerk, postcode gebieden, plaatsnamen, controlepunten.">locatie</option>
			<option value="oceans" title="Gebieden met zoutwaterlichamen (niet binnenlands). Vb: Getijden, tsunami’s, kustinformatie, riffen.">oceanen</option>
			<option value="planningCadastre" title="Informatie die gebruikt wordt voor nodige planmatige activiteiten. Vb: Landgebruik, kadastrale informatie.">planning kadaster</option>
			<option value="society" title="Kenmerken van maatschappij en culturen. Vb: antropologie, archeologie, criminaliteit, gewoonten, nederzettingen, onderwijs.">maatschappij</option>
			<option value="structure" title="Civiele werken (door mensen gemaakte structuren). Gebouwen, musea, kerken, winkels, torens.">(civiele) structuren</option>
			<option value="transportation" title="Middelen voor vervoer van goederen en/of personen. Vb: Wegen, Vliegvelden, tunnels, spoorwegen.">transport</option>
			<option value="utilities" title="Communication Energie, waterleidingen en riolering en communicatie infrastructuren en services. Vb: elektriciteit- en gasdistributie, waterzuivering en verstrekking, telecommunicatie, radio.">nutsbedrijven communicatie</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_LanguageCode">
		<select id="picklist_LanguageCode" name="picklist_LanguageCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="dut" title="Nederlands">Dutch</option>
			<option value="fry" title="Fries">Fries</option>
			<option value="eng" title="Engels">English</option>
			<option value="ger" title="Duits">German</option>
			<option value="fre" title="Frans">French</option>
			<option value="ita" title="Italiaans">Italian</option>
			<option value="slo" title="Slovaaks/Slowaaks">Slovak</option>
			<option value="bul" title="Bulgarian">Bulgarian</option>
			<option value="cze" title="Czech">Czech</option>
			<option value="lav" title="">Latvian</option>
			<option value="dan" title="Latvian">Danish</option>
			<option value="lit" title="Lithuanian">Lithuanian</option>
			<option value="mlt" title="Maltese">Maltese</option>
			<option value="pol" title="Polish">Polish</option>
			<option value="est" title="Estonian">Estonian</option>
			<option value="por" title="Portuguese">Portuguese</option>
			<option value="fin" title="Finnish">Finnish</option>
			<option value="rum" title="Romanian">Romanian</option>
			<option value="slv" title="Slovenian">Slovenian</option>
			<option value="gre" title="Greek">Greek</option>
			<option value="spa" title="Spanish">Spanish</option>
			<option value="hun" title="Hungarian">Hungarian</option>
			<option value="swe" title="Swedish">Swedish</option>
			<option value="gle" title="Irish">Irish</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_Country">
		<select id="picklist_Country" name="picklist_Country" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="UK" title="Verenigd Koninkrijk">UK</option>
			<option value="NL" title="The Netherlands">NL</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_SV_ServiceType">
		<select id="picklist_SV_ServiceType" name="picklist_SV_ServiceType" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="OGC:CSW" title="Web Catalog service">Web Catalog service</option>
			<option value="OGC:WMS" title="Web Map service">Web Map service</option>
			<option value="OGC:WFS" title="Web Feature Service">Web Feature Service</option>
			<option value="OGC:WCS" title="Web Coverage Service">Web Coverage Service</option>
			<option value="OGC:WCTS" title="Web Coordinate Transformation Service">Web Coordinate Transformation Service</option>
			<option value="OGC:WPS" title="Web Processing Service">Web Processing Service</option>
			<option value="UKST" title="Unknown Service Type">Unknown Service Type</option>
			<option value="OGC:WMC" title="Web Map Context">Web Map Context</option>
			<option value="OGC:KML" title="Keyhole Mark-up Language">Keyhole Mark-up Language</option>
			<option value="OGC:GML" title="Geography Markup Language">Geography Markup Language</option>
			<option value="OGC:WFS-G" title="Gazzetteer service">Gazzetteer service</option>
			<option value="OGC:SOS" title="Sensor Observation Service">Sensor Observation Service</option>
			<option value="OGC:SPS" title="Sensor Planning Service">Sensor Planning Service</option>
			<option value="OGC:SAS" title="Sensor Alert Service">Sensor Alert Service</option>
			<option value="OGC:WNS" title="Web Notification Service">Web Notification Service</option>
			<option value="OGC:ODS" title="OpenLS Directory Service">OpenLS Directory Service</option>
			<option value="OGC:OGS" title="OpenLS Gateway Service">OpenLS Gateway Service</option>
			<option value="OGC:OUS" title="OpenLS Utility Service">OpenLS Utility Service</option>
			<option value="OGC:OPS" title="OpenLS Presentation Service">OpenLS Presentation Service</option>
			<option value="OGC:ORS" title="OpenLS Route Service">OpenLS Route Service</option>
			<option value="website" title="website">website</option>
			<option value="download" title="download">download</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_Boolean">
		<select id="picklist_Boolean" name="picklist_Boolean" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="TRUE" title="'TRUE'">waar</option>
			<option value="FALSE" title="'FALSE'">onwaar</option>
		</select>
	</xsl:template>
</xsl:stylesheet>
