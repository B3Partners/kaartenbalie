DROP TABLE
    acc_account;
CREATE
    TABLE acc_account
    (
        id INTEGER NOT NULL,
        acc_creditbalance NUMERIC(15,2),
        PRIMARY KEY (id)
    );
DROP TABLE
    acc_layerpricing;
CREATE
    TABLE acc_layerpricing
    (
        id SERIAL,
        lpr_layername CHARACTER VARYING(255),
        lpr_serverproviderprefix CHARACTER VARYING(255),
        lpr_plantype INTEGER,
        lpr_validfrom TIMESTAMP WITHOUT TIME ZONE,
        lpr_validuntil TIMESTAMP WITHOUT TIME ZONE,
        lpr_creationdate TIMESTAMP WITHOUT TIME ZONE,
        lpr_deletiondate TIMESTAMP WITHOUT TIME ZONE,
        lpr_layerisfree BOOLEAN,
        lpr_unitprice NUMERIC(9,2),
        lpr_service CHARACTER VARYING(255),
        lpr_operation CHARACTER VARYING(255),
        lpr_minscale NUMERIC(20,10),
        lpr_maxscale NUMERIC(20,10),
        lpr_projection CHARACTER VARYING(255),
        PRIMARY KEY (id),
        UNIQUE (lpr_creationdate)
    );
DROP TABLE
    acc_pricecomp;
CREATE
    TABLE acc_pricecomp
    (
        id SERIAL,
        prc_tra_id INTEGER,
        prc_serverproviderprefix CHARACTER VARYING(255),
        prc_layername CHARACTER VARYING(255),
        prc_calculationdate TIMESTAMP WITHOUT TIME ZONE,
        prc_plantype INTEGER,
        prc_service CHARACTER VARYING(255),
        prc_operation CHARACTER VARYING(255),
        lpr_units NUMERIC(5,2),
        prc_projection CHARACTER VARYING(255),
        prc_layerisfree BOOLEAN,
        prc_method INTEGER,
        prc_calculationtime BIGINT,
        lpr_layerprice NUMERIC(12,2),
        prc_scale NUMERIC(20,2),
        PRIMARY KEY (id)
    );
DROP TABLE
    acc_transaction;
CREATE
    TABLE acc_transaction
    (
        id SERIAL,
        tra_creditalteration NUMERIC(12,2),
        tra_transactiondate TIMESTAMP WITHOUT TIME ZONE,
        tra_mutationdate TIMESTAMP WITHOUT TIME ZONE,
        tra_status INTEGER,
        tra_type INTEGER,
        tra_errormessage CHARACTER VARYING(255),
        tra_userid INTEGER,
        tra_description CHARACTER VARYING(32),
        tra_acc_id INTEGER,
        tra_billingamount NUMERIC(10,2),
        tra_txexchangerate INTEGER,
        PRIMARY KEY (id)
    );
DROP TABLE
    attribution;
CREATE
    TABLE attribution
    (
        id INTEGER NOT NULL,
        title CHARACTER VARYING(50) DEFAULT '0',
        attributionurl CHARACTER VARYING(4000) DEFAULT '0',
        logourl CHARACTER VARYING(4000),
        logowidth CHARACTER VARYING(50),
        logoheight CHARACTER VARYING(50),
        PRIMARY KEY (id)
    );
DROP TABLE
    contactinformation;
CREATE
    TABLE contactinformation
    (
        id INTEGER NOT NULL,
        contactperson CHARACTER VARYING(50),
        contactposition CHARACTER VARYING(50),
        address CHARACTER VARYING(50),
        addresstype CHARACTER VARYING(50),
        postcode CHARACTER VARYING(50),
        city CHARACTER VARYING(50),
        stateorprovince CHARACTER VARYING(50),
        country CHARACTER VARYING(50),
        voicetelephone CHARACTER VARYING(50),
        fascimiletelephone CHARACTER VARYING(50),
        emailaddress CHARACTER VARYING(50),
        PRIMARY KEY (id)
    );
