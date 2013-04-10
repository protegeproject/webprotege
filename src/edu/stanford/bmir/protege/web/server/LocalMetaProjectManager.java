package edu.stanford.bmir.protege.web.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.server.ServerProperties;
import edu.stanford.smi.protege.server.metaproject.*;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;
import edu.stanford.smi.protege.storage.database.DatabaseKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;


public class LocalMetaProjectManager extends AbstractMetaProjectManager {


    private MetaProject metaproject;

    boolean runsInClientServerMode;

    //cache for performance reasons
//    private ArrayList<ProjectData> projectData;

    private int _saveIntervalMsec = edu.stanford.bmir.protege.web.server.ApplicationProperties.NO_SAVE;

    //thread that save periodically the projects for local mode
    private Thread _updateThread;

    public LocalMetaProjectManager() {
        metaproject = new MetaProjectImpl(ApplicationProperties.getLocalMetaprojectURI());
        //automatic save logic
        int saveInt = ApplicationProperties.getLocalProjectSaveInterval();
        if (saveInt != ApplicationProperties.NO_SAVE) {
            _saveIntervalMsec = saveInt * 1000;
        }
        startProjectUpdateThread();
    }

    public MetaProject getMetaProject() {
        return metaproject;
    }

    public Project openProject(String projectName) {
        Project project = null;
        URI uri = getProjectURI(projectName);
        Log.getLogger().info("Loading project " + projectName + " from " + uri);

        Collection errors = new ArrayList();
        try {
            project = Project.loadProjectFromURI(uri, errors);
            if (errors.size() > 0) {
                Log.getLogger().warning("There were errors at loading project " + projectName);
                Log.handleErrors(Log.getLogger(), Level.WARNING, errors);
            }
        }
        catch (Throwable e) {
            Log.getLogger().log(Level.WARNING, "There were exceptions at loading project " + projectName, e);
            throw new RuntimeException("Cannot open project " + projectName, e);
        }
        return project;
    }

    public void createProject(NewProjectSettings newProjectSettings) {
        addProjectToMetaProject(newProjectSettings);
        rebuildCaches();
    }
//
//    public Project createProject(String sourceFileName, NewProjectInfo newProjectInfo, String userName) {
//        try {
//            String projectName = newProjectInfo.getProjectName();
//            LocalProjectLocationDescriptor descriptor = new LocalOWLProjectLocationDescriptor(projectName);
//            File uploadsDirectory = getUploadsDirectory();
//            File sourceFile = new File(uploadsDirectory, sourceFileName);
//            Project project = descriptor.initialiseProjectWithSources(sourceFile);
//            addProjectToMetaProject(descriptor, newProjectInfo, userName);
//            rebuildCaches();
//            return project;
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//


    /**
     * Adds a new project to the metaproject.  This sets up the name of the project and the description of the project
     * in the metaproject.  (Location is not set/used by this implementation - not all implementations use pprj files
     * anymore).
     * @param newProjectSettings The info about the new project
     */
    private void addProjectToMetaProject(NewProjectSettings newProjectSettings) {
        MetaProject mp = getMetaProject();
        ProjectInstance pi = mp.createProject(newProjectSettings.getProjectName());
        pi.setDescription(newProjectSettings.getProjectDescription());
        User user = mp.getUser(newProjectSettings.getProjectOwner().getUserName());
        pi.setOwner(user);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(this);
        rebuildCaches();
    }

//    /**
//     * Gets the directory where project source files - i.e. OWL files are uploaded to.
//     * @return The directory where source files are uploaded to.
//     */
//    private File getUploadsDirectory() {
//        URI webProtegeDirectory = ApplicationProperties.getWeprotegeDirectory();
//        return new File(new File(webProtegeDirectory), FileUploadConstants.UPLOADS_DIRECTORY.getName());
//    }

    public void moveProjectToTrash(String userName, String name) {
        MetaProject mp = getMetaProject();
        ProjectInstance projectInstance = mp.getProject(name);
        String projectOwnerName = projectInstance.getOwner().getName();
        if (!userName.equals(projectOwnerName)) {
            throw new RuntimeException("A project can only be moved to the trash by its owner");
        }
        Instance instance = projectInstance.getProtegeInstance();
        Slot inTrashSlot = instance.getKnowledgeBase().getSlot("inTrash");
        instance.setOwnSlotValue(inTrashSlot, Boolean.TRUE);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(this);
        rebuildCaches();
    }

