/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.reporting.domain.operations.ClientXFerOperation;
import nl.b3p.ogc.utils.OGCRequest;
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
    private int responseCode;
    private int contentLength;
    private HttpServletResponse response;
    private OutputStream sos;
    private String contentDisposition;
    private long startTime;
    private long endTime;    //Use for reporting...
    private DataMonitoring requestReporting;
    private Map requestParameterMap;
    private Class requestClassType;
    private String operation;
    //end
    //Used for layering
    private Map layeringParameterMap;
    //end
    private OGCRequest ogcrequest;

    public DataWrapper(HttpServletResponse response) throws IOException {
        this.response = response;
        this.sos = response.getOutputStream();
        this.requestParameterMap = new HashMap();
        layeringParameterMap = new HashMap();
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
        this.contentLength = lenght;

    }

    public int getContentLength() {
        return contentLength;
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
        //Logging the dataspeed...
        Map parameterMap = new HashMap();
        if (requestReporting != null) {
            parameterMap.put("DataSize", new Long(baos.size()));
            parameterMap.put("MsSinceRequestStart", new Long(requestReporting.getMSSinceStart()));
        }
        long startTime = System.currentTimeMillis();
        // Log initialized, now start the operation...
        this.setContentLength(baos.size());
        try {
            sos.write(baos.toByteArray());
            sos.flush();
        } finally {
            if (sos != null) {
                sos.close();
            }
        }
        // Operation done.. now write the log...
        if (requestReporting != null) {
            parameterMap.put("Duration", new Long(System.currentTimeMillis() - startTime));
            try {
                requestReporting.addRequestOperation(ClientXFerOperation.class, parameterMap);
            } catch (Throwable t) {
                log.warn("", t);
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

    /*
     *Used for layering
     */
    public Map getLayeringParameterMap() {
        return layeringParameterMap;
    }

    public void setLayeringParameterMap(Map layeringParameterMap) {
        this.layeringParameterMap = layeringParameterMap;
    }

    /*
     *Used for reporting...
     */
    public DataMonitoring getRequestReporting() {
        return requestReporting;
    }

    public void setRequestReporting(DataMonitoring requestReporting) {
        this.requestReporting = requestReporting;
    }

    public Class getRequestClassType() {
        return requestClassType;
    }

    public void setRequestClassType(Class requestClassType) {
        this.requestClassType = requestClassType;
    }

    public Map getRequestParameterMap() {
        return requestParameterMap;
    }

    public void setRequestParameter(String key, Object object) {
        this.requestParameterMap.put(key, object);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    /*
     * End of used for reporting...
     */

    public OGCRequest getOgcrequest() {
        return ogcrequest;
    }

    public void setOgcrequest(OGCRequest ogcrequest) {
        this.ogcrequest = ogcrequest;
    }
}
