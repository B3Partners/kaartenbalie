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
 * Class ResponseFrequency.
 * 
 * @version $Revision$ $Date$
 */
public class ResponseFrequency implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _responseTimeList
     */
    private java.util.ArrayList _responseTimeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResponseFrequency() 
     {
        super();
        _responseTimeList = new java.util.ArrayList();
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResponseTime
     * 
     * 
     * 
     * @param vResponseTime
     */
    public void addResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime vResponseTime)
        throws java.lang.IndexOutOfBoundsException
    {
        _responseTimeList.add(vResponseTime);
    } //-- void addResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Method addResponseTime
     * 
     * 
     * 
     * @param index
     * @param vResponseTime
     */
    public void addResponseTime(int index, nl.b3p.kaartenbalie.reporting.castor.ResponseTime vResponseTime)
        throws java.lang.IndexOutOfBoundsException
    {
        _responseTimeList.add(index, vResponseTime);
    } //-- void addResponseTime(int, nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Method clearResponseTime
     * 
     */
    public void clearResponseTime()
    {
        _responseTimeList.clear();
    } //-- void clearResponseTime() 

    /**
     * Method enumerateResponseTime
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateResponseTime()
    {
        return Collections.enumeration(_responseTimeList);
    } //-- java.util.Enumeration enumerateResponseTime() 

    /**
     * Method getResponseTime
     * 
     * 
     * 
     * @param index
     * @return ResponseTime
     */
    public nl.b3p.kaartenbalie.reporting.castor.ResponseTime getResponseTime(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _responseTimeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (nl.b3p.kaartenbalie.reporting.castor.ResponseTime) _responseTimeList.get(index);
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseTime getResponseTime(int) 

    /**
     * Method getResponseTime
     * 
     * 
     * 
     * @return ResponseTime
     */
    public nl.b3p.kaartenbalie.reporting.castor.ResponseTime[] getResponseTime()
    {
        int size = _responseTimeList.size();
        nl.b3p.kaartenbalie.reporting.castor.ResponseTime[] mArray = new nl.b3p.kaartenbalie.reporting.castor.ResponseTime[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (nl.b3p.kaartenbalie.reporting.castor.ResponseTime) _responseTimeList.get(index);
        }
        return mArray;
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseTime[] getResponseTime() 

    /**
     * Method getResponseTimeCount
     * 
     * 
     * 
     * @return int
     */
    public int getResponseTimeCount()
    {
        return _responseTimeList.size();
    } //-- int getResponseTimeCount() 

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
     * Method removeResponseTime
     * 
     * 
     * 
     * @param vResponseTime
     * @return boolean
     */
    public boolean removeResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime vResponseTime)
    {
        boolean removed = _responseTimeList.remove(vResponseTime);
        return removed;
    } //-- boolean removeResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Method setResponseTime
     * 
     * 
     * 
     * @param index
     * @param vResponseTime
     */
    public void setResponseTime(int index, nl.b3p.kaartenbalie.reporting.castor.ResponseTime vResponseTime)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _responseTimeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _responseTimeList.set(index, vResponseTime);
    } //-- void setResponseTime(int, nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Method setResponseTime
     * 
     * 
     * 
     * @param responseTimeArray
     */
    public void setResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime[] responseTimeArray)
    {
        //-- copy array
        _responseTimeList.clear();
        for (int i = 0; i < responseTimeArray.length; i++) {
            _responseTimeList.add(responseTimeArray[i]);
        }
    } //-- void setResponseTime(nl.b3p.kaartenbalie.reporting.castor.ResponseTime) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ResponseFrequency
     */
    public static nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency unmarshal(java.io.Reader) 

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
