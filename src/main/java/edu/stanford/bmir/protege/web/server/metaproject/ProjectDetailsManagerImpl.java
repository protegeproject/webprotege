package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
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
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectDetailsManagerImpl implements ProjectDetailsManager {

    public static final String DISPLAY_NAME_SLOT_NAME = "displayName";

    private static final String IN_TRASH_SLOT_NAME = "inTrash";

    private static final String PROJECT_TYPE_PROPERTY_NAME = "projectType";


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
        ProjectInstance pi = getProjectInstance(projectId);
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
        return getProjectInstance(projectId) != null;
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        if (userId.isGuest()) {
            return false;
        }
        ProjectInstance project = getProjectInstance(projectId);
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
     *
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


    public boolean isInTrash(ProjectId projectId) {
        ProjectInstance pi = getProjectInstance(projectId);
        if(pi == null) {
            throw new UnknownProjectException(projectId);
        }
        Instance instance = pi.getProtegeInstance();
        KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
        Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
        if (inTrashSlot == null) {
            return false;
        }
        Object val = instance.getOwnSlotValue(inTrashSlot);
        if (!(val instanceof Boolean)) {
            return false;
        }
        return (Boolean) val;
    }

    public void setInTrash(ProjectId projectId, boolean b) {
        ProjectInstance pi = getProjectInstance(projectId);
        if (pi == null) {
            return;
        }
        Instance instance = pi.getProtegeInstance();
        KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
        Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
        if (inTrashSlot != null) {
            instance.setOwnSlotValue(inTrashSlot, b);
        }
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    public OWLAPIProjectType getType(ProjectId projectId) {
        String defaultProjectTypeName = OWLAPIProjectType.getDefaultProjectType().getProjectTypeName();
        String projectType = getPropertyValue(projectId, PROJECT_TYPE_PROPERTY_NAME, defaultProjectTypeName);
        if (projectType.equals(OWLAPIProjectType.getOBOProjectType().getProjectTypeName())) {
            return OWLAPIProjectType.getOBOProjectType();
        } else {
            return OWLAPIProjectType.getDefaultProjectType();
        }
    }

    @Override
    public void setType(ProjectId projectId, OWLAPIProjectType projectType) {
        setPropertyValue(projectId, PROJECT_TYPE_PROPERTY_NAME, projectType.getProjectTypeName());
    }

    private void setPropertyValue(ProjectId projectId, String propertyName, String propertyValue) {
        ProjectInstance pi = getProjectInstance(projectId);
        PropertyValue pv = metaProject.createPropertyValue(propertyName, propertyValue);
        Set<PropertyValue> propertyValues = new HashSet<PropertyValue>(pi.getPropertyValues());
        for (Iterator<PropertyValue> it = propertyValues.iterator(); it.hasNext(); ) {
            PropertyValue curPv = it.next();
            if (curPv.getPropertyName().equals(propertyName)) {
                it.remove();
            }
        }
        propertyValues.add(pv);
        pi.setPropertyValues(propertyValues);
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    private String getPropertyValue(ProjectId projectId, String propertyName, String defaultValue) {
        ProjectInstance pi = getProjectInstance(projectId);
        String value = pi.getPropertyValue(propertyName);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }


    @Override
    public void setProjectSettings(ProjectSettings projectSettings) {
            ProjectId projectId = projectSettings.getProjectId();
            setType(projectId, new OWLAPIProjectType(projectSettings.getProjectType().getName()));

            setDescription(projectId, projectSettings.getProjectDescription());
            setDisplayName(projectId, projectSettings.getProjectDisplayName());
    }

    public void setDescription(ProjectId projectId, String description) {
        ProjectInstance pi = getProjectInstance(projectId);
        pi.setDescription(description);
    }

    @Override
    public ProjectSettings getProjectSettings(ProjectId projectId) throws UnknownProjectException{
        ProjectDetails projectDetails = getProjectDetails(projectId);
            return new ProjectSettings(projectId,
                    new ProjectType(getType(projectId).getProjectTypeName()), projectDetails.getDisplayName(),
                    projectDetails.getDescription());
    }

    public void setDisplayName(ProjectId projectId, String displayName) {
        checkNotNull(displayName);
        ProjectInstance pi = getProjectInstance(projectId);
        Slot displayNameSlot = pi.getProtegeInstance().getKnowledgeBase().getSlot("displayName");
        if(displayNameSlot != null) {
            // If it's not present then it's some really old version of the metaproject
            pi.getProtegeInstance().setDirectOwnSlotValue(displayNameSlot, displayName);
        }
    }

    private ProjectInstance getProjectInstance(ProjectId projectId) {
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());
        if (projectInstance == null) {
            throw new UnknownProjectException(projectId);
        }
        return projectInstance;
    }


}
