package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class Project implements HasProjectId, HasDispose {

    private final ProjectDetails projectDetails;

    /**
     * Creates a project from the specified details.
     * @param projectDetails The details.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    @Inject
    public Project(ProjectDetails projectDetails, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.projectDetails = checkNotNull(projectDetails);
    }

    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

    public String getDisplayName() {
        return projectDetails.getDisplayName();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void dispose() {
        // TODO: we might notify the session that project has been closed
    }
}