DROP TABLE
    dimensions;
CREATE
    TABLE dimensions
    (
        id SERIAL,
        layerid INTEGER NOT NULL,
        dimensionsname CHARACTER VARYING(50),
        dimensionsunit CHARACTER VARYING(50),
        dimensionsunitsymbol CHARACTER VARYING(50),
        extentname CHARACTER VARYING(50),
        extentdefaults CHARACTER VARYING(50),
        extentnearestvalue CHARACTER VARYING(50),
        extentmultiplevalues CHARACTER VARYING(50),
        extentcurrent CHARACTER VARYING(50),
        PRIMARY KEY (id)
    );
DROP TABLE
    exceptions;
CREATE
    TABLE exceptions
    (
        serviceproviderid INTEGER NOT NULL,
        format CHARACTER VARYING(50) NOT NULL,
        PRIMARY KEY (serviceproviderid, format)
    );
DROP TABLE
    identifier;
CREATE
    TABLE identifier
    (
        id SERIAL,
        layerid INTEGER NOT NULL,
        authorityname CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        authorityurl CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        PRIMARY KEY (id)
    );
DROP TABLE
    layer;
CREATE
    TABLE layer
    (
        id SERIAL,
        parentid INTEGER,
        serviceproviderid INTEGER NOT NULL,
        name CHARACTER VARYING(200),
        title CHARACTER VARYING(255) DEFAULT '0' NOT NULL,
        abstracts TEXT,
        queryable CHARACTER VARYING(50) DEFAULT '0',
        cascaded CHARACTER VARYING(50) DEFAULT '0',
        opaque CHARACTER VARYING(50) DEFAULT '0',
        nosubsets CHARACTER VARYING(50) DEFAULT '0',
        fixedwidth CHARACTER VARYING(50) DEFAULT '0',
        fixedheight CHARACTER VARYING(50) DEFAULT '0',
        scalehintmin CHARACTER VARYING(50) DEFAULT '0',
        scalehintmax CHARACTER VARYING(50) DEFAULT '0',
        metadata TEXT,
        PRIMARY KEY (id)
    );
DROP TABLE
    layerdomainformat;
CREATE
    TABLE layerdomainformat
    (
        ldrid INTEGER NOT NULL,
        format CHARACTER VARYING(100) NOT NULL,
        PRIMARY KEY (ldrid, format)
    );
DROP TABLE
    layerdomainresource;
CREATE
    TABLE layerdomainresource
    (
        id SERIAL,
        layerid INTEGER NOT NULL,
        domain CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        url CHARACTER VARYING(4000) NOT NULL,
        PRIMARY KEY (id)
    );
DROP TABLE
    layerkeywordlist;
CREATE
    TABLE layerkeywordlist
    (
        layerid INTEGER NOT NULL,
        keyword CHARACTER VARYING(50) NOT NULL,
        PRIMARY KEY (layerid, keyword)
    );
DROP TABLE
    mon_clientrequest;
CREATE
    TABLE mon_clientrequest
    (
        id SERIAL,
        clr_clientrequesturi CHARACTER VARYING(4000),
        clr_timestamp TIMESTAMP WITHOUT TIME ZONE,
        clr_userid INTEGER,
        clr_method CHARACTER VARYING(255),
        clr_clientip CHARACTER VARYING(255),
        clr_service CHARACTER VARYING(255),
        clr_operation CHARACTER VARYING(255),
        clr_organizationid INTEGER,
        clr_exceptionclass CHARACTER VARYING(255),
        clr_exceptionmessage CHARACTER VARYING(4000),
        PRIMARY KEY (id)
    );
DROP TABLE
    mon_requestoperation;
CREATE
    TABLE mon_requestoperation
    (
        id SERIAL,
        rqo_clr_id INTEGER,
        rqo_duration BIGINT,
        rqo_mssincerequeststart BIGINT,
        rqo_numberofimages INTEGER,
        rqo_datasize BIGINT,
        rqo_bytesreceivedfromuser INTEGER,
        rqo_bytessendtouser INTEGER,
        rqo_type INTEGER,
        PRIMARY KEY (id)
    );
