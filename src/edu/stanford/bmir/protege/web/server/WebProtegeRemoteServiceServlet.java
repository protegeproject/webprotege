package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.logging.WPLogParam;
import edu.stanford.smi.protege.server.metaproject.Group;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 * <p>
 *     A base for web-protege services.  This class provides useful functionality for checking that a caller is logged in,
 *     checking whether the caller is the project owner etc.
 * </p>
 */
public abstract class WebProtegeRemoteServiceServlet extends RemoteServiceServlet {

    protected MetaProjectManager getMetaProjectManager() {
        return Protege3ProjectManager.getProjectManager().getMetaProjectManager();
    }

    /**
     * Gets the userId for the client associated with the current thread local request.
     * @return The UserId.  Not null (if no user is logged in then the value specified by {@link edu.stanford.bmir.protege.web.client.rpc.data.UserId#getNull()}
     * will be returned.
     */
    public UserId getUserInSession() {
        HttpServletRequest request = getThreadLocalRequest();
        final HttpSession session = request.getSession();
        final UserData userData = (UserData) session.getAttribute(SessionConstants.USER_DATA_PARAMETER);
        if (userData == null) {
            return UserId.getNull();
        }
        else {
            final String name = userData.getName();
            if(name == null) {
                throw new IllegalStateException("UserData.getName() returned null");
            }
            return UserId.getUserId(name);
        }
    }

    /**
     * Gets the userId for the signed in client that is associated with the current thread local request.
     * local request does not have a signed in user associated with it.
     * @return The {@link UserId} of the signed in user associated with the thread local request.
     * @throws NotSignedInException if there is not a signed in user associated with the thread local request.
     */
    public UserId getUserInSessionAndEnsureSignedIn() throws NotSignedInException {
        UserId userId = getUserInSession();
        if(userId.isNull()) {
            throw new NotSignedInException();
        }
        else {
            return userId;
        }
    }

    /**
     * Ensures that there is a user signed in for the current thread local request.
     * @throws NotSignedInException if there is no user signed in for the current thread local request.
     */
    public void ensureSignedIn() throws NotSignedInException {
        getUserInSessionAndEnsureSignedIn();
    }



    /**
     * Detemines if the signed in user is the owner of the specified project
     * @param projectId The project id
     * @return <code>true</code> if the project exists AND there is a user signed in AND the signed in user is the
     * owner of the project, otherwise <code>false</code>.
     */
    protected boolean isSignedInUserProjectOwner(ProjectId projectId) {
        UserId userId = getUserInSession();
        if(userId.isNull()) {
            return false;
        }
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectInstance project = metaProject.getProject(projectId.getProjectName());
        if(project == null) {
            return false;
        }
        User owner = project.getOwner();
        return owner != null && userId.getUserName().equals(owner.getName());
    }

    /**
     * Determines if the signed in user is an admin.
     * @return <code>true</code> if there is a user signed in AND the signed in user corresponds to a user which exists
     * where that user is an admin, otherwise <code>false</code>
     */
    protected boolean isSignedInUserAdmin() {
        UserId userId = getUserInSession();
        if(userId.isNull()) {
            return false;
        }
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject metaProject = mpm.getMetaProject();
        User user = metaProject.getUser(userId.getUserName());
        if(user == null) {
            return false;
        }
        for(Group group : user.getGroups()) {
            if("AdminGroup".equals(group.getName())) {
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
    protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
//        // TODO: Logging Code
//        Method method = rpcRequest.getMethod();
//        Object [] parameters = rpcRequest.getParameters();
//        UserId userId = getUserInSession();
//        System.out.println(userId.getUserName() + " invoked " + method.getName());
//        Annotation[] [] annotations = method.getParameterAnnotations();
//        TypeVariable[] vars = method.getTypeParameters();
//        String paramName = "";
//        for(int i = 0; i < parameters.length; i++) {
//            Object value = parameters[i];
//            for(Annotation annotation : annotations [i]) {
//                if(annotation instanceof WPLogParam) {
//                    WPLogParam param = (WPLogParam) annotation;
//                    paramName = param.name();
//
//                }
//            }
//            System.out.println("\tParam " + i + " " + paramName + "=" + value );
//        }
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        // TODO: Log timing in here
        return super.processCall(payload);
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {
        super.doUnexpectedFailure(e);
        // TODO: Log Exception Properly!
        Log.getLogger().severe("UNEXPECTED FAILURE");
        Log.getLogger().severe(e.getMessage());
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        Log.getLogger().severe(sw.toString());
    }
}
