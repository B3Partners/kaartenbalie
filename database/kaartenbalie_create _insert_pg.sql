CREATE TABLE layer ( id INTEGER DEFAULT nextval('layer_id_seq'::regclass) NOT NULL, parentid INTEGER, serviceproviderid INTEGER NOT NULL, name CHARACTER VARYING(200), title CHARACTER VARYING(255) DEFAULT '0' NOT NULL, abstracts TEXT, queryable CHARACTER VARYING(50) DEFAULT '0', cascaded CHARACTER VARYING(50) DEFAULT '0', opaque CHARACTER VARYING(50) DEFAULT '0', nosubsets CHARACTER VARYING(50) DEFAULT '0', fixedwidth CHARACTER VARYING(50) DEFAULT '0', fixedheight CHARACTER VARYING(50) DEFAULT '0', scalehintmin CHARACTER VARYING(50) DEFAULT '0', scalehintmax CHARACTER VARYING(50) DEFAULT '0', metadata TEXT, PRIMARY KEY (id) );
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (12, null, 1, 'nieuwekaart', 'de Nieuwe Kaart van Nederland', null, null, '1', null, null, null, null, '0.498902848429637', '14967.0854528891', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (2, 12, 1, 'nk_werken', 'Nieuwe Kaart - Werken', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (3, 12, 1, 'nk_recreatie', 'Nieuwe Kaart - Recreatie', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (4, 12, 1, 'nk_water', 'Nieuwe Kaart - Water', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (6, 12, 1, 'nk_landbouw', 'Nieuwe Kaart - Landbouw', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (7, 12, 1, 'nk_lijn', 'Nieuwe Kaart - Lijn', 'nk_lijn', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (8, 12, 1, 'nk_voorziening', 'Nieuwe Kaart - Voorziening', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (9, 12, 1, 'nk_wonen', 'Nieuwe Kaart - Wonen', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (10, 12, 1, 'nk_verkeer', 'Nieuwe Kaart - Verkeer', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (16, null, 2, 'OpenStreetMap2', 'OpenStreetMap2', null, null, '1', null, null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (17, 16, 2, 'waterwood', 'land use: industrial', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (18, 16, 2, 'greens', 'leisure', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (19, 16, 2, 'primaryroads50_400', 'straten5', null, '0', '1', '0', null, null, null, '24.9451424214819', '199.561139371855', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (20, 16, 2, 'railways', 'railways', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (21, 16, 2, 'places', 'City layer', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (22, 16, 2, 'highways0_50', 'straten6', null, '0', '1', '0', null, null, null, '4.98902848429637', '24.9451424214819', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (23, 16, 2, 'provinciegrens', 'provinciegrens', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (24, 16, 2, 'highways50_4000', 'snelwegen50_4000', null, '0', '1', '0', null, null, null, '24.9451424214819', '1995.61139371855', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (25, 16, 2, 'roads0_50', 'straten3', null, '0', '1', '0', null, null, null, '4.98902848429637', '24.9451424214819', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (26, 16, 2, 'roads50_100', 'straten4', null, '0', '1', '0', null, null, null, '24.9451424214819', '49.8902848429637', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (27, 16, 2, 'streets30_70', 'straten2', null, '0', '1', '0', null, null, null, '14.9670854528891', '34.9231993900746', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (13, 16, 2, 'basislaag', 'basislaag', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (14, 16, 2, 'streets0_30', 'straten1', null, '0', '1', '0', null, null, null, '4.98902848429637', '14.9670854528891', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (15, 16, 2, 'residentialandindustry', 'residentialandindustry', null, '0', '1', '0', null, null, null, '4.98902848429637', '0', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (42, null, 3, 'OpenStreetMap', 'OpenStreetMap', null, null, '1', null, null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (28, 42, 3, 'basislaag', 'basislaag', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (29, 42, 3, 'streets0_30', 'straten1', null, '0', '1', '0', null, null, null, '0', '14.9670854528891', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (30, 42, 3, 'residentialandindustry', 'residentialandindustry', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (31, 42, 3, 'waterwood', 'land use: industrial', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (32, 42, 3, 'greens', 'leisure', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (33, 42, 3, 'primaryroads50_400', 'straten5', null, '0', '1', '0', null, null, null, '24.9451424214819', '199.561139371855', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (34, 42, 3, 'railways', 'railways', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (35, 42, 3, 'places', 'City layer', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (36, 42, 3, 'highways0_50', 'straten6', null, '0', '1', '0', null, null, null, '0', '24.9451424214819', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (37, 42, 3, 'provinciegrens', 'provinciegrens', null, '0', '1', '0', null, null, null, null, null, null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (38, 42, 3, 'highways50_4000', 'snelwegen50_4000', null, '0', '1', '0', null, null, null, '24.9451424214819', '1995.61139371855', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (39, 42, 3, 'roads0_50', 'straten3', null, '0', '1', '0', null, null, null, '0', '24.9451424214819', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (40, 42, 3, 'roads50_100', 'straten4', null, '0', '1', '0', null, null, null, '24.9451424214819', '49.8902848429637', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (41, 42, 3, 'streets30_70', 'straten2', null, '0', '1', '0', null, null, null, '14.9670854528891', '34.9231993900746', null);
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (5, 12, 1, 'nk_gemengd', 'Nieuwe Kaart - Gemengd', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, '<?xml version="1.0" encoding="UTF-8"?>
<MD_Metadata xmlns="http://www.isotc211.org/2005/gmd"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gml="http://www.opengis.net/gml"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <gmd:fileIdentifier xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>erytety</gco:CharacterString>
    </gmd:fileIdentifier>
    <gmd:language xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>deu</gco:CharacterString>
    </gmd:language>
    <gmd:characterSet xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
    </gmd:characterSet>
    <gmd:hierarchyLevel xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
    </gmd:hierarchyLevel>
    <gmd:hierarchyLevelName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString/>
    </gmd:hierarchyLevelName>
    <gmd:contact xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:CI_ResponsibleParty>
            <gmd:organisationName>
                <gco:CharacterString/>
            </gmd:organisationName>
            <gmd:role>
                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue=""/>
            </gmd:role>
            <gmd:contactInfo>
                <gmd:CI_Contact>
                    <gmd:onlineResource>
                        <gmd:CI_OnlineResource>
                            <gmd:linkage>
                                <gmd:URL/>
                            </gmd:linkage>
                        </gmd:CI_OnlineResource>
                    </gmd:onlineResource>
                </gmd:CI_Contact>
            </gmd:contactInfo>
        </gmd:CI_ResponsibleParty>
    </gmd:contact>
    <gmd:dateStamp xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:Date>2008-12-20</gco:Date>
    </gmd:dateStamp>
    <gmd:metadataStandardName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>ISO 19115:2003</gco:CharacterString>
    </gmd:metadataStandardName>
    <gmd:metadataStandardVersion xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>Nederlandse metadatastandaard voor geografie 1.1</gco:CharacterString>
    </gmd:metadataStandardVersion>
    <gmd:referenceSystemInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ReferenceSystem>
            <gmd:referenceSystemIdentifier>
                <gmd:RS_Identifier>
                    <gmd:code>
                        <gco:CharacterString>28992</gco:CharacterString>
                    </gmd:code>
                    <gmd:codeSpace>
                        <gco:CharacterString>EPSG</gco:CharacterString>
                    </gmd:codeSpace>
                </gmd:RS_Identifier>
            </gmd:referenceSystemIdentifier>
        </gmd:MD_ReferenceSystem>
    </gmd:referenceSystemInfo>
    <gmd:identificationInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_DataIdentification>
            <gmd:characterSet>
                <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
            </gmd:characterSet>
            <gmd:citation>
                <gmd:CI_Citation>
                    <gmd:title>
                        <gco:CharacterString>een nieuwe titel</gco:CharacterString>
                    </gmd:title>
                    <gmd:date>
                        <gmd:CI_Date>
                            <gmd:date>
                                <gco:Date/>
                            </gmd:date>
                            <gmd:dateType>
                                <gmd:CI_DateTypeCode
                                    codeList="#CI_DateTypeCode" codeListValue=""/>
                            </gmd:dateType>
                        </gmd:CI_Date>
                    </gmd:date>
                </gmd:CI_Citation>
            </gmd:citation>
            <gmd:abstract>
                <gco:CharacterString/>
            </gmd:abstract>
            <gmd:status>
                <gmd:MD_ProgressCode codeList="#MD_ProgressCode" codeListValue="complete">compleet</gmd:MD_ProgressCode>
            </gmd:status>
            <gmd:pointOfContact>
                <gmd:CI_ResponsibleParty>
                    <gmd:organisationName>
                        <gco:CharacterString/>
                    </gmd:organisationName>
                    <gmd:role>
                        <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue=""/>
                    </gmd:role>
                    <gmd:contactInfo>
                        <gmd:CI_Contact>
                            <gmd:onlineResource>
                                <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL/>
                                    </gmd:linkage>
                                </gmd:CI_OnlineResource>
                            </gmd:onlineResource>
                        </gmd:CI_Contact>
                    </gmd:contactInfo>
                </gmd:CI_ResponsibleParty>
            </gmd:pointOfContact>
            <gmd:descriptiveKeywords>
                <gmd:MD_Keywords>
                    <gmd:keyword>
                        <gco:CharacterString/>
                    </gmd:keyword>
                </gmd:MD_Keywords>
            </gmd:descriptiveKeywords>
            <gmd:resourceConstraints>
                <gmd:MD_Constraints>
                    <gmd:useLimitation>
                        <gco:CharacterString/>
                    </gmd:useLimitation>
                </gmd:MD_Constraints>
            </gmd:resourceConstraints>
            <gmd:resourceConstraints>
                <gmd:MD_LegalConstraints>
                    <gmd:accessConstraints>
                        <gmd:MD_RestrictionCode
                            codeList="#MD_RestrictionCode" codeListValue=""/>
                    </gmd:accessConstraints>
                </gmd:MD_LegalConstraints>
            </gmd:resourceConstraints>
            <gmd:spatialRepresentationType>
                <gmd:MD_SpatialRepresentationTypeCode
                    codeList="#MD_SpatialRepresentationTypeCode" codeListValue=""/>
            </gmd:spatialRepresentationType>
            <gmd:spatialResolution>
                <gmd:MD_Resolution>
                    <gmd:equivalentScale>
                        <gmd:MD_RepresentativeFraction>
                            <gmd:denominator>
                                <gco:Integer/>
                            </gmd:denominator>
                        </gmd:MD_RepresentativeFraction>
                    </gmd:equivalentScale>
                </gmd:MD_Resolution>
            </gmd:spatialResolution>
            <gmd:language>
                <gco:CharacterString>nld</gco:CharacterString>
            </gmd:language>
            <gmd:topicCategory>
                <gmd:MD_TopicCategoryCode/>
            </gmd:topicCategory>
            <gmd:extent>
                <gmd:EX_Extent>
                    <gmd:geographicElement>
                        <gmd:EX_GeographicBoundingBox>
                            <gmd:westBoundLongitude>
                                <gco:Decimal>3.3</gco:Decimal>
                            </gmd:westBoundLongitude>
                            <gmd:eastBoundLongitude>
                                <gco:Decimal>7.2</gco:Decimal>
                            </gmd:eastBoundLongitude>
                            <gmd:southBoundLatitude>
                                <gco:Decimal>50.5</gco:Decimal>
                            </gmd:southBoundLatitude>
                            <gmd:northBoundLatitude>
                                <gco:Decimal>53.5</gco:Decimal>
                            </gmd:northBoundLatitude>
                        </gmd:EX_GeographicBoundingBox>
                    </gmd:geographicElement>
                    <gmd:temporalElement>
                        <gmd:EX_TemporalExtent>
                            <gmd:extent>
                                <gml:TimePeriod>
                                    <gml:begin>
                                    <gml:TimeInstant gml:id="t11">
                                    <gml:timePosition/>
                                    </gml:TimeInstant>
                                    </gml:begin>
                                    <gml:end>
                                    <gml:TimeInstant gml:id="t12">
                                    <gml:timePosition/>
                                    </gml:TimeInstant>
                                    </gml:end>
                                </gml:TimePeriod>
                            </gmd:extent>
                        </gmd:EX_TemporalExtent>
                    </gmd:temporalElement>
                </gmd:EX_Extent>
            </gmd:extent>
        </gmd:MD_DataIdentification>
    </gmd:identificationInfo>
    <gmd:distributionInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_Distribution>
            <gmd:distributor>
                <gmd:MD_Distributor>
                    <gmd:distributorContact>
                        <gmd:CI_ResponsibleParty>
                            <gmd:organisationName>
                                <gco:CharacterString/>
                            </gmd:organisationName>
                            <gmd:role>
                                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue=""/>
                            </gmd:role>
                            <gmd:contactInfo>
                                <gmd:CI_Contact>
                                    <gmd:onlineResource>
                                    <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL/>
                                    </gmd:linkage>
                                    </gmd:CI_OnlineResource>
                                    </gmd:onlineResource>
                                </gmd:CI_Contact>
                            </gmd:contactInfo>
                        </gmd:CI_ResponsibleParty>
                    </gmd:distributorContact>
                </gmd:MD_Distributor>
            </gmd:distributor>
        </gmd:MD_Distribution>
    </gmd:distributionInfo>
    <gmd:dataQualityInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:DQ_DataQuality>
            <gmd:scope>
                <gmd:DQ_Scope>
                    <gmd:level>
                        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
                    </gmd:level>
                </gmd:DQ_Scope>
            </gmd:scope>
            <gmd:lineage>
                <gmd:LI_Lineage>
                    <gmd:statement>
                        <gco:CharacterString/>
                    </gmd:statement>
                </gmd:LI_Lineage>
            </gmd:lineage>
        </gmd:DQ_DataQuality>
    </gmd:dataQualityInfo>
</MD_Metadata>
');
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (11, 12, 1, 'nk_plan', 'Nieuwe Kaart - Plannen', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, '<?xml version="1.0" encoding="UTF-8"?>
<MD_Metadata xmlns="http://www.isotc211.org/2005/gmd"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gml="http://www.opengis.net/gml"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <gmd:fileIdentifier xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>a7f2d375-5e2d-423c-9a43-9dda0d1eff44</gco:CharacterString>
    </gmd:fileIdentifier>
    <gmd:language xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>nld</gco:CharacterString>
    </gmd:language>
    <gmd:characterSet xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
    </gmd:characterSet>
    <gmd:hierarchyLevel xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
    </gmd:hierarchyLevel>
    <gmd:hierarchyLevelName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>sdfg</gco:CharacterString>
    </gmd:hierarchyLevelName>
    <gmd:contact xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:CI_ResponsibleParty>
            <gmd:organisationName>
                <gco:CharacterString>sdfg</gco:CharacterString>
            </gmd:organisationName>
            <gmd:role>
                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="custodian">beheerder</gmd:CI_RoleCode>
            </gmd:role>
            <gmd:contactInfo>
                <gmd:CI_Contact>
                    <gmd:onlineResource>
                        <gmd:CI_OnlineResource>
                            <gmd:linkage>
                                <gmd:URL>sdfg</gmd:URL>
                            </gmd:linkage>
                        </gmd:CI_OnlineResource>
                    </gmd:onlineResource>
                </gmd:CI_Contact>
            </gmd:contactInfo>
        </gmd:CI_ResponsibleParty>
    </gmd:contact>
    <gmd:dateStamp xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:Date>2008-12-20</gco:Date>
    </gmd:dateStamp>
    <gmd:metadataStandardName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>ISO 19115:2003</gco:CharacterString>
    </gmd:metadataStandardName>
    <gmd:metadataStandardVersion xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>Nederlandse metadatastandaard voor geografie 1.1</gco:CharacterString>
    </gmd:metadataStandardVersion>
    <gmd:referenceSystemInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ReferenceSystem>
            <gmd:referenceSystemIdentifier>
                <gmd:RS_Identifier>
                    <gmd:code>
                        <gco:CharacterString>28992</gco:CharacterString>
                    </gmd:code>
                    <gmd:codeSpace>
                        <gco:CharacterString>EPSG</gco:CharacterString>
                    </gmd:codeSpace>
                </gmd:RS_Identifier>
            </gmd:referenceSystemIdentifier>
        </gmd:MD_ReferenceSystem>
    </gmd:referenceSystemInfo>
    <gmd:identificationInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_DataIdentification>
            <gmd:characterSet>
                <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
            </gmd:characterSet>
            <gmd:citation>
                <gmd:CI_Citation>
                    <gmd:title>
                        <gco:CharacterString>Nieuwe Kaart - Plannen</gco:CharacterString>
                    </gmd:title>
                    <gmd:date>
                        <gmd:CI_Date>
                            <gmd:date>
                                <gco:Date>2008-12-31</gco:Date>
                            </gmd:date>
                            <gmd:dateType>
                                <gmd:CI_DateTypeCode
                                    codeList="#CI_DateTypeCode" codeListValue="creation">creatie</gmd:CI_DateTypeCode>
                            </gmd:dateType>
                        </gmd:CI_Date>
                    </gmd:date>
                </gmd:CI_Citation>
            </gmd:citation>
            <gmd:abstract>
                <gco:CharacterString>dfghdf</gco:CharacterString>
            </gmd:abstract>
            <gmd:status>
                <gmd:MD_ProgressCode codeList="#MD_ProgressCode" codeListValue="completed">compleet</gmd:MD_ProgressCode>
            </gmd:status>
            <gmd:pointOfContact>
                <gmd:CI_ResponsibleParty>
                    <gmd:organisationName>
                        <gco:CharacterString>dfsfg</gco:CharacterString>
                    </gmd:organisationName>
                    <gmd:role>
                        <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="custodian">beheerder</gmd:CI_RoleCode>
                    </gmd:role>
                    <gmd:contactInfo>
                        <gmd:CI_Contact>
                            <gmd:onlineResource>
                                <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL>Klik om te bewerken.sdfgs</gmd:URL>
                                    </gmd:linkage>
                                </gmd:CI_OnlineResource>
                            </gmd:onlineResource>
                        </gmd:CI_Contact>
                    </gmd:contactInfo>
                </gmd:CI_ResponsibleParty>
            </gmd:pointOfContact>
            <gmd:descriptiveKeywords>
                <gmd:MD_Keywords>
                    <gmd:keyword>
                        <gco:CharacterString>fddhgh</gco:CharacterString>
                    </gmd:keyword>
                </gmd:MD_Keywords>
            </gmd:descriptiveKeywords>
            <gmd:resourceConstraints>
                <gmd:MD_Constraints>
                    <gmd:useLimitation>
                        <gco:CharacterString>Klik om te bewersghsdfgken.</gco:CharacterString>
                    </gmd:useLimitation>
                </gmd:MD_Constraints>
            </gmd:resourceConstraints>
            <gmd:resourceConstraints>
                <gmd:MD_LegalConstraints>
                    <gmd:accessConstraints>
                        <gmd:MD_RestrictionCode
                            codeList="#MD_RestrictionCode" codeListValue="patentPending">patent in wording</gmd:MD_RestrictionCode>
                    </gmd:accessConstraints>
                </gmd:MD_LegalConstraints>
            </gmd:resourceConstraints>
            <gmd:spatialRepresentationType>
                <gmd:MD_SpatialRepresentationTypeCode
                    codeList="#MD_SpatialRepresentationTypeCode" codeListValue="vector">vector</gmd:MD_SpatialRepresentationTypeCode>
            </gmd:spatialRepresentationType>
            <gmd:spatialResolution>
                <gmd:MD_Resolution>
                    <gmd:equivalentScale>
                        <gmd:MD_RepresentativeFraction>
                            <gmd:denominator>
                                <gco:Integer>sgsdf</gco:Integer>
                            </gmd:denominator>
                        </gmd:MD_RepresentativeFraction>
                    </gmd:equivalentScale>
                </gmd:MD_Resolution>
            </gmd:spatialResolution>
            <gmd:language>
                <gco:CharacterString>nld</gco:CharacterString>
            </gmd:language>
            <gmd:topicCategory>
                <gmd:MD_TopicCategoryCode>economy</gmd:MD_TopicCategoryCode>
            </gmd:topicCategory>
            <gmd:extent>
                <gmd:EX_Extent>
                    <gmd:geographicElement>
                        <gmd:EX_GeographicBoundingBox>
                            <gmd:westBoundLongitude>
                                <gco:Decimal>3.3</gco:Decimal>
                            </gmd:westBoundLongitude>
                            <gmd:eastBoundLongitude>
                                <gco:Decimal>7.2</gco:Decimal>
                            </gmd:eastBoundLongitude>
                            <gmd:southBoundLatitude>
                                <gco:Decimal>50.5</gco:Decimal>
                            </gmd:southBoundLatitude>
                            <gmd:northBoundLatitude>
                                <gco:Decimal>53.5</gco:Decimal>
                            </gmd:northBoundLatitude>
                        </gmd:EX_GeographicBoundingBox>
                    </gmd:geographicElement>
                    <gmd:temporalElement>
                        <gmd:EX_TemporalExtent>
                            <gmd:extent>
                                <gml:TimePeriod>
                                    <gml:begin>
                                    <gml:TimeInstant gml:id="t11">
                                    <gml:timePosition>2008-12-31</gml:timePosition>
                                    </gml:TimeInstant>
                                    </gml:begin>
                                    <gml:end>
                                    <gml:TimeInstant gml:id="t12">
                                    <gml:timePosition>2008-12-31</gml:timePosition>
                                    </gml:TimeInstant>
                                    </gml:end>
                                </gml:TimePeriod>
                            </gmd:extent>
                        </gmd:EX_TemporalExtent>
                    </gmd:temporalElement>
                </gmd:EX_Extent>
            </gmd:extent>
        </gmd:MD_DataIdentification>
    </gmd:identificationInfo>
    <gmd:distributionInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_Distribution>
            <gmd:distributor>
                <gmd:MD_Distributor>
                    <gmd:distributorContact>
                        <gmd:CI_ResponsibleParty>
                            <gmd:organisationName>
                                <gco:CharacterString>glkl</gco:CharacterString>
                            </gmd:organisationName>
                            <gmd:role>
                                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="distributor">distributeur</gmd:CI_RoleCode>
                            </gmd:role>
                            <gmd:contactInfo>
                                <gmd:CI_Contact>
                                    <gmd:onlineResource>
                                    <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL>hjkl</gmd:URL>
                                    </gmd:linkage>
                                    </gmd:CI_OnlineResource>
                                    </gmd:onlineResource>
                                </gmd:CI_Contact>
                            </gmd:contactInfo>
                        </gmd:CI_ResponsibleParty>
                    </gmd:distributorContact>
                </gmd:MD_Distributor>
            </gmd:distributor>
        </gmd:MD_Distribution>
    </gmd:distributionInfo>
    <gmd:dataQualityInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:DQ_DataQuality>
            <gmd:scope>
                <gmd:DQ_Scope>
                    <gmd:level>
                        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
                    </gmd:level>
                </gmd:DQ_Scope>
            </gmd:scope>
            <gmd:lineage>
                <gmd:LI_Lineage>
                    <gmd:statement>
                        <gco:CharacterString>hfg</gco:CharacterString>
                    </gmd:statement>
                </gmd:LI_Lineage>
            </gmd:lineage>
        </gmd:DQ_DataQuality>
    </gmd:dataQualityInfo>
</MD_Metadata>
');
insert into layer (id, parentid, serviceproviderid, name, title, abstracts, queryable, cascaded, opaque, nosubsets, fixedwidth, fixedheight, scalehintmin, scalehintmax, metadata) values (1, 12, 1, 'nk_groen', 'Nieuwe Kaart - Groen', 'Nieuwe Kaart', '1', '1', '0', null, null, null, null, null, '<?xml version="1.0" encoding="UTF-8"?>
<MD_Metadata xmlns="http://www.isotc211.org/2005/gmd"
    xmlns:gco="http://www.isotc211.org/2005/gco"
    xmlns:gml="http://www.opengis.net/gml"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <gmd:fileIdentifier xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>1d15490f-97bd-42ca-b3ad-b143dba942df-cvl</gco:CharacterString>
    </gmd:fileIdentifier>
    <gmd:fileIdentifier xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString/>
    </gmd:fileIdentifier>
    <gmd:language xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>nld</gco:CharacterString>
    </gmd:language>
    <gmd:characterSet xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
    </gmd:characterSet>
    <gmd:hierarchyLevel xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
    </gmd:hierarchyLevel>
    <gmd:hierarchyLevelName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>geen niveau</gco:CharacterString>
    </gmd:hierarchyLevelName>
    <gmd:contact xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:CI_ResponsibleParty>
            <gmd:organisationName>
                <gco:CharacterString>b3</gco:CharacterString>
            </gmd:organisationName>
            <gmd:role>
                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="pointOfContact">contactpunt</gmd:CI_RoleCode>
            </gmd:role>
            <gmd:contactInfo>
                <gmd:CI_Contact>
                    <gmd:onlineResource>
                        <gmd:CI_OnlineResource>
                            <gmd:linkage>
                                <gmd:URL>www.b3.nl</gmd:URL>
                            </gmd:linkage>
                        </gmd:CI_OnlineResource>
                    </gmd:onlineResource>
                </gmd:CI_Contact>
            </gmd:contactInfo>
        </gmd:CI_ResponsibleParty>
    </gmd:contact>
    <gmd:dateStamp xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:Date>2008-12-20</gco:Date>
    </gmd:dateStamp>
    <gmd:metadataStandardName xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>ISO 19115:2003</gco:CharacterString>
    </gmd:metadataStandardName>
    <gmd:metadataStandardVersion xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gco:CharacterString>Nederlandse metadatastandaard voor geografie 1.1</gco:CharacterString>
    </gmd:metadataStandardVersion>
    <gmd:referenceSystemInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_ReferenceSystem>
            <gmd:referenceSystemIdentifier>
                <gmd:RS_Identifier>
                    <gmd:code>
                        <gco:CharacterString>28992</gco:CharacterString>
                    </gmd:code>
                    <gmd:codeSpace>
                        <gco:CharacterString>EPSG</gco:CharacterString>
                    </gmd:codeSpace>
                </gmd:RS_Identifier>
            </gmd:referenceSystemIdentifier>
        </gmd:MD_ReferenceSystem>
    </gmd:referenceSystemInfo>
    <gmd:identificationInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_DataIdentification>
            <gmd:characterSet>
                <gmd:MD_CharacterSetCode codeList="#MD_CharacterSetCode" codeListValue="utf8">utf8</gmd:MD_CharacterSetCode>
            </gmd:characterSet>
            <gmd:citation>
                <gmd:CI_Citation>
                    <gmd:title>
                        <gco:CharacterString>Nieuwe Kaart - Groen</gco:CharacterString>
                    </gmd:title>
                    <gmd:date>
                        <gmd:CI_Date>
                            <gmd:date>
                                <gco:Date>2008-12-31</gco:Date>
                            </gmd:date>
                            <gmd:dateType>
                                <gmd:CI_DateTypeCode
                                    codeList="#CI_DateTypeCode" codeListValue="creation">creatie</gmd:CI_DateTypeCode>
                            </gmd:dateType>
                        </gmd:CI_Date>
                    </gmd:date>
                </gmd:CI_Citation>
            </gmd:citation>
            <gmd:abstract>
                <gco:CharacterString>een samenvatiing</gco:CharacterString>
            </gmd:abstract>
            <gmd:status>
                <gmd:MD_ProgressCode codeList="#MD_ProgressCode" codeListValue="completed">compleet</gmd:MD_ProgressCode>
            </gmd:status>
            <gmd:pointOfContact>
                <gmd:CI_ResponsibleParty>
                    <gmd:organisationName>
                        <gco:CharacterString>b3p</gco:CharacterString>
                    </gmd:organisationName>
                    <gmd:role>
                        <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="resourceProvider">verstrekker</gmd:CI_RoleCode>
                    </gmd:role>
                    <gmd:contactInfo>
                        <gmd:CI_Contact>
                            <gmd:onlineResource>
                                <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL>www.b3p.nl</gmd:URL>
                                    </gmd:linkage>
                                </gmd:CI_OnlineResource>
                            </gmd:onlineResource>
                        </gmd:CI_Contact>
                    </gmd:contactInfo>
                </gmd:CI_ResponsibleParty>
            </gmd:pointOfContact>
            <gmd:descriptiveKeywords>
                <gmd:MD_Keywords>
                    <gmd:keyword>
                        <gco:CharacterString>trefwoord</gco:CharacterString>
                    </gmd:keyword>
                </gmd:MD_Keywords>
            </gmd:descriptiveKeywords>
            <gmd:resourceConstraints>
                <gmd:MD_Constraints>
                    <gmd:useLimitation>
                        <gco:CharacterString>beperking</gco:CharacterString>
                    </gmd:useLimitation>
                </gmd:MD_Constraints>
            </gmd:resourceConstraints>
            <gmd:resourceConstraints>
                <gmd:MD_LegalConstraints>
                    <gmd:accessConstraints>
                        <gmd:MD_RestrictionCode
                            codeList="#MD_RestrictionCode" codeListValue="patent">patent</gmd:MD_RestrictionCode>
                    </gmd:accessConstraints>
                </gmd:MD_LegalConstraints>
            </gmd:resourceConstraints>
            <gmd:spatialRepresentationType>
                <gmd:MD_SpatialRepresentationTypeCode
                    codeList="#MD_SpatialRepresentationTypeCode" codeListValue="vector">vector</gmd:MD_SpatialRepresentationTypeCode>
            </gmd:spatialRepresentationType>
            <gmd:spatialResolution>
                <gmd:MD_Resolution>
                    <gmd:equivalentScale>
                        <gmd:MD_RepresentativeFraction>
                            <gmd:denominator>
                                <gco:Integer>100000</gco:Integer>
                            </gmd:denominator>
                        </gmd:MD_RepresentativeFraction>
                    </gmd:equivalentScale>
                </gmd:MD_Resolution>
            </gmd:spatialResolution>
            <gmd:language>
                <gco:CharacterString>nld</gco:CharacterString>
            </gmd:language>
            <gmd:topicCategory>
                <gmd:MD_TopicCategoryCode>climatologyMeteorologyAtmosphere</gmd:MD_TopicCategoryCode>
            </gmd:topicCategory>
            <gmd:extent>
                <gmd:EX_Extent>
                    <gmd:geographicElement>
                        <gmd:EX_GeographicBoundingBox>
                            <gmd:westBoundLongitude>
                                <gco:Decimal>3.3</gco:Decimal>
                            </gmd:westBoundLongitude>
                            <gmd:eastBoundLongitude>
                                <gco:Decimal>7.2</gco:Decimal>
                            </gmd:eastBoundLongitude>
                            <gmd:southBoundLatitude>
                                <gco:Decimal>50.5</gco:Decimal>
                            </gmd:southBoundLatitude>
                            <gmd:northBoundLatitude>
                                <gco:Decimal>53.5</gco:Decimal>
                            </gmd:northBoundLatitude>
                        </gmd:EX_GeographicBoundingBox>
                    </gmd:geographicElement>
                    <gmd:temporalElement>
                        <gmd:EX_TemporalExtent>
                            <gmd:extent>
                                <gml:TimePeriod>
                                    <gml:begin>
                                    <gml:TimeInstant gml:id="t11">
                                    <gml:timePosition>2007-12-31</gml:timePosition>
                                    </gml:TimeInstant>
                                    </gml:begin>
                                    <gml:end>
                                    <gml:TimeInstant gml:id="t12">
                                    <gml:timePosition>2008-12-31</gml:timePosition>
                                    </gml:TimeInstant>
                                    </gml:end>
                                </gml:TimePeriod>
                            </gmd:extent>
                        </gmd:EX_TemporalExtent>
                    </gmd:temporalElement>
                </gmd:EX_Extent>
            </gmd:extent>
        </gmd:MD_DataIdentification>
    </gmd:identificationInfo>
    <gmd:distributionInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:MD_Distribution>
            <gmd:distributor>
                <gmd:MD_Distributor>
                    <gmd:distributorContact>
                        <gmd:CI_ResponsibleParty>
                            <gmd:organisationName>
                                <gco:CharacterString>b3partners</gco:CharacterString>
                            </gmd:organisationName>
                            <gmd:role>
                                <gmd:CI_RoleCode codeList="#CI_RoleCode" codeListValue="publisher">uitgever</gmd:CI_RoleCode>
                            </gmd:role>
                            <gmd:contactInfo>
                                <gmd:CI_Contact>
                                    <gmd:onlineResource>
                                    <gmd:CI_OnlineResource>
                                    <gmd:linkage>
                                    <gmd:URL>www.b3partners.nl</gmd:URL>
                                    </gmd:linkage>
                                    </gmd:CI_OnlineResource>
                                    </gmd:onlineResource>
                                </gmd:CI_Contact>
                            </gmd:contactInfo>
                        </gmd:CI_ResponsibleParty>
                    </gmd:distributorContact>
                </gmd:MD_Distributor>
            </gmd:distributor>
        </gmd:MD_Distribution>
    </gmd:distributionInfo>
    <gmd:dataQualityInfo xmlns:gmd="http://www.isotc211.org/2005/gmd">
        <gmd:DQ_DataQuality>
            <gmd:scope>
                <gmd:DQ_Scope>
                    <gmd:level>
                        <gmd:MD_ScopeCode codeList="#MD_ScopeCode" codeListValue="dataset">dataset</gmd:MD_ScopeCode>
                    </gmd:level>
                </gmd:DQ_Scope>
            </gmd:scope>
            <gmd:lineage>
                <gmd:LI_Lineage>
                    <gmd:statement>
                        <gco:CharacterString>beschrijving2</gco:CharacterString>
                    </gmd:statement>
                </gmd:LI_Lineage>
            </gmd:lineage>
        </gmd:DQ_DataQuality>
    </gmd:dataQualityInfo>
</MD_Metadata>
');
CREATE TABLE attribution ( id INTEGER NOT NULL, title CHARACTER VARYING(50) DEFAULT '0', attributionurl CHARACTER VARYING(4000) DEFAULT '0', logourl CHARACTER VARYING(4000), logowidth CHARACTER VARYING(50), logoheight CHARACTER VARYING(50), PRIMARY KEY (id) );
insert into attribution (id, title, attributionurl, logourl, logowidth, logoheight) values (12, null, 'http://www.kaart.nieuwekaart.nl/chameleon/images/logo_nkn.gif', null, '241', '70');
CREATE TABLE mon_serviceproviderrequest ( id INTEGER DEFAULT nextval('mon_serviceproviderrequest_id_seq'::regclass) NOT NULL, spr_clr_id INTEGER, spr_bytessend BIGINT, spr_bytesreceived BIGINT, spr_responsestatus INTEGER, spr_providerrequesturi CHARACTER VARYING(4000), spr_requestresponsetime BIGINT, spr_mssincerequeststart BIGINT, spr_exceptionclass CHARACTER VARYING(255), spr_exceptionmessage CHARACTER VARYING(4000), spr_wmsversion CHARACTER VARYING(255), spr_serviceproviderid INTEGER, spr_srs CHARACTER VARYING(255), spr_width INTEGER, spr_height INTEGER, spr_format CHARACTER VARYING(255), PRIMARY KEY (id) );
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (10, 18, 254, 7383, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_groen&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 1150, 3712, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (11, 30, 265, 6156, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_landbouw,nk_plan&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 770, 81, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (19, 48, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 4619, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (20, 50, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 880, 3838, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (21, 50, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 12, 3838, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (22, 51, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 706, 3908, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (23, 51, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 55, 3908, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (24, 52, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 479, 6390, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (25, 52, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 948, 6390, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (26, 53, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 342, 4279, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (27, 53, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 850, 4279, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (28, 54, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 597, 3515, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (29, 54, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 1, 3515, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (30, 55, 256, 2808, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 520, 7577, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (31, 55, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 2, 7577, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (32, 56, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 7803, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (33, 57, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 4033, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (34, 58, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 3403, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (35, 59, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 4273, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (36, 61, 249, 3267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 0, 4711, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (37, 62, 250, 8267, 200, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 0, 3560, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (39, 64, 257, 9677, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 1028, 3137, null, null, '1.1.1', 1, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (40, 64, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 165, 3137, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (41, 65, 257, 9677, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 611, 4153, null, null, '1.1.1', 1, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (42, 65, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 14, 4153, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (43, 66, 257, 9677, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 620, 3360, null, null, '1.1.1', 1, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (44, 66, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 166, 3360, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (48, 70, 257, 9677, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 824, 5377, null, null, '1.1.1', 1, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (49, 70, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 301, 5377, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (50, 71, 257, 9677, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 764, 23, null, null, '1.1.1', 1, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (51, 71, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', 377, 23, null, null, '1.1.1', null, null, 900, 1200, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (54, 77, 256, 2804, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 687, 1, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (55, 77, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 4, 1, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (56, 78, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 3, 0, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (57, 78, 256, 2804, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 546, 0, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (58, 79, 256, 2804, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 544, 2, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (59, 79, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 5, 2, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (60, 80, null, null, null, 'http://layering.kaartenbalie.nl/BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 4, 1, null, null, '1.1.1', null, null, 300, 400, 'image/gif');
insert into mon_serviceproviderrequest (id, spr_clr_id, spr_bytessend, spr_bytesreceived, spr_responsestatus, spr_providerrequesturi, spr_requestresponsetime, spr_mssincerequeststart, spr_exceptionclass, spr_exceptionmessage, spr_wmsversion, spr_serviceproviderid, spr_srs, spr_width, spr_height, spr_format) values (61, 80, 256, 2804, 200, 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', 548, 1, null, null, '1.1.1', 1, null, 300, 400, 'image/gif');
CREATE TABLE acc_transaction ( id INTEGER DEFAULT nextval('acc_transaction_id_seq'::regclass) NOT NULL, tra_creditalteration NUMERIC(12,2), tra_transactiondate TIMESTAMP WITHOUT TIME ZONE, tra_mutationdate TIMESTAMP WITHOUT TIME ZONE, tra_status INTEGER, tra_type INTEGER, tra_errormessage CHARACTER VARYING(255), tra_userid INTEGER, tra_description CHARACTER VARYING(32), tra_acc_id INTEGER, tra_billingamount NUMERIC(10,2), tra_txexchangerate INTEGER, PRIMARY KEY (id) );
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (1, 1000.00, '2008-11-30 13:38:55', '2008-11-30 13:38:55', 1, 2, null, 1, 'chris/Bibit/1:100', 1, 10.00, 100);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (2, 1.00, '2008-11-30 14:46:43', '2008-11-30 14:46:43', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (6, 1.00, '2008-11-30 15:53:06', '2008-11-30 15:53:06', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (7, 0.00, '2008-11-30 15:56:54', '2008-11-30 15:56:54', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (8, 0.00, '2008-12-06 10:15:36', '2008-12-06 10:15:36', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (9, 0.00, '2008-12-06 10:16:12', '2008-12-06 10:16:12', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (10, 0.00, '2008-12-06 10:17:26', '2008-12-06 10:17:26', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (11, 0.00, '2008-12-06 10:18:45', '2008-12-06 10:18:45', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (12, 0.00, '2008-12-06 10:19:36', '2008-12-06 10:19:36', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (13, 0.00, '2008-12-06 10:21:44', '2008-12-06 10:21:44', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (14, 0.00, '2008-12-06 10:22:43', '2008-12-06 10:22:43', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (15, 0.00, '2008-12-06 19:27:36', '2008-12-06 19:27:37', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (54, 0.20, '2009-01-03 17:07:47', '2009-01-03 17:07:49', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (55, 0.20, '2009-01-03 17:43:30', '2009-01-03 17:43:30', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (57, 0.00, '2009-01-04 11:20:32', null, 0, 1, null, null, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (59, 0.20, '2009-01-04 11:25:41', '2009-01-04 11:25:42', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (60, 0.30, '2009-01-04 11:27:37', '2009-01-04 11:27:37', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (61, 0.30, '2009-01-04 11:27:40', '2009-01-04 11:27:40', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (62, 0.30, '2009-01-04 11:27:42', '2009-01-04 11:27:42', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (40, 0.20, '2009-01-03 15:55:18', '2009-01-03 15:55:20', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (41, 0.20, '2009-01-03 15:56:48', '2009-01-03 15:56:49', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (42, 0.20, '2009-01-03 15:58:40', '2009-01-03 16:25:22', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (43, 0.00, '2009-01-03 16:32:20', null, 0, 1, null, null, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (44, 0.00, '2009-01-03 16:35:01', null, 0, 1, null, null, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (45, 0.20, '2009-01-03 16:52:51', '2009-01-03 16:52:55', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (46, 0.20, '2009-01-03 16:53:29', '2009-01-03 16:53:31', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (48, 0.20, '2009-01-03 17:00:39', '2009-01-03 17:00:42', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (49, 0.20, '2009-01-03 17:01:56', '2009-01-03 17:01:58', 1, 1, null, 1, null, 1, null, null);
insert into acc_transaction (id, tra_creditalteration, tra_transactiondate, tra_mutationdate, tra_status, tra_type, tra_errormessage, tra_userid, tra_description, tra_acc_id, tra_billingamount, tra_txexchangerate) values (50, 0.20, '2009-01-03 17:02:24', '2009-01-03 17:02:26', 1, 1, null, 1, null, 1, null, null);
CREATE TABLE identifier ( id INTEGER DEFAULT nextval('identifier_id_seq'::regclass) NOT NULL, layerid INTEGER NOT NULL, authorityname CHARACTER VARYING(50) DEFAULT '0' NOT NULL, authorityurl CHARACTER VARYING(50) DEFAULT '0' NOT NULL, PRIMARY KEY (id) );
CREATE TABLE userroles ( userid INTEGER NOT NULL, roleid INTEGER NOT NULL, PRIMARY KEY (userid, roleid) );
insert into userroles (userid, roleid) values (1, 4);
insert into userroles (userid, roleid) values (1, 2);
insert into userroles (userid, roleid) values (1, 3);
insert into userroles (userid, roleid) values (1, 1);
insert into userroles (userid, roleid) values (2, 3);
insert into userroles (userid, roleid) values (2, 4);
insert into userroles (userid, roleid) values (2, 2);
insert into userroles (userid, roleid) values (2, 1);
CREATE TABLE mon_requestoperation ( id INTEGER DEFAULT nextval('mon_requestoperation_id_seq'::regclass) NOT NULL, rqo_clr_id INTEGER, rqo_duration BIGINT, rqo_mssincerequeststart BIGINT, rqo_numberofimages INTEGER, rqo_datasize BIGINT, rqo_bytesreceivedfromuser INTEGER, rqo_bytessendtouser INTEGER, rqo_type INTEGER, PRIMARY KEY (id) );
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (41, 21, 162913, 12641, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (42, 21, 230, 162683, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (43, 22, 263749, 1785, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (44, 22, 91, 263658, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (45, 23, 236289, 2117, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (46, 23, 122, 236167, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (47, 24, 65389, 7188, null, null, 95, 366, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (48, 24, 0, 65388, null, 366, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (49, 25, 88, 105024, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (50, 25, 105112, 1693, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (51, 26, 84, 50485, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (52, 26, 50570, 5217, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (53, 27, 151, 294891, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (54, 27, 295042, 1660, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (35, 18, 0, 4955, null, 7383, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (36, 18, 4956, 3664, null, null, 290, 7383, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (37, 19, 133, 3913, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (38, 19, 4046, 3354, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (55, 28, 2011, 1971, null, null, 95, 383, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (56, 28, 0, 2011, null, 383, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (39, 20, 91, 4106, null, 37559, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (40, 20, 4197, 3748, null, null, 95, 37559, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (57, 29, 98, 19958, null, 50969, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (58, 29, 20057, 1915, null, null, 95, 50969, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (59, 30, 0, 1096, null, 6156, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (60, 30, 1097, 9, null, null, 305, 6156, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (61, 31, 664, 0, null, null, 95, 50930, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (62, 31, 116, 548, null, 50930, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (1, 1, 1196, 17, null, null, 95, 50930, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (2, 1, 224, 972, null, 50930, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (101, 48, 18, 90031, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (102, 48, 117586, 54, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (103, 48, 6, 99890, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (104, 49, 4349, 1, null, null, 314, 3320, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (105, 49, 1, 4348, null, 3320, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (106, 50, 69, 8158, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (107, 50, 0, 8349, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (108, 50, 888, 7270, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (109, 50, 8353, 8, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (110, 51, 209317, 1, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (111, 51, 778, 208368, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (112, 51, 4, 209146, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (113, 51, 3, 209312, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (114, 52, 8, 79750, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (115, 52, 79903, 49, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (116, 52, 957, 78793, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (117, 52, 0, 79900, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (118, 53, 0, 371224, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (119, 53, 856, 370212, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (120, 53, 15, 371068, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (121, 53, 371261, 70, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (122, 54, 111810, 1, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (123, 54, 608, 111017, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (124, 54, 3, 111625, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (125, 54, 0, 111808, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (126, 55, 3, 104752, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (127, 55, 9979, 96265, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (128, 55, 112976, 1, null, null, 313, 4641, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (129, 55, 1, 105954, null, 4641, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (130, 56, 0, 85633, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (131, 56, 7, 85548, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (132, 56, 85636, 1, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (133, 57, 9240, 48, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (134, 57, 1, 9105, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (135, 57, 0, 9230, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (136, 58, 0, 6355, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (137, 58, 6361, 0, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (138, 58, 4, 6247, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (139, 59, 4, 1618206, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (140, 59, 1618439, 1, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (141, 59, 1, 1618436, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (142, 60, 1, 7590, null, 3611, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (143, 60, 7591, 1, null, null, 292, 3611, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (144, 61, 1084090, 1, null, null, 292, 3267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (145, 61, 1, 1084088, null, 3267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (146, 61, 2, 1083995, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (147, 62, 13, 7233, 1, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (148, 62, 7635, 1, null, null, 293, 8267, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (149, 62, 0, 7628, null, 8267, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (153, 64, 2, 9974, null, 16760, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (154, 64, 9977, 0, null, null, 314, 16760, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (155, 64, 190, 9177, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (156, 64, 1055, 8122, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (157, 65, 181, 8707, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (158, 65, 611, 8096, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (159, 65, 9514, 1, null, null, 314, 16760, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (160, 65, 1, 9511, null, 16760, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (161, 66, 3, 8250, null, 16760, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (162, 66, 32, 7553, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (163, 66, 792, 6761, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (164, 66, 8255, 4, null, null, 314, 16760, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (174, 70, 33, 22806, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (175, 70, 23610, 1, null, null, 314, 16760, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (176, 70, 1141, 21665, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (177, 70, 1, 23607, null, 16760, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (178, 71, 771, 209, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (179, 71, 1876, 16, null, null, 314, 16760, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (180, 71, 2, 1867, null, 16760, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (181, 71, 241, 980, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (185, 73, 616, 0, null, null, 95, 50930, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (186, 73, 63, 553, null, 50930, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (187, 74, 114, 918, null, 50930, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (188, 74, 1032, 25, null, null, 95, 50930, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (189, 75, 776, 2, null, null, 292, 6427, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (190, 75, 1, 775, null, 6427, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (194, 77, 1039, 1, null, null, 313, 4614, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (195, 77, 6, 918, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (196, 77, 694, 223, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (197, 77, 1, 1036, null, 4614, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (198, 78, 695, 0, null, null, 313, 4614, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (199, 78, 1, 691, null, 4614, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (200, 78, 549, 18, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (201, 78, 7, 567, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (202, 79, 178, 567, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (203, 79, 0, 836, null, 4614, null, null, 3);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (204, 79, 546, 21, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (205, 79, 839, 2, null, null, 313, 4614, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (206, 80, 6, 565, 2, null, null, null, 4);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (207, 80, 682, 1, null, null, 313, 4614, 5);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (208, 80, 548, 17, null, null, null, null, 6);
insert into mon_requestoperation (id, rqo_clr_id, rqo_duration, rqo_mssincerequeststart, rqo_numberofimages, rqo_datasize, rqo_bytesreceivedfromuser, rqo_bytessendtouser, rqo_type) values (209, 80, 1, 680, null, 4614, null, null, 3);
CREATE TABLE userip ( userid INTEGER NOT NULL, ipaddress CHARACTER VARYING(45) NOT NULL, PRIMARY KEY (userid, ipaddress) );
insert into userip (userid, ipaddress) values (1, '0.0.0.0');
CREATE TABLE exceptions ( serviceproviderid INTEGER NOT NULL, format CHARACTER VARYING(50) NOT NULL, PRIMARY KEY (serviceproviderid, format) );
insert into exceptions (serviceproviderid, format) values (1, 'application/vnd.ogc.se_blank');
insert into exceptions (serviceproviderid, format) values (1, 'application/vnd.ogc.se_inimage');
insert into exceptions (serviceproviderid, format) values (1, 'application/vnd.ogc.se_xml');
insert into exceptions (serviceproviderid, format) values (2, 'application/vnd.ogc.se_blank');
insert into exceptions (serviceproviderid, format) values (2, 'application/vnd.ogc.se_inimage');
insert into exceptions (serviceproviderid, format) values (2, 'application/vnd.ogc.se_xml');
insert into exceptions (serviceproviderid, format) values (3, 'application/vnd.ogc.se_blank');
insert into exceptions (serviceproviderid, format) values (3, 'application/vnd.ogc.se_inimage');
insert into exceptions (serviceproviderid, format) values (3, 'application/vnd.ogc.se_xml');
CREATE TABLE acc_pricecomp ( id INTEGER DEFAULT nextval('acc_pricecomp_id_seq'::regclass) NOT NULL, prc_tra_id INTEGER, prc_serverproviderprefix CHARACTER VARYING(255), prc_layername CHARACTER VARYING(255), prc_calculationdate TIMESTAMP WITHOUT TIME ZONE, prc_plantype INTEGER, prc_service CHARACTER VARYING(255), prc_operation CHARACTER VARYING(255), lpr_units NUMERIC(5,2), prc_projection CHARACTER VARYING(255), prc_layerisfree BOOLEAN, prc_method INTEGER, prc_calculationtime BIGINT, lpr_layerprice NUMERIC(12,2), prc_scale NUMERIC(20,2), PRIMARY KEY (id) );
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (1, 2, 'nkn', 'nk_groen', '2008-11-30 14:46:43', 1, 'WMS', 'GetMap', 1.00, 'EPSG:25831', '1', 3, 110, 0.00, null);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (2, 2, 'nkn', 'nk_gemengd', '2008-11-30 14:46:43', 1, 'WMS', 'GetMap', 1.00, 'EPSG:25831', '0', 3, 95, 1.00, null);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (36, 6, 'nkn', 'nk_gemengd', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '0', 3, 4, 1.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (37, 6, 'nkn', 'nk_werken', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 24, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (38, 6, 'nkn', 'nk_water', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 27, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (39, 6, 'nkn', 'nk_voorziening', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 23, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (40, 6, 'nkn', 'nk_plan', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 24, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (41, 6, 'nkn', 'nk_landbouw', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (42, 6, 'nkn', 'nk_recreatie', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 67, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (43, 6, 'nkn', 'nk_lijn', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 23, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (44, 6, 'nkn', 'nk_groen', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 27, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (45, 6, 'nkn', 'nk_wonen', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 21, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (46, 6, 'nkn', 'nk_verkeer', '2008-11-30 15:53:06', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (47, 7, 'nkn', 'nk_landbouw', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 40, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (48, 7, 'nkn', 'nk_werken', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (49, 7, 'nkn', 'nk_wonen', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (50, 7, 'nkn', 'nk_water', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (51, 7, 'nkn', 'nk_verkeer', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 20, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (52, 7, 'nkn', 'nk_plan', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 17, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (53, 7, 'nkn', 'nk_recreatie', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 19, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (54, 7, 'nkn', 'nk_gemengd', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 114, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (55, 7, 'nkn', 'nk_groen', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 24, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (56, 7, 'nkn', 'nk_voorziening', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 24, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (57, 7, 'nkn', 'nk_lijn', '2008-11-30 15:56:54', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 19, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (58, 8, 'nkn', 'nk_lijn', '2008-12-06 10:15:36', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 21, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (59, 8, 'nkn', 'nk_groen', '2008-12-06 10:15:36', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 28, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (60, 8, 'nkn', 'nk_landbouw', '2008-12-06 10:15:36', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 21, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (61, 8, 'nkn', 'nk_gemengd', '2008-12-06 10:15:36', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 117, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (62, 9, 'nkn', 'nk_groen', '2008-12-06 10:16:12', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 32, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (63, 9, 'nkn', 'nk_lijn', '2008-12-06 10:16:12', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 23, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (64, 9, 'nkn', 'nk_landbouw', '2008-12-06 10:16:12', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 25, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (65, 9, 'nkn', 'nk_gemengd', '2008-12-06 10:16:12', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 22, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (66, 10, 'nkn', 'nk_groen', '2008-12-06 10:17:26', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 27, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (67, 10, 'nkn', 'nk_gemengd', '2008-12-06 10:17:26', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 23, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (68, 10, 'nkn', 'nk_landbouw', '2008-12-06 10:17:26', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 25, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (69, 10, 'nkn', 'nk_lijn', '2008-12-06 10:17:26', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 33, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (70, 11, 'nkn', 'nk_lijn', '2008-12-06 10:18:45', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 20, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (71, 11, 'nkn', 'nk_landbouw', '2008-12-06 10:18:45', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 19, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (72, 11, 'nkn', 'nk_groen', '2008-12-06 10:18:45', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 20, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (73, 11, 'nkn', 'nk_gemengd', '2008-12-06 10:18:45', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 19, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (74, 12, 'nkn', 'nk_groen', '2008-12-06 10:19:36', 1, 'WMS', 'GetMap', 1.00, 'EPSG:4326', '1', 3, 26, 0.00, 235744393742.10);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (75, 13, 'nkn', 'nk_groen', '2008-12-06 10:21:44', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 24, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (76, 14, 'nkn', 'nk_groen', '2008-12-06 10:22:43', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 22, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (77, 15, 'nkn', 'nk_plan', '2008-12-06 19:27:37', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 19, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (78, 15, 'nkn', 'nk_landbouw', '2008-12-06 19:27:37', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 24, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (79, null, 'b3p', 'allowwithdrawals', '2009-01-03 12:41:32', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 21, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (80, null, 'nkn', 'nk_gemengd', '2009-01-03 12:41:32', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 2, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (84, null, 'b3p', 'allowwithdrawals', '2009-01-03 13:44:34', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 21, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (81, null, 'b3p', 'allowwithdrawals', '2009-01-03 13:28:55', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 30, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (82, null, 'nkn', 'nk_gemengd', '2009-01-03 13:28:55', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 13, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (83, null, 'nkn', 'nk_gemengd', '2009-01-03 13:44:34', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 142, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (85, null, 'b3p', 'allowwithdrawals', '2009-01-03 13:50:13', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 25, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (86, null, 'nkn', 'nk_gemengd', '2009-01-03 13:50:13', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 104, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (87, null, 'b3p', 'allowwithdrawals', '2009-01-03 14:04:22', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 5, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (88, null, 'nkn', 'nk_gemengd', '2009-01-03 14:04:22', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 2, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (89, null, 'nkn', 'nk_gemengd', '2009-01-03 14:19:00', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 3, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (90, null, 'b3p', 'allowwithdrawals', '2009-01-03 14:19:00', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 10, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (91, 40, 'nkn', 'nk_gemengd', '2009-01-03 15:55:18', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 100, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (92, 41, 'nkn', 'nk_gemengd', '2009-01-03 15:56:48', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 3, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (93, 42, 'nkn', 'nk_gemengd', '2009-01-03 15:59:12', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 16, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (94, 45, 'nkn', 'nk_gemengd', '2009-01-03 16:52:53', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 7, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (95, 46, 'nkn', 'nk_gemengd', '2009-01-03 16:53:30', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 7, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (96, 48, 'b3p', 'allowwithdrawals', '2009-01-03 17:00:40', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 24, 0.00, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (97, 48, 'nkn', 'nk_gemengd', '2009-01-03 17:00:40', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 5, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (98, 49, 'nkn', 'nk_gemengd', '2009-01-03 17:01:57', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 11, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (99, 49, 'b3p', 'allowwithdrawals', '2009-01-03 17:01:57', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 9, 0.00, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (100, 50, 'b3p', 'allowwithdrawals', '2009-01-03 17:02:25', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 13, 0.00, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (101, 50, 'nkn', 'nk_gemengd', '2009-01-03 17:02:25', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 5, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (102, 54, 'b3p', 'allowwithdrawals', '2009-01-03 17:07:48', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 16, 0.00, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (103, 54, 'nkn', 'nk_gemengd', '2009-01-03 17:07:48', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 1, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (104, 55, 'b3p', 'allowwithdrawals', '2009-01-03 17:43:30', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 7, 0.00, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (105, 55, 'nkn', 'nk_gemengd', '2009-01-03 17:43:30', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 26, 0.20, 903819.88);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (106, 59, 'b3p', 'allowwithdrawals', '2009-01-04 11:25:41', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 195, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (107, 59, 'nkn', 'nk_gemengd', '2009-01-04 11:25:41', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 3, 0.20, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (108, 60, 'b3p', 'allowwithdrawals', '2009-01-04 11:27:37', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 2, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (109, 60, 'nkn', 'nk_gemengd', '2009-01-04 11:27:37', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 2, 0.30, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (110, 61, 'b3p', 'allowwithdrawals', '2009-01-04 11:27:40', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 3, 0.00, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (111, 61, 'nkn', 'nk_gemengd', '2009-01-04 11:27:40', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 6, 0.30, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (112, 62, 'nkn', 'nk_gemengd', '2009-01-04 11:27:42', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '0', 3, 2, 0.30, 2711459.63);
insert into acc_pricecomp (id, prc_tra_id, prc_serverproviderprefix, prc_layername, prc_calculationdate, prc_plantype, prc_service, prc_operation, lpr_units, prc_projection, prc_layerisfree, prc_method, prc_calculationtime, lpr_layerprice, prc_scale) values (113, 62, 'b3p', 'allowwithdrawals', '2009-01-04 11:27:42', 1, 'WMS', 'GetMap', 1.00, 'EPSG:28992', '1', 3, 2, 0.00, 2711459.63);
CREATE TABLE styledomainformat ( sdrid INTEGER NOT NULL, format CHARACTER VARYING(45) NOT NULL, PRIMARY KEY (sdrid, format) );
insert into styledomainformat (sdrid, format) values (1, 'image/png');
insert into styledomainformat (sdrid, format) values (2, 'image/png');
insert into styledomainformat (sdrid, format) values (3, 'image/png');
insert into styledomainformat (sdrid, format) values (4, 'image/png');
insert into styledomainformat (sdrid, format) values (5, 'image/png');
insert into styledomainformat (sdrid, format) values (6, 'image/png');
insert into styledomainformat (sdrid, format) values (7, 'image/png');
insert into styledomainformat (sdrid, format) values (8, 'image/png');
insert into styledomainformat (sdrid, format) values (9, 'image/png');
insert into styledomainformat (sdrid, format) values (10, 'image/png');
insert into styledomainformat (sdrid, format) values (11, 'image/png');
insert into styledomainformat (sdrid, format) values (12, 'image/png');
insert into styledomainformat (sdrid, format) values (13, 'image/png');
insert into styledomainformat (sdrid, format) values (14, 'image/png');
insert into styledomainformat (sdrid, format) values (15, 'image/png');
insert into styledomainformat (sdrid, format) values (16, 'image/png');
insert into styledomainformat (sdrid, format) values (17, 'image/png');
insert into styledomainformat (sdrid, format) values (18, 'image/png');
insert into styledomainformat (sdrid, format) values (19, 'image/png');
insert into styledomainformat (sdrid, format) values (20, 'image/png');
insert into styledomainformat (sdrid, format) values (21, 'image/png');
insert into styledomainformat (sdrid, format) values (22, 'image/png');
insert into styledomainformat (sdrid, format) values (23, 'image/png');
insert into styledomainformat (sdrid, format) values (24, 'image/png');
insert into styledomainformat (sdrid, format) values (25, 'image/png');
insert into styledomainformat (sdrid, format) values (26, 'image/png');
CREATE TABLE dimensions ( id INTEGER DEFAULT nextval('dimensions_id_seq'::regclass) NOT NULL, layerid INTEGER NOT NULL, dimensionsname CHARACTER VARYING(50), dimensionsunit CHARACTER VARYING(50), dimensionsunitsymbol CHARACTER VARYING(50), extentname CHARACTER VARYING(50), extentdefaults CHARACTER VARYING(50), extentnearestvalue CHARACTER VARYING(50), extentmultiplevalues CHARACTER VARYING(50), extentcurrent CHARACTER VARYING(50), PRIMARY KEY (id) );
CREATE TABLE roles ( id INTEGER DEFAULT nextval('roles_id_seq'::regclass) NOT NULL, role CHARACTER VARYING(45) NOT NULL, PRIMARY KEY (id) );
insert into roles (id, role) values (1, 'beheerder');
insert into roles (id, role) values (2, 'organisatiebeheerder');
insert into roles (id, role) values (3, 'themabeheerder');
insert into roles (id, role) values (4, 'gebruiker');
insert into roles (id, role) values (5, 'demogebruiker');
CREATE TABLE acc_account ( id INTEGER NOT NULL, acc_creditbalance NUMERIC(15,2), PRIMARY KEY (id) );
insert into acc_account (id, acc_creditbalance) values (1, 996.90);
CREATE TABLE wfs_organizationlayer ( organizationid INTEGER NOT NULL, wfslayerid INTEGER NOT NULL, PRIMARY KEY (organizationid, wfslayerid) );
CREATE TABLE servicedomainresource ( id INTEGER DEFAULT nextval('servicedomainresource_id_seq'::regclass) NOT NULL, serviceproviderid INTEGER NOT NULL, domain CHARACTER VARYING(50) DEFAULT '0' NOT NULL, geturl CHARACTER VARYING(4000) DEFAULT '0', posturl CHARACTER VARYING(4000), PRIMARY KEY (id) );
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (1, 1, 'GetLegendGraphic', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (2, 1, 'GetFeatureInfo', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (3, 1, 'GetMap', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (4, 1, 'GetCapabilities', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (5, 1, 'DescribeLayer', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (6, 2, 'GetFeatureInfo', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (7, 2, 'GetLegendGraphic', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (8, 2, 'DescribeLayer', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (9, 2, 'GetCapabilities', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (10, 2, 'GetStyles', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (11, 2, 'GetMap', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (12, 3, 'GetLegendGraphic', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (13, 3, 'GetStyles', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (14, 3, 'GetCapabilities', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (15, 3, 'GetFeatureInfo', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (16, 3, 'DescribeLayer', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
insert into servicedomainresource (id, serviceproviderid, domain, geturl, posturl) values (17, 3, 'GetMap', 'http://osm.kaartenbalie.nl/wms/mapserver?', 'http://osm.kaartenbalie.nl/wms/mapserver?');
CREATE TABLE servicedomainformat ( sdrid INTEGER NOT NULL, format CHARACTER VARYING(100) NOT NULL, PRIMARY KEY (sdrid, format) );
insert into servicedomainformat (sdrid, format) values (1, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (1, 'image/png');
insert into servicedomainformat (sdrid, format) values (1, 'image/gif');
insert into servicedomainformat (sdrid, format) values (1, 'image/png; mode=24bit');
insert into servicedomainformat (sdrid, format) values (2, 'text/plain');
insert into servicedomainformat (sdrid, format) values (2, 'application/vnd.ogc.gml');
insert into servicedomainformat (sdrid, format) values (2, 'text/xml');
insert into servicedomainformat (sdrid, format) values (3, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (3, 'image/png');
insert into servicedomainformat (sdrid, format) values (3, 'image/tiff');
insert into servicedomainformat (sdrid, format) values (3, 'image/gif');
insert into servicedomainformat (sdrid, format) values (3, 'image/png; mode=24bit');
insert into servicedomainformat (sdrid, format) values (4, 'application/vnd.ogc.wms_xml');
insert into servicedomainformat (sdrid, format) values (5, 'text/xml');
insert into servicedomainformat (sdrid, format) values (6, 'text/plain');
insert into servicedomainformat (sdrid, format) values (6, 'application/vnd.ogc.gml');
insert into servicedomainformat (sdrid, format) values (7, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (7, 'image/png');
insert into servicedomainformat (sdrid, format) values (7, 'image/wbmp');
insert into servicedomainformat (sdrid, format) values (7, 'image/gif');
insert into servicedomainformat (sdrid, format) values (7, 'image/png; mode=24bit');
insert into servicedomainformat (sdrid, format) values (8, 'text/xml');
insert into servicedomainformat (sdrid, format) values (9, 'application/vnd.ogc.wms_xml');
insert into servicedomainformat (sdrid, format) values (10, 'text/xml');
insert into servicedomainformat (sdrid, format) values (11, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (11, 'image/png');
insert into servicedomainformat (sdrid, format) values (11, 'image/tiff');
insert into servicedomainformat (sdrid, format) values (11, 'image/wbmp');
insert into servicedomainformat (sdrid, format) values (11, 'image/gif');
insert into servicedomainformat (sdrid, format) values (11, 'image/png; mode=24bit');
insert into servicedomainformat (sdrid, format) values (12, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (12, 'image/png');
insert into servicedomainformat (sdrid, format) values (12, 'image/wbmp');
insert into servicedomainformat (sdrid, format) values (12, 'image/gif');
insert into servicedomainformat (sdrid, format) values (12, 'image/png; mode=24bit');
insert into servicedomainformat (sdrid, format) values (13, 'text/xml');
insert into servicedomainformat (sdrid, format) values (14, 'application/vnd.ogc.wms_xml');
insert into servicedomainformat (sdrid, format) values (15, 'text/plain');
insert into servicedomainformat (sdrid, format) values (15, 'application/vnd.ogc.gml');
insert into servicedomainformat (sdrid, format) values (16, 'text/xml');
insert into servicedomainformat (sdrid, format) values (17, 'image/jpeg');
insert into servicedomainformat (sdrid, format) values (17, 'image/png');
insert into servicedomainformat (sdrid, format) values (17, 'image/tiff');
insert into servicedomainformat (sdrid, format) values (17, 'image/wbmp');
insert into servicedomainformat (sdrid, format) values (17, 'image/gif');
insert into servicedomainformat (sdrid, format) values (17, 'image/png; mode=24bit');
CREATE TABLE styledomainresource ( id INTEGER DEFAULT nextval('styledomainresource_id_seq'::regclass) NOT NULL, styleid INTEGER NOT NULL, domain CHARACTER VARYING(45) NOT NULL, url CHARACTER VARYING(4000) NOT NULL, width CHARACTER VARYING(45), height CHARACTER VARYING(45), PRIMARY KEY (id) );
insert into styledomainresource (id, styleid, domain, url, width, height) values (1, 1, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_groen&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (2, 2, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_werken&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (3, 3, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_recreatie&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (4, 4, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_water&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (5, 5, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_gemengd&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (6, 6, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_landbouw&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (7, 7, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_lijn&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (8, 8, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_voorziening&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (9, 9, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_wonen&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (10, 10, 'LegendURL', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=nk_verkeer&format=image/png', '18', '12');
insert into styledomainresource (id, styleid, domain, url, width, height) values (11, 11, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=streets0_30&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (12, 12, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=primaryroads50_400&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (13, 13, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=railways&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (14, 14, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=highways0_50&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (15, 15, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=highways50_4000&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (16, 16, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=roads0_50&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (17, 17, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=roads50_100&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (18, 18, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=streets30_70&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (19, 19, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=streets0_30&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (20, 20, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=primaryroads50_400&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (21, 21, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=railways&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (22, 22, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=highways0_50&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (23, 23, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=highways50_4000&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (24, 24, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=roads0_50&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (25, 25, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=roads50_100&format=image/png', '20', '10');
insert into styledomainresource (id, styleid, domain, url, width, height) values (26, 26, 'LegendURL', 'http://osm.kaartenbalie.nl/wms/mapserver?version=1.1.1&service=WMS&request=GetLegendGraphic&layer=streets30_70&format=image/png', '20', '10');
CREATE TABLE style ( id INTEGER DEFAULT nextval('style_id_seq'::regclass) NOT NULL, layerid INTEGER NOT NULL, name CHARACTER VARYING(50) DEFAULT '0' NOT NULL, title CHARACTER VARYING(255) DEFAULT '0' NOT NULL, abstracts TEXT, PRIMARY KEY (id) );
insert into style (id, layerid, name, title, abstracts) values (1, 1, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (2, 2, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (3, 3, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (4, 4, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (5, 5, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (6, 6, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (7, 7, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (8, 8, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (9, 9, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (10, 10, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (11, 14, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (12, 19, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (13, 20, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (14, 22, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (15, 24, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (16, 25, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (17, 26, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (18, 27, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (19, 29, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (20, 33, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (21, 34, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (22, 36, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (23, 38, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (24, 39, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (25, 40, 'default', 'default', null);
insert into style (id, layerid, name, title, abstracts) values (26, 41, 'default', 'default', null);
CREATE TABLE wfs_serviceprovider ( id INTEGER DEFAULT nextval('wfs_serviceprovider_id_seq'::regclass) NOT NULL, name CHARACTER VARYING(60) DEFAULT '0' NOT NULL, abbr CHARACTER VARYING(60) DEFAULT '0' NOT NULL, title CHARACTER VARYING(50) DEFAULT '0' NOT NULL, givenname CHARACTER VARYING(50) DEFAULT '0' NOT NULL, url CHARACTER VARYING(4000) DEFAULT '0' NOT NULL, updateddate TIMESTAMP WITHOUT TIME ZONE NOT NULL, wfsversion CHARACTER VARYING(50) DEFAULT '0' NOT NULL, PRIMARY KEY (id) );
CREATE TABLE layerdomainformat ( ldrid INTEGER NOT NULL, format CHARACTER VARYING(100) NOT NULL, PRIMARY KEY (ldrid, format) );
insert into layerdomainformat (ldrid, format) values (1, 'text/xml');
insert into layerdomainformat (ldrid, format) values (2, 'text/xml');
insert into layerdomainformat (ldrid, format) values (3, 'text/xml');
insert into layerdomainformat (ldrid, format) values (4, 'text/xml');
insert into layerdomainformat (ldrid, format) values (5, 'text/xml');
insert into layerdomainformat (ldrid, format) values (6, 'text/xml');
insert into layerdomainformat (ldrid, format) values (7, 'text/xml');
insert into layerdomainformat (ldrid, format) values (8, 'text/xml');
insert into layerdomainformat (ldrid, format) values (9, 'text/xml');
insert into layerdomainformat (ldrid, format) values (10, 'text/xml');
insert into layerdomainformat (ldrid, format) values (11, 'text/xml');
CREATE TABLE layerdomainresource ( id INTEGER DEFAULT nextval('layerdomainresource_id_seq'::regclass) NOT NULL, layerid INTEGER NOT NULL, domain CHARACTER VARYING(50) DEFAULT '0' NOT NULL, url CHARACTER VARYING(4000) NOT NULL, PRIMARY KEY (id) );
insert into layerdomainresource (id, layerid, domain, url) values (1, 1, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (2, 2, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (3, 3, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (4, 4, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (5, 5, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (6, 6, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (7, 7, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (8, 8, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (9, 9, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (10, 10, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
insert into layerdomainresource (id, layerid, domain, url) values (11, 11, 'MetadataURL', 'http://www.kaart.nieuwekaart.nl/chameleon/metadata/nk_metadata.xml');
CREATE TABLE serviceproviderkeywordlist ( serviceproviderid INTEGER NOT NULL, keyword CHARACTER VARYING(50) NOT NULL, PRIMARY KEY (serviceproviderid, keyword) );
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'water');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'kaarten');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'Ruimtelijke ontwikkelingen');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'groen');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'toekomst');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'de Nieuwe Kaart van Nederland');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'wandkaart');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'recreatie');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'infrastructuur');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'wonen');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'Nederland');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'voorzieningen');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'Plannendatabase');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'werken');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'gis-bestand');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'Ruimtelijke Ordening');
insert into serviceproviderkeywordlist (serviceproviderid, keyword) values (1, 'natuur');
CREATE TABLE layerkeywordlist ( layerid INTEGER NOT NULL, keyword CHARACTER VARYING(50) NOT NULL, PRIMARY KEY (layerid, keyword) );
CREATE TABLE users ( id INTEGER DEFAULT nextval('users_id_seq'::regclass) NOT NULL, organizationid INTEGER NOT NULL, firstname CHARACTER VARYING(50) DEFAULT '0' NOT NULL, lastname CHARACTER VARYING(50) DEFAULT '0' NOT NULL, emailaddress CHARACTER VARYING(50) DEFAULT '0' NOT NULL, username CHARACTER VARYING(50) DEFAULT '0' NOT NULL, password CHARACTER VARYING(50) DEFAULT '0' NOT NULL, personalurl CHARACTER VARYING(4000), timeout TIMESTAMP WITHOUT TIME ZONE, defaultgetmap CHARACTER VARYING(4000), PRIMARY KEY (id) );
insert into users (id, organizationid, firstname, lastname, emailaddress, username, password, personalurl, timeout, defaultgetmap) values (2, 2, 'gemeente', 'gemeente', 'gem@gem.nl', 'gemeente', 'dP%2F2JJiEKq45ZKuBhsVveA%3D%3D', 'http://localhost:8084/kaartenbalie/services/96269d59d4405ee8859bdb4fdbbbe861', '2009-03-18 21:19:26', null);
insert into users (id, organizationid, firstname, lastname, emailaddress, username, password, personalurl, timeout, defaultgetmap) values (1, 1, 'beheerder', 'beheerder', 'info@b3partners.nl', 'beheerder', 'JMzUf6QkCdc%3D', 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff', '2009-02-05 00:00:00', 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&LAYERS=nkn_nk_gemengd&BBOX=12000,304000,280000,620000&SRS=EPSG:28992&HEIGHT=400&WIDTH=300&FORMAT=image/gif&BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&STYLES=');
CREATE TABLE organizationlayer ( organizationid INTEGER NOT NULL, layerid INTEGER NOT NULL, PRIMARY KEY (organizationid, layerid) );
insert into organizationlayer (organizationid, layerid) values (1, 41);
insert into organizationlayer (organizationid, layerid) values (1, 37);
insert into organizationlayer (organizationid, layerid) values (1, 10);
insert into organizationlayer (organizationid, layerid) values (1, 5);
insert into organizationlayer (organizationid, layerid) values (1, 11);
insert into organizationlayer (organizationid, layerid) values (1, 4);
insert into organizationlayer (organizationid, layerid) values (1, 12);
insert into organizationlayer (organizationid, layerid) values (1, 1);
insert into organizationlayer (organizationid, layerid) values (1, 32);
insert into organizationlayer (organizationid, layerid) values (1, 3);
insert into organizationlayer (organizationid, layerid) values (1, 20);
insert into organizationlayer (organizationid, layerid) values (1, 15);
insert into organizationlayer (organizationid, layerid) values (1, 24);
insert into organizationlayer (organizationid, layerid) values (1, 34);
insert into organizationlayer (organizationid, layerid) values (1, 35);
insert into organizationlayer (organizationid, layerid) values (1, 7);
insert into organizationlayer (organizationid, layerid) values (1, 31);
insert into organizationlayer (organizationid, layerid) values (1, 33);
insert into organizationlayer (organizationid, layerid) values (1, 30);
insert into organizationlayer (organizationid, layerid) values (1, 38);
insert into organizationlayer (organizationid, layerid) values (1, 2);
insert into organizationlayer (organizationid, layerid) values (1, 8);
insert into organizationlayer (organizationid, layerid) values (1, 39);
insert into organizationlayer (organizationid, layerid) values (1, 6);
insert into organizationlayer (organizationid, layerid) values (1, 29);
insert into organizationlayer (organizationid, layerid) values (1, 28);
insert into organizationlayer (organizationid, layerid) values (1, 36);
insert into organizationlayer (organizationid, layerid) values (1, 23);
insert into organizationlayer (organizationid, layerid) values (1, 40);
insert into organizationlayer (organizationid, layerid) values (1, 9);
insert into organizationlayer (organizationid, layerid) values (2, 1);
insert into organizationlayer (organizationid, layerid) values (2, 5);
CREATE TABLE rep_report ( id INTEGER DEFAULT nextval('rep_report_id_seq'::regclass) NOT NULL, rpd_org_id INTEGER, rep_reportdate TIMESTAMP WITHOUT TIME ZONE, rep_processingtime BIGINT, rep_startdate TIMESTAMP WITHOUT TIME ZONE, rep_enddate TIMESTAMP WITHOUT TIME ZONE, rep_organizationid INTEGER, PRIMARY KEY (id) );
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (1, 1, '2008-11-30 13:32:38', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (2, 1, '2008-11-30 13:32:49', null, '2008-11-12 00:00:00', '2008-11-28 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (3, 1, '2008-11-30 15:57:47', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (4, 1, '2008-11-30 16:15:24', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (5, 1, '2008-11-30 16:17:28', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (6, 1, '2008-11-30 16:18:56', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (7, 1, '2008-11-30 16:38:06', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (8, 1, '2008-11-30 16:40:20', null, '2008-10-01 00:00:00', '2008-11-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (9, 1, '2008-12-02 21:37:55', null, '2008-11-01 00:00:00', '2008-12-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (10, 1, '2008-12-02 21:43:25', 800, '2008-11-01 00:00:00', '2008-12-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (11, 1, '2008-12-02 21:43:33', 567, '2008-11-01 00:00:00', '2008-12-01 23:59:59', 1);
insert into rep_report (id, rpd_org_id, rep_reportdate, rep_processingtime, rep_startdate, rep_enddate, rep_organizationid) values (12, 1, '2008-12-06 18:44:28', 1142, '2008-11-01 00:00:00', '2008-12-01 23:59:59', 1);
CREATE TABLE serviceprovider ( id INTEGER DEFAULT nextval('serviceprovider_id_seq'::regclass) NOT NULL, name CHARACTER VARYING(60) DEFAULT '0' NOT NULL, abbr CHARACTER VARYING(60) DEFAULT '0' NOT NULL, title CHARACTER VARYING(255) DEFAULT '0' NOT NULL, abstracts TEXT, fees TEXT, accessconstraints TEXT, givenname CHARACTER VARYING(50) DEFAULT '0' NOT NULL, url CHARACTER VARYING(4000) DEFAULT '0' NOT NULL, updateddate TIMESTAMP WITHOUT TIME ZONE NOT NULL, wmsversion CHARACTER VARYING(50) DEFAULT '0' NOT NULL, PRIMARY KEY (id) );
insert into serviceprovider (id, name, abbr, title, abstracts, fees, accessconstraints, givenname, url, updateddate, wmsversion) values (1, 'OGC:WMS', 'nkn', 'de Nieuwe Kaart van Nederland', 'De Nieuwe Kaart van Nederland is een totaaloverzicht van nieuwe ruimtelijke ontwikkelingen', 'none', 'CC Creative Commons Licentie - NIROV', 'Nieuwe Kaart Nederland', 'http://webservice.nieuwekaart.nl/cgi-bin/nkn?', '2008-11-30 13:36:47', '1.1.1');
insert into serviceprovider (id, name, abbr, title, abstracts, fees, accessconstraints, givenname, url, updateddate, wmsversion) values (2, 'OGC:WMS', 'osmsc', 'OpenStreetMap2', 'Controlled access to OGC WMS maps. See B3P GIS at http://www.b3partners.nl/', 'none', 'none', 'osm scaleed', 'http://osm.kaartenbalie.nl/wms/mapserver-scale?', '2008-12-06 10:29:18', '1.1.1');
insert into serviceprovider (id, name, abbr, title, abstracts, fees, accessconstraints, givenname, url, updateddate, wmsversion) values (3, 'OGC:WMS', 'osm', 'OpenStreetMap', 'Controlled access to OGC WMS maps. See B3P GIS at http://www.b3partners.nl/', 'none', 'none', 'osm', 'http://osm.kaartenbalie.nl/wms/mapserver?', '2008-12-06 10:29:37', '1.1.1');
CREATE TABLE organization ( id INTEGER DEFAULT nextval('organization_id_seq'::regclass) NOT NULL, name CHARACTER VARYING(50) DEFAULT '0' NOT NULL, street CHARACTER VARYING(50) DEFAULT '0', number CHARACTER VARYING(5) DEFAULT '0', addition CHARACTER VARYING(10), postalcode CHARACTER VARYING(45), province CHARACTER VARYING(50) DEFAULT '0', country CHARACTER VARYING(50) DEFAULT '0', postbox CHARACTER VARYING(50), billingaddress CHARACTER VARYING(50), visitorsaddress CHARACTER VARYING(50), telephone CHARACTER VARYING(50) DEFAULT '0' NOT NULL, fax CHARACTER VARYING(50), hasvalidgetcapabilities BOOLEAN NOT NULL, bbox CHARACTER VARYING(50), code CHARACTER VARYING(50), allowaccountinglayers BOOLEAN, PRIMARY KEY (id) );
insert into organization (id, name, street, number, addition, postalcode, province, country, postbox, billingaddress, visitorsaddress, telephone, fax, hasvalidgetcapabilities, bbox, code, allowaccountinglayers) values (1, 'Beheerders', 'jfk', '146', null, '3931xm', 'woudenberg', null, null, null, null, '1234567890', null, '1', null, null, '1');
insert into organization (id, name, street, number, addition, postalcode, province, country, postbox, billingaddress, visitorsaddress, telephone, fax, hasvalidgetcapabilities, bbox, code, allowaccountinglayers) values (2, 'gemeente', null, null, null, null, null, null, null, null, null, '0123456789', null, '1', null, null, '0');
CREATE TABLE contactinformation ( id INTEGER NOT NULL, contactperson CHARACTER VARYING(50), contactposition CHARACTER VARYING(50), address CHARACTER VARYING(50), addresstype CHARACTER VARYING(50), postcode CHARACTER VARYING(50), city CHARACTER VARYING(50), stateorprovince CHARACTER VARYING(50), country CHARACTER VARYING(50), voicetelephone CHARACTER VARYING(50), fascimiletelephone CHARACTER VARYING(50), emailaddress CHARACTER VARYING(50), PRIMARY KEY (id) );
insert into contactinformation (id, contactperson, contactposition, address, addresstype, postcode, city, stateorprovince, country, voicetelephone, fascimiletelephone, emailaddress) values (1, 'Manon van Heusden', 'Teamleider De Nieuwe Kaart van Nederland', 'Mauritskade 23', 'Bezoekadres', '2514 HD', 'Den Haag', 'Zuid-Holland', 'Nederland', '070-3028494', '', 'info@nieuwekaart.nl');
insert into contactinformation (id, contactperson, contactposition, address, addresstype, postcode, city, stateorprovince, country, voicetelephone, fascimiletelephone, emailaddress) values (2, 'Chris van Lith', 'Partner', 'Zonnebaan 12C', 'Postal', '3542 EC', 'Utrecht', 'Utrecht', 'Nederland', '+31 30 214 20 81', '', 'info@b3partners.nl');
insert into contactinformation (id, contactperson, contactposition, address, addresstype, postcode, city, stateorprovince, country, voicetelephone, fascimiletelephone, emailaddress) values (3, 'Chris van Lith', 'Partner', 'Zonnebaan 12C', 'Postal', '3542 EC', 'Utrecht', 'Utrecht', 'Nederland', '+31 30 214 20 81', '', 'info@b3partners.nl');
CREATE TABLE mon_clientrequest ( id INTEGER DEFAULT nextval('mon_clientrequest_id_seq'::regclass) NOT NULL, clr_clientrequesturi CHARACTER VARYING(4000), clr_timestamp TIMESTAMP WITHOUT TIME ZONE, clr_userid INTEGER, clr_method CHARACTER VARYING(255), clr_clientip CHARACTER VARYING(255), clr_service CHARACTER VARYING(255), clr_operation CHARACTER VARYING(255), clr_organizationid INTEGER, clr_exceptionclass CHARACTER VARYING(255), clr_exceptionmessage CHARACTER VARYING(4000), PRIMARY KEY (id) );
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (18, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_groen&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2008-12-06 10:22:43', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (19, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:30:19', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (20, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:31:24', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (21, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:37:44', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (22, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:40:39', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (23, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:45:09', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (24, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:53:27', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, 'java.lang.ClassCastException', 'nl.b3p.wms.capabilities.Layer cannot be cast to java.lang.Comparable');
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (25, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 10:57:16', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (26, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 11:00:16', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (27, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 11:01:09', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (28, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 11:21:11', null, 'GET', '0:0:0:0:0:0:0:1', 'WMS', null, null, 'nl.b3p.kaartenbalie.service.AccessDeniedException', 'Personal URL not found! Authorisation required for this service!');
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (29, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 11:21:39', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (30, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_landbouw,nkn_nk_plan&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2008-12-06 19:27:36', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (31, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2008-12-06 19:28:21', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (1, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2009-01-02 20:23:52', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (48, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 12:36:01', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (49, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allow_withdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 12:40:05', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, 'java.lang.Exception', 'Trying to fetch unregistered or non-existing configLayer '' allow_withdrawals''.');
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (50, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 12:41:27', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (51, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 13:28:49', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (52, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 13:44:22', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (53, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 13:50:07', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (54, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 14:02:45', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (55, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 14:18:25', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (56, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 14:21:01', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (57, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 15:55:12', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (58, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 15:56:43', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (59, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 15:58:24', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (60, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 16:32:12', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, 'java.lang.Exception', 'Kaartenbalie GetMap fout: Ongeldige kaartlaag in de LAYERS parameter, update uw service providers in Kaartenbalie.');
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (61, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-03 16:34:51', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (62, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 16:53:24', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (64, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 17:00:34', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (65, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 17:01:50', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (66, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 17:02:19', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (70, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 17:07:28', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (71, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=1200&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=900&SRS=EPSG:28992&', '2009-01-03 17:43:30', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (73, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2009-01-03 17:43:58', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (74, 'http://localhost:8084/kaartenbalie/services/?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities&', '2009-01-04 11:20:10', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetCapabilities', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (75, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-04 11:20:32', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, 'java.lang.IllegalArgumentException', 'org.hibernate.QueryException: could not resolve property: indexCount of: nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing [FROM nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing AS lp WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) AND lp.planType = :planType AND lp.layerName = :layerName AND lp.serverProviderPrefix = :serverProviderPrefix AND lp.creationDate <= :validationDate AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) AND (lp.validUntil >= :validationDate OR lp.validUntil IS null) AND (lp.service = :service) AND (lp.operation = :operation OR lp.operation IS null) ORDER BY lp.indexCount DESC]');
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (77, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-04 11:25:41', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (78, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-04 11:27:37', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (79, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-04 11:27:40', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
insert into mon_clientrequest (id, clr_clientrequesturi, clr_timestamp, clr_userid, clr_method, clr_clientip, clr_service, clr_operation, clr_organizationid, clr_exceptionclass, clr_exceptionmessage) values (80, 'http://localhost:8084/kaartenbalie/services/767fb15bd2204181ca71fc9bda8913ff?BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&BBOX=12000,304000,280000,620000&VERSION=1.1.1&FORMAT=image/gif&SERVICE=WMS&HEIGHT=400&LAYERS=nkn_nk_gemengd,b3p_allowwithdrawals&REQUEST=GetMap&STYLES=&WIDTH=300&SRS=EPSG:28992&', '2009-01-04 11:27:42', 1, 'GET', '0:0:0:0:0:0:0:1', 'WMS', 'GetMap', 1, null, null);
CREATE TABLE srs ( id INTEGER DEFAULT nextval('srs_id_seq'::regclass) NOT NULL, layerid INTEGER NOT NULL, srs CHARACTER VARYING(150), minx CHARACTER VARYING(50), maxx CHARACTER VARYING(50), miny CHARACTER VARYING(50), maxy CHARACTER VARYING(50), resx CHARACTER VARYING(50), resy CHARACTER VARYING(50), PRIMARY KEY (id) );
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (1, 1, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (2, 1, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (3, 1, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (4, 2, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (5, 2, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (6, 2, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (7, 3, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (8, 3, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (9, 3, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (10, 4, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (11, 4, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (12, 4, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (13, 5, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (14, 5, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (15, 5, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (16, 6, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (17, 6, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (18, 6, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (19, 7, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (20, 7, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (21, 7, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (22, 8, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (23, 8, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (24, 8, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (25, 9, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (26, 9, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (27, 9, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (28, 10, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (29, 10, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (30, 10, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (31, 11, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (32, 11, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (33, 11, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (34, 12, 'EPSG:28992', '0', '326118', '295608', '648000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (35, 12, null, '3.03512', '7.98455', '50.626', '53.8188', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (36, 12, 'EPSG:25831', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (37, 12, 'EPSG:3043', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (38, 12, 'EPSG:4258', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (39, 12, 'EPSG:54004', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (40, 12, 'EPSG:4326', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (41, 12, 'EPSG:3044', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (42, 12, 'EPSG:25832', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (43, 12, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (44, 13, 'EPSG:28992', '13558.4', '278090', '306867', '618986', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (45, 13, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (46, 13, null, '3.25384', '7.24474', '50.7352', '53.5581', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (47, 14, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (48, 14, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (49, 14, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (50, 15, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (51, 15, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (52, 15, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (53, 17, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (54, 17, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (55, 17, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (56, 18, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (57, 18, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (58, 18, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (59, 19, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (60, 19, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (61, 19, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (62, 20, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (63, 20, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (64, 20, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (65, 21, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (66, 21, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (67, 21, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (68, 22, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (69, 22, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (70, 22, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (71, 23, null, '3.25384', '7.24474', '50.7352', '53.5581', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (72, 23, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (73, 23, 'EPSG:28992', '13558.4', '278090', '306867', '618986', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (74, 24, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (75, 24, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (76, 24, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (77, 25, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (78, 25, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (79, 25, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (80, 26, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (81, 26, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (82, 26, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (83, 27, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (84, 27, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (85, 27, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (86, 16, 'EPSG:28992', '-400000', '625000', '225000', '950000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (87, 16, null, '-3.56558', '12.9826', '49.7555', '56.5301', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (88, 16, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (89, 28, null, '3.25384', '7.24474', '50.7352', '53.5581', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (90, 28, 'EPSG:28992', '13558.4', '278090', '306867', '618986', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (91, 28, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (92, 29, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (93, 29, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (94, 29, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (95, 30, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (96, 30, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (97, 30, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (98, 31, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (99, 31, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (100, 31, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (101, 32, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (102, 32, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (103, 32, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (104, 33, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (105, 33, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (106, 33, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (107, 34, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (108, 34, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (109, 34, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (110, 35, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (111, 35, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (112, 35, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (113, 36, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (114, 36, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (115, 36, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (116, 37, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (117, 37, null, '3.25384', '7.24474', '50.7352', '53.5581', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (118, 37, 'EPSG:28992', '13558.4', '278090', '306867', '618986', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (119, 38, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (120, 38, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (121, 38, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (122, 39, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (123, 39, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (124, 39, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (125, 40, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (126, 40, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (127, 40, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (128, 41, 'EPSG:28992', null, null, null, null, null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (129, 41, 'EPSG:28992', '-2.5e+07', '2.5e+07', '-2.5e+07', '2.5e+07', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (130, 41, null, '-179.156', '179.909', '-74.7705', '2.64457', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (131, 42, 'EPSG:28992', '-400000', '625000', '225000', '950000', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (132, 42, null, '-3.56558', '12.9826', '49.7555', '56.5301', null, null);
insert into srs (id, layerid, srs, minx, maxx, miny, maxy, resx, resy) values (133, 42, 'EPSG:28992', null, null, null, null, null, null);
CREATE TABLE acc_layerpricing ( id INTEGER DEFAULT nextval('acc_layerpricing_id_seq'::regclass) NOT NULL, lpr_layername CHARACTER VARYING(255), lpr_serverproviderprefix CHARACTER VARYING(255), lpr_plantype INTEGER, lpr_validfrom TIMESTAMP WITHOUT TIME ZONE, lpr_validuntil TIMESTAMP WITHOUT TIME ZONE, lpr_creationdate TIMESTAMP WITHOUT TIME ZONE, lpr_deletiondate TIMESTAMP WITHOUT TIME ZONE, lpr_layerisfree BOOLEAN, lpr_unitprice NUMERIC(9,2), lpr_service CHARACTER VARYING(255), lpr_operation CHARACTER VARYING(255), lpr_minscale NUMERIC(20,10), lpr_maxscale NUMERIC(20,10), lpr_projection CHARACTER VARYING(255), PRIMARY KEY (id), UNIQUE (lpr_creationdate) );
insert into acc_layerpricing (id, lpr_layername, lpr_serverproviderprefix, lpr_plantype, lpr_validfrom, lpr_validuntil, lpr_creationdate, lpr_deletiondate, lpr_layerisfree, lpr_unitprice, lpr_service, lpr_operation, lpr_minscale, lpr_maxscale, lpr_projection) values (1, 'nk_gemengd', 'nkn', 1, '2008-11-04 00:00:00', '2008-12-31 00:00:00', '2008-11-30 13:37:42', '2008-11-30 15:54:47', null, 1.00, 'WMS', 'GetMap', null, null, null);
insert into acc_layerpricing (id, lpr_layername, lpr_serverproviderprefix, lpr_plantype, lpr_validfrom, lpr_validuntil, lpr_creationdate, lpr_deletiondate, lpr_layerisfree, lpr_unitprice, lpr_service, lpr_operation, lpr_minscale, lpr_maxscale, lpr_projection) values (2, 'nk_gemengd', 'nkn', 1, '2008-11-05 00:00:00', '2008-11-15 00:00:00', '2008-11-30 15:55:00', null, null, 2.00, 'WMS', 'GetMap', null, null, null);
insert into acc_layerpricing (id, lpr_layername, lpr_serverproviderprefix, lpr_plantype, lpr_validfrom, lpr_validuntil, lpr_creationdate, lpr_deletiondate, lpr_layerisfree, lpr_unitprice, lpr_service, lpr_operation, lpr_minscale, lpr_maxscale, lpr_projection) values (3, 'nk_gemengd', 'nkn', 1, '2008-12-02 00:00:00', '2008-12-27 00:00:00', '2008-12-06 18:59:25', null, null, 12.00, 'WMS', 'GetMap', null, null, null);
insert into acc_layerpricing (id, lpr_layername, lpr_serverproviderprefix, lpr_plantype, lpr_validfrom, lpr_validuntil, lpr_creationdate, lpr_deletiondate, lpr_layerisfree, lpr_unitprice, lpr_service, lpr_operation, lpr_minscale, lpr_maxscale, lpr_projection) values (4, 'nk_gemengd', 'nkn', 1, '2008-12-01 00:00:00', '2009-01-31 00:00:00', '2008-12-06 19:26:18', null, null, 0.20, 'WMS', 'GetMap', null, null, null);
insert into acc_layerpricing (id, lpr_layername, lpr_serverproviderprefix, lpr_plantype, lpr_validfrom, lpr_validuntil, lpr_creationdate, lpr_deletiondate, lpr_layerisfree, lpr_unitprice, lpr_service, lpr_operation, lpr_minscale, lpr_maxscale, lpr_projection) values (5, 'nk_gemengd', 'nkn', 1, '2008-12-28 00:00:00', '2009-01-31 00:00:00', '2009-01-04 11:26:37', null, null, 0.30, 'WMS', 'GetMap', null, null, null);
CREATE TABLE wfs_layer ( id INTEGER DEFAULT nextval('wfs_layer_id_seq'::regclass) NOT NULL, wfsserviceproviderid INTEGER NOT NULL, name CHARACTER VARYING(200), title CHARACTER VARYING(255) DEFAULT '0' NOT NULL, metadata TEXT, PRIMARY KEY (id) );
ALTER TABLE layer ADD CONSTRAINT fk61fd5513501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE layer ADD CONSTRAINT fk61fd551f2bbfbee FOREIGN KEY (parentid) REFERENCES layer (id);
ALTER TABLE layer ADD CONSTRAINT 2200_17262_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE layer ADD CONSTRAINT 2200_17262_3_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE layer ADD CONSTRAINT 2200_17262_5_not_null CHECK (title IS NOT NULL);
ALTER TABLE attribution ADD CONSTRAINT fked87907facb075c4 FOREIGN KEY (id) REFERENCES layer (id);
ALTER TABLE attribution ADD CONSTRAINT 2200_17229_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE mon_serviceproviderrequest ADD CONSTRAINT fk712ceebc223516a7 FOREIGN KEY (spr_clr_id) REFERENCES mon_clientrequest (id);
ALTER TABLE mon_serviceproviderrequest ADD CONSTRAINT 2200_17299_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE acc_transaction ADD CONSTRAINT fkfc20f02025126df8 FOREIGN KEY (tra_acc_id) REFERENCES acc_account (id);
ALTER TABLE acc_transaction ADD CONSTRAINT 2200_17223_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE identifier ADD CONSTRAINT fk9f88aca9aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE identifier ADD CONSTRAINT 2200_17254_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE identifier ADD CONSTRAINT 2200_17254_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE identifier ADD CONSTRAINT 2200_17254_3_not_null CHECK (authorityname IS NOT NULL);
ALTER TABLE identifier ADD CONSTRAINT 2200_17254_4_not_null CHECK (authorityurl IS NOT NULL);
ALTER TABLE userroles ADD CONSTRAINT fk154649d26bcbfe49 FOREIGN KEY (userid) REFERENCES users (id);
ALTER TABLE userroles ADD CONSTRAINT fk154649d275e26a26 FOREIGN KEY (roleid) REFERENCES roles (id);
ALTER TABLE userroles ADD CONSTRAINT 2200_17403_1_not_null CHECK (userid IS NOT NULL);
ALTER TABLE userroles ADD CONSTRAINT 2200_17403_2_not_null CHECK (roleid IS NOT NULL);
ALTER TABLE mon_requestoperation ADD CONSTRAINT fkce03888b8a8c8dec FOREIGN KEY (rqo_clr_id) REFERENCES mon_clientrequest (id);
ALTER TABLE mon_requestoperation ADD CONSTRAINT 2200_17296_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE userip ADD CONSTRAINT fkce2b32326bcbfe49 FOREIGN KEY (userid) REFERENCES users (id);
ALTER TABLE userip ADD CONSTRAINT 2200_17400_1_not_null CHECK (userid IS NOT NULL);
ALTER TABLE userip ADD CONSTRAINT 2200_17400_2_not_null CHECK (ipaddress IS NOT NULL);
ALTER TABLE exceptions ADD CONSTRAINT fkb1aa3a043501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE exceptions ADD CONSTRAINT 2200_17249_1_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE exceptions ADD CONSTRAINT 2200_17249_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE acc_pricecomp ADD CONSTRAINT fk165d199a1c0bba79 FOREIGN KEY (prc_tra_id) REFERENCES acc_transaction (id);
ALTER TABLE acc_pricecomp ADD CONSTRAINT 2200_17217_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE styledomainformat ADD CONSTRAINT fk1ac945ecf79e2f77 FOREIGN KEY (sdrid) REFERENCES styledomainresource (id);
ALTER TABLE styledomainformat ADD CONSTRAINT 2200_17391_1_not_null CHECK (sdrid IS NOT NULL);
ALTER TABLE styledomainformat ADD CONSTRAINT 2200_17391_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE dimensions ADD CONSTRAINT fk18b23fcdaa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE dimensions ADD CONSTRAINT 2200_17246_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE dimensions ADD CONSTRAINT 2200_17246_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE roles ADD CONSTRAINT 2200_17350_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE roles ADD CONSTRAINT 2200_17350_2_not_null CHECK (role IS NOT NULL);
ALTER TABLE acc_account ADD CONSTRAINT fk7fe8986f435502a6 FOREIGN KEY (id) REFERENCES organization (id);
ALTER TABLE acc_account ADD CONSTRAINT 2200_17208_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE wfs_organizationlayer ADD CONSTRAINT fk49308c0311ca431d FOREIGN KEY (wfslayerid) REFERENCES wfs_layer (id);
ALTER TABLE wfs_organizationlayer ADD CONSTRAINT fk49308c03c136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE wfs_organizationlayer ADD CONSTRAINT 2200_17424_1_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE wfs_organizationlayer ADD CONSTRAINT 2200_17424_2_not_null CHECK (wfslayerid IS NOT NULL);
ALTER TABLE servicedomainresource ADD CONSTRAINT fk2b43fac73501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE servicedomainresource ADD CONSTRAINT 2200_17356_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE servicedomainresource ADD CONSTRAINT 2200_17356_2_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE servicedomainresource ADD CONSTRAINT 2200_17356_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE servicedomainformat ADD CONSTRAINT fkefd888f0c557cb7b FOREIGN KEY (sdrid) REFERENCES servicedomainresource (id);
ALTER TABLE servicedomainformat ADD CONSTRAINT 2200_17353_1_not_null CHECK (sdrid IS NOT NULL);
ALTER TABLE servicedomainformat ADD CONSTRAINT 2200_17353_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE styledomainresource ADD CONSTRAINT fk5cf968c33d563395 FOREIGN KEY (styleid) REFERENCES style (id);
ALTER TABLE styledomainresource ADD CONSTRAINT 2200_17394_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE styledomainresource ADD CONSTRAINT 2200_17394_2_not_null CHECK (styleid IS NOT NULL);
ALTER TABLE styledomainresource ADD CONSTRAINT 2200_17394_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE styledomainresource ADD CONSTRAINT 2200_17394_4_not_null CHECK (url IS NOT NULL);
ALTER TABLE style ADD CONSTRAINT fk68b1db1aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE style ADD CONSTRAINT 2200_17383_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE style ADD CONSTRAINT 2200_17383_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE style ADD CONSTRAINT 2200_17383_3_not_null CHECK (name IS NOT NULL);
ALTER TABLE style ADD CONSTRAINT 2200_17383_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_3_not_null CHECK (abbr IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_5_not_null CHECK (givenname IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_6_not_null CHECK (url IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_7_not_null CHECK (updateddate IS NOT NULL);
ALTER TABLE wfs_serviceprovider ADD CONSTRAINT 2200_17427_8_not_null CHECK (wfsversion IS NOT NULL);
ALTER TABLE layerdomainformat ADD CONSTRAINT fk17e78d8c25e66a90 FOREIGN KEY (ldrid) REFERENCES layerdomainresource (id);
ALTER TABLE layerdomainformat ADD CONSTRAINT 2200_17277_1_not_null CHECK (ldrid IS NOT NULL);
ALTER TABLE layerdomainformat ADD CONSTRAINT 2200_17277_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE layerdomainresource ADD CONSTRAINT fk8ba44863aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE layerdomainresource ADD CONSTRAINT 2200_17280_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE layerdomainresource ADD CONSTRAINT 2200_17280_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE layerdomainresource ADD CONSTRAINT 2200_17280_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE layerdomainresource ADD CONSTRAINT 2200_17280_4_not_null CHECK (url IS NOT NULL);
ALTER TABLE serviceproviderkeywordlist ADD CONSTRAINT fk9b8f46a13501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE serviceproviderkeywordlist ADD CONSTRAINT 2200_17376_1_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE serviceproviderkeywordlist ADD CONSTRAINT 2200_17376_2_not_null CHECK (keyword IS NOT NULL);
ALTER TABLE layerkeywordlist ADD CONSTRAINT fk30f31016aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE layerkeywordlist ADD CONSTRAINT 2200_17287_1_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE layerkeywordlist ADD CONSTRAINT 2200_17287_2_not_null CHECK (keyword IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT fk6a68e08c136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE users ADD CONSTRAINT 2200_17406_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_2_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_3_not_null CHECK (firstname IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_4_not_null CHECK (lastname IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_5_not_null CHECK (emailaddress IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_6_not_null CHECK (username IS NOT NULL);
ALTER TABLE users ADD CONSTRAINT 2200_17406_7_not_null CHECK (password IS NOT NULL);
ALTER TABLE organizationlayer ADD CONSTRAINT fkcdee2ffeaa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE organizationlayer ADD CONSTRAINT fkcdee2ffec136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE organizationlayer ADD CONSTRAINT 2200_17317_1_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE organizationlayer ADD CONSTRAINT 2200_17317_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE rep_report ADD CONSTRAINT fk23f41f16e340125a FOREIGN KEY (rpd_org_id) REFERENCES organization (id);
ALTER TABLE rep_report ADD CONSTRAINT 2200_17323_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_10_not_null CHECK (updateddate IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_11_not_null CHECK (wmsversion IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_3_not_null CHECK (abbr IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_8_not_null CHECK (givenname IS NOT NULL);
ALTER TABLE serviceprovider ADD CONSTRAINT 2200_17364_9_not_null CHECK (url IS NOT NULL);
ALTER TABLE organization ADD CONSTRAINT 2200_17305_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE organization ADD CONSTRAINT 2200_17305_12_not_null CHECK (telephone IS NOT NULL);
ALTER TABLE organization ADD CONSTRAINT 2200_17305_14_not_null CHECK (hasvalidgetcapabilities IS NOT NULL);
ALTER TABLE organization ADD CONSTRAINT 2200_17305_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE contactinformation ADD CONSTRAINT fk65b57d0c4250fe39 FOREIGN KEY (id) REFERENCES serviceprovider (id);
ALTER TABLE contactinformation ADD CONSTRAINT 2200_17240_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE mon_clientrequest ADD CONSTRAINT 2200_17290_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE srs ADD CONSTRAINT fk1bdf4aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE srs ADD CONSTRAINT 2200_17380_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE srs ADD CONSTRAINT 2200_17380_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE acc_layerpricing ADD CONSTRAINT 2200_17211_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE wfs_layer ADD CONSTRAINT fkbb3050d66a0442a7 FOREIGN KEY (wfsserviceproviderid) REFERENCES wfs_serviceprovider (id);
ALTER TABLE wfs_layer ADD CONSTRAINT 2200_17417_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE wfs_layer ADD CONSTRAINT 2200_17417_2_not_null CHECK (wfsserviceproviderid IS NOT NULL);
ALTER TABLE wfs_layer ADD CONSTRAINT 2200_17417_4_not_null CHECK (title IS NOT NULL);
