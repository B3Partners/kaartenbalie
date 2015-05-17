create table layer_metadata (
        id number(10,0) not null,
        layer number(10,0) not null,
        metadata clob,
        primary key (id)
);

alter table layer_metadata
        add constraint FKBDAF425DB2D03DFA
        foreign key (layer) 
        references layer;

create sequence layer_metadata_id_seq;