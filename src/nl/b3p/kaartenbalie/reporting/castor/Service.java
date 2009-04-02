/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.2</a>, using an XML
 * Schema.
 * $Id$
 */

package nl.b3p.kaartenbalie.reporting.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Service.
 * 
 * @version $Revision$ $Date$
 */
public class Service implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _requestSummary
     */
    private nl.b3p.kaartenbalie.reporting.castor.RequestSummary _requestSummary;

    /**
     * Field _responseFrequency
     */
    private nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency _responseFrequency;

    /**
     * Field _serviceProviders
     */
    private nl.b3p.kaartenbalie.reporting.castor.ServiceProviders _serviceProviders;

    /**
     * Field _requestLoad
     */
    private nl.b3p.kaartenbalie.reporting.castor.RequestLoad _requestLoad;


      //----------------/
     //- Constructors -/
    //----------------/

    public Service() 
     {
        super();
    } //-- nl.b3p.kaartenbalie.reporting.castor.Service()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'requestLoad'.
     * 
     * @return RequestLoad
     * @return the value of field 'requestLoad'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.RequestLoad getRequestLoad()
    {
        return this._requestLoad;
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestLoad getRequestLoad() 

    /**
     * Returns the value of field 'requestSummary'.
     * 
     * @return RequestSummary
     * @return the value of field 'requestSummary'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.RequestSummary getRequestSummary()
    {
        return this._requestSummary;
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestSummary getRequestSummary() 

    /**
     * Returns the value of field 'responseFrequency'.
     * 
     * @return ResponseFrequency
     * @return the value of field 'responseFrequency'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency getResponseFrequency()
    {
        return this._responseFrequency;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency getResponseFrequency() 

    /**
     * Returns the value of field 'serviceProviders'.
     * 
     * @return ServiceProviders
     * @return the value of field 'serviceProviders'.
     */
    public nl.b3p.kaartenbalie.reporting.castor.ServiceProviders getServiceProviders()
    {
        return this._serviceProviders;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProviders getServiceProviders() 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'requestLoad'.
     * 
     * @param requestLoad the value of field 'requestLoad'.
     */
    public void setRequestLoad(nl.b3p.kaartenbalie.reporting.castor.RequestLoad requestLoad)
    {
        this._requestLoad = requestLoad;
    } //-- void setRequestLoad(nl.b3p.kaartenbalie.reporting.castor.RequestLoad) 

    /**
     * Sets the value of field 'requestSummary'.
     * 
     * @param requestSummary the value of field 'requestSummary'.
     */
    public void setRequestSummary(nl.b3p.kaartenbalie.reporting.castor.RequestSummary requestSummary)
    {
        this._requestSummary = requestSummary;
    } //-- void setRequestSummary(nl.b3p.kaartenbalie.reporting.castor.RequestSummary) 

    /**
     * Sets the value of field 'responseFrequency'.
     * 
     * @param responseFrequency the value of field
     * 'responseFrequency'.
     */
    public void setResponseFrequency(nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency responseFrequency)
    {
        this._responseFrequency = responseFrequency;
    } //-- void setResponseFrequency(nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency) 

    /**
     * Sets the value of field 'serviceProviders'.
     * 
     * @param serviceProviders the value of field 'serviceProviders'
     */
    public void setServiceProviders(nl.b3p.kaartenbalie.reporting.castor.ServiceProviders serviceProviders)
    {
        this._serviceProviders = serviceProviders;
    } //-- void setServiceProviders(nl.b3p.kaartenbalie.reporting.castor.ServiceProviders) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Service
     */
    public static nl.b3p.kaartenbalie.reporting.castor.Service unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.Service) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.Service.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.Service unmarshal(java.io.Reader) 

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
