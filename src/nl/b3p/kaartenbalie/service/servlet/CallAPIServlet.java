package nl.b3p.kaartenbalie.service.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import static nl.b3p.kaartenbalie.service.servlet.GeneralServlet.log;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Boy de Wit, B3Partners
 */
public class CallAPIServlet extends GeneralServlet {

    public static String SLD_FOLDER;
    public static String SLD_EXTENSION = ".xml";
    public static final String SLD_GET_PARAM = "sld";

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        if (!hasValidConfigAndParams(request, response)) {
            try {
                writeErrorMessage(response, "Service not configured.");
                response.sendError(response.SC_BAD_REQUEST, "Service not configured.");
            } catch (IOException ex) {
            }

            return;
        }
        
        String sld = request.getParameter(SLD_GET_PARAM);

        if (sld != null && !sld.isEmpty()) {
            String folder = SLD_FOLDER;

            try {
                FileInputStream fis = new FileInputStream(new File(folder + sld));

                OutputStream os = response.getOutputStream();
                byte buffer[] = new byte[8192];
                int bytesRead;
                
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                fis.close();
                os.flush();
                os.close();
                
            } catch (FileNotFoundException fnfex) {
                log.error("Error creating SLD. Could not find file: " + folder + sld);
                return;
            } catch (IOException ioex) {
                log.error("Error creating SLD.", ioex);
                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        boolean canLogin = canLogin(request, response);
        
        // voor POST verzoeken moet wel zijn ingelogd via basic http auth
        if (!canLogin) {
            try {
                writeErrorMessage(response, "Not authorized.");
                response.sendError(response.SC_UNAUTHORIZED, "Not authorized.");
            } catch (IOException ex) {
            }
            return;
        }

        if (!hasValidConfigAndParams(request, response)) {
            try {
                writeErrorMessage(response, "Service not configured.");
                response.sendError(response.SC_BAD_REQUEST, "Service not configured.");
            } catch (IOException ex) {
            }
            return;
        }

        try {
            InputStream in = null;
            String workspace = null;

            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldname = item.getFieldName();
                    String fieldvalue = item.getString();

                    if (fieldname.equals("workspace")) {
                        workspace = item.getString();
                    }
                } else {
                    // Process form file field (input type="file").
                    String fieldname = item.getFieldName();
                    String filename = FilenameUtils.getName(item.getName());
                    in = item.getInputStream();
                }
            }

            if (workspace != null && in != null) {
                String folder = SLD_FOLDER;
                Path path = Paths.get(folder);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }

                String fileName = workspace + SLD_EXTENSION;
                FileOutputStream out = new FileOutputStream(new File(folder + fileName), false);
                IOUtils.copy(in, out);

                /* return sld url */
                String base = createBaseUrl(request, false).toString();
                String api = "/api?sld=";
                String url = base + api + fileName;

                response.getWriter().write(url);
            } else {
                writeErrorMessage(response, "Resource conflict.");
                response.sendError(response.SC_CONFLICT, "Resource conflict.");
            }
        } catch (Exception ex) {
            log.error("Error during POST: ", ex);
        }
    }

    private void writeErrorMessage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<title>Call API Servlet</title>");
        pw.println("<script type=\"text/javascript\"> if(window.parent && (typeof window.parent.showCsvError == 'function')) { window.parent.showCsvError(); } </script>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<h1>Info</h1>");
        pw.println("<h3>" + message + "</h3>");
        pw.println("</body>");
        pw.println("</html>");
    }

    private boolean canLogin(HttpServletRequest request, HttpServletResponse response) {
        User user = null;
        Object identity = null;
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();

            tx.begin();

            user = checkLogin(request, null);

            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }

            return false;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }

        if (user != null) {
            return true;
        }

        return false;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        log = LogFactory.getLog(this.getClass());

        if (config.getInitParameter("sldFolder") != null
                && !config.getInitParameter("sldFolder").isEmpty()) {
            SLD_FOLDER = config.getInitParameter("sldFolder");
        }
        if (config.getInitParameter("sldExtension") != null
                && !config.getInitParameter("sldExtension").isEmpty()) {
            SLD_EXTENSION = config.getInitParameter("sldExtension");
        }

        // maybe add seperator
        if (SLD_FOLDER != null && SLD_FOLDER.lastIndexOf(File.separatorChar) != SLD_FOLDER.length() - 1) {
            SLD_FOLDER += File.separatorChar;
        }
        // maybe add dot
        if (SLD_EXTENSION != null && !SLD_EXTENSION.contains(".")) {
            SLD_EXTENSION = "." + SLD_EXTENSION;
        }
    }

    private boolean hasValidConfigAndParams(HttpServletRequest request, HttpServletResponse response) {
        if (SLD_FOLDER == null) {
            return false;
        }

        return true;
    }

    @Override
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServletInfo() {
        return "Servlet for incoming RESTful API calls";
    }
}
