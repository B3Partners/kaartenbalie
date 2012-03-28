package stubs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author rachelle
 */
public class InputStreamStub extends InputStream {
    private ArrayList<Byte> content;
    private int pointer;
    
    public InputStreamStub(){
        this.pointer    = 0;
    }
    
    public void setContent(ArrayList<Byte> content){
        this.content    = content;
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is returned as an int in the range 0 to 255. If no byte is available because the end of the stream has been reached, the value -1 is returned. This method blocks until input data is available, the end of the stream is detected, or an exception is thrown. 
     * 
     * @return      the next byte of data, or -1 if the end of the stream is reached. 
     * @throws      IOException     if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        if( this.content.isEmpty() )  throw new IOException("Content can not be read");
        if( this.pointer >= this.content.size() )   return -1;
        
        int value   = this.content.get(pointer);
        this.pointer++;
        
        return value;
    }
    
    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b. The number of bytes actually read is returned as an integer. This method blocks until input data is available, end of file is detected, or an exception is thrown.
     * 
     * If the length of b is zero, then no bytes are read and 0 is returned; otherwise, there is an attempt to read at least one byte. If no byte is available because the stream is at the end of the file, the value -1 is returned; otherwise, at least one byte is read and stored into b. 
     * 
     * @param b     the buffer into which the data is read. 
     * @return      the total number of bytes read into the buffer, or -1 is there is no more data because the end of the stream has been reached. 
     * @throws IOException  If the first byte cannot be read for any reason other than the end of the file, if the input stream has been closed, or if some other I/O error occurs. 
     * @throws  NullPointerException    if b is null.
     */
    @Override
    public int read(byte[] b) throws IOException, NullPointerException {
        return this.read(b,0,b.length);
    }
    
    /**
     * Reads up to len bytes of data from the input stream into an array of bytes. An attempt is made to read as many as len bytes, but a smaller number may be read. The number of bytes actually read is returned as an integer.
     * 
     * This method blocks until input data is available, end of file is detected, or an exception is thrown.
     * 
     * If len is zero, then no bytes are read and 0 is returned; otherwise, there is an attempt to read at least one byte. If no byte is available because the stream is at end of file, the value -1 is returned; otherwise, at least one byte is read and stored into b. 
     * 
     * @param b     the buffer into which the data is read.
     * @param off   the start offset in array b at which the data is written.
     * @param len   the maximum number of bytes to read. 
     * @return      the total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached. 
     * @throws IOException                  If the first byte cannot be read for any reason other than end of file.
     * @throws NullPointerException         If b is null.
     * @throws IndexOutOfBoundsException    If off is negative, len is negative, or len is greater than b.length - off
     */
    @Override
    public int read(byte[] b,int off,int len) throws IOException, NullPointerException, IndexOutOfBoundsException {
        if( this.content.isEmpty() )  throw new IOException("Content can not be read");
        if( off < 0 || len < 0 || len > (b.length - off) )  throw new IndexOutOfBoundsException("Invalid offset or length");
        if( b == null || b.length == 0 )    throw new NullPointerException("Unusable buffer");
        
        this.pointer += off;
        if( this.pointer >= this.content.size() )   return -1;
        
        int read    = 0;
        for(int i=0; i<b.length; i++){
            b[i]    = this.content.get(this.pointer);
            this.pointer++;
            read++;
            
            if( this.pointer == this.content.size() )   break;
            if( read == len )   break;
        }
            
        return read;
    }
    
    /**
     * Skips over and discards n bytes of data from this input stream. The skip method may, for a variety of reasons, end up skipping over some smaller number of bytes, possibly 0. This may result from any of a number of conditions; reaching end of file before n bytes have been skipped is only one possibility. The actual number of bytes skipped is returned. If n is negative, no bytes are skipped. 
     * 
     * @param n     the number of bytes to be skipped.
     * @return      the actual number of bytes skipped. 
     * @throws IOException  if the stream does not support seek, or if some other I/O error occurs.
     */
    @Override
    public long skip(long n) throws IOException {
        if( this.content.isEmpty() )  throw new IOException("Content can not be read");
        
        long skipped    = 0;
        if( (this.pointer + n) > this.content.size() ){
            skipped = this.content.size() - this.pointer;            
        }
        else {
            skipped = n;
        }
        
        this.pointer += skipped;
                
        return skipped;
    }
    
    /**
     * Returns an estimate of the number of bytes that can be read (or skipped over) from this input stream without blocking by the next invocation of a method for this input stream. The next invocation might be the same thread or another thread. A single read or skip of this many bytes will not block, but may read or skip fewer bytes. 
     * 
     * @return      an estimate of the number of bytes that can be read (or skipped over) from this input stream without blocking or 0 when it reaches the end of the input stream. 
     * @throws IOException  if an I/O error occurs.
     */
    @Override
    public int available() throws IOException {
        return 0;
    }
    
    /**
     * Closes this input stream and releases any system resources associated with the stream.
     * 
     * The close method of InputStream does nothing. 
     * 
     * @throws IOException  if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {        
    }
    
    /**
     * Marks the current position in this input stream. A subsequent call to the reset method repositions this stream at the last marked position so that subsequent reads re-read the same bytes.
     * 
     * The readlimit arguments tells this input stream to allow that many bytes to be read before the mark position gets invalidated. 
     * 
     * The mark method of InputStream does nothing. 
     * 
     * @param readLimit the maximum limit of bytes that can be read before the mark position becomes invalid.
     */
    @Override
    public void mark(int readLimit){
    }
    
    /**
     * Repositions this stream to the position at the time the mark method was last called on this input stream. 
     * 
     * The method reset for class InputStream does nothing except throw an IOException.
     * 
     * @throws IOException  Reset is not supported
     */
    @Override
    public void reset() throws IOException {
        throw new IOException("Reset is not supported");
    }
    
    /**
     * Tests if this input stream supports the mark and reset methods. Whether or not mark and reset are supported is an invariant property of a particular input stream instance. The markSupported method of InputStream returns false. 
     * 
     * @return false. Mark and reset is not supported
     */
    @Override
    public boolean markSupported(){
        return false;
    }
}
