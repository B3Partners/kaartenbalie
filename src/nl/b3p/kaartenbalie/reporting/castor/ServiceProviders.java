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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ServiceProviders.
 * 
 * @version $Revision$ $Date$
 */
public class ServiceProviders implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _serviceProviderList
     */
    private java.util.ArrayList _serviceProviderList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServiceProviders() 
     {
        super();
        _serviceProviderList = new ArrayList();
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProviders()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addServiceProvider
     * 
     * 
     * 
     * @param vServiceProvider
     */
    public void addServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceProviderList.add(vServiceProvider);
    } //-- void addServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) 

    /**
     * Method addServiceProvider
     * 
     * 
     * 
     * @param index
     * @param vServiceProvider
     */
    public void addServiceProvider(int index, nl.b3p.kaartenbalie.reporting.castor.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceProviderList.add(index, vServiceProvider);
    } //-- void addServiceProvider(int, nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) 

    /**
     * Method clearServiceProvider
     * 
     */
    public void clearServiceProvider()
    {
        _serviceProviderList.clear();
    } //-- void clearServiceProvider() 

    /**
     * Method enumerateServiceProvider
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateServiceProvider()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_serviceProviderList.iterator());
    } //-- java.util.Enumeration enumerateServiceProvider() 

    /**
     * Method getServiceProvider
     * 
     * 
     * 
     * @param index
     * @return ServiceProvider
     */
    public nl.b3p.kaartenbalie.reporting.castor.ServiceProvider getServiceProvider(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _serviceProviderList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) _serviceProviderList.get(index);
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProvider getServiceProvider(int) 

    /**
     * Method getServiceProvider
     * 
     * 
     * 
     * @return ServiceProvider
     */
    public nl.b3p.kaartenbalie.reporting.castor.ServiceProvider[] getServiceProvider()
    {
        int size = _serviceProviderList.size();
        nl.b3p.kaartenbalie.reporting.castor.ServiceProvider[] mArray = new nl.b3p.kaartenbalie.reporting.castor.ServiceProvider[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) _serviceProviderList.get(index);
        }
        return mArray;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProvider[] getServiceProvider() 

    /**
     * Method getServiceProviderCount
     * 
     * 
     * 
     * @return int
     */
    public int getServiceProviderCount()
    {
        return _serviceProviderList.size();
    } //-- int getServiceProviderCount() 

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
     * Method removeServiceProvider
     * 
     * 
     * 
     * @param vServiceProvider
     * @return boolean
     */
    public boolean removeServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider vServiceProvider)
    {
        boolean removed = _serviceProviderList.remove(vServiceProvider);
        return removed;
    } //-- boolean removeServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) 

    /**
     * Method setServiceProvider
     * 
     * 
     * 
     * @param index
     * @param vServiceProvider
     */
    public void setServiceProvider(int index, nl.b3p.kaartenbalie.reporting.castor.ServiceProvider vServiceProvider)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _serviceProviderList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _serviceProviderList.set(index, vServiceProvider);
    } //-- void setServiceProvider(int, nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) 

    /**
     * Method setServiceProvider
     * 
     * 
     * 
     * @param serviceProviderArray
     */
    public void setServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider[] serviceProviderArray)
    {
        //-- copy array
        _serviceProviderList.clear();
        for (int i = 0; i < serviceProviderArray.length; i++) {
            _serviceProviderList.add(serviceProviderArray[i]);
        }
    } //-- void setServiceProvider(nl.b3p.kaartenbalie.reporting.castor.ServiceProvider) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ServiceProviders
     */
    public static nl.b3p.kaartenbalie.reporting.castor.ServiceProviders unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.ServiceProviders) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.ServiceProviders.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.ServiceProviders unmarshal(java.io.Reader) 

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
