<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="picklist_CI_DateTypeCode">
		<select id="picklist_CI_DateTypeCode" name="picklist_CI_DateTypeCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="creatie" title="Datum waarop de dataset of dataset serie is gecreëerd.">creatie</option>
			<option value="publicatie" title="Datum waarop de dataset of dataset serie is gepubliceerd.">publicatie</option>
			<option value="revisie" title="Datum waarop de dataset of dataset serie is gecontroleerd, verbeterd of is gewijzigd.">revisie</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_CI_RoleCode">
		<select id="picklist_CI_RoleCode" name="picklist_CI_RoleCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="verstrekker" title="Organisatie die de data verstrekt.">verstrekker</option>
			<option value="beheerder" title="Partij die verantwoordelijkheid heeft geaccepteerd en zorg draagt voor het beheer van de data.">beheerder</option>
			<option value="eigenaar" title="Partij die eigenaar is van de data.">eigenaar</option>
			<option value="gebruiker" title="Partij die de data gebruikt.">gebruiker</option>
			<option value="distributeur" title="Partij die de data verstrekt.">distributeur</option>
			<option value="maker" title="Partij die de data heeft gecreëerd.">maker</option>
			<option value="contactpunt" title="Partij waarmee contact kan worden opgenomen voor het vergaren van kennis of verstrekking van de data.">contactpunt</option>
			<option value="inwinner" title="Sleutelpartij verantwoordelijk voor verzamelen van data en de uitvoering van onderzoek.">inwinner</option>
			<option value="bewerker" title="Partij die de data heeft bewerkt, zodanig dat de data is gewijzigd.">bewerker</option>
			<option value="uitgever" title="Partij die de data publiceert.">uitgever</option>
			<option value="auteur" title="Partij die auteur is van de data.">auteur</option>
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
			<option value="vrij toegankelijk" title="Beschikbaar voor algemene ontsluiting.">vrij toegankelijk</option>
			<option value="niet toegankelijk" title="Niet geschikt voor algemene ontsluiting.">niet toegankelijk</option>
			<option value="vertrouwelijk" title="Beschikbaar voor personen die vertrouwd kan omgaan met de informatie.">vertrouwelijk</option>
			<option value="geheim" title="Dient geheim en verborgen te worden gehouden voor iedereen behalve een geselecteerde groep personen.">geheim</option>
			<option value="zeer geheim" title="Hoogste geheimhouding verplicht.">zeer geheim</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MaintenanceFrequencyCode">
		<select id="picklist_MD_MaintenanceFrequencyCode" name="picklist_MD_MaintenanceFrequencyCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="continu" title="Data wordt herhaaldelijk en vaak geactualiseerd.">continu</option>
			<option value="dagelijks" title="Data wordt elke dag geactualiseerd.">dagelijks</option>
			<option value="wekelijks" title="Data wordt wekelijks geactualiseerd.">wekelijks</option>
			<option value="2-wekelijks" title="Data wordt 2-wekelijks geactualiseerd.">2-wekelijks</option>
			<option value="maandelijks" title="Data wordt maandelijks geactualiseerd.">maandelijks</option>
			<option value="1 x per kwartaal" title="Data wordt elke kwartaal geactualiseerd.">1 x per kwartaal</option>
			<option value="1 x per half jaar" title="Data wordt half jaarlijks geactualiseerd.">1 x per half jaar</option>
			<option value="jaarlijks" title="Data wordt jaarlijks geactualiseerd.">jaarlijks</option>
			<option value="2-jaarlijks" title="Data wordt één keer per 2 jaar geactualiseerd.">2-jaarlijks</option>
			<option value="3-jaarlijks" title="Data wordt één keer per 3 jaar geactualiseerd.">3-jaarlijks</option>
			<option value="4-jaarlijks" title="Data wordt één keer per 4 jaar geactualiseerd.">4-jaarlijks</option>
			<option value="5-jaarlijks" title="Data wordt één keer per 5 jaar geactualiseerd.">5-jaarlijks</option>
			<option value="6-jaarlijks" title="Data wordt één keer per 6 jaar geactualiseerd.">6-jaarlijks</option>
			<option value="7-jaarlijks" title="Data wordt één keer per 7 jaar geactualiseerd.">7-jaarlijks</option>
			<option value="8-jaarlijks" title="Data wordt één keer per 8 jaar geactualiseerd.">8-jaarlijks</option>
			<option value="9-jaarlijks" title="Data wordt één keer per 9 jaar geactualiseerd.">9-jaarlijks</option>
			<option value="10-jaarlijks" title="Data wordt één keer per 10 jaar geactualiseerd.">10-jaarlijks</option>
			<option value="Meer dan 10-jaarlijks" title="Data wordt niet binnen 10 jaar geactualiseerd.">Meer dan 10-jaarlijks</option>
			<option value="indien nodig" title="Data wordt geactualiseerd indien nodig.">indien nodig</option>
			<option value="onregelmatig" title="Data wordt geactualiseerd in intervallen die niet even lang duren.">onregelmatig</option>
			<option value="niet gepland" title="Er zijn geen plannen om de data te actualiseren.">niet gepland</option>
			<option value="onbekend" title="Herzieningsfrequentie is niet bekend.">onbekend</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ProgressCode">
		<select id="picklist_MD_ProgressCode" name="picklist_MD_ProgressCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="compleet" title="Productie van de data is compleet / afgerond.">compleet</option>
			<option value="historisch archief" title="De data is opgeslagen in een offline opslagmedium.">historisch archief</option>
			<option value="niet relevant" title="Data is niet langer relevant.">niet relevant</option>
			<option value="continu geactualiseerd" title="Data wordt continu geactualiseerd.">continu geactualiseerd</option>
			<option value="gepland" title="Datum is al bekend wanneer de data gecreëerd of geactualiseerd moet zijn.">gepland</option>
			<option value="actualisatie vereist" title="Data moet nog gegenereerd of geactualiseerd worden.">actualisatie vereist</option>
			<option value="in ontwikkeling" title="Data wordt momenteel gecreëerd.">in ontwikkeling</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_RestrictionCode">
		<select id="picklist_MD_RestrictionCode" name="picklist_MD_RestrictionCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="copyright" title="Exclusief recht voor publicatie, productie, of verkoop van rechten op een literair, theater, muzikaal of artistiek werk, of op het gebruik van een commerciële druk of label, toegekend bij wet voor een specifieke periode of tijd aan een auteur, componist, artiest of distributeur.">copyright</option>
			<option value="patent" title="Overheid heeft een exclusief recht toegekend om een uitvinding te maken, verkopen, gebruiken of in licentie uit te geven.">patent</option>
			<option value="patent in wording" title="Geproduceerde of verkochte informatie wachtend op een patent.">patent in wording</option>
			<option value="merknaam" title="Een naam, symbool of ander object om een product te identificeren, wat officieel geregistreerd is en gebruik wettelijk voorbehouden is aan de eigenaar of fabrikant.">merknaam</option>
			<option value="licentie" title="Formele toestemming of iets te doen.">licentie</option>
			<option value="intellectueel eigendom" title="Recht op een financieel voordeel van en controle hebben op de distributie een niet tastbaar eigendom wat het resultaat is van creativiteit.">intellectueel eigendom</option>
			<option value="niet toegankelijk" title="Verbod op distributie en gebruik.">niet toegankelijk</option>
			<option value="anders" title="Restrictie niet opgenomen in lijst.">anders</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ScopeCode">
		<select id="picklist_MD_ScopeCode" name="picklist_MD_ScopeCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="dataset" title="Informatie heeft betrekking op de dataset.">dataset</option>
			<option value="series" title="Informatie heeft betrekking op de serie.">series</option>
			<option value="service" title="Informatie heeft betrekking op de service.">service</option>
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
			<option value="landbouw en veeteelt" title="Houden van dieren en/of verbouwen van planten. Vb: landbouw, irrigatie, ziekten die gewassen aantasten.">landbouw en veeteelt</option>
			<option value="biota" title="Flora en fauna in natuurlijke omgeving. Vb: habitats, ecologie.">biota</option>
			<option value="grenzen" title="Wettelijke gebiedsbeschrijvingen. Vb: politieke en administratieve grenzen.">grenzen</option>
			<option value="klimatologie, meteorologie, atmosfeer" title="Processen en fenomenen in de atmosfeer. Vb: wolkbedekking, weer, klimaat verandering.">klimatologie, meteorologie, atmosfeer</option>
			<option value="economie" title="Economische activiteiten, condities en werkgelegenheid. Vb: Werkgelegenheid, industrie, toerisme, olie- en gasvelden, bosbouw, visserij.">economie</option>
			<option value="hoogte" title="Hoogte boven of onder zeeniveau. Vb: hoogtekaart, DEM, hellingen.">hoogte</option>
			<option value="natuur en milieu" title="Natuurlijke bronnen, bescherming en beheer. Vb: milieuverontreiniging, landschap, natuurlijke reserves, vuilopslag.">natuur en milieu</option>
			<option value="geowetenschappelijke data" title="Data die behoort tot een aardwetenschap. Vb: geologie, mineralen, structuur van de aarde, zwaartekrachtskaart, grondstoffen, erosie.">geowetenschappelijke data</option>
			<option value="gezondheid" title="Gezondheid(szorg), menselijke ecologie en veiligheid. Vb: ziekten, hygiëne, gezondheidszorg.">gezondheid</option>
			<option value="referentie materiaal aardbedekking" title="Basiskaarten. Vb: landbedekking, topografische kaarten, foto’s, ongeclassificeerde kaarten.">referentie materiaal aardbedekking</option>
			<option value="militair" title="Militaire basissen, structuren en activiteiten. Vb: barakken, oefenterreinen, militaire transporten.">militair</option>
			<option value="binnenwater" title="Binnenwater, drainagesystemen en hun karakteristieken. Vb: Rivieren en gletsjers, dijken, stromen, waterzuiveringsinstallaties, overloopgebieden.">binnenwater</option>
			<option value="locatie" title="Positionele informatie en services. Vb: adressen, geodetisch netwerk, postcode gebieden, plaatsnamen, controlepunten.">locatie</option>
			<option value="oceanen" title="Gebieden met zoutwaterlichamen (niet binnenlands). Vb: Getijden, tsunami’s, kustinformatie, riffen.">oceanen</option>
			<option value="planning kadaster" title="Informatie die gebruikt wordt voor nodige planmatige activiteiten. Vb: Landgebruik, kadastrale informatie.">planning kadaster</option>
			<option value="maatschappij" title="Kenmerken van maatschappij en culturen. Vb: antropologie, archeologie, criminaliteit, gewoonten, nederzettingen, onderwijs.">maatschappij</option>
			<option value="(civiele) structuren" title="Civiele werken (door mensen gemaakte structuren). Gebouwen, musea, kerken, winkels, torens.">(civiele) structuren</option>
			<option value="transport" title="Middelen voor vervoer van goederen en/of personen. Vb: Wegen, Vliegvelden, tunnels, spoorwegen.">transport</option>
			<option value="nutsbedrijven communicatie" title="Communication Energie, waterleidingen en riolering en communicatie infrastructuren en services. Vb: elektriciteit- en gasdistributie, waterzuivering en verstrekking, telecommunicatie, radio.">nutsbedrijven communicatie</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_LanguageCode">
		<select id="picklist_LanguageCode" name="picklist_LanguageCode" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="eng" title="Engels">eng</option>
			<option value="dut" title="Nederlands">dut</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_Country">
		<select id="picklist_Country" name="picklist_Country" onchange="selectPickListValue(event)" onblur="destroyPickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="UK" title="Verenigd Koninkrijk">UK</option>
			<option value="NL" title="The Netherlands">NL</option>
		</select>
	</xsl:template>
</xsl:stylesheet>
