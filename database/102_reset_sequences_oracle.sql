
select max(id) from client_request;
select max(id) from dimensions;
select max(id) from identifier;
select max(id) from layer_domain_resource;
select max(id) from layer;
select max(id) from layer_price_composition;
select max(id) from layer_pricing;
select max(id) from operation;
select max(id) from organization;
select max(id) from report;
select max(id) from roles;
select max(id) from service_domain_resource;
select max(id) from service_provider;
select max(id) from service_provider_request;
select max(id) from srs_bounding_box;
select max(id) from style_domain_resource;
select max(id) from style;
select max(id) from transaction;
select max(id) from users;
select max(id) from wfs_layer;
select max(id) from wfs_service_provider;

    drop sequence client_request_id_seq;

    drop sequence dimensions_id_seq;

    drop sequence identifier_id_seq;

    drop sequence layer_domain_resource_id_seq;

    drop sequence layer_id_seq;

    drop sequence layer_price_composition_id_seq;

    drop sequence layer_pricing_id_seq;

    drop sequence operation_id_seq;

    drop sequence organization_id_seq;

    drop sequence report_id_seq;

    drop sequence roles_id_seq;

    drop sequence service_domain_resource_id_seq;

    drop sequence service_provider_id_seq;

    drop sequence service_provider_req_id_seq;

    drop sequence srs_bounding_box_id_seq;

    drop sequence style_domain_resource_id_seq;

    drop sequence style_id_seq;

    drop sequence transaction_id_seq;

    drop sequence users_id_seq;

    drop sequence wfs_layer_id_seq;

    drop sequence wfs_service_provider_id_seq;


    create sequence client_request_id_seq INCREMENT BY 1 START WITH 1;

    create sequence dimensions_id_seq INCREMENT BY 1 START WITH 1;

    create sequence identifier_id_seq INCREMENT BY 1 START WITH 1;

    create sequence layer_domain_resource_id_seq INCREMENT BY 1 START WITH 1082;

    create sequence layer_id_seq INCREMENT BY 1 START WITH 4812;

    create sequence layer_price_composition_id_seq INCREMENT BY 1 START WITH 1;

    create sequence layer_pricing_id_seq INCREMENT BY 1 START WITH 1;

    create sequence operation_id_seq INCREMENT BY 1 START WITH 1;

    create sequence organization_id_seq INCREMENT BY 1 START WITH 266;

    create sequence report_id_seq INCREMENT BY 1 START WITH 14;

    create sequence roles_id_seq INCREMENT BY 1 START WITH 6;

    create sequence service_domain_resource_id_seq INCREMENT BY 1 START WITH 1251;

    create sequence service_provider_id_seq INCREMENT BY 1 START WITH 220;

    create sequence service_provider_req_id_seq INCREMENT BY 1 START WITH 1;

    create sequence srs_bounding_box_id_seq INCREMENT BY 1 START WITH 25034;

    create sequence style_domain_resource_id_seq INCREMENT BY 1 START WITH 3176;

    create sequence style_id_seq INCREMENT BY 1 START WITH 3185;

    create sequence transaction_id_seq INCREMENT BY 1 START WITH 1;

    create sequence users_id_seq INCREMENT BY 1 START WITH 244;

    create sequence wfs_layer_id_seq INCREMENT BY 1 START WITH 478;

    create sequence wfs_service_provider_id_seq INCREMENT BY 1 START WITH 29;
