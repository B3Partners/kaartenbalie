DROP TABLE IF EXISTS `kaartenbalie`.`mon_clientrequest`;
CREATE TABLE  `kaartenbalie`.`mon_clientrequest` (
  `clr_id` int(11) NOT NULL auto_increment,
  `clr_clientRequestURI` varchar(4000) default NULL,
  `clr_timeStamp` datetime default NULL,
  `clr_userId` int(11) default NULL,
  PRIMARY KEY  (`clr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `kaartenbalie`.`mon_requestoperation`;
CREATE TABLE  `kaartenbalie`.`mon_requestoperation` (
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

DROP TABLE IF EXISTS `kaartenbalie`.`mon_serviceproviderrequest`;
CREATE TABLE  `kaartenbalie`.`mon_serviceproviderrequest` (
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
  PRIMARY KEY  (`spr_id`),
  KEY `FK712CEEBC223516A7` (`spr_clr_id`),
  CONSTRAINT `FK712CEEBC223516A7` FOREIGN KEY (`spr_clr_id`) REFERENCES `mon_clientrequest` (`clr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;