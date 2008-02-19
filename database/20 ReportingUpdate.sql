ALTER TABLE `rep_status` MODIFY COLUMN `sta_statusMessage` VARCHAR(4000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

DROP TABLE IF EXISTS `rep_table`;
CREATE TABLE  `rep_table` (
  `tab_id` int(11) NOT NULL auto_increment,
  `tab_rep_id` int(11) default NULL,
  `rep_tableName` varchar(255) default NULL,
  PRIMARY KEY  (`tab_id`),
  KEY `FKCFB6ADACFD6ECBDE` (`tab_rep_id`),
  CONSTRAINT `FKCFB6ADACFD6ECBDE` FOREIGN KEY (`tab_rep_id`) REFERENCES `rep_report` (`rep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `rep_tablerow`;
CREATE TABLE  `rep_tablerow` (
  `tro_id` int(11) NOT NULL auto_increment,
  `tro_tab_id` int(11) default NULL,
  `tro_header` bit(1) default NULL,
  `tro_rowOrder` int(11) default NULL,
  PRIMARY KEY  (`tro_id`),
  KEY `FKDB7E1CAEEDED1520` (`tro_tab_id`),
  CONSTRAINT `FKDB7E1CAEEDED1520` FOREIGN KEY (`tro_tab_id`) REFERENCES `rep_table` (`tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `rep_rowvalue`;
CREATE TABLE  `rep_rowvalue` (
  `rva_id` int(11) NOT NULL auto_increment,
  `rva_tro_id` int(11) default NULL,
  `rva_rowValue` varchar(255) default NULL,
  `rva_valueOrder` int(11) default NULL,
  PRIMARY KEY  (`rva_id`),
  KEY `FK14B3C939A32715CE` (`rva_tro_id`),
  CONSTRAINT `FK14B3C939A32715CE` FOREIGN KEY (`rva_tro_id`) REFERENCES `rep_tablerow` (`tro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;