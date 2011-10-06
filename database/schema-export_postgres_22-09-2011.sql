
    create table account (
        id int4 not null,
        credit_balance numeric(15, 2),
        primary key (id)
    );

    comment on table account is
        'Accounting module: balans en transacties van een organisatie';

    create table attribution (
        id int4 not null,
        title varchar(255),
        attribution_url varchar(4000),
        logo_url varchar(4000),
        logo_width varchar(50),
        logo_height varchar(50),
        primary key (id)
    );

    create table client_request (
        id  serial not null,
        client_request_uri varchar(4000),
        timestamp timestamp,
        user_id int4,
        method varchar(255),
        client_ip varchar(255),
        service varchar(255),
        operation varchar(255),
        organization_id int4,
        exception_class varchar(255),
        exception_message varchar(4000),
        primary key (id)
    );

    comment on table client_request is
        'Monitoring module';

    create table contact_information (
        id int4 not null,
        contact_person varchar(80),
        contact_position varchar(255),
        address varchar(80),
        address_type varchar(80),
        postcode varchar(50),
        city varchar(80),
        state_or_province varchar(80),
        country varchar(255),
        voice_telephone varchar(80),
        fascimile_telephone varchar(80),
        email_address varchar(255),
        primary key (id)
    );

    create table dimensions (
        id  serial not null,
        layer int4 not null,
        dimensions_name varchar(50),
        dimensions_unit varchar(50),
        dimensions_unit_symbol varchar(50),
        extent_name varchar(50),
        extent_defaults varchar(50),
        extent_nearest_value varchar(50),
        extent_multiple_values varchar(50),
        extent_current varchar(50),
        primary key (id)
    );

    create table identifier (
        id  serial not null,
        layer int4 not null,
        authority_name varchar(50) not null,
        authority_url varchar(50) not null,
        primary key (id)
    );

    create table layer (
        id  serial not null,
        service_provider int4 not null,
        parent int4,
        name varchar(200),
        title varchar(255) not null,
        abstracts text,
        queryable varchar(50),
        cascaded varchar(50),
        opaque varchar(50),
        nosubsets varchar(50),
        fixed_width varchar(50),
        fixed_height varchar(50),
        scale_hint_min varchar(50),
        scale_hint_max varchar(50),
        metadata text,
        primary key (id)
    );

    create table layer_domain_resource (
        id  serial not null,
        layer int4 not null,
        domain varchar(50) not null,
        url varchar(4000) not null,
        primary key (id)
    );

    create table layer_domain_resource_formats (
        layer_domain_resource int4 not null,
        format varchar(100) not null,
        primary key (layer_domain_resource, format)
    );

    create table layer_keyword_list (
        layer int4 not null,
        keyword varchar(50) not null,
        primary key (layer, keyword)
    );

    create table layer_metadata (
        id int4 not null,
        layer int4 not null,
        metadata text,
        primary key (id)
    );

    create table layer_price_composition (
        id  serial not null,
        transaction int4,
        server_provider_prefix varchar(255),
        layer_name varchar(255),
        calculation_date timestamp,
        plan_type int4,
        service varchar(255),
        operation varchar(255),
        units numeric(5, 2),
        scale numeric(20, 2),
        projection varchar(255),
        layer_is_free bool,
        method int4,
        calculation_time int8,
        layer_price numeric(12, 2),
        primary key (id)
    );

    comment on table layer_price_composition is
        'Accounting module: geeft opbouw van de prijs van een enkele layer uit een transactie weer';

    create table layer_pricing (
        id  serial not null,
        layer_name varchar(255),
        server_provider_prefix varchar(255),
        plan_type int4,
        valid_from timestamp,
        valid_until timestamp,
        creation_date timestamp,
        deletion_date timestamp,
        layer_is_free bool,
        unit_price numeric(9, 2),
        service varchar(255),
        operation varchar(255),
        min_scale numeric(20, 10),
        max_scale numeric(20, 10),
        projection varchar(255),
        primary key (id)
    );

    comment on table layer_pricing is
        'Accounting module: ingestelde prijs van een layer';

    create table operation (
        id  serial not null,
        client_request int4,
        soort int4,
        duration int8,
        ms_since_request_start int8,
        number_of_images int4,
        data_size int8,
        bytes_received_from_user int4,
        bytes_sent_to_user int4,
        primary key (id)
    );

    comment on table operation is
        'Monitoring module';

    create table organization (
        id  serial not null,
        name varchar(50) not null,
        street varchar(50),
        streetnumber varchar(5),
        addition varchar(10),
        postalcode varchar(45),
        province varchar(50),
        country varchar(50),
        postbox varchar(50),
        billing_address varchar(50),
        visitors_address varchar(50),
        telephone varchar(50),
        fax varchar(50),
        has_valid_get_capabilities bool not null,
        bbox varchar(50),
        code varchar(50),
        allow_accounting_layers bool not null,
        primary key (id)
    );

    create table organization_layers (
        organization int4 not null,
        layer int4 not null,
        primary key (organization, layer)
    );

    create table organization_wfs_layers (
        organization int4 not null,
        layer int4 not null,
        primary key (organization, layer)
    );

    create table report (
        id  serial not null,
        organization int4,
        report_date timestamp,
        processing_time int8,
        start_date timestamp,
        end_date timestamp,
        report_xml text,
        name varchar(255),
        report_mime varchar(255),
        primary key (id)
    );

    comment on table report is
        'Reporting module';

    create table roles (
        id  serial not null,
        role varchar(45) not null,
        primary key (id)
    );

    create table service_domain_resource (
        id  serial not null,
        service_provider int4 not null,
        domain varchar(50) not null,
        get_url varchar(4000),
        post_url varchar(4000),
        primary key (id)
    );

    create table service_domain_resource_fmts (
        service_domain_resource int4 not null,
        format varchar(100) not null,
        primary key (service_domain_resource, format)
    );

    create table service_provider (
        id  serial not null,
        name varchar(60) not null,
        abbr varchar(60) not null,
        title varchar(255) not null,
        abstracts text,
        fees text,
        access_constraints text,
        given_name varchar(50) not null,
        url varchar(4000) not null,
        updated_date timestamp not null,
        wms_version varchar(50) not null,
        status varchar(80),
        sld_url varchar(255),
        primary key (id)
    );

    create table service_provider_exceptions (
        service_provider int4 not null,
        format varchar(50) not null,
        primary key (service_provider, format)
    );

    create table service_provider_keyword_list (
        service_provider int4 not null,
        keyword varchar(50) not null,
        primary key (service_provider, keyword)
    );

    create table service_provider_request (
        id  serial not null,
        client_request int4,
        bytes_sent int8,
        bytes_received int8,
        message_sent text,
        message_received text,
        response_status int4,
        provider_request_uri varchar(4000),
        request_response_time int8,
        ms_since_request_start int8,
        exception_class varchar(255),
        exception_message varchar(4000),
        wms_version varchar(255),
        service_provider_id int4,
        srs varchar(255),
        width int4,
        height int4,
        format varchar(255),
        primary key (id)
    );

    comment on table service_provider_request is
        'Monitoring module';

    create table srs_bounding_box (
        id  serial not null,
        layer int4 not null,
        srs varchar(150),
        minx varchar(50),
        maxx varchar(50),
        miny varchar(50),
        maxy varchar(50),
        resx varchar(50),
        resy varchar(50),
        primary key (id)
    );

    create table style (
        id  serial not null,
        layer int4 not null,
        name varchar(255) not null,
        title varchar(255) not null,
        abstracts text,
        sld_part text,
        primary key (id)
    );

    create table style_domain_resource (
        id  serial not null,
        style int4 not null,
        domain varchar(45) not null,
        url varchar(4000) not null,
        width varchar(45),
        height varchar(45),
        primary key (id)
    );

    create table style_domain_resource_formats (
        style_domain_resource int4 not null,
        format varchar(45) not null,
        primary key (style_domain_resource, format)
    );

    create table transaction (
        id  serial not null,
        credit_alteration numeric(12, 2),
        transaction_date timestamp,
        mutation_date timestamp,
        status int4,
        soort int4,
        error_message varchar(255),
        user_id int4,
        description varchar(32),
        account int4,
        billing_amount numeric(10, 2),
        tx_exchange_rate int4,
        primary key (id)
    );

    comment on table transaction is
        'Accounting module';

    create table users (
        id  serial not null,
        main_organization int4 not null,
        first_name varchar(50),
        surname varchar(50),
        email_address varchar(50),
        username varchar(50) not null,
        password varchar(50) not null,
        personalurl varchar(4000),
        timeout timestamp,
        default_get_map varchar(4000),
        primary key (id)
    );

    create table users_ips (
        users int4 not null,
        ipaddress varchar(45) not null,
        primary key (users, ipaddress)
    );

    create table users_orgs (
        organization int4 not null,
        users int4 not null,
        primary key (users, organization)
    );

    create table users_roles (
        users int4 not null,
        role int4 not null,
        primary key (users, role)
    );

    create table wfs_layer (
        id  serial not null,
        wfs_service_provider int4 not null,
        name varchar(200),
        title varchar(255) not null,
        metadata text,
        primary key (id)
    );

    create table wfs_service_provider (
        id  serial not null,
        name varchar(80) not null,
        abbr varchar(80) not null,
        title varchar(255),
        given_name varchar(50) not null,
        url varchar(4000) not null,
        updated_date timestamp not null,
        wfs_version varchar(50) not null,
        status varchar(80),
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

    alter table layer_metadata 
        add constraint FKBDAF425DB2D03DFA 
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

    create sequence layer_metadata_id_seq;