DROP TABLE
    mon_serviceproviderrequest;
CREATE
    TABLE mon_serviceproviderrequest
    (
        id SERIAL,
        spr_clr_id INTEGER,
        spr_bytessend BIGINT,
        spr_bytesreceived BIGINT,
        spr_responsestatus INTEGER,
        spr_providerrequesturi CHARACTER VARYING(4000),
        spr_requestresponsetime BIGINT,
        spr_mssincerequeststart BIGINT,
        spr_exceptionclass CHARACTER VARYING(255),
        spr_exceptionmessage CHARACTER VARYING(4000),
        spr_wmsversion CHARACTER VARYING(255),
        spr_serviceproviderid INTEGER,
        spr_srs CHARACTER VARYING(255),
        spr_width INTEGER,
        spr_height INTEGER,
        spr_format CHARACTER VARYING(255),
        PRIMARY KEY (id)
    );
DROP TABLE
    organization;
CREATE
    TABLE organization
    (
        id SERIAL,
        name CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        street CHARACTER VARYING(50) DEFAULT '0',
        NUMBER CHARACTER VARYING(5) DEFAULT '0',
        addition CHARACTER VARYING(10),
        postalcode CHARACTER VARYING(45),
        province CHARACTER VARYING(50) DEFAULT '0',
        country CHARACTER VARYING(50) DEFAULT '0',
        postbox CHARACTER VARYING(50),
        billingaddress CHARACTER VARYING(50),
        visitorsaddress CHARACTER VARYING(50),
        telephone CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        fax CHARACTER VARYING(50),
        hasvalidgetcapabilities BOOLEAN NOT NULL,
        bbox CHARACTER VARYING(50),
        code CHARACTER VARYING(50),
        allowaccountinglayers BOOLEAN,
        PRIMARY KEY (id)
    );
DROP TABLE
    organizationlayer;
CREATE
    TABLE organizationlayer
    (
        organizationid INTEGER NOT NULL,
        layerid INTEGER NOT NULL,
        PRIMARY KEY (organizationid, layerid)
    );
DROP TABLE
    rep_report;
CREATE
    TABLE rep_report
    (
        id SERIAL,
        rpd_org_id INTEGER,
        rep_reportdate TIMESTAMP WITHOUT TIME ZONE,
        rep_processingtime BIGINT,
        rep_startdate TIMESTAMP WITHOUT TIME ZONE,
        rep_enddate TIMESTAMP WITHOUT TIME ZONE,
        rep_organizationid INTEGER,
        rep_xml CHARACTER VARYING(8000),
        rep_mime CHARACTER VARYING(100),
        rep_name CHARACTER VARYING(255),
        PRIMARY KEY (id)
    );
DROP TABLE
    roles;
CREATE
    TABLE roles
    (
        id SERIAL,
        role CHARACTER VARYING(45) NOT NULL,
        PRIMARY KEY (id)
    );
DROP TABLE
    servicedomainformat;
CREATE
    TABLE servicedomainformat
    (
        sdrid INTEGER NOT NULL,
        format CHARACTER VARYING(100) NOT NULL,
        PRIMARY KEY (sdrid, format)
    );
DROP TABLE
    servicedomainresource;
CREATE
    TABLE servicedomainresource
    (
        id SERIAL,
        serviceproviderid INTEGER NOT NULL,
        domain CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        geturl CHARACTER VARYING(4000) DEFAULT '0',
        posturl CHARACTER VARYING(4000),
        PRIMARY KEY (id)
    );
DROP TABLE
    serviceprovider;
CREATE
    TABLE serviceprovider
    (
        id SERIAL,
        name CHARACTER VARYING(60) DEFAULT '0' NOT NULL,
        abbr CHARACTER VARYING(60) DEFAULT '0' NOT NULL,
        title CHARACTER VARYING(255) DEFAULT '0' NOT NULL,
        abstracts TEXT,
        fees TEXT,
        accessconstraints TEXT,
        givenname CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        url CHARACTER VARYING(4000) DEFAULT '0' NOT NULL,
        updateddate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        wmsversion CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        PRIMARY KEY (id)
    );
