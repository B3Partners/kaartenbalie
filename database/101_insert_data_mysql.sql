insert into organization (id, name, has_valid_get_capabilities, allow_accounting_layers) 
	values (1, 'Beheerders', false, false);

insert into account (id, credit_balance)
	values (1, 0.0);
	
insert into roles (id, `role`) values (1, 'beheerder');
insert into roles (id, `role`) values (2, 'gebruiker');

insert into users (id, main_organization, first_name, surname, email_address, username, `password`, personalurl) 
	values (1, 1, 'beheerder', 'beheerder', 'info@b3partners.nl', 'beheerder', 'JMzUf6QkCdc%3D', '6c3916be182da4c736091f1f73c590b7');

insert into users_roles (users, `role`) values (1,1);
insert into users_roles (users, `role`) values (1,2);

insert into users_orgs (organization, users) values (1,1);

insert into users_ips (users, ipaddress) values (1, '0.0.0.0');