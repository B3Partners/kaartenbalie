DELETE FROM `acc_layerpricing`;

INSERT INTO `acc_layerpricing` (lpr_layerName, lpr_planType, lpr_creationDate, lpr_layerIsFree,
lpr_serverProviderPrefix, lpr_service, lpr_operation, lpr_indexCount)
SELECT layer.NAME AS lpr_layerName,
'1' AS lpr_planType,
now() as lpr_creationDate,
true as lpr_layerIsFree,
IF(serviceprovider.abbr = null,serviceprovider.SERVICEPROVIDERID,serviceprovider.abbr) as lpr_serverProviderPrefix,
'WMS' as lpr_service,
'GetMap' as lpr_operation,
'0' as lpr_indexCount
FROM Layer AS layer LEFT JOIN serviceprovider ON layer.SERVICEPROVIDERID = serviceprovider.SERVICEPROVIDERID WHERE layer.name is not null;

INSERT INTO `acc_layerpricing` (lpr_layerName, lpr_planType, lpr_creationDate, lpr_layerIsFree,
lpr_serverProviderPrefix, lpr_service, lpr_operation, lpr_indexCount)
SELECT layer.NAME AS lpr_layerName,
'1' AS lpr_planType,
now() as lpr_creationDate,
true as lpr_layerIsFree,
IF(serviceprovider.abbr = null,serviceprovider.SERVICEPROVIDERID,serviceprovider.abbr) as lpr_serverProviderPrefix,
'WMS' as lpr_service,
'GetFeatureInfo' as lpr_operation,
'0' as lpr_indexCount
FROM Layer AS layer LEFT JOIN serviceprovider ON layer.SERVICEPROVIDERID = serviceprovider.SERVICEPROVIDERID WHERE layer.name is not null;

INSERT INTO `acc_layerpricing` (lpr_layerName, lpr_planType, lpr_creationDate, lpr_layerIsFree,
lpr_serverProviderPrefix, lpr_service, lpr_operation, lpr_indexCount)
SELECT layer.NAME AS lpr_layerName,
'1' AS lpr_planType,
now() as lpr_creationDate,
true as lpr_layerIsFree,
IF(serviceprovider.abbr = null,serviceprovider.SERVICEPROVIDERID,serviceprovider.abbr) as lpr_serverProviderPrefix,
'WMS' as lpr_service,
'GetLegendGraphic' as lpr_operation,
'0' as lpr_indexCount
FROM Layer AS layer LEFT JOIN serviceprovider ON layer.SERVICEPROVIDERID = serviceprovider.SERVICEPROVIDERID WHERE layer.name is not null;



DELETE FROM `kb_uniquenumber` WHERE unn_indexName = 'LAYERPRICING';
INSERT INTO `kb_uniquenumber` (unn_indexName, unn_indexCount) VALUES ('LAYERPRICING','0');