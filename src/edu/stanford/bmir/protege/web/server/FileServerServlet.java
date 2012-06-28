package edu.stanford.bmir.protege.web.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.smi.protege.util.Log;

/**
 * Server-side services for notification management.
 */
public class FileServerServlet extends HttpServlet {
    byte[] byteBuffer = new byte[1024];
    private static Logger logger = Log.getLogger(FileServerServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filename =  request.getParameter("FILE_NAME");
        if (logger.isLoggable(Level.FINE)){
            logger.log(Level.FINE, "Getting file name " + filename);
        }
        try {
            ServletOutputStream out = response.getOutputStream();
            ServletContext context = getServletConfig().getServletContext();

            File file = new File(ApplicationProperties.getICDExportDirectory() + filename);
            String mimetype = context.getMimeType(filename);

            response.setContentType((mimetype != null) ? mimetype : "application/vnd.ms-excel");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            DataInputStream in = new DataInputStream(new FileInputStream(file));

            int length;
            while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
                out.write(byteBuffer, 0, length);
            }

            in.close();
            out.flush();
            out.close();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Caught when serving file " + filename, e);
        }
    }


}
