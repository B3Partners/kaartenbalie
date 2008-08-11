-- MySQL dump 10.10
--
-- Host: localhost    Database: kaartenbalie_ev
-- ------------------------------------------------------
-- Server version	5.0.21-Debian_2.dotdeb.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acc_account`
--

DROP TABLE IF EXISTS `acc_account`;
CREATE TABLE `acc_account` (
  `acc_id` int(11) NOT NULL,
  `acc_creditBalance` decimal(15,2) default NULL,
  PRIMARY KEY  (`acc_id`),
  KEY `FK7FE8986FEE7C9324` (`acc_id`),
  CONSTRAINT `FK7FE8986FEE7C9324` FOREIGN KEY (`acc_id`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `acc_account`
--


/*!40000 ALTER TABLE `acc_account` DISABLE KEYS */;
LOCK TABLES `acc_account` WRITE;
INSERT INTO `acc_account` VALUES (1,'0.00');
UNLOCK TABLES;
/*!40000 ALTER TABLE `acc_account` ENABLE KEYS */;

--
-- Table structure for table `acc_layerpricing`
--

DROP TABLE IF EXISTS `acc_layerpricing`;
CREATE TABLE `acc_layerpricing` (
  `lpr_id` int(11) NOT NULL auto_increment,
  `lpr_layerName` varchar(255) default NULL,
  `lpr_serverProviderPrefix` varchar(255) default NULL,
  `lpr_planType` int(11) default NULL,
  `lpr_validFrom` datetime default NULL,
  `lpr_validUntil` datetime default NULL,
  `lpr_creationDate` datetime default NULL,
  `lpr_layerIsFree` bit(1) default NULL,
  `lpr_unitPrice` decimal(9,2) default NULL,
  `lpr_deletionDate` datetime default NULL,
  `lpr_indexCount` int(11) default NULL,
  `lpr_service` varchar(255) default NULL,
  `lpr_operation` varchar(255) default NULL,
  `lpr_minScale` decimal(20,10) default NULL,
  `lpr_maxScale` decimal(20,10) default NULL,
  `lpr_projection` varchar(255) default NULL,
  PRIMARY KEY  (`lpr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `acc_layerpricing`
--


/*!40000 ALTER TABLE `acc_layerpricing` DISABLE KEYS */;
LOCK TABLES `acc_layerpricing` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `acc_layerpricing` ENABLE KEYS */;

--
-- Table structure for table `acc_pricecomp`
--

