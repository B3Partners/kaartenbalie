package nl.b3p.kaartenbalie.service.servlet;
import java.io.*;
import java.net.URL;

import javax.servlet.http.*;
import javax.servlet.*;

public class SelectCSS extends HttpServlet {
    public static final long serialVersionUID = 24362462L;

    private String DOMAIN = "http://localhost:8084/kaartenbalie_mnp/styles/";
    private String DEFAULT = "main";
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String s = "";
        if(req.getParameter("subdomain").equals("")) {
            StringBuffer url = req.getRequestURL();
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
        } else {
            s = req.getParameter("subdomain");
        }
        
        String cssUrl = DOMAIN + s + ".css";
        try {
            URL cssur = new URL(cssUrl);
            StringBuffer result = new StringBuffer();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader( new InputStreamReader(cssur.openStream()));
            } catch (FileNotFoundException e) {
                cssUrl = DOMAIN + DEFAULT + ".css";
                cssur = new URL(cssUrl);
                reader = new BufferedReader( new InputStreamReader(cssur.openStream()));
            }
            String line = null;
            while ( (line = reader.readLine()) != null) {
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
}