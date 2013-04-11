package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.ServerProperties;
import edu.stanford.smi.protege.server.metaproject.*;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import edu.stanford.smi.protege.util.Log;

import java.io.File;
import java.util.*;
import java.util.logging.Level;


public class LocalMetaProjectManager extends AbstractMetaProjectManager {


    private static final String METAPROJECT_DIRECTORY = "metaproject";

    private static final String METAPROJECT_PPRJ_FILE_NAME = "metaproject.pprj";


    private MetaProject metaproject;

    boolean runsInClientServerMode;

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
        rebuildCaches();
    }

//
//    public void moveProjectToTrash(String userName, String name) {
//        MetaProject mp = getMetaProject();
//        ProjectInstance projectInstance = mp.getProject(name);
//        String projectOwnerName = projectInstance.getOwner().getName();
//        if (!userName.equals(projectOwnerName)) {
//            throw new RuntimeException("A project can only be moved to the trash by its owner");
//        }
//        Instance instance = projectInstance.getProtegeInstance();
//        Slot inTrashSlot = instance.getKnowledgeBase().getSlot("inTrash");
//        instance.setOwnSlotValue(inTrashSlot, Boolean.TRUE);
//        OWLAPIMetaProjectStore.getStore().saveMetaProject(this);
//        rebuildCaches();
//    }

//    public void removeProjectFromTrash(String name) {
//    }

    private void rebuildCaches() {
//        projectData = null;
    }


    public UserData registerUser(String userName, String password) throws UserRegistrationException {
//        if (!ServerProperties.getAllowsCreateUsers()) {
//            throw new RuntimeException(
//            "The server does not allow the creation of new users. Please contact the administartor to create a new user account.");
//        }
        User existingUser = metaproject.getUser(userName);
        if (existingUser != null) {
            throw new UserNameAlreadyExistsException(userName);
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
        final UserId ownerId = projectOwner != null ? UserId.getUserId(projectOwner.getName()) : UserId.getNull();
        final boolean inTrash = isInTrash(projectInstance);
        final Slot displayNameSlot = projectInstance.getProtegeInstance().getKnowledgeBase().getSlot("displayName");
        final String displayName = (String) projectInstance.getProtegeInstance().getOwnSlotValue(displayNameSlot);
        return new ProjectDetails(projectId, displayName, description, ownerId, inTrash);
    }


    public List<ProjectData> getProjectsData(String userName) {
//        if (projectData != null) {
//            return projectData;
//        }

        if (userName == null) {
            userName = "Guest";
        }
        //TODO: check with Tim if it needs synchronization
        List<ProjectData> projectData = new ArrayList<ProjectData>();

        Policy policy = metaproject.getPolicy();
        User user = policy.getUserByName(userName);

        for (ProjectInstance projectInstance : metaproject.getProjects()) {
            if (!isAuthorisedToReadAndList(policy, user, projectInstance)) {
                continue;
            }

            try {
                String description = projectInstance.getDescription();
                String location = projectInstance.getLocation();
                String name = projectInstance.getName();
                User projectUser = projectInstance.getOwner();
                String owner = "";
                if (projectUser != null) {
                    owner = projectUser.getName();
                }
                boolean inTrash = isInTrash(projectInstance);

                ProjectData pd = new ProjectData(description, location, name, owner, inTrash);

                Log.getLogger().info("Found project def in metaproject: " + pd.getName() + " at: " + pd.getLocation());
                projectData.add(pd);

            }
            catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Found project def with problems: " + projectInstance + " Message: " + e.getMessage(), e);
            }
        }

        Collections.sort(projectData, new ProjectsDataComparator());

        return projectData;
    }

    private boolean isAuthorisedToReadAndList(Policy policy, User user, ProjectInstance projectInstance) {
        User owner = projectInstance.getOwner();
        return (user != null && owner != null && owner.equals(user)) || isAuthorisedToDisplayInList(policy, user, projectInstance) && isAuthorisedToRead(policy, user, projectInstance);
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
            if ("AdminGroup".equals(group.getName())) {
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
        runsInClientServerMode = loadOntologiesFromServer;
    }

//    public URI getMetaProjectURI() {
//        return LocalMetaProjectManager.getMetaProjectFile().toURI();
//    }

//    /* (non-Javadoc)
//     * @see edu.stanford.bmir.protege.web.server.MetaProjectManager#reloadMetaProject()
//     */
//    public void reloadMetaProject() {
//        if (metaproject != null) {
//            ((MetaProjectImpl) metaproject).getKnowledgeBase().getProject().dispose();
//        }
//        metaproject = new MetaProjectImpl(getMetaProjectURI());
//    }

    public void dispose() {
        metaproject.dispose();
        synchronized (this) {
            notifyAll();
        }
    }

    /*
     * Helper class
     */
    class ProjectsDataComparator implements Comparator<ProjectData> {

        public int compare(ProjectData prj1, ProjectData prj2) {
            return prj1.getName().compareTo(prj2.getName());
        }
    }


    public String getUserSalt(String userName) {
        User user = getMetaProject().getUser(userName);
        if (user == null) {
            return null;
        }
        return user.getSalt();
    }

    public boolean allowsCreateUser() {
        return ServerProperties.getAllowsCreateUsers();
    }
}
