ALTER TABLE `mon_clientrequest` ADD COLUMN `clr_method` VARCHAR(255) NOT NULL AFTER `clr_organizationId`,
 ADD COLUMN `clr_clientIp` VARCHAR(255) NOT NULL AFTER `clr_method`;
