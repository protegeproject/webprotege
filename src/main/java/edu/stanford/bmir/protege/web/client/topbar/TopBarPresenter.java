package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.help.HelpPresenter;
import edu.stanford.bmir.protege.web.client.project.ProjectMenuPresenter;
import edu.stanford.bmir.protege.web.client.sharing.SharingButtonPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class TopBarPresenter implements HasDispose {

    private final TopBarView view;

    private final GoToHomePresenter goToHomePresenter;

    private final LoggedInUserPresenter loggedInUserPresenter;

    private final ProjectMenuPresenter projectMenuPresenter;

    private final HelpPresenter helpPresenter;

    private final SharingButtonPresenter sharingButtonPresenter;

    @Inject
    public TopBarPresenter(TopBarView view,
                           GoToHomePresenter goToHomePresenter,
                           ProjectMenuPresenter projectMenuPresenter,
                           SharingButtonPresenter sharingButtonPresenter,
                           LoggedInUserPresenter loggedInUserPresenter,
                           HelpPresenter helpPresenter) {
        this.view = view;
        this.projectMenuPresenter = projectMenuPresenter;
        this.goToHomePresenter = goToHomePresenter;
        this.sharingButtonPresenter = sharingButtonPresenter;
        this.loggedInUserPresenter = loggedInUserPresenter;
        this.helpPresenter = helpPresenter;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        goToHomePresenter.start(view.getGoToHomeWidgetContainer());
        projectMenuPresenter.start(view.getProjectMenuContainer());
        loggedInUserPresenter.start(view.getLoggedInUserWidgetContainer());
        helpPresenter.start(view.getHelpWidgetContainer());
        sharingButtonPresenter.start(view.getSharingSettingsContainer());
    }

    @Override
    public void dispose() {

    }
}
