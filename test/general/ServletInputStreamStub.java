package general;

import java.io.IOException;
import javax.servlet.ServletInputStream;

/**
 *
 * @author rachelle
 */
public class ServletInputStreamStub extends ServletInputStream {
    private int pointer = 0;
    private String contentType;
    private byte[] content;
    private boolean hasLoaded   = false;

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an int in the range 0 to 255. If no byte is available because the end of the stream has been reached, the value -1 is returned. This method blocks until input data is available, the end of the stream is detected, or an exception is thrown.
     * 
     * @return  the next byte of data, or -1 if the end of the stream is reached. 
     * @throws IOException If a I/O error occurs
     */
    @Override
    public int read() throws IOException {
        if( this.pointer >= this.content.length )   return -1;
        
        this.pointer++;
        
        return (int) this.content[this.pointer-1];
    }    
    
    /**
     * Returns the length of the stream content
     * 
     * @return The length of the content
     */
    public int getLength(){
        return this.content.length;
    }
    
    /**
     * Returns the MIME type of the body of the stream
     * 
     * @return  a String containing the name of the MIME type of the stream
     */
    public String getContentType(){
        return this.contentType;
    }

    /**
     * Checks if the stream is already read
     * 
     * @return True if the stream is already read, otherwise false
     */
    public boolean isRead() {
        return this.pointer != 0;
    }

    /**
     * Returns the embedded stream
     * 
     * @return The stream
     */
    public ServletInputStream getStream() {
        this.hasLoaded  = true;
        
        return this;
    }

    /**
     * Checks if the embedded stream is already loaded
     * 
     * @return  True if the stream is already loaded, otherwise false
     */
    public boolean hasLoaded() {
        return this.hasLoaded;
    }
}
