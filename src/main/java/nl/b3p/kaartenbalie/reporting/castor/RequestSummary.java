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
 * Class RequestSummary.
 * 
 * @version $Revision$ $Date$
 */
public class RequestSummary implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _typeSummaryList
     */
    private java.util.ArrayList _typeSummaryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RequestSummary() 
     {
        super();
        _typeSummaryList = new java.util.ArrayList();
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestSummary()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTypeSummary
     * 
     * 
     * 
     * @param vTypeSummary
     */
    public void addTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary vTypeSummary)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeSummaryList.add(vTypeSummary);
    } //-- void addTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary) 

    /**
     * Method addTypeSummary
     * 
     * 
     * 
     * @param index
     * @param vTypeSummary
     */
    public void addTypeSummary(int index, nl.b3p.kaartenbalie.reporting.castor.TypeSummary vTypeSummary)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeSummaryList.add(index, vTypeSummary);
    } //-- void addTypeSummary(int, nl.b3p.kaartenbalie.reporting.castor.TypeSummary) 

    /**
     * Method clearTypeSummary
     * 
     */
    public void clearTypeSummary()
    {
        _typeSummaryList.clear();
    } //-- void clearTypeSummary() 

    /**
     * Method enumerateTypeSummary
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateTypeSummary()
    {
        return Collections.enumeration(_typeSummaryList);
    } //-- java.util.Enumeration enumerateTypeSummary() 

    /**
     * Method getTypeSummary
     * 
     * 
     * 
     * @param index
     * @return TypeSummary
     */
    public nl.b3p.kaartenbalie.reporting.castor.TypeSummary getTypeSummary(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _typeSummaryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (nl.b3p.kaartenbalie.reporting.castor.TypeSummary) _typeSummaryList.get(index);
    } //-- nl.b3p.kaartenbalie.reporting.castor.TypeSummary getTypeSummary(int) 

    /**
     * Method getTypeSummary
     * 
     * 
     * 
     * @return TypeSummary
     */
    public nl.b3p.kaartenbalie.reporting.castor.TypeSummary[] getTypeSummary()
    {
        int size = _typeSummaryList.size();
        nl.b3p.kaartenbalie.reporting.castor.TypeSummary[] mArray = new nl.b3p.kaartenbalie.reporting.castor.TypeSummary[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (nl.b3p.kaartenbalie.reporting.castor.TypeSummary) _typeSummaryList.get(index);
        }
        return mArray;
    } //-- nl.b3p.kaartenbalie.reporting.castor.TypeSummary[] getTypeSummary() 

    /**
     * Method getTypeSummaryCount
     * 
     * 
     * 
     * @return int
     */
    public int getTypeSummaryCount()
    {
        return _typeSummaryList.size();
    } //-- int getTypeSummaryCount() 

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
     * Method removeTypeSummary
     * 
     * 
     * 
     * @param vTypeSummary
     * @return boolean
     */
    public boolean removeTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary vTypeSummary)
    {
        boolean removed = _typeSummaryList.remove(vTypeSummary);
        return removed;
    } //-- boolean removeTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary) 

    /**
     * Method setTypeSummary
     * 
     * 
     * 
     * @param index
     * @param vTypeSummary
     */
    public void setTypeSummary(int index, nl.b3p.kaartenbalie.reporting.castor.TypeSummary vTypeSummary)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _typeSummaryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _typeSummaryList.set(index, vTypeSummary);
    } //-- void setTypeSummary(int, nl.b3p.kaartenbalie.reporting.castor.TypeSummary) 

    /**
     * Method setTypeSummary
     * 
     * 
     * 
     * @param typeSummaryArray
     */
    public void setTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary[] typeSummaryArray)
    {
        //-- copy array
        _typeSummaryList.clear();
        for (int i = 0; i < typeSummaryArray.length; i++) {
            _typeSummaryList.add(typeSummaryArray[i]);
        }
    } //-- void setTypeSummary(nl.b3p.kaartenbalie.reporting.castor.TypeSummary) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return RequestSummary
     */
    public static nl.b3p.kaartenbalie.reporting.castor.RequestSummary unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.kaartenbalie.reporting.castor.RequestSummary) Unmarshaller.unmarshal(nl.b3p.kaartenbalie.reporting.castor.RequestSummary.class, reader);
    } //-- nl.b3p.kaartenbalie.reporting.castor.RequestSummary unmarshal(java.io.Reader) 

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
