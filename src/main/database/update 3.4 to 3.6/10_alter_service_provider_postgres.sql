alter table SERVICE_PROVIDER add column sld_url varchar(255);

alter table SERVICE_PROVIDER add column ignore_resource boolean;

ALTER TABLE service_provider ADD COLUMN user_name varchar(4000) NULL;
ALTER TABLE service_provider ADD COLUMN password varchar(4000) NULL;

ALTER TABLE wfs_service_provider ADD COLUMN user_name varchar(4000) NULL;
ALTER TABLE wfs_service_provider ADD COLUMN password varchar(4000) NULL;

ALTER TABLE service_provider ADD COLUMN allowed boolean DEFAULT true;
ALTER TABLE wfs_service_provider ADD COLUMN allowed boolean DEFAULT true;