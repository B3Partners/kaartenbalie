
insert into organization (id, name, has_valid_get_capabilities, allow_accounting_layers) 
	values (1, 'Beheerders', false, false);
	
select setval('organization_id_seq', (select max(id) from organization));

insert into account (id, credit_balance)
	values (1, 0.0);
	
insert into roles (id, "role") values (1, 'beheerder');
insert into roles (id, "role") values (2, 'organisatiebeheerder');
insert into roles (id, "role") values (3, 'themabeheerder');
insert into roles (id, "role") values (4, 'gebruiker');
insert into roles (id, "role") values (5, 'demogebruiker');
    
select setval('roles_id_seq', (select max(id) from roles));

insert into _user (id, organization, first_name, surname, email_address, username, "password") 
	values (1, 1, 'beheerder', 'beheerder', 'info@b3partners.nl', 'beheerder', 'JMzUf6QkCdc%3D');

select setval('_user_id_seq', (select max(id) from _user));

insert into _user_roles (_user, "role") values (1,1);
insert into _user_roles (_user, "role") values (1,2);
insert into _user_roles (_user, "role") values (1,3);
insert into _user_roles (_user, "role") values (1,4);