-- pas op hierdoor wordt oude metadata verwijderd!
--drop table layer_metadata;

create table layer_metadata (
     id serial not null,
     layer int4 not null,
     metadata text,
     primary key (id)
);

alter table layer_metadata
     add constraint FKBDAF425DB2D03DFA
     foreign key (layer)
     references layer;
