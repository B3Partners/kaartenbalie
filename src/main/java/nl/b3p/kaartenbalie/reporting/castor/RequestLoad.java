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
 * Class RequestLoad.
 * 
 * @version $Revision$ $Date$
 */
public class RequestLoad implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _hourlyLoadList
     */
    private java.util.ArrayList _hourlyLoadList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RequestLoad() 
     {
        super();
        _hourlyLoadList = new java.util.ArrayList();
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestLoad()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addHourlyLoad
     * 
     * 
     * 
     * @param vHourlyLoad
     */
    public void addHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad vHourlyLoad)
        throws java.lang.IndexOutOfBoundsException
    {
        _hourlyLoadList.add(vHourlyLoad);
    } //-- void addHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) 

    /**
     * Method addHourlyLoad
     * 
     * 
     * 
     * @param index
     * @param vHourlyLoad
     */
    public void addHourlyLoad(int index, nl.b3p.kaartenbalie.reporting.castor.HourlyLoad vHourlyLoad)
        throws java.lang.IndexOutOfBoundsException
    {
        _hourlyLoadList.add(index, vHourlyLoad);
    } //-- void addHourlyLoad(int, nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) 

    /**
     * Method clearHourlyLoad
     * 
     */
    public void clearHourlyLoad()
    {
        _hourlyLoadList.clear();
    } //-- void clearHourlyLoad() 

    /**
     * Method enumerateHourlyLoad
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateHourlyLoad()
    {
        return Collections.enumeration(_hourlyLoadList);
    } //-- java.util.Enumeration enumerateHourlyLoad() 

    /**
     * Method getHourlyLoad
     * 
     * 
     * 
     * @param index
     * @return HourlyLoad
     */
    public nl.b3p.kaartenbalie.reporting.castor.HourlyLoad getHourlyLoad(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _hourlyLoadList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) _hourlyLoadList.get(index);
    } //-- nl.b3p.kaartenbalie.reporting.castor.HourlyLoad getHourlyLoad(int) 

    /**
     * Method getHourlyLoad
     * 
     * 
     * 
     * @return HourlyLoad
     */
    public nl.b3p.kaartenbalie.reporting.castor.HourlyLoad[] getHourlyLoad()
    {
        int size = _hourlyLoadList.size();
        nl.b3p.kaartenbalie.reporting.castor.HourlyLoad[] mArray = new nl.b3p.kaartenbalie.reporting.castor.HourlyLoad[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) _hourlyLoadList.get(index);
        }
        return mArray;
    } //-- nl.b3p.kaartenbalie.reporting.castor.HourlyLoad[] getHourlyLoad() 

    /**
     * Method getHourlyLoadCount
     * 
     * 
     * 
     * @return int
     */
    public int getHourlyLoadCount()
    {
        return _hourlyLoadList.size();
    } //-- int getHourlyLoadCount() 

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
     * Method removeHourlyLoad
     * 
     * 
     * 
     * @param vHourlyLoad
     * @return boolean
     */
    public boolean removeHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad vHourlyLoad)
    {
        boolean removed = _hourlyLoadList.remove(vHourlyLoad);
        return removed;
    } //-- boolean removeHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) 

    /**
     * Method setHourlyLoad
     * 
     * 
     * 
     * @param index
     * @param vHourlyLoad
     */
    public void setHourlyLoad(int index, nl.b3p.kaartenbalie.reporting.castor.HourlyLoad vHourlyLoad)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _hourlyLoadList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _hourlyLoadList.set(index, vHourlyLoad);
    } //-- void setHourlyLoad(int, nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) 

    /**
     * Method setHourlyLoad
     * 
     * 
     * 
     * @param hourlyLoadArray
     */
    public void setHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad[] hourlyLoadArray)
    {
        //-- copy array
        _hourlyLoadList.clear();
        for (int i = 0; i < hourlyLoadArray.length; i++) {
            _hourlyLoadList.add(hourlyLoadArray[i]);
        }
    } //-- void setHourlyLoad(nl.b3p.kaartenbalie.reporting.castor.HourlyLoad) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return RequestLoad
     */
    public static nl.b3p.kaartenbalie.reporting.castor.RequestLoad unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.RequestLoad) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.RequestLoad.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestLoad unmarshal(java.io.Reader) 

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
