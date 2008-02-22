ALTER TABLE `acc_layerpricing` ADD COLUMN `lpr_projection` VARCHAR(255) AFTER `lpr_maxScale`;
ALTER TABLE `acc_pricecomp` ADD COLUMN `prc_projection` VARCHAR(255) AFTER `prc_scale`;
