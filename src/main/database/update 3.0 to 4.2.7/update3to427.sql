
alter table attribution ALTER COLUMN title TYPE varchar(255);

create table layer_metadata (
     id serial,
     layer int4 not null,
     metadata text,
     primary key (id)
);



--create sequence layer_metadata_id_seq;

alter table service_provider ALTER COLUMN abbr TYPE varchar(60);
alter table service_provider ALTER COLUMN abbr DROP not null;
alter table service_provider add column status varchar(80);
alter table service_provider add column sld_url varchar(255);
ALTER TABLE service_provider ADD COLUMN user_name varchar(4000) NULL;
ALTER TABLE service_provider ADD COLUMN password varchar(4000) NULL;
alter table service_provider add column ignore_resource boolean;
ALTER TABLE service_provider ADD COLUMN allowed boolean DEFAULT true;

--zorg er voor dat boundingbox kan worden gebruikt in tileset
ALTER TABLE srs_bounding_box
ALTER COLUMN layer DROP NOT NULL;

alter table style add column sld_part text;
alter table style add column sld_constraints text;

create table tile_set (
        id  serial not null,
        bounding_box int4,
        srs varchar(50),
        resolutions text,
        width int4,
        height int4,
        format varchar(50),
        styles varchar(255),
        service_provider int4,
        primary key (id)
);

create table tile_set_layers (
	tileset int4 not null,
	layer int4 not null,
	primary key (tileset, layer)
);

alter table users add column last_login_status varchar(255);
ALTER TABLE users DROP CONSTRAINT fk6a68e08c51d1b84;	
alter table users alter column main_organization drop not null;

alter table users_orgs add column soort varchar(255);
        
alter table wfs_service_provider ALTER COLUMN name TYPE varchar(80);
alter table wfs_service_provider ALTER COLUMN name DROP not null;
alter table wfs_service_provider ALTER COLUMN abbr TYPE varchar(80);
alter table wfs_service_provider ALTER COLUMN abbr DROP not null;
alter table wfs_service_provider ALTER COLUMN title TYPE varchar(255);

ALTER TABLE wfs_service_provider ADD COLUMN status varchar(80);
ALTER TABLE wfs_service_provider ADD COLUMN user_name varchar(4000);
ALTER TABLE wfs_service_provider ADD COLUMN password varchar(4000);
ALTER TABLE wfs_service_provider ADD COLUMN allowed boolean DEFAULT true;

alter table layer_metadata
     add constraint FKBDAF425DB2D03DFA
     foreign key (layer)
     references layer;

alter table tile_set 
        add constraint FK827E375177621D59 
        foreign key (service_provider) 
        references service_provider;
        
alter table tile_set 
        add constraint FK827E3751FEAFC74B 
        foreign key (bounding_box) 
        references srs_bounding_box;

alter table tile_set_layers 
        add constraint FKF1EFEDF0B2D03DFA 
        foreign key (layer) 
        references layer;

alter table tile_set_layers 
        add constraint FKF1EFEDF0A7FB2B20 
        foreign key (tileset) 
        references tile_set;

			
	