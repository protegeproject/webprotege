package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectType;
import edu.stanford.bmir.protege.web.server.project.ProjectRecord;
import edu.stanford.bmir.protege.web.server.project.ProjectRecordRepository;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ProjectDetailsManagerImpl implements ProjectDetailsManager {

    private final ProjectRecordRepository repository;

    @Inject
    public ProjectDetailsManagerImpl(ProjectRecordRepository repository) {
        this.repository = checkNotNull(repository);
    }

    @Override
    public void registerProject(ProjectId projectId, NewProjectSettings settings) {
        ProjectRecord record = new ProjectRecord(
                projectId,
                settings.getProjectOwner(),
                settings.getDisplayName(),
                settings.getProjectDescription(),
                false);
        repository.save(record);
    }

    @Override
    public ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException {
        ProjectRecord record = repository.findOne(projectId);
        if(record == null) {
            throw new UnknownProjectException(projectId);
        }
        return createProjectDetails(record);
    }

    @Override
    public boolean isExistingProject(ProjectId projectId) {
        return repository.findOne(projectId) != null;
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        if (userId.isGuest()) {
            return false;
        }
        ProjectRecord record = repository.findOne(projectId);
        return record != null && userId.equals(record.getOwner());
    }

    private static ProjectDetails createProjectDetails(ProjectRecord record) {
        final ProjectId projectId = record.getProjectId();
        final String displayName = record.getDisplayName();
        final String description = record.getDescription();
        final UserId ownerId = record.getOwner();
        final boolean inTrash = record.isInTrash();
        return new ProjectDetails(projectId, displayName, description, ownerId, inTrash);
    }

    @Override
    public void setInTrash(ProjectId projectId, boolean b) {
        ProjectRecord record = repository.findOne(projectId);
        if(record == null) {
            return;
        }
        ProjectRecord replacementRecord = record.builder().setInTrash(b).build();
        repository.save(replacementRecord);
    }

    @Override
    public void setProjectSettings(ProjectSettings projectSettings) {
        ProjectRecord record = repository.findOne(projectSettings.getProjectId());
        if(record == null) {
            return;
        }
        ProjectRecord updatedRecord = record.builder()
                .setDisplayName(projectSettings.getProjectDisplayName())
                .setDescription(projectSettings.getProjectDescription())
                .build();
        repository.save(updatedRecord);
    }

    @Override
    public ProjectSettings getProjectSettings(ProjectId projectId) throws UnknownProjectException{
        ProjectDetails projectDetails = getProjectDetails(projectId);
            return new ProjectSettings(projectId,
                    projectDetails.getDisplayName(),
                    projectDetails.getDescription());
    }

}
