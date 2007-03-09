-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.27-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema kaartenbalie
--

CREATE DATABASE IF NOT EXISTS kaartenbalie;
USE kaartenbalie;

--
-- Definition of table `attribution`
--

DROP TABLE IF EXISTS `attribution`;
CREATE TABLE `attribution` (
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `TITLE` varchar(50) default '0',
  `ATTRIBUTIONURL` varchar(150) default '0',
  `LOGOURL` varchar(50) default NULL,
  `LOGOWIDTH` varchar(50) default NULL,
  `LOGOHEIGHT` varchar(50) default NULL,
  PRIMARY KEY  (`LAYERID`),
  CONSTRAINT `LAYERID` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB';

--
-- Dumping data for table `attribution`
--

/*!40000 ALTER TABLE `attribution` DISABLE KEYS */;
/*!40000 ALTER TABLE `attribution` ENABLE KEYS */;


--
-- Definition of table `contactinformation`
--

DROP TABLE IF EXISTS `contactinformation`;
CREATE TABLE `contactinformation` (
  `SERVICEPROVIDERID` int(10) NOT NULL default '0',
  `CONTACTPERSON` varchar(50) default NULL,
  `CONTACTPOSITION` varchar(50) default NULL,
  `ADDRESS` varchar(50) default NULL,
  `ADDRESSTYPE` varchar(50) default NULL,
  `POSTCODE` varchar(50) default NULL,
  `CITY` varchar(50) default NULL,
  `STATEORPROVINCE` varchar(50) default NULL,
  `COUNTRY` varchar(50) default NULL,
  `VOICETELEPHONE` varchar(50) default NULL,
  `FASCIMILETELEPHONE` varchar(50) default NULL,
  `EMAILADDRESS` varchar(50) default NULL,
  PRIMARY KEY  (`SERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contactinformation`
--

/*!40000 ALTER TABLE `contactinformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `contactinformation` ENABLE KEYS */;


--
-- Definition of table `dimensions`
--

DROP TABLE IF EXISTS `dimensions`;
CREATE TABLE `dimensions` (
  `DIMENSIONSID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `DIMENSIONSNAME` varchar(50) default NULL,
  `DIMENSIONSUNIT` varchar(50) default NULL,
  `DIMENSIONSUNITSYMBOL` varchar(50) default NULL,
  `EXTENTNAME` varchar(50) default NULL,
  `EXTENTDEFAULTS` varchar(50) default NULL,
  `EXTENTNEARESTVALUE` varchar(50) default NULL,
  `EXTENTMULTIPLEVALUES` varchar(50) default NULL,
  `EXTENTCURRENT` varchar(50) default NULL,
  PRIMARY KEY  (`DIMENSIONSID`),
  KEY `FK_Dimensions_1` (`LAYERID`),
  CONSTRAINT `FK_Dimensions_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dimensions`
--

/*!40000 ALTER TABLE `dimensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `dimensions` ENABLE KEYS */;


--
-- Definition of table `exceptions`
--

DROP TABLE IF EXISTS `exceptions`;
CREATE TABLE `exceptions` (
  `SERVICEPROVIDERID` int(10) unsigned NOT NULL default '0',
  `FORMAT` varchar(50) NOT NULL default '0',
  PRIMARY KEY  (`SERVICEPROVIDERID`,`FORMAT`),
  CONSTRAINT `FK_Exceptions_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `exceptions`
--

/*!40000 ALTER TABLE `exceptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `exceptions` ENABLE KEYS */;


--
-- Definition of table `identifier`
--

DROP TABLE IF EXISTS `identifier`;
CREATE TABLE `identifier` (
  `IDENTIFIERID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `AUTHORITYNAME` varchar(50) NOT NULL default '0',
  `AUTHORITYURL` varchar(50) NOT NULL default '0',
  PRIMARY KEY  (`IDENTIFIERID`),
  KEY `FK_Identifier_1` (`LAYERID`),
  CONSTRAINT `FK_Identifier_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `identifier`
--

/*!40000 ALTER TABLE `identifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `identifier` ENABLE KEYS */;


--
-- Definition of table `latlonboundingbox`
--

DROP TABLE IF EXISTS `latlonboundingbox`;
CREATE TABLE `latlonboundingbox` (
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `MINX` double default '0',
  `MINY` double default '0',
  `MAXX` double default '0',
  `MAXY` double default '0',
  PRIMARY KEY  (`LAYERID`),
  CONSTRAINT `FK_LatLonBoundingBox_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `latlonboundingbox`
--

/*!40000 ALTER TABLE `latlonboundingbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `latlonboundingbox` ENABLE KEYS */;


--
-- Definition of table `layer`
--

DROP TABLE IF EXISTS `layer`;
CREATE TABLE `layer` (
  `LAYERID` int(10) unsigned NOT NULL auto_increment,
  `SERVICEPROVIDERID` int(10) unsigned default '0',
  `NAME` varchar(50) default NULL,
  `TITLE` varchar(200) NOT NULL default '0',
  `ABSTRACTS` mediumtext,
  `QUERYABLE` varchar(50) default '0',
  `CASCADED` varchar(50) default '0',
  `OPAQUE` varchar(50) default '0',
  `NOSUBSETS` varchar(50) default '0',
  `FIXEDWIDTH` varchar(50) default '0',
  `FIXEDHEIGHT` varchar(50) default '0',
  `SCALEHINTMIN` varchar(50) default '0',
  `SCALEHINTMAX` varchar(50) default '0',
  `PARENTID` int(10) unsigned default NULL,
  PRIMARY KEY  (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB';

--
-- Dumping data for table `layer`
--

/*!40000 ALTER TABLE `layer` DISABLE KEYS */;
/*!40000 ALTER TABLE `layer` ENABLE KEYS */;


--
-- Definition of table `layerdomainformat`
--

DROP TABLE IF EXISTS `layerdomainformat`;
CREATE TABLE `layerdomainformat` (
  `LDRID` int(10) unsigned NOT NULL default '0',
  `FORMAT` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`LDRID`,`FORMAT`),
  CONSTRAINT `FK_LayerDomainFormat_1` FOREIGN KEY (`LDRID`) REFERENCES `layerdomainresource` (`LDRID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB';

--
-- Dumping data for table `layerdomainformat`
--

/*!40000 ALTER TABLE `layerdomainformat` DISABLE KEYS */;
/*!40000 ALTER TABLE `layerdomainformat` ENABLE KEYS */;


--
-- Definition of table `layerdomainresource`
--

DROP TABLE IF EXISTS `layerdomainresource`;
CREATE TABLE `layerdomainresource` (
  `LDRID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `DOMAIN` varchar(50) NOT NULL default '0',
  `URL` varchar(100) NOT NULL,
  PRIMARY KEY  (`LDRID`),
  KEY `FK_LayerDomainResource_1` (`LAYERID`),
  CONSTRAINT `FK_LayerDomainResource_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `layerdomainresource`
--

/*!40000 ALTER TABLE `layerdomainresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `layerdomainresource` ENABLE KEYS */;


--
-- Definition of table `layerkeywordlist`
--

DROP TABLE IF EXISTS `layerkeywordlist`;
CREATE TABLE `layerkeywordlist` (
  `KEYWORDLISTID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `KEYWORD` varchar(50) NOT NULL default '0',
  PRIMARY KEY  (`KEYWORDLISTID`),
  KEY `FK_LayerKeywordList_1` (`LAYERID`),
  CONSTRAINT `FK_LayerKeywordList_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `layerkeywordlist`
--

/*!40000 ALTER TABLE `layerkeywordlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `layerkeywordlist` ENABLE KEYS */;


--
-- Definition of table `organization`
--

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `ORGANIZATIONID` int(10) unsigned NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL default '0',
  `STREET` varchar(50) NOT NULL default '0',
  `NUMBER` smallint(5) NOT NULL default '0',
  `ADDITION` varchar(10) default NULL,
  `PROVINCE` varchar(50) NOT NULL default '0',
  `COUNTRY` varchar(50) NOT NULL default '0',
  `POSTBOX` varchar(50) default NULL,
  `BILLINGADDRESS` varchar(50) default NULL,
  `VISITORSADDRESS` varchar(50) default NULL,
  `TELEPHONE` varchar(50) NOT NULL default '0',
  `FAX` varchar(50) default NULL,
  `POSTALCODE` varchar(45) NOT NULL,
  `HASVALIDGETCAPABILITIES` tinyint(1) NOT NULL,
  PRIMARY KEY  (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `organization`
--

/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` (`ORGANIZATIONID`,`NAME`,`STREET`,`NUMBER`,`ADDITION`,`PROVINCE`,`COUNTRY`,`POSTBOX`,`BILLINGADDRESS`,`VISITORSADDRESS`,`TELEPHONE`,`FAX`,`POSTALCODE`,`HASVALIDGETCAPABILITIES`) VALUES 
 (1,'B3Partners','Zonnebaan',12,'C','Utrecht','Nederland','0','yes','yes','030-2142081','030-2141297','3542 EC',1);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;


--
-- Definition of table `organizationlayer`
--

DROP TABLE IF EXISTS `organizationlayer`;
CREATE TABLE `organizationlayer` (
  `ORGANIZATIONID` int(10) unsigned NOT NULL default '0',
  `LAYERID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ORGANIZATIONID`,`LAYERID`),
  KEY `FK_OrganizationLayer_2` (`LAYERID`),
  CONSTRAINT `FK_OrganizationLayer_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`),
  CONSTRAINT `FK_OrganizationLayer_2` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB';

--
-- Dumping data for table `organizationlayer`
--

/*!40000 ALTER TABLE `organizationlayer` DISABLE KEYS */;
/*!40000 ALTER TABLE `organizationlayer` ENABLE KEYS */;


--
-- Definition of table `servicedomainformat`
--

DROP TABLE IF EXISTS `servicedomainformat`;
CREATE TABLE `servicedomainformat` (
  `SDRID` int(10) unsigned NOT NULL default '0',
  `FORMAT` varchar(100) NOT NULL default '0',
  PRIMARY KEY  (`SDRID`,`FORMAT`),
  CONSTRAINT `FK_ServiceDomainFormat_1` FOREIGN KEY (`SDRID`) REFERENCES `servicedomainresource` (`SDRID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `servicedomainformat`
--

/*!40000 ALTER TABLE `servicedomainformat` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicedomainformat` ENABLE KEYS */;


--
-- Definition of table `servicedomainresource`
--

DROP TABLE IF EXISTS `servicedomainresource`;
CREATE TABLE `servicedomainresource` (
  `SDRID` int(10) unsigned NOT NULL auto_increment,
  `SERVICEPROVIDERID` int(10) unsigned NOT NULL default '0',
  `DOMAIN` varchar(50) NOT NULL default '0',
  `GETURL` varchar(100) default '0',
  `POSTURL` varchar(100) default NULL,
  PRIMARY KEY  (`SDRID`),
  KEY `FK_RequestDomainResource_1` (`SERVICEPROVIDERID`),
  CONSTRAINT `FK_RequestDomainResource_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `servicedomainresource`
--

/*!40000 ALTER TABLE `servicedomainresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicedomainresource` ENABLE KEYS */;


--
-- Definition of table `serviceprovider`
--

DROP TABLE IF EXISTS `serviceprovider`;
CREATE TABLE `serviceprovider` (
  `SERVICEPROVIDERID` int(10) unsigned NOT NULL auto_increment,
  `NAME` varchar(60) NOT NULL default '0',
  `TITLE` varchar(50) NOT NULL default '0',
  `ABSTRACTS` varchar(200) default NULL,
  `FEES` varchar(50) default NULL,
  `ACCESSCONSTRAINTS` varchar(50) default NULL,
  `GIVENNAME` varchar(50) NOT NULL default '0',
  `URL` varchar(200) NOT NULL default '0',
  `UPDATEDDATE` datetime NOT NULL default '0000-00-00 00:00:00',
  `REVIEWED` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`SERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `serviceprovider`
--

/*!40000 ALTER TABLE `serviceprovider` DISABLE KEYS */;
/*!40000 ALTER TABLE `serviceprovider` ENABLE KEYS */;


--
-- Definition of table `serviceproviderkeywordlist`
--

DROP TABLE IF EXISTS `serviceproviderkeywordlist`;
CREATE TABLE `serviceproviderkeywordlist` (
  `SERVICEPROVIDERID` int(10) unsigned NOT NULL default '0',
  `KEYWORD` varchar(50) NOT NULL default '0',
  PRIMARY KEY  (`SERVICEPROVIDERID`,`KEYWORD`),
  CONSTRAINT `FK_ServiceProviderKeywordList_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `serviceproviderkeywordlist`
--

/*!40000 ALTER TABLE `serviceproviderkeywordlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `serviceproviderkeywordlist` ENABLE KEYS */;


--
-- Definition of table `srs`
--

DROP TABLE IF EXISTS `srs`;
CREATE TABLE `srs` (
  `SRSID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `SRS` varchar(150) NOT NULL default '0',
  `MINX` double default NULL,
  `MAXX` double default NULL,
  `MINY` double default NULL,
  `MAXY` double default NULL,
  `RESX` double default NULL,
  `RESY` double default NULL,
  PRIMARY KEY  (`SRSID`),
  KEY `FK_SRS_1` (`LAYERID`),
  CONSTRAINT `FK_SRS_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `srs`
--

/*!40000 ALTER TABLE `srs` DISABLE KEYS */;
/*!40000 ALTER TABLE `srs` ENABLE KEYS */;


--
-- Definition of table `style`
--

DROP TABLE IF EXISTS `style`;
CREATE TABLE `style` (
  `STYLEID` int(10) unsigned NOT NULL auto_increment,
  `LAYERID` int(10) unsigned NOT NULL default '0',
  `NAME` varchar(50) NOT NULL default '0',
  `TITLE` varchar(50) NOT NULL default '0',
  `ABSTRACTS` varchar(50) default NULL,
  `STYLEURL` varchar(50) NOT NULL default '0',
  `STYLESHEETURL` varchar(50) default NULL,
  PRIMARY KEY  (`STYLEID`),
  KEY `FK_Style_1` (`LAYERID`),
  CONSTRAINT `FK_Style_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `style`
--

/*!40000 ALTER TABLE `style` DISABLE KEYS */;
/*!40000 ALTER TABLE `style` ENABLE KEYS */;


--
-- Definition of table `styledomainformat`
--

DROP TABLE IF EXISTS `styledomainformat`;
CREATE TABLE `styledomainformat` (
  `SDRID` int(10) unsigned NOT NULL auto_increment,
  `FORMAT` char(45) NOT NULL default '',
  PRIMARY KEY  (`SDRID`,`FORMAT`),
  CONSTRAINT `FK_StyleDomainFormat_1` FOREIGN KEY (`SDRID`) REFERENCES `styledomainresource` (`SDRID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=FIXED;

--
-- Dumping data for table `styledomainformat`
--

/*!40000 ALTER TABLE `styledomainformat` DISABLE KEYS */;
/*!40000 ALTER TABLE `styledomainformat` ENABLE KEYS */;


--
-- Definition of table `styledomainresource`
--

DROP TABLE IF EXISTS `styledomainresource`;
CREATE TABLE `styledomainresource` (
  `SDRID` int(10) unsigned NOT NULL auto_increment,
  `STYLEID` int(10) unsigned NOT NULL default '0',
  `DOMAIN` varchar(45) NOT NULL default '',
  `URL` varchar(200) NOT NULL,
  `WIDTH` varchar(45) default NULL,
  `HEIGHT` varchar(45) default NULL,
  PRIMARY KEY  (`SDRID`),
  KEY `FK_StyleDomainResource_1` (`STYLEID`),
  CONSTRAINT `FK_StyleDomainResource_1` FOREIGN KEY (`STYLEID`) REFERENCES `style` (`STYLEID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `styledomainresource`
--

/*!40000 ALTER TABLE `styledomainresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `styledomainresource` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USERID` int(10) unsigned NOT NULL auto_increment,
  `ORGANIZATIONID` int(10) unsigned NOT NULL default '0',
  `FIRSTNAME` varchar(50) NOT NULL default '0',
  `LASTNAME` varchar(50) NOT NULL default '0',
  `EMAILADDRESS` varchar(50) NOT NULL default '0',
  `USERNAME` varchar(50) NOT NULL default '0',
  `PASSWORD` varchar(50) NOT NULL default '0',
  `ROLE` varchar(50) NOT NULL default '0',
  `PERSONALURL` varchar(100) default NULL,
  `TIMEOUT` datetime default NULL,
  `DEFAULTGETMAP` varchar(2000) default NULL,
  PRIMARY KEY  (`USERID`),
  KEY `FK_User_1` (`ORGANIZATIONID`),
  CONSTRAINT `FK_User_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB; (`ORGANIZATIONID`) REFER `kaartenbali';

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`USERID`,`ORGANIZATIONID`,`FIRSTNAME`,`LASTNAME`,`EMAILADDRESS`,`USERNAME`,`PASSWORD`,`ROLE`,`PERSONALURL`,`TIMEOUT`,`DEFAULTGETMAP`) VALUES 
 (1,1,'beheerder','beheerder','info@b3p.nl','beheerder','beheerder','beheerder','http://www.b3p.nl/',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
