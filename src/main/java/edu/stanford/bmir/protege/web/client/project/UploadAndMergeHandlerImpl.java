package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.merge.UploadAndMergeProjectWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class UploadAndMergeHandlerImpl implements UploadAndMergeHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<UploadAndMergeProjectWorkflow> workflowProvider;

    @Inject
    public UploadAndMergeHandlerImpl(@Nonnull ProjectId projectId,
                                     @Nonnull Provider<UploadAndMergeProjectWorkflow> workflowProvider) {
        this.projectId = checkNotNull(projectId);
        this.workflowProvider = checkNotNull(workflowProvider);
    }

    @Override
    public void handleUploadAndMerge() {
        UploadAndMergeProjectWorkflow workflow = workflowProvider.get();
        workflow.start(projectId);
    }
}
