package stubs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author rachelle
 */
public class ByteArrayOutputStreamStub extends ByteArrayOutputStream {
    private ArrayList<Byte> buffer;
    
    /**
     * Creates a new byte array output stream. The buffer capacity is initially 32 bytes, though its size increases if necessary. 
     */
    public ByteArrayOutputStreamStub(){
        this(32);
    }
    
    /**
     * Creates a new byte array output stream, with a buffer capacity of the specified size, in bytes. 
     * 
     * @param size the initial size. 
     */
    public ByteArrayOutputStreamStub(int size){
        this.buffer = new ArrayList<Byte>(size);
        
        this.count  = 0;
    }
    
    /**
     * Writes the specified byte to this byte array output stream. 
     * 
     * @param b     the byte to be written.
     */
    @Override
    public void write(int b){
        ByteBuffer buffer   = ByteBuffer.allocate(4);
        buffer.putInt(b);
        byte[] byteBuffer = buffer.array();
        
        byte bit;
        for(int i=0; i<byteBuffer.length; i++){
            bit = byteBuffer[i];
            this.buffer.add(bit);
            this.count++;
        }
    }
    
    /**
     * Writes len bytes from the specified byte array starting at offset off to this byte array output stream. 
     * 
     * @param b     the data.
     * @param off   the start offset in the data.
     * @param len   the number of bytes to write.
     */
    @Override
    public void write(byte[] b,int off,int len){
        for(int i=off; i<b.length; i++){
            this.buffer.add(b[i]);
            this.count++;
            
            if( i == (len-1) )  break;
        }
    }
    
    /**
     * Writes the complete contents of this byte array output stream to the specified output stream argument, as if by calling the output stream's write method using out.write(buf, 0, count). 
     * 
     * @param out   the output stream to which to write the data. 
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void writeTo(OutputStream out) throws IOException{
        for(int i=0; i<count; i++){
            out.write((int) this.buffer.get(i));
        }
    }
    
    /**
     * Resets the count field of this byte array output stream to zero, so that all currently accumulated output in the output stream is discarded. The output stream can be used again, reusing the already allocated buffer space. 
     */
    @Override
    public void reset(){
        this.buffer.clear();
        this.count  = 0;
    }
    
    /**
     * Creates a newly allocated byte array. Its size is the current size of this output stream and the valid contents of the buffer have been copied into it. 
     * 
     * @return the current contents of this output stream, as a byte array.
     */
    @Override
    public byte[] toByteArray(){
        byte[] output   = new byte[this.buffer.size()];
        
        for(int i=0; i<count; i++){
            output[i]  = this.buffer.get(i);
        }
        
        return output;
    }

    /**
     * Returns the current size of the buffer.
     * 
     * @return the value of the count field, which is the number of valid bytes in this output stream.
     */
    @Override
    public int size(){
        return this.count;
    }
    
    /**
     * Converts the buffer's contents into a string decoding bytes using the platform's default character set. The length of the new String is a function of the character set, and hence may not be equal to the size of the buffer. 
     * 
     * @return String decoded from the buffer's contents.
     */
    @Override
    public String toString(){
        return new String(this.toByteArray());
    }
    
    /**
     * Converts the buffer's contents into a string by decoding the bytes using the specified charsetName. The length of the new String is a function of the charset, and hence may not be equal to the length of the byte array.
     * 
     * This method always replaces malformed-input and unmappable-character sequences with this charset's default replacement string. The CharsetDecoder class should be used when more control over the decoding process is required.
     * 
     * @param charsetName   the name of a supported charset
     * @return  String decoded from the buffer's contents. 
     * @throws UnsupportedEncodingException If the named charset is not supported
     */
    @Override
    public String toString(String charsetName) throws UnsupportedEncodingException{
        return new String(this.toByteArray(),charsetName);
    }
}