    public void removeProjectFromTrash(String name) {
    }

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
        for(ProjectInstance projectInstance : metaproject.getProjects()) {
            final String name = projectInstance.getName();
            if (name != null) {
                final ProjectId projectId = ProjectId.get(name);
                if(isAuthorisedToReadAndList(policy, user, projectInstance)) {
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
        ProjectInstance pi = metaproject.getProject(projectId.getProjectName());
        if(pi == null) {
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
        return new ProjectDetails(projectId, projectId.getProjectName(), description, ownerId, inTrash);
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
                if(projectUser != null) {
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
        if(user == null) {
            return isWorldAllowedOperation(projectInstance, MetaProjectConstants.OPERATION_READ);
        }
        else if(isAdminUser(user)) {
            return true;
        }
        else
            return policy.isOperationAuthorized(user, MetaProjectConstants.OPERATION_READ, projectInstance);

    }
    
    private boolean isAdminUser(User user) {
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
    

    private boolean isAuthorisedToDisplayInList(Policy policy, User user, ProjectInstance projectInstance) {
        Operation operation = MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST;
        if(user == null) {
            return isWorldAllowedOperation(projectInstance, operation);
        }
        else {
            return isAdminUser(user) || policy.isOperationAuthorized(user, operation, projectInstance);
        }

    }

    private boolean isWorldAllowedOperation(ProjectInstance projectInstance, Operation operation) {
        for(GroupOperation groupOperation : projectInstance.getAllowedGroupOperations()) {
            Group group = groupOperation.getAllowedGroup();
            String groupName = group.getName();
            if(MetaProjectConstants.USER_WORLD.equals(groupName)) {
                Set<Operation> operations = groupOperation.getAllowedOperations();
                if(operations.contains(operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Path methods
     */
    public URI getProjectURI(String projectName) {
        for (ProjectInstance projectInstance : metaproject.getProjects()) {
            String name = projectInstance.getName();
            if (name.equals(projectName)) {
                String path = projectInstance.getLocation();
                URL url = URIUtilities.toURL(path, ApplicationProperties.getWeprotegeDirectory());
                URI uri = null;
                try {
                    uri = url.toURI();
                }
                catch (URISyntaxException e) {
                    Log.getLogger().log(Level.SEVERE, "Error at getting path for project " + projectName + ". Computed path: " + url, e);
                }
                return uri;
            }
        }
        return null;
    }

    public void init(boolean loadOntologiesFromServer) {
        runsInClientServerMode = loadOntologiesFromServer;
    }

    /* (non-Javadoc)
     * @see edu.stanford.bmir.protege.web.server.MetaProjectManager#reloadMetaProject()
     */
    public void reloadMetaProject() {
        if (metaproject != null) {
            ((MetaProjectImpl) metaproject).getKnowledgeBase().getProject().dispose();
        }
        metaproject = new MetaProjectImpl(ApplicationProperties.getLocalMetaprojectURI());
    }


    /*
     * Saving automatically the projects - to be removed when integrated with server	 *
     */
    private void startProjectUpdateThread() {
        if (_saveIntervalMsec != edu.stanford.bmir.protege.web.server.ApplicationProperties.NO_SAVE) {
            _updateThread = new Thread("Save Projects") {
                @Override
                public void run() {
                    try {
                        while (true) {
                            synchronized (LocalMetaProjectManager.this) {
                                LocalMetaProjectManager.this.wait(_saveIntervalMsec);
                            }
                            if (_updateThread != this) {
                                break;
                            }
                            saveAllProjects();
                        }
                    }
                    catch (Throwable e) {
                        Log.getLogger().log(Level.INFO, "Exception caught", e);
                    }
                }
            };
            _updateThread.setDaemon(true);
            _updateThread.start();
        }
    }

    //just for the local loading of ontologies
    private void saveAllProjects() {
//        try {
//            for (ServerProject<Project> serverProject : Protege3ProjectManager.getProjectManager().getOpenServerProjects()) {
//                Project prj = serverProject.getProject();
//                if (prj != null) {
//                    save(prj);
//                    prj.getKnowledgeBase().setChanged(false);
//
//                    KnowledgeBase changesKb = ChAOUtil.getChangesKb(prj.getKnowledgeBase());
//                    if (changesKb != null && changesKb.hasChanged()) {
//                        save(changesKb.getProject());
//                        changesKb.setChanged(false);
//                    }
//                }
//            }
//        }
//        catch (Exception e) {
//            Log.getLogger().log(Level.WARNING, "Errors at saving server projects", e);
//        }
        throw new RuntimeException("BROKEN");
    }

    //just for the local loading of ontologies
    private static void save(Project project) {
        Log.getLogger().info("Saving " + project);
        Collection errors = new ArrayList();
        /*
         * The order of these synchronize statements is critical.  There is some
         * OWLFrameStore code (which holds the knowledgebase lock) that makes calls
         * to the internal project knowledge base to get configuration parameters.
         */
        synchronized (project.getKnowledgeBase()) {
            synchronized (project.getInternalProjectKnowledgeBase()) {
                /* TT: Save only the domain kb, not the prj kb.
                 * Saving the prj kb while a client opens a
                 * remote project can corrupt the client prj kb.
                 */
                KnowledgeBase kb = project.getKnowledgeBase();
                KnowledgeBaseFactory factory = kb.getKnowledgeBaseFactory();
                if (!(factory instanceof DatabaseKnowledgeBaseFactory)) {
                    factory.saveKnowledgeBase(kb, project.getSources(), errors);
                }
            }
        }
        dumpErrors(project, errors);
    }

    private static void dumpErrors(Project p, Collection errors) {
        if (!errors.isEmpty()) {
            Log.getLogger().warning("Unable to save project " + p);
            Iterator i = errors.iterator();
            while (i.hasNext()) {
                Object o = i.next();
                Log.getLogger().warning("\t" + o.toString());
            }
        }
    }

    public void dispose() {
        metaproject.dispose();
        _updateThread = null;
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
