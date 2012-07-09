DROP TABLE IF EXISTS "allowedServiceProviders";
CREATE TABLE "allowed_service_providers"
(
  id number primary key,
  name varchar2(255) NOT NULL
);

DROP TABLE IF EXISTS "wfsAllowedServiceProviders";
CREATE TABLE "wfs_allowed_service_providers"
(
  id number primary key,
  name varchar2(255) NOT NULL
);