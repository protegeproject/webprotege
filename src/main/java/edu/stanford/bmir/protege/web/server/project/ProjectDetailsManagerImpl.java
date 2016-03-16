package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.project.ProjectRecordTranslator.translateToProjectDetails;

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
        Optional<ProjectRecord> record = repository.findOne(projectId);
        if(!record.isPresent()) {
            throw new UnknownProjectException(projectId);
        }
        return translateToProjectDetails(record.get());
    }

    @Override
    public boolean isExistingProject(ProjectId projectId) {
        return repository.findOne(projectId).isPresent();
    }

    @Override
    public boolean isProjectOwner(UserId userId, ProjectId projectId) {
        if (userId.isGuest()) {
            return false;
        }
        Optional<ProjectRecord> record = repository.findOne(projectId);
        return record.isPresent() && userId.equals(record.get().getOwner());
    }



    @Override
    public void setInTrash(ProjectId projectId, boolean b) {
        Optional<ProjectRecord> record = repository.findOne(projectId);
        if(!record.isPresent()) {
            return;
        }
        ProjectRecord replacementRecord = record.get().builder().setInTrash(b).build();
        repository.save(replacementRecord);
    }

    @Override
    public void setProjectSettings(ProjectSettings projectSettings) {
        Optional<ProjectRecord> record = repository.findOne(projectSettings.getProjectId());
        if(!record.isPresent()) {
            return;
        }
        ProjectRecord updatedRecord = record.get().builder()
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