DROP TABLE
    serviceproviderkeywordlist;
CREATE
    TABLE serviceproviderkeywordlist
    (
        serviceproviderid INTEGER NOT NULL,
        keyword CHARACTER VARYING(50) NOT NULL,
        PRIMARY KEY (serviceproviderid, keyword)
    );
DROP TABLE
    srs;
CREATE
    TABLE srs
    (
        id SERIAL,
        layerid INTEGER NOT NULL,
        srs CHARACTER VARYING(150),
        minx CHARACTER VARYING(50),
        maxx CHARACTER VARYING(50),
        miny CHARACTER VARYING(50),
        maxy CHARACTER VARYING(50),
        resx CHARACTER VARYING(50),
        resy CHARACTER VARYING(50),
        PRIMARY KEY (id)
    );
DROP TABLE
    style;
CREATE
    TABLE style
    (
        id SERIAL,
        layerid INTEGER NOT NULL,
        name CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        title CHARACTER VARYING(255) DEFAULT '0' NOT NULL,
        abstracts TEXT,
        PRIMARY KEY (id)
    );
DROP TABLE
    styledomainformat;
CREATE
    TABLE styledomainformat
    (
        sdrid INTEGER NOT NULL,
        format CHARACTER VARYING(45) NOT NULL,
        PRIMARY KEY (sdrid, format)
    );
DROP TABLE
    styledomainresource;
CREATE
    TABLE styledomainresource
    (
        id SERIAL,
        styleid INTEGER NOT NULL,
        domain CHARACTER VARYING(45) NOT NULL,
        url CHARACTER VARYING(4000) NOT NULL,
        width CHARACTER VARYING(45),
        height CHARACTER VARYING(45),
        PRIMARY KEY (id)
    );
DROP TABLE
    userip;
CREATE
    TABLE userip
    (
        userid INTEGER NOT NULL,
        ipaddress CHARACTER VARYING(45) NOT NULL,
        PRIMARY KEY (userid, ipaddress)
    );
DROP TABLE
    userroles;
CREATE
    TABLE userroles
    (
        userid INTEGER NOT NULL,
        roleid INTEGER NOT NULL,
        PRIMARY KEY (userid, roleid)
    );
DROP TABLE
    users;
CREATE
    TABLE users
    (
        id SERIAL,
        organizationid INTEGER NOT NULL,
        firstname CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        lastname CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        emailaddress CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        username CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        password CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        personalurl CHARACTER VARYING(4000),
        timeout TIMESTAMP WITHOUT TIME ZONE,
        defaultgetmap CHARACTER VARYING(4000),
        PRIMARY KEY (id)
    );
DROP TABLE
    wfs_layer;
CREATE
    TABLE wfs_layer
    (
        id SERIAL,
        wfsserviceproviderid INTEGER NOT NULL,
        name CHARACTER VARYING(200),
        title CHARACTER VARYING(255) DEFAULT '0' NOT NULL,
        metadata TEXT,
        PRIMARY KEY (id)
    );
DROP TABLE
    wfs_organizationlayer;
CREATE
    TABLE wfs_organizationlayer
    (
        organizationid INTEGER NOT NULL,
        wfslayerid INTEGER NOT NULL,
        PRIMARY KEY (organizationid, wfslayerid)
    );
DROP TABLE
    wfs_serviceprovider;
CREATE
    TABLE wfs_serviceprovider
    (
        id SERIAL,
        name CHARACTER VARYING(60) DEFAULT '0' NOT NULL,
        abbr CHARACTER VARYING(60) DEFAULT '0' NOT NULL,
        title CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        givenname CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        url CHARACTER VARYING(4000) DEFAULT '0' NOT NULL,
        updateddate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
        wfsversion CHARACTER VARYING(50) DEFAULT '0' NOT NULL,
        PRIMARY KEY (id)
    );
