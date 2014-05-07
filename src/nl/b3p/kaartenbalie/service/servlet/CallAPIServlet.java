package nl.b3p.kaartenbalie.service.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        log = LogFactory.getLog(this.getClass());
        log.debug("Initializing CallAPIServlet");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        boolean canLogin = canLogin(request, response);

        if (!canLogin) {
            try {
                writeErrorMessage(response, "Not authorized.");
                response.sendError(response.SC_UNAUTHORIZED, "Not authorized.");
            } catch (IOException ex) {
            }
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
                String folder = getServletContext().getRealPath("") + "/sld/";
                
                Path path = Paths.get(folder);                
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                
                String fileName = folder + workspace + ".sld";
                
                FileOutputStream out = new FileOutputStream(new File(fileName), false);
                IOUtils.copy(in, out);

                response.getWriter().write(fileName);
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
        pw.println("<h1>Fout</h1>");
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
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServletInfo() {
        return "Servlet for incoming RESTful API calls";
    }
}
