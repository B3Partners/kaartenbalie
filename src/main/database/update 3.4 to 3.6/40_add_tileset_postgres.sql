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
        
--zorg er voor dat boundingbox kan worden gebruikt in tileset
ALTER TABLE srs_bounding_box
ALTER COLUMN layer DROP NOT NULL;