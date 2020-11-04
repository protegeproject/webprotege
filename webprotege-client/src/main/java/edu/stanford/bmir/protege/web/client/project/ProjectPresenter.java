package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager;
import edu.stanford.bmir.protege.web.client.topbar.TopBarPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.LargeNumberOfChangesEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
@ProjectSingleton
public class ProjectPresenter implements HasDispose, HasProjectId {

    private final ProjectId projectId;

    private final ProjectView view;

    private final BusyView busyView;

    private final DispatchServiceManager dispatchServiceManager;

    private final TopBarPresenter topBarPresenter;

    private final PerspectiveSwitcherPresenter linkBarPresenter;

    private final PerspectivePresenter perspectivePresenter;

    private final PermissionScreener permissionScreener;

    private final EventPollingManager eventPollingManager;

    private final WebProtegeEventBus eventBus;

    private final ProjectTagsStyleManager projectTagsStyleManager;

    private final LargeNumberOfChangesManager largeNumberOfChangesHandler;


    @Inject
    public ProjectPresenter(ProjectId projectId,
                            ProjectView view,
                            BusyView busyView,
                            DispatchServiceManager dispatchServiceManager,
                            EventPollingManager eventPollingManager,
                            TopBarPresenter topBarPresenter,
                            PerspectiveSwitcherPresenter linkBarPresenter,
                            PerspectivePresenter perspectivePresenter,
                            PermissionScreener permissionScreener,
                            WebProtegeEventBus eventBus,
                            ProjectTagsStyleManager projectTagsStyleManager,
                            LargeNumberOfChangesManager largeNumberOfChangesHandler) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.eventPollingManager = eventPollingManager;
        this.permissionScreener = permissionScreener;
        this.topBarPresenter = topBarPresenter;
        this.linkBarPresenter = linkBarPresenter;
        this.perspectivePresenter = perspectivePresenter;
        this.eventBus = eventBus;
        this.projectTagsStyleManager = projectTagsStyleManager;
        this.largeNumberOfChangesHandler = largeNumberOfChangesHandler;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus,
                      @Nonnull ProjectViewPlace place) {
        GWT.log("[ProjectPresenter] Starting project presenter " + eventBus.getClass().getName());
        busyView.setMessage("Loading project.  Please wait.");
        container.setWidget(busyView);
        permissionScreener.checkPermission(VIEW_PROJECT.getActionId(),
                                           container,
                                           () -> displayProject(container, eventBus, place));
    }

    private void displayProject(@Nonnull AcceptsOneWidget container,
                                @Nonnull EventBus eventBus,
                                @Nonnull ProjectViewPlace place) {
        dispatchServiceManager.execute(new LoadProjectAction(projectId),
                                       result -> handleProjectLoaded(container, eventBus, place));
    }

    private void handleProjectLoaded(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus, @Nonnull ProjectViewPlace place) {
        dispatchServiceManager.beginBatch();
        topBarPresenter.start(view.getTopBarContainer(), eventBus, place);
        linkBarPresenter.start(view.getPerspectiveLinkBarViewContainer(), eventBus, place);
        perspectivePresenter.start(view.getPerspectiveViewContainer(), eventBus, place);
        eventPollingManager.start();
        eventBus.addHandlerToSource(LargeNumberOfChangesEvent.LARGE_NUMBER_OF_CHANGES,
                                    projectId,
                                    largeNumberOfChangesHandler);
        container.setWidget(view);
        dispatchServiceManager.execute(new GetProjectTagsAction(projectId),
                                       r -> projectTagsStyleManager.setProjectTags(r.getTags(), view));
        dispatchServiceManager.executeCurrentBatch();
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
