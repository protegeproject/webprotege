package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStoreImpl;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.*;
import java.util.logging.Logger;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public class ProjectManagerServiceImpl extends WebProtegeRemoteServiceServlet implements ProjectManagerService {


    public static final String IN_TRASH_SLOT_NAME = "inTrash";

    public ProjectManagerServiceImpl() {
    }

    public synchronized List<ProjectData> getProjects() {
        List<ProjectData> rawList = getMetaProjectManager().getProjectsData(null);
        return filterOutNonExistantProjects(rawList);
    }


    public synchronized List<ProjectData> getProjects(UserId userId) {
        // Don't think the call to get the project data from the metaproject is thread safe
        String userName = userId.getUserName();
        final List<ProjectData> projectsData = getMetaProjectManager().getProjectsData(userName);
        final ArrayList<ProjectData> rawList = new ArrayList<ProjectData>(projectsData);
        return filterOutNonExistantProjects(rawList);
    }


    private synchronized List<ProjectData> filterOutNonExistantProjects(List<ProjectData> rawList) {
        List<ProjectData> result = new ArrayList<ProjectData>(rawList.size() + 1);
        for (ProjectData projectData : rawList) {
            String projectName = projectData.getName();
            ProjectId projectId = new ProjectId(projectName);
            OWLAPIProjectDocumentStoreImpl docMan = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
            if (docMan.exists()) {
                result.add(projectData);
            }
            else {
                Logger logger = Logger.getLogger("ProjectManagerService");
                logger.warning("Filtering out non-existant project: " + projectId.getProjectName());
            }
        }
        return result;
    }

    public synchronized List<String> getProjectNames() {
        List<String> result = new ArrayList<String>();
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject mp = mpm.getMetaProject();
        for (ProjectInstance instance : mp.getProjects()) {
            String projectName = instance.getName();
            result.add(projectName);
        }
        return result;
    }

    public synchronized List<String> getOwnedProjectNames() {
        UserId userId = getUserInSession();
        if(userId.isNull()) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<String>();
        MetaProjectManager mpm = getMetaProjectManager();
        MetaProject mp = mpm.getMetaProject();
        for (ProjectInstance instance : mp.getProjects()) {
            User owner = instance.getOwner();
            if(owner != null && userId.getUserName().equals(owner.getName())) {
                String projectName = instance.getName();
                result.add(projectName);
            }
        }
        return result;
    }

    public synchronized boolean isRegisteredProject(ProjectId projectId) {
        if (projectId == null) {
            throw new NullPointerException("projectId must not be null");
        }
        ProjectInstance pi = Protege3ProjectManager.getProjectManager().getMetaProjectManager().getMetaProject().getProject(projectId.getProjectName());
        return pi != null;
    }

    public synchronized void createNewProject(NewProjectSettings newProjectSettings) throws NotSignedInException, ProjectAlreadyRegisteredException, ProjectDocumentExistsException {
        if (newProjectSettings == null) {
            throw new NullPointerException("newProjectSettings must not be null");
        }
        ensureSignedIn();

        ProjectId projectId = new ProjectId(newProjectSettings.getProjectName());
        
        if(isRegisteredProject(projectId)) {
            // Not allowed to overwrite
            if(isProjectExistsOnDisk(projectId)) {
                throw new ProjectAlreadyRegisteredException(projectId);
            }
            // For the time being, allow owners to put new sources in place
            else if(!isSignedInUserAllowedToOverwriteProjectSources(projectId)) {
                throw new ProjectAlreadyRegisteredException(projectId);
            }
        }
        else if(isProjectExistsOnDisk(projectId)) {
            // Too dangerous to do anything here.
            throw new ProjectDocumentExistsException(projectId);
        }
       


        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        pm.createNewProject(newProjectSettings);
        if (!isRegisteredProject(projectId)) {
            getMetaProjectManager().createProject(newProjectSettings);
            applyDefaultSharingSettings(projectId);
        }
    }

    private boolean isProjectExistsOnDisk(ProjectId projectId) {
        OWLAPIProjectDocumentStoreImpl docStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
        return docStore.exists();
    }

    /**
     * Detemines whether or not the signed in user is allowed to overwrite project source.  In this implementation, the
     * signed in user can overwrite project sources if they are either the project owner, or they are an admin.
     * @param projectId The project id that identifies the project sources to be overwritten.
     * @return <code>true</code> if the signed in user can overwrite project sources, otherwise <code>false</code>.
     */
    private boolean isSignedInUserAllowedToOverwriteProjectSources(ProjectId projectId) {
        return isSignedInUserProjectOwner(projectId) || isSignedInUserAdmin();
    }


    /**
     * Applies the default sharing setting to a project.  The default sharing settings are that the project is private,
     * but the signed in user is an editor.
     * @param projectId The project id that identifies the project to apply sharing settings to.
     */
    private void applyDefaultSharingSettings(ProjectId projectId) {
        List<UserSharingSetting> userSharingSettings = new ArrayList<UserSharingSetting>();
        UserId userInSession = getUserInSession();
        if (!userInSession.isNull()) {
            userSharingSettings.add(new UserSharingSetting(userInSession, SharingSetting.EDIT));
        }
        SharingSettingsManager.getManager().updateSharingSettings(new ProjectSharingSettings(projectId, SharingSetting.NONE, userSharingSettings));
    }

    public synchronized void moveProjectsToTrash(Set<ProjectId> projectIds) throws NotSignedInException, NotProjectOwnerException {
        ensureSignedIn();
        for (ProjectId projectId : projectIds) {
            MetaProject metaProject = getMetaProjectManager().getMetaProject();
            ProjectInstance pi = metaProject.getProject(projectId.getProjectName());
            Instance instance = pi.getProtegeInstance();
            KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
            Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
            if (inTrashSlot != null) {
                instance.setOwnSlotValue(inTrashSlot, Boolean.TRUE);
            }
        }
    }

    public synchronized void removeProjectsFromTrash(Set<ProjectId> projectIds) throws NotSignedInException, NotProjectOwnerException {
        ensureSignedIn();
        for (ProjectId projectId : projectIds) {
            MetaProject metaProject = getMetaProjectManager().getMetaProject();
            ProjectInstance pi = metaProject.getProject(projectId.getProjectName());
            Instance instance = pi.getProtegeInstance();
            KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
            Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
            if (inTrashSlot != null) {
                instance.setOwnSlotValue(inTrashSlot, Boolean.FALSE);
            }
        }
    }

    public long getLastAccessTime(ProjectId projectId) {
        return OWLAPIProjectManager.getProjectManager().getLastAccessTime(projectId);
    }
}
