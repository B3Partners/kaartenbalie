-- pg
--ALTER TABLE users ALTER COLUMN first_name DROP NOT NULL;
--ALTER TABLE users ALTER COLUMN surname DROP NOT NULL;
--ALTER TABLE users ALTER COLUMN email_address DROP NOT NULL;

--oracle
ALTER TABLE USERS MODIFY (FIRST_NAME NULL);
ALTER TABLE USERS MODIFY (SURNAME NULL);
ALTER TABLE USERS MODIFY (EMAIL_ADDRESS NULL);