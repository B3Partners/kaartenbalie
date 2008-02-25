ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_deletionDate` DATETIME AFTER `lpr_unitPrice`;
ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_indexCount` INTEGER AFTER `lpr_deletionDate`;


DROP TABLE IF EXISTS `kb_uniquenumber`;
CREATE TABLE  `kb_uniquenumber` (
  `unn_id` int(11) NOT NULL auto_increment,
  `unn_indexName` varchar(255) default NULL,
  `unn_indexCount` int(11) default NULL,
  PRIMARY KEY  (`unn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;