create table layer_metadata (
     id int4 not null,
     layer int4 not null,
     metadata text,
     primary key (id)
);

alter table layer_metadata
     add constraint FKBDAF425DB2D03DFA
     foreign key (layer)
     references layer;

create sequence layer_metadata_id_seq;