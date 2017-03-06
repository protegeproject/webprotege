package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.topbar.TopBarPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public class ProjectPresenter implements HasDispose, HasProjectId {

    private final ProjectId projectId;

    private final ProjectView view;

    private final TopBarPresenter topBarPresenter;

    private final PerspectiveSwitcherPresenter linkBarPresenter;

    private final PerspectivePresenter perspectivePresenter;

    private final PermissionScreener permissionScreener;

    private final EventPollingManager eventPollingManager;


    @Inject
    public ProjectPresenter(ProjectId projectId,
                            ProjectView view,
                            EventPollingManager eventPollingManager,
                            TopBarPresenter topBarPresenter,
                            PerspectiveSwitcherPresenter linkBarPresenter,
                            PerspectivePresenter perspectivePresenter,
                            PermissionScreener permissionScreener) {
        this.projectId = projectId;
        this.view = view;
        this.eventPollingManager = eventPollingManager;
        this.permissionScreener = permissionScreener;
        this.topBarPresenter = topBarPresenter;
        this.linkBarPresenter = linkBarPresenter;
        this.perspectivePresenter = perspectivePresenter;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus,
                      @Nonnull ProjectViewPlace place) {
        GWT.log("[ProjectPresenter] Starting project presenter " + eventBus.getClass().getName());
        permissionScreener.checkPermission(VIEW_PROJECT.getActionId(),
                                           container,
                                           () -> displayProject(container, eventBus, place));
    }

    private void displayProject(@Nonnull AcceptsOneWidget container,
                                @Nonnull EventBus eventBus,
                                @Nonnull ProjectViewPlace place) {
        container.setWidget(view);
        topBarPresenter.start(view.getTopBarContainer(), eventBus);
        linkBarPresenter.start(view.getPerspectiveLinkBarViewContainer(), eventBus, place);
        perspectivePresenter.start(view.getPerspectiveViewContainer(), eventBus, place);
        eventPollingManager.start();
    }

    @Override
    public void dispose() {
        topBarPresenter.dispose();
        linkBarPresenter.dispose();
        perspectivePresenter.dispose();
        eventPollingManager.stop();
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectPresenter")
                .addValue(projectId)
                .toString();
    }
}
