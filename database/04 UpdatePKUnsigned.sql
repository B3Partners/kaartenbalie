
--
-- Definition of table attribution
--
CREATE TABLE new_attribution (
  LAYERID int(11) NOT NULL,
  TITLE varchar(50),
  ATTRIBUTIONURL varchar(150),
  LOGOURL varchar(50),
  LOGOWIDTH varchar(50),
  LOGOHEIGHT varchar(50),
  PRIMARY KEY  (LAYERID)
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

--
-- Definition of table contactinformation
--

CREATE TABLE new_contactinformation (
  SERVICEPROVIDERID int(11) NOT NULL,
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

--
-- Definition of table dimensions
--

CREATE TABLE new_dimensions (
  DIMENSIONSID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
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

--
-- Definition of table exceptions
--

CREATE TABLE new_exceptions (
  SERVICEPROVIDERID int(11) NOT NULL,
  FORMAT varchar(50) NOT NULL,
  PRIMARY KEY  (SERVICEPROVIDERID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_exceptions (
  SERVICEPROVIDERID,
  FORMAT
	)
  select 
  SERVICEPROVIDERID,
  FORMAT
  from exceptions;

--
-- Definition of table identifier
--

CREATE TABLE new_identifier (
  IDENTIFIERID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
  AUTHORITYNAME varchar(50) NOT NULL,
  AUTHORITYURL varchar(50) NOT NULL,
  PRIMARY KEY  (IDENTIFIERID)
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

--
-- Definition of table ipaddresses
--

CREATE TABLE new_ipaddresses ( 
   IPADDRESSID int(11) NOT NULL AUTO_INCREMENT, 
   IPADDRESS varchar(45) NOT NULL, 
   PRIMARY KEY (IPADDRESSID) 
) ENGINE= InnoDB DEFAULT CHARSET= utf8;

insert into new_ipaddresses (
  IPADDRESSID,
  IPADDRESS
	)
  select 
  IPADDRESSID,
  IPADDRESS
  from ipaddresses;

--
-- Definition of table layer
--

CREATE TABLE new_layer (
  LAYERID int(11) NOT NULL auto_increment,
  SERVICEPROVIDERID int(11),
  NAME varchar(50),
  TITLE varchar(200) NOT NULL,
  ABSTRACTS mediumtext,
  QUERYABLE varchar(50) default '0',
  CASCADED varchar(50) default '0',
  OPAQUE varchar(50) default '0',
  NOSUBSETS varchar(50) default '0',
  FIXEDWIDTH varchar(50) default '0',
  FIXEDHEIGHT varchar(50) default '0',
  SCALEHINTMIN varchar(50) default '0',
  SCALEHINTMAX varchar(50) default '0',
  PARENTID int(11),
  METADATA text,
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
  PARENTID,
  METADATA
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
  PARENTID,
  METADATA
  from layer;

--
-- Definition of table layerdomainformat
--

CREATE TABLE new_layerdomainformat (
  LDRID int(11) NOT NULL,
  FORMAT varchar(100) NOT NULL default '',
  PRIMARY KEY  (LDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_layerdomainformat (
  LDRID,
  FORMAT
	)
  select 
  LDRID,
  FORMAT
  from layerdomainformat;

--
-- Definition of table layerdomainresource
--

CREATE TABLE new_layerdomainresource (
  LDRID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
  DOMAIN varchar(50) NOT NULL,
  URL varchar(100) NOT NULL,
  PRIMARY KEY  (LDRID)
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

--
-- Definition of table layerkeywordlist
--

CREATE TABLE new_layerkeywordlist (
  KEYWORDLISTID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
  KEYWORD varchar(50) NOT NULL,
  PRIMARY KEY  (KEYWORDLISTID)
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

--
-- Definition of table organization
--

CREATE TABLE new_organization (
  ORGANIZATIONID int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL,
  STREET varchar(50),
  NUMBER varchar(10),
  ADDITION varchar(10),
  PROVINCE varchar(50),
  COUNTRY varchar(50),
  POSTBOX varchar(50),
  BILLINGADDRESS varchar(50),
  VISITORSADDRESS varchar(50),
  TELEPHONE varchar(50) NOT NULL,
  FAX varchar(50),
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
  cast(NUMBER as char(10)),
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

--
-- Definition of table organizationlayer
--

CREATE TABLE new_organizationlayer (
  ORGANIZATIONID int(11) NOT NULL,
  LAYERID int(11) NOT NULL,
  PRIMARY KEY  (ORGANIZATIONID,LAYERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_organizationlayer (
  ORGANIZATIONID,
  LAYERID
	)
  select 
  ORGANIZATIONID,
  LAYERID
  from organizationlayer;

--
-- Definition of table roles
--

CREATE TABLE new_roles ( 
   ROLEID int(11) NOT NULL AUTO_INCREMENT, 
   ROLE varchar(45) NOT NULL, 
   PRIMARY KEY (ROLEID) 
) ENGINE= InnoDB DEFAULT CHARSET= utf8;

insert into new_roles (
  ROLEID,
  ROLE
	)
  select 
  ROLEID,
  ROLE
  from roles;
    
--
-- Definition of table servicedomainformat
--

CREATE TABLE new_servicedomainformat (
  SDRID int(11) NOT NULL,
  FORMAT varchar(100) NOT NULL,
  PRIMARY KEY  (SDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_servicedomainformat (
  SDRID,
  FORMAT
	)
  select 
  SDRID,
  FORMAT
  from servicedomainformat;

--
-- Definition of table servicedomainresource
--

CREATE TABLE new_servicedomainresource (
  SDRID int(11) NOT NULL auto_increment,
  SERVICEPROVIDERID int(11) NOT NULL,
  DOMAIN varchar(50) NOT NULL,
  GETURL varchar(100),
  POSTURL varchar(100),
  PRIMARY KEY  (SDRID)
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

--
-- Definition of table serviceprovider
--

CREATE TABLE new_serviceprovider (
  SERVICEPROVIDERID int(11) NOT NULL auto_increment,
  NAME varchar(60) NOT NULL,
  TITLE varchar(50) NOT NULL,
  ABSTRACTS varchar(200),
  FEES varchar(50),
  ACCESSCONSTRAINTS varchar(50),
  GIVENNAME varchar(50) NOT NULL,
  URL varchar(200) NOT NULL,
  UPDATEDDATE datetime NOT NULL,
  WMSVERSION varchar(50) NOT NULL,
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
  WMSVERSION
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
  WMSVERSION
  from serviceprovider;

--
-- Definition of table serviceproviderkeywordlist
--

CREATE TABLE new_serviceproviderkeywordlist (
  SERVICEPROVIDERID int(11) NOT NULL,
  KEYWORD varchar(50) NOT NULL,
  PRIMARY KEY  (SERVICEPROVIDERID,KEYWORD)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_serviceproviderkeywordlist (
  SERVICEPROVIDERID,
  KEYWORD
	)
  select 
  SERVICEPROVIDERID,
  KEYWORD
  from serviceproviderkeywordlist;

--
-- Definition of table srs
--

CREATE TABLE new_srs (
  SRSID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
  SRS varchar(150),
  MINX varchar(50),
  MAXX varchar(50),
  MINY varchar(50),
  MAXY varchar(50),
  RESX varchar(50),
  RESY varchar(50),
  PRIMARY KEY  (SRSID)
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
  cast(MINX as char(50)),
  cast(MAXX as char(50)),
  cast(MINY as char(50)),
  cast(MAXY as char(50)),
  cast(RESX as char(50)),
  cast(RESY as char(50)),
  from srs;

--
-- Definition of table style
--

CREATE TABLE new_style (
  STYLEID int(11) NOT NULL auto_increment,
  LAYERID int(11) NOT NULL,
  NAME varchar(50) NOT NULL,
  TITLE varchar(50) NOT NULL,
  ABSTRACTS varchar(50),
  STYLEURL varchar(50) NOT NULL,
  STYLESHEETURL varchar(50),
  PRIMARY KEY  (STYLEID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_style (
  STYLEID,
  LAYERID,
  NAME,
  TITLE,
  ABSTRACTS,
  STYLEURL,
  STYLESHEETURL
	)
  select 
  STYLEID,
  LAYERID,
  NAME,
  TITLE,
  ABSTRACTS,
  STYLEURL,
  STYLESHEETURL
  from style;

--
-- Definition of table styledomainformat
--

CREATE TABLE new_styledomainformat (
  SDRID int(11) NOT NULL auto_increment,
  FORMAT char(45) NOT NULL,
  PRIMARY KEY  (SDRID,FORMAT)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_styledomainformat (
  SDRID,
  FORMAT
	)
  select 
  SDRID,
  FORMAT
  from styledomainformat;

--
-- Definition of table styledomainresource
--

CREATE TABLE new_styledomainresource (
  SDRID int(11) NOT NULL auto_increment,
  STYLEID int(11) NOT NULL,
  DOMAIN varchar(45) NOT NULL,
  URL varchar(200) NOT NULL,
  WIDTH varchar(45),
  HEIGHT varchar(45),
  PRIMARY KEY  (SDRID)
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

--
-- Definition of table user
--

CREATE TABLE new_user (
  USERID int(11) NOT NULL auto_increment,
  ORGANIZATIONID int(11) NOT NULL,
  FIRSTNAME varchar(50) NOT NULL,
  LASTNAME varchar(50) NOT NULL,
  EMAILADDRESS varchar(50) NOT NULL,
  USERNAME varchar(50) NOT NULL,
  PASSWORD varchar(50) NOT NULL,
  PERSONALURL varchar(4000),
  TIMEOUT datetime,
  DEFAULTGETMAP varchar(2000),
  PRIMARY KEY  (USERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into new_user (
  USERID,
  ORGANIZATIONID,
  FIRSTNAME,
  LASTNAME,
  EMAILADDRESS,
  USERNAME,
  PASSWORD,
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
  PERSONALURL,
  TIMEOUT,
  DEFAULTGETMAP
  from user;

--
-- Definition of table userip
--

CREATE TABLE new_userip ( 
   USERID int(11) NOT NULL AUTO_INCREMENT, 
   IPADDRESSID int(11) NOT NULL, 
   PRIMARY KEY (USERID, IPADDRESSID) 
) ENGINE= InnoDB DEFAULT CHARSET= utf8;
    
insert into new_userip (
  USERID,
  IPADDRESSID
	)
  select 
  USERID,
  IPADDRESSID
  from userip;
  
--
-- Definition of table userroles
--

CREATE TABLE new_userroles ( 
   USERID int(11) NOT NULL AUTO_INCREMENT, 
   ROLEID int(11)  NOT NULL, 
   PRIMARY KEY (USERID, ROLEID) 
) ENGINE= InnoDB DEFAULT CHARSET= utf8;   
 
insert into new_userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  ROLEID
  from userroles;
    
--

alter table attribution drop foreign key FK_Attribution_1;
--alter table contactinformation drop foreign key ;
alter table dimensions drop foreign key FK_Dimensions_1;
alter table exceptions drop foreign key FK_Exceptions_1;
alter table identifier drop foreign key FK_Identifier_1;
--alter table ipaddresses drop foreign key ;
alter table layer drop foreign key FK_Layer_1;
alter table layer drop foreign key FK_Layer_2;
alter table layerdomainformat drop foreign key FK_LayerDomainFormat_1;
alter table layerdomainresource drop foreign key FK_LayerDomainResource_1;
alter table layerkeywordlist drop foreign key FK_LayerKeywordList_1;
--alter table organization drop foreign key ;
alter table organizationlayer drop foreign key FK_OrganizationLayer_1;
alter table organizationlayer drop foreign key FK_OrganizationLayer_2;
--alter table roles drop foreign key ;
alter table servicedomainformat drop foreign key FK_ServiceDomainFormat_1;
alter table servicedomainresource drop foreign key FK_RequestDomainResource_1;
--alter table serviceprovider drop foreign key ;
alter table serviceproviderkeywordlist drop foreign key FK_ServiceProviderKeywordList_1;
alter table srs drop foreign key FK_SRS_1;
alter table style drop foreign key FK_Style_1;
alter table styledomainformat drop foreign key FK_StyleDomainFormat_1;
alter table styledomainresource drop foreign key FK_StyleDomainResource_1;
alter table user drop foreign key FK_User_1;
alter table userip drop foreign key FK_Userip_1;
alter table userip drop foreign key FK_Userip_2;
alter table userroles drop foreign key FK_Userroles_1;
alter table userroles drop foreign key FK_Userroles_2;

drop table attribution;
drop table contactinformation;
drop table dimensions;
drop table exceptions;
drop table identifier;
drop table ipaddresses;
drop table layer;
drop table layerdomainformat;
drop table layerdomainresource;
drop table layerkeywordlist;
drop table organization;
drop table organizationlayer;
drop table roles;
drop table servicedomainformat;
drop table servicedomainresource;
drop table serviceprovider;
drop table serviceproviderkeywordlist;
drop table srs;
drop table style;
drop table styledomainformat;
drop table styledomainresource;
drop table user;
drop table userip;
drop table userroles;


rename table new_attribution to attribution;
rename table new_contactinformation to contactinformation;
rename table new_dimensions to dimensions;
rename table new_exceptions to exceptions;
rename table new_identifier to identifier;
rename table new_ipaddresses to ipaddresses;
rename table new_layer to layer;
rename table new_layerdomainformat to layerdomainformat;
rename table new_layerdomainresource to layerdomainresource;
rename table new_layerkeywordlist to layerkeywordlist;
rename table new_organization to organization;
rename table new_organizationlayer to organizationlayer;
rename table new_roles to roles;
rename table new_servicedomainformat to servicedomainformat;
rename table new_servicedomainresource to servicedomainresource;
rename table new_serviceprovider to serviceprovider;
rename table new_serviceproviderkeywordlist to serviceproviderkeywordlist;
rename table new_srs to srs;
rename table new_style to style;
rename table new_styledomainformat to styledomainformat;
rename table new_styledomainresource to styledomainresource;
rename table new_user to user;
rename table new_userip to userip;
rename table new_userroles to userroles;


alter table attribution add CONSTRAINT LAYERID FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE ON UPDATE CASCADE;
--alter table contactinformation add CONSTRAINT symbol
alter table dimensions add CONSTRAINT FK_Dimensions_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
alter table exceptions add CONSTRAINT FK_Exceptions_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID);
alter table identifier add CONSTRAINT FK_Identifier_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
--alter table ipaddresses add CONSTRAINT symbol;
alter table layer add CONSTRAINT FK_Layer_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID); 
alter table layer add CONSTRAINT FK_Layer_2 FOREIGN KEY (PARENTID) REFERENCES layer (LAYERID); 
alter table layerdomainformat add CONSTRAINT FK_LayerDomainFormat_1 FOREIGN KEY (LDRID) REFERENCES layerdomainresource (LDRID);
alter table layerdomainresource add CONSTRAINT FK_LayerDomainResource_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table layerkeywordlist add CONSTRAINT FK_LayerKeywordList_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
--alter table organization add CONSTRAINT symbol 
alter table organizationlayer add CONSTRAINT FK_OrganizationLayer_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID);
alter table organizationlayer add CONSTRAINT FK_OrganizationLayer_2 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID);
--alter table roles add CONSTRAINT symbol 
alter table servicedomainformat add CONSTRAINT FK_ServiceDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES servicedomainresource (SDRID) ON DELETE CASCADE;
alter table servicedomainresource add CONSTRAINT FK_RequestDomainResource_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE;
--alter table serviceprovider add CONSTRAINT symbol 
alter table serviceproviderkeywordlist add CONSTRAINT FK_ServiceProviderKeywordList_1 FOREIGN KEY (SERVICEPROVIDERID) REFERENCES serviceprovider (SERVICEPROVIDERID) ON DELETE CASCADE;
alter table srs add CONSTRAINT FK_SRS_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table style add CONSTRAINT FK_Style_1 FOREIGN KEY (LAYERID) REFERENCES layer (LAYERID) ON DELETE CASCADE;
alter table styledomainformat add CONSTRAINT FK_StyleDomainFormat_1 FOREIGN KEY (SDRID) REFERENCES styledomainresource (SDRID) ON DELETE CASCADE;
alter table styledomainresource add CONSTRAINT FK_StyleDomainResource_1 FOREIGN KEY (STYLEID) REFERENCES style (STYLEID) ON DELETE CASCADE;
alter table user add CONSTRAINT FK_User_1 FOREIGN KEY (ORGANIZATIONID) REFERENCES organization (ORGANIZATIONID);
alter table userip add CONSTRAINT FK_userip_2 FOREIGN KEY (IPADDRESSID) REFERENCES ipaddresses (IPADDRESSID);
alter table userip add CONSTRAINT FK_userip_1 FOREIGN KEY (USERID) REFERENCES user (USERID);
alter table userroles add CONSTRAINT FK_userroles_2 FOREIGN KEY (ROLEID) REFERENCES roles (ROLEID);
alter table userroles add CONSTRAINT FK_userroles_1 FOREIGN KEY (USERID) REFERENCES user (USERID);
