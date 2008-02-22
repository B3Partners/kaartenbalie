@echo off
echo mysql -u%1 -p%2 -t %3 < %4
mysql -u%1 -p%2 -t %3 < %4

echo mysql -u%1 -p%2 -t %3 "08 EnableReporting.sql"
mysql -u%1 -p%2 -t %3 < "08 EnableReporting.sql"

echo mysql -u%1 -p%2 -t %3 "09 Account.sql"
mysql -u%1 -p%2 -t %3 < "09 Account.sql"

echo mysql -u%1 -p%2 -t %3 "10 OrganizationCode.sql"
mysql -u%1 -p%2 -t %3 < "10 OrganizationCode.sql"

echo mysql -u%1 -p%2 -t %3 "11 AccountingUpdate.sql"
mysql -u%1 -p%2 -t %3 < "11 AccountingUpdate.sql"

echo mysql -u%1 -p%2 -t %3 "12 ReportingClientRequest.sql"
mysql -u%1 -p%2 -t %3 < "12 ReportingClientRequest.sql"

echo mysql -u%1 -p%2 -t %3 "13 ReportingOwningCompanyFix.sql"
mysql -u%1 -p%2 -t %3 < "13 ReportingOwningCompanyFix.sql"

echo mysql -u%1 -p%2 -t %3 "14 LayerPricingUpdate.sql"
mysql -u%1 -p%2 -t %3 < "14 LayerPricingUpdate.sql"

echo mysql -u%1 -p%2 -t %3 "15 ExtensionForClientRequest.sql"
mysql -u%1 -p%2 -t %3 < "15 ExtensionForClientRequest.sql"

echo mysql -u%1 -p%2 -t %3 "16 LayerPriceComposition.sql"
mysql -u%1 -p%2 -t %3 < "16 LayerPriceComposition.sql"

echo mysql -u%1 -p%2 -t %3 "17 TheWholeWorldIsForFree.sql"
mysql -u%1 -p%2 -t %3 < "17 TheWholeWorldIsForFree.sql"

echo mysql -u%1 -p%2 -t %3 "18 DataMonitoringLogErrors.sql"
mysql -u%1 -p%2 -t %3 < "18 DataMonitoringLogErrors.sql"

echo mysql -u%1 -p%2 -t %3 "19 minMaxScaleForLayerPricing.sql"
mysql -u%1 -p%2 -t %3 < "19 minMaxScaleForLayerPricing.sql"

echo mysql -u%1 -p%2 -t %3 "20 ReportingUpdate.sql"
mysql -u%1 -p%2 -t %3 < "20 ReportingUpdate.sql"

echo mysql -u%1 -p%2 -t %3 "21 LayerPricingProjection.sql"
mysql -u%1 -p%2 -t %3 < "21 LayerPricingProjection.sql"