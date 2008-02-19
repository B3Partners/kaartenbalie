ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_minScale` DECIMAL(20,10) AFTER `lpr_operation`;
ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_maxScale` DECIMAL(20,10) AFTER `lpr_minScale`;
ALTER TABLE `acc_pricecomp` ADD COLUMN `prc_scale` DECIMAL(20,10) AFTER `prc_operation`;
