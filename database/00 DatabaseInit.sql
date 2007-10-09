--
-- Definition of table attribution
--
CREATE TABLE attribution (
  LAYERID int(10) unsigned NOT NULL default '0',
  TITLE varchar(50) default '0',
  ATTRIBUTIONURL varchar(150) default '0',
  LOGOURL varchar(50),
  LOGOWIDTH varchar(50),
  LOGOHEIGHT varchar(50),
  PRIMARY KEY  (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


--
-- Definition of table contactinformation
--

CREATE TABLE contactinformation (
  SERVICEPROVIDERID int(10) NOT NULL default '0',
  CONTACTPERSON varchar(50),
  CONTACTPOSITION varchar(50),
  ADDRESS varchar(50),
  ADDRESSTYPE varchar(50),
  POSTCODE varchar(50),
  CITY varchar(50),
  STATEORPROVINCE varchar(50),
  COUNTRY varchar(50),
  VOICETELEPHONE varchar(50),
  FASCIMILETELEPHONE varchar(50),
  EMAILADDRESS varchar(50),
  PRIMARY KEY  (SERVICEPROVIDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table dimensions
--

CREATE TABLE dimensions (
  DIMENSIONSID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  DIMENSIONSNAME varchar(50),
  DIMENSIONSUNIT varchar(50),
  DIMENSIONSUNITSYMBOL varchar(50),
  EXTENTNAME varchar(50),
  EXTENTDEFAULTS varchar(50),
  EXTENTNEARESTVALUE varchar(50),
  EXTENTMULTIPLEVALUES varchar(50),
  EXTENTCURRENT varchar(50),
  PRIMARY KEY  (DIMENSIONSID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table exceptions
--

CREATE TABLE exceptions (
  SERVICEPROVIDERID int(10) unsigned NOT NULL default '0',
  FORMAT varchar(50) NOT NULL default '0',
  PRIMARY KEY  (SERVICEPROVIDERID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table identifier
--

CREATE TABLE identifier (
  IDENTIFIERID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  AUTHORITYNAME varchar(50) NOT NULL default '0',
  AUTHORITYURL varchar(50) NOT NULL default '0',
  PRIMARY KEY  (IDENTIFIERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Definition of table layer
--

CREATE TABLE layer (
  LAYERID int(10) unsigned NOT NULL auto_increment,
  SERVICEPROVIDERID int(10) unsigned default '0',
  NAME varchar(50),
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
  PARENTID int(10) unsigned,
  METADATA text,
  PRIMARY KEY  (LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

--
-- Definition of table layerdomainformat
--

CREATE TABLE layerdomainformat (
  LDRID int(10) unsigned NOT NULL default '0',
  FORMAT varchar(100) NOT NULL default '',
  PRIMARY KEY  (LDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table layerdomainresource
--

CREATE TABLE layerdomainresource (
  LDRID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  DOMAIN varchar(50) NOT NULL default '0',
  URL varchar(100) NOT NULL,
  PRIMARY KEY  (LDRID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table layerkeywordlist
--

CREATE TABLE layerkeywordlist (
  KEYWORDLISTID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  KEYWORD varchar(50) NOT NULL default '0',
  PRIMARY KEY  (KEYWORDLISTID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table organization
--

CREATE TABLE organization (
  ORGANIZATIONID int(10) unsigned NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '0',
  STREET varchar(50) NOT NULL default '0',
  NUMBER smallint(5) NOT NULL default '0',
  ADDITION varchar(10),
  PROVINCE varchar(50) NOT NULL default '0',
  COUNTRY varchar(50) NOT NULL default '0',
  POSTBOX varchar(50),
  BILLINGADDRESS varchar(50),
  VISITORSADDRESS varchar(50),
  TELEPHONE varchar(50) NOT NULL default '0',
  FAX varchar(50),
  POSTALCODE varchar(45) NOT NULL,
  HASVALIDGETCAPABILITIES tinyint(1) NOT NULL,
  PRIMARY KEY  (ORGANIZATIONID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table organizationlayer
--

CREATE TABLE organizationlayer (
  ORGANIZATIONID int(10) unsigned NOT NULL default '0',
  LAYERID int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (ORGANIZATIONID,LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table servicedomainformat
--

CREATE TABLE servicedomainformat (
  SDRID int(10) unsigned NOT NULL default '0',
  FORMAT varchar(100) NOT NULL default '0',
  PRIMARY KEY  (SDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table servicedomainresource
--

CREATE TABLE servicedomainresource (
  SDRID int(10) unsigned NOT NULL auto_increment,
  SERVICEPROVIDERID int(10) unsigned NOT NULL default '0',
  DOMAIN varchar(50) NOT NULL default '0',
  GETURL varchar(100) default '0',
  POSTURL varchar(100),
  PRIMARY KEY  (SDRID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table serviceprovider
--

CREATE TABLE serviceprovider (
  SERVICEPROVIDERID int(10) unsigned NOT NULL auto_increment,
  NAME varchar(60) NOT NULL default '0',
  TITLE varchar(50) NOT NULL default '0',
  ABSTRACTS varchar(200),
  FEES varchar(50),
  ACCESSCONSTRAINTS varchar(50),
  GIVENNAME varchar(50) NOT NULL default '0',
  URL varchar(200) NOT NULL default '0',
  UPDATEDDATE datetime NOT NULL default '0000-00-00 00:00:00',
  WMSVERSION varchar(50) NOT NULL,
  PRIMARY KEY  (SERVICEPROVIDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table serviceproviderkeywordlist
--

CREATE TABLE serviceproviderkeywordlist (
  SERVICEPROVIDERID int(10) unsigned NOT NULL default '0',
  KEYWORD varchar(50) NOT NULL default '0',
  PRIMARY KEY  (SERVICEPROVIDERID,KEYWORD)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table srs
--

CREATE TABLE srs (
  SRSID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  SRS varchar(150),
  MINX double,
  MAXX double,
  MINY double,
  MAXY double,
  RESX double,
  RESY double,
  PRIMARY KEY  (SRSID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table style
--

CREATE TABLE style (
  STYLEID int(10) unsigned NOT NULL auto_increment,
  LAYERID int(10) unsigned NOT NULL default '0',
  NAME varchar(50) NOT NULL default '0',
  TITLE varchar(50) NOT NULL default '0',
  ABSTRACTS varchar(50),
  STYLEURL varchar(50) NOT NULL default '0',
  STYLESHEETURL varchar(50),
  PRIMARY KEY  (STYLEID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table styledomainformat
--

CREATE TABLE styledomainformat (
  SDRID int(10) unsigned NOT NULL auto_increment,
  FORMAT char(45) NOT NULL default '',
  PRIMARY KEY  (SDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table styledomainresource
--

CREATE TABLE styledomainresource (
  SDRID int(10) unsigned NOT NULL auto_increment,
  STYLEID int(10) unsigned NOT NULL default '0',
  DOMAIN varchar(45) NOT NULL default '',
  URL varchar(200) NOT NULL,
  WIDTH varchar(45),
  HEIGHT varchar(45),
  PRIMARY KEY  (SDRID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table user
--

CREATE TABLE user (
  USERID int(10) unsigned NOT NULL auto_increment,
  ORGANIZATIONID int(10) unsigned NOT NULL default '0',
  FIRSTNAME varchar(50) NOT NULL default '0',
  LASTNAME varchar(50) NOT NULL default '0',
  EMAILADDRESS varchar(50) NOT NULL default '0',
  USERNAME varchar(50) NOT NULL default '0',
  PASSWORD varchar(50) NOT NULL default '0',
  ROLE varchar(50) NOT NULL default '0',
  PERSONALURL varchar(100),
  TIMEOUT datetime,
  DEFAULTGETMAP varchar(2000),
  IPADDRESS varchar(255), 
  PRIMARY KEY  (USERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



alter table attribution add CONSTRAINT FK_Attribution_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE ON UPDATE CASCADE;
alter table dimensions add CONSTRAINT FK_Dimensions_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
alter table exceptions add CONSTRAINT FK_Exceptions_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID);
alter table identifier add CONSTRAINT FK_Identifier_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
alter table layer add CONSTRAINT FK_Layer_1 FOREIGN KEY (PARENTID) REFERENCES layer (LAYERID);
alter table layer add CONSTRAINT FK_Layer_2 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID);
alter table layerdomainformat add CONSTRAINT FK_LayerDomainFormat_1 FOREIGN KEY (LDRID) REFERENCES layerdomainresource (LDRID);
alter table layerdomainresource add CONSTRAINT FK_LayerDomainResource_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table layerkeywordlist add CONSTRAINT FK_LayerKeywordList_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table organizationlayer add CONSTRAINT FK_OrganizationLayer_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID);
alter table organizationlayer add CONSTRAINT FK_OrganizationLayer_2 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
alter table servicedomainformat add CONSTRAINT FK_ServiceDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES servicedomainresource (SDRID) ON DELETE CASCADE;
alter table servicedomainresource add CONSTRAINT FK_RequestDomainResource_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE;
alter table serviceproviderkeywordlist add CONSTRAINT FK_ServiceProviderKeywordList_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE;
alter table srs add CONSTRAINT FK_SRS_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table style add CONSTRAINT FK_Style_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table styledomainformat add CONSTRAINT FK_StyleDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES styledomainresource (SDRID) ON DELETE CASCADE;
alter table styledomainresource add CONSTRAINT FK_StyleDomainResource_1 FOREIGN KEY (STYLEID) REFERENCES style (STYLEID) ON DELETE CASCADE;
alter table user add CONSTRAINT FK_User_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID);


--
-- Dumping data for table `organization`
--

INSERT INTO `organization` (`ORGANIZATIONID`,`NAME`,`STREET`,`NUMBER`,`ADDITION`,`PROVINCE`,`COUNTRY`,`POSTBOX`,`BILLINGADDRESS`,`VISITORSADDRESS`,`TELEPHONE`,`FAX`,`POSTALCODE`,`HASVALIDGETCAPABILITIES`) VALUES 
 (1,'B3Partners','Zonnebaan',12,'C','Utrecht','Nederland','0','yes','yes','030-2142081','030-2141297','3542 EC',1);

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`USERID`,`ORGANIZATIONID`,`FIRSTNAME`,`LASTNAME`,`EMAILADDRESS`,`USERNAME`,`PASSWORD`,`ROLE`,`PERSONALURL`,`TIMEOUT`,`DEFAULTGETMAP`) VALUES 
 (1,1,'beheerder','beheerder','info@b3p.nl','beheerder','beheerder','beheerder','http://www.b3p.nl/',NULL,NULL);
