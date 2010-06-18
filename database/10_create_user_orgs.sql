CREATE
    TABLE _user_orgs
    (
        _user INTEGER NOT NULL,
        organization INTEGER NOT NULL,
        type varchar(255),
        PRIMARY KEY (_user, organization),
        CONSTRAINT _user_orgs__user_fkey FOREIGN KEY (_user) REFERENCES _user (id),
        CONSTRAINT _user_orgs_organization_fkey FOREIGN KEY (organization) REFERENCES organization (id)
    );
    
INSERT
INTO
    _user_orgs
    (
        _user,
        organization,
        type
    )
SELECT DISTINCT
    _user.id,
    organization.id,
    'main'
FROM
    _user
INNER JOIN organization
ON
    (
        _user.organization = organization.id
    ) ;
    
ALTER TABLE _user DROP COLUMN organization;


