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
 * Class Parameters.
 * 
 * @version $Revision$ $Date$
 */
public class Parameters implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _organization
     */
    private java.lang.String _organization;

    /**
     * Field _timeStamp
     */
    private java.util.Date _timeStamp;

    /**
     * Field _processingTime
     */
    private long _processingTime;

    /**
     * keeps track of state for field: _processingTime
     */
    private boolean _has_processingTime;

    /**
     * Field _dateStart
     */
    private org.exolab.castor.types.Date _dateStart;

    /**
     * Field _dateEnd
     */
    private org.exolab.castor.types.Date _dateEnd;


      //----------------/
     //- Constructors -/
    //----------------/

    public Parameters() 
     {
        super();
    } //-- nl.b3p.kaartenbalie.reporting.castor.Parameters()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteProcessingTime
     * 
     */
    public void deleteProcessingTime()
    {
        this._has_processingTime= false;
    } //-- void deleteProcessingTime() 

    /**
     * Returns the value of field 'dateEnd'.
     * 
     * @return Date
     * @return the value of field 'dateEnd'.
     */
    public org.exolab.castor.types.Date getDateEnd()
    {
        return this._dateEnd;
    } //-- org.exolab.castor.types.Date getDateEnd() 

    /**
     * Returns the value of field 'dateStart'.
     * 
     * @return Date
     * @return the value of field 'dateStart'.
     */
    public org.exolab.castor.types.Date getDateStart()
    {
        return this._dateStart;
    } //-- org.exolab.castor.types.Date getDateStart() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return String
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'organization'.
     * 
     * @return String
     * @return the value of field 'organization'.
     */
    public java.lang.String getOrganization()
    {
        return this._organization;
    } //-- java.lang.String getOrganization() 

    /**
     * Returns the value of field 'processingTime'.
     * 
     * @return long
     * @return the value of field 'processingTime'.
     */
    public long getProcessingTime()
    {
        return this._processingTime;
    } //-- long getProcessingTime() 

    /**
     * Returns the value of field 'timeStamp'.
     * 
     * @return Date
     * @return the value of field 'timeStamp'.
     */
    public java.util.Date getTimeStamp()
    {
        return this._timeStamp;
    } //-- java.util.Date getTimeStamp() 

    /**
     * Method hasProcessingTime
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasProcessingTime()
    {
        return this._has_processingTime;
    } //-- boolean hasProcessingTime() 

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
     * Sets the value of field 'dateEnd'.
     * 
     * @param dateEnd the value of field 'dateEnd'.
     */
    public void setDateEnd(org.exolab.castor.types.Date dateEnd)
    {
        this._dateEnd = dateEnd;
    } //-- void setDateEnd(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'dateStart'.
     * 
     * @param dateStart the value of field 'dateStart'.
     */
    public void setDateStart(org.exolab.castor.types.Date dateStart)
    {
        this._dateStart = dateStart;
    } //-- void setDateStart(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Sets the value of field 'organization'.
     * 
     * @param organization the value of field 'organization'.
     */
    public void setOrganization(java.lang.String organization)
    {
        this._organization = organization;
    } //-- void setOrganization(java.lang.String) 

    /**
     * Sets the value of field 'processingTime'.
     * 
     * @param processingTime the value of field 'processingTime'.
     */
    public void setProcessingTime(long processingTime)
    {
        this._processingTime = processingTime;
        this._has_processingTime = true;
    } //-- void setProcessingTime(long) 

    /**
     * Sets the value of field 'timeStamp'.
     * 
     * @param timeStamp the value of field 'timeStamp'.
     */
    public void setTimeStamp(java.util.Date timeStamp)
    {
        this._timeStamp = timeStamp;
    } //-- void setTimeStamp(java.util.Date) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Parameters
     */
    public static nl.b3p.kaartenbalie.reporting.castor.Parameters unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.Parameters) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.Parameters.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.Parameters unmarshal(java.io.Reader) 

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
