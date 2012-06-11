/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2012,2013,2014 B3Partners BV
 * 
 * author : Rachelle Scheijen
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */


package general;
import nl.b3p.kaartenbalie.core.server.*;
import nl.b3p.kaartenbalie.core.server.accounting.*;
import nl.b3p.kaartenbalie.core.server.accounting.entity.*;
import nl.b3p.kaartenbalie.core.server.b3pLayering.*;
import nl.b3p.kaartenbalie.core.server.monitoring.*;
import nl.b3p.kaartenbalie.reporting.*;
import nl.b3p.kaartenbalie.reporting.castor.*;
import nl.b3p.kaartenbalie.service.*;
import nl.b3p.kaartenbalie.service.requesthandler.*;
import nl.b3p.kaartenbalie.service.servlet.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    FlamingoMapTest.class, OrganizationTest.class,UserTest.class,
    AccountManagerTest.class, ExtLayerCalculatorTest.class, LayerCalculatorTest.class,
    AccountTest.class,LayerPriceCompositionTest.class, LayerPricingTest.class, TransactionTest.class,
    AllowTransactionsLayerTest.class,BalanceLayerTest.class, ExceptionLayerTest.class, KBTitleLayerTest.class,
    ClientRequestTest.class, DataMonitoringTest.class,
    CastorXmlTransformerTest.class, ReportTest.class,
    HourlyLoadDescriptorTest.class, HourlyLoadTest.class,
    KBImageToolTest.class, LayerTreeSupportTest.class, LayerValidatorTest.class, MapParserTest.class, SecurityRealmTest.class, ServiceProviderValidatorTest.class,
    DOMValidatorTest.class, DataWrapperTest.class, DescribeLayerRequestHandlerTest.class, GetCapabilitiesRequestHandlerTest.class, GetFeatureInfoRequestHandlerTest.class, TextToImageTest.class,
    CallWMSServletTest.class,ProxySLDServletTest.class, SelectCSSTest.class
})
public class AllTests {
    // Empty, since the annotations include all of the necessary configuration
}
