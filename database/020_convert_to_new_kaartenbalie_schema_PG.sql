-- Voor als je nog een oude kaartenbalie schema hebt zoals Digitree had
-- Boy de Wit, 1 december 2010

-- Rename kolommen
ALTER TABLE operation RENAME COLUMN type TO soort;
ALTER TABLE organization RENAME COLUMN number TO streetnumber;
ALTER TABLE transaction RENAME COLUMN type TO soort;
ALTER TABLE _user_ips RENAME COLUMN _user TO users;
ALTER TABLE _user_roles RENAME COLUMN _user TO users;

-- Add kolommen
alter table service_provider_request add column message_sent text;
alter table service_provider_request add column message_received text;

-- Rename tabellen
ALTER TABLE "service_domain_resource_formats" rename to "service_domain_resource_fmts";
ALTER TABLE "_user" rename to "users";
ALTER TABLE "_user_ips" rename to "users_ips";
ALTER TABLE "_user_roles" rename to "users_roles";
ALTER TABLE "_user_orgs" rename to "users_orgs";

-- Renamen kolomen van vernieuwde tabellen
ALTER TABLE "users_orgs" RENAME COLUMN _user TO users;

-- Droppen NOT NULL
ALTER TABLE users ALTER COLUMN first_name DROP NOT NULL;
ALTER TABLE users ALTER COLUMN surname DROP NOT NULL;
ALTER TABLE users ALTER COLUMN email_address DROP NOT NULL;

-- Toevoegen main_organization kolom
-- Hoeft alleen als er geen organization kolom bestaat in users anders renamen
ALTER TABLE users ADD COLUMN main_organization int4;
