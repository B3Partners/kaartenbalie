package nl.b3p.kaartenbalie.struts;

import java.io.IOException;
import org.xml.sax.SAXException;
//import kaartenbaliecore.beans.*;
import nl.b3p.kaartenbalie.core.server.*;
import java.net.URL;

public class Test {
	private ServiceProvider serviceProvider;
	private URL url;
	
	public Test() {
		/*
		 * Several locations of WMS servers:
		 */
		 
		//String location = "wmt";
		String location = "http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities";
		//String location = "http://viz.globe.gov/viz-bin/wmt.cgi?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities";
		//String location = "http://maps1.intergraph.com/wms/world/request.asp?REQUEST=GetCapabilities";
		//String location = "http://columbo.nrlssc.navy.mil/ogcwms/servlet/WMSServlet/Newport_Beach_CA_Maps.wms?SERVICE=WMS&REQUEST=GetCapabilities";
		//String location = "newport.wms";
				
		
		serviceProvider = new ServiceProvider();
		WMSCapabilitiesReader wms = new WMSCapabilitiesReader(serviceProvider);
		try {
			serviceProvider = wms.getProvider(location);
			//wmt = wms.getWMT_MS(url);
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (SAXException saxe) {
			System.out.println(saxe);
		}
		
		/*
    	 * Test case if elements in the object are correctly saved
    	 */    	    	
    	if (null != serviceProvider) {
    		System.out.println(serviceProvider);
		}
	}
	
	public static void main(String [] args) {
		Test test = new Test();
	}
}