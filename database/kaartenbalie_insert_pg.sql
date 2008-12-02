INSERT 
INTO 
    organization VALUES 
    (
        1,
        'Beheerders',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
         NULL,
       '1234567890',
        NULL,
        true,
        NULL,
        NULL,
        false
    )
    ; 
    
select setval('organization_id_seq', (select max(id) from organization));

INSERT 
INTO 
    acc_account VALUES 
    (
        1,
        '0.00'
    )
    ; 
INSERT 
INTO 
    roles VALUES 
    (
        1,
        'beheerder'
    )
    ,
    (
        2,
        'organisatiebeheerder'
    )
    ,
    (
        3,
        'themabeheerder'
    )
    ,
    (
        4,
        'gebruiker'
    )
    ,
    (
        5,
        'demogebruiker'
    )
    ; 
select setval('roles_id_seq', (select max(id) from roles));
INSERT 
INTO 
    users VALUES 
    (
        1,1,
        'beheerder',
        'beheerder',
        'info@b3partners.nl',
        'beheerder',
        'JMzUf6QkCdc%3D',
        'http://www.kaartenbalie.nl/kaartenbalie/services/a0b3d6ef13be91ff42b89b33f116b823',
        '2009-01-01 00:00:00',
        'http://localhost:8084/kaartenbalie/wms/f9530b2d908a3494950a246ca309c6ba?VERSION=1.1.1&REQUEST=GetMap&LAYERS=demo_bron_nieuwekaart,demo_buurten_2006,demo_plan_lijnen,demo_plan_polygonen,demo_gemeenten_2006,demo_wijken_2006&BBOX=12000,304000,280000,620000&SRS=EPSG:25832&HEIGHT=400&WIDTH=300&FORMAT=image/gif&BGCOLOR=0xF0F0F0&EXCEPTIONS=application/vnd.ogc.se_inimage&STYLES='
    )
    ; 
select setval('users_id_seq', (select max(id) from users));

INSERT 
INTO 
    userip VALUES 
    (
        1,
        '213.84.161.200'
    )
    ; 
INSERT 
INTO 
    userroles VALUES 
    (
        1,1
    )
    ,
    (
        1,2
    )
    ,
    (
        1,3
    )
    ,
    (
        1,4
    )
    ; 