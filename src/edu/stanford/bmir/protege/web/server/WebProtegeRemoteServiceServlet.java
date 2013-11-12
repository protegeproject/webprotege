package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.Group;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 * <p>
 * A base for web-protege services.  This class provides useful functionality for checking that a caller is logged in,
 * checking whether the caller is the project owner etc.
 * </p>
 */
public abstract class WebProtegeRemoteServiceServlet extends RemoteServiceServlet {

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(RemoteServiceServlet.class);

    protected MetaProjectManager getMetaProjectManager() {
        return MetaProjectManager.getManager();
    }

    /**
     * Gets the userId for the client associated with the current thread local request.
     *
     * @return The UserId.  Not null (if no user is logged in then the value specified by {@link edu.stanford.bmir.protege.web.shared.user.UserId#getNull()}
     *         will be returned.
     */
    public UserId getUserInSession() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        return SessionConstants.getUserId(session);
    }

    /**
     * Gets the userId for the signed in client that is associated with the current thread local request.
     * local request does not have a signed in user associated with it.
     *
     * @return The {@link UserId} of the signed in user associated with the thread local request.
     * @throws NotSignedInException if there is not a signed in user associated with the thread local request.
     */
    public UserId getUserInSessionAndEnsureSignedIn() throws NotSignedInException {
        UserId userId = getUserInSession();
        if (userId.isGuest()) {
            throw new NotSignedInException();
        } else {
            return userId;
        }
    }

    /**
     * Ensures that there is a user signed in for the current thread local request.
     *
     * @throws NotSignedInException if there is no user signed in for the current thread local request.
     */
    public void ensureSignedIn() throws NotSignedInException {
        getUserInSessionAndEnsureSignedIn();
    }

    /**
     * Detemines if the signed in user is the owner of the specified project
     *
     * @param projectId The project id
     * @return <code>true</code> if the project exists AND there is a user signed in AND the signed in user is the
     *         owner of the project, otherwise <code>false</code>.
     */
    protected boolean isSignedInUserProjectOwner(ProjectId projectId) {
        UserId userId = getUserInSession();
        if (userId.isGuest()) {
            return false;
        }
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectInstance project = metaProject.getProject(projectId.getId());
        if (project == null) {
            return false;
        }
        User owner = project.getOwner();
        return owner != null && userId.getUserName().equals(owner.getName());
    }

    /**
     * Determines if the signed in user is an admin.
     *
     * @return <code>true</code> if there is a user signed in AND the signed in user corresponds to a user which exists
     *         where that user is an admin, otherwise <code>false</code>
     */
    protected boolean isSignedInUserAdmin() {
        UserId userId = getUserInSession();
        if (userId.isGuest()) {
            return false;
        }
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject metaProject = mpm.getMetaProject();
        User user = metaProject.getUser(userId.getUserName());
        if (user == null) {
            return false;
        }
        for (Group group : user.getGroups()) {
            if (OntologyShareAccessConstants.ADMIN_GROUP.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////  Logging
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String processCall(String payload) throws SerializationException {
        // TODO: Log timing in here
        return super.processCall(payload);
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        HttpServletRequest request = getThreadLocalRequest();
        LOGGER.severe(e, getUserInSession(), request);
        super.doUnexpectedFailure(e);
    }

    @Override
    protected void onAfterResponseSerialized(String serializedResponse) {
        super.onAfterResponseSerialized(serializedResponse);
    }

    @Override
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        // See  https://groups.google.com/forum/?fromgroups#!searchin/google-web-toolkit/proxypass$20serialization/google-web-toolkit/3wE9yWLMJo4/Mebd0XgW1EIJ
        try {
            final String contextPath = request.getContextPath();
            // When deploying locally, the module base URL is something like
            // http://127.0.0.1:8888/webprotege/
            // In production this is
            // http://webprotege.stanford.edu
            final URI rawModuleBaseURI = URI.create(moduleBaseURL);
            final String rawModulePath = rawModuleBaseURI.getPath();
            int contextIndex = rawModulePath.indexOf(contextPath);
            final String modulePath;
            if (contextIndex < 0) {
                modulePath = contextPath + rawModulePath;
            } else if (contextIndex > 0) {
                modulePath = rawModulePath.substring(contextIndex);
            } else {
                modulePath = rawModulePath;
            }
            URI moduleBaseURI = new URI(rawModuleBaseURI.getScheme(), rawModuleBaseURI.getAuthority(), modulePath, rawModuleBaseURI.getQuery(), rawModuleBaseURI.getFragment());
            return loadSerializationPolicy(this, request, moduleBaseURI.toString(), strongName);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static SerializationPolicy loadSerializationPolicy(HttpServlet servlet,
                                                               HttpServletRequest request, String moduleBaseURL, String strongName) {
        // The request can tell you the path of the web app relative to the
        // container root.
        String contextPath = request.getContextPath();
        String modulePath = null;
        if (moduleBaseURL != null) {
            try {
                modulePath = new URL(moduleBaseURL).getPath();
            } catch (MalformedURLException ex) {
                // log the information, we will default
                servlet.log("Malformed moduleBaseURL: " + moduleBaseURL, ex);
            }
        }
        SerializationPolicy serializationPolicy = null;

    /*
     * Check that the module path must be in the same web app as the servlet
     * itself. If you need to implement a scheme different than this, override
     * this method.
     */
        if (modulePath == null || !modulePath.startsWith(contextPath)) {
            String message = "ERROR: The module path requested, "
                    + modulePath
                    + ", is not in the same web application as this servlet, "
                    + contextPath
                    + ".  Your module may not be properly configured or your client and server code maybe out of date.";
            servlet.log(message);
        } else {
            // Strip off the context path from the module base URL. It should be a
            // strict prefix.
            String contextRelativePath = modulePath.substring(contextPath.length());
            String serializationPolicyFilePath = SerializationPolicyLoader.getSerializationPolicyFileName(contextRelativePath
                    + strongName);
            // Open the RPC resource file and read its contents.
            InputStream is = servlet.getServletContext().getResourceAsStream(
                    serializationPolicyFilePath);
            try {
                if (is != null) {
                    try {
                        serializationPolicy = SerializationPolicyLoader.loadFromStream(is,
                                null);
                    } catch (ParseException e) {
                        servlet.log("ERROR: Failed to parse the policy file '"
                                + serializationPolicyFilePath + "'", e);
                    } catch (IOException e) {
                        servlet.log("ERROR: Could not read the policy file '"
                                + serializationPolicyFilePath + "'", e);
                    }
                } else {
                    String message = "ERROR: The serialization policy file '"
                            + serializationPolicyFilePath
                            + "' was not found; did you forget to include it in this deployment?";
                    servlet.log(message);
                }
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // Ignore this error
                    }
                }
            }
        }
        return serializationPolicy;
    }
}
