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
INSERT INTO `attribution` (`LAYERID`,`TITLE`,`ATTRIBUTIONURL`,`LOGOURL`,`LOGOWIDTH`,`LOGOHEIGHT`) VALUES 
 (705,'European Space Agency','http://mapserv2.esrin.esa.it/cubestor/cubeview/esa_esrin.jpg',NULL,'261','68'),
 (708,'World Map','http://mapserv2.esrin.esa.it/cubestor/cubeserv/wmsLogo.gif',NULL,'67','38'),
 (729,'European Space Agency','http://mapserv2.esrin.esa.it/cubestor/cubeview/esa_esrin.jpg',NULL,'261','68'),
 (825,'CubeWerx Inc.','http://www.cubewerx.com/cubewerx.gif',NULL,'89','97'),
 (826,'CubeWerx Inc.','http://www.cubewerx.com/cubewerx.gif',NULL,'89','97'),
 (894,'CubeWerx Inc.','http://demo.cubewerx.com/datastore_icons/gsc_100.gif',NULL,'100','100');
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
INSERT INTO `contactinformation` (`SERVICEPROVIDERID`,`CONTACTPERSON`,`CONTACTPOSITION`,`ADDRESS`,`ADDRESSTYPE`,`POSTCODE`,`CITY`,`STATEORPROVINCE`,`COUNTRY`,`VOICETELEPHONE`,`FASCIMILETELEPHONE`,`EMAILADDRESS`) VALUES 
 (30,'Chris van Lith','Software Developer','Zonnebaan 12C','Postal','3542 EC','Utrecht','Utrecht','The Netherlands','0(031) 30 214 20 81','','info@b3p.nl'),
 (31,'Chris van Lith','Software Developer','Zonnebaan 12C','Postal','3542 EC','Utrecht','Utrecht','The Netherlands','0(031) 30 214 20 81','','info@b3p.nl'),
 (32,'Chris van Lith','Software Developer','Zonnebaan 12C','Postal','3542 EC','Utrecht','Utrecht','The Netherlands','0(031) 30 214 20 81','','info@b3p.nl'),
 (36,'Chris van Lith','Software Developer','Zonnebaan 12C','Postal','3542 EC','Utrecht','Utrecht','The Netherlands','0(031) 30 214 20 81','','info@b3p.nl');
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
INSERT INTO `exceptions` (`SERVICEPROVIDERID`,`FORMAT`) VALUES 
 (30,'application/vnd.ogc.se_blank'),
 (30,'application/vnd.ogc.se_inimage'),
 (30,'application/vnd.ogc.se_xml'),
 (31,'application/vnd.ogc.se_blank'),
 (31,'application/vnd.ogc.se_inimage'),
 (31,'application/vnd.ogc.se_xml'),
 (32,'application/vnd.ogc.se_blank'),
 (32,'application/vnd.ogc.se_inimage'),
 (32,'application/vnd.ogc.se_xml'),
 (36,'application/vnd.ogc.se_blank'),
 (36,'application/vnd.ogc.se_inimage'),
 (36,'application/vnd.ogc.se_xml'),
 (36,'text/html'),
 (36,'text/plain');
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
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (497,3.31325,47.9748,3.31325,47.9748),
 (498,-180,-90,180,90),
 (500,4.57751,52.4504,4.71404,52.505),
 (501,4.57751,52.4504,4.71404,52.505),
 (503,4.58621,52.4867,4.72041,52.534),
 (504,4.57694,52.4828,4.72511,52.5363),
 (505,4.57694,52.4828,4.72511,52.5363),
 (507,4.51708,52.4092,4.7409,52.4947),
 (508,4.51708,52.4092,4.7409,52.4947),
 (509,-180,-90,180,90),
 (510,3.51498,50.7427,7.2031,53.2183),
 (511,1.03347,49.2361,9.48071,54.717),
 (513,4.68633,52.4917,4.78389,52.5546),
 (514,4.68861,52.4944,4.78376,52.5541),
 (515,4.68633,52.4917,4.78389,52.5546),
 (516,-180,-90,180,90),
 (517,3.31325,47.9748,3.31325,47.9748),
 (518,3.12456,47.9748,4.97264,52.1283),
 (519,3.12456,47.9748,4.97266,52.1283),
 (520,-180,-90,180,90),
 (521,3.31325,47.9748,3.31325,47.9748),
 (708,-180,-90,180,90),
 (709,-180,-90,180,90),
 (710,-180,-90,180,90),
 (711,-180,-90,180,90),
 (712,-180,-90,180,90),
 (713,-180,-90,180,90),
 (714,-180,-90,180,90),
 (715,-180,-90,180,90);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (716,-180,-90,180,90),
 (717,-180,-90,180,90),
 (718,-180,-90,180,90),
 (719,-180,-90,180,90),
 (720,-180,-90,180,90),
 (721,-180,-90,180,90),
 (722,-180,-90,180,90),
 (723,-180,-90,180,90),
 (724,-180,-90,180,90),
 (725,-180,-90,180,90),
 (726,-180,-90,180,90),
 (727,-180,-90,180,90),
 (728,-180,-90,180,90),
 (731,-180,-90,180,90),
 (732,-180,-90,180,90),
 (733,-180,-90,180,90),
 (734,0,-45,180,45),
 (735,-180,-90,180,90),
 (737,-180,-90,180,90),
 (738,-180,-90,180,90),
 (740,-180,-90,180,90),
 (741,-180,-90,180,90),
 (742,-180,-90,180,90),
 (743,-180,-90,180,90),
 (744,-180,-90,180,90),
 (745,-180,-90,180,90),
 (746,-180,-90,180,90),
 (747,0,0,180,90),
 (749,0,0,90,90),
 (750,-90,-90,180,90),
 (751,0,0,90,90),
 (752,0,-45,90,90),
 (754,-180,0,0,90),
 (755,-180,-45,0,90),
 (756,-180,0,0,90),
 (757,-180,0,0,90),
 (758,-180,0,0,90),
 (760,-180,-90,180,90),
 (761,-180,-90,180,90),
 (763,-180,-90,180,90),
 (764,-180,-90,180,90),
 (765,-180,-90,180,90);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (766,-180,-90,180,90),
 (767,-180,-90,180,90),
 (768,-180,-90,180,90),
 (769,-90,-45,180,90),
 (771,-180,-90,180,90),
 (772,-180,-90,180,90),
 (773,-180,-90,180,90),
 (774,-180,-90,180,90),
 (775,-180,-45,180,90),
 (777,-90,-45,180,90),
 (778,-180,-90,180,90),
 (780,-180,-90,180,90),
 (781,0,-45,180,45),
 (782,-90,-45,180,90),
 (783,-180,-90,180,90),
 (784,-90,-45,180,90),
 (786,-180,-90,180,45),
 (787,-180,-90,180,45),
 (788,-180,-45,180,90),
 (789,-180,-90,180,90),
 (790,-180,-45,180,90),
 (791,-180,-90,180,45),
 (792,-180,-45,180,90),
 (793,-180,-45,180,90),
 (794,-180,-90,180,90),
 (795,-180,-90,180,90),
 (796,-180,-45,180,90),
 (797,-180,-90,180,45),
 (799,-180,-90,180,90),
 (800,-180,-90,180,90),
 (801,-180,-90,180,90),
 (803,-180,-90,180,90),
 (804,-180,-90,180,90),
 (805,-180,-90,180,90),
 (806,-180,-45,180,90),
 (807,-180,-90,180,90),
 (808,0,-45,180,90),
 (810,-90,-45,90,45),
 (812,-180,-90,180,90),
 (813,-180,-45,180,90);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (814,-180,-90,180,90),
 (815,-180,-90,180,90),
 (816,-180,-90,180,90),
 (817,-180,-45,180,90),
 (818,-180,-90,180,90),
 (819,-90,-45,180,90),
 (820,-180,-45,180,90),
 (821,-180,-45,180,90),
 (823,-180,-90,180,90),
 (824,-180,-90,180,90),
 (825,-180,-90,180,90),
 (826,-180,-90,180,90),
 (827,-180,-90,180,90),
 (828,-148.866836582311,-43.6799964727834,149.593505845405,63.6882248008624),
 (829,-179.878326407634,-54.9311103438959,179.339859033935,79.5294418511912),
 (830,-179.999420166016,-54.8880233764648,179.9999,74.740592956543),
 (831,-149.702819824219,-50.3082847595215,179.451965332031,69.6709136962891),
 (832,-179.999420166016,-47.162166595459,179.9999,77.2239837646484),
 (833,-124.787292480469,-30.7493667602539,141.380554199219,64.9872741699219),
 (834,-124.029273991473,-31.4281540224329,154.642608663999,63.0295295873657),
 (835,-167.325347908773,-44.7975158924237,177.578002936207,76.9732436863706),
 (836,-165.244674682617,-53.138427734375,179.609893798828,78.16796875),
 (837,-180,-90,180,90);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (838,-179.909469620325,-82.9724044678733,179.911941527389,83.618995626457),
 (839,-179.991973866709,-77.9279632447287,179.856246965937,83.6733627459034),
 (840,-179.9999,-78.1118240356445,179.999969482422,89.9186553955078),
 (841,-179.9999,-89.9999,179.9999,89.997917175293),
 (842,-179.9999,-89.9999,179.9999,83.6274185180664),
 (843,9.54319477081299,32.2058334350586,119.997001647949,52.90283203125),
 (844,-179.9999,-85.582763671875,179.999969482422,89.9999),
 (845,-179.999420166016,-85.582763671875,179.9999,83.6274185180664),
 (846,-180,-90,180,90),
 (847,-179.999420166016,-88.9542388916016,179.999420166016,83.5941696166992),
 (848,-179.998489371501,-89.8383789276704,179.992340072058,83.5204086033627),
 (849,-180,-90,180,90),
 (850,-122.004005432129,-45.6619415283203,177.989562988281,58.5494728088379),
 (851,-179.991973866709,-73.9846038585529,179.856246965937,83.6733627459034),
 (852,-179.999420166016,-70.9172515869141,179.9999,83.5759506225586),
 (853,-179.942245460115,-70.7184143224731,179.72499080468,82.7138214139268);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (854,-178.965972927399,-54.9324951181188,178.452606168576,71.4137801947072),
 (855,-179.999420166016,-55,179.9999,83.623046875),
 (856,-7.94513893127441,-37.1427764892578,144.668533325195,56.8571662902832),
 (857,-180,-90,180,90),
 (858,-180,-90,180,85.733333333335),
 (859,-180,-90,180,90),
 (860,-180,-90,180,90),
 (861,-180,-90,180,90),
 (862,-168.095977783203,-27.6065120697021,-14.0277805328369,75.0072631835938),
 (863,-123.152442932129,18.9949569702148,-61.9560737609863,58.6110038757324),
 (864,-139.997573852539,25.281270980835,-52.6617202758789,66.7187652587891),
 (865,-140.496292114258,53.4538917541504,-58.5955696105957,67.8282012939453),
 (866,-124.205703735352,24.5680294036865,-70.9565353393555,54.9354667663574),
 (867,-180,-90,180,90),
 (868,-179.948303222656,-51.9939651489258,179.9999,71.1174163818359),
 (869,-159.769439697266,-53.7913970947266,178.448303222656,69.7912216186523),
 (870,-148.744964632206,-32.5369033729658,155.010223430581,65.5288238497451),
 (871,-150.71989440918,-53.7913970947266,177.974609375,70.6666641235352);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (872,-158.631637603976,-45.7761955400929,176.14938356448,75.544586176984),
 (873,-180,-90,180,90),
 (874,-159.769439697266,-53.7913970947266,178.448303222656,69.7912216186523),
 (875,-180,-90,180,90),
 (876,20.6402492523193,-7.79213905334473,162.09294128418,59.9833602905274),
 (877,-164.431106583215,-70.2329864585772,170.260772709735,69.6751403762028),
 (878,-167.528930674307,-53.181308764033,175.347671541385,75.4231567075476),
 (879,-131.269302368164,-48.0415916442871,158.857467651367,63.6904182434082),
 (880,-169.677917459048,-46.5901946788654,174.464172343723,71.0141906468198),
 (881,-131.303222668357,-41.0020981030539,169.563095108606,64.8489685216919),
 (882,-180,-90,180,90),
 (883,-179.129608154297,-53.167423248291,178.443252563477,70.9927215576172),
 (884,-58.4966392675415,-34.8679313482717,153.187316902913,59.8631133651361),
 (885,-179.98931881506,-86.4852066105232,179.996978784911,81.8230590457097),
 (886,-84.8929824633524,-38.9450721954927,133.667388916947,64.1353378118947);
INSERT INTO `latlonboundingbox` (`LAYERID`,`MINX`,`MINY`,`MAXX`,`MAXY`) VALUES 
 (887,25.203592300415,-8.67711067199707,120.379692077637,23.4393615722656),
 (888,-180,-90,180,90),
 (889,-179.582321166992,-85.7026901245117,179.849975585938,82.6487579345703),
 (890,-179.923385591246,-87.4566039768979,179.627944962122,88.5763778770342),
 (891,-179.442779541016,-87.4625091552734,179.831909179688,83.5644226074219),
 (892,-179.999420166016,-89.9994277954102,179.9999,89.9999),
 (893,79.9577484130859,-37.2615852355957,150.848724365234,42.4208068847656),
 (894,-180,-90,180,90),
 (895,-180,-90,180,90),
 (896,-141.747323,42.75222705,-52.95665124,83.14517952),
 (897,-143.4193834,32.80806774,-10.30835952,83.7749511),
 (898,-180,-90,180,90),
 (899,-179.9989999,-84.30888267,179.9993808,83.62360026),
 (900,-179.9989999,-84.30888267,179.9993808,83.62360026),
 (901,-179.9989999,-84.79229076,179.9993808,83.62360026),
 (902,-165.8402708,-84.84944787,179.2562476,82.81369782),
 (903,-179.9989999,-84.30888267,179.9993808,83.62360026);
/*!40000 ALTER TABLE `latlonboundingbox` ENABLE KEYS */;


--
-- Definition of table `layer`
--

