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

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class MonitorReportDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class MonitorReportDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public MonitorReportDescriptor() 
     {
        super();
        xmlName = "monitor-report";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _parameters
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(nl.b3p.kaartenbalie.reporting.castor.Parameters.class, "_parameters", "parameters", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MonitorReport target = (MonitorReport) object;
                return target.getParameters();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MonitorReport target = (MonitorReport) object;
                    target.setParameters( (nl.b3p.kaartenbalie.reporting.castor.Parameters) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new nl.b3p.kaartenbalie.reporting.castor.Parameters();
            }
        };
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _parameters
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _requestSummary
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(nl.b3p.kaartenbalie.reporting.castor.RequestSummary.class, "_requestSummary", "request-summary", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MonitorReport target = (MonitorReport) object;
                return target.getRequestSummary();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MonitorReport target = (MonitorReport) object;
                    target.setRequestSummary( (nl.b3p.kaartenbalie.reporting.castor.RequestSummary) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new nl.b3p.kaartenbalie.reporting.castor.RequestSummary();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _requestSummary
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _responseFrequency
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency.class, "_responseFrequency", "response-frequency", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MonitorReport target = (MonitorReport) object;
                return target.getResponseFrequency();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MonitorReport target = (MonitorReport) object;
                    target.setResponseFrequency( (nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _responseFrequency
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _serviceProviders
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(nl.b3p.kaartenbalie.reporting.castor.ServiceProviders.class, "_serviceProviders", "service-providers", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MonitorReport target = (MonitorReport) object;
                return target.getServiceProviders();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MonitorReport target = (MonitorReport) object;
                    target.setServiceProviders( (nl.b3p.kaartenbalie.reporting.castor.ServiceProviders) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new nl.b3p.kaartenbalie.reporting.castor.ServiceProviders();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _serviceProviders
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _requestLoad
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(nl.b3p.kaartenbalie.reporting.castor.RequestLoad.class, "_requestLoad", "request-load", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                MonitorReport target = (MonitorReport) object;
                return target.getRequestLoad();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    MonitorReport target = (MonitorReport) object;
                    target.setRequestLoad( (nl.b3p.kaartenbalie.reporting.castor.RequestLoad) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new nl.b3p.kaartenbalie.reporting.castor.RequestLoad();
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _requestLoad
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- nl.b3p.kaartenbalie.reporting.castor.MonitorReportDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass()
    {
        return nl.b3p.kaartenbalie.reporting.castor.MonitorReport.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
