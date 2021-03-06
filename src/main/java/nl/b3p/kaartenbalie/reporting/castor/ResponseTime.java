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
 * Class ResponseTime.
 * 
 * @version $Revision$ $Date$
 */
public class ResponseTime implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _durationLow
     */
    private long _durationLow;

    /**
     * keeps track of state for field: _durationLow
     */
    private boolean _has_durationLow;

    /**
     * Field _durationHigh
     */
    private long _durationHigh;

    /**
     * keeps track of state for field: _durationHigh
     */
    private boolean _has_durationHigh;

    /**
     * Field _count
     */
    private int _count;

    /**
     * keeps track of state for field: _count
     */
    private boolean _has_count;

    /**
     * Field _type
     */
    private java.lang.String _type;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResponseTime() 
     {
        super();
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseTime()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteCount
     * 
     */
    public void deleteCount()
    {
        this._has_count= false;
    } //-- void deleteCount() 

    /**
     * Method deleteDurationHigh
     * 
     */
    public void deleteDurationHigh()
    {
        this._has_durationHigh= false;
    } //-- void deleteDurationHigh() 

    /**
     * Method deleteDurationLow
     * 
     */
    public void deleteDurationLow()
    {
        this._has_durationLow= false;
    } //-- void deleteDurationLow() 

    /**
     * Returns the value of field 'count'.
     * 
     * @return int
     * @return the value of field 'count'.
     */
    public int getCount()
    {
        return this._count;
    } //-- int getCount() 

    /**
     * Returns the value of field 'durationHigh'.
     * 
     * @return long
     * @return the value of field 'durationHigh'.
     */
    public long getDurationHigh()
    {
        return this._durationHigh;
    } //-- long getDurationHigh() 

    /**
     * Returns the value of field 'durationLow'.
     * 
     * @return long
     * @return the value of field 'durationLow'.
     */
    public long getDurationLow()
    {
        return this._durationLow;
    } //-- long getDurationLow() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasCount
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasCount()
    {
        return this._has_count;
    } //-- boolean hasCount() 

    /**
     * Method hasDurationHigh
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDurationHigh()
    {
        return this._has_durationHigh;
    } //-- boolean hasDurationHigh() 

    /**
     * Method hasDurationLow
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDurationLow()
    {
        return this._has_durationLow;
    } //-- boolean hasDurationLow() 

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
     * Sets the value of field 'count'.
     * 
     * @param count the value of field 'count'.
     */
    public void setCount(int count)
    {
        this._count = count;
        this._has_count = true;
    } //-- void setCount(int) 

    /**
     * Sets the value of field 'durationHigh'.
     * 
     * @param durationHigh the value of field 'durationHigh'.
     */
    public void setDurationHigh(long durationHigh)
    {
        this._durationHigh = durationHigh;
        this._has_durationHigh = true;
    } //-- void setDurationHigh(long) 

    /**
     * Sets the value of field 'durationLow'.
     * 
     * @param durationLow the value of field 'durationLow'.
     */
    public void setDurationLow(long durationLow)
    {
        this._durationLow = durationLow;
        this._has_durationLow = true;
    } //-- void setDurationLow(long) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ResponseTime
     */
    public static nl.b3p.kaartenbalie.reporting.castor.ResponseTime unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.ResponseTime) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.ResponseTime.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.ResponseTime unmarshal(java.io.Reader) 

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
