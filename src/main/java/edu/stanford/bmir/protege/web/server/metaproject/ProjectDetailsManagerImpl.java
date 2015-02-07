package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectDetailsManagerImpl implements ProjectDetailsManager {

    public static final String DISPLAY_NAME_SLOT_NAME = "displayName";

    private MetaProject metaProject;

    @Inject
    public ProjectDetailsManagerImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
    }

    @Override
    public void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings) {
        addProjectToMetaProject(projectId, newProjectSettings);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    @Override
    public ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException {
        ProjectInstance pi = metaProject.getProject(projectId.getId());
        if (pi == null) {
            throw new UnknownProjectException(projectId);
        }
        return createProjectDetailsFromProjectInstance(pi);
    }

    @Override
    public int getProjectCount() {
        Set<ProjectInstance> projectInstances = metaProject.getProjects();
        return projectInstances.size();
    }

    @Override
    public boolean isExistingProject(ProjectId projectId) {
        return metaProject.getProject(projectId.getId()) != null;
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        if (userId.isGuest()) {
            return false;
        }
        ProjectInstance project = metaProject.getProject(projectId.getId());
        if (project == null) {
            return false;
        }
        User owner = project.getOwner();
        return owner != null && userId.getUserName().equals(owner.getName());
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

    /**
     * Adds a new project to the metaproject.  This sets up the name of the project and the description of the project
     * in the metaproject.  (Location is not set/used by this implementation - not all implementations use pprj files
     * anymore).
     * @param newProjectSettings The info about the new project
     */
    private void addProjectToMetaProject(ProjectId projectId, NewProjectSettings newProjectSettings) {
        ProjectInstance pi = metaProject.createProject(projectId.getId());
        pi.setDescription(newProjectSettings.getProjectDescription());
        final Instance protegeInstance = pi.getProtegeInstance();
        final KnowledgeBase kb = protegeInstance.getKnowledgeBase();
        final Slot displayNameSlot = kb.getSlot(DISPLAY_NAME_SLOT_NAME);
        protegeInstance.setOwnSlotValue(displayNameSlot, newProjectSettings.getDisplayName());
        User user = metaProject.getUser(newProjectSettings.getProjectOwner().getUserName());
        pi.setOwner(user);
    }
}
