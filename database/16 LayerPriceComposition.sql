DROP TABLE IF EXISTS `acc_pricecomp`;
CREATE TABLE  `acc_pricecomp` (
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
  PRIMARY KEY  (`prc_id`),
  KEY `FK165D199A1C0BBA79` (`prc_tra_id`),
  CONSTRAINT `FK165D199A1C0BBA79` FOREIGN KEY (`prc_tra_id`) REFERENCES `acc_transaction` (`tra_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_service` VARCHAR(255) AFTER `lpr_indexCount`,
 ADD COLUMN `lpr_operation` VARCHAR(255) AFTER `lpr_service`;
