DROP TABLE IF EXISTS `acc_layerpricing`;
CREATE TABLE  `acc_layerpricing` (
  `lpr_id` int(11) NOT NULL auto_increment,
  `lpr_layerName` varchar(255) default NULL,
  `lpr_serverProviderPrefix` varchar(255) default NULL,
  `lpr_planType` int(11) default NULL,
  `lpr_validFrom` datetime default NULL,
  `lpr_validUntil` datetime default NULL,
  `lpr_creationDate` datetime default NULL,
  `lpr_layerIsFree` bit(1) default NULL,
  `lpr_unitPrice` decimal(9,2) default NULL,
  PRIMARY KEY  (`lpr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `acc_transaction`;
DROP TABLE IF EXISTS `acc_account`;

CREATE TABLE  `acc_account` (
  `acc_id` int(11) NOT NULL,
  `acc_creditBalance` decimal(15,2) default NULL,
  PRIMARY KEY  (`acc_id`),
  KEY `FK7FE8986FEE7C9324` (`acc_id`),
  CONSTRAINT `FK7FE8986FEE7C9324` FOREIGN KEY (`acc_id`) REFERENCES `organization` (`ORGANIZATIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE  `acc_transaction` (
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



