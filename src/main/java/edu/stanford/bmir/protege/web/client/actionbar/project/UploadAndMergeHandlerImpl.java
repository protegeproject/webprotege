package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.merge.MergeUploadedProjectWorkflow;
import edu.stanford.bmir.protege.web.client.merge.UploadAndMergeProjectWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class UploadAndMergeHandlerImpl implements UploadAndMergeHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    @Inject
    public UploadAndMergeHandlerImpl(DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
    }

    @Override
    public void handleUploadAndMerge() {
        MergeUploadedProjectWorkflow mergeWorkflow = new MergeUploadedProjectWorkflow(dispatchServiceManager);
        UploadAndMergeProjectWorkflow workflow = new UploadAndMergeProjectWorkflow(mergeWorkflow);
        Optional<ProjectId> currentProject = activeProjectManager.getActiveProjectId();
        if (currentProject.isPresent()) {
            workflow.start(currentProject.get());
        }
    }
}
