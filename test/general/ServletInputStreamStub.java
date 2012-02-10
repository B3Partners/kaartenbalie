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

    @Override
    public int read() throws IOException {
        this.pointer++;
        
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
        int read        = -1;
        this.pointer    += (off + len);
        
        return read;
    }
    
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

    public boolean isRead() {
        return this.pointer != 0;
    }
    
    @Override
    public void reset() throws IOException {
        if( this.pointer == 0 ) throw new IOException("Reset failed");
        
        this.pointer    = 0;
        
        
    }
}
