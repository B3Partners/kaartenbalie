CREATE TABLE roles (
  ROLEID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ROLE VARCHAR(45) NOT NULL,
  PRIMARY KEY(ROLEID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


CREATE TABLE userroles (
  USERID INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ROLEID INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(USERID, ROLEID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

alter table userroles add CONSTRAINT FK_Userroles_1 FOREIGN KEY (USERID) REFERENCES user (USERID);
alter table userroles add CONSTRAINT FK_Userroles_2 FOREIGN KEY (ROLEID)REFERENCES roles (ROLEID);

INSERT INTO roles (ROLEID, ROLE) VALUES (1,'beheerder');
INSERT INTO roles (ROLEID, ROLE) VALUES (2,'organisatiebeheerder');
INSERT INTO roles (ROLEID, ROLE) VALUES (3,'themabeheerder');
INSERT INTO roles (ROLEID, ROLE) VALUES (4,'gebruiker');
INSERT INTO roles (ROLEID, ROLE) VALUES (5,'demogebruiker');

insert into userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  4
  from user
  where role='gebruiker';
  
insert into userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  1
  from user
  where role='beheerder';

insert into userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  5
  from user
  where role='demogebruiker';

insert into userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  2
  from user
  where role='organisatiebeheerder';

insert into userroles (
  USERID,
  ROLEID
	)
  select 
  USERID,
  3
  from user
  where role='themabeheerder';


ALTER TABLE user DROP COLUMN ROLE;

ALTER TABLE style DROP COLUMN STYLEURL;
ALTER TABLE style DROP COLUMN STYLESHEETURL;

