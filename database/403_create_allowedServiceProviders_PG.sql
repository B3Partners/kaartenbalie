CREATE TABLE "allowedServiceProviders"
(
  id integer NOT NULL,
  name character varying(255) NOT NULL
)
WITH (
  OIDS=TRUE
);

CREATE TABLE "wfsAllowedServiceProviders"
(
  id integer NOT NULL,
  name character varying(255) NOT NULL
)
WITH (
  OIDS=TRUE
);