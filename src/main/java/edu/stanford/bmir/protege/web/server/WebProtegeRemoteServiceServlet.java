package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        ProjectDetailsManager mpm = getMetaProjectManager();
        return mpm.isProjectOwner(userId, projectId);
    }

    /**
     * Determines if the signed in user is an admin.
     *
     * @return <code>true</code> if there is a user signed in AND the signed in user corresponds to a user which exists
     *         where that user is an admin, otherwise <code>false</code>
     */
    protected boolean isSignedInUserAdmin() {
        UserId userId = getUserInSession();
        return getMetaProjectManager().isUserAdmin(userId);
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
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        // Taken from http://stackoverflow.com/a/3771391

        //get the base url from the header instead of the body this way
        //apache reverse proxy with rewrite on the header can work

        String moduleBaseURLHdr = request.getHeader("X-GWT-Module-Base");
        if(moduleBaseURLHdr != null){
            moduleBaseURL = moduleBaseURLHdr;
        }
        return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
    }

//    private void dumpHeaders(HttpServletRequest request) {
//        System.out.println("Headers");
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()) {
//            String s = headerNames.nextElement();
//            System.out.print(s);
//            System.out.print("  --->  ");
//            System.out.println(request.getHeader(s));
//        }
//        System.out.println();
//        System.out.println();
//    }
}
