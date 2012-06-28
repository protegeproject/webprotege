package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserRegistrationException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.Operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This interface assumes a particular implementation i.e. a Protege 3 implementation with Project object.
 * i.e. the openProject method.  Is this a problem?
 */
public interface MetaProjectManager {

    public boolean hasValidCredentials(String userName, String password);

    public UserData registerUser(String userName, String password) throws UserRegistrationException;

    public void changePassword(String userName, String password);

    public String getUserEmail(String userName);

    public void setUserEmail(String userName, String email);

    public List<ProjectData> getProjectsData(String userName);

    public Collection<Operation> getAllowedOperations(String project, String userName);

    public Collection<Operation> getAllowedServerOperations(String userName);

    public Project openProject(String name);

    /**
     * Creates a new project description inside the meta project (with the default access policy etc.)
     * @param newProjectSettings The {@link edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings} that describes the new project.  Not <code>null</code>.
     */
    public void createProject(NewProjectSettings newProjectSettings);

    public MetaProject getMetaProject();

    /**
     * Reloads the metaproject. Should be used with care, because it reloads
     * besides the user/password info, also the projects info. Clients should be
     * notified of the change.
     */
    public void reloadMetaProject();

    public void dispose();
    
    public UserData getUserAssociatedWithOpenId(String userOpenId);

    public String getUserSalt(String userName);

    public boolean allowsCreateUser(); 

}