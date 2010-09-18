CREATE
    TABLE users_orgs
    (
        users INTEGER NOT NULL,
        organization INTEGER NOT NULL,
        PRIMARY KEY (users, organization),
        CONSTRAINT users_orgs__user_fkey FOREIGN KEY (users) REFERENCES users (id),
        CONSTRAINT users_orgs_organization_fkey FOREIGN KEY (organization) REFERENCES organization (id)
    );
    
INSERT
INTO
    users_orgs
    (
        users,
        organization
    )
SELECT DISTINCT
    users.id,
    organization.id
FROM
    users
INNER JOIN organization
ON
    (
        users.organization = organization.id
    ) ;
    
ALTER TABLE users RENAME COLUMN organization TO main_organization;


--ALTER TABLE users ADD COLUMN main_organization integer;
--ALTER TABLE users ALTER COLUMN main_organization SET NOT NULL;
--update users set main_organization = (select organization from users_orgs where users=users.id);