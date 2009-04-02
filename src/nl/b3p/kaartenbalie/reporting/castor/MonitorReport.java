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

import java.util.Collections;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

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
     * Field _serviceList
     */
    private java.util.ArrayList _serviceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MonitorReport() 
     {
        super();
        _serviceList = new java.util.ArrayList();
    } //-- nl.b3p.kaartenbalie.reporting.castor.MonitorReport()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addService
     * 
     * 
     * 
     * @param vService
     */
    public void addService(nl.b3p.kaartenbalie.reporting.castor.Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceList.add(vService);
    } //-- void addService(nl.b3p.kaartenbalie.reporting.castor.Service) 

    /**
     * Method addService
     * 
     * 
     * 
     * @param index
     * @param vService
     */
    public void addService(int index, nl.b3p.kaartenbalie.reporting.castor.Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        _serviceList.add(index, vService);
    } //-- void addService(int, nl.b3p.kaartenbalie.reporting.castor.Service) 

    /**
     * Method clearService
     * 
     */
    public void clearService()
    {
        _serviceList.clear();
    } //-- void clearService() 

    /**
     * Method enumerateService
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateService()
    {
        return Collections.enumeration(_serviceList);
    } //-- java.util.Enumeration enumerateService() 

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
     * Method getService
     * 
     * 
     * 
     * @param index
     * @return Service
     */
    public nl.b3p.kaartenbalie.reporting.castor.Service getService(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _serviceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (nl.b3p.kaartenbalie.reporting.castor.Service) _serviceList.get(index);
    } //-- nl.b3p.kaartenbalie.reporting.castor.Service getService(int) 

    /**
     * Method getService
     * 
     * 
     * 
     * @return Service
     */
    public nl.b3p.kaartenbalie.reporting.castor.Service[] getService()
    {
        int size = _serviceList.size();
        nl.b3p.kaartenbalie.reporting.castor.Service[] mArray = new nl.b3p.kaartenbalie.reporting.castor.Service[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (nl.b3p.kaartenbalie.reporting.castor.Service) _serviceList.get(index);
        }
        return mArray;
    } //-- nl.b3p.kaartenbalie.reporting.castor.Service[] getService() 

    /**
     * Method getServiceCount
     * 
     * 
     * 
     * @return int
     */
    public int getServiceCount()
    {
        return _serviceList.size();
    } //-- int getServiceCount() 

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
     * Method removeService
     * 
     * 
     * 
     * @param vService
     * @return boolean
     */
    public boolean removeService(nl.b3p.kaartenbalie.reporting.castor.Service vService)
    {
        boolean removed = _serviceList.remove(vService);
        return removed;
    } //-- boolean removeService(nl.b3p.kaartenbalie.reporting.castor.Service) 

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
     * Method setService
     * 
     * 
     * 
     * @param index
     * @param vService
     */
    public void setService(int index, nl.b3p.kaartenbalie.reporting.castor.Service vService)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _serviceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _serviceList.set(index, vService);
    } //-- void setService(int, nl.b3p.kaartenbalie.reporting.castor.Service) 

    /**
     * Method setService
     * 
     * 
     * 
     * @param serviceArray
     */
    public void setService(nl.b3p.kaartenbalie.reporting.castor.Service[] serviceArray)
    {
        //-- copy array
        _serviceList.clear();
        for (int i = 0; i < serviceArray.length; i++) {
            _serviceList.add(serviceArray[i]);
        }
    } //-- void setService(nl.b3p.kaartenbalie.reporting.castor.Service) 

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
