package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class Project implements HasProjectId, HasDispose {

    private final ProjectDetails projectDetails;

    private ProjectLayoutConfiguration projectLayoutConfiguration;

    private EventPollingManager eventPollingManager;

    /**
     * Creates a project from the specified details.
     * @param projectDetails The details.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    @Inject
    public Project(ProjectDetails projectDetails, EventBus eventBus, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.projectDetails = checkNotNull(projectDetails);
        this.eventPollingManager = EventPollingManager.get(10 * 1000, projectDetails.getProjectId(), eventBus, dispatchServiceManager, loggedInUserProvider);
        eventPollingManager.start();
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

    public void setProjectLayoutConfiguration(ProjectLayoutConfiguration projectLayoutConfiguration) {
        this.projectLayoutConfiguration = projectLayoutConfiguration;
    }

    public ProjectLayoutConfiguration getProjectLayoutConfiguration() {
        return projectLayoutConfiguration;
    }

    public void dispose() {
        // TODO: we might notify the session that project has been closed
        eventPollingManager.stop();
    }
}
