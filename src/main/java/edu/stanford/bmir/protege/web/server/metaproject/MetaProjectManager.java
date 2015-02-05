package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.Operation;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.Collection;
import java.util.List;

/**
 * This interface assumes a particular implementation i.e. a Protege 3 implementation with Project object.
 * i.e. the openProject method.  Is this a problem?
 */
public abstract class MetaProjectManager {

    private static MetaProjectManager instance;

    protected MetaProjectManager() {

    }

    public static synchronized MetaProjectManager getManager() {
        if(instance == null) {
            instance  = new LocalMetaProjectManager();
        }
        return instance;
    }

    public abstract boolean hasValidCredentials(String userName, String password);

    public abstract UserData registerUser(String userName, String email, String password) throws UserRegistrationException;

    public abstract void changePassword(String userName, String password);

    public abstract String getUserEmail(String userName);

    public abstract void setUserEmail(String userName, String email);

    public abstract List<ProjectDetails> getListableReadableProjects(UserId userId);

//    public abstract List<ProjectData> getProjectsData(String userName);

    public abstract Collection<Operation> getAllowedOperations(String project, String userName);

    public abstract Collection<Operation> getAllowedServerOperations(String userName);

//    public abstract Project openProject(String name);

    /**
     * Creates a new project description inside the meta project (with the default access policy etc.)
     * @param newProjectSettings The {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings} that describes the new project.  Not <code>null</code>.
     */
    public abstract void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings);

    public abstract MetaProject getMetaProject();

    /**
     * Reloads the metaproject. Should be used with care, because it reloads
     * besides the user/password info, also the projects info. Clients should be
     * notified of the change.
     */
//    public abstract void reloadMetaProject();

    public abstract void dispose();
    
    public abstract Optional<UserId> getUserAssociatedWithOpenId(String userOpenId);

    public abstract boolean allowsCreateUser();

    //FIXME: this adds a dependency on the Protege 3 User class. But this implementation will be replace soon,
    //so, hopefully this is not so bad.
    public abstract User getUser(String userNameOrEmail);

    public abstract ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException;

}