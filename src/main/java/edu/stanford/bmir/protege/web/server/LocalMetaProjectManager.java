package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserNameAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.ServerProperties;
import edu.stanford.smi.protege.server.metaproject.*;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;


public class LocalMetaProjectManager extends AbstractMetaProjectManager {


    private static final String ADMIN_GROUP = "AdminGroup";

	private static final String METAPROJECT_DIRECTORY = "metaproject";

    private static final String METAPROJECT_PPRJ_FILE_NAME = "metaproject.pprj";


    private MetaProject metaproject;

    public LocalMetaProjectManager() {
        File metaProjectFile = getMetaProjectFile();
        metaproject = new MetaProjectImpl(metaProjectFile.toURI());

        UUIDMigrator migrator = new UUIDMigrator(this);
        migrator.runMigrator();
    }

    public static File getMetaProjectFile() {
        File metaProjectDirectory = new File(WebProtegeFileStore.getInstance().getDataDirectory(), METAPROJECT_DIRECTORY);
        return new File(metaProjectDirectory, METAPROJECT_PPRJ_FILE_NAME);
    }

    public MetaProject getMetaProject() {
        return metaproject;
    }

    public void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings) {
        addProjectToMetaProject(projectId, newProjectSettings);
    }


    /**
     * Adds a new project to the metaproject.  This sets up the name of the project and the description of the project
     * in the metaproject.  (Location is not set/used by this implementation - not all implementations use pprj files
     * anymore).
     * @param newProjectSettings The info about the new project
     */
    private void addProjectToMetaProject(ProjectId projectId, NewProjectSettings newProjectSettings) {
        MetaProject mp = getMetaProject();
        ProjectInstance pi = mp.createProject(projectId.getId());
        pi.setDescription(newProjectSettings.getProjectDescription());
        final Instance protegeInstance = pi.getProtegeInstance();
        final KnowledgeBase kb = protegeInstance.getKnowledgeBase();
        final Slot displayNameSlot = kb.getSlot("displayName");
        protegeInstance.setOwnSlotValue(displayNameSlot, newProjectSettings.getDisplayName());
        User user = mp.getUser(newProjectSettings.getProjectOwner().getUserName());
        pi.setOwner(user);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(this);
    }


    public UserData registerUser(String userName, String email, String password) throws UserRegistrationException {
        checkNotNull(userName);
        checkNotNull(email);
        checkNotNull(password);
        User existingUser = metaproject.getUser(userName);
        if (existingUser != null) {
            throw new UserNameAlreadyExistsException(userName);
        }
        for(User user : metaproject.getUsers()) {
            if(email.equals(user.getEmail())) {
                throw new UserEmailAlreadyExistsException(email);
            }
        }
        User newUser = metaproject.createUser(userName, password);
        return AuthenticationUtil.createUserData(UserId.getUserId(newUser.getName()));
    }

    private static Slot getInTrashSlot(KnowledgeBase kb) {
        Slot inTrashSlot = kb.getSlot("inTrash");
        if (inTrashSlot == null) {
            throw new RuntimeException("inTrash slot is not defined in meta-project");
        }
        return inTrashSlot;
    }

    private static boolean isInTrash(ProjectInstance projectInstance) {
        Instance protegeInstance = projectInstance.getProtegeInstance();
        KnowledgeBase kb = protegeInstance.getKnowledgeBase();
        Slot inTrashSlot = getInTrashSlot(kb);
        Object ownSlotValue = protegeInstance.getOwnSlotValue(inTrashSlot);
        return ownSlotValue != null && ownSlotValue.equals(Boolean.TRUE);
    }

    public List<ProjectDetails> getListableReadableProjects(UserId userId) {
        Policy policy = metaproject.getPolicy();
        User user = policy.getUserByName(userId.getUserName());
        List<ProjectDetails> result = new ArrayList<ProjectDetails>();
        for (ProjectInstance projectInstance : metaproject.getProjects()) {
            final String name = projectInstance.getName();
            if (name != null && ProjectId.isWelFormedProjectId(name)) {
                final ProjectId projectId = ProjectId.get(name);
                if (isAuthorisedToReadAndList(policy, user, projectInstance)) {
                    OWLAPIProjectDocumentStore ds = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
                    if (ds.exists()) {
                        final ProjectDetails projectDetails = createProjectDetailsFromProjectInstance(projectInstance);
                        result.add(projectDetails);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException {
        ProjectInstance pi = metaproject.getProject(projectId.getId());
        if (pi == null) {
            throw new UnknownProjectException(projectId);
        }
        return createProjectDetailsFromProjectInstance(pi);
    }

    private static ProjectDetails createProjectDetailsFromProjectInstance(ProjectInstance projectInstance) {
        final ProjectId projectId = ProjectId.get(projectInstance.getName());
        final String description = projectInstance.getDescription();
        final User projectOwner = projectInstance.getOwner();
        final UserId ownerId = projectOwner != null ? UserId.getUserId(projectOwner.getName()) : UserId.getGuest();
        final boolean inTrash = isInTrash(projectInstance);
        final Slot displayNameSlot = projectInstance.getProtegeInstance().getKnowledgeBase().getSlot("displayName");
        final String displayName = (String) projectInstance.getProtegeInstance().getOwnSlotValue(displayNameSlot);
        return new ProjectDetails(projectId, displayName, description, ownerId, inTrash);
    }

    private boolean isAuthorisedToReadAndList(Policy policy, User user, ProjectInstance projectInstance) {
        User owner = projectInstance.getOwner();
        if(isUserOwner(user, owner)) {
        	return true;
        }
        return isAuthorisedToDisplayInList(policy, user, projectInstance) && isAuthorisedToRead(policy, user, projectInstance);
    }

	private boolean isUserOwner(User user, User owner) {
		return user != null && owner != null && owner.equals(user);
	}

    private boolean isAuthorisedToRead(Policy policy, User user, ProjectInstance projectInstance) {
        if (user == null) {
            return isWorldAllowedOperation(projectInstance, MetaProjectConstants.OPERATION_READ);
        }
        else if (isAdminUser(user)) {
            return true;
        }
        else
            return policy.isOperationAuthorized(user, MetaProjectConstants.OPERATION_READ, projectInstance);

    }

    private boolean isAdminUser(User user) {
        if (user == null) {
            return false;
        }
        for (Group group : user.getGroups()) {
            if (ADMIN_GROUP.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }


    private boolean isAuthorisedToDisplayInList(Policy policy, User user, ProjectInstance projectInstance) {
        Operation operation = MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST;
        if (user == null) {
            return isWorldAllowedOperation(projectInstance, operation);
        }
        else {
            return isAdminUser(user) || policy.isOperationAuthorized(user, operation, projectInstance);
        }

    }

    private boolean isWorldAllowedOperation(ProjectInstance projectInstance, Operation operation) {
        for (GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            Group group = groupOperation.getAllowedGroup();
            String groupName = group.getName();
            if (MetaProjectConstants.USER_WORLD.equals(groupName)) {
                Set<Operation> operations = groupOperation.getAllowedOperations();
                if (operations.contains(operation)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void init(boolean loadOntologiesFromServer) {
    }


    public void dispose() {
        metaproject.dispose();
        synchronized (this) {
            notifyAll();
        }
    }


    public boolean allowsCreateUser() {
        return ServerProperties.getAllowsCreateUsers();
    }
}
