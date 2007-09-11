/*
 * DataWrapper.java
 *
 * Created on 15 maart 2007, 15:20
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris
 */
public class DataWrapper {
    private static final Log log = LogFactory.getLog(DataWrapper.class);

    private String contentType;
    private String errorContentType;
    private String contentEncoding;
    private String responseMessage;
    private int  responseCode;
    private int  contentLength;
    private byte[] data;
    private HttpServletResponse response;
    private OutputStream sos;
    private String contentDisposition;
    private long startTime;
    private long endTime;
    
    public DataWrapper(HttpServletResponse response) throws IOException {
        this.response = response;
        this.sos = response.getOutputStream();
    }

    public String getContentType() {
        return response.getContentType();        
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }
    
    public String getErrorContentType() {
        return errorContentType;
    }

    public void setErrorContentType(String errorContentType) {
        this.errorContentType = errorContentType;
    }
    
    public int getBufferSize() {
        return response.getBufferSize();
    }
    
    public void setBufferSize(int buffer) {
        response.setBufferSize(buffer);
    }
    
    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }
    
    public void setCharacterEncoding(String characterEncoding) {
        response.setCharacterEncoding(characterEncoding);
    }
    
    private void setContentLength(int lenght) {
        response.setContentLength(lenght);
    }
    
    public void setDateHeader(String name, long date) {
        response.setDateHeader(name, date);
    }

    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }
    
    public void setIntHeader(String name, int value) {
        response.setIntHeader(name, value);
    }
    
    public Locale getLocale() {
        return response.getLocale();
    }
    
    public void setLocale(Locale locale) {
        response.setLocale(locale);
    }
    
    public void setStatus(int sc) {
        response.setStatus(sc);
    }
    
    public OutputStream getOutputStream() {
        return this.sos;
    }
    
    public void write(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = 0;
        while ((bytesRead = is.read()) != -1) {
            baos.write(bytesRead);
        }
        this.write(baos);
    }
    
    public void write(ByteArrayOutputStream baos) throws IOException {
        this.setContentLength(baos.size());
        try {
            sos.write(baos.toByteArray());
            sos.flush();
        } finally {
            if (sos!=null) {
                sos.close();
            }
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
