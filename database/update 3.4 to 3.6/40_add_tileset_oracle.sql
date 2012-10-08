create table tile_set (
        id number(10,0) not null,
        bounding_box number(10,0),
        srs varchar2(50 char),
        resolutions clob,
        width number(10,0),
        height number(10,0),
        format varchar2(50 char),
        styles varchar2(255 char),
        service_provider number(10,0),
        primary key (id)
);

create table tile_set_layers (
        tileset number(10,0) not null,
        layer number(10,0) not null,
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
        
        
create sequence tile_set_id_seq;

alter table srs_bounding_box modify layer number(10,0) null