ALTER TABLE
    acc_account ADD CONSTRAINT fk7fe8986f435502a6 FOREIGN KEY (id) REFERENCES organization (id);
ALTER TABLE
    acc_account ADD CONSTRAINT nnc_17208_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    acc_layerpricing ADD CONSTRAINT nnc_17211_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    acc_pricecomp ADD CONSTRAINT fk165d199a1c0bba79 FOREIGN KEY (prc_tra_id) REFERENCES acc_transaction (id);
ALTER TABLE
    acc_pricecomp ADD CONSTRAINT nnc_17217_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    acc_transaction ADD CONSTRAINT fkfc20f02025126df8 FOREIGN KEY (tra_acc_id) REFERENCES acc_account (id);
ALTER TABLE
    acc_transaction ADD CONSTRAINT nnc_17223_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    attribution ADD CONSTRAINT fked87907facb075c4 FOREIGN KEY (id) REFERENCES layer (id);
ALTER TABLE
    attribution ADD CONSTRAINT nnc_17229_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    contactinformation ADD CONSTRAINT fk65b57d0c4250fe39 FOREIGN KEY (id) REFERENCES serviceprovider (id);
ALTER TABLE
    contactinformation ADD CONSTRAINT nnc_17240_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    dimensions ADD CONSTRAINT fk18b23fcdaa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    dimensions ADD CONSTRAINT nnc_17246_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    dimensions ADD CONSTRAINT nnc_17246_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    exceptions ADD CONSTRAINT fkb1aa3a043501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE
    exceptions ADD CONSTRAINT nnc_17249_1_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE
    exceptions ADD CONSTRAINT nnc_17249_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE
    identifier ADD CONSTRAINT fk9f88aca9aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    identifier ADD CONSTRAINT nnc_17254_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    identifier ADD CONSTRAINT nnc_17254_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    identifier ADD CONSTRAINT nnc_17254_3_not_null CHECK (authorityname IS NOT NULL);
ALTER TABLE
    identifier ADD CONSTRAINT nnc_17254_4_not_null CHECK (authorityurl IS NOT NULL);
ALTER TABLE
    layer ADD CONSTRAINT fk61fd5513501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE
    layer ADD CONSTRAINT fk61fd551f2bbfbee FOREIGN KEY (parentid) REFERENCES layer (id);
ALTER TABLE
    layer ADD CONSTRAINT nnc_17262_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    layer ADD CONSTRAINT nnc_17262_3_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE
    layer ADD CONSTRAINT nnc_17262_5_not_null CHECK (title IS NOT NULL);
ALTER TABLE
    layerdomainformat ADD CONSTRAINT fk17e78d8c25e66a90 FOREIGN KEY (ldrid) REFERENCES layerdomainresource (id);
ALTER TABLE
    layerdomainformat ADD CONSTRAINT nnc_17277_1_not_null CHECK (ldrid IS NOT NULL);
ALTER TABLE
    layerdomainformat ADD CONSTRAINT nnc_17277_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE
    layerdomainresource ADD CONSTRAINT fk8ba44863aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    layerdomainresource ADD CONSTRAINT nnc_17280_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    layerdomainresource ADD CONSTRAINT nnc_17280_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    layerdomainresource ADD CONSTRAINT nnc_17280_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE
    layerdomainresource ADD CONSTRAINT nnc_17280_4_not_null CHECK (url IS NOT NULL);
ALTER TABLE
    layerkeywordlist ADD CONSTRAINT fk30f31016aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    layerkeywordlist ADD CONSTRAINT nnc_17287_1_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    layerkeywordlist ADD CONSTRAINT nnc_17287_2_not_null CHECK (keyword IS NOT NULL);
ALTER TABLE
    mon_clientrequest ADD CONSTRAINT nnc_17290_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    mon_requestoperation ADD CONSTRAINT fkce03888b8a8c8dec FOREIGN KEY (rqo_clr_id) REFERENCES mon_clientrequest (id);
