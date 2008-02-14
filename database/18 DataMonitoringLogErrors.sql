ALTER TABLE `mon_clientrequest` ADD COLUMN `clr_exceptionClass` VARCHAR(255) AFTER `clr_operation`,
 ADD COLUMN `clr_exceptionMessage` VARCHAR(4000) AFTER `clr_exceptionClass`;
 
ALTER TABLE `mon_serviceproviderrequest` ADD COLUMN `spr_exceptionClass` VARCHAR(255) AFTER `spr_serviceProviderId`,
 ADD COLUMN `spr_exceptionMessage` VARCHAR(4000) AFTER `spr_exceptionClass`;
 
 
