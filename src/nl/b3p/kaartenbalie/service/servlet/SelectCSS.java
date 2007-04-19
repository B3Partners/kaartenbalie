package nl.b3p.kaartenbalie.service.servlet;
import java.io.*;
import java.net.URL;

import javax.servlet.http.*;
import javax.servlet.*;

public class SelectCSS extends HttpServlet {
    public static final long serialVersionUID = 24362462L;

    private String STYLESFOLDER = "styles/";
    private String DEFAULT = "b3p";
    private String DOMAIN = "http://localhost:8084/kaartenbalie_mnp/";
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String s = "";
        
        if(req.getParameter("subdomain") == null) {
            s = getStylesheetName(req.getRequestURL());
        } else {
            if(req.getParameter("subdomain").equals("")) {
                s = getStylesheetName(req.getRequestURL());
            } else {
                s = req.getParameter("subdomain");
            }
        }
        
        
        String cssUrl = DOMAIN + STYLESFOLDER + s + ".css";
        try {
            URL cssur = new URL(cssUrl);
            StringBuffer result = new StringBuffer();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader( new InputStreamReader(cssur.openStream()));
            } catch (FileNotFoundException e) {
                cssUrl = DOMAIN + STYLESFOLDER + DEFAULT + ".css";
                cssur = new URL(cssUrl);
                reader = new BufferedReader( new InputStreamReader(cssur.openStream()));
            }
            String line = null;
            while ( (line = reader.readLine()) != null) {
                if(line.indexOf("url") != -1) {
                    line = line.replace("../", DOMAIN);
                }
                result.append(line);
            }
            String content = result.toString();
            
            res.setContentType("text/css");
            PrintWriter out = res.getWriter();
            res.setContentLength(content.length());
            out.write(content);
            out.close();
            
            if (reader != null) reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getStylesheetName(StringBuffer url) {
        String s = "";
        int index1 = url.indexOf("//");
        s = url.substring(index1 + 2, url.length());

        int index2 = s.indexOf(":");
        if(index2 == -1) index2 = s.indexOf("/");
        s = s.substring(0, index2);

        int index3 = s.indexOf(".");
        if(index3 == -1) {
            // Geen subdomain - Default stylesheet
            s = DEFAULT;
        } else {
            // Select subdomain
            s = s.substring(0, index3);
        }
        return s;
    }
}