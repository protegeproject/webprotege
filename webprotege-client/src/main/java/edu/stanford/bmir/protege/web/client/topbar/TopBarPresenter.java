package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.help.HelpPresenter;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.project.ProjectMenuPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingButtonPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class TopBarPresenter implements HasDispose {

    private final TopBarView view;

    private final ActiveProjectManager activeProjectManager;

    private final GoToHomePresenter goToHomePresenter;

    private final LoggedInUserPresenter loggedInUserPresenter;

    private final ProjectMenuPresenter projectMenuPresenter;

    private final HelpPresenter helpPresenter;

    private final SharingButtonPresenter sharingButtonPresenter;



    @Inject
    public TopBarPresenter(@Nonnull TopBarView view,
                           @Nonnull ActiveProjectManager projectDetailsProvider,
                           @Nonnull GoToHomePresenter goToHomePresenter,
                           @Nonnull ProjectMenuPresenter projectMenuPresenter,
                           @Nonnull SharingButtonPresenter sharingButtonPresenter,
                           @Nonnull LoggedInUserPresenter loggedInUserPresenter,
                           @Nonnull HelpPresenter helpPresenter) {
        this.view = view;
        this.activeProjectManager = projectDetailsProvider;
        this.projectMenuPresenter = projectMenuPresenter;
        this.goToHomePresenter = goToHomePresenter;
        this.sharingButtonPresenter = sharingButtonPresenter;
        this.loggedInUserPresenter = loggedInUserPresenter;
        this.helpPresenter = helpPresenter;
    }

    /**
     * Starts the {@link TopBarPresenter}
     * @param container The container for the associated view.
     * @param eventBus An event bus.  The event bus will be reset when the presenter is stopped.
     *                 It is therefore unnecessary for users to remove handlers.
     */
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus,
                      @Nonnull ProjectViewPlace place) {
        container.setWidget(view);
        goToHomePresenter.start(view.getGoToHomeContainer(), eventBus);
        projectMenuPresenter.start(view.getProjectMenuContainer(), eventBus);
        loggedInUserPresenter.start(view.getLoggedInUserContainer(), eventBus);
        helpPresenter.start(view.getHelpContainer(), eventBus);
        sharingButtonPresenter.start(view.getSharingSettingsContainer(), eventBus);
        activeProjectManager.getActiveProjectDetails(projectDetails -> {
            String displayName = projectDetails.map(ProjectDetails::getDisplayName).orElse("");
            view.setProjectTitle(displayName);
        });
    }

    @Override
    public void dispose() {
        goToHomePresenter.dispose();
        loggedInUserPresenter.dispose();
        sharingButtonPresenter.dispose();
        projectMenuPresenter.dispose();
        helpPresenter.dispose();
    }
}
