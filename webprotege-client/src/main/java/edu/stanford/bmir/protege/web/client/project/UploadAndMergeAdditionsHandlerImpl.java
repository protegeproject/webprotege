package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.merge_add.UploadAndMergeAdditionsProjectsWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public class UploadAndMergeAdditionsHandlerImpl implements UploadAndMergeAdditionsHandler{

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<UploadAndMergeAdditionsProjectsWorkflow> uploadAndMergeAdditionsProjectsWorkflowProvider;

    @Inject
    public UploadAndMergeAdditionsHandlerImpl(@Nonnull ProjectId projectId,@Nonnull Provider<UploadAndMergeAdditionsProjectsWorkflow> uploadAndMergeAdditionsProjectsWorkflowProvider) {
        this.projectId = checkNotNull(projectId);
        this.uploadAndMergeAdditionsProjectsWorkflowProvider = checkNotNull(uploadAndMergeAdditionsProjectsWorkflowProvider);
    }

    @Override
    public void handleUploadAndMergeAdditions() {
        UploadAndMergeAdditionsProjectsWorkflow uploadAndMergeAdditionsProjectsWorkflow = uploadAndMergeAdditionsProjectsWorkflowProvider.get();
        uploadAndMergeAdditionsProjectsWorkflow.start(projectId);
    }
}
