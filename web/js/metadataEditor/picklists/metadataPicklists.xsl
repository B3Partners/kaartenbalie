<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="picklist_CI_DateTypeCode">
		<select id="picklist_CI_DateTypeCode" name="picklist_CI_DateTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="CI_DateTypeCode_creation" title="date identifies when the resource was brought into existence">creation</option>
			<option value="CI_DateTypeCode_publication" title="date identifies when the resource was issued">publication</option>
			<option value="CI_DateTypeCode_revision" title="date identifies when the resource was examined or re-examined and imporved or amended">revision</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_CI_OnLineFunctionCode">
		<select id="picklist_CI_OnLineFunctionCode" name="picklist_CI_OnLineFunctionCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="CI_OnLineFunctionCode_download" title="online instructions for transferring data from one storage device or system to another">download</option>
			<option value="CI_OnLineFunctionCode_information" title="online information about the resource">information</option>
			<option value="CI_OnLineFunctionCode_offlineAccess" title="online instructions for requesting the resource from the provider">offlineAccess</option>
			<option value="CI_OnLineFunctionCode_order" title="online order process for obtening the resource">order</option>
			<option value="CI_OnLineFunctionCode_search" title="online search interface for seeking out information about the resource">search</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_CI_PresentationFormCode">
		<select id="picklist_CI_PresentationFormCode" name="picklist_CI_PresentationFormCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="CI_PresentationFormCode_documentDigital" title="digital representation of a primarily textual item (can contain illustrations also)">documentDigital</option>
			<option value="CI_PresentationFormCode_documentHardcopy" title="representation of a primarily textual item (can contain illustrations also) on paper, photograhic material, or other media">imageDigital</option>
			<option value="CI_PresentationFormCode_imageDigital" title="likeness of natural or man-made features, objects, and activities acquired through the sensing of visual or any other segment of the electromagnetic spectrum by sensors, such as thermal infrared, and high resolution radar and stored in digital format">documentHardcopy</option>
			<option value="CI_PresentationFormCode_imageHardcopy" title="likeness of natural or man-made features, objects, and activities acquired through the sensing of visual or any other segment of the electromagnetic spectrum by sensors, such as thermal infrared, and high resolution radar and reproduced on paper, photographic material, or other media for use directly by the human user">imageHardcopy</option>
			<option value="CI_PresentationFormCode_mapDigital" title="map represented in raster or vector form">mapDigital</option>
			<option value="CI_PresentationFormCode_mapHardcopy" title="map printed on paper, photographic material, or other media for use directly by the human user">mapHardcopy</option>
			<option value="CI_PresentationFormCode_modelDigital" title="multi-dimensional digital representation of a feature, process, etc.">modelDigital</option>
			<option value="CI_PresentationFormCode_modelHardcopy" title="3-dimensional, physical model">modelHardcopy</option>
			<option value="CI_PresentationFormCode_profileDigital" title="vertical cross-section in digital form">profileDigital</option>
			<option value="CI_PresentationFormCode_profileHardcopy" title="vertical cross-section printed on paper, etc.">profileHardcopy</option>
			<option value="CI_PresentationFormCode_tableDigital" title="digital representation of facts or figures systematically displayed, especially in columns">tableDigital</option>
			<option value="CI_PresentationFormCode_tableHardcopy" title="representation of facts or figures systematically displayed, especially in columns, printed onpapers, photographic material, or other media">tableHardcopy</option>
			<option value="CI_PresentationFormCode_videoDigital" title="digital video recording">videoDigital</option>
			<option value="CI_PresentationFormCode_videoHardcopy" title="video recording on film">videoHardcopy</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_CI_RoleCode">
		<select id="picklist_CI_RoleCode" name="picklist_CI_RoleCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="CI_RoleCode_resourceProvider" title="party that supplies the resource">resourceProvider</option>
			<option value="CI_RoleCode_custodian" title="party that accepts accountability and responsability for the data and ensures appropriate care and maintenance of the resource">custodian</option>
			<option value="CI_RoleCode_owner" title="party that owns the resource">owner</option>
			<option value="CI_RoleCode_user" title="party who uses the resource">user</option>
			<option value="CI_RoleCode_distributor" title="party who distributes the resource">distributor</option>
			<option value="CI_RoleCode_originator" title="party who created the resource">originator</option>
			<option value="CI_RoleCode_pointOfContact" title="party who can be contacted for acquiring knowledge about or acquisition of the resource">pointOfContact</option>
			<option value="CI_RoleCode_principalInvestigator" title="key party responsible for gathering information and conducting research">principalInvestigator</option>
			<option value="CI_RoleCode_processor" title="party wha has processed the data in a manner such that the resource has been modified">processor</option>
			<option value="CI_RoleCode_publisher" title="party who published the resource">publisher</option>
			<option value="CI_RoleCode_author" title="party who authored the resource">author</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_DQ_EvaluationMethodTypeCode">
		<select id="picklist_DQ_EvaluationMethodTypeCode" name="picklist_DQ_EvaluationMethodTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="DQ_EvaluationMethodTypeCode_directInternal" title="method of evaluating the quality of a dataset based on inspection of items within the dataset, where all data required is internal to the dataset being evaluated">directInternal</option>
			<option value="DQ_EvaluationMethodTypeCode_directExternal" title="method of evaluating the quality of a dataset based on inspection of items within the dataset, where reference data external to the dataset being evaluated is required">directExternal</option>
			<option value="DQ_EvaluationMethodTypeCode_indirect" title="method of evaluating the quality of a dataset based on external knowledge">indirect</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_DS_AssociationTypeCode">
		<select id="picklist_DS_AssociationTypeCode" name="picklist_DS_AssociationTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="DS_AssociationTypeCode_crossReference" title="reference from one dataset to another">crossReference</option>
			<option value="DS_AssociationTypeCode_largerWorkCitation" title="reference to a master dataset of which this one is a part">largerWorkCitation</option>
			<option value="DS_AssociationTypeCode_partOfSeamlessDatabase" title="part of the same structured set of data held in a computer">partOfSeamlessDatabase</option>
			<option value="DS_AssociationTypeCode_source" title="mapping and charting information from which the dataset content originates">source</option>
			<option value="DS_AssociationTypeCode_stereoMate" title="part of a set of imagery that when used together, provides three-dimensional images">stereoMate</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_DS_InitiativeTypeCode">
		<select id="picklist_DS_InitiativeTypeCode" name="picklist_DS_InitiativeTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="DS_InitiativeTypeCode_campaign" title="series of organized planned actions">campaign</option>
			<option value="DS_InitiativeTypeCode_collection" title="accumulation of datasets assembled for a specific purpose">collection</option>
			<option value="DS_InitiativeTypeCode_exercise" title="specific performance of a function or group of functions">exercise</option>
			<option value="DS_InitiativeTypeCode_experiment" title="process designed to find if something is effective or valid">experiment</option>
			<option value="DS_InitiativeTypeCode_investigation" title="search or systematic inquiry">investigation</option>
			<option value="DS_InitiativeTypeCode_mission" title="specific operation of a data collection system">mission</option>
			<option value="DS_InitiativeTypeCode_sensor" title="device or piece of equipment which detects or records">sensor</option>
			<option value="DS_InitiativeTypeCode_operation" title="action that is part of a series of actions">operation</option>
			<option value="DS_InitiativeTypeCode_platform" title="vehicle or other support base that holds a sensor">platform</option>
			<option value="DS_InitiativeTypeCode_process" title="method of doing something involving a number of steps">process</option>
			<option value="DS_InitiativeTypeCode_program" title="specific planned activity">program</option>
			<option value="DS_InitiativeTypeCode_project" title="organized undertaking, research, or development">project</option>
			<option value="DS_InitiativeTypeCode_study" title="examination or investigation">study</option>
			<option value="DS_InitiativeTypeCode_task" title="piece of work">task</option>
			<option value="DS_InitiativeTypeCode_trial" title="process of testing to discover or demonstrate something">trial</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_CellGeometryCode">
		<select id="picklist_MD_CellGeometryCode" name="picklist_MD_CellGeometryCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_CellGeometryCode_point" title="each cell represents a point">point</option>
			<option value="MD_CellGeometryCode_area" title="each cell represents an area">area</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_CharacterSetCode">
		<select id="picklist_MD_CharacterSetCode" name="picklist_MD_CharacterSetCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_CharacterSetCode_ucs2" title="16-bit fixed size Universal Character Set, based on ISO/IEC 10646">ucs2</option>
			<option value="MD_CharacterSetCode_ucs4" title="32-bit fixed size Universal Character Set, based on ISO/IEC 10646">ucs4</option>
			<option value="MD_CharacterSetCode_utf7" title="7-bit variable size UCS Transfer Format, based on ISO/IEC 10646">utf7</option>
			<option value="MD_CharacterSetCode_utf8" title="8-bit variable size UCS Transfer Format, based on ISO/IEC 10646">utf8</option>
			<option value="MD_CharacterSetCode_utf16" title="16-bit variable size UCS Transfer Format, based on ISO/IEC 10646">utf16</option>
			<option value="MD_CharacterSetCode_8859part1" title="ISO/IEC 8859-1, Information technology - 8-bit single byte coded graphic character sets - Part 1 : Latin alphabet No.1">8859part1</option>
			<option value="MD_CharacterSetCode_8859part2" title="ISO/IEC 8859-2, Information technology - 8-bit single byte coded graphic character sets - Part 2 : Latin alphabet No.2">8859part2</option>
			<option value="MD_CharacterSetCode_8859part3" title="ISO/IEC 8859-3, Information technology - 8-bit single byte coded graphic character sets - Part 3 : Latin alphabet No.3">8859part3</option>
			<option value="MD_CharacterSetCode_8859part4" title="ISO/IEC 8859-4, Information technology - 8-bit single byte coded graphic character sets - Part 4 : Latin alphabet No.4">8859part4</option>
			<option value="MD_CharacterSetCode_8859part5" title="ISO/IEC 8859-5, Information technology - 8-bit single byte coded graphic character sets - Part 5 : Latin/Cyrillic alphabet">8859part5</option>
			<option value="MD_CharacterSetCode_8859part6" title="ISO/IEC 8859-6, Information technology - 8-bit single byte coded graphic character sets - Part 6 : Latin/Arabic alphabet">8859part6</option>
			<option value="MD_CharacterSetCode_8859part7" title="ISO/IEC 8859-7, Information technology - 8-bit single byte coded graphic character sets - Part 7 : Latin/Greek alphabet">8859part7</option>
			<option value="MD_CharacterSetCode_8859part8" title="ISO/IEC 8859-8, Information technology - 8-bit single byte coded graphic character sets - Part 8 : Latin/Hebrew alphabet">8859part8</option>
			<option value="MD_CharacterSetCode_8859part9" title="ISO/IEC 8859-9, Information technology - 8-bit single byte coded graphic character sets - Part 9 : Latin alphabet No.5">8859part9</option>
			<option value="MD_CharacterSetCode_8859part10" title="ISO/IEC 8859-10, Information technology - 8-bit single byte coded graphic character sets - Part 10 : Latin alphabet No.6">8859part10</option>
			<option value="MD_CharacterSetCode_8859part11" title="ISO/IEC 8859-11, Information technology - 8-bit single byte coded graphic character sets - Part 11 : Latin/Thai alphabet">8859part11</option>
			<option value="MD_CharacterSetCode_8859part13" title="ISO/IEC 8859-13, Information technology - 8-bit single byte coded graphic character sets - Part 13 : Latin alphabet No.7">8859part13</option>
			<option value="MD_CharacterSetCode_8859part14" title="ISO/IEC 8859-14, Information technology - 8-bit single byte coded graphic character sets - Part 14 : Latin alphabet No.8 (Celtic)">8859part14</option>
			<option value="MD_CharacterSetCode_8859part15" title="ISO/IEC 8859-15, Information technology - 8-bit single byte coded graphic character sets - Part 15 : Latin alphabet No.9">8859part15</option>
			<option value="MD_CharacterSetCode_8859part16" title="ISO/IEC 8859-16, Information technology - 8-bit single byte coded graphic character sets - Part 16 : Latin alphabet No.10">8859part16</option>
			<option value="MD_CharacterSetCode_jis" title="japanese code set used for electronic transmission">jis</option>
			<option value="MD_CharacterSetCode_shiftJIS" title="japanese code set used on MS-DOS machines">shiftJIS</option>
			<option value="MD_CharacterSetCode_eucJP" title="japanese code set used on UNIX based machines">eucJP</option>
			<option value="MD_CharacterSetCode_usAscii" title="United States ASCII code set (ISO 646 US)">usAscii</option>
			<option value="MD_CharacterSetCode_ebcdic" title="IBM mainframe code set">ebcdic</option>
			<option value="MD_CharacterSetCode_eucKR" title="Korean code set">eucKR</option>
			<option value="MD_CharacterSetCode_big5" title="traditional Chinese code set used in Taiwan, Hong Kong of China and other areas">big5</option>
			<option value="MD_CharacterSetCode_GB2312" title="simplified Chinese code set">GB2312</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ClassificationCode">
		<select id="picklist_MD_ClassificationCode" name="picklist_MD_ClassificationCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_ClassificationCode_unclassified" title="available for general disclosure">unclassified</option>
			<option value="MD_ClassificationCode_restricted" title="not for general disclosure">restricted</option>
			<option value="MD_ClassificationCode_confidentiel" title="available for someone who can be entrusted with information">confidentiel</option>
			<option value="MD_ClassificationCode_secret" title="kept or meant to be kept private, unknown, or hidden from all but a select group of people">secret</option>
			<option value="MD_ClassificationCode_topSecret" title="of the highest secrecy">topSecret</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_CoverageContentTypeCode">
		<select id="picklist_MD_CoverageContentTypeCode" name="picklist_MD_CoverageContentTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_CoverageContentTypeCode_image" title="meaningful numerical representation of a physical parameter that is not the actual value of the physical parameter">image</option>
			<option value="MD_CoverageContentTypeCode_thematicClassification" title="code value with no quantitative meaning, used to represent a physical quantity">thematicClassification</option>
			<option value="MD_CoverageContentTypeCode_physicalMeasurement" title="value in physical units of the quantity being measured">physicalMeasurement</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_DatatypeCode">
		<select id="picklist_MD_DatatypeCode" name="picklist_MD_DatatypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_DatatypeCode_class" title="descriptor of a set of objects that share the same attributes, operations, methods, relationships, and behavior">class</option>
			<option value="MD_DatatypeCode_codelist" title="descriptor of a set of objects that share the same attributes, operations, methods, relationships, and behavior">codelist</option>
			<option value="MD_DatatypeCode_enumeration" title="data type whose instances form a list of named literal values, not extendable">enumeration</option>
			<option value="MD_DatatypeCode_codelistElement" title="permissible value for a codelist or enumeration">codelistElement</option>
			<option value="MD_DatatypeCode_abstractClass" title="class that cannot be directly instantiated">abstractClass</option>
			<option value="MD_DatatypeCode_aggregateClass" title="class that is composed of classes it is connected to by an aggregate relationship">aggregateClass</option>
			<option value="MD_DatatypeCode_specifiedClass" title="subclass that may be substituted for its superclass">specifiedClass</option>
			<option value="MD_DatatypeCode_datatypeClass" title="class with few or no operations whose primary purpose is to hold the abstract state of another class for transmittal, storage, encoding or persistent storage">datatypeClass</option>
			<option value="MD_DatatypeCode_interfaceClass" title="named set of operations that characterize the behavior of an element">interfaceClass</option>
			<option value="MD_DatatypeCode_unionClass" title="class describing a selection of one of the specified types">unionClass</option>
			<option value="MD_DatatypeCode_metaClass" title="class whose instances are classes">metaClass</option>
			<option value="MD_DatatypeCode_typeClass" title="class used for specification of a domain of instances (objects), together with the operations applicable to the objects. A type may have attributes and associations">typeClass</option>
			<option value="MD_DatatypeCode_characterString" title="free text field">characterString</option>
			<option value="MD_DatatypeCode_integer" title="numerical field">integer</option>
			<option value="MD_DatatypeCode_association" title="semantic relationship between two classes that involves connections among their instances">association</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_DimensionNameTypeCode">
		<select id="picklist_MD_DimensionNameTypeCode" name="picklist_MD_DimensionNameTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_DimensionNameTypeCode_row" title="ordinate (y) axis">row</option>
			<option value="MD_DimensionNameTypeCode_column" title="abscissa (x) axis">column</option>
			<option value="MD_DimensionNameTypeCode_vertical" title="vertical (z) axis">vertical</option>
			<option value="MD_DimensionNameTypeCode_track" title="along the direction of motion of the scan point">track</option>
			<option value="MD_DimensionNameTypeCode_crossTrack" title="perpendicular to the direction of motion of the scan point">crossTrack</option>
			<option value="MD_DimensionNameTypeCode_line" title="scan line of a sensor">line</option>
			<option value="MD_DimensionNameTypeCode_sample" title="element along a scan line">sample</option>
			<option value="MD_DimensionNameTypeCode_time" title="duration">time</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_GeometricObjectTypeCode">
		<select id="picklist_MD_GeometricObjectTypeCode" name="picklist_MD_GeometricObjectTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_GeometricObjectTypeCode_complex" title="set of geometric primitives such that their boundaries can be represented as a union of other primitives">complex</option>
			<option value="MD_GeometricObjectTypeCode_composite" title="connected set of curves, solids or surfaces">composite</option>
			<option value="MD_GeometricObjectTypeCode_curve" title="bounded, 1-dimensional geometric primitive, representing the continuous image of a line">curve</option>
			<option value="MD_GeometricObjectTypeCode_point" title="zero-dimensional geometric primitive, representing a position but not having an extent">point</option>
			<option value="MD_GeometricObjectTypeCode_solid" title="bounded, connected 3-dimensional geometric primitive, representing the continuous image of a region of space">solid</option>
			<option value="MD_GeometricObjectTypeCode_surface" title="bounded, connected 2-dimensional geometric primitive, representing the continuous image of a region of a plane">surface</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ImagingConditionCode">
		<select id="picklist_MD_ImagingConditionCode" name="picklist_MD_ImagingConditionCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_ImagingConditionCode_blurredImage" title="portion of the image is blurred">blurredImage</option>
			<option value="MD_ImagingConditionCode_cloud" title="portion of the image is partially obscured by cloud cover">cloud</option>
			<option value="MD_ImagingConditionCode_degradingObliquity" title="acute angle between the plane of the ecliptic (the plane of the Earth s orbit) and the plane of the celestial equator">degradingObliquity</option>
			<option value="MD_ImagingConditionCode_fog" title="portion of the image is partially obscured by fog">fog</option>
			<option value="MD_ImagingConditionCode_heavySmokeOrDust" title="portion of the image is partially obscured by heavy smoke or dust">heavySmokeOrDust</option>
			<option value="MD_ImagingConditionCode_night" title="image was taken at night">night</option>
			<option value="MD_ImagingConditionCode_rain" title="image was taken during rainfall">rain</option>
			<option value="MD_ImagingConditionCode_semiDarkness" title="image was taken during semi-dark conditions -- twilight conditions">semiDarkness</option>
			<option value="MD_ImagingConditionCode_shadow" title="portion of the image is obscured by shadow">shadow</option>
			<option value="MD_ImagingConditionCode_snow" title="portion of the image is obscured by snow">snow</option>
			<option value="MD_ImagingConditionCode_terrainMasking" title="the absence of collection data of a given point or area caused by the relative location of topographic features which obstruct the collection path between the collector(s) and the subject(s) of interest">terrainMasking</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_KeywordTypeCode">
		<select id="picklist_MD_KeywordTypeCode" name="picklist_MD_KeywordTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_KeywordTypeCode_discipline" title="keyword identifies a branch of instruction or specialized learning">discipline</option>
			<option value="MD_KeywordTypeCode_place" title="keyword identifies a location">place</option>
			<option value="MD_KeywordTypeCode_stratum" title="keyword identifies the layer(s) of any deposited substance">stratum</option>
			<option value="MD_KeywordTypeCode_temporal" title="keyword identifies a time period related to the dataset">temporal</option>
			<option value="MD_KeywordTypeCode_theme" title="keyword identifies a particular subject or topic">theme</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MaintenanceFrequencyCode">
		<select id="picklist_MD_MaintenanceFrequencyCode" name="picklist_MD_MaintenanceFrequencyCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_MaintenanceFrequencyCode_continual" title="data is repeatedly and frequently updated">continual</option>
			<option value="MD_MaintenanceFrequencyCode_daily" title="data is updated each day">daily</option>
			<option value="MD_MaintenanceFrequencyCode_weekly" title="data is updated on a weekly basis">weekly</option>
			<option value="MD_MaintenanceFrequencyCode_fortnightly" title="data is updated every two weeks">fortnightly</option>
			<option value="MD_MaintenanceFrequencyCode_monthly" title="data is updated each month">monthly</option>
			<option value="MD_MaintenanceFrequencyCode_quartely" title="data is updated every three months">quartely</option>
			<option value="MD_MaintenanceFrequencyCode_biannually" title="data is updated twice each year">biannually</option>
			<option value="MD_MaintenanceFrequencyCode_annually" title="data is updated every year">annually</option>
			<option value="MD_MaintenanceFrequencyCode_asNeeded" title="data is updated as deemed necessary">asNeeded</option>
			<option value="MD_MaintenanceFrequencyCode_irregular" title="data is updated in intervals that are uneven in duration">irregular</option>
			<option value="MD_MaintenanceFrequencyCode_notPlanned" title="there are no plans to update the data">notPlanned</option>
			<option value="MD_MaintenanceFrequencyCode_unknown" title="frequency of maintenance for the data is not known">unknwon</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MediumFormatCode">
		<select id="picklist_MD_MediumFormatCode" name="picklist_MD_MediumFormatCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_MediumFormatCode_cpio" title="CoPy In / Out (UNIX file format and command)">cpio</option>
			<option value="MD_MediumFormatCode_tar" title="Tape ARchive">tar</option>
			<option value="MD_MediumFormatCode_highSierra" title="high sierra file system">highSierra</option>
			<option value="MD_MediumFormatCode_iso9660" title="information processing   volume and file structure of CD-ROM">iso9660</option>
			<option value="MD_MediumFormatCode_iso9660RockRidge" title="rock ridge interchange protocol (UNIX)">iso9660RockRidge</option>
			<option value="MD_MediumFormatCode_iso9660AppleHFS" title="hierarchical file system (Macintosh)">iso9660AppleHFS</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_MediumNameCode">
		<select id="picklist_MD_MediumNameCode" name="picklist_MD_MediumNameCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_MediumNameCode_cdRom" title="read-only optical disk">cdRom</option>
			<option value="MD_MediumNameCode_dvd" title="digital versatile disk">dvd</option>
			<option value="MD_MediumNameCode_dvdRom" title="digital versatile disk, read only">dvdRom</option>
			<option value="MD_MediumNameCode_3halfInchFloppy" title="3,5 inch magnetic disk">3halfInchFloppy</option>
			<option value="MD_MediumNameCode_5quarterInchFloppy" title="5,25 inch magnetic disk">5quarterInchFloppy</option>
			<option value="MD_MediumNameCode_7trackTape" title="7 track magnetic tape">7trackTape</option>
			<option value="MD_MediumNameCode_9trackType" title="9 track magnetic tape">9trackType</option>
			<option value="MD_MediumNameCode_3480Cartridge" title="3480 cartridge tape drive">3480Cartridge</option>
			<option value="MD_MediumNameCode_3490Cartridge" title="3490 cartridge tape drive">3490Cartridge</option>
			<option value="MD_MediumNameCode_3580Cartridge" title="3580 cartridge tape drive">3580Cartridge</option>
			<option value="MD_MediumNameCode_4mmCartridgeTape" title="4 millimetre magnetic tape">4mmCartridgeTape</option>
			<option value="MD_MediumNameCode_8mmCartridgeTape" title="8 millimetre magnetic tape">8mmCartridgeTape</option>
			<option value="MD_MediumNameCode_1quarterInchCartridgeTape" title="0,25 inch magnetic tape">1quarterInchCartridgeTape</option>
			<option value="MD_MediumNameCode_digitalLinearTap" title="half inch cartridge streaming tape drive">digitalLinearTap</option>
			<option value="MD_MediumNameCode_onLine" title="direct computer linkage">onLine</option>
			<option value="MD_MediumNameCode_satellite" title="linkage through a satellite communication system">satellite</option>
			<option value="MD_MediumNameCode_telephoneLink" title="communication through a telephone network">telephoneLink</option>
			<option value="MD_MediumNameCode_hardcopy" title="pamphlet or leaflet giving descriptive information">hardcopy</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ObligationCode">
		<select id="picklist_MD_ObligationCode" name="picklist_MD_ObligationCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_ObligationCode_mandatory" title="element is always required">mandatory</option>
			<option value="MD_ObligationCode_optional" title="element is not required">optional</option>
			<option value="MD_ObligationCode_conditional" title="element is required when a specific condition is met">conditional</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_PixelOrientationCode">
		<select id="picklist_MD_PixelOrientationCode" name="picklist_MD_PixelOrientationCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_PixelOrientationCode_center" title="point halfway between the lower left and the upper right of the pixel">center</option>
			<option value="MD_PixelOrientationCode_lowerLeft" title="the corner in the pixel closest to the origin of the SRS; if two are at the same distance from the origin, the one with the smallest x-value">lowerLeft</option>
			<option value="MD_PixelOrientationCode_lowerRight" title="next corner counterclockwise from the lower left">lowerRight</option>
			<option value="MD_PixelOrientationCode_upperRight" title="next corner counterclockwise from the lower right">upperRight</option>
			<option value="MD_PixelOrientationCode_upperLeft" title="next corner counterclockwise from the upper right">upperLeft</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ProgressCode">
		<select id="picklist_MD_ProgressCode" name="picklist_MD_ProgressCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_ProgressCode_completed" title="production of the data has been completed">completed</option>
			<option value="MD_ProgressCode_historicalArchive" title="data has been stored in an offline storage facility">historicalArchive</option>
			<option value="MD_ProgressCode_obsolete" title="data is no longer relevant">obsolete</option>
			<option value="MD_ProgressCode_onGoing" title="data is continually being updated">onGoing</option>
			<option value="MD_ProgressCode_planned" title="fixed date has been established upon or by which the data will be created or updated">planned</option>
			<option value="MD_ProgressCode_required" title="data needs to be generated or updated">required</option>
			<option value="MD_ProgressCode_underDevelopment" title="data is currently in the process of being created">underDevelopment</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_RestrictionCode">
		<select id="picklist_MD_RestrictionCode" name="picklist_MD_RestrictionCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_RestrictionCode_copyright" title="exclusive right to the publication, production, or sale of the rights to a literary, dramatic, musical, or artistic work, or to the use of a commercial print or label, granted by law for a specified period of time to an author, composer, artist, distributor">copyright</option>
			<option value="MD_RestrictionCode_patent" title="government has granted exclusive right to make, sell, use or license an invention or discovery">patent</option>
			<option value="MD_RestrictionCode_patentPending" title="produced or sold information awaiting a patent">patentPending</option>
			<option value="MD_RestrictionCode_trademark" title="a name, symbol, or other device identifying a product, officially registered and legally restricted to the use of the owner or manufacturer">trademark</option>
			<option value="MD_RestrictionCode_license" title="formal permission to do something">license</option>
			<option value="MD_RestrictionCode_intellectualPropertyRights" title="rights to financial benefit from and control of distribution of non-tangible property that is a result of creativity">intellectualPropertyRights</option>
			<option value="MD_RestrictionCode_restricted" title="withheld from general circulation or disclosure">restricted</option>
			<option value="MD_RestrictionCode_otherRestrictions" title="limitation not listed">otherRestrictions</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_ScopeCode">
		<select id="picklist_MD_ScopeCode" name="picklist_MD_ScopeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_ScopeCode_attribute" title="information applies to the attribute class">attribute</option>
			<option value="MD_ScopeCode_attributeType" title="information applies to the characteristic of a feature">attributeType</option>
			<option value="MD_ScopeCode_collectionHardware" title="information applies to the collection hardware class">collectionHardware</option>
			<option value="MD_ScopeCode_collectionSession" title="information applies to the collection session">collectionSession</option>
			<option value="MD_ScopeCode_dataset" title="information applies to the dataset">dataset</option>
			<option value="MD_ScopeCode_series" title="information applies to the series">series</option>
			<option value="MD_ScopeCode_nonGeographicDataset" title="information applies to non-geographic data">nonGeographicDataset</option>
			<option value="MD_ScopeCode_dimensionGroup" title="information applies to a dimension group">dimensionGroup</option>
			<option value="MD_ScopeCode_feature" title="information applies to a feature">feature</option>
			<option value="MD_ScopeCode_featureType" title="information applies to a feature type">featureType</option>
			<option value="MD_ScopeCode_propertyType" title="information applies to a property type">propertyType</option>
			<option value="MD_ScopeCode_fieldSession" title="information applies to a field session">fieldSession</option>
			<option value="MD_ScopeCode_software" title="information applies to a computer program or routine">software</option>
			<option value="MD_ScopeCode_service" title="information applies to a capability which a service provider entity makes available to a service user entity through a set of interfaces that define a behaviour, such as a use case">service</option>
			<option value="MD_ScopeCode_model" title="information applies to a copy or imitation of an existing or hypothetical object">model</option>
			<option value="MD_ScopeCode_tile" title="information applies to a tile, a spatial subset of geographic data">tile</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_SpatialRepresentationTypeCode">
		<select id="picklist_MD_SpatialRepresentationTypeCode" name="picklist_MD_SpatialRepresentationTypeCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_SpatialRepresentationTypeCode_vector" title="vector data is used to represent geographic data">vector</option>
			<option value="MD_SpatialRepresentationTypeCode_grid" title="grid data is used to represent geographic data">grid</option>
			<option value="MD_SpatialRepresentationTypeCode_textTable" title="textual or tabular data is used to represent geographic data">textTable</option>
			<option value="MD_SpatialRepresentationTypeCode_tin" title="triangulated irregular network">tin</option>
			<option value="MD_SpatialRepresentationTypeCode_stereoModel" title="three-dimensional view formed by the intersecting homologous rays of an overlapping pair of images">stereoModel</option>
			<option value="MD_SpatialRepresentationTypeCode_video" title="scene from a video recording">video</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_TopicCategoryCode">
		<select id="picklist_MD_TopicCategoryCode" name="picklist_MD_TopicCategoryCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_TopicCategoryCode_farming" title="rearing of animals and/or cultivation of plants. Examples: agriculture, irrigation, aquaculture, plantations, herding, pests and diseases affecting crops and livestock">farming</option>
			<option value="MD_TopicCategoryCode_biota" title="flora and/or fauna in natural environment. Examples: wildlife, vegetation, biological sciences, ecology, wilderness, sealife, wetlands, habitat">biota</option>
			<option value="MD_TopicCategoryCode_boundaries" title="legal land descriptions. Examples: political and administrative boundaries">boundaries</option>
			<option value="MD_TopicCategoryCode_climatologyMeteorologyAtmosphere" title="processes and phenomena of the atmosphere. Examples: cloud cover, weather, climate, atmospheric conditions, climate change, precipitation">climatologyMeteorologyAtmosphere</option>
			<option value="MD_TopicCategoryCode_economy" title="economic activities, conditions and employment. Examples: production, labour, revenue, commerce, industry, tourism and ecotourism, forestry, fisheries, commercial or subsistence hunting, exploration and exploitation of resources such as minerals, oil and gas">economy</option>
			<option value="MD_TopicCategoryCode_elevation" title="height above or below sea level. Examples: altitude, bathymetry, digital elevation models, slope, derived products">elevation</option>
			<option value="MD_TopicCategoryCode_environment" title="environmental resources, protection and conservation. Examples: environmental pollution, waste storage and treatment, environmental impact assessment, monitoring environmental risk, nature reserves, landscape">environment</option>
			<option value="MD_TopicCategoryCode_geoscientificInformation" title="information pertaining to earth sciences. Examples: geophysical features and processes, geology, minerals, sciences dealing with the composition, structure and origin of the earth s rocks, risks of earthquakes, volcanic activity, landslides, gravity information, soils, permafrost, hydrogeology, erosion">geoscientificInformation</option>
			<option value="MD_TopicCategoryCode_health" title="health, health services, human ecology, and safety. Examples: disease and illness, factors affecting health, hygiene, substance abuse, mental and physical health, health services">health</option>
			<option value="MD_TopicCategoryCode_imageryBaseMapsEarthCover" title="base maps. Examples: land cover, topographic maps, imagery, unclassified images, annotations">imageryBaseMapsEarthCover</option>
			<option value="MD_TopicCategoryCode_intelligenceMilitary" title="military bases, structures, activities. Examples: barracks, training grounds, military transportation, information collection">intelligenceMilitary</option>
			<option value="MD_TopicCategoryCode_inlandWaters" title="inland water features, drainage systems and their characteristics. Examples: rivers and glaciers, salt lakes, water utilization plans, dams, currents, floods, water quality, hydrographic charts">inlandWaters</option>
			<option value="MD_TopicCategoryCode_location" title="positional information and services. Examples: addresses, geodetic networks, control points, postal zones and services, place names">location</option>
			<option value="MD_TopicCategoryCode_oceans" title="features and characteristics of salt water bodies (excluding inland waters). Examples: tides, tidal waves, coastal information, reefs">oceans</option>
			<option value="MD_TopicCategoryCode_planningCadastre" title="information used for appropriate actions for future use of the land. Examples: land use maps, zoning maps, cadastral surveys, land ownership">planningCadastre</option>
			<option value="MD_TopicCategoryCode_society" title="characteristics of society and cultures. Examples: settlements, anthropology, archaeology, education, traditional beliefs, manners and customs, demographic data, recreational areas and activities, social impact assessments, crime and justice, census information">society</option>
			<option value="MD_TopicCategoryCode_structure" title="man-made construction. Examples: buildings, museums, churches, factories, housing, monuments, shops, towers">structure</option>
			<option value="MD_TopicCategoryCode_transportation" title="means and aids for conveying persons and/or goods. Examples: roads, airports/airstrips, shipping routes, tunnels, nautical charts, vehicle or vessel location, aeronautical charts, railways">transportation</option>
			<option value="MD_TopicCategoryCode_utilitiesCommunication" title="energy, water and waste systems and communications infrastructure and services. Examples: hydroelectricity, geothermal, solar and nuclear sources of energy, water purification and distribution, sewage collection and disposal, electricity and gas distribution, data communication, telecommunication, radio, communication networks">utilitiesCommunication</option>
		</select>
	</xsl:template>
	<xsl:template name="picklist_MD_TopologyLevelCode">
		<select id="picklist_MD_TopologyLevelCode" name="picklist_MD_TopologyLevelCode" onchange="pickList(event)" onBlur="pickList(event)" onkeypress="pickListKeyPress(event)" onkeydown="pickListKeyPress(event)">
			<option value="default">[huidige waarde]</option>
			<option value="MD_TopologyLevelCode_geometryOnly" title="geometry objects without any additional structure which describes topology">geometryOnly</option>
			<option value="MD_TopologyLevelCode_topology1D" title="1-dimensional topological complex --  commonly called  chain-node  topology">topology1D</option>
			<option value="MD_TopologyLevelCode_planarGraph" title="1-dimensional topological complex that is planar. (A planar graph is a graph that can be drawn in a plane in such a way that no two edges intersect except at a vertex.)">planarGraph</option>
			<option value="MD_TopologyLevelCode_fullPlanarGraph" title="2-dimensional topological complex that is planar. (A 2-dimensional topological complex is commonly called  full topology  in a cartographic 2D environment.)">fullPlanarGraph</option>
			<option value="MD_TopologyLevelCode_surfaceGraph" title="1-dimensional topological complex that is isomorphic to a subset of a surface. (A geometric complex is isomorphic to a topological complex if their elements are in a one-to-one, dimensional-and boundry-preserving correspondence to one another.)">surfaceGraph</option>
			<option value="MD_TopologyLevelCode_fullSurfaceGraph" title="2-dimensional topological complex that is isomorphic to a subset of a surface">fullSurfaceGraph</option>
			<option value="MD_TopologyLevelCode_topology3D" title="3-dimensional topological complex. (A topological complex is a collection of topological primitives that are closed under the boundary operations.)">topology3D</option>
			<option value="MD_TopologyLevelCode_fullTopology3D" title="complete coverage of a 3D Euclidean coordinate space">fullTopology3D</option>
			<option value="MD_TopologyLevelCode_abstract" title="topological complex without any specified geometric realisation">abstract</option>
		</select>
	</xsl:template>
</xsl:stylesheet>