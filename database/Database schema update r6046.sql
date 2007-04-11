-- In r6046 kolom user.registeredIP toegevoegd

ALTER TABLE `user` ADD COLUMN `IPADDRESS` VARCHAR(50) AFTER `DEFAULTGETMAP`;