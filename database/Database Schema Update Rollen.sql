CREATE TABLE `kaartenbalie`.`roles` (
  `ROLEID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `ROLE` VARCHAR(45) NOT NULL,
  PRIMARY KEY(`ROLEID`)
)
ENGINE = InnoDB;

CREATE TABLE `kaartenbalie`.`userroles` (
  `USERID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `ROLEID` INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(`USERID`, `ROLEID`),
  CONSTRAINT `FK_userroles_1` FOREIGN KEY `FK_userroles_1` (`USERID`)
    REFERENCES `user` (`USERID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_userroles_2` FOREIGN KEY `FK_userroles_2` (`ROLEID`)
    REFERENCES `roles` (`ROLEID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
ENGINE = InnoDB;

INSERT INTO roles (ROLE) VALUES ('beheerder');
INSERT INTO roles (ROLE) VALUES ('organisatiebeheerder');
INSERT INTO roles (ROLE) VALUES ('themabeheerder');
INSERT INTO roles (ROLE) VALUES ('gebruiker');
INSERT INTO roles (ROLE) VALUES ('demogebruiker');

ALTER TABLE `kaartenbalie`.`user` DROP COLUMN `ROLE`;
