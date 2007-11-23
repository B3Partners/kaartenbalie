DROP TABLE IF EXISTS `kaartenbalie`.`rep_dailyusage`;
DROP TABLE IF EXISTS `kaartenbalie`.`rep_usagedetails`;
DROP TABLE IF EXISTS `kaartenbalie`.`rep_users`;
DROP TABLE IF EXISTS `kaartenbalie`.`rep_reportdata`;
DROP TABLE IF EXISTS `kaartenbalie`.`rep_status`;
DROP TABLE IF EXISTS `kaartenbalie`.`rep_report`;

CREATE TABLE  `kaartenbalie`.`rep_report` (
  `rep_id` int(11) NOT NULL auto_increment,
  `rep_discriminator` varchar(255) NOT NULL,
  `rep_reportDate` datetime default NULL,
  `rep_processingTime` bigint(20) default NULL,
  `rep_startDate` datetime default NULL,
  `rep_endDate` datetime default NULL,
  PRIMARY KEY  (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE  `kaartenbalie`.`rep_status` (
  `sta_id` int(11) NOT NULL auto_increment,
  `sta_reportId` int(11) default NULL,
  `sta_creationDate` datetime default NULL,
  `sta_state` int(11) default NULL,
  `sta_statusMessage` varchar(255) default NULL,
  PRIMARY KEY  (`sta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE  `kaartenbalie`.`rep_users` (
  `rpd_usr_id` int(11) NOT NULL,
  `usr_id` int(11) NOT NULL,
  PRIMARY KEY  (`rpd_usr_id`,`usr_id`),
  KEY `FKCFCCFFE6C53CB6F` (`rpd_usr_id`),
  CONSTRAINT `FKCFCCFFE6C53CB6F` FOREIGN KEY (`rpd_usr_id`) REFERENCES `rep_report` (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `kaartenbalie`.`rep_reportdata` (
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

CREATE TABLE  `kaartenbalie`.`rep_usagedetails` (
  `usd_id` int(11) NOT NULL auto_increment,
  `usd_rpd_id` int(11) default NULL,
  `usd_userId` int(11) default NULL,
  PRIMARY KEY  (`usd_id`),
  KEY `FK312665E3F5CA8D83` (`usd_rpd_id`),
  CONSTRAINT `FK312665E3F5CA8D83` FOREIGN KEY (`usd_rpd_id`) REFERENCES `rep_reportdata` (`rpd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  `kaartenbalie`.`rep_dailyusage` (
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