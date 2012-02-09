package general;

import java.io.IOException;
import javax.servlet.ServletInputStream;

/**
 *
 * @author rachelle
 */
public class ServletInputStreamStub extends ServletInputStream{
    private int length  = 0;
    private String contentType;

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * 
     * @param b
     * @param off
     * @param len
     * @return
     * @throws java.io.IOException
     */
    @Override
    public int readLine(byte[] b,int off,int len) throws java.io.IOException {
        int read    = -1;
        
        return read;
    }
    
    public int getLength(){
        return this.length;
    }
    
    /**
     * Returns the MIME type of the body of the stream
     * 
     * @return  a String containing the name of the MIME type of the stream
     */
    public String getContentType(){
        return this.contentType;
    }
}