ALTER TABLE
    mon_requestoperation ADD CONSTRAINT nnc_17296_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    mon_serviceproviderrequest ADD CONSTRAINT fk712ceebc223516a7 FOREIGN KEY (spr_clr_id) REFERENCES mon_clientrequest (id);
ALTER TABLE
    mon_serviceproviderrequest ADD CONSTRAINT nnc_17299_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    organization ADD CONSTRAINT nnc_17305_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    organization ADD CONSTRAINT nnc_17305_12_not_null CHECK (telephone IS NOT NULL);
ALTER TABLE
    organization ADD CONSTRAINT nnc_17305_14_not_null CHECK (hasvalidgetcapabilities IS NOT NULL);
ALTER TABLE
    organization ADD CONSTRAINT nnc_17305_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE
    organizationlayer ADD CONSTRAINT fkcdee2ffeaa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    organizationlayer ADD CONSTRAINT fkcdee2ffec136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE
    organizationlayer ADD CONSTRAINT nnc_17317_1_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE
    organizationlayer ADD CONSTRAINT nnc_17317_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    rep_report ADD CONSTRAINT fk23f41f16e340125a FOREIGN KEY (rpd_org_id) REFERENCES organization (id);
ALTER TABLE
    rep_report ADD CONSTRAINT nnc_17323_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    roles ADD CONSTRAINT nnc_17350_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    roles ADD CONSTRAINT nnc_17350_2_not_null CHECK (role IS NOT NULL);
ALTER TABLE
    servicedomainformat ADD CONSTRAINT fkefd888f0c557cb7b FOREIGN KEY (sdrid) REFERENCES servicedomainresource (id);
ALTER TABLE
    servicedomainformat ADD CONSTRAINT nnc_17353_1_not_null CHECK (sdrid IS NOT NULL);
