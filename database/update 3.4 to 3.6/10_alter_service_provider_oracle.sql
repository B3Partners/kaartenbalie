ALTER TABLE SERVICE_PROVIDER ADD (SLD_URL VARCHAR2(255));

ALTER TABLE SERVICE_PROVIDER ADD (ignore_resource NUMBER(1));

ALTER TABLE service_provider ADD (user_name varchar(4000) NULL, password varchar(4000) NULL);
ALTER TABLE wfs_service_provider ADD (user_name varchar(4000) NULL, password varchar(4000) NULL);

ALTER TABLE service_provider ADD allowed number(1) default 1;
ALTER TABLE wfs_service_provider ADD allowed number(1) default 1;