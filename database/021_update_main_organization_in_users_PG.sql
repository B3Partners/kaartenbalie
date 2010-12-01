-- Updaten main_organization met id van organizations uit users_orgs
update users set main_organization = (
SELECT organization FROM users_orgs as uo WHERE
users.id = uo.users AND uo.type = 'main');

-- Drop type kolom uit users_orgs
ALTER TABLE users_orgs DROP COLUMN "type";