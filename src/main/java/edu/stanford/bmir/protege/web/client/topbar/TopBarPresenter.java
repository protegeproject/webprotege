package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.help.HelpPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectMenuPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingButtonPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class TopBarPresenter implements HasDispose, Presenter {

    private final TopBarView view;

    private final GoToHomePresenter goToHomePresenter;

    private final LoggedInUserPresenter loggedInUserPresenter;

    private final ProjectMenuPresenter projectMenuPresenter;

    private final HelpPresenter helpPresenter;

    private final SharingButtonPresenter sharingButtonPresenter;

    @Inject
    public TopBarPresenter(@Nonnull TopBarView view,
                           @Nonnull GoToHomePresenter goToHomePresenter,
                           @Nonnull ProjectMenuPresenter projectMenuPresenter,
                           @Nonnull SharingButtonPresenter sharingButtonPresenter,
                           @Nonnull LoggedInUserPresenter loggedInUserPresenter,
                           @Nonnull HelpPresenter helpPresenter) {
        this.view = view;
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
                      @Nonnull EventBus eventBus) {
        container.setWidget(view);
        goToHomePresenter.start(view.getGoToHomeWidgetContainer(), eventBus);
        projectMenuPresenter.start(view.getProjectMenuContainer(), eventBus);
        loggedInUserPresenter.start(view.getLoggedInUserWidgetContainer(), eventBus);
        helpPresenter.start(view.getHelpWidgetContainer(), eventBus);
        sharingButtonPresenter.start(view.getSharingSettingsContainer(), eventBus);
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
