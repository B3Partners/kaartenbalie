DROP TABLE IF EXISTS "allowedServiceProviders";
CREATE TABLE "allowed_service_providers"
(
  id integer NOT NULL,
  name character varying(255) NOT NULL
)
WITH (
  OIDS=TRUE
);

DROP TABLE IF EXISTS "wfsAllowedServiceProviders";
CREATE TABLE "wfs_allowed_service_providers"
(
  id integer NOT NULL,
  name character varying(255) NOT NULL
)
WITH (
  OIDS=TRUE
);