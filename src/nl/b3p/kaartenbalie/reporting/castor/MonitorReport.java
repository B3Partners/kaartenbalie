/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0M2</a>, using an XML
 * Schema.
 * $Id$
 */

package nl.b3p.kaartenbalie.reporting.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * selection of monitoring data for creating reports
 * 
 * @version $Revision$ $Date$
 */
public class MonitorReport implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _parameters
     */
    private nl.b3p.kaartenbalie.reporting.castor.Parameters _parameters;

    /**
     * Field _responseTime
     */
    private nl.b3p.kaartenbalie.reporting.castor.ResponseTime _responseTime;

    /**
     * Field _clientSummary
     */
    private nl.b3p.kaartenbalie.reporting.castor.ClientSummary _clientSummary;

    /**
     * Field _serviceProviderSummary
     */
    private nl.b3p.kaartenbalie.reporting.castor.ServiceProviderSummary _serviceProviderSummary;

    /**
     * Field _hits
     */
    private nl.b3p.kaartenbalie.reporting.castor.Hits _hits;


      //----------------/
     //- Constructors -/
    //----------------/

    public MonitorReport() 
     {
        super();
    } //-- nl.b3p.kaartenbalie.reporting.castor.MonitorReport()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'clientSummary'.
     * 
     * @return ClientSummary
     * @return the value of field 'clientSummary'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.ClientSummary getClientSummary()
    {
        return this._clientSummary;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ClientSummary getClientSummary() 

    /**
     * Returns the value of field 'hits'.
     * 
     * @return Hits
     * @return the value of field 'hits'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.Hits getHits()
    {
        return this._hits;
    } //-- nl.b3p.kaartenbalie.reporting.castor.Hits getHits() 

    /**
     * Returns the value of field 'parameters'.
     * 
     * @return Parameters
     * @return the value of field 'parameters'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.Parameters getParameters()
    {
        return this._parameters;
    } //-- nl.b3p.kaartenbalie.reporting.castor.Parameters getParameters() 

    /**
     * Returns the value of field 'responseTime'.
     * 
     * @return ResponseTime
     * @return the value of field 'responseTime'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.ResponseTime getResponseTime()
    {
        return this._responseTime;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseTime getResponseTime() 

    /**
     * Returns the value of field 'serviceProviderSummary'.
     * 
     * @return ServiceProviderSummary
     * @return the value of field 'serviceProviderSummary'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.ServiceProviderSummary getServiceProviderSummary()
    {
        return this._serviceProviderSummary;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProviderSummary getServiceProviderSummary() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'clientSummary'.
     * 
     * @param clientSummary the value of field 'clientSummary'.
     */
    public void setClientSummary(nl.b3p.kaartenbalie.reporting.castor.ClientSummary clientSummary)
    {
        this._clientSummary = clientSummary;
    } //-- void setClientSummary(nl.b3p.kaartenbalie.reporting.castor.ClientSummary) 

    /**
     * Sets the value of field 'hits'.
     * 
     * @param hits the value of field 'hits'.
     */
    public void setHits(nl.b3p.kaartenbalie.reporting.castor.Hits hits)
    {
        this._hits = hits;
    } //-- void setHits(nl.b3p.kaartenbalie.reporting.castor.Hits) 

    /**
     * Sets the value of field 'parameters'.
     * 
     * @param parameters the value of field 'parameters'.
     */
    public void setParameters(nl.b3p.kaartenbalie.reporting.castor.Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(nl.b3p.kaartenbalie.reporting.castor.Parameters) 

    /**
     * Sets the value of field 'responseTime'.
     * 
     * @param responseTime the value of field 'responseTime'.
     */
    public void setResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime responseTime)
    {
        this._responseTime = responseTime;
    } //-- void setResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Sets the value of field 'serviceProviderSummary'.
     * 
     * @param serviceProviderSummary the value of field
     * 'serviceProviderSummary'.
     */
    public void setServiceProviderSummary(nl.b3p.kaartenbalie.reporting.castor.ServiceProviderSummary serviceProviderSummary)
    {
        this._serviceProviderSummary = serviceProviderSummary;
    } //-- void setServiceProviderSummary(nl.b3p.kaartenbalie.reporting.castor.ServiceProviderSummary) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MonitorReport
     */
    public static nl.b3p.kaartenbalie.reporting.castor.MonitorReport unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.MonitorReport) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.MonitorReport.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.MonitorReport unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