DROP TABLE IF EXISTS `acc_pricecomp`;
CREATE TABLE `acc_pricecomp` (
  `prc_id` int(11) NOT NULL auto_increment,
  `prc_tra_id` int(11) default NULL,
  `prc_serverProviderPrefix` varchar(255) default NULL,
  `prc_layerName` varchar(255) default NULL,
  `prc_calculationDate` datetime default NULL,
  `prc_planType` int(11) default NULL,
  `lpr_units` decimal(5,2) default NULL,
  `prc_layerIsFree` bit(1) default NULL,
  `prc_method` int(11) default NULL,
  `prc_calculationTime` bigint(20) default NULL,
  `lpr_layerPrice` decimal(12,2) default NULL,
  `prc_service` varchar(255) default NULL,
  `prc_operation` varchar(255) default NULL,
  `prc_scale` decimal(40,10) default NULL,
  `prc_projection` varchar(255) default NULL,
  PRIMARY KEY  (`prc_id`),
  KEY `FK165D199A1C0BBA79` (`prc_tra_id`),
  CONSTRAINT `FK165D199A1C0BBA79` FOREIGN KEY (`prc_tra_id`) REFERENCES `acc_transaction` (`tra_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `acc_pricecomp`
--


/*!40000 ALTER TABLE `acc_pricecomp` DISABLE KEYS */;
LOCK TABLES `acc_pricecomp` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `acc_pricecomp` ENABLE KEYS */;

--
-- Table structure for table `acc_transaction`
--

DROP TABLE IF EXISTS `acc_transaction`;
CREATE TABLE `acc_transaction` (
  `tra_id` int(11) NOT NULL auto_increment,
  `tra_discriminator` varchar(255) NOT NULL,
  `tra_creditAlteration` decimal(12,2) default NULL,
  `tra_transactionDate` datetime default NULL,
  `tra_mutationDate` datetime default NULL,
  `tra_status` int(11) default NULL,
  `tra_type` int(11) default NULL,
  `tra_errorMessage` varchar(255) default NULL,
  `tra_userId` int(11) default NULL,
  `tra_description` varchar(32) default NULL,
  `tra_acc_id` int(11) default NULL,
  `tra_billingAmount` decimal(10,2) default NULL,
  `tra_txExchangeRate` int(11) default NULL,
  PRIMARY KEY  (`tra_id`),
  KEY `FKFC20F02025126DF8` (`tra_acc_id`),
  CONSTRAINT `FKFC20F02025126DF8` FOREIGN KEY (`tra_acc_id`) REFERENCES `acc_account` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `acc_transaction`
--


/*!40000 ALTER TABLE `acc_transaction` DISABLE KEYS */;
LOCK TABLES `acc_transaction` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `acc_transaction` ENABLE KEYS */;

--
-- Table structure for table `attribution`
--

DROP TABLE IF EXISTS `attribution`;
CREATE TABLE `attribution` (
  `LAYERID` int(11) NOT NULL,
  `TITLE` varchar(50) default NULL,
  `ATTRIBUTIONURL` varchar(4000) default NULL,
  `LOGOURL` varchar(4000) default NULL,
  `LOGOWIDTH` varchar(50) default NULL,
  `LOGOHEIGHT` varchar(50) default NULL,
  PRIMARY KEY  (`LAYERID`),
  CONSTRAINT `FK_Attribution_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `attribution`
--


/*!40000 ALTER TABLE `attribution` DISABLE KEYS */;
LOCK TABLES `attribution` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `attribution` ENABLE KEYS */;

--
-- Table structure for table `contactinformation`
--

DROP TABLE IF EXISTS `contactinformation`;
CREATE TABLE `contactinformation` (
  `SERVICEPROVIDERID` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `contactinformation`
--


/*!40000 ALTER TABLE `contactinformation` DISABLE KEYS */;
LOCK TABLES `contactinformation` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `contactinformation` ENABLE KEYS */;

--
-- Table structure for table `dimensions`
--

DROP TABLE IF EXISTS `dimensions`;
CREATE TABLE `dimensions` (
  `DIMENSIONSID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `dimensions`
--


/*!40000 ALTER TABLE `dimensions` DISABLE KEYS */;
LOCK TABLES `dimensions` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `dimensions` ENABLE KEYS */;

--
-- Table structure for table `exceptions`
--

DROP TABLE IF EXISTS `exceptions`;
CREATE TABLE `exceptions` (
  `SERVICEPROVIDERID` int(11) NOT NULL,
  `FORMAT` varchar(50) NOT NULL,
  PRIMARY KEY  (`SERVICEPROVIDERID`,`FORMAT`),
  CONSTRAINT `FK_Exceptions_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `exceptions`
--


/*!40000 ALTER TABLE `exceptions` DISABLE KEYS */;
LOCK TABLES `exceptions` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `exceptions` ENABLE KEYS */;

--
-- Table structure for table `identifier`
--

DROP TABLE IF EXISTS `identifier`;
CREATE TABLE `identifier` (
  `IDENTIFIERID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
  `AUTHORITYNAME` varchar(50) NOT NULL,
  `AUTHORITYURL` varchar(4000) NOT NULL,
  PRIMARY KEY  (`IDENTIFIERID`),
  KEY `FK_Identifier_1` (`LAYERID`),
  CONSTRAINT `FK_Identifier_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `identifier`
--


/*!40000 ALTER TABLE `identifier` DISABLE KEYS */;
LOCK TABLES `identifier` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `identifier` ENABLE KEYS */;

--
-- Table structure for table `kb_uniquenumber`
--

DROP TABLE IF EXISTS `kb_uniquenumber`;
CREATE TABLE `kb_uniquenumber` (
  `unn_id` int(11) NOT NULL auto_increment,
  `unn_indexName` varchar(255) default NULL,
  `unn_indexCount` int(11) default NULL,
  PRIMARY KEY  (`unn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `kb_uniquenumber`
--


/*!40000 ALTER TABLE `kb_uniquenumber` DISABLE KEYS */;
LOCK TABLES `kb_uniquenumber` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `kb_uniquenumber` ENABLE KEYS */;

--
-- Table structure for table `layer`
--

DROP TABLE IF EXISTS `layer`;
CREATE TABLE `layer` (
  `LAYERID` int(11) NOT NULL auto_increment,
  `SERVICEPROVIDERID` int(11) default NULL,
  `NAME` varchar(50) default NULL,
  `TITLE` varchar(200) NOT NULL,
  `ABSTRACTS` text,
  `QUERYABLE` varchar(50) default NULL,
  `CASCADED` varchar(50) default NULL,
  `OPAQUE` varchar(50) default NULL,
  `NOSUBSETS` varchar(50) default NULL,
  `FIXEDWIDTH` varchar(50) default NULL,
  `FIXEDHEIGHT` varchar(50) default NULL,
  `SCALEHINTMIN` varchar(50) default NULL,
  `SCALEHINTMAX` varchar(50) default NULL,
  `PARENTID` int(11) default NULL,
  `METADATA` text,
  PRIMARY KEY  (`LAYERID`),
  KEY `FK_Layer_1` (`SERVICEPROVIDERID`),
  KEY `FK_Layer_2` (`PARENTID`),
  CONSTRAINT `FK_Layer_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`),
  CONSTRAINT `FK_Layer_2` FOREIGN KEY (`PARENTID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `layer`
--


/*!40000 ALTER TABLE `layer` DISABLE KEYS */;
LOCK TABLES `layer` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `layer` ENABLE KEYS */;

--
-- Table structure for table `layerdomainformat`
--

DROP TABLE IF EXISTS `layerdomainformat`;
CREATE TABLE `layerdomainformat` (
  `LDRID` int(11) NOT NULL,
  `FORMAT` varchar(100) NOT NULL,
  PRIMARY KEY  (`LDRID`,`FORMAT`),
  CONSTRAINT `FK_LayerDomainFormat_1` FOREIGN KEY (`LDRID`) REFERENCES `layerdomainresource` (`LDRID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `layerdomainformat`
--


/*!40000 ALTER TABLE `layerdomainformat` DISABLE KEYS */;
LOCK TABLES `layerdomainformat` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `layerdomainformat` ENABLE KEYS */;

--
-- Table structure for table `layerdomainresource`
--

DROP TABLE IF EXISTS `layerdomainresource`;
CREATE TABLE `layerdomainresource` (
  `LDRID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
  `DOMAIN` varchar(50) NOT NULL,
  `URL` varchar(4000) NOT NULL,
  PRIMARY KEY  (`LDRID`),
  KEY `FK_LayerDomainResource_1` (`LAYERID`),
  CONSTRAINT `FK_LayerDomainResource_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `layerdomainresource`
--


/*!40000 ALTER TABLE `layerdomainresource` DISABLE KEYS */;
LOCK TABLES `layerdomainresource` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `layerdomainresource` ENABLE KEYS */;

--
-- Table structure for table `layerkeywordlist`
--

DROP TABLE IF EXISTS `layerkeywordlist`;
CREATE TABLE `layerkeywordlist` (
  `KEYWORDLISTID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
  `KEYWORD` varchar(50) NOT NULL,
  PRIMARY KEY  (`KEYWORDLISTID`),
  KEY `FK_LayerKeywordList_1` (`LAYERID`),
  CONSTRAINT `FK_LayerKeywordList_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `layerkeywordlist`
--


/*!40000 ALTER TABLE `layerkeywordlist` DISABLE KEYS */;
LOCK TABLES `layerkeywordlist` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `layerkeywordlist` ENABLE KEYS */;

--
-- Table structure for table `mon_clientrequest`
--

DROP TABLE IF EXISTS `mon_clientrequest`;
CREATE TABLE `mon_clientrequest` (
  `clr_id` int(11) NOT NULL auto_increment,
  `clr_clientRequestURI` varchar(4000) default NULL,
  `clr_timeStamp` datetime default NULL,
  `clr_userId` int(11) default NULL,
  `clr_organizationId` int(11) default NULL,
  `clr_method` varchar(255) NOT NULL,
  `clr_clientIp` varchar(255) NOT NULL,
  `clr_service` varchar(10) default NULL,
  `clr_operation` varchar(50) default NULL,
  `clr_exceptionClass` varchar(255) default NULL,
  `clr_exceptionMessage` varchar(4000) default NULL,
  PRIMARY KEY  (`clr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mon_clientrequest`
--


/*!40000 ALTER TABLE `mon_clientrequest` DISABLE KEYS */;
LOCK TABLES `mon_clientrequest` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `mon_clientrequest` ENABLE KEYS */;

--
-- Table structure for table `mon_requestoperation`
--

DROP TABLE IF EXISTS `mon_requestoperation`;
CREATE TABLE `mon_requestoperation` (
  `rqo_id` int(11) NOT NULL auto_increment,
  `rqo_discriminator` varchar(255) NOT NULL,
  `rqo_clr_id` int(11) default NULL,
  `rqo_duration` bigint(20) default NULL,
  `rqo_msSinceRequestStart` bigint(20) default NULL,
  `rqo_numberOfImages` int(11) default NULL,
  `rqo_dataSize` bigint(20) default NULL,
  `rqo_bytesReceivedFromUser` int(11) default NULL,
  `rqo_bytesSendToUser` int(11) default NULL,
  PRIMARY KEY  (`rqo_id`),
  KEY `FKCE03888B8A8C8DEC` (`rqo_clr_id`),
  CONSTRAINT `FKCE03888B8A8C8DEC` FOREIGN KEY (`rqo_clr_id`) REFERENCES `mon_clientrequest` (`clr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mon_requestoperation`
--


/*!40000 ALTER TABLE `mon_requestoperation` DISABLE KEYS */;
LOCK TABLES `mon_requestoperation` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `mon_requestoperation` ENABLE KEYS */;

--
-- Table structure for table `mon_serviceproviderrequest`
--

DROP TABLE IF EXISTS `mon_serviceproviderrequest`;
CREATE TABLE `mon_serviceproviderrequest` (
  `spr_id` int(11) NOT NULL auto_increment,
  `spr_discriminator` varchar(255) NOT NULL,
  `spr_clr_id` int(11) default NULL,
  `spr_bytesSend` bigint(20) default NULL,
  `spr_bytesReceived` bigint(20) default NULL,
  `spr_responseStatus` int(11) default NULL,
  `spr_providerRequestURI` varchar(4000) default NULL,
  `spr_requestResponseTime` bigint(20) default NULL,
  `spr_msSinceRequestStart` bigint(20) default NULL,
  `spr_wmsVersion` varchar(255) default NULL,
  `spr_srs` varchar(255) default NULL,
  `spr_width` int(11) default NULL,
  `spr_height` int(11) default NULL,
  `spr_format` varchar(255) default NULL,
  `spr_serviceProviderId` int(11) default NULL,
  `spr_exceptionClass` varchar(255) default NULL,
  `spr_exceptionMessage` varchar(4000) default NULL,
  PRIMARY KEY  (`spr_id`),
  KEY `FK712CEEBC223516A7` (`spr_clr_id`),
  CONSTRAINT `FK712CEEBC223516A7` FOREIGN KEY (`spr_clr_id`) REFERENCES `mon_clientrequest` (`clr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mon_serviceproviderrequest`
--


/*!40000 ALTER TABLE `mon_serviceproviderrequest` DISABLE KEYS */;
LOCK TABLES `mon_serviceproviderrequest` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `mon_serviceproviderrequest` ENABLE KEYS */;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `ORGANIZATIONID` int(11) NOT NULL auto_increment,
  `NAME` varchar(50) NOT NULL,
  `STREET` varchar(50) default NULL,
  `NUMBER` varchar(10) default NULL,
  `ADDITION` varchar(10) default NULL,
  `PROVINCE` varchar(50) default NULL,
  `COUNTRY` varchar(50) default NULL,
  `POSTBOX` varchar(50) default NULL,
  `BILLINGADDRESS` varchar(50) default NULL,
  `VISITORSADDRESS` varchar(50) default NULL,
  `TELEPHONE` varchar(50) NOT NULL,
  `FAX` varchar(50) default NULL,
  `POSTALCODE` varchar(45) default NULL,
  `HASVALIDGETCAPABILITIES` tinyint(1) NOT NULL,
  `BBOX` varchar(50) default NULL,
  `CODE` varchar(50) default NULL,
  `ALLOWACCOUNTINGLAYERS` tinyint(1) NOT NULL,
  PRIMARY KEY  (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `organization`
--


/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
LOCK TABLES `organization` WRITE;
INSERT INTO `organization` VALUES (1,'Beheerders',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1234567890',NULL,NULL,1,NULL,NULL,0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;

--
-- Table structure for table `organizationlayer`
--

DROP TABLE IF EXISTS `organizationlayer`;
CREATE TABLE `organizationlayer` (
  `ORGANIZATIONID` int(11) NOT NULL,
  `LAYERID` int(11) NOT NULL,
  PRIMARY KEY  (`ORGANIZATIONID`,`LAYERID`),
  KEY `FK_OrganizationLayer_2` (`LAYERID`),
  CONSTRAINT `FK_OrganizationLayer_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`),
  CONSTRAINT `FK_OrganizationLayer_2` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `organizationlayer`
--


/*!40000 ALTER TABLE `organizationlayer` DISABLE KEYS */;
LOCK TABLES `organizationlayer` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `organizationlayer` ENABLE KEYS */;

--
-- Table structure for table `rep_dailyusage`
--

DROP TABLE IF EXISTS `rep_dailyusage`;
CREATE TABLE `rep_dailyusage` (
  `dyu_id` int(11) NOT NULL auto_increment,
  `dyu_usd_id` int(11) default NULL,
  `dyu_date` datetime default NULL,
  `dyu_hour` int(11) default NULL,
  `dyu_dataUsage` bigint(20) default NULL,
  `dyu_hits` bigint(20) default NULL,
  PRIMARY KEY  (`dyu_id`),
  KEY `FKA947900A6971056F` (`dyu_usd_id`),
  CONSTRAINT `FKA947900A6971056F` FOREIGN KEY (`dyu_usd_id`) REFERENCES `rep_usagedetails` (`usd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_dailyusage`
--


/*!40000 ALTER TABLE `rep_dailyusage` DISABLE KEYS */;
LOCK TABLES `rep_dailyusage` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_dailyusage` ENABLE KEYS */;

--
-- Table structure for table `rep_report`
--

DROP TABLE IF EXISTS `rep_report`;
CREATE TABLE `rep_report` (
  `rep_id` int(11) NOT NULL auto_increment,
  `rep_discriminator` varchar(255) NOT NULL,
  `rep_reportDate` datetime default NULL,
  `rep_processingTime` bigint(20) default NULL,
  `rep_startDate` datetime default NULL,
  `rep_endDate` datetime default NULL,
  `rpd_org_id` int(11) default NULL,
  `rep_organizationId` int(11) default NULL,
  PRIMARY KEY  (`rep_id`),
  KEY `FK23F41F16E340125A` (`rpd_org_id`),
  CONSTRAINT `FK23F41F16E340125A` FOREIGN KEY (`rpd_org_id`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_report`
--


/*!40000 ALTER TABLE `rep_report` DISABLE KEYS */;
LOCK TABLES `rep_report` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_report` ENABLE KEYS */;

--
-- Table structure for table `rep_reportdata`
--

DROP TABLE IF EXISTS `rep_reportdata`;
CREATE TABLE `rep_reportdata` (
  `rpd_id` int(11) NOT NULL auto_increment,
  `rpd_discriminator` varchar(255) NOT NULL,
  `rpd_rep_id` int(11) default NULL,
  `rpd_minHour` int(11) default NULL,
  `rpd_maxHour` int(11) default NULL,
  `rpd_ctoHits` bigint(20) default NULL,
  `rpd_ctoAverageResponse` double default NULL,
  `rpd_cioHits` bigint(20) default NULL,
  `rpd_cioAverageResponse` double default NULL,
  `rpd_cxoHits` bigint(20) default NULL,
  `rpd_cxoData` bigint(20) default NULL,
  `rpd_cxoAverageResponse` double default NULL,
  `rpd_roHits` bigint(20) default NULL,
  `rpd_roUpload` bigint(20) default NULL,
  `rpd_roDownload` bigint(20) default NULL,
  `rpd_roAverageResponse` double default NULL,
  PRIMARY KEY  (`rpd_id`),
  KEY `FKC03193C066F0FC6` (`rpd_rep_id`),
  CONSTRAINT `FKC03193C066F0FC6` FOREIGN KEY (`rpd_rep_id`) REFERENCES `rep_report` (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_reportdata`
--


/*!40000 ALTER TABLE `rep_reportdata` DISABLE KEYS */;
LOCK TABLES `rep_reportdata` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_reportdata` ENABLE KEYS */;

--
-- Table structure for table `rep_rowvalue`
--

DROP TABLE IF EXISTS `rep_rowvalue`;
CREATE TABLE `rep_rowvalue` (
  `rva_id` int(11) NOT NULL auto_increment,
  `rva_tro_id` int(11) default NULL,
  `rva_rowValue` varchar(255) default NULL,
  `rva_valueOrder` int(11) default NULL,
  PRIMARY KEY  (`rva_id`),
  KEY `FK14B3C939A32715CE` (`rva_tro_id`),
  CONSTRAINT `FK14B3C939A32715CE` FOREIGN KEY (`rva_tro_id`) REFERENCES `rep_tablerow` (`tro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_rowvalue`
--


/*!40000 ALTER TABLE `rep_rowvalue` DISABLE KEYS */;
LOCK TABLES `rep_rowvalue` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_rowvalue` ENABLE KEYS */;

--
-- Table structure for table `rep_status`
--

DROP TABLE IF EXISTS `rep_status`;
CREATE TABLE `rep_status` (
  `sta_id` int(11) NOT NULL auto_increment,
  `sta_reportId` int(11) default NULL,
  `sta_creationDate` datetime default NULL,
  `sta_state` int(11) default NULL,
  `sta_statusMessage` varchar(4000) default NULL,
  `sta_org_id` int(11) default NULL,
  PRIMARY KEY  (`sta_id`),
  KEY `FK267599D4F0F7B8E0` (`sta_org_id`),
  CONSTRAINT `FK267599D4F0F7B8E0` FOREIGN KEY (`sta_org_id`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_status`
--


/*!40000 ALTER TABLE `rep_status` DISABLE KEYS */;
LOCK TABLES `rep_status` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_status` ENABLE KEYS */;

--
-- Table structure for table `rep_table`
--

DROP TABLE IF EXISTS `rep_table`;
CREATE TABLE `rep_table` (
  `tab_id` int(11) NOT NULL auto_increment,
  `tab_rep_id` int(11) default NULL,
  `rep_tableName` varchar(255) default NULL,
  PRIMARY KEY  (`tab_id`),
  KEY `FKCFB6ADACFD6ECBDE` (`tab_rep_id`),
  CONSTRAINT `FKCFB6ADACFD6ECBDE` FOREIGN KEY (`tab_rep_id`) REFERENCES `rep_report` (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_table`
--


/*!40000 ALTER TABLE `rep_table` DISABLE KEYS */;
LOCK TABLES `rep_table` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_table` ENABLE KEYS */;

--
-- Table structure for table `rep_tablerow`
--

DROP TABLE IF EXISTS `rep_tablerow`;
CREATE TABLE `rep_tablerow` (
  `tro_id` int(11) NOT NULL auto_increment,
  `tro_tab_id` int(11) default NULL,
  `tro_header` bit(1) default NULL,
  `tro_rowOrder` int(11) default NULL,
  PRIMARY KEY  (`tro_id`),
  KEY `FKDB7E1CAEEDED1520` (`tro_tab_id`),
  CONSTRAINT `FKDB7E1CAEEDED1520` FOREIGN KEY (`tro_tab_id`) REFERENCES `rep_table` (`tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_tablerow`
--


/*!40000 ALTER TABLE `rep_tablerow` DISABLE KEYS */;
LOCK TABLES `rep_tablerow` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_tablerow` ENABLE KEYS */;

--
-- Table structure for table `rep_usagedetails`
--

DROP TABLE IF EXISTS `rep_usagedetails`;
CREATE TABLE `rep_usagedetails` (
  `usd_id` int(11) NOT NULL auto_increment,
  `usd_rpd_id` int(11) default NULL,
  `usd_userId` int(11) default NULL,
  PRIMARY KEY  (`usd_id`),
  KEY `FK312665E3F5CA8D83` (`usd_rpd_id`),
  CONSTRAINT `FK312665E3F5CA8D83` FOREIGN KEY (`usd_rpd_id`) REFERENCES `rep_reportdata` (`rpd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_usagedetails`
--


/*!40000 ALTER TABLE `rep_usagedetails` DISABLE KEYS */;
LOCK TABLES `rep_usagedetails` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_usagedetails` ENABLE KEYS */;

--
-- Table structure for table `rep_users`
--

DROP TABLE IF EXISTS `rep_users`;
CREATE TABLE `rep_users` (
  `rpd_usr_id` int(11) NOT NULL,
  `usr_id` int(11) NOT NULL,
  PRIMARY KEY  (`rpd_usr_id`,`usr_id`),
  KEY `FKCFCCFFE6C53CB6F` (`rpd_usr_id`),
  CONSTRAINT `FKCFCCFFE6C53CB6F` FOREIGN KEY (`rpd_usr_id`) REFERENCES `rep_report` (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rep_users`
--


/*!40000 ALTER TABLE `rep_users` DISABLE KEYS */;
LOCK TABLES `rep_users` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `rep_users` ENABLE KEYS */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `ROLEID` int(11) NOT NULL auto_increment,
  `ROLE` varchar(45) NOT NULL,
  PRIMARY KEY  (`ROLEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `roles`
--


/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES (1,'beheerder'),(2,'organisatiebeheerder'),(3,'themabeheerder'),(4,'gebruiker'),(5,'demogebruiker');
UNLOCK TABLES;
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

--
-- Table structure for table `servicedomainformat`
--

DROP TABLE IF EXISTS `servicedomainformat`;
CREATE TABLE `servicedomainformat` (
  `SDRID` int(11) NOT NULL,
  `FORMAT` varchar(100) NOT NULL,
  PRIMARY KEY  (`SDRID`,`FORMAT`),
  CONSTRAINT `FK_ServiceDomainFormat_1` FOREIGN KEY (`SDRID`) REFERENCES `servicedomainresource` (`SDRID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `servicedomainformat`
--


/*!40000 ALTER TABLE `servicedomainformat` DISABLE KEYS */;
LOCK TABLES `servicedomainformat` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `servicedomainformat` ENABLE KEYS */;

--
-- Table structure for table `servicedomainresource`
--

DROP TABLE IF EXISTS `servicedomainresource`;
CREATE TABLE `servicedomainresource` (
  `SDRID` int(11) NOT NULL auto_increment,
  `SERVICEPROVIDERID` int(11) NOT NULL,
  `DOMAIN` varchar(50) NOT NULL,
  `GETURL` varchar(4000) default NULL,
  `POSTURL` varchar(4000) default NULL,
  PRIMARY KEY  (`SDRID`),
  KEY `FK_RequestDomainResource_1` (`SERVICEPROVIDERID`),
  CONSTRAINT `FK_RequestDomainResource_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `servicedomainresource`
--


/*!40000 ALTER TABLE `servicedomainresource` DISABLE KEYS */;
LOCK TABLES `servicedomainresource` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `servicedomainresource` ENABLE KEYS */;

--
-- Table structure for table `serviceprovider`
--

DROP TABLE IF EXISTS `serviceprovider`;
CREATE TABLE `serviceprovider` (
  `SERVICEPROVIDERID` int(11) NOT NULL auto_increment,
  `NAME` varchar(60) NOT NULL,
  `TITLE` varchar(50) NOT NULL,
  `ABSTRACTS` text,
  `FEES` text,
  `ACCESSCONSTRAINTS` text,
  `GIVENNAME` varchar(50) NOT NULL,
  `URL` varchar(4000) default NULL,
  `UPDATEDDATE` datetime NOT NULL,
  `WMSVERSION` varchar(50) NOT NULL,
  `ABBR` varchar(60) NOT NULL,
  PRIMARY KEY  (`SERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `serviceprovider`
--


/*!40000 ALTER TABLE `serviceprovider` DISABLE KEYS */;
LOCK TABLES `serviceprovider` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `serviceprovider` ENABLE KEYS */;

--
-- Table structure for table `serviceproviderkeywordlist`
--

DROP TABLE IF EXISTS `serviceproviderkeywordlist`;
CREATE TABLE `serviceproviderkeywordlist` (
  `SERVICEPROVIDERID` int(11) NOT NULL,
  `KEYWORD` varchar(50) NOT NULL,
  PRIMARY KEY  (`SERVICEPROVIDERID`,`KEYWORD`),
  CONSTRAINT `FK_ServiceProviderKeywordList_1` FOREIGN KEY (`SERVICEPROVIDERID`) REFERENCES `serviceprovider` (`SERVICEPROVIDERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `serviceproviderkeywordlist`
--


/*!40000 ALTER TABLE `serviceproviderkeywordlist` DISABLE KEYS */;
LOCK TABLES `serviceproviderkeywordlist` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `serviceproviderkeywordlist` ENABLE KEYS */;

--
-- Table structure for table `srs`
--

DROP TABLE IF EXISTS `srs`;
CREATE TABLE `srs` (
  `SRSID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
  `SRS` varchar(150) default NULL,
  `MINX` varchar(50) default NULL,
  `MAXX` varchar(50) default NULL,
  `MINY` varchar(50) default NULL,
  `MAXY` varchar(50) default NULL,
  `RESX` varchar(50) default NULL,
  `RESY` varchar(50) default NULL,
  PRIMARY KEY  (`SRSID`),
  KEY `FK_SRS_1` (`LAYERID`),
  CONSTRAINT `FK_SRS_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `srs`
--


/*!40000 ALTER TABLE `srs` DISABLE KEYS */;
LOCK TABLES `srs` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `srs` ENABLE KEYS */;

--
-- Table structure for table `style`
--

DROP TABLE IF EXISTS `style`;
CREATE TABLE `style` (
  `STYLEID` int(11) NOT NULL auto_increment,
  `LAYERID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TITLE` varchar(50) NOT NULL,
  `ABSTRACTS` text,
  PRIMARY KEY  (`STYLEID`),
  KEY `FK_Style_1` (`LAYERID`),
  CONSTRAINT `FK_Style_1` FOREIGN KEY (`LAYERID`) REFERENCES `layer` (`LAYERID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `style`
--


/*!40000 ALTER TABLE `style` DISABLE KEYS */;
LOCK TABLES `style` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `style` ENABLE KEYS */;

--
-- Table structure for table `styledomainformat`
--

DROP TABLE IF EXISTS `styledomainformat`;
CREATE TABLE `styledomainformat` (
  `SDRID` int(11) NOT NULL auto_increment,
  `FORMAT` varchar(45) NOT NULL,
  PRIMARY KEY  (`SDRID`,`FORMAT`),
  CONSTRAINT `FK_StyleDomainFormat_1` FOREIGN KEY (`SDRID`) REFERENCES `styledomainresource` (`SDRID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `styledomainformat`
--


/*!40000 ALTER TABLE `styledomainformat` DISABLE KEYS */;
LOCK TABLES `styledomainformat` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `styledomainformat` ENABLE KEYS */;

--
-- Table structure for table `styledomainresource`
--

DROP TABLE IF EXISTS `styledomainresource`;
CREATE TABLE `styledomainresource` (
  `SDRID` int(11) NOT NULL auto_increment,
  `STYLEID` int(11) NOT NULL,
  `DOMAIN` varchar(45) NOT NULL,
  `URL` varchar(4000) default NULL,
  `WIDTH` varchar(45) default NULL,
  `HEIGHT` varchar(45) default NULL,
  PRIMARY KEY  (`SDRID`),
  KEY `FK_StyleDomainResource_1` (`STYLEID`),
  CONSTRAINT `FK_StyleDomainResource_1` FOREIGN KEY (`STYLEID`) REFERENCES `style` (`STYLEID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `styledomainresource`
--


/*!40000 ALTER TABLE `styledomainresource` DISABLE KEYS */;
LOCK TABLES `styledomainresource` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `styledomainresource` ENABLE KEYS */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USERID` int(11) NOT NULL auto_increment,
  `ORGANIZATIONID` int(11) NOT NULL,
  `FIRSTNAME` varchar(50) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  `EMAILADDRESS` varchar(50) NOT NULL,
  `USERNAME` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `PERSONALURL` varchar(4000) default NULL,
  `TIMEOUT` datetime default NULL,
  `DEFAULTGETMAP` varchar(4000) default NULL,
  PRIMARY KEY  (`USERID`),
  KEY `FK_User_1` (`ORGANIZATIONID`),
  CONSTRAINT `FK_User_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--


/*!40000 ALTER TABLE `user` DISABLE KEYS */;
LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,1,'beheerder','beheerder','info@b3partners.nl','beheerder','JMzUf6QkCdc%3D','http://dev.b3p.nl/kaartenbalie_ev/services/a0b3d6ef13be91ff42b89b33f116b823','2009-01-01 00:00:00','http://localhost:8084/kaartenbalie/wms/f9530b2d908a3494950a246ca309c6ba?VERSION=1.1.1&REQUEST=GetMap&LAYERS=demo_bron_nieuwekaart,demo_buurten_2006,demo_plan_lijnen,demo_plan_polygonen,demo_gemeenten_2006,demo_wijken_2006&BBOX=12000,304000,280000,620000&SRS=EPSG:25832&HEIGHT=400&WIDTH=300&FORMAT=image/gif&BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&STYLES=');
UNLOCK TABLES;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

--
-- Table structure for table `userip`
--

DROP TABLE IF EXISTS `userip`;
CREATE TABLE `userip` (
  `USERIPID` int(11) NOT NULL auto_increment,
  `USERID` int(11) NOT NULL,
  `IPADDRESS` varchar(45) NOT NULL,
  PRIMARY KEY  (`USERIPID`),
  KEY `FK_userip_1` (`USERID`),
  CONSTRAINT `FK_userip_1` FOREIGN KEY (`USERID`) REFERENCES `user` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userip`
--


/*!40000 ALTER TABLE `userip` DISABLE KEYS */;
LOCK TABLES `userip` WRITE;
INSERT INTO `userip` VALUES (76,1,'213.84.161.200');
UNLOCK TABLES;
/*!40000 ALTER TABLE `userip` ENABLE KEYS */;

--
-- Table structure for table `userroles`
--

DROP TABLE IF EXISTS `userroles`;
CREATE TABLE `userroles` (
  `USERID` int(11) NOT NULL auto_increment,
  `ROLEID` int(11) NOT NULL,
  PRIMARY KEY  (`USERID`,`ROLEID`),
  KEY `FK_userroles_2` (`ROLEID`),
  CONSTRAINT `FK_userroles_1` FOREIGN KEY (`USERID`) REFERENCES `user` (`USERID`),
  CONSTRAINT `FK_userroles_2` FOREIGN KEY (`ROLEID`) REFERENCES `roles` (`ROLEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userroles`
--


/*!40000 ALTER TABLE `userroles` DISABLE KEYS */;
LOCK TABLES `userroles` WRITE;
INSERT INTO `userroles` VALUES (1,1),(1,2),(1,3),(1,4);
UNLOCK TABLES;
/*!40000 ALTER TABLE `userroles` ENABLE KEYS */;

--
-- Table structure for table `wfs_layer`
--

DROP TABLE IF EXISTS `wfs_layer`;
CREATE TABLE `wfs_layer` (
  `WFSLAYERID` int(11) NOT NULL auto_increment,
  `WFSSERVICEPROVIDERID` int(11) default NULL,
  `NAME` varchar(50) default NULL,
  `TITLE` varchar(200) NOT NULL,
  `METADATA` text,
  PRIMARY KEY  (`WFSLAYERID`),
  KEY `FK_wfs_layer_1` (`WFSSERVICEPROVIDERID`),
  CONSTRAINT `FK_wfs_layer_1` FOREIGN KEY (`WFSSERVICEPROVIDERID`) REFERENCES `wfs_serviceprovider` (`WFSSERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `wfs_layer`
--


/*!40000 ALTER TABLE `wfs_layer` DISABLE KEYS */;
LOCK TABLES `wfs_layer` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `wfs_layer` ENABLE KEYS */;

--
-- Table structure for table `wfs_organizationlayer`
--

DROP TABLE IF EXISTS `wfs_organizationlayer`;
CREATE TABLE `wfs_organizationlayer` (
  `ORGANIZATIONID` int(11) NOT NULL,
  `WFSLAYERID` int(11) NOT NULL,
  PRIMARY KEY  (`ORGANIZATIONID`,`WFSLAYERID`),
  KEY `FK_wfs_organisationlayer_1` (`WFSLAYERID`),
  CONSTRAINT `FK_wfs_organisationlayer_1` FOREIGN KEY (`WFSLAYERID`) REFERENCES `wfs_layer` (`WFSLAYERID`),
  CONSTRAINT `FK_wfs_organisationlayer_2` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `wfs_organizationlayer`
--


/*!40000 ALTER TABLE `wfs_organizationlayer` DISABLE KEYS */;
LOCK TABLES `wfs_organizationlayer` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `wfs_organizationlayer` ENABLE KEYS */;

--
-- Table structure for table `wfs_serviceprovider`
--

DROP TABLE IF EXISTS `wfs_serviceprovider`;
CREATE TABLE `wfs_serviceprovider` (
  `WFSSERVICEPROVIDERID` int(11) NOT NULL auto_increment,
  `NAME` varchar(60) NOT NULL,
  `TITLE` varchar(50) NOT NULL,
  `GIVENNAME` varchar(50) default NULL,
  `URL` varchar(4000) default NULL,
  `UPDATEDDATE` datetime default NULL,
  `WFSVERSION` varchar(50) default NULL,
  `ABBR` varchar(60) default NULL,
  PRIMARY KEY  (`WFSSERVICEPROVIDERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `wfs_serviceprovider`
--


/*!40000 ALTER TABLE `wfs_serviceprovider` DISABLE KEYS */;
LOCK TABLES `wfs_serviceprovider` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `wfs_serviceprovider` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