ALTER TABLE
    servicedomainformat ADD CONSTRAINT nnc_17353_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE
    servicedomainresource ADD CONSTRAINT fk2b43fac73501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE
    servicedomainresource ADD CONSTRAINT nnc_17356_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    servicedomainresource ADD CONSTRAINT nnc_17356_2_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE
    servicedomainresource ADD CONSTRAINT nnc_17356_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_10_not_null CHECK (updateddate IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_11_not_null CHECK (wmsversion IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_3_not_null CHECK (abbr IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_8_not_null CHECK (givenname IS NOT NULL);
ALTER TABLE
    serviceprovider ADD CONSTRAINT nnc_17364_9_not_null CHECK (url IS NOT NULL);
ALTER TABLE
    serviceproviderkeywordlist ADD CONSTRAINT fk9b8f46a13501f45f FOREIGN KEY (serviceproviderid) REFERENCES serviceprovider (id);
ALTER TABLE
    serviceproviderkeywordlist ADD CONSTRAINT nnc_17376_1_not_null CHECK (serviceproviderid IS NOT NULL);
ALTER TABLE
    serviceproviderkeywordlist ADD CONSTRAINT nnc_17376_2_not_null CHECK (keyword IS NOT NULL);
ALTER TABLE
    spatial_ref_sys ADD CONSTRAINT nnc_16794_1_not_null CHECK (srid IS NOT NULL);
ALTER TABLE
    srs ADD CONSTRAINT fk1bdf4aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    srs ADD CONSTRAINT nnc_17380_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    srs ADD CONSTRAINT nnc_17380_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    style ADD CONSTRAINT fk68b1db1aa303ad5 FOREIGN KEY (layerid) REFERENCES layer (id);
ALTER TABLE
    style ADD CONSTRAINT nnc_17383_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    style ADD CONSTRAINT nnc_17383_2_not_null CHECK (layerid IS NOT NULL);
ALTER TABLE
    style ADD CONSTRAINT nnc_17383_3_not_null CHECK (name IS NOT NULL);
ALTER TABLE
    style ADD CONSTRAINT nnc_17383_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE
    styledomainformat ADD CONSTRAINT fk1ac945ecf79e2f77 FOREIGN KEY (sdrid) REFERENCES styledomainresource (id);
ALTER TABLE
    styledomainformat ADD CONSTRAINT nnc_17391_1_not_null CHECK (sdrid IS NOT NULL);
ALTER TABLE
    styledomainformat ADD CONSTRAINT nnc_17391_2_not_null CHECK (format IS NOT NULL);
ALTER TABLE
    styledomainresource ADD CONSTRAINT fk5cf968c33d563395 FOREIGN KEY (styleid) REFERENCES style (id);
ALTER TABLE
    styledomainresource ADD CONSTRAINT nnc_17394_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    styledomainresource ADD CONSTRAINT nnc_17394_2_not_null CHECK (styleid IS NOT NULL);
ALTER TABLE
    styledomainresource ADD CONSTRAINT nnc_17394_3_not_null CHECK (domain IS NOT NULL);
ALTER TABLE
    styledomainresource ADD CONSTRAINT nnc_17394_4_not_null CHECK (url IS NOT NULL);
ALTER TABLE
    userip ADD CONSTRAINT fkce2b32326bcbfe49 FOREIGN KEY (userid) REFERENCES users (id);
ALTER TABLE
    userip ADD CONSTRAINT nnc_17400_1_not_null CHECK (userid IS NOT NULL);
ALTER TABLE
    userip ADD CONSTRAINT nnc_17400_2_not_null CHECK (ipaddress IS NOT NULL);
ALTER TABLE
    userroles ADD CONSTRAINT fk154649d26bcbfe49 FOREIGN KEY (userid) REFERENCES users (id);
ALTER TABLE
    userroles ADD CONSTRAINT fk154649d275e26a26 FOREIGN KEY (roleid) REFERENCES roles (id);
ALTER TABLE
    userroles ADD CONSTRAINT nnc_17403_1_not_null CHECK (userid IS NOT NULL);
ALTER TABLE
    userroles ADD CONSTRAINT nnc_17403_2_not_null CHECK (roleid IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT fk6a68e08c136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_2_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_3_not_null CHECK (firstname IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_4_not_null CHECK (lastname IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_5_not_null CHECK (emailaddress IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_6_not_null CHECK (username IS NOT NULL);
ALTER TABLE
    users ADD CONSTRAINT nnc_17406_7_not_null CHECK (password IS NOT NULL);
ALTER TABLE
    wfs_layer ADD CONSTRAINT fkbb3050d66a0442a7 FOREIGN KEY (wfsserviceproviderid) REFERENCES wfs_serviceprovider (id);
ALTER TABLE
    wfs_layer ADD CONSTRAINT nnc_17417_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    wfs_layer ADD CONSTRAINT nnc_17417_2_not_null CHECK (wfsserviceproviderid IS NOT NULL);
ALTER TABLE
    wfs_layer ADD CONSTRAINT nnc_17417_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE
    wfs_organizationlayer ADD CONSTRAINT fk49308c0311ca431d FOREIGN KEY (wfslayerid) REFERENCES wfs_layer (id);
ALTER TABLE
    wfs_organizationlayer ADD CONSTRAINT fk49308c03c136f19 FOREIGN KEY (organizationid) REFERENCES organization (id);
ALTER TABLE
    wfs_organizationlayer ADD CONSTRAINT nnc_17424_1_not_null CHECK (organizationid IS NOT NULL);
ALTER TABLE
    wfs_organizationlayer ADD CONSTRAINT nnc_17424_2_not_null CHECK (wfslayerid IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_1_not_null CHECK (id IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_2_not_null CHECK (name IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_3_not_null CHECK (abbr IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_4_not_null CHECK (title IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_5_not_null CHECK (givenname IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_6_not_null CHECK (url IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_7_not_null CHECK (updateddate IS NOT NULL);
ALTER TABLE
    wfs_serviceprovider ADD CONSTRAINT nnc_17427_8_not_null CHECK (wfsversion IS NOT NULL);