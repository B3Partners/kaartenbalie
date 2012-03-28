package stubs;

import org.xml.sax.Locator;

/**
 *
 * @author rachelle
 */
public class LocatorStub implements Locator {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;
    
    public LocatorStub(){
        this.publicId       = "stub ID";
        this.systemId       = "JUnit test";
        this.lineNumber     = -1;
        this.columnNumber   = -1;
    }
    
    
    /**
     * Sets the public identifier for the current document event.
     * 
     * The return value is the public identifier of the document entity or of the external parsed entity in which the markup triggering the event appears.
     * 
     * @param   publicId    A string containing the public identifier, or null if none is available.
     */
    public void setPublicId(String publicId){
        this.publicId   = publicId;
    }

    /**
     * Return the public identifier for the current document event.
     * 
     * The return value is the public identifier of the document entity or of the external parsed entity in which the markup triggering the event appears.
     * 
     * @return  A string containing the public identifier, or null if none is available.
     */
    public String getPublicId() {
        return this.publicId;
    }
    
    /**
     * Sets the system identifier for the current document event.
     * 
     * The return value is the system identifier of the document entity or of the external parsed entity in which the markup triggering the event appears.
     * 
     * @param   systemId  A string containing the system identifier, or null if none is available.
     */
    public void setSystemId(String systemId){
        this.systemId   = systemId;
    }

    /**
     * Return the system identifier for the current document event.
     * 
     * The return value is the system identifier of the document entity or of the external parsed entity in which the markup triggering the event appears.
     * 
     * @return  A string containing the system identifier, or null if none is available.
     */
    public String getSystemId() {
        return this.systemId;
    }

    /**
     * Sets the line number where the current document event ends. Lines are delimited by line ends, which are defined in the XML specification.
     * 
     * @param   lineNumber The line number, or -1 if none is available.
     */
    public void setLineNumber(int lineNumber){
        this.lineNumber = lineNumber;
    }
    
    /**
     * Return the line number where the current document event ends. Lines are delimited by line ends, which are defined in the XML specification.
     * 
     * Warning: The return value from the method is intended only as an approximation for the sake of diagnostics; it is not intended to provide sufficient information to edit the character content of the original XML document. In some cases, these "line" numbers match what would be displayed as columns, and in others they may not match the source text due to internal entity expansion. 
     * 
     * @return The line number, or -1 if none is available.
     */
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    /**
     * Sets the column number where the current document event ends. This is one-based number of Java char values since the last line end.
     * 
     * @param   columnNumber  The column number, or -1 if none is available.
     */
    public void setColumnNumber(int columnNumber){
        this.columnNumber    = columnNumber;
    }

    /**
     * Return the column number where the current document event ends. This is one-based number of Java char values since the last line end.
     * 
     * Warning: The return value from the method is intended only as an approximation for the sake of diagnostics; it is not intended to provide sufficient information to edit the character content of the original XML document. For example, when lines contain combining character sequences, wide characters, surrogate pairs, or bi-directional text, the value may not correspond to the column in a text editor's display. 
     * 
     * @return  The column number, or -1 if none is available.
     */
    public int getColumnNumber() {
        return this.columnNumber;
    }
    
}
