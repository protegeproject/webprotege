package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.topbar.TopBarPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
// TODO: Auto-generate
public class ProjectPresenterFactory {

    @Nonnull
    private final ProjectView view;

    @Nonnull
    private final EventPollingManager eventPollingManager;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final TopBarPresenter topBarPresenter;

    @Nonnull
    private final PerspectiveSwitcherPresenter perspectiveSwitcherPresenter;

    @Nonnull
    private final PerspectivePresenter perspectivePersenter;

    @Nonnull
    private final PermissionScreener permissionScreener;

    @Inject
    public ProjectPresenterFactory(@Nonnull ProjectView view,
                                   @Nonnull EventPollingManager eventPollingManager,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull LoggedInUserProvider loggedInUserProvider,
                                   @Nonnull TopBarPresenter topBarPresenter,
                                   @Nonnull PerspectiveSwitcherPresenter perspectiveSwitcherPresenter,
                                   @Nonnull PerspectivePresenter perspectivePersenter,
                                   @Nonnull PermissionScreener permissionScreener) {
        this.view = view;
        this.eventPollingManager = eventPollingManager;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
        this.topBarPresenter = topBarPresenter;
        this.perspectiveSwitcherPresenter = perspectiveSwitcherPresenter;
        this.perspectivePersenter = perspectivePersenter;
        this.permissionScreener = permissionScreener;
    }

    public ProjectPresenter createProjectPresenter(ProjectId projectId) {
        return new ProjectPresenter(projectId,
                                    view,
                                    eventPollingManager,
                                    dispatchServiceManager,
                                    loggedInUserProvider,
                                    topBarPresenter,
                                    perspectiveSwitcherPresenter,
                                    perspectivePersenter,
                                    permissionScreener);
    }
}