DROP TABLE IF EXISTS `layer`;
CREATE TABLE `layer` (
  `LAYERID` int(10) unsigned NOT NULL auto_increment,
  `SERVICEPROVIDERID` int(10) unsigned default '0',
  `NAME` varchar(50) default NULL,
  `TITLE` varchar(50) NOT NULL default '0',
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
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (497,30,'ijmond-map','B3Partners mapserver',NULL,'0',NULL,'0','0',NULL,NULL,NULL,NULL,NULL),
 (498,30,'labels_ijmond','gemeentes_ijmond',NULL,'0','0','0','0',NULL,NULL,'24.9456','0',497),
 (499,30,'beverwijk','beverwijk','beverwijk','0',NULL,'0','0',NULL,NULL,NULL,NULL,497),
 (500,30,'beverwijk_wegen','beverwijk_wegen',NULL,'0','0','0','0',NULL,NULL,'3.11864','24.9451',499),
 (501,30,'beverwijk_all','beverwijk_all',NULL,'0','0','0','0',NULL,NULL,'0','3.11814',499),
 (502,30,'heemskerk','heemskerk','heemskerk','0',NULL,'0','0',NULL,NULL,NULL,NULL,497),
 (503,30,'heemskerktxt','heemskerktxt',NULL,'0','0','0','0',NULL,NULL,'0','0.299342',502),
 (504,30,'heemskerk_wegen','heemskerk_wegen',NULL,'0','0','0','0',NULL,NULL,'3.11864','24.9451',502),
 (505,30,'heemskerk_all','heemskerk_all',NULL,'0','0','0','0',NULL,NULL,'0','3.11814',502),
 (506,30,'velsen','velsen','velsen','0',NULL,'0','0',NULL,NULL,NULL,NULL,497);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (507,30,'velsen_all','velsen_all',NULL,'0','0','0','0',NULL,NULL,'0','3.11814',506),
 (508,30,'velsen_wegen','velsen_wegen',NULL,'0','0','0','0',NULL,NULL,'3.11864','24.9451',506),
 (509,30,'gemeentes_ijmond','gemeentes_ijmond',NULL,'0','0','0','0',NULL,NULL,NULL,NULL,497),
 (510,30,'demo_autowegen','demo_autowegen',NULL,'0','0','0','0',NULL,NULL,'24.9456','0',497),
 (511,30,'demo_basis','demo_basis',NULL,'0','0','0','0',NULL,NULL,NULL,NULL,497),
 (512,30,'uitgeest','uitgeest','uitgeest','0',NULL,'0','0',NULL,NULL,NULL,NULL,497),
 (513,30,'uitgeest_wegen','uitgeest_wegen',NULL,'0','0','0','0',NULL,NULL,'3.11864','24.9451',512),
 (514,30,'uitgeesttxt','uitgeesttxt',NULL,'0','0','0','0',NULL,NULL,'0','0.299342',512),
 (515,30,'uitgeest_all','uitgeest_all',NULL,'0','0','0','0',NULL,NULL,'0','3.11814',512),
 (516,30,'gemeentes','gemeentes',NULL,'0','0','0','0',NULL,NULL,'0.997806','0',497);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (517,31,'gbkn-map','B3Partners mapserver',NULL,'0',NULL,'0','0',NULL,NULL,NULL,NULL,NULL),
 (518,31,'gbkn-all','gbkn-all',NULL,'0','0','0','0',NULL,NULL,NULL,NULL,517),
 (519,31,'gbkn-annotaties','gbkn-annotaties',NULL,'0','0','0','0',NULL,NULL,NULL,NULL,517),
 (520,31,'gemeentes','gemeentes',NULL,'0','0','0','0',NULL,NULL,'0.997806','0',517),
 (521,32,'gstreet-demo','B3Partners mapserver',NULL,'0',NULL,'0','0',NULL,NULL,NULL,NULL,NULL),
 (522,32,'gstreet1','gstreet1',NULL,'0','0','0','0',NULL,NULL,'0','12.4721',521),
 (523,32,'gstreet4','gstreet4',NULL,'0','0','0','0',NULL,NULL,'99.7806','199.561',521),
 (524,32,'gstreet3','gstreet3',NULL,'0','0','0','0',NULL,NULL,'24.9451','99.7801',521),
 (525,32,'gstreet6','gstreet6',NULL,'0','0','0','0',NULL,NULL,'299.342','0',521),
 (526,32,'gstreet2','gstreet2',NULL,'0','0','0','0',NULL,NULL,'12.4726','24.9446',521),
 (527,32,'gstreet5','gstreet5',NULL,'0','0','0','0',NULL,NULL,'199.561','299.341',521);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (705,36,'','ESA CubeSERV layers','ESA CubeSERV layers','0','0','0','0',NULL,NULL,NULL,NULL,NULL),
 (706,36,'','TEST2 (X)','\n','0','1','0','0',NULL,NULL,NULL,NULL,705),
 (707,36,'','TEST (X)','\n','0','1','0','0',NULL,NULL,NULL,NULL,705),
 (708,36,'','DEMIS','remote layers served from World Map','0','1','0','0',NULL,NULL,NULL,NULL,705),
 (709,36,'Borders:DEMIS','Borders',NULL,'1','1','0','0',NULL,NULL,NULL,NULL,708),
 (710,36,'Ocean features:DEMIS','Ocean features',NULL,'1','1','0','0',NULL,NULL,'0','1084612608',708),
 (711,36,'Streams:DEMIS','Streams',NULL,'1','1','0','0',NULL,NULL,'0','1085661184',708),
 (712,36,'Highways:DEMIS','Highways',NULL,'1','1','0','0',NULL,NULL,'0','1082515456',708),
 (713,36,'Topography:DEMIS','Topography',NULL,'1','1','0','0',NULL,NULL,NULL,NULL,708),
 (714,36,'Builtup areas:DEMIS','Builtup areas',NULL,'1','1','0','0',NULL,NULL,'0','1083564032',708),
 (715,36,'Cities:DEMIS','Cities',NULL,'1','1','0','0',NULL,NULL,'0','1088116736',708);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (716,36,'Trails:DEMIS','Trails',NULL,'1','1','0','0',NULL,NULL,'0','1081180160',708),
 (717,36,'Waterbodies:DEMIS','Waterbodies',NULL,'1','1','0','0',NULL,NULL,'0','1085661184',708),
 (718,36,'Countries:DEMIS','Countries',NULL,'1','1','0','0',NULL,NULL,NULL,NULL,708),
 (719,36,'Rivers:DEMIS','Rivers',NULL,'1','1','0','0',NULL,NULL,'0','1086709760',708),
 (720,36,'Inundated:DEMIS','Inundated',NULL,'1','1','0','0',NULL,NULL,'0','1083564032',708),
 (721,36,'Airports:DEMIS','Airports',NULL,'1','1','0','0',NULL,NULL,'0','1082228736',708),
 (722,36,'Coastlines:DEMIS','Coastlines',NULL,'1','1','0','0',NULL,NULL,NULL,NULL,708),
 (723,36,'Roads:DEMIS','Roads',NULL,'1','1','0','0',NULL,NULL,'0','1081753600',708),
 (724,36,'Bathymetry:DEMIS','Bathymetry',NULL,'1','1','0','0',NULL,NULL,NULL,NULL,708),
 (725,36,'Spot elevations:DEMIS','Spot elevations',NULL,'0','1','0','0',NULL,NULL,'0','1081180160',708);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (726,36,'Hillshading:DEMIS','Hillshading',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,708),
 (727,36,'Railroads:DEMIS','Railroads',NULL,'1','1','0','0',NULL,NULL,'0','1082802176',708),
 (728,36,'Settlements:DEMIS','Settlements',NULL,'1','1','0','0',NULL,NULL,'0','1082228736',708),
 (729,36,'','MapAdmin','local MapAdmin layers','0','0','0','0',NULL,NULL,NULL,NULL,705),
 (730,36,'','PHYSIOGRAPHY','Physiography','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (731,36,'GROUNDA_1M:MapAdmin','Ground Surface Areas','Physiography / GROUNDA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,730),
 (732,36,'SEAICEA_1M:MapAdmin','Sea Ice Areas','Physiography / SEAICEA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,730),
 (733,36,'PHYSTXT_1M:MapAdmin','Physiography Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,730),
 (734,36,'LNDFRML_1M:MapAdmin','Landform Line Features','Physiography / LNDFRML_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,730);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (735,36,'LANDICEA_1M:MapAdmin','Snow/Ice Fields and Glaciers','\n','1','0','0','0',NULL,NULL,NULL,NULL,730),
 (736,36,'','ELEVATION','Elevation','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (737,36,'CONTOURL_1M:MapAdmin','Elevation Contours','\n','1','0','0','0',NULL,NULL,NULL,NULL,736),
 (738,36,'ELEVP_1M:MapAdmin','Spot Elevations','\n','1','0','0','0',NULL,NULL,NULL,NULL,736),
 (739,36,'','BOUNDARIES','Boundaries','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (740,36,'OCEANSEA_1M:MapAdmin','Oceans/Seas','\n','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (741,36,'DEPTHL_1M:MapAdmin','Depth Contours','\n','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (742,36,'POLBNDL_1M:MapAdmin','Political Boundaries','Boundaries / POLBNDL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (743,36,'BNDTXT_1M:MapAdmin','Boundaries Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (744,36,'POLBNDA_1M:MapAdmin','Administrative Areas','\n','1','0','0','0',NULL,NULL,NULL,NULL,739);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (745,36,'POLBNDP_1M:MapAdmin','Political Boundary Point Features','\n','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (746,36,'COASTL_1M:MapAdmin','Coastlines','\n','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (747,36,'BARRIERL_1M:MapAdmin','Barrier Line Features','Boundaries / BARRIERL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,739),
 (748,36,'','ENVISAT','ASAR Wide Swath Mode','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (749,36,'ASAR_WSM:MapAdmin','ASAR_WSM','ASAR Wide Swath Mode / ASAR_WSM:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,748),
 (750,36,'MERIS_RR:MapAdmin','MERIS_RR','ASAR Wide Swath Mode / MERIS_RR:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,748),
 (751,36,'ASAR:MapAdmin','ASAR','ASAR Wide Swath Mode / ASAR:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,748),
 (752,36,'AATSR:MapAdmin','AATSR','ASAR Wide Swath Mode / AATSR:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,748),
 (753,36,'','VEGETATION','Vegetation','0','0','0','0',NULL,NULL,NULL,NULL,729);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (754,36,'GRASSA_1M:MapAdmin','Grasslands','Vegetation / GRASSA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,753),
 (755,36,'SWAMPA_1M:MapAdmin','Marshs/Swamps','Vegetation / SWAMPA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,753),
 (756,36,'TUNDRAA_1M:MapAdmin','Tundra','Vegetation / TUNDRAA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,753),
 (757,36,'TREESA_1M:MapAdmin','Trees','\n','1','0','0','0',NULL,NULL,NULL,NULL,753),
 (758,36,'CROPA_1M:MapAdmin','Croplands','Vegetation / CROPA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,753),
 (759,36,'','DIGITAL TERRAIN','Digital Terrain','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (760,36,'ETOPO5:MapAdmin','Global 5 Minute Elevations','ETOPO5 was generated from a digital data base of land and sea- floor elevations on a 5-minute latitude/longitude grid. The resolution of the gridded data varies from true 5-minute for the ocean floors, the USA., Europe, Japan,and Australia to 1 degree in data-deficient parts of Asia, South America, northern Canada, and Africa. Data sources are as follows: Ocean Areas: US Naval Oceanographic Ofice; USA., W.  Europe, Japan/Korea: US Defense Mapping Agency; Australia: Bureau of Mineral Resources, Australia; New Zealand: Department of Industrial and Scientific Research, New Zealand; balance of world land masses: US Navy Fleet Numerical Oceanographic Center. These various data bases were originally assembled in 1988 into the worldwide 5-minute grid by Margo Edwards, then at Washington University, St. Louis, MO.','1','0','0','0',NULL,NULL,NULL,NULL,759),
 (761,36,'GTOPO30:MapAdmin','Global 30 Second Elevations','GTOPO30 is a global digital elevation model (DEM) resulting from a collaborative effort led by the staff at the U.S. Geological Survey\'s EROS Data Center in Sioux Falls, South Dakota.  Elevations in GTOPO30 are regularly spaced at 30-arc seconds (approximately 1 kilometer).  GTOPO30 was developed to meet the needs of the geospatial data user community for regional and continental scale topographic data.','1','0','0','0',NULL,NULL,NULL,NULL,759);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (762,36,'','HYDROGRAPHY','Hydrography','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (763,36,'WATRCRSL_1M:MapAdmin','Water Courses','Hydrography / WATRCRSL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (764,36,'DANGERP_1M:MapAdmin','Danger Point Features','Hydrography / DANGERP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (765,36,'AQUECANL_1M:MapAdmin','Aqueducts/Canals/Flumes/Penstocks','Hydrography / AQUECANL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (766,36,'INWATERA_1M:MapAdmin','Inland Water Areas','\n','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (767,36,'MISCP_1M:MapAdmin','Miscellaneous Point Features','\n','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (768,36,'HYDROTXT_1M:MapAdmin','Hydrography Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,762),
 (769,36,'MISCL_1M:MapAdmin','Miscellaneous Line Features','Hydrography / MISCL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,762);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (770,36,'','UTILITIES','Utilities','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (771,36,'UTILP_1M:MapAdmin','Utility Point Features','Utilities / UTILP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,770),
 (772,36,'UTILL_1M:MapAdmin','Power Transmission/Telephone/Telegraph Lines','Utilities / UTILL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,770),
 (773,36,'PIPEL_1M:MapAdmin','Pipelines','Utilities / PIPEL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,770),
 (774,36,'DQLINE_UTIL_1M:MapAdmin','Data Quality Line Features','Utilities / DQLINE_UTIL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,770),
 (775,36,'UTILTXT_1M:MapAdmin','Utilities Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,770),
 (776,36,'','DATA QUALITY','Data Quality','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (777,36,'DQLINE_TRANS_1M:MapAdmin','Data Quality Line Features','Data Quality / DQLINE_TRANS_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,776);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (778,36,'DQLINE_UTIL_1M:MapAdmin','Data Quality Line Features','Data Quality / DQLINE_UTIL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,776),
 (779,36,'','POPULATION','Population','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (780,36,'MISPOPP_1M:MapAdmin','Miscellaneous Population Points','\n','1','0','0','0',NULL,NULL,NULL,NULL,779),
 (781,36,'MISPOPA_1M:MapAdmin','Miscellaneous Population Areas','Population / MISPOPA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,779),
 (782,36,'POPTXT_1M:MapAdmin','Population Coverage Text','\n\n','1','0','0','0',NULL,NULL,NULL,NULL,779),
 (783,36,'BUILTUPA_1M:MapAdmin','Built-Up Areas','\n','1','0','0','0',NULL,NULL,NULL,NULL,779),
 (784,36,'BUILTUPP_1M:MapAdmin','Built-Up Area Points','\n','1','0','0','0',NULL,NULL,NULL,NULL,779),
 (785,36,'','BURNSCARS','Global  Burn Scars data','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (786,36,'GLOBSCAR022002:MapAdmin','GLOBSCAR022002','Global  Burn Scars data / GLOBSCAR022002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (787,36,'GLOBSCAR112002:MapAdmin','GLOBSCAR112002','Global  Burn Scars data / GLOBSCAR112002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (788,36,'GLOBSCAR062002:MapAdmin','GLOBSCAR062002','Global  Burn Scars data / GLOBSCAR062002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (789,36,'GLOBSCAR102002:MapAdmin','GLOBSCAR102002','Global  Burn Scars data / GLOBSCAR102002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (790,36,'GLOBSCAR082002:MapAdmin','GLOBSCAR082002','Global  Burn Scars data / GLOBSCAR082002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (791,36,'GLOBSCAR122002:MapAdmin','GLOBSCAR122002','Global  Burn Scars data / GLOBSCAR122002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (792,36,'GLOBSCAR072002:MapAdmin','GLOBSCAR072002','Global  Burn Scars data / GLOBSCAR072002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (793,36,'GLOBSCAR052002:MapAdmin','GLOBSCAR052002','Global  Burn Scars data / GLOBSCAR052002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (794,36,'GLOBSCAR042002:MapAdmin','GLOBSCAR042002','Global  Burn Scars data / GLOBSCAR042002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (795,36,'GLOBSCAR032002:MapAdmin','GLOBSCAR032002','Global  Burn Scars data / GLOBSCAR032002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (796,36,'GLOBSCAR092002:MapAdmin','GLOBSCAR092002','Global  Burn Scars data / GLOBSCAR092002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (797,36,'GLOBSCAR012002:MapAdmin','GLOBSCAR012002','Global  Burn Scars data / GLOBSCAR012002:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,785),
 (798,36,'','EO_MAPS','Global maps derived from various Earth Observation sensors','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (799,36,'WORLD_DMSP_NIGHT:MapAdmin','WORLD_DMSP_NIGHT','Global maps derived from various Earth Observation sensors / WORLD_DMSP_NIGHT:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,798),
 (800,36,'WORLD_MODIS_5KM:MapAdmin','WORLD_MODIS_5KM','ous Earth Observation sensors / WORLD_MODIS_5KM:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,798);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (801,36,'WORLD_MODIS_1KM:MapAdmin','WORLD_MODIS_1KM','Global maps derived from various Earth Observation sensors / WORLD_MODIS_1KM:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,798),
 (802,36,'','INDUSTRY','Industry','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (803,36,'STORAGEP_1M:MapAdmin','Storage Point Features','Industry / STORAGEP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,802),
 (804,36,'EXTRACTA_1M:MapAdmin','Extraction Areas','Industry / EXTRACTA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,802),
 (805,36,'EXTRACTP_1M:MapAdmin','Extraction Point Features','Industry / EXTRACTP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,802),
 (806,36,'INDTXT_1M:MapAdmin','Industry Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,802),
 (807,36,'MISINDP_1M:MapAdmin','Miscellaneous Industry Point Features','Industry / MISINDP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,802),
 (808,36,'FISHINDA_1M:MapAdmin','Fish Hatcheries/Fish Farms','Industry / FISHINDA_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,802);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (809,36,'','GISD','Geographic Information for Sustainable Development','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (810,36,'GISD_ROI:MapAdmin','GISD_ROI','Geographic Information for Sustainable Development / GISD_ROI:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,809),
 (811,36,'','TRANSPORTATION','Transportation','0','0','0','0',NULL,NULL,NULL,NULL,729),
 (812,36,'ROADL_1M:MapAdmin','Roads','\n','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (813,36,'RRYARDP_1M:MapAdmin','Railroad Yard Points','Transportation / RRYARDP_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (814,36,'TRAILL_1M:MapAdmin','Trails and Tracks','Transportation / TRAILL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (815,36,'TRANSTRL_1M:MapAdmin','Transportation Structures Lines','Transportation / TRANSTRL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (816,36,'RAILRDL_1M:MapAdmin','Railroads','Transportation / RAILRDL_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (817,36,'MISTRANL_1M:MapAdmin','Miscellaneous Transportation Line Features','\n','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (818,36,'AEROFACP_1M:MapAdmin','Airport Facilities Points','\n','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (819,36,'DQLINE_TRANS_1M:MapAdmin','Data Quality Line Features','Transportation / DQLINE_TRANS_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (820,36,'TRANSTRC_1M:MapAdmin','Transportation Structures Points','Transportation / TRANSTRC_1M:MapAdmin','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (821,36,'TRANSTXT_1M:MapAdmin','Transportation Coverage Text','\n','1','0','0','0',NULL,NULL,NULL,NULL,811),
 (822,36,'','map annotations','map annotations','0','0','0','0',NULL,NULL,NULL,NULL,705),
 (823,36,'graticule','Lat/Lon Graticule','Produces graticule lines every 10 degrees of latitude and longitude.  If the STYLE for this layer is set to a number, the graticule lines will be spaced that many degrees apart.  A STYLE of the form \"number:number\" is also accepted, specifying the longitudinal and latitudinal spacing separately.','0','0','0','0',NULL,NULL,NULL,NULL,822),
 (824,36,'crosshairs','Crosshairs','Displays a set of crosshairs to indicate the center of the image','0','0','0','0',NULL,NULL,NULL,NULL,822);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (825,36,'','CubeWerx','CubeSERV layers','0','1','0','0',NULL,NULL,NULL,NULL,705),
 (826,36,'','Foundation','local Foundation layers','0','1','0','0',NULL,NULL,NULL,NULL,825),
 (827,36,'','Transportation',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (828,36,'RRYARDP_1M:Foundation:CubeWerx','Railroad Yard Points','Railroad Yard Points','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (829,36,'AEROFACP_1M:Foundation:CubeWerx','Airport Facilities Points','\nwere collected.','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (830,36,'ROADL_1M:Foundation:CubeWerx','Roads','\nsources.','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (831,36,'TRANSTRL_1M:Foundation:CubeWerx','Transportation Structures Lines','Transportation Structures Lines','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (832,36,'TRAILL_1M:Foundation:CubeWerx','Trails and Tracks','Trails and Tracks','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (833,36,'MISTRANL_1M:Foundation:CubeWerx','Miscellaneous Transportation Line Features','\ninformation.','1','1','0','0',NULL,NULL,NULL,NULL,827);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (834,36,'TRANSTRC_1M:Foundation:CubeWerx','Transportation Structures Points','Transportation Structures Points','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (835,36,'TRANSTXT_1M:Foundation:CubeWerx','Transportation Coverage Text','\ntransportation coverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (836,36,'RAILRDL_1M:Foundation:CubeWerx','Railroads','Railroads','1','1','0','0',NULL,NULL,NULL,NULL,827),
 (837,36,'','Boundaries',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (838,36,'BNDTXT_1M:Foundation:CubeWerx','Boundaries Coverage Text','\nboundaries coverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (839,36,'POLBNDP_1M:Foundation:CubeWerx','Political Boundary Point Features','>','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (840,36,'DEPTHL_1M:Foundation:CubeWerx','Depth Contours','\n600, 1000. 2000, 4000, 6000, and 8000 meters.','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (841,36,'POLBNDL_1M:Foundation:CubeWerx','Political Boundaries','Political Boundaries','1','1','0','0',NULL,NULL,NULL,NULL,837);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (842,36,'POLBNDA_1M:Foundation:CubeWerx','Administrative Areas','>','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (843,36,'BARRIERL_1M:Foundation:CubeWerx','Barrier Line Features','Barrier Line Features','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (844,36,'OCEANSEA_1M:Foundation:CubeWerx','Oceans/Seas',' change.','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (845,36,'COASTL_1M:Foundation:CubeWerx','Coastlines','\ninland islands.','1','1','0','0',NULL,NULL,NULL,NULL,837),
 (846,36,'','Elevation',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (847,36,'CONTOURL_1M:Foundation:CubeWerx','Elevation Contours','\nmeters.','1','1','0','0',NULL,NULL,NULL,NULL,846),
 (848,36,'ELEVP_1M:Foundation:CubeWerx','Spot Elevations','\n1, July 1992) to meters.','1','1','0','0',NULL,NULL,NULL,NULL,846),
 (849,36,'','Hydrography',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (850,36,'AQUECANL_1M:Foundation:CubeWerx','Aqueducts/Canals/Flumes/Penstocks','Aqueducts/Canals/Flumes/Penstocks','1','1','0','0',NULL,NULL,NULL,NULL,849);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (851,36,'MISCP_1M:Foundation:CubeWerx','Miscellaneous Point Features','\npolbnda.aft when the name appeared on the ONC litho.','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (852,36,'INWATERA_1M:Foundation:CubeWerx','Inland Water Areas','\nareas (BH000) were taken from Digital Chart of the World, edition 1, July 1992.','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (853,36,'HYDROTXT_1M:Foundation:CubeWerx','Hydrography Coverage Text','\ncoverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (854,36,'DANGERP_1M:Foundation:CubeWerx','Danger Point Features','Danger Point Features','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (855,36,'WATRCRSL_1M:Foundation:CubeWerx','Water Courses','Water Courses','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (856,36,'MISCL_1M:Foundation:CubeWerx','Miscellaneous Line Features','Miscellaneous Line Features','1','1','0','0',NULL,NULL,NULL,NULL,849),
 (857,36,'','Digital Terrain',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (858,36,'GTOPO30:Foundation:CubeWerx','Global 30 Second Elevations','GTOPO30 is a global digital elevation model (DEM) resulting from a collaborative effort led by the staff at the U.S. Geological Survey\'s EROS Data Center in Sioux Falls, South Dakota.  Elevations in GTOPO30 are regularly spaced at 30-arc seconds (approximately 1 kilometer).  GTOPO30 was developed to meet the needs of the geospatial data user community for regional and continental scale topographic data.','1','1','0','0',NULL,NULL,NULL,NULL,857),
 (859,36,'ETOPO2:Foundation:CubeWerx','Global 2 Minute Elevations','ETOPO2 was generated from a digital data base of land and sea-floor elevations on a 2-minute latitude/longitude grid. The data sources used to create the ETOPO2 data set were: Smith/Sandwell, GLOBE, DBDBV, IBCAO, and DBDB5','1','1','0','0',NULL,NULL,NULL,NULL,857),
 (860,36,'RELIEF:Foundation:CubeWerx','Two Minute Shaded Relief','This image was generated from digital data bases of seafloor and land elevations on a 2-minute latitude/longitude grid (1 minute of latitude = 1 nautical mile, or 1.852 km). Assumed illumination is from the west; shading is computed as a function of the east-west slope of the surface with a nonlinear exaggeration favoring low-relief areas. A Cylindrical Equidistant projection was used for the world image, which spans 360 degrees of of longitude from 180 West eastward to 180 East; latitude coverage is from 90 degrees North to 90 degrees South. The resolution of the gridded data varies from true 2-minute for the Atlantic, Pacific, and Indian Ocean floors and all land masses to 5 minutes for the Arctic Ocean floor. Clicking on a square above brings up a 512 x 512 pixel color relief image of the 45 degree area selected, clicking on the 512 x 512 image brings up the full-resolution 1350 x 1350 pixel (roughly 3 mb) color image of the area.','1','1','0','0',NULL,NULL,NULL,NULL,857);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (861,36,'','Vegetation',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (862,36,'SWAMPA_1M:Foundation:CubeWerx','Marshs/Swamps','Marshs/Swamps','1','1','0','0',NULL,NULL,NULL,NULL,861),
 (863,36,'CROPA_1M:Foundation:CubeWerx','Croplands','Croplands','1','1','0','0',NULL,NULL,NULL,NULL,861),
 (864,36,'TREESA_1M:Foundation:CubeWerx','Trees','\n\"transitional\" forests.','1','1','0','0',NULL,NULL,NULL,NULL,861),
 (865,36,'TUNDRAA_1M:Foundation:CubeWerx','Tundra','Tundra','1','1','0','0',NULL,NULL,NULL,NULL,861),
 (866,36,'GRASSA_1M:Foundation:CubeWerx','Grasslands','Grasslands','1','1','0','0',NULL,NULL,NULL,NULL,861),
 (867,36,'','Utilities',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (868,36,'UTILL_1M:Foundation:CubeWerx','Power Transmission/Telephone/Telegraph Lines','Power Transmission/Telephone/Telegraph Lines','1','1','0','0',NULL,NULL,NULL,NULL,867),
 (869,36,'DQLINE_UTIL_1M:Foundation:CubeWerx','Data Quality Line Features','Data Quality Line Features','1','1','0','0',NULL,NULL,NULL,NULL,867);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (870,36,'UTILTXT_1M:Foundation:CubeWerx','Utilities Coverage Text','\ncoverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,867),
 (871,36,'PIPEL_1M:Foundation:CubeWerx','Pipelines','Pipelines','1','1','0','0',NULL,NULL,NULL,NULL,867),
 (872,36,'UTILP_1M:Foundation:CubeWerx','Utility Point Features','Utility Point Features','1','1','0','0',NULL,NULL,NULL,NULL,867),
 (873,36,'','Data Quality',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (874,36,'DQLINE_UTIL_1M:Foundation:CubeWerx','Data Quality Line Features','Data Quality Line Features','1','1','0','0',NULL,NULL,NULL,NULL,873),
 (875,36,'','Industry',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (876,36,'FISHINDA_1M:Foundation:CubeWerx','Fish Hatcheries/Fish Farms','Fish Hatcheries/Fish Farms','1','1','0','0',NULL,NULL,NULL,NULL,875),
 (877,36,'STORAGEP_1M:Foundation:CubeWerx','Storage Point Features','Storage Point Features','1','1','0','0',NULL,NULL,NULL,NULL,875);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (878,36,'EXTRACTP_1M:Foundation:CubeWerx','Extraction Point Features','Extraction Point Features','1','1','0','0',NULL,NULL,NULL,NULL,875),
 (879,36,'EXTRACTA_1M:Foundation:CubeWerx','Extraction Areas','Extraction Areas','1','1','0','0',NULL,NULL,NULL,NULL,875),
 (880,36,'MISINDP_1M:Foundation:CubeWerx','Miscellaneous Industry Point Features','Miscellaneous Industry Point Features','1','1','0','0',NULL,NULL,NULL,NULL,875),
 (881,36,'INDTXT_1M:Foundation:CubeWerx','Industry Coverage Text','\ncoverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,875),
 (882,36,'','Population',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (883,36,'BUILTUPA_1M:Foundation:CubeWerx','Built-Up Areas','of the World, edition 1, July 1992.','1','1','0','0',NULL,NULL,NULL,NULL,882),
 (884,36,'POPTXT_1M:Foundation:CubeWerx','Population Coverage Text','\ncoverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,882),
 (885,36,'MISPOPP_1M:Foundation:CubeWerx','Miscellaneous Population Points','\nsettlement (AL135).','1','1','0','0',NULL,NULL,NULL,NULL,882);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (886,36,'BUILTUPP_1M:Foundation:CubeWerx','Built-Up Area Points','\nplace names in city tints\' in Digital Chart of the World, edition 1, July 1992.','1','1','0','0',NULL,NULL,NULL,NULL,882),
 (887,36,'MISPOPA_1M:Foundation:CubeWerx','Miscellaneous Population Areas','Miscellaneous Population Areas','1','1','0','0',NULL,NULL,NULL,NULL,882),
 (888,36,'','Physiography',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,826),
 (889,36,'GROUNDA_1M:Foundation:CubeWerx','Ground Surface Areas','Ground Surface Areas','1','1','0','0',NULL,NULL,NULL,NULL,888),
 (890,36,'PHYSTXT_1M:Foundation:CubeWerx','Physiography Coverage Text','\ncoverage feature.','1','1','0','0',NULL,NULL,NULL,NULL,888),
 (891,36,'LANDICEA_1M:Foundation:CubeWerx','Snow/Ice Fields and Glaciers','\nfeature code (BJ100).','1','1','0','0',NULL,NULL,NULL,NULL,888),
 (892,36,'SEAICEA_1M:Foundation:CubeWerx','Sea Ice Areas','Sea Ice Areas','1','1','0','0',NULL,NULL,NULL,NULL,888);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (893,36,'LNDFRML_1M:Foundation:CubeWerx','Landform Line Features','Landform Line Features','1','1','0','0',NULL,NULL,NULL,NULL,888),
 (894,36,'','GSC','local GSC layers','0','1','0','0',NULL,NULL,NULL,NULL,825),
 (895,36,'','Canadian Geology',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,894),
 (896,36,'FAULTS_CA:GSC:CubeWerx','Canadian Fault Lines','Canadian Fault Lines','1','1','0','0',NULL,NULL,NULL,NULL,895),
 (897,36,'AGE_ROCK_TYPE_CA:GSC:CubeWerx','Canadian Age Rock Type','Canadian Age Rock Type','1','1','0','0',NULL,NULL,NULL,NULL,895),
 (898,36,'','World Geology',NULL,'0','1','0','0',NULL,NULL,NULL,NULL,894),
 (899,36,'AGE_ROCK_TYPE:GSC:CubeWerx','World Age Rock Type','World Age Rock Type','1','1','0','0',NULL,NULL,NULL,NULL,898),
 (900,36,'PRECAMBRIAN:GSC:CubeWerx','Precambrian','Precambrian','1','1','0','0',NULL,NULL,NULL,NULL,898),
 (901,36,'FELSIC_MAGMATIC:GSC:CubeWerx','Felsic Magmatic','Felsic Magmatic','1','1','0','0',NULL,NULL,NULL,NULL,898);
INSERT INTO `layer` (`LAYERID`,`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`QUERYABLE`,`CASCADED`,`OPAQUE`,`NOSUBSETS`,`FIXEDWIDTH`,`FIXEDHEIGHT`,`SCALEHINTMIN`,`SCALEHINTMAX`,`PARENTID`) VALUES 
 (902,36,'FAULTS:GSC:CubeWerx','World Fault Lines','World Fault Lines','1','1','0','0',NULL,NULL,NULL,NULL,898),
 (903,36,'MAFIC_MAGMATIC:GSC:CubeWerx','Mafic Magmatic','Mafic Magmatic','1','1','0','0',NULL,NULL,NULL,NULL,898);
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
INSERT INTO `layerdomainformat` (`LDRID`,`FORMAT`) VALUES 
 (66,'image/html'),
 (67,'content/unknown'),
 (68,'content/unknown'),
 (69,'content/unknown'),
 (70,'content/unknown'),
 (71,'content/unknown'),
 (72,'content/unknown'),
 (73,'content/unknown'),
 (74,'content/unknown'),
 (75,'content/unknown'),
 (76,'content/unknown'),
 (77,'content/unknown'),
 (78,'content/unknown'),
 (79,'content/unknown'),
 (80,'content/unknown'),
 (81,'content/unknown'),
 (82,'content/unknown'),
 (83,'content/unknown'),
 (84,'content/unknown'),
 (85,'content/unknown'),
 (86,'content/unknown'),
 (87,'content/unknown'),
 (88,'content/unknown'),
 (89,'content/unknown'),
 (90,'content/unknown'),
 (91,'content/unknown'),
 (92,'content/unknown'),
 (93,'content/unknown'),
 (94,'content/unknown'),
 (95,'image/html'),
 (96,'image/html'),
 (97,'content/unknown'),
 (98,'content/unknown'),
 (99,'content/unknown'),
 (100,'content/unknown'),
 (101,'content/unknown'),
 (102,'content/unknown'),
 (103,'content/unknown'),
 (104,'content/unknown'),
 (105,'content/unknown');
INSERT INTO `layerdomainformat` (`LDRID`,`FORMAT`) VALUES 
 (106,'content/unknown'),
 (107,'content/unknown'),
 (108,'content/unknown'),
 (109,'content/unknown'),
 (110,'content/unknown'),
 (111,'content/unknown'),
 (112,'content/unknown'),
 (113,'content/unknown'),
 (114,'content/unknown'),
 (115,'content/unknown'),
 (116,'content/unknown'),
 (117,'content/unknown'),
 (118,'content/unknown'),
 (119,'content/unknown'),
 (120,'content/unknown'),
 (121,'content/unknown'),
 (122,'content/unknown'),
 (123,'content/unknown'),
 (124,'content/unknown'),
 (125,'content/unknown'),
 (126,'content/unknown'),
 (127,'content/unknown'),
 (128,'content/unknown'),
 (129,'content/unknown'),
 (130,'content/unknown');
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
INSERT INTO `layerdomainresource` (`LDRID`,`LAYERID`,`DOMAIN`,`URL`) VALUES 
 (66,760,'DataURL','http://www.ngdc.noaa.gov/mgg/global/etopo5.HTML'),
 (67,761,'DataURL','http://edcdaac.usgs.gov/gtopo30/README.html#h1'),
 (68,828,'DataURL','Railroad Yard Points'),
 (69,829,'DataURL','Airport Facilities Points'),
 (70,830,'DataURL','Roads'),
 (71,831,'DataURL','Transportation Structures Lines'),
 (72,832,'DataURL','Trails and Tracks'),
 (73,833,'DataURL','Miscellaneous Transportation Line Features'),
 (74,834,'DataURL','Transportation Structures Points'),
 (75,835,'DataURL','Transportation Coverage Text'),
 (76,836,'DataURL','Railroads'),
 (77,838,'DataURL','Boundaries Coverage Text'),
 (78,839,'DataURL','Political Boundary Point Features'),
 (79,840,'DataURL','Depth Contours'),
 (80,841,'DataURL','Political Boundaries'),
 (81,842,'DataURL','Administrative Areas'),
 (82,843,'DataURL','Barrier Line Features'),
 (83,844,'DataURL','Oceans/Seas'),
 (84,845,'DataURL','Coastlines'),
 (85,847,'DataURL','Elevation Contours'),
 (86,848,'DataURL','Spot Elevations');
INSERT INTO `layerdomainresource` (`LDRID`,`LAYERID`,`DOMAIN`,`URL`) VALUES 
 (87,850,'DataURL','Aqueducts/Canals/Flumes/Penstocks'),
 (88,851,'DataURL','Miscellaneous Point Features'),
 (89,852,'DataURL','Inland Water Areas'),
 (90,853,'DataURL','Hydrography Coverage Text'),
 (91,854,'DataURL','Danger Point Features'),
 (92,855,'DataURL','Water Courses'),
 (93,856,'DataURL','Miscellaneous Line Features'),
 (94,858,'DataURL','http://edcdaac.usgs.gov/gtopo30/README.html#h1'),
 (95,859,'DataURL','http://www.ngdc.noaa.gov/mgg/image/2minrelief.html'),
 (96,860,'DataURL','http://www.ngdc.noaa.gov/mgg/image/2minrelief.html'),
 (97,862,'DataURL','Marshs/Swamps'),
 (98,863,'DataURL','Croplands'),
 (99,864,'DataURL','Trees'),
 (100,865,'DataURL','Tundra'),
 (101,866,'DataURL','Grasslands'),
 (102,868,'DataURL','Power Transmission/Telephone/Telegraph Lines'),
 (103,869,'DataURL','Data Quality Line Features'),
 (104,870,'DataURL','Utilities Coverage Text'),
 (105,871,'DataURL','Pipelines'),
 (106,872,'DataURL','Utility Point Features'),
 (107,874,'DataURL','Data Quality Line Features');
INSERT INTO `layerdomainresource` (`LDRID`,`LAYERID`,`DOMAIN`,`URL`) VALUES 
 (108,876,'DataURL','Fish Hatcheries/Fish Farms'),
 (109,877,'DataURL','Storage Point Features'),
 (110,878,'DataURL','Extraction Point Features'),
 (111,879,'DataURL','Extraction Areas'),
 (112,880,'DataURL','Miscellaneous Industry Point Features'),
 (113,881,'DataURL','Industry Coverage Text'),
 (114,883,'DataURL','Built-Up Areas'),
 (115,884,'DataURL','Population Coverage Text'),
 (116,885,'DataURL','Miscellaneous Population Points'),
 (117,886,'DataURL','Built-Up Area Points'),
 (118,887,'DataURL','Miscellaneous Population Areas'),
 (119,889,'DataURL','Ground Surface Areas'),
 (120,890,'DataURL','Physiography Coverage Text'),
 (121,891,'DataURL','Snow/Ice Fields and Glaciers'),
 (122,892,'DataURL','Sea Ice Areas'),
 (123,893,'DataURL','Landform Line Features'),
 (124,896,'DataURL','Canadian Fault Lines'),
 (125,897,'DataURL','Canadian Age Rock Type'),
 (126,899,'DataURL','World Age Rock Type'),
 (127,900,'DataURL','Precambrian'),
 (128,901,'DataURL','Felsic Magmatic');
INSERT INTO `layerdomainresource` (`LDRID`,`LAYERID`,`DOMAIN`,`URL`) VALUES 
 (129,902,'DataURL','World Fault Lines'),
 (130,903,'DataURL','Mafic Magmatic');
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
  `TELEPHONE` int(10) unsigned NOT NULL default '0',
  `FAX` int(10) unsigned default NULL,
  PRIMARY KEY  (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `organization`
--

/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` (`ORGANIZATIONID`,`NAME`,`STREET`,`NUMBER`,`ADDITION`,`PROVINCE`,`COUNTRY`,`POSTBOX`,`BILLINGADDRESS`,`VISITORSADDRESS`,`TELEPHONE`,`FAX`) VALUES 
 (1,'B3Partners','Zonnebaan',12,'C','Utrecht','Nederland',NULL,NULL,NULL,302142081,NULL),
 (2,'Vactik','Zonnebaan',12,'A','Utrecht','Nederland',NULL,NULL,NULL,302433414,NULL),
 (6,'Test Organisatie','Teststraat',12,'C','Utrecht','Nederland','51','Teststraat 8','Teststraat 8',9008844,9008844),
 (7,'Firma digibeet','Digistraat',8,'A','Utrecht','Nederland','51','Teststraat 8','Teststraat 8',302142081,9008844);
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
INSERT INTO `organizationlayer` (`ORGANIZATIONID`,`LAYERID`) VALUES 
 (1,497),
 (6,497),
 (7,497),
 (1,498),
 (6,498),
 (7,498),
 (1,499),
 (6,499),
 (1,500),
 (1,501),
 (7,501),
 (1,502),
 (1,503),
 (1,504),
 (1,505),
 (1,506),
 (1,507),
 (1,508),
 (1,509),
 (1,510),
 (1,511),
 (1,512),
 (1,513),
 (1,514),
 (1,515),
 (1,516);
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
INSERT INTO `servicedomainformat` (`SDRID`,`FORMAT`) VALUES 
 (146,'text/xml'),
 (147,'application/vnd.ogc.gml'),
 (147,'text/plain'),
 (148,'image/gif'),
 (148,'image/jpeg'),
 (148,'image/png'),
 (148,'image/wbmp'),
 (149,'image/gif'),
 (149,'image/jpeg'),
 (149,'image/png'),
 (149,'image/tiff'),
 (149,'image/wbmp'),
 (150,'application/vnd.ogc.wms_xml'),
 (151,'image/gif'),
 (151,'image/jpeg'),
 (151,'image/png'),
 (151,'image/wbmp'),
 (152,'application/vnd.ogc.gml'),
 (152,'text/plain'),
 (153,'text/xml'),
 (154,'application/vnd.ogc.wms_xml'),
 (155,'image/gif'),
 (155,'image/jpeg'),
 (155,'image/png'),
 (155,'image/tiff'),
 (155,'image/wbmp'),
 (156,'image/gif'),
 (156,'image/jpeg'),
 (156,'image/png'),
 (156,'image/tiff'),
 (156,'image/wbmp'),
 (157,'text/xml'),
 (158,'application/vnd.ogc.gml'),
 (158,'text/plain'),
 (159,'application/vnd.ogc.wms_xml'),
 (160,'image/gif'),
 (160,'image/jpeg'),
 (160,'image/png'),
 (160,'image/wbmp'),
 (161,'text/xml'),
 (162,'application/vnd.ogc.sld+xml'),
 (163,'application/x-cubestor-any');
INSERT INTO `servicedomainformat` (`SDRID`,`FORMAT`) VALUES 
 (163,'application/x-cubestor-gml.1'),
 (163,'text/html'),
 (164,'application/vnd.ogc.wms_xml'),
 (165,'application/x-cubestor-wkb'),
 (165,'image/gif'),
 (165,'image/jpeg'),
 (165,'image/png'),
 (165,'image/ppm'),
 (165,'image/tiff'),
 (166,'application/x-cubestor-gml.1'),
 (166,'application/x-cubestor-gml.2'),
 (166,'application/x-cubestor-wkb'),
 (166,'image/gif'),
 (166,'image/jpeg'),
 (166,'image/png'),
 (166,'image/ppm'),
 (166,'image/tiff');
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
INSERT INTO `servicedomainresource` (`SDRID`,`SERVICEPROVIDERID`,`DOMAIN`,`GETURL`,`POSTURL`) VALUES 
 (146,30,'DescribeLayer','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&'),
 (147,30,'GetFeatureInfo','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&'),
 (148,30,'GetLegendGraphic','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&'),
 (149,30,'GetMap','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&'),
 (150,30,'GetCapabilities','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&'),
 (151,31,'GetLegendGraphic','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&'),
 (152,31,'GetFeatureInfo','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&');
INSERT INTO `servicedomainresource` (`SDRID`,`SERVICEPROVIDERID`,`DOMAIN`,`GETURL`,`POSTURL`) VALUES 
 (153,31,'DescribeLayer','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&'),
 (154,31,'GetCapabilities','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&'),
 (155,31,'GetMap','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&'),
 (156,32,'GetMap','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&'),
 (157,32,'DescribeLayer','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&'),
 (158,32,'GetFeatureInfo','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&'),
 (159,32,'GetCapabilities','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&');
INSERT INTO `servicedomainresource` (`SDRID`,`SERVICEPROVIDERID`,`DOMAIN`,`GETURL`,`POSTURL`) VALUES 
 (160,32,'GetLegendGraphic','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&'),
 (161,36,'DescribeLayer','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?',NULL),
 (162,36,'GetStyles','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?',NULL),
 (163,36,'GetFeatureInfo','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi'),
 (164,36,'GetCapabilities','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?',NULL),
 (165,36,'GetLegendGraphic','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?',NULL),
 (166,36,'GetMap','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi');
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
INSERT INTO `serviceprovider` (`SERVICEPROVIDERID`,`NAME`,`TITLE`,`ABSTRACTS`,`FEES`,`ACCESSCONSTRAINTS`,`GIVENNAME`,`URL`,`UPDATEDDATE`,`REVIEWED`) VALUES 
 (30,'OGC:WMS','B3Partners mapserver','WMS-based access to different maps. Try B3Partners Portal System at http://www.b3p.nl/','None','None','Map Server 1','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&','2006-11-24 17:07:29',0),
 (31,'OGC:WMS','B3Partners mapserver','WMS-based access to different maps. Try B3Partners Portal System at http://www.b3p.nl/','None','None','Map Server 2','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&','2006-11-24 17:07:42',0),
 (32,'OGC:WMS','B3Partners mapserver','WMS-based access to different maps. Try B3Partners Portal System at http://www.b3p.nl/','None','None','Map Server 3','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&','2006-11-24 17:07:54',0),
 (36,'GetMap','ESA CubeSERV','European Space Agency CubeSERV map server.','None','None','Mapserver 4','http://www.esrin.esa.it/esrin/esrin.html','2006-11-30 15:41:09',0);
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
  `SRS` varchar(50) NOT NULL default '0',
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
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (808,498,'EPSG:4326',NULL,NULL,NULL,NULL,NULL,NULL),
 (809,498,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (810,500,'EPSG:28992',100000,109214,496040,502021,NULL,NULL),
 (811,500,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (812,501,'EPSG:28992',100000,109214,496040,502021,NULL,NULL),
 (813,501,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (814,503,'EPSG:28992',100626,109684,500074,505240,NULL,NULL),
 (815,503,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (816,504,'EPSG:28992',100000,110000,499649,505500,NULL,NULL),
 (817,504,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (818,505,'EPS',NULL,NULL,NULL,NULL,NULL,NULL),
 (819,505,'G:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (820,505,'EPSG:28992',100000,110000,499649,505500,NULL,NULL),
 (821,507,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (822,507,'EPSG:28992',95883.4,111000,491500,500855,NULL,NULL),
 (823,508,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (824,508,'EPSG:28992',95883.4,111000,491500,500855,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (825,509,'EPSG:4326',NULL,NULL,NULL,NULL,NULL,NULL),
 (826,509,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (827,510,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (828,510,'EPSG:28992',29886.4,276290,307285,581180,NULL,NULL),
 (829,511,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (830,511,'EPSG:28992',-126116,419215,145498,748020,NULL,NULL),
 (831,513,'EPSG:28992',107442,114000,500554,507500,NULL,NULL),
 (832,513,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (833,514,'EPSG:28992',107595,113994,500862,507445,NULL,NULL),
 (834,514,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (835,515,'EPSG:28992',107442,114000,500554,507500,NULL,NULL),
 (836,515,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (837,516,'EPSG:4326',NULL,NULL,NULL,NULL,NULL,NULL),
 (838,516,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (839,497,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (840,497,'EPSG:28992',-1,-1,-1,-1,NULL,NULL),
 (841,518,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (842,518,'EPSG:28992',0,124000,0,460000,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (843,519,'EPSG:28992',0,124001,0,459997,NULL,NULL),
 (844,519,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (845,520,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (846,520,'EPSG:4326',NULL,NULL,NULL,NULL,NULL,NULL),
 (847,517,'EPSG:28992',-1,-1,-1,-1,NULL,NULL),
 (848,517,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (849,522,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (850,523,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (851,524,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (852,525,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (853,526,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (854,527,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (855,521,'EPSG:28992',NULL,NULL,NULL,NULL,NULL,NULL),
 (856,521,'EPSG:28992',-1,-1,-1,-1,NULL,NULL),
 (857,709,'EPSG:4326',-184,176,-60,75,NULL,NULL),
 (858,710,'EPSG:4326',-180,179.999420166016,-62.9231796264648,68.6906585693359,NULL,NULL),
 (859,711,'EPSG:4326',-180,180,-55,85,NULL,NULL),
 (860,712,'EPSG:4326',-175,180,-55,75,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (861,713,'EPSG:4326',-184,180,-90,90,NULL,NULL),
 (862,714,'EPSG:4326',-180,180,-55,75,NULL,NULL),
 (863,715,'EPSG:4326',-180,180,-55,75,NULL,NULL),
 (864,716,'EPSG:4326',-184,180,-50,80,NULL,NULL),
 (865,717,'EPSG:4326',-184,180,-79.0616683959961,85,NULL,NULL),
 (866,718,'EPSG:4326',-184,180,-90,85,NULL,NULL),
 (867,719,'EPSG:4326',-180,180,-55,85,NULL,NULL),
 (868,720,'EPSG:4326',-180,180,-50,80,NULL,NULL),
 (869,721,'EPSG:4326',-184,180,-55,80,NULL,NULL),
 (870,722,'EPSG:4326',-184,180,-90,85,NULL,NULL),
 (871,723,'EPSG:4326',-184,180,-55,75,NULL,NULL),
 (872,724,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (873,725,'EPSG:4326',-179.983093261719,179.991439819336,-89.83837890625,83.5204086303711,NULL,NULL),
 (874,726,'EPSG:4326',-180,180,-90,90,NULL,NULL),
 (875,727,'EPSG:4326',-170,180,-55,80,NULL,NULL),
 (876,728,'EPSG:4326',-180,180,-55,85,NULL,NULL),
 (877,708,'EPSG:4326',-184,180,-90,90,NULL,NULL),
 (878,705,'EPSG:26923',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (879,705,'EPSG:32706',NULL,NULL,NULL,NULL,NULL,NULL),
 (880,705,'EPSG:32712',NULL,NULL,NULL,NULL,NULL,NULL),
 (881,705,'EPSG:26716',NULL,NULL,NULL,NULL,NULL,NULL),
 (882,705,'EPSG:32648',NULL,NULL,NULL,NULL,NULL,NULL),
 (883,705,'AUTO:42001',NULL,NULL,NULL,NULL,NULL,NULL),
 (884,705,'EPSG:32755',NULL,NULL,NULL,NULL,NULL,NULL),
 (885,705,'EPSG:27700',NULL,NULL,NULL,NULL,NULL,NULL),
 (886,705,'EPSG:32705',NULL,NULL,NULL,NULL,NULL,NULL),
 (887,705,'EPSG:42104',NULL,NULL,NULL,NULL,NULL,NULL),
 (888,705,'EPSG:32619',NULL,NULL,NULL,NULL,NULL,NULL),
 (889,705,'EPSG:32720',NULL,NULL,NULL,NULL,NULL,NULL),
 (890,705,'EPSG:32609',NULL,NULL,NULL,NULL,NULL,NULL),
 (891,705,'EPSG:26986',NULL,NULL,NULL,NULL,NULL,NULL),
 (892,705,'EPSG:32734',NULL,NULL,NULL,NULL,NULL,NULL),
 (893,705,'EPSG:32603',NULL,NULL,NULL,NULL,NULL,NULL),
 (894,705,'EPSG:26921',NULL,NULL,NULL,NULL,NULL,NULL),
 (895,705,'EPSG:32618',NULL,NULL,NULL,NULL,NULL,NULL),
 (896,705,'EPSG:26722',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (897,705,'EPSG:26715',NULL,NULL,NULL,NULL,NULL,NULL),
 (898,705,'EPSG:32754',NULL,NULL,NULL,NULL,NULL,NULL),
 (899,705,'EPSG:26916',NULL,NULL,NULL,NULL,NULL,NULL),
 (900,705,'EPSG:26720',NULL,NULL,NULL,NULL,NULL,NULL),
 (901,705,'EPSG:26717',NULL,NULL,NULL,NULL,NULL,NULL),
 (902,705,'EPSG:42206',NULL,NULL,NULL,NULL,NULL,NULL),
 (903,705,'EPSG:32626',NULL,NULL,NULL,NULL,NULL,NULL),
 (904,705,'AUTO:42002',NULL,NULL,NULL,NULL,NULL,NULL),
 (905,705,'EPSG:26904',NULL,NULL,NULL,NULL,NULL,NULL),
 (906,705,'EPSG:32643',NULL,NULL,NULL,NULL,NULL,NULL),
 (907,705,'EPSG:32710',NULL,NULL,NULL,NULL,NULL,NULL),
 (908,705,'EPSG:26911',NULL,NULL,NULL,NULL,NULL,NULL),
 (909,705,'EPSG:32651',NULL,NULL,NULL,NULL,NULL,NULL),
 (910,705,'EPSG:32752',NULL,NULL,NULL,NULL,NULL,NULL),
 (911,705,'EPSG:26907',NULL,NULL,NULL,NULL,NULL,NULL),
 (912,705,'EPSG:32652',NULL,NULL,NULL,NULL,NULL,NULL),
 (913,705,'EPSG:32723',NULL,NULL,NULL,NULL,NULL,NULL),
 (914,705,'EPSG:32756',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (915,705,'EPSG:32608',NULL,NULL,NULL,NULL,NULL,NULL),
 (916,705,'EPSG:32725',NULL,NULL,NULL,NULL,NULL,NULL),
 (917,705,'EPSG:26710',NULL,NULL,NULL,NULL,NULL,NULL),
 (918,705,'EPSG:32726',NULL,NULL,NULL,NULL,NULL,NULL),
 (919,705,'EPSG:26906',NULL,NULL,NULL,NULL,NULL,NULL),
 (920,705,'EPSG:32621',NULL,NULL,NULL,NULL,NULL,NULL),
 (921,705,'EPSG:26905',NULL,NULL,NULL,NULL,NULL,NULL),
 (922,705,'EPSG:32740',NULL,NULL,NULL,NULL,NULL,NULL),
 (923,705,'EPSG:32708',NULL,NULL,NULL,NULL,NULL,NULL),
 (924,705,'EPSG:32741',NULL,NULL,NULL,NULL,NULL,NULL),
 (925,705,'EPSG:32714',NULL,NULL,NULL,NULL,NULL,NULL),
 (926,705,'EPSG:26712',NULL,NULL,NULL,NULL,NULL,NULL),
 (927,705,'EPSG:32660',NULL,NULL,NULL,NULL,NULL,NULL),
 (928,705,'EPSG:32736',NULL,NULL,NULL,NULL,NULL,NULL),
 (929,705,'EPSG:32716',NULL,NULL,NULL,NULL,NULL,NULL),
 (930,705,'EPSG:32730',NULL,NULL,NULL,NULL,NULL,NULL),
 (931,705,'EPSG:42103',NULL,NULL,NULL,NULL,NULL,NULL),
 (932,705,'EPSG:26706',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (933,705,'EPSG:32659',NULL,NULL,NULL,NULL,NULL,NULL),
 (934,705,'EPSG:32701',NULL,NULL,NULL,NULL,NULL,NULL),
 (935,705,'EPSG:32623',NULL,NULL,NULL,NULL,NULL,NULL),
 (936,705,'EPSG:32633',NULL,NULL,NULL,NULL,NULL,NULL),
 (937,705,'EPSG:32748',NULL,NULL,NULL,NULL,NULL,NULL),
 (938,705,'EPSG:42204',NULL,NULL,NULL,NULL,NULL,NULL),
 (939,705,'EPSG:32743',NULL,NULL,NULL,NULL,NULL,NULL),
 (940,705,'EPSG:42102',NULL,NULL,NULL,NULL,NULL,NULL),
 (941,705,'EPSG:32607',NULL,NULL,NULL,NULL,NULL,NULL),
 (942,705,'EPSG:32757',NULL,NULL,NULL,NULL,NULL,NULL),
 (943,705,'EPSG:32627',NULL,NULL,NULL,NULL,NULL,NULL),
 (944,705,'EPSG:32628',NULL,NULL,NULL,NULL,NULL,NULL),
 (945,705,'EPSG:26708',NULL,NULL,NULL,NULL,NULL,NULL),
 (946,705,'EPSG:32639',NULL,NULL,NULL,NULL,NULL,NULL),
 (947,705,'EPSG:32724',NULL,NULL,NULL,NULL,NULL,NULL),
 (948,705,'EPSG:32616',NULL,NULL,NULL,NULL,NULL,NULL),
 (949,705,'EPSG:26714',NULL,NULL,NULL,NULL,NULL,NULL),
 (950,705,'EPSG:32704',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (951,705,'EPSG:42205',NULL,NULL,NULL,NULL,NULL,NULL),
 (952,705,'EPSG:42310',NULL,NULL,NULL,NULL,NULL,NULL),
 (953,705,'EPSG:32645',NULL,NULL,NULL,NULL,NULL,NULL),
 (954,705,'EPSG:32601',NULL,NULL,NULL,NULL,NULL,NULL),
 (955,705,'EPSG:32737',NULL,NULL,NULL,NULL,NULL,NULL),
 (956,705,'EPSG:32637',NULL,NULL,NULL,NULL,NULL,NULL),
 (957,705,'EPSG:32635',NULL,NULL,NULL,NULL,NULL,NULL),
 (958,705,'EPSG:32727',NULL,NULL,NULL,NULL,NULL,NULL),
 (959,705,'EPSG:42308',NULL,NULL,NULL,NULL,NULL,NULL),
 (960,705,'EPSG:32715',NULL,NULL,NULL,NULL,NULL,NULL),
 (961,705,'EPSG:32642',NULL,NULL,NULL,NULL,NULL,NULL),
 (962,705,'EPSG:32632',NULL,NULL,NULL,NULL,NULL,NULL),
 (963,705,'EPSG:26705',NULL,NULL,NULL,NULL,NULL,NULL),
 (964,705,'EPSG:26914',NULL,NULL,NULL,NULL,NULL,NULL),
 (965,705,'EPSG:32735',NULL,NULL,NULL,NULL,NULL,NULL),
 (966,705,'EPSG:42303',NULL,NULL,NULL,NULL,NULL,NULL),
 (967,705,'EPSG:26918',NULL,NULL,NULL,NULL,NULL,NULL),
 (968,705,'EPSG:26920',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (969,705,'AUTO:42004',NULL,NULL,NULL,NULL,NULL,NULL),
 (970,705,'EPSG:32656',NULL,NULL,NULL,NULL,NULL,NULL),
 (971,705,'EPSG:32719',NULL,NULL,NULL,NULL,NULL,NULL),
 (972,705,'EPSG:26719',NULL,NULL,NULL,NULL,NULL,NULL),
 (973,705,'EPSG:32717',NULL,NULL,NULL,NULL,NULL,NULL),
 (974,705,'EPSG:32615',NULL,NULL,NULL,NULL,NULL,NULL),
 (975,705,'EPSG:26709',NULL,NULL,NULL,NULL,NULL,NULL),
 (976,705,'EPSG:32638',NULL,NULL,NULL,NULL,NULL,NULL),
 (977,705,'EPSG:26919',NULL,NULL,NULL,NULL,NULL,NULL),
 (978,705,'EPSG:26917',NULL,NULL,NULL,NULL,NULL,NULL),
 (979,705,'EPSG:32747',NULL,NULL,NULL,NULL,NULL,NULL),
 (980,705,'EPSG:27582',NULL,NULL,NULL,NULL,NULL,NULL),
 (981,705,'EPSG:32713',NULL,NULL,NULL,NULL,NULL,NULL),
 (982,705,'EPSG:26910',NULL,NULL,NULL,NULL,NULL,NULL),
 (983,705,'EPSG:42200',NULL,NULL,NULL,NULL,NULL,NULL),
 (984,705,'EPSG:32760',NULL,NULL,NULL,NULL,NULL,NULL),
 (985,705,'EPSG:32739',NULL,NULL,NULL,NULL,NULL,NULL),
 (986,705,'EPSG:42306',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (987,705,'EPSG:42208',NULL,NULL,NULL,NULL,NULL,NULL),
 (988,705,'EPSG:32118',NULL,NULL,NULL,NULL,NULL,NULL),
 (989,705,'EPSG:32722',NULL,NULL,NULL,NULL,NULL,NULL),
 (990,705,'EPSG:42309',NULL,NULL,NULL,NULL,NULL,NULL),
 (991,705,'EPSG:42201',NULL,NULL,NULL,NULL,NULL,NULL),
 (992,705,'EPSG:32733',NULL,NULL,NULL,NULL,NULL,NULL),
 (993,705,'EPSG:41002',NULL,NULL,NULL,NULL,NULL,NULL),
 (994,705,'EPSG:42304',NULL,NULL,NULL,NULL,NULL,NULL),
 (995,705,'EPSG:26915',NULL,NULL,NULL,NULL,NULL,NULL),
 (996,705,'EPSG:32745',NULL,NULL,NULL,NULL,NULL,NULL),
 (997,705,'EPSG:32606',NULL,NULL,NULL,NULL,NULL,NULL),
 (998,705,'EPSG:32654',NULL,NULL,NULL,NULL,NULL,NULL),
 (999,705,'EPSG:32657',NULL,NULL,NULL,NULL,NULL,NULL),
 (1000,705,'EPSG:41001',NULL,NULL,NULL,NULL,NULL,NULL),
 (1001,705,'EPSG:32611',NULL,NULL,NULL,NULL,NULL,NULL),
 (1002,705,'EPSG:32612',NULL,NULL,NULL,NULL,NULL,NULL),
 (1003,705,'EPSG:32711',NULL,NULL,NULL,NULL,NULL,NULL),
 (1004,705,'EPSG:32620',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (1005,705,'AUTO:42003',NULL,NULL,NULL,NULL,NULL,NULL),
 (1006,705,'EPSG:42207',NULL,NULL,NULL,NULL,NULL,NULL),
 (1007,705,'EPSG:32655',NULL,NULL,NULL,NULL,NULL,NULL),
 (1008,705,'EPSG:32605',NULL,NULL,NULL,NULL,NULL,NULL),
 (1009,705,'EPSG:32751',NULL,NULL,NULL,NULL,NULL,NULL),
 (1010,705,'EPSG:32636',NULL,NULL,NULL,NULL,NULL,NULL),
 (1011,705,'EPSG:32647',NULL,NULL,NULL,NULL,NULL,NULL),
 (1012,705,'EPSG:32703',NULL,NULL,NULL,NULL,NULL,NULL),
 (1013,705,'EPSG:32129',NULL,NULL,NULL,NULL,NULL,NULL),
 (1014,705,'EPSG:26903',NULL,NULL,NULL,NULL,NULL,NULL),
 (1015,705,'EPSG:32731',NULL,NULL,NULL,NULL,NULL,NULL),
 (1016,705,'EPSG:32625',NULL,NULL,NULL,NULL,NULL,NULL),
 (1017,705,'EPSG:26912',NULL,NULL,NULL,NULL,NULL,NULL),
 (1018,705,'EPSG:32729',NULL,NULL,NULL,NULL,NULL,NULL),
 (1019,705,'EPSG:4326',NULL,NULL,NULL,NULL,NULL,NULL),
 (1020,705,'EPSG:32750',NULL,NULL,NULL,NULL,NULL,NULL),
 (1021,705,'EPSG:32614',NULL,NULL,NULL,NULL,NULL,NULL),
 (1022,705,'EPSG:32649',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (1023,705,'EPSG:32742',NULL,NULL,NULL,NULL,NULL,NULL),
 (1024,705,'EPSG:32641',NULL,NULL,NULL,NULL,NULL,NULL),
 (1025,705,'EPSG:32653',NULL,NULL,NULL,NULL,NULL,NULL),
 (1026,705,'EPSG:32634',NULL,NULL,NULL,NULL,NULL,NULL),
 (1027,705,'EPSG:32658',NULL,NULL,NULL,NULL,NULL,NULL),
 (1028,705,'EPSG:32746',NULL,NULL,NULL,NULL,NULL,NULL),
 (1029,705,'EPSG:32604',NULL,NULL,NULL,NULL,NULL,NULL),
 (1030,705,'EPSG:26711',NULL,NULL,NULL,NULL,NULL,NULL),
 (1031,705,'EPSG:32759',NULL,NULL,NULL,NULL,NULL,NULL),
 (1032,705,'EPSG:26713',NULL,NULL,NULL,NULL,NULL,NULL),
 (1033,705,'EPSG:32732',NULL,NULL,NULL,NULL,NULL,NULL),
 (1034,705,'EPSG:100001',NULL,NULL,NULL,NULL,NULL,NULL),
 (1035,705,'EPSG:26922',NULL,NULL,NULL,NULL,NULL,NULL),
 (1036,705,'EPSG:42302',NULL,NULL,NULL,NULL,NULL,NULL),
 (1037,705,'EPSG:42203',NULL,NULL,NULL,NULL,NULL,NULL),
 (1038,705,'EPSG:32644',NULL,NULL,NULL,NULL,NULL,NULL),
 (1039,705,'EPSG:32630',NULL,NULL,NULL,NULL,NULL,NULL),
 (1040,705,'EPSG:32640',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (1041,705,'EPSG:32613',NULL,NULL,NULL,NULL,NULL,NULL),
 (1042,705,'EPSG:32646',NULL,NULL,NULL,NULL,NULL,NULL),
 (1043,705,'EPSG:32128',NULL,NULL,NULL,NULL,NULL,NULL),
 (1044,705,'EPSG:32624',NULL,NULL,NULL,NULL,NULL,NULL),
 (1045,705,'EPSG:4269',NULL,NULL,NULL,NULL,NULL,NULL),
 (1046,705,'EPSG:26930',NULL,NULL,NULL,NULL,NULL,NULL),
 (1047,705,'EPSG:32753',NULL,NULL,NULL,NULL,NULL,NULL),
 (1048,705,'EPSG:42101',NULL,NULL,NULL,NULL,NULL,NULL),
 (1049,705,'EPSG:32622',NULL,NULL,NULL,NULL,NULL,NULL),
 (1050,705,'EPSG:26909',NULL,NULL,NULL,NULL,NULL,NULL),
 (1051,705,'EPSG:32728',NULL,NULL,NULL,NULL,NULL,NULL),
 (1052,705,'EPSG:32650',NULL,NULL,NULL,NULL,NULL,NULL),
 (1053,705,'EPSG:42106',NULL,NULL,NULL,NULL,NULL,NULL),
 (1054,705,'EPSG:26703',NULL,NULL,NULL,NULL,NULL,NULL),
 (1055,705,'EPSG:32707',NULL,NULL,NULL,NULL,NULL,NULL),
 (1056,705,'EPSG:32631',NULL,NULL,NULL,NULL,NULL,NULL),
 (1057,705,'EPSG:32721',NULL,NULL,NULL,NULL,NULL,NULL),
 (1058,705,'EPSG:42301',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (1059,705,'EPSG:32718',NULL,NULL,NULL,NULL,NULL,NULL),
 (1060,705,'EPSG:32749',NULL,NULL,NULL,NULL,NULL,NULL),
 (1061,705,'EPSG:32709',NULL,NULL,NULL,NULL,NULL,NULL),
 (1062,705,'EPSG:26987',NULL,NULL,NULL,NULL,NULL,NULL),
 (1063,705,'EPSG:26721',NULL,NULL,NULL,NULL,NULL,NULL),
 (1064,705,'EPSG:32610',NULL,NULL,NULL,NULL,NULL,NULL),
 (1065,705,'EPSG:4267',NULL,NULL,NULL,NULL,NULL,NULL),
 (1066,705,'EPSG:42305',NULL,NULL,NULL,NULL,NULL,NULL),
 (1067,705,'EPSG:32758',NULL,NULL,NULL,NULL,NULL,NULL),
 (1068,705,'EPSG:42202',NULL,NULL,NULL,NULL,NULL,NULL),
 (1069,705,'EPSG:26707',NULL,NULL,NULL,NULL,NULL,NULL),
 (1070,705,'EPSG:32738',NULL,NULL,NULL,NULL,NULL,NULL),
 (1071,705,'EPSG:26704',NULL,NULL,NULL,NULL,NULL,NULL),
 (1072,705,'EPSG:42105',NULL,NULL,NULL,NULL,NULL,NULL),
 (1073,705,'EPSG:26913',NULL,NULL,NULL,NULL,NULL,NULL),
 (1074,705,'EPSG:42307',NULL,NULL,NULL,NULL,NULL,NULL),
 (1075,705,'EPSG:32629',NULL,NULL,NULL,NULL,NULL,NULL),
 (1076,705,'EPSG:32702',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `srs` (`SRSID`,`LAYERID`,`SRS`,`MINX`,`MAXX`,`MINY`,`MAXY`,`RESX`,`RESY`) VALUES 
 (1077,705,'EPSG:26908',NULL,NULL,NULL,NULL,NULL,NULL),
 (1078,705,'EPSG:32617',NULL,NULL,NULL,NULL,NULL,NULL),
 (1079,705,'EPSG:26718',NULL,NULL,NULL,NULL,NULL,NULL),
 (1080,705,'EPSG:32744',NULL,NULL,NULL,NULL,NULL,NULL),
 (1081,705,'EPSG:32602',NULL,NULL,NULL,NULL,NULL,NULL);
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
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (329,500,'default','default',NULL,'0',NULL),
 (330,501,'default','default',NULL,'0',NULL),
 (331,503,'default','default',NULL,'0',NULL),
 (332,504,'default','default',NULL,'0',NULL),
 (333,505,'default','default',NULL,'0',NULL),
 (334,507,'default','default',NULL,'0',NULL),
 (335,508,'default','default',NULL,'0',NULL),
 (336,509,'default','default',NULL,'0',NULL),
 (337,510,'default','default',NULL,'0',NULL),
 (338,511,'default','default',NULL,'0',NULL),
 (339,513,'default','default',NULL,'0',NULL),
 (340,514,'default','default',NULL,'0',NULL),
 (341,515,'default','default',NULL,'0',NULL),
 (342,516,'default','default',NULL,'0',NULL),
 (343,518,'default','default',NULL,'0',NULL),
 (344,519,'default','default',NULL,'0',NULL),
 (345,520,'default','default',NULL,'0',NULL),
 (513,731,'brown','brown',NULL,'0',NULL),
 (514,732,'powderblue','powderblue',NULL,'0',NULL),
 (515,733,'black','black',NULL,'0',NULL),
 (516,734,'wheat','wheat',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (517,735,'snow4','snow4',NULL,'0',NULL),
 (518,737,'chocolate','chocolate',NULL,'0',NULL),
 (519,738,'sandybrown','sandybrown',NULL,'0',NULL),
 (520,740,'0xb8d8f6','0xb8d8f6',NULL,'0',NULL),
 (521,741,'black','black',NULL,'0',NULL),
 (522,742,'black','black',NULL,'0',NULL),
 (523,743,'black','black',NULL,'0',NULL),
 (524,744,'transparent/black','transparent/black',NULL,'0',NULL),
 (525,745,'black','black',NULL,'0',NULL),
 (526,746,'white','white',NULL,'0',NULL),
 (527,747,'black','black',NULL,'0',NULL),
 (528,749,'default','default',NULL,'0',NULL),
 (529,750,'default','default',NULL,'0',NULL),
 (530,751,'default','default',NULL,'0',NULL),
 (531,752,'default','default',NULL,'0',NULL),
 (532,754,'0x00df00','0x00df00',NULL,'0',NULL),
 (533,755,'0x78bb3e','0x78bb3e',NULL,'0',NULL),
 (534,756,'0xaaa24e','0xaaa24e',NULL,'0',NULL),
 (535,757,'0x28a028','0x28a028',NULL,'0',NULL),
 (536,758,'0xf5deb3','0xf5deb3',NULL,'0',NULL),
 (537,760,'COLORMAP_ETOPO5','COLORMAP_ETOPO5',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (538,761,'classic shaded','classic shaded',NULL,'0',NULL),
 (539,761,'desaturated','desaturated',NULL,'0',NULL),
 (540,761,'classic','classic',NULL,'0',NULL),
 (541,761,'color shaded','color shaded',NULL,'0',NULL),
 (542,761,'color','color',NULL,'0',NULL),
 (543,761,'desaturated shaded','desaturated shaded',NULL,'0',NULL),
 (544,763,'0x101040','0x101040',NULL,'0',NULL),
 (545,764,'darkblue','darkblue',NULL,'0',NULL),
 (546,765,'darkblue','darkblue',NULL,'0',NULL),
 (547,766,'0xb8d8f6/0x101040','0xb8d8f6/0x101040',NULL,'0',NULL),
 (548,767,'black','black',NULL,'0',NULL),
 (549,768,'black/white','black/white',NULL,'0',NULL),
 (550,769,'black','black',NULL,'0',NULL),
 (551,771,'grey10','grey10',NULL,'0',NULL),
 (552,772,'grey10','grey10',NULL,'0',NULL),
 (553,773,'grey10','grey10',NULL,'0',NULL),
 (554,774,'grey10','grey10',NULL,'0',NULL),
 (555,775,'black','black',NULL,'0',NULL),
 (556,777,'grey50','grey50',NULL,'0',NULL),
 (557,778,'grey10','grey10',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (558,780,'black','black',NULL,'0',NULL),
 (559,781,'purple','purple',NULL,'0',NULL),
 (560,782,'0x0000FF','0x0000FF',NULL,'0',NULL),
 (561,783,'0xff8080','0xff8080',NULL,'0',NULL),
 (562,784,'purple','purple',NULL,'0',NULL),
 (563,786,'default','default',NULL,'0',NULL),
 (564,787,'default','default',NULL,'0',NULL),
 (565,788,'default','default',NULL,'0',NULL),
 (566,789,'default','default',NULL,'0',NULL),
 (567,790,'default','default',NULL,'0',NULL),
 (568,791,'default','default',NULL,'0',NULL),
 (569,792,'default','default',NULL,'0',NULL),
 (570,793,'default','default',NULL,'0',NULL),
 (571,794,'default','default',NULL,'0',NULL),
 (572,795,'default','default',NULL,'0',NULL),
 (573,796,'default','default',NULL,'0',NULL),
 (574,797,'default','default',NULL,'0',NULL),
 (575,799,'default','default',NULL,'0',NULL),
 (576,800,'default','default',NULL,'0',NULL),
 (577,801,'default','default',NULL,'0',NULL),
 (578,803,'black','black',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (579,804,'pink','pink',NULL,'0',NULL),
 (580,805,'black','black',NULL,'0',NULL),
 (581,806,'black','black',NULL,'0',NULL),
 (582,807,'black','black',NULL,'0',NULL),
 (583,808,'lightgreen','lightgreen',NULL,'0',NULL),
 (584,810,'default','default',NULL,'0',NULL),
 (585,812,'0xaa1818','0xaa1818',NULL,'0',NULL),
 (586,813,'black','black',NULL,'0',NULL),
 (587,814,'black','black',NULL,'0',NULL),
 (588,815,'black','black',NULL,'0',NULL),
 (589,816,'0x760000','0x760000',NULL,'0',NULL),
 (590,817,'black','black',NULL,'0',NULL),
 (591,818,'black','black',NULL,'0',NULL),
 (592,819,'grey50','grey50',NULL,'0',NULL),
 (593,820,'black','black',NULL,'0',NULL),
 (594,821,'black','black',NULL,'0',NULL),
 (595,823,'1 degree','1 degree',NULL,'0',NULL),
 (596,823,'0.2 degree','0.2 degree',NULL,'0',NULL),
 (597,823,'5 degree','5 degree',NULL,'0',NULL),
 (598,823,'10 degree','10 degree',NULL,'0',NULL),
 (599,823,'2 degree','2 degree',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (600,824,'magenta wire','magenta wire',NULL,'0',NULL),
 (601,824,'black wire','black wire',NULL,'0',NULL),
 (602,824,'blue wire','blue wire',NULL,'0',NULL),
 (603,824,'white wire','white wire',NULL,'0',NULL),
 (604,824,'cyan wire','cyan wire',NULL,'0',NULL),
 (605,824,'red wire','red wire',NULL,'0',NULL),
 (606,824,'yellow wire','yellow wire',NULL,'0',NULL),
 (607,824,'green wire','green wire',NULL,'0',NULL),
 (608,828,'black','black',NULL,'0',NULL),
 (609,829,'black','black',NULL,'0',NULL),
 (610,830,'0xaa1818','0xaa1818',NULL,'0',NULL),
 (611,831,'black','black',NULL,'0',NULL),
 (612,832,'black','black',NULL,'0',NULL),
 (613,833,'black','black',NULL,'0',NULL),
 (614,834,'black','black',NULL,'0',NULL),
 (615,835,'black','black',NULL,'0',NULL),
 (616,836,'0x760000','0x760000',NULL,'0',NULL),
 (617,838,'black','black',NULL,'0',NULL),
 (618,839,'black','black',NULL,'0',NULL),
 (619,840,'black','black',NULL,'0',NULL),
 (620,841,'black','black',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (621,842,'Navteq','Navteq',NULL,'0',NULL),
 (622,843,'black','black',NULL,'0',NULL),
 (623,844,'0xb8d8f6','0xb8d8f6',NULL,'0',NULL),
 (624,845,'navy','navy',NULL,'0',NULL),
 (625,847,'chocolate','chocolate',NULL,'0',NULL),
 (626,848,'sandybrown','sandybrown',NULL,'0',NULL),
 (627,850,'darkblue','darkblue',NULL,'0',NULL),
 (628,851,'black','black',NULL,'0',NULL),
 (629,852,'0xb8d8f6/navy','0xb8d8f6/navy',NULL,'0',NULL),
 (630,853,'black/white','black/white',NULL,'0',NULL),
 (631,854,'darkblue','darkblue',NULL,'0',NULL),
 (632,855,'navy','navy',NULL,'0',NULL),
 (633,856,'black','black',NULL,'0',NULL),
 (634,858,'desaturated','desaturated',NULL,'0',NULL),
 (635,858,'SHADE','SHADE',NULL,'0',NULL),
 (636,858,'color','color',NULL,'0',NULL),
 (637,858,'ELEV_NOSHADE','Elevations No Shade',NULL,'0',NULL),
 (638,858,'classic','classic',NULL,'0',NULL),
 (639,858,'Low Elevation Spread','Low Elevation Spread',NULL,'0',NULL),
 (640,858,'desaturated shaded','desaturated shaded',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (641,858,'classic shaded','classic shaded',NULL,'0',NULL),
 (642,858,'color shaded','color shaded',NULL,'0',NULL),
 (643,859,'Classic','Classic',NULL,'0',NULL),
 (644,859,'Classic Shaded','Classic Shaded',NULL,'0',NULL),
 (645,860,'RELIEF','RELIEF',NULL,'0',NULL),
 (646,862,'0x78bb3e','0x78bb3e',NULL,'0',NULL),
 (647,863,'0xf5deb3','0xf5deb3',NULL,'0',NULL),
 (648,864,'0x28a028','0x28a028',NULL,'0',NULL),
 (649,865,'0xaaa24e','0xaaa24e',NULL,'0',NULL),
 (650,866,'0x00df00','0x00df00',NULL,'0',NULL),
 (651,868,'grey10','grey10',NULL,'0',NULL),
 (652,869,'grey10','grey10',NULL,'0',NULL),
 (653,870,'black','black',NULL,'0',NULL),
 (654,871,'grey10','grey10',NULL,'0',NULL),
 (655,872,'grey10','grey10',NULL,'0',NULL),
 (656,874,'grey10','grey10',NULL,'0',NULL),
 (657,876,'lightgreen','lightgreen',NULL,'0',NULL),
 (658,877,'black','black',NULL,'0',NULL),
 (659,878,'black','black',NULL,'0',NULL),
 (660,879,'pink','pink',NULL,'0',NULL);
INSERT INTO `style` (`STYLEID`,`LAYERID`,`NAME`,`TITLE`,`ABSTRACTS`,`STYLEURL`,`STYLESHEETURL`) VALUES 
 (661,880,'black','black',NULL,'0',NULL),
 (662,881,'black','black',NULL,'0',NULL),
 (663,883,'0xfe8080','0xfe8080',NULL,'0',NULL),
 (664,884,'0x0000FE','0x0000FE',NULL,'0',NULL),
 (665,885,'black','black',NULL,'0',NULL),
 (666,886,'purple','purple',NULL,'0',NULL),
 (667,887,'purple','purple',NULL,'0',NULL),
 (668,889,'brown','brown',NULL,'0',NULL),
 (669,890,'black','black',NULL,'0',NULL),
 (670,891,'snow4','snow4',NULL,'0',NULL),
 (671,892,'powderblue','powderblue',NULL,'0',NULL),
 (672,893,'wheat','wheat',NULL,'0',NULL),
 (673,896,'red','red',NULL,'0',NULL),
 (674,897,'COLORMAP_ROX','COLORMAP_ROX',NULL,'0',NULL),
 (675,899,'COLORMAP_GEN','COLORMAP_GEN',NULL,'0',NULL),
 (676,900,'COLORMAP_PRE','COLORMAP_PRE',NULL,'0',NULL),
 (677,901,'COLORMAP_FLS','COLORMAP_FLS',NULL,'0',NULL),
 (678,902,'red','red',NULL,'0',NULL),
 (679,903,'COLORMAP_MAF','COLORMAP_MAF',NULL,'0',NULL);
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
INSERT INTO `styledomainformat` (`SDRID`,`FORMAT`) VALUES 
 (329,'image/png'),
 (330,'image/png'),
 (331,'image/png'),
 (332,'image/png'),
 (333,'image/png'),
 (334,'image/png'),
 (335,'image/png'),
 (336,'image/png'),
 (337,'image/png'),
 (338,'image/png'),
 (339,'image/png'),
 (340,'image/png'),
 (341,'image/png'),
 (342,'image/png'),
 (343,'image/png'),
 (344,'image/png'),
 (345,'image/png'),
 (513,'image/gif'),
 (514,'image/gif'),
 (515,'image/gif'),
 (516,'image/gif'),
 (517,'image/gif'),
 (518,'image/gif'),
 (519,'image/gif'),
 (520,'image/gif'),
 (521,'image/gif'),
 (522,'image/gif'),
 (523,'image/gif'),
 (524,'image/gif'),
 (525,'image/gif'),
 (526,'image/gif'),
 (527,'image/gif'),
 (528,'image/gif'),
 (529,'image/gif'),
 (530,'image/gif'),
 (531,'image/gif'),
 (532,'image/gif'),
 (533,'image/gif'),
 (534,'image/gif'),
 (535,'image/gif'),
 (536,'image/gif'),
 (537,'image/gif'),
 (538,'image/gif'),
 (539,'image/gif'),
 (540,'image/gif'),
 (541,'image/gif'),
 (542,'image/gif'),
 (543,'image/gif'),
 (544,'image/gif');
INSERT INTO `styledomainformat` (`SDRID`,`FORMAT`) VALUES 
 (545,'image/gif'),
 (546,'image/gif'),
 (547,'image/gif'),
 (548,'image/gif'),
 (549,'image/gif'),
 (550,'image/gif'),
 (551,'image/gif'),
 (552,'image/gif'),
 (553,'image/gif'),
 (554,'image/gif'),
 (555,'image/gif'),
 (556,'image/gif'),
 (557,'image/gif'),
 (558,'image/gif'),
 (559,'image/gif'),
 (560,'image/gif'),
 (561,'image/gif'),
 (562,'image/gif'),
 (563,'image/gif'),
 (564,'image/gif'),
 (565,'image/gif'),
 (566,'image/gif'),
 (567,'image/gif'),
 (568,'image/gif'),
 (569,'image/gif'),
 (570,'image/gif'),
 (571,'image/gif'),
 (572,'image/gif'),
 (573,'image/gif'),
 (574,'image/gif'),
 (575,'image/gif'),
 (576,'image/gif'),
 (577,'image/gif'),
 (578,'image/gif'),
 (579,'image/gif'),
 (580,'image/gif'),
 (581,'image/gif'),
 (582,'image/gif'),
 (583,'image/gif'),
 (584,'image/gif'),
 (585,'image/gif'),
 (586,'image/gif'),
 (587,'image/gif'),
 (588,'image/gif'),
 (589,'image/gif'),
 (590,'image/gif'),
 (591,'image/gif'),
 (592,'image/gif'),
 (593,'image/gif');
INSERT INTO `styledomainformat` (`SDRID`,`FORMAT`) VALUES 
 (594,'image/gif'),
 (595,'image/gif'),
 (596,'image/gif'),
 (597,'image/gif'),
 (598,'image/gif'),
 (599,'image/gif'),
 (600,'image/gif'),
 (601,'image/gif'),
 (602,'image/gif'),
 (603,'image/gif'),
 (604,'image/gif'),
 (605,'image/gif'),
 (606,'image/gif'),
 (607,'image/gif'),
 (608,'image/png'),
 (609,'image/png'),
 (610,'image/png'),
 (611,'image/png'),
 (612,'image/png'),
 (613,'image/png'),
 (614,'image/png'),
 (615,'image/png'),
 (616,'image/png'),
 (617,'image/png'),
 (618,'image/png'),
 (619,'image/png'),
 (620,'image/png'),
 (621,'image/png'),
 (622,'image/png'),
 (623,'image/png'),
 (624,'image/png'),
 (625,'image/png'),
 (626,'image/png'),
 (627,'image/png'),
 (628,'image/png'),
 (629,'image/png'),
 (630,'image/png'),
 (631,'image/png'),
 (632,'image/png'),
 (633,'image/png'),
 (634,'image/png'),
 (635,'image/png'),
 (636,'image/png'),
 (637,'image/png'),
 (638,'image/png'),
 (639,'image/png'),
 (640,'image/png'),
 (641,'image/png'),
 (642,'image/png');
INSERT INTO `styledomainformat` (`SDRID`,`FORMAT`) VALUES 
 (643,'image/png'),
 (644,'image/png'),
 (645,'image/png'),
 (646,'image/png'),
 (647,'image/png'),
 (648,'image/png'),
 (649,'image/png'),
 (650,'image/png'),
 (651,'image/png'),
 (652,'image/png'),
 (653,'image/png'),
 (654,'image/png'),
 (655,'image/png'),
 (656,'image/png'),
 (657,'image/png'),
 (658,'image/png'),
 (659,'image/png'),
 (660,'image/png'),
 (661,'image/png'),
 (662,'image/png'),
 (663,'image/png'),
 (664,'image/png'),
 (665,'image/png'),
 (666,'image/png'),
 (667,'image/png'),
 (668,'image/png'),
 (669,'image/png'),
 (670,'image/png'),
 (671,'image/png'),
 (672,'image/png'),
 (673,'image/png'),
 (674,'image/png'),
 (675,'image/png'),
 (676,'image/png'),
 (677,'image/png'),
 (678,'image/png'),
 (679,'image/png');
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
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (329,329,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=beverwijk_wegen&format=image/png',NULL,NULL),
 (330,330,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=beverwijk_all&format=image/png',NULL,NULL),
 (331,331,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=heemskerktxt&format=image/png',NULL,NULL),
 (332,332,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=heemskerk_wegen&format=image/png',NULL,NULL),
 (333,333,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=heemskerk_all&format=image/png',NULL,NULL),
 (334,334,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=velsen_all&format=image/png',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (335,335,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=velsen_wegen&format=image/png',NULL,NULL),
 (336,336,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=gemeentes_ijmond&format=image/png',NULL,NULL),
 (337,337,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=demo_autowegen&format=image/png',NULL,NULL),
 (338,338,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=demo_basis&format=image/png',NULL,NULL),
 (339,339,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=uitgeest_wegen&format=image/png',NULL,NULL),
 (340,340,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=uitgeesttxt&format=image/png',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (341,341,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=uitgeest_all&format=image/png',NULL,NULL),
 (342,342,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=gemeentes&format=image/png',NULL,NULL),
 (343,343,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=gbkn-all&format=image/png',NULL,NULL),
 (344,344,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=gbkn-annotaties&format=image/png',NULL,NULL),
 (345,345,'LegendURL','http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&version=1.1.1&service=WMS&request=GetLegendGraphic&layer=gemeentes&format=image/png',NULL,NULL),
 (513,513,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GROUNDA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=brown&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (514,514,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=SEAICEA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=powderblue&FORMAT=image%2Fgif',NULL,NULL),
 (515,515,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=PHYSTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (516,516,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=LNDFRML_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=wheat&FORMAT=image%2Fgif',NULL,NULL),
 (517,517,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=LANDICEA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=snow4&FORMAT=image%2Fgif',NULL,NULL),
 (518,518,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=CONTOURL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=chocolate&FORMAT=image%2Fgif',NULL,NULL),
 (519,519,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ELEVP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=sandybrown&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (520,520,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=OCEANSEA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0xb8d8f6&FORMAT=image%2Fgif',NULL,NULL),
 (521,521,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DEPTHL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (522,522,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (523,523,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BNDTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (524,524,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=transparent%2Fblack&FORMAT=image%2Fgif',NULL,NULL),
 (525,525,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (526,526,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=COASTL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=white&FORMAT=image%2Fgif',NULL,NULL),
 (527,527,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BARRIERL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (528,528,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ASAR_WSM%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (529,529,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MERIS_RR%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (530,530,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ASAR%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (531,531,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AATSR%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (532,532,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GRASSA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0x00df00&FORMAT=image%2Fgif',NULL,NULL),
 (533,533,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=SWAMPA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0x78bb3e&FORMAT=image%2Fgif',NULL,NULL),
 (534,534,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TUNDRAA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0xaaa24e&FORMAT=image%2Fgif',NULL,NULL),
 (535,535,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TREESA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0x28a028&FORMAT=image%2Fgif',NULL,NULL),
 (536,536,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=CROPA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0xf5deb3&FORMAT=image%2Fgif',NULL,NULL),
 (537,537,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ETOPO5%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=COLORMAP_ETOPO5&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (538,538,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=classic+shaded&FORMAT=image%2Fgif',NULL,NULL),
 (539,539,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=desaturated&FORMAT=image%2Fgif',NULL,NULL),
 (540,540,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=classic&FORMAT=image%2Fgif',NULL,NULL),
 (541,541,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=color+shaded&FORMAT=image%2Fgif',NULL,NULL),
 (542,542,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=color&FORMAT=image%2Fgif',NULL,NULL),
 (543,543,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AMapAdmin&SPATIAL_TYPE=RASTER&STYLE=desaturated+shaded&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (544,544,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=WATRCRSL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=0x101040&FORMAT=image%2Fgif',NULL,NULL),
 (545,545,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DANGERP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=darkblue&FORMAT=image%2Fgif',NULL,NULL),
 (546,546,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AQUECANL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=darkblue&FORMAT=image%2Fgif',NULL,NULL),
 (547,547,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=INWATERA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0xb8d8f6%2F0x101040&FORMAT=image%2Fgif',NULL,NULL),
 (548,548,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISCP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (549,549,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=HYDROTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black%2Fwhite&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (550,550,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISCL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (551,551,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=grey10&FORMAT=image%2Fgif',NULL,NULL),
 (552,552,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey10&FORMAT=image%2Fgif',NULL,NULL),
 (553,553,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=PIPEL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey10&FORMAT=image%2Fgif',NULL,NULL),
 (554,554,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_UTIL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey10&FORMAT=image%2Fgif',NULL,NULL),
 (555,555,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (556,556,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_TRANS_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey50&FORMAT=image%2Fgif',NULL,NULL),
 (557,557,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_UTIL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey10&FORMAT=image%2Fgif',NULL,NULL),
 (558,558,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISPOPP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (559,559,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISPOPA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=purple&FORMAT=image%2Fgif',NULL,NULL),
 (560,560,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POPTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=0x0000FF&FORMAT=image%2Fgif',NULL,NULL),
 (561,561,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BUILTUPA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=0xff8080&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (562,562,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BUILTUPP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=purple&FORMAT=image%2Fgif',NULL,NULL),
 (563,563,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR022002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (564,564,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR112002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (565,565,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR062002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (566,566,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR102002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (567,567,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR082002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (568,568,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR122002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (569,569,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR072002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (570,570,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR052002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (571,571,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR042002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (572,572,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR032002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (573,573,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR092002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (574,574,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GLOBSCAR012002%3AMapAdmin&SPATIAL_TYPE=POLYGON&FORMAT=image%2Fgif',NULL,NULL),
 (575,575,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=WORLD_DMSP_NIGHT%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (576,576,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=WORLD_MODIS_5KM%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (577,577,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=WORLD_MODIS_1KM%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (578,578,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=STORAGEP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (579,579,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=EXTRACTA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=pink&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (580,580,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=EXTRACTP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (581,581,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=INDTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (582,582,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISINDP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (583,583,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=FISHINDA_1M%3AMapAdmin&SPATIAL_TYPE=POLYGON&STYLE=lightgreen&FORMAT=image%2Fgif',NULL,NULL),
 (584,584,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GISD_ROI%3AMapAdmin&SPATIAL_TYPE=RASTER&FORMAT=image%2Fgif',NULL,NULL),
 (585,585,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ROADL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=0xaa1818&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (586,586,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=RRYARDP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (587,587,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRAILL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (588,588,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTRL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (589,589,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=RAILRDL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=0x760000&FORMAT=image%2Fgif',NULL,NULL),
 (590,590,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISTRANL_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (591,591,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AEROFACP_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (592,592,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_TRANS_1M%3AMapAdmin&SPATIAL_TYPE=LINE&STYLE=grey50&FORMAT=image%2Fgif',NULL,NULL),
 (593,593,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTRC_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (594,594,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTXT_1M%3AMapAdmin&SPATIAL_TYPE=POINT&STYLE=black&FORMAT=image%2Fgif',NULL,NULL),
 (595,595,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=graticule&STYLE=1+degree&FORMAT=image%2Fgif',NULL,NULL),
 (596,596,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=graticule&STYLE=0.2+degree&FORMAT=image%2Fgif',NULL,NULL),
 (597,597,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=graticule&STYLE=5+degree&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (598,598,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=graticule&STYLE=10+degree&FORMAT=image%2Fgif',NULL,NULL),
 (599,599,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=graticule&STYLE=2+degree&FORMAT=image%2Fgif',NULL,NULL),
 (600,600,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=magenta+wire&FORMAT=image%2Fgif',NULL,NULL),
 (601,601,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=black+wire&FORMAT=image%2Fgif',NULL,NULL),
 (602,602,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=blue+wire&FORMAT=image%2Fgif',NULL,NULL),
 (603,603,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=white+wire&FORMAT=image%2Fgif',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (604,604,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=cyan+wire&FORMAT=image%2Fgif',NULL,NULL),
 (605,605,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=red+wire&FORMAT=image%2Fgif',NULL,NULL),
 (606,606,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=yellow+wire&FORMAT=image%2Fgif',NULL,NULL),
 (607,607,'LegendURL','http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=crosshairs&STYLE=green+wire&FORMAT=image%2Fgif',NULL,NULL),
 (608,608,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=RRYARDP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (609,609,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AEROFACP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (610,610,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ROADL_1M%3AFoundation&STYLE=0xaa1818&FORMAT=image%2Fpng',NULL,NULL),
 (611,611,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTRL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (612,612,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRAILL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (613,613,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISTRANL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (614,614,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTRC_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (615,615,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TRANSTXT_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (616,616,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=RAILRDL_1M%3AFoundation&STYLE=0x760000&FORMAT=image%2Fpng',NULL,NULL),
 (617,617,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BNDTXT_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (618,618,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (619,619,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DEPTHL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (620,620,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (621,621,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POLBNDA_1M%3AFoundation&STYLE=Navteq&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (622,622,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BARRIERL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (623,623,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=OCEANSEA_1M%3AFoundation&STYLE=0xb8d8f6&FORMAT=image%2Fpng',NULL,NULL),
 (624,624,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=COASTL_1M%3AFoundation&STYLE=navy&FORMAT=image%2Fpng',NULL,NULL),
 (625,625,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=CONTOURL_1M%3AFoundation&STYLE=chocolate&FORMAT=image%2Fpng',NULL,NULL),
 (626,626,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ELEVP_1M%3AFoundation&STYLE=sandybrown&FORMAT=image%2Fpng',NULL,NULL),
 (627,627,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AQUECANL_1M%3AFoundation&STYLE=darkblue&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (628,628,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISCP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (629,629,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=INWATERA_1M%3AFoundation&STYLE=0xb8d8f6%2Fnavy&FORMAT=image%2Fpng',NULL,NULL),
 (630,630,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=HYDROTXT_1M%3AFoundation&STYLE=black%2Fwhite&FORMAT=image%2Fpng',NULL,NULL),
 (631,631,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DANGERP_1M%3AFoundation&STYLE=darkblue&FORMAT=image%2Fpng',NULL,NULL),
 (632,632,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=WATRCRSL_1M%3AFoundation&STYLE=navy&FORMAT=image%2Fpng',NULL,NULL),
 (633,633,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISCL_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (634,634,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=desaturated&FORMAT=image%2Fpng',NULL,NULL),
 (635,635,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=SHADE&FORMAT=image%2Fpng',NULL,NULL),
 (636,636,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=color&FORMAT=image%2Fpng',NULL,NULL),
 (637,637,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=ELEV_NOSHADE&FORMAT=image%2Fpng',NULL,NULL),
 (638,638,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=classic&FORMAT=image%2Fpng',NULL,NULL),
 (639,639,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=Low+Elevation+Spread&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (640,640,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=desaturated+shaded&FORMAT=image%2Fpng',NULL,NULL),
 (641,641,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=classic+shaded&FORMAT=image%2Fpng',NULL,NULL),
 (642,642,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GTOPO30%3AFoundation&STYLE=color+shaded&FORMAT=image%2Fpng',NULL,NULL),
 (643,643,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ETOPO2%3AFoundation&STYLE=Classic&FORMAT=image%2Fpng',NULL,NULL),
 (644,644,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=ETOPO2%3AFoundation&STYLE=Classic+Shaded&FORMAT=image%2Fpng',NULL,NULL),
 (645,645,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=RELIEF%3AFoundation&STYLE=RELIEF&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (646,646,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=SWAMPA_1M%3AFoundation&STYLE=0x78bb3e&FORMAT=image%2Fpng',NULL,NULL),
 (647,647,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=CROPA_1M%3AFoundation&STYLE=0xf5deb3&FORMAT=image%2Fpng',NULL,NULL),
 (648,648,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TREESA_1M%3AFoundation&STYLE=0x28a028&FORMAT=image%2Fpng',NULL,NULL),
 (649,649,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=TUNDRAA_1M%3AFoundation&STYLE=0xaaa24e&FORMAT=image%2Fpng',NULL,NULL),
 (650,650,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GRASSA_1M%3AFoundation&STYLE=0x00df00&FORMAT=image%2Fpng',NULL,NULL),
 (651,651,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILL_1M%3AFoundation&STYLE=grey10&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (652,652,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_UTIL_1M%3AFoundation&STYLE=grey10&FORMAT=image%2Fpng',NULL,NULL),
 (653,653,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILTXT_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (654,654,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=PIPEL_1M%3AFoundation&STYLE=grey10&FORMAT=image%2Fpng',NULL,NULL),
 (655,655,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=UTILP_1M%3AFoundation&STYLE=grey10&FORMAT=image%2Fpng',NULL,NULL),
 (656,656,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=DQLINE_UTIL_1M%3AFoundation&STYLE=grey10&FORMAT=image%2Fpng',NULL,NULL),
 (657,657,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=FISHINDA_1M%3AFoundation&STYLE=lightgreen&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (658,658,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=STORAGEP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (659,659,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=EXTRACTP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (660,660,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=EXTRACTA_1M%3AFoundation&STYLE=pink&FORMAT=image%2Fpng',NULL,NULL),
 (661,661,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISINDP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (662,662,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=INDTXT_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (663,663,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BUILTUPA_1M%3AFoundation&STYLE=0xfe8080&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (664,664,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=POPTXT_1M%3AFoundation&STYLE=0x0000FE&FORMAT=image%2Fpng',NULL,NULL),
 (665,665,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISPOPP_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL),
 (666,666,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=BUILTUPP_1M%3AFoundation&STYLE=purple&FORMAT=image%2Fpng',NULL,NULL),
 (667,667,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MISPOPA_1M%3AFoundation&STYLE=purple&FORMAT=image%2Fpng',NULL,NULL),
 (668,668,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=GROUNDA_1M%3AFoundation&STYLE=brown&FORMAT=image%2Fpng',NULL,NULL),
 (669,669,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=PHYSTXT_1M%3AFoundation&STYLE=black&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (670,670,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=LANDICEA_1M%3AFoundation&STYLE=snow4&FORMAT=image%2Fpng',NULL,NULL),
 (671,671,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=SEAICEA_1M%3AFoundation&STYLE=powderblue&FORMAT=image%2Fpng',NULL,NULL),
 (672,672,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=LNDFRML_1M%3AFoundation&STYLE=wheat&FORMAT=image%2Fpng',NULL,NULL),
 (673,673,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=FAULTS_CA%3AGSC&STYLE=red&FORMAT=image%2Fpng',NULL,NULL),
 (674,674,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AGE_ROCK_TYPE_CA%3AGSC&STYLE=COLORMAP_ROX&FORMAT=image%2Fpng',NULL,NULL),
 (675,675,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=AGE_ROCK_TYPE%3AGSC&STYLE=COLORMAP_GEN&FORMAT=image%2Fpng',NULL,NULL);
INSERT INTO `styledomainresource` (`SDRID`,`STYLEID`,`DOMAIN`,`URL`,`WIDTH`,`HEIGHT`) VALUES 
 (676,676,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=PRECAMBRIAN%3AGSC&STYLE=COLORMAP_PRE&FORMAT=image%2Fpng',NULL,NULL),
 (677,677,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=FELSIC_MAGMATIC%3AGSC&STYLE=COLORMAP_FLS&FORMAT=image%2Fpng',NULL,NULL),
 (678,678,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=FAULTS%3AGSC&STYLE=red&FORMAT=image%2Fpng',NULL,NULL),
 (679,679,'LegendURL','http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=simple&SERVICE=WMS&VERSION=1.1.2&REQUEST=GetLegendGraphic&LAYER=MAFIC_MAGMATIC%3AGSC&STYLE=COLORMAP_MAF&FORMAT=image%2Fpng',NULL,NULL);
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
  `TIMEOUT` varchar(45) default NULL,
  PRIMARY KEY  (`USERID`),
  KEY `FK_User_1` (`ORGANIZATIONID`),
  CONSTRAINT `FK_User_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 87040 kB; (`ORGANIZATIONID`) REFER `kaartenbali';

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`USERID`,`ORGANIZATIONID`,`FIRSTNAME`,`LASTNAME`,`EMAILADDRESS`,`USERNAME`,`PASSWORD`,`ROLE`,`PERSONALURL`,`TIMEOUT`) VALUES 
 (1,1,'Nando','de Goeij','degoeij@orange.nl','nando','nando','beheerder','http://localhost:8084/kaartenbalie/servlet/CallWMSServlet/98100ae84e47961542bc82c14f19aa4f',NULL),
 (4,2,'Danka','Cieskova','dcieskova@orange.nl','danka','danka','gebruiker',NULL,NULL),
 (5,2,'Martin','van der Valk','vandervalk@hotmail.com','martin','martin','gebruiker',NULL,NULL),
 (6,2,'Marinka','de Goeij','mdegoeij@orange.nl','marinka','marinka','gebruiker',NULL,NULL),
 (7,2,'Marc','Vloemans','marc@b3p.nl','marc','marc','gebruiker',NULL,NULL),
 (8,1,'Chris','van Lith','chris@b3p.nl','chris','chris','beheerder',NULL,NULL),
 (9,1,'Jeffrey','Lasut','jeffrey@b3p.nl','jeffrey','jeffrey','gebruiker',NULL,NULL),
 (10,7,'Klaas','Vaak','klaas@vaak.nl','klaas','klaas','gebruiker',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
