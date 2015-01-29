package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.merge.MergeUploadedProjectWorkflow;
import edu.stanford.bmir.protege.web.client.merge.UploadAndMergeProjectWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class UploadAndMergeHandlerImpl implements UploadAndMergeHandler {

    @Override
    public void handleUploadAndMerge() {
        DispatchServiceManager dispatchServiceManager = DispatchServiceManager.get();
        MergeUploadedProjectWorkflow mergeWorkflow = new MergeUploadedProjectWorkflow(dispatchServiceManager);
        UploadAndMergeProjectWorkflow workflow = new UploadAndMergeProjectWorkflow(mergeWorkflow);
        Optional<ProjectId> currentProject = Application.get().getActiveProject();
        if (currentProject.isPresent()) {
            workflow.start(currentProject.get());
        }
    }
}
