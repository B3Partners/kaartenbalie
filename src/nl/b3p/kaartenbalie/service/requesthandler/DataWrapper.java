/*
 * DataWrapper.java
 *
 * Created on 15 maart 2007, 15:20
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

/**
 *
 * @author Chris
 */
public class DataWrapper {

    private String contentType;
    private String contentEncoding;
    private String responseMessage;
    private int  responseCode;
    private int  contentLength;
    private byte[] data;
    private String contentDisposition;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String header) {
        this.contentDisposition = header;
    }
}
