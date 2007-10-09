SET FOREIGN_KEY_CHECKS = 0;

--
-- Definition of table attribution
--
CREATE TABLE new_attribution (
  LAYERID int(11) NOT NULL default '0',
  TITLE varchar(50) default '0',
  ATTRIBUTIONURL varchar(150) default '0',
  LOGOURL varchar(50) default NULL,
  LOGOWIDTH varchar(50) default NULL,
  LOGOHEIGHT varchar(50) default NULL,
  PRIMARY KEY  (LAYERID),
  CONSTRAINT LAYERID FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

insert into new_attribution (
  LAYERID,
  TITLE,
  ATTRIBUTIONURL,
  LOGOURL,
  LOGOWIDTH,
  LOGOHEIGHT
	)
  select 
  LAYERID,
  TITLE,
  ATTRIBUTIONURL,
  LOGOURL,
  LOGOWIDTH,
  LOGOHEIGHT
  from attribution;

drop table attribution;
rename table new_attribution to attribution;


--
-- Definition of table contactinformation
--

CREATE TABLE new_contactinformation (
  SERVICEPROVIDERID int(11) NOT NULL default '0',
  CONTACTPERSON varchar(50) default NULL,
  CONTACTPOSITION varchar(50) default NULL,
  ADDRESS varchar(50) default NULL,
  ADDRESSTYPE varchar(50) default NULL,
  POSTCODE varchar(50) default NULL,
  CITY varchar(50) default NULL,
  STATEORPROVINCE varchar(50) default NULL,
  COUNTRY varchar(50) default NULL,
  VOICETELEPHONE varchar(50) default NULL,
  FASCIMILETELEPHONE varchar(50) default NULL,
  EMAILADDRESS varchar(50) default NULL,
  PRIMARY KEY  (SERVICEPROVIDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_contactinformation (
  SERVICEPROVIDERID,
  CONTACTPERSON,
  CONTACTPOSITION,
  ADDRESS,
  ADDRESSTYPE,
  POSTCODE,
  CITY,
  STATEORPROVINCE,
  COUNTRY,
  VOICETELEPHONE,
  FASCIMILETELEPHONE,
  EMAILADDRESS
	)
  select 
  SERVICEPROVIDERID,
  CONTACTPERSON,
  CONTACTPOSITION,
  ADDRESS,
  ADDRESSTYPE,
  POSTCODE,
  CITY,
  STATEORPROVINCE,
  COUNTRY,
  VOICETELEPHONE,
  FASCIMILETELEPHONE,
  EMAILADDRESS
  from contactinformation;

drop table contactinformation;
rename table new_contactinformation to contactinformation;

--
-- Definition of table dimensions
--

CREATE TABLE new_dimensions (
  DIMENSIONSID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  DIMENSIONSNAME varchar(50) default NULL,
  DIMENSIONSUNIT varchar(50) default NULL,
  DIMENSIONSUNITSYMBOL varchar(50) default NULL,
  EXTENTNAME varchar(50) default NULL,
  EXTENTDEFAULTS varchar(50) default NULL,
  EXTENTNEARESTVALUE varchar(50) default NULL,
  EXTENTMULTIPLEVALUES varchar(50) default NULL,
  EXTENTCURRENT varchar(50) default NULL,
  PRIMARY KEY  (DIMENSIONSID),
  KEY FK_Dimensions_1 (LAYERID),
  CONSTRAINT FK_Dimensions_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_dimensions (
  DIMENSIONSID,
  LAYERID,
  DIMENSIONSNAME,
  DIMENSIONSUNIT,
  DIMENSIONSUNITSYMBOL,
  EXTENTNAME,
  EXTENTDEFAULTS,
  EXTENTNEARESTVALUE,
  EXTENTMULTIPLEVALUES,
  EXTENTCURRENT
	)
  select 
  DIMENSIONSID,
  LAYERID,
  DIMENSIONSNAME,
  DIMENSIONSUNIT,
  DIMENSIONSUNITSYMBOL,
  EXTENTNAME,
  EXTENTDEFAULTS,
  EXTENTNEARESTVALUE,
  EXTENTMULTIPLEVALUES,
  EXTENTCURRENT
  from dimensions;

drop table dimensions;
rename table new_dimensions to dimensions;

--
-- Definition of table exceptions
--

CREATE TABLE new_exceptions (
  SERVICEPROVIDERID int(11) NOT NULL default '0',
  FORMAT varchar(50) NOT NULL default '0',
  PRIMARY KEY  (SERVICEPROVIDERID,FORMAT),
  CONSTRAINT FK_Exceptions_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_exceptions (
  SERVICEPROVIDERID,
  FORMAT
	)
  select 
  SERVICEPROVIDERID,
  FORMAT
  from exceptions;

drop table exceptions;
rename table new_exceptions to exceptions;

--
-- Definition of table identifier
--

CREATE TABLE new_identifier (
  IDENTIFIERID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  AUTHORITYNAME varchar(50) NOT NULL default '0',
  AUTHORITYURL varchar(50) NOT NULL default '0',
  PRIMARY KEY  (IDENTIFIERID),
  KEY FK_Identifier_1 (LAYERID),
  CONSTRAINT FK_Identifier_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_identifier (
  IDENTIFIERID,
  LAYERID,
  AUTHORITYNAME,
  AUTHORITYURL
	)
  select 
  IDENTIFIERID,
  LAYERID,
  AUTHORITYNAME,
  AUTHORITYURL
  from identifier;

drop table identifier;
rename table new_identifier to identifier;

--
-- Definition of table latlonboundingbox
--

CREATE TABLE new_latlonboundingbox (
  LAYERID int(11) NOT NULL default '0',
  MINX double default '0',
  MINY double default '0',
  MAXX double default '0',
  MAXY double default '0',
  PRIMARY KEY  (LAYERID),
  CONSTRAINT FK_LatLonBoundingBox_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_latlonboundingbox (
  LAYERID,
  MINX,
  MINY,
  MAXX,
  MAXY
	)
  select 
  LAYERID,
  MINX,
  MINY,
  MAXX,
  MAXY
  from latlonboundingbox;

drop table latlonboundingbox;
rename table new_latlonboundingbox to latlonboundingbox;

--
-- Definition of table layer
--

CREATE TABLE new_layer (
  LAYERID int(11) NOT NULL auto_increment,
  SERVICEPROVIDERID int(11) default '0',
  NAME varchar(50) default NULL,
  TITLE varchar(200) NOT NULL default '0',
  ABSTRACTS mediumtext,
  QUERYABLE varchar(50) default '0',
  CASCADED varchar(50) default '0',
  OPAQUE varchar(50) default '0',
  NOSUBSETS varchar(50) default '0',
  FIXEDWIDTH varchar(50) default '0',
  FIXEDHEIGHT varchar(50) default '0',
  SCALEHINTMIN varchar(50) default '0',
  SCALEHINTMAX varchar(50) default '0',
  PARENTID int(11) default NULL,
  PRIMARY KEY  (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

insert into new_layer (
  LAYERID,
  SERVICEPROVIDERID,
  NAME,
  TITLE,
  ABSTRACTS,
  QUERYABLE,
  CASCADED,
  OPAQUE,
  NOSUBSETS,
  FIXEDWIDTH,
  FIXEDHEIGHT,
  SCALEHINTMIN,
  SCALEHINTMAX,
  PARENTID
	)
  select 
  LAYERID,
  SERVICEPROVIDERID,
  NAME,
  TITLE,
  ABSTRACTS,
  QUERYABLE,
  CASCADED,
  OPAQUE,
  NOSUBSETS,
  FIXEDWIDTH,
  FIXEDHEIGHT,
  SCALEHINTMIN,
  SCALEHINTMAX,
  PARENTID
  from layer;

drop table layer;
rename table new_layer to layer;

--
-- Definition of table layerdomainformat
--

CREATE TABLE new_layerdomainformat (
  LDRID int(11) NOT NULL default '0',
  FORMAT varchar(100) NOT NULL default '',
  PRIMARY KEY  (LDRID,FORMAT),
  CONSTRAINT FK_LayerDomainFormat_1 FOREIGN KEY (LDRID) REFERENCES layerdomainresource (LDRID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 87040 kB';

insert into new_layerdomainformat (
  LDRID,
  FORMAT
	)
  select 
  LDRID,
  FORMAT
  from layerdomainformat;

drop table layerdomainformat;
rename table new_layerdomainformat to layerdomainformat;

--
-- Definition of table layerdomainresource
--

CREATE TABLE new_layerdomainresource (
  LDRID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  DOMAIN varchar(50) NOT NULL default '0',
  URL varchar(100) NOT NULL,
  PRIMARY KEY  (LDRID),
  KEY FK_LayerDomainResource_1 (LAYERID),
  CONSTRAINT FK_LayerDomainResource_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_layerdomainresource (
  LDRID,
  LAYERID,
  DOMAIN,
  URL
	)
  select 
  LDRID,
  LAYERID,
  DOMAIN,
  URL
  from layerdomainresource;

drop table layerdomainresource;
rename table new_layerdomainresource to layerdomainresource;

--
-- Definition of table layerkeywordlist
--

CREATE TABLE new_layerkeywordlist (
  KEYWORDLISTID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  KEYWORD varchar(50) NOT NULL default '0',
  PRIMARY KEY  (KEYWORDLISTID),
  KEY FK_LayerKeywordList_1 (LAYERID),
  CONSTRAINT FK_LayerKeywordList_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_layerkeywordlist (
  KEYWORDLISTID,
  LAYERID,
  KEYWORD
	)
  select 
  KEYWORDLISTID,
  LAYERID,
  KEYWORD
  from layerkeywordlist;

drop table layerkeywordlist;
rename table new_layerkeywordlist to layerkeywordlist;

--
-- Definition of table organization
--

CREATE TABLE new_organization (
  ORGANIZATIONID int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '0',
  STREET varchar(50) NOT NULL default '0',
  NUMBER smallint(5) NOT NULL default '0',
  ADDITION varchar(10) default NULL,
  PROVINCE varchar(50) NOT NULL default '0',
  COUNTRY varchar(50) NOT NULL default '0',
  POSTBOX varchar(50) default NULL,
  BILLINGADDRESS varchar(50) default NULL,
  VISITORSADDRESS varchar(50) default NULL,
  TELEPHONE varchar(50) NOT NULL default '0',
  FAX varchar(50) default NULL,
  POSTALCODE varchar(45) NOT NULL,
  HASVALIDGETCAPABILITIES tinyint(1) NOT NULL,
  PRIMARY KEY  (ORGANIZATIONID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_organization (
  ORGANIZATIONID,
  NAME,
  STREET,
  NUMBER,
  ADDITION,
  PROVINCE,
  COUNTRY,
  POSTBOX,
  BILLINGADDRESS,
  VISITORSADDRESS,
  TELEPHONE,
  FAX,
  POSTALCODE,
  HASVALIDGETCAPABILITIES
	)
  select 
  ORGANIZATIONID,
  NAME,
  STREET,
  NUMBER,
  ADDITION,
  PROVINCE,
  COUNTRY,
  POSTBOX,
  BILLINGADDRESS,
  VISITORSADDRESS,
  TELEPHONE,
  FAX,
  POSTALCODE,
  HASVALIDGETCAPABILITIES
  from organization;

drop table organization;
rename table new_organization to organization;

--
-- Definition of table organizationlayer
--

CREATE TABLE new_organizationlayer (
  ORGANIZATIONID int(11) NOT NULL default '0',
  LAYERID int(11) NOT NULL default '0',
  PRIMARY KEY  (ORGANIZATIONID,LAYERID),
  KEY FK_OrganizationLayer_2 (LAYERID),
  CONSTRAINT FK_OrganizationLayer_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID),
  CONSTRAINT FK_OrganizationLayer_2 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 87040 kB';

insert into new_organizationlayer (
  ORGANIZATIONID,
  LAYERID
	)
  select 
  ORGANIZATIONID,
  LAYERID
  from organizationlayer;

drop table organizationlayer;
rename table new_organizationlayer to organizationlayer;

--
-- Definition of table servicedomainformat
--

CREATE TABLE new_servicedomainformat (
  SDRID int(11) NOT NULL default '0',
  FORMAT varchar(100) NOT NULL default '0',
  PRIMARY KEY  (SDRID,FORMAT),
  CONSTRAINT FK_ServiceDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES servicedomainresource (SDRID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_servicedomainformat (
  SDRID,
  FORMAT
	)
  select 
  SDRID,
  FORMAT
  from servicedomainformat;

drop table servicedomainformat;
rename table new_servicedomainformat to servicedomainformat;

--
-- Definition of table servicedomainresource
--

CREATE TABLE new_servicedomainresource (
  SDRID int(11) NOT NULL auto_increment,
  SERVICEPROVIDERID int(11) NOT NULL default '0',
  DOMAIN varchar(50) NOT NULL default '0',
  GETURL varchar(100) default '0',
  POSTURL varchar(100) default NULL,
  PRIMARY KEY  (SDRID),
  KEY FK_RequestDomainResource_1 (SERVICEPROVIDERID),
  CONSTRAINT FK_RequestDomainResource_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_servicedomainresource (
  SDRID,
  SERVICEPROVIDERID,
  DOMAIN,
  GETURL,
  POSTURL
	)
  select 
  SDRID,
  SERVICEPROVIDERID,
  DOMAIN,
  GETURL,
  POSTURL
  from servicedomainresource;

drop table servicedomainresource;
rename table new_servicedomainresource to servicedomainresource;

--
-- Definition of table serviceprovider
--

CREATE TABLE new_serviceprovider (
  SERVICEPROVIDERID int(11) NOT NULL auto_increment,
  NAME varchar(60) NOT NULL default '0',
  TITLE varchar(50) NOT NULL default '0',
  ABSTRACTS varchar(200) default NULL,
  FEES varchar(50) default NULL,
  ACCESSCONSTRAINTS varchar(50) default NULL,
  GIVENNAME varchar(50) NOT NULL default '0',
  URL varchar(200) NOT NULL default '0',
  UPDATEDDATE datetime NOT NULL default '0000-00-00 00:00:00',
  REVIEWED tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (SERVICEPROVIDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_serviceprovider (
  SERVICEPROVIDERID,
  NAME,
  TITLE,
  ABSTRACTS,
  FEES,
  ACCESSCONSTRAINTS,
  GIVENNAME,
  URL,
  UPDATEDDATE,
  REVIEWED
	)
  select 
  SERVICEPROVIDERID,
  NAME,
  TITLE,
  ABSTRACTS,
  FEES,
  ACCESSCONSTRAINTS,
  GIVENNAME,
  URL,
  UPDATEDDATE,
  REVIEWED
  from serviceprovider;

drop table serviceprovider;
rename table new_serviceprovider to serviceprovider;

--
-- Definition of table serviceproviderkeywordlist
--

CREATE TABLE new_serviceproviderkeywordlist (
  SERVICEPROVIDERID int(11) NOT NULL default '0',
  KEYWORD varchar(50) NOT NULL default '0',
  PRIMARY KEY  (SERVICEPROVIDERID,KEYWORD),
  CONSTRAINT FK_ServiceProviderKeywordList_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_serviceproviderkeywordlist (
  SERVICEPROVIDERID,
  KEYWORD
	)
  select 
  SERVICEPROVIDERID,
  KEYWORD
  from serviceproviderkeywordlist;

drop table serviceproviderkeywordlist;
rename table new_serviceproviderkeywordlist to serviceproviderkeywordlist;

--
-- Definition of table srs
--

CREATE TABLE new_srs (
  SRSID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  SRS varchar(150) NOT NULL default '0',
  MINX double default NULL,
  MAXX double default NULL,
  MINY double default NULL,
  MAXY double default NULL,
  RESX double default NULL,
  RESY double default NULL,
  PRIMARY KEY  (SRSID),
  KEY FK_SRS_1 (LAYERID),
  CONSTRAINT FK_SRS_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_srs (
  SRSID,
  LAYERID,
  SRS,
  MINX,
  MAXX,
  MINY,
  MAXY,
  RESX,
  RESY
	)
  select 
  SRSID,
  LAYERID,
  SRS,
  MINX,
  MAXX,
  MINY,
  MAXY,
  RESX,
  RESY
  from srs;

drop table srs;
rename table new_srs to srs;

--
-- Definition of table style
--

CREATE TABLE new_style (
  STYLEID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL default '0',
  NAME varchar(50) NOT NULL default '0',
  TITLE varchar(50) NOT NULL default '0',
  ABSTRACTS varchar(50) default NULL,
  STYLEURL varchar(50) NOT NULL default '0',
  STYLESHEETURL varchar(50) default NULL,
  PRIMARY KEY  (STYLEID),
  KEY FK_Style_1 (LAYERID),
  CONSTRAINT FK_Style_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_style (
  STYLEID,
  LAYERID,
  NAME,
  TITLE,
  ABSTRACTS,
  STYLEURL,
  STYLESHEETURL,
	)
  select 
  STYLEID,
  LAYERID,
  NAME,
  TITLE,
  ABSTRACTS,
  STYLEURL,
  STYLESHEETURL,
  from style;

drop table style;
rename table new_style to style;

--
-- Definition of table styledomainformat
--

CREATE TABLE new_styledomainformat (
  SDRID int(11) NOT NULL auto_increment,
  FORMAT char(45) NOT NULL default '',
  PRIMARY KEY  (SDRID,FORMAT),
  CONSTRAINT FK_StyleDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES styledomainresource (SDRID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

insert into new_styledomainformat (
  SDRID,
  FORMAT
	)
  select 
  SDRID,
  FORMAT
  from styledomainformat;

drop table styledomainformat;
rename table new_styledomainformat to styledomainformat;

--
-- Definition of table styledomainresource
--

CREATE TABLE new_styledomainresource (
  SDRID int(11) NOT NULL auto_increment,
  STYLEID int(11) NOT NULL default '0',
  DOMAIN varchar(45) NOT NULL default '',
  URL varchar(200) NOT NULL,
  WIDTH varchar(45) default NULL,
  HEIGHT varchar(45) default NULL,
  PRIMARY KEY  (SDRID),
  KEY FK_StyleDomainResource_1 (STYLEID),
  CONSTRAINT FK_StyleDomainResource_1 FOREIGN KEY (STYLEID) REFERENCES style (STYLEID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_styledomainresource (
  SDRID,
  STYLEID,
  DOMAIN,
  URL,
  WIDTH,
  HEIGHT
	)
  select 
  SDRID,
  STYLEID,
  DOMAIN,
  URL,
  WIDTH,
  HEIGHT
  from styledomainresource;

drop table styledomainresource;
rename table new_styledomainresource to styledomainresource;

--
-- Definition of table user
--

CREATE TABLE new_user (
  USERID int(11) NOT NULL auto_increment,
  ORGANIZATIONID int(11) NOT NULL default '0',
  FIRSTNAME varchar(50) NOT NULL default '0',
  LASTNAME varchar(50) NOT NULL default '0',
  EMAILADDRESS varchar(50) NOT NULL default '0',
  USERNAME varchar(50) NOT NULL default '0',
  PASSWORD varchar(50) NOT NULL default '0',
  ROLE varchar(50) NOT NULL default '0',
  PERSONALURL varchar(100) default NULL,
  TIMEOUT datetime default NULL,
  DEFAULTGETMAP varchar(2000) default NULL,
  PRIMARY KEY  (USERID),
  KEY FK_User_1 (ORGANIZATIONID),
  CONSTRAINT FK_User_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_user (
  USERID,
  ORGANIZATIONID,
  FIRSTNAME,
  LASTNAME,
  EMAILADDRESS,
  USERNAME,
  PASSWORD,
  ROLE,
  PERSONALURL,
  TIMEOUT,
  DEFAULTGETMAP
	)
  select 
  USERID,
  ORGANIZATIONID,
  FIRSTNAME,
  LASTNAME,
  EMAILADDRESS,
  USERNAME,
  PASSWORD,
  ROLE,
  PERSONALURL,
  TIMEOUT,
  DEFAULTGETMAP
  from user;

drop table user;
rename table new_user to user;

SET FOREIGN_KEY_CHECKS = 1;
