select setval('client_request_id_seq', (select max(id) from client_request));
select setval('dimensions_id_seq', (select max(id) from dimensions));
select setval('identifier_id_seq', (select max(id) from identifier));
select setval('layer_domain_resource_id_seq', (select max(id) from layer_domain_resource));
select setval('layer_id_seq', (select max(id) from layer));
select setval('layer_price_composition_id_seq', (select max(id) from layer_price_composition));
select setval('layer_pricing_id_seq', (select max(id) from layer_pricing));
select setval('operation_id_seq', (select max(id) from operation));
select setval('organization_id_seq', (select max(id) from organization));
select setval('report_id_seq', (select max(id) from report));
select setval('roles_id_seq', (select max(id) from roles));
select setval('service_domain_resource_id_seq', (select max(id) from service_domain_resource));
select setval('service_provider_id_seq', (select max(id) from service_provider));
select setval('service_provider_request_id_seq', (select max(id) from service_provider_request));
select setval('srs_bounding_box_id_seq', (select max(id) from srs_bounding_box));
select setval('style_domain_resource_id_seq', (select max(id) from style_domain_resource));
select setval('style_id_seq', (select max(id) from style));
select setval('transaction_id_seq', (select max(id) from transaction));
select setval('_user_id_seq', (select max(id) from _user));
select setval('wfs_layer_id_seq', (select max(id) from wfs_layer));
select setval('wfs_service_provider_id_seq', (select max(id) from wfs_service_provider));