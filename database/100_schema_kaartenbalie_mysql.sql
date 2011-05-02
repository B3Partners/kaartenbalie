
    create table account (
        id integer not null,
        credit_balance decimal(15,2),
        primary key (id)
    ) comment='Accounting module: balans en transacties van een organisatie' ENGINE=InnoDB;

    create table attribution (
        id integer not null,
        title varchar(50),
        attribution_url varchar(4000),
        logo_url varchar(4000),
        logo_width varchar(50),
        logo_height varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table client_request (
        id integer not null auto_increment,
        client_request_uri varchar(4000),
        timestamp datetime,
        user_id integer,
        method varchar(255),
        client_ip varchar(255),
        service varchar(255),
        operation varchar(255),
        organization_id integer,
        exception_class varchar(255),
        exception_message varchar(4000),
        primary key (id)
    ) comment='Monitoring module' ENGINE=InnoDB;

    create table contact_information (
        id integer not null,
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
    ) ENGINE=InnoDB;

    create table dimensions (
        id integer not null auto_increment,
        layer integer not null,
        dimensions_name varchar(50),
        dimensions_unit varchar(50),
        dimensions_unit_symbol varchar(50),
        extent_name varchar(50),
        extent_defaults varchar(50),
        extent_nearest_value varchar(50),
        extent_multiple_values varchar(50),
        extent_current varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table identifier (
        id integer not null auto_increment,
        layer integer not null,
        authority_name varchar(50) not null,
        authority_url varchar(50) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table layer (
        id integer not null auto_increment,
        service_provider integer not null,
        parent integer,
        name varchar(200),
        title varchar(255) not null,
        abstracts longtext,
        queryable varchar(50),
        cascaded varchar(50),
        opaque varchar(50),
        nosubsets varchar(50),
        fixed_width varchar(50),
        fixed_height varchar(50),
        scale_hint_min varchar(50),
        scale_hint_max varchar(50),
        metadata longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table layer_domain_resource (
        id integer not null auto_increment,
        layer integer not null,
        domain varchar(50) not null,
        url varchar(4000) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table layer_domain_resource_formats (
        layer_domain_resource integer not null,
        format varchar(100) not null,
        primary key (layer_domain_resource, format)
    ) ENGINE=InnoDB;

    create table layer_keyword_list (
        layer integer not null,
        keyword varchar(50) not null,
        primary key (layer, keyword)
    ) ENGINE=InnoDB;

    create table layer_metadata (
        id integer not null auto_increment,
        layer integer not null,
        metadata longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table layer_price_composition (
        id integer not null auto_increment,
        transaction integer,
        server_provider_prefix varchar(255),
        layer_name varchar(255),
        calculation_date datetime,
        plan_type integer,
        service varchar(255),
        operation varchar(255),
        units decimal(5,2),
        scale decimal(20,2),
        projection varchar(255),
        layer_is_free bit,
        method integer,
        calculation_time bigint,
        layer_price decimal(12,2),
        primary key (id)
    ) comment='Accounting module: geeft opbouw van de prijs van een enkele layer uit een transactie weer' ENGINE=InnoDB;

    create table layer_pricing (
        id integer not null auto_increment,
        layer_name varchar(255),
        server_provider_prefix varchar(255),
        plan_type integer,
        valid_from datetime,
        valid_until datetime,
        creation_date datetime,
        deletion_date datetime,
        layer_is_free bit,
        unit_price decimal(9,2),
        service varchar(255),
        operation varchar(255),
        min_scale decimal(20,10),
        max_scale decimal(20,10),
        projection varchar(255),
        primary key (id)
    ) comment='Accounting module: ingestelde prijs van een layer' ENGINE=InnoDB;

    create table operation (
        id integer not null auto_increment,
        client_request integer,
        soort integer,
        duration bigint,
        ms_since_request_start bigint,
        number_of_images integer,
        data_size bigint,
        bytes_received_from_user integer,
        bytes_sent_to_user integer,
        primary key (id)
    ) comment='Monitoring module' ENGINE=InnoDB;

    create table organization (
        id integer not null auto_increment,
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
        has_valid_get_capabilities bit not null,
        bbox varchar(50),
        code varchar(50),
        allow_accounting_layers bit not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table organization_layers (
        organization integer not null,
        layer integer not null,
        primary key (organization, layer)
    ) ENGINE=InnoDB;

    create table organization_wfs_layers (
        organization integer not null,
        layer integer not null,
        primary key (organization, layer)
    ) ENGINE=InnoDB;

    create table report (
        id integer not null auto_increment,
        organization integer,
        report_date datetime,
        processing_time bigint,
        start_date datetime,
        end_date datetime,
        report_xml longtext,
        name varchar(255),
        report_mime varchar(255),
        primary key (id)
    ) comment='Reporting module' ENGINE=InnoDB;

    create table roles (
        id integer not null auto_increment,
        role varchar(45) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table service_domain_resource (
        id integer not null auto_increment,
        service_provider integer not null,
        domain varchar(50) not null,
        get_url varchar(4000),
        post_url varchar(4000),
        primary key (id)
    ) ENGINE=InnoDB;

    create table service_domain_resource_fmts (
        service_domain_resource integer not null,
        format varchar(100) not null,
        primary key (service_domain_resource, format)
    ) ENGINE=InnoDB;

    create table service_provider (
        id integer not null auto_increment,
        name varchar(60) not null,
        abbr varchar(60) not null,
        title varchar(255) not null,
        abstracts longtext,
        fees longtext,
        access_constraints longtext,
        given_name varchar(50) not null,
        url varchar(4000) not null,
        updated_date datetime not null,
        wms_version varchar(50) not null,
        status varchar(80),
        primary key (id)
    ) ENGINE=InnoDB;

    create table service_provider_exceptions (
        service_provider integer not null,
        format varchar(50) not null,
        primary key (service_provider, format)
    ) ENGINE=InnoDB;

    create table service_provider_keyword_list (
        service_provider integer not null,
        keyword varchar(50) not null,
        primary key (service_provider, keyword)
    ) ENGINE=InnoDB;

    create table service_provider_request (
        id integer not null auto_increment,
        client_request integer,
        bytes_sent bigint,
        bytes_received bigint,
        message_sent longtext,
        message_received longtext,
        response_status integer,
        provider_request_uri varchar(4000),
        request_response_time bigint,
        ms_since_request_start bigint,
        exception_class varchar(255),
        exception_message varchar(4000),
        wms_version varchar(255),
        service_provider_id integer,
        srs varchar(255),
        width integer,
        height integer,
        format varchar(255),
        primary key (id)
    ) comment='Monitoring module' ENGINE=InnoDB;

    create table srs_bounding_box (
        id integer not null auto_increment,
        layer integer not null,
        srs varchar(150),
        minx varchar(50),
        maxx varchar(50),
        miny varchar(50),
        maxy varchar(50),
        resx varchar(50),
        resy varchar(50),
        primary key (id)
    ) ENGINE=InnoDB;

    create table style (
        id integer not null auto_increment,
        layer integer not null,
        name varchar(255) not null,
        title varchar(255) not null,
        abstracts longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table style_domain_resource (
        id integer not null auto_increment,
        style integer not null,
        domain varchar(45) not null,
        url varchar(4000) not null,
        width varchar(45),
        height varchar(45),
        primary key (id)
    ) ENGINE=InnoDB;

    create table style_domain_resource_formats (
        style_domain_resource integer not null,
        format varchar(45) not null,
        primary key (style_domain_resource, format)
    ) ENGINE=InnoDB;

    create table transaction (
        id integer not null auto_increment,
        credit_alteration decimal(12,2),
        transaction_date datetime,
        mutation_date datetime,
        status integer,
        soort integer,
        error_message varchar(255),
        user_id integer,
        description varchar(32),
        account integer,
        billing_amount decimal(10,2),
        tx_exchange_rate integer,
        primary key (id)
    ) comment='Accounting module' ENGINE=InnoDB;

    create table users (
        id integer not null auto_increment,
        main_organization integer not null,
        first_name varchar(50),
        surname varchar(50),
        email_address varchar(50),
        username varchar(50) not null,
        password varchar(50) not null,
        personalurl varchar(4000),
        timeout datetime,
        default_get_map varchar(4000),
        primary key (id)
    ) ENGINE=InnoDB;

    create table users_ips (
        users integer not null,
        ipaddress varchar(45) not null,
        primary key (users, ipaddress)
    ) ENGINE=InnoDB;

    create table users_orgs (
        organization integer not null,
        users integer not null,
        primary key (users, organization)
    ) ENGINE=InnoDB;

    create table users_roles (
        users integer not null,
        role integer not null,
        primary key (users, role)
    ) ENGINE=InnoDB;

    create table wfs_layer (
        id integer not null auto_increment,
        wfs_service_provider integer not null,
        name varchar(200),
        title varchar(255) not null,
        metadata longtext,
        primary key (id)
    ) ENGINE=InnoDB;

    create table wfs_service_provider (
        id integer not null auto_increment,
        name varchar(80) not null,
        abbr varchar(80) not null,
        title varchar(255),
        given_name varchar(50) not null,
        url varchar(4000) not null,
        updated_date datetime not null,
        wfs_version varchar(50) not null,
        status varchar(80),
        primary key (id)
    ) ENGINE=InnoDB;

    alter table account 
        add index FKB9D38A2D435502A6 (id), 
        add constraint FKB9D38A2D435502A6 
        foreign key (id) 
        references organization (id);

    alter table attribution 
        add index FKED87907FACB075C4 (id), 
        add constraint FKED87907FACB075C4 
        foreign key (id) 
        references layer (id);

    alter table contact_information 
        add index FK6BC4C78D4250FE39 (id), 
        add constraint FK6BC4C78D4250FE39 
        foreign key (id) 
        references service_provider (id);

    alter table dimensions 
        add index FK18B23FCDB2D03DFA (layer), 
        add constraint FK18B23FCDB2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table identifier 
        add index FK9F88ACA9B2D03DFA (layer), 
        add constraint FK9F88ACA9B2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table layer 
        add index FK61FD55177621D59 (service_provider), 
        add constraint FK61FD55177621D59 
        foreign key (service_provider) 
        references service_provider (id);

    alter table layer 
        add index FK61FD551715B7153 (parent), 
        add constraint FK61FD551715B7153 
        foreign key (parent) 
        references layer (id);

    alter table layer_domain_resource 
        add index FKF41CA63BB2D03DFA (layer), 
        add constraint FKF41CA63BB2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table layer_domain_resource_formats 
        add index FK80AE95813E1F836 (layer_domain_resource), 
        add constraint FK80AE95813E1F836 
        foreign key (layer_domain_resource) 
        references layer_domain_resource (id);

    alter table layer_keyword_list 
        add index FK480A1562B2D03DFA (layer), 
        add constraint FK480A1562B2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table layer_metadata 
        add index FKBDAF425DB2D03DFA (layer), 
        add constraint FKBDAF425DB2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table layer_price_composition 
        add index FKD866E046FF0042D2 (transaction), 
        add constraint FKD866E046FF0042D2 
        foreign key (transaction) 
        references transaction (id);

    alter table operation 
        add index FK631AD567B2136F51 (client_request), 
        add constraint FK631AD567B2136F51 
        foreign key (client_request) 
        references client_request (id);

    alter table organization_layers 
        add index FK6CED35CE8999E2BE (organization), 
        add constraint FK6CED35CE8999E2BE 
        foreign key (organization) 
        references organization (id);

    alter table organization_layers 
        add index FK6CED35CEB2D03DFA (layer), 
        add constraint FK6CED35CEB2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table organization_wfs_layers 
        add index FKD17104E98999E2BE (organization), 
        add constraint FKD17104E98999E2BE 
        foreign key (organization) 
        references organization (id);

    alter table organization_wfs_layers 
        add index FKD17104E9CEDB4E46 (layer), 
        add constraint FKD17104E9CEDB4E46 
        foreign key (layer) 
        references wfs_layer (id);

    alter table report 
        add index FKC84C55348999E2BE (organization), 
        add constraint FKC84C55348999E2BE 
        foreign key (organization) 
        references organization (id);

    alter table service_domain_resource 
        add index FK2A994F9F77621D59 (service_provider), 
        add constraint FK2A994F9F77621D59 
        foreign key (service_provider) 
        references service_provider (id);

    alter table service_domain_resource_fmts 
        add index FK709D5A26E96D5DFE (service_domain_resource), 
        add constraint FK709D5A26E96D5DFE 
        foreign key (service_domain_resource) 
        references service_domain_resource (id);

    alter table service_provider_exceptions 
        add index FKE735008877621D59 (service_provider), 
        add constraint FKE735008877621D59 
        foreign key (service_provider) 
        references service_provider (id);

    alter table service_provider_keyword_list 
        add index FK9F2ECAB877621D59 (service_provider), 
        add constraint FK9F2ECAB877621D59 
        foreign key (service_provider) 
        references service_provider (id);

    alter table service_provider_request 
        add index FK50C95A8BB2136F51 (client_request), 
        add constraint FK50C95A8BB2136F51 
        foreign key (client_request) 
        references client_request (id);

    alter table srs_bounding_box 
        add index FK3591391BB2D03DFA (layer), 
        add constraint FK3591391BB2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table style 
        add index FK68B1DB1B2D03DFA (layer), 
        add constraint FK68B1DB1B2D03DFA 
        foreign key (layer) 
        references layer (id);

    alter table style_domain_resource 
        add index FKC4AB2E9BB3A6CEBA (style), 
        add constraint FKC4AB2E9BB3A6CEBA 
        foreign key (style) 
        references style (id);

    alter table style_domain_resource_formats 
        add index FK5F3B11B8B5C5A0F6 (style_domain_resource), 
        add constraint FK5F3B11B8B5C5A0F6 
        foreign key (style_domain_resource) 
        references style_domain_resource (id);

    alter table transaction 
        add index FK7FA0D2DE7E3BAC70 (account), 
        add constraint FK7FA0D2DE7E3BAC70 
        foreign key (account) 
        references account (id);

    alter table users 
        add index FK6A68E08C51D1B84 (main_organization), 
        add constraint FK6A68E08C51D1B84 
        foreign key (main_organization) 
        references organization (id);

    alter table users_ips 
        add index FK154D1175A4475A2B (users), 
        add constraint FK154D1175A4475A2B 
        foreign key (users) 
        references users (id);

    alter table users_orgs 
        add index FK9457DDE6A4475A2B (users), 
        add constraint FK9457DDE6A4475A2B 
        foreign key (users) 
        references users (id);

    alter table users_orgs 
        add index FK9457DDE68999E2BE (organization), 
        add constraint FK9457DDE68999E2BE 
        foreign key (organization) 
        references organization (id);

    alter table users_roles 
        add index FKF6CCD9C6A4475A2B (users), 
        add constraint FKF6CCD9C6A4475A2B 
        foreign key (users) 
        references users (id);

    alter table users_roles 
        add index FKF6CCD9C6AD40A28B (role), 
        add constraint FKF6CCD9C6AD40A28B 
        foreign key (role) 
        references roles (id);

    alter table wfs_layer 
        add index FKBB3050D69B6DCE00 (wfs_service_provider), 
        add constraint FKBB3050D69B6DCE00 
        foreign key (wfs_service_provider) 
        references wfs_service_provider (id);
