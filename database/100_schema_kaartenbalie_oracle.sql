
    create table account (
        id number(10,0) not null,
        credit_balance number(15,2),
        primary key (id)
    );

    comment on table account is
        'Accounting module: balans en transacties van een organisatie';

    create table attribution (
        id number(10,0) not null,
        title varchar2(50 char),
        attribution_url varchar2(4000 char),
        logo_url varchar2(4000 char),
        logo_width varchar2(50 char),
        logo_height varchar2(50 char),
        primary key (id)
    );

    create table client_request (
        id number(10,0) not null,
        client_request_uri varchar2(4000 char),
        timestamp timestamp,
        user_id number(10,0),
        method varchar2(255 char),
        client_ip varchar2(255 char),
        service varchar2(255 char),
        operation varchar2(255 char),
        organization_id number(10,0),
        exception_class varchar2(255 char),
        exception_message varchar2(4000 char),
        primary key (id)
    );

    comment on table client_request is
        'Monitoring module';

    create table contact_information (
        id number(10,0) not null,
        contact_person varchar2(80 char),
        contact_position varchar2(255 char),
        address varchar2(80 char),
        address_type varchar2(80 char),
        postcode varchar2(50 char),
        city varchar2(80 char),
        state_or_province varchar2(80 char),
        country varchar2(255 char),
        voice_telephone varchar2(80 char),
        fascimile_telephone varchar2(80 char),
        email_address varchar2(255 char),
        primary key (id)
    );

    create table dimensions (
        id number(10,0) not null,
        layer number(10,0) not null,
        dimensions_name varchar2(50 char),
        dimensions_unit varchar2(50 char),
        dimensions_unit_symbol varchar2(50 char),
        extent_name varchar2(50 char),
        extent_defaults varchar2(50 char),
        extent_nearest_value varchar2(50 char),
        extent_multiple_values varchar2(50 char),
        extent_current varchar2(50 char),
        primary key (id)
    );

    create table identifier (
        id number(10,0) not null,
        layer number(10,0) not null,
        authority_name varchar2(50 char) not null,
        authority_url varchar2(50 char) not null,
        primary key (id)
    );

    create table layer (
        id number(10,0) not null,
        service_provider number(10,0) not null,
        parent number(10,0),
        name varchar2(200 char),
        title varchar2(255 char) not null,
        abstracts clob,
        queryable varchar2(50 char),
        cascaded varchar2(50 char),
        opaque varchar2(50 char),
        nosubsets varchar2(50 char),
        fixed_width varchar2(50 char),
        fixed_height varchar2(50 char),
        scale_hint_min varchar2(50 char),
        scale_hint_max varchar2(50 char),
        metadata clob,
        primary key (id)
    );

    create table layer_domain_resource (
        id number(10,0) not null,
        layer number(10,0) not null,
        domain varchar2(50 char) not null,
        url varchar2(4000 char) not null,
        primary key (id)
    );

    create table layer_domain_resource_formats (
        layer_domain_resource number(10,0) not null,
        format varchar2(100 char) not null,
        primary key (layer_domain_resource, format)
    );

    create table layer_keyword_list (
        layer number(10,0) not null,
        keyword varchar2(50 char) not null,
        primary key (layer, keyword)
    );

    create table layer_price_composition (
        id number(10,0) not null,
        transaction number(10,0),
        server_provider_prefix varchar2(255 char),
        layer_name varchar2(255 char),
        calculation_date timestamp,
        plan_type number(10,0),
        service varchar2(255 char),
        operation varchar2(255 char),
        units number(5,2),
        scale number(20,2),
        projection varchar2(255 char),
        layer_is_free number(1,0),
        method number(10,0),
        calculation_time number(19,0),
        layer_price number(12,2),
        primary key (id)
    );

    comment on table layer_price_composition is
        'Accounting module: geeft opbouw van de prijs van een enkele layer uit een transactie weer';

    create table layer_pricing (
        id number(10,0) not null,
        layer_name varchar2(255 char),
        server_provider_prefix varchar2(255 char),
        plan_type number(10,0),
        valid_from timestamp,
        valid_until timestamp,
        creation_date timestamp,
        deletion_date timestamp,
        layer_is_free number(1,0),
        unit_price number(9,2),
        service varchar2(255 char),
        operation varchar2(255 char),
        min_scale number(20,10),
        max_scale number(20,10),
        projection varchar2(255 char),
        primary key (id)
    );

    comment on table layer_pricing is
        'Accounting module: ingestelde prijs van een layer';

    create table operation (
        id number(10,0) not null,
        client_request number(10,0),
        soort number(10,0),
        duration number(19,0),
        ms_since_request_start number(19,0),
        number_of_images number(10,0),
        data_size number(19,0),
        bytes_received_from_user number(10,0),
        bytes_sent_to_user number(10,0),
        primary key (id)
    );

    comment on table operation is
        'Monitoring module';

    create table organization (
        id number(10,0) not null,
        name varchar2(50 char) not null,
        street varchar2(50 char),
        streetnumber varchar2(5 char),
        addition varchar2(10 char),
        postalcode varchar2(45 char),
        province varchar2(50 char),
        country varchar2(50 char),
        postbox varchar2(50 char),
        billing_address varchar2(50 char),
        visitors_address varchar2(50 char),
        telephone varchar2(50 char),
        fax varchar2(50 char),
        has_valid_get_capabilities number(1,0) not null,
        bbox varchar2(50 char),
        code varchar2(50 char),
        allow_accounting_layers number(1,0) not null,
        primary key (id)
    );

    create table organization_layers (
        organization number(10,0) not null,
        layer number(10,0) not null,
        primary key (organization, layer)
    );

    create table organization_wfs_layers (
        organization number(10,0) not null,
        layer number(10,0) not null,
        primary key (organization, layer)
    );

    create table report (
        id number(10,0) not null,
        organization number(10,0),
        report_date timestamp,
        processing_time number(19,0),
        start_date timestamp,
        end_date timestamp,
        report_xml clob,
        name varchar2(255 char),
        report_mime varchar2(255 char),
        primary key (id)
    );

    comment on table report is
        'Reporting module';

    create table roles (
        id number(10,0) not null,
        role varchar2(45 char) not null,
        primary key (id)
    );

    create table service_domain_resource (
        id number(10,0) not null,
        service_provider number(10,0) not null,
        domain varchar2(50 char) not null,
        get_url varchar2(4000 char),
        post_url varchar2(4000 char),
        primary key (id)
    );

    create table service_domain_resource_fmts (
        service_domain_resource number(10,0) not null,
        format varchar2(100 char) not null,
        primary key (service_domain_resource, format)
    );

    create table service_provider (
        id number(10,0) not null,
        name varchar2(60 char) not null,
        abbr varchar2(50 char) not null,
        title varchar2(255 char) not null,
        abstracts clob,
        fees clob,
        access_constraints clob,
        given_name varchar2(50 char) not null,
        url varchar2(4000 char) not null,
        updated_date timestamp not null,
        wms_version varchar2(50 char) not null,
        primary key (id)
    );

    create table service_provider_exceptions (
        service_provider number(10,0) not null,
        format varchar2(50 char) not null,
        primary key (service_provider, format)
    );

    create table service_provider_keyword_list (
        service_provider number(10,0) not null,
        keyword varchar2(50 char) not null,
        primary key (service_provider, keyword)
    );

    create table service_provider_request (
        id number(10,0) not null,
        client_request number(10,0),
        bytes_sent number(19,0),
        bytes_received number(19,0),
        response_status number(10,0),
        message_sent clob,
        message_received clob,
        provider_request_uri varchar2(4000 char),
        request_response_time number(19,0),
        ms_since_request_start number(19,0),
        exception_class varchar2(255 char),
        exception_message varchar2(4000 char),
        wms_version varchar2(255 char),
        service_provider_id number(10,0),
        srs varchar2(255 char),
        width number(10,0),
        height number(10,0),
        format varchar2(255 char),
        primary key (id)
    );

    comment on table service_provider_request is
        'Monitoring module';

    create table srs_bounding_box (
        id number(10,0) not null,
        layer number(10,0) not null,
        srs varchar2(150 char),
        minx varchar2(50 char),
        maxx varchar2(50 char),
        miny varchar2(50 char),
        maxy varchar2(50 char),
        resx varchar2(50 char),
        resy varchar2(50 char),
        primary key (id)
    );

    create table style (
        id number(10,0) not null,
        layer number(10,0) not null,
        name varchar2(255 char) not null,
        title varchar2(255 char) not null,
        abstracts clob,
        primary key (id)
    );

    create table style_domain_resource (
        id number(10,0) not null,
        style number(10,0) not null,
        domain varchar2(45 char) not null,
        url varchar2(4000 char) not null,
        width varchar2(45 char),
        height varchar2(45 char),
        primary key (id)
    );

    create table style_domain_resource_formats (
        style_domain_resource number(10,0) not null,
        format varchar2(45 char) not null,
        primary key (style_domain_resource, format)
    );

    create table transaction (
        id number(10,0) not null,
        credit_alteration number(12,2),
        transaction_date timestamp,
        mutation_date timestamp,
        status number(10,0),
        soort number(10,0),
        error_message varchar2(255 char),
        user_id number(10,0),
        description varchar2(32 char),
        account number(10,0),
        billing_amount number(10,2),
        tx_exchange_rate number(10,0),
        primary key (id)
    );

    comment on table transaction is
        'Accounting module';

    create table users (
        id number(10,0) not null,
        main_organization number(10,0) not null,
        first_name varchar2(50 char),
        surname varchar2(50 char),
        email_address varchar2(50 char),
        username varchar2(50 char) not null,
        password varchar2(50 char) not null,
        personalurl varchar2(4000 char),
        timeout timestamp,
        default_get_map varchar2(4000 char),
        primary key (id)
    );

    create table users_ips (
        users number(10,0) not null,
        ipaddress varchar2(45 char) not null,
        primary key (users, ipaddress)
    );

    create table users_orgs (
        organization number(10,0) not null,
        users number(10,0) not null,
        primary key (users, organization)
    );

    create table users_roles (
        users number(10,0) not null,
        role number(10,0) not null,
        primary key (users, role)
    );

    create table wfs_layer (
        id number(10,0) not null,
        wfs_service_provider number(10,0) not null,
        name varchar2(200 char),
        title varchar2(255 char) not null,
        metadata clob,
        primary key (id)
    );

    create table wfs_service_provider (
        id number(10,0) not null,
        name varchar2(80 char) not null,
        abbr varchar2(50 char) not null,
        title varchar2(255 char),
        given_name varchar2(50 char) not null,
        url varchar2(4000 char) not null,
        updated_date timestamp not null,
        wfs_version varchar2(50 char) not null,
        primary key (id)
    );

    alter table account 
        add constraint FKB9D38A2D435502A6 
        foreign key (id) 
        references organization;

    alter table attribution 
        add constraint FKED87907FACB075C4 
        foreign key (id) 
        references layer;

    alter table contact_information 
        add constraint FK6BC4C78D4250FE39 
        foreign key (id) 
        references service_provider;

    alter table dimensions 
        add constraint FK18B23FCDB2D03DFA 
        foreign key (layer) 
        references layer;

    alter table identifier 
        add constraint FK9F88ACA9B2D03DFA 
        foreign key (layer) 
        references layer;

    alter table layer 
        add constraint FK61FD55177621D59 
        foreign key (service_provider) 
        references service_provider;

    alter table layer 
        add constraint FK61FD551715B7153 
        foreign key (parent) 
        references layer;

    alter table layer_domain_resource 
        add constraint FKF41CA63BB2D03DFA 
        foreign key (layer) 
        references layer;

    alter table layer_domain_resource_formats 
        add constraint FK80AE95813E1F836 
        foreign key (layer_domain_resource) 
        references layer_domain_resource;

    alter table layer_keyword_list 
        add constraint FK480A1562B2D03DFA 
        foreign key (layer) 
        references layer;

    alter table layer_price_composition 
        add constraint FKD866E046FF0042D2 
        foreign key (transaction) 
        references transaction;

    alter table operation 
        add constraint FK631AD567B2136F51 
        foreign key (client_request) 
        references client_request;

    alter table organization_layers 
        add constraint FK6CED35CE8999E2BE 
        foreign key (organization) 
        references organization;

    alter table organization_layers 
        add constraint FK6CED35CEB2D03DFA 
        foreign key (layer) 
        references layer;

    alter table organization_wfs_layers 
        add constraint FKD17104E98999E2BE 
        foreign key (organization) 
        references organization;

    alter table organization_wfs_layers 
        add constraint FKD17104E9CEDB4E46 
        foreign key (layer) 
        references wfs_layer;

    alter table report 
        add constraint FKC84C55348999E2BE 
        foreign key (organization) 
        references organization;

    alter table service_domain_resource 
        add constraint FK2A994F9F77621D59 
        foreign key (service_provider) 
        references service_provider;

    alter table service_domain_resource_fmts 
        add constraint FK709D5A26E96D5DFE 
        foreign key (service_domain_resource) 
        references service_domain_resource;

    alter table service_provider_exceptions 
        add constraint FKE735008877621D59 
        foreign key (service_provider) 
        references service_provider;

    alter table service_provider_keyword_list 
        add constraint FK9F2ECAB877621D59 
        foreign key (service_provider) 
        references service_provider;

    alter table service_provider_request 
        add constraint FK50C95A8BB2136F51 
        foreign key (client_request) 
        references client_request;

    alter table srs_bounding_box 
        add constraint FK3591391BB2D03DFA 
        foreign key (layer) 
        references layer;

    alter table style 
        add constraint FK68B1DB1B2D03DFA 
        foreign key (layer) 
        references layer;

    alter table style_domain_resource 
        add constraint FKC4AB2E9BB3A6CEBA 
        foreign key (style) 
        references style;

    alter table style_domain_resource_formats 
        add constraint FK5F3B11B8B5C5A0F6 
        foreign key (style_domain_resource) 
        references style_domain_resource;

    alter table transaction 
        add constraint FK7FA0D2DE7E3BAC70 
        foreign key (account) 
        references account;

    alter table users 
        add constraint FK6A68E08C51D1B84 
        foreign key (main_organization) 
        references organization;

    alter table users_ips 
        add constraint FK154D1175A4475A2B 
        foreign key (users) 
        references users;

    alter table users_orgs 
        add constraint FK9457DDE6A4475A2B 
        foreign key (users) 
        references users;

    alter table users_orgs 
        add constraint FK9457DDE68999E2BE 
        foreign key (organization) 
        references organization;

    alter table users_roles 
        add constraint FKF6CCD9C6A4475A2B 
        foreign key (users) 
        references users;

    alter table users_roles 
        add constraint FKF6CCD9C6AD40A28B 
        foreign key (role) 
        references roles;

    alter table wfs_layer 
        add constraint FKBB3050D69B6DCE00 
        foreign key (wfs_service_provider) 
        references wfs_service_provider;

    create sequence client_request_id_seq;

    create sequence dimensions_id_seq;

    create sequence identifier_id_seq;

    create sequence layer_domain_resource_id_seq;

    create sequence layer_id_seq;

    create sequence layer_price_composition_id_seq;

    create sequence layer_pricing_id_seq;

    create sequence operation_id_seq;

    create sequence organization_id_seq;

    create sequence report_id_seq;

    create sequence roles_id_seq;

    create sequence service_domain_resource_id_seq;

    create sequence service_provider_id_seq;

    create sequence service_provider_req_id_seq;

    create sequence srs_bounding_box_id_seq;

    create sequence style_domain_resource_id_seq;

    create sequence style_id_seq;

    create sequence transaction_id_seq;

    create sequence users_id_seq;

    create sequence wfs_layer_id_seq;

    create sequence wfs_service_provider_id_seq;
