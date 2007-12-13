DROP TABLE IF EXISTS `acc_layerpricing`;
CREATE TABLE `acc_layerpricing` (
  `lpr_id` int(11) NOT NULL auto_increment,
  `lpr_LAYERID` int(11) default NULL,
  `lpr_planType` int(11) default NULL,
  `lpr_validFrom` datetime default NULL,
  `lpr_validUntil` datetime default NULL,
  `lpr_creationDate` datetime default NULL,
  `lpr_unitPrice` decimal(9,2) default NULL,
  PRIMARY KEY  (`lpr_id`),
  KEY `FKE251E853EDD63704` (`lpr_LAYERID`),
  CONSTRAINT `FKE251E853EDD63704` FOREIGN KEY (`lpr_LAYERID`) REFERENCES `layer` (`LAYERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;