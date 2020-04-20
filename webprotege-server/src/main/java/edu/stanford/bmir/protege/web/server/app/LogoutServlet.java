package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-20
 */
@ApplicationSingleton
public class LogoutServlet extends HttpServlet {

    @Inject
    public LogoutServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.logout();
        resp.sendRedirect("/");
    }
}
