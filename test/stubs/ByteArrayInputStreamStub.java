package stubs;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author rachelle
 */
public class ByteArrayInputStreamStub extends ByteArrayInputStream {
    private int readAheadLimit;
    
    /**
     * Creates a ByteArrayInputStream so that it uses buf as its buffer array. The buffer array is not copied. The initial value of pos is 0 and the initial value of count is the length of buf. 
     * 
     * @param buf the input buffer
     */
    public ByteArrayInputStreamStub(byte[] buf){
        super(buf);
        
        this.readAheadLimit = buf.length;
    }
    
    /**
     * Creates ByteArrayInputStream that uses buf as its buffer array. The initial value of pos is offset and the initial value of count is the minimum of offset+length and buf.length. The buffer array is not copied. The buffer's mark is set to the specified offset. 
     * 
     * @param buf       the input buffer
     * @param offset    the offset in the buffer of the first byte to read
     * @param length    the maximum number of bytes to read from the buffer
     */
    public ByteArrayInputStreamStub(byte[] buf,int offset,int length){
        super(buf,offset,length);
        
        this.readAheadLimit = length;
    }
    
    @Override
    /**
     * Reads the next byte of data from this input stream. The value byte is returned as an int in the range 0 to 255. If no byte is available because the end of the stream has been reached, the value -1 is returned.
     * 
     * This read method cannot block. 
     * 
     * @return the next byte of data, or -1 if the end of the stream has been reached.
     */
    public int read(){
        if( this.pos >= this.count ){
            return -1;
        }
        
        this.pos++;
        
        this.markLimit();
        
        return this.buf[(this.pos-1)];
    }
    
    @Override
    /**
     * Reads up to len bytes of data into an array of bytes from this input stream. If pos equals count, then -1 is returned to indicate end of file. 
     * 
     * @param   b   the buffer into which the data is read
     * @param   off the start offset of the data
     * @param   len the maximum number of bytes read
     * @return  the total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached.
     */
    public int read(byte[] b, int off,int len){
        if( off > 0 )   this.pos += off;
        if( this.pos >= this.count )    return -1;
        
        int end = b.length;
        if( len < end ) end = len;
        end = this.pos + end;
        
        int counter = 0;
        while( this.pos < end ){
            b[counter]  = this.buf[this.pos];
            
            this.pos++;
            
            if( this.pos >= this.count )    break;
            counter++;
        }
        
        this.markLimit();
        
        return counter;
    }
    
    /**
     * Skips n bytes of input from this input stream. Fewer bytes might be skipped if the end of the input stream is reached. 
     * 
     * @param n     the number of bytes to be skipped. 
     * @return      the actual number of bytes skipped.
     */
    @Override
    public long skip(long n){
        long skipped = 0;
        
        if( (this.pos + n) >= this.count ){
            skipped = (this.count - this.pos);
            this.pos    = this.count;
        }
        else {
            this.pos    += n;
            skipped     = n;
        }
        
        this.markLimit();
        
        return skipped;
    }
    
    /**
     * Returns the number of bytes that can be read from this input stream without blocking.
     * 
     * @return the number of bytes that can be read from the input stream without blocking.
     */
    @Override
    public int available(){
        return this.count - this.pos;
    }
    
    /**
     * Tests if this InputStream supports mark/reset.
     * 
     * @return true if this stream instance supports the mark and reset methods; false otherwise.
     */
    @Override
    public boolean markSupported(){
        return true;
    }
    
    /**
     * Set the current marked position in the stream. ByteArrayInputStream objects are marked at position zero by default when constructed. They may be marked at another position within the buffer by this method. 
     * 
     * @param readAheadLimit 
     */
    @Override
    public void mark(int readAheadLimit){
        this.readAheadLimit = readAheadLimit;
        
        this.mark   = this.pos;
    }

    /**
     * Checks if the read ahead limit is reached
     */
    private void markLimit() {
        if( this.pos > this.readAheadLimit ){
            this.mark   = 0;
        }
    }
    
    /**
     * Resets the buffer to the marked position. The marked position is 0 unless another position was marked or an offset was specified in the constructor. 
     */
    @Override
    public void reset(){
        this.pos    = this.mark;
    }
    
    /**
     * Closing a ByteArrayInputStream has no effect. The methods in this class can be called after the stream has been closed without generating an IOException. 
     * 
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException{
        
    }
}
