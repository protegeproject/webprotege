package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.help.HelpPresenter;
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

    private final HelpPresenter helpPresenter;

    @Inject
    public TopBarPresenter(TopBarView view,
                           GoToHomePresenter goToHomePresenter,
                           LoggedInUserPresenter loggedInUserPresenter,
                           HelpPresenter helpPresenter) {
        this.view = view;
        this.goToHomePresenter = goToHomePresenter;
        this.loggedInUserPresenter = loggedInUserPresenter;
        this.helpPresenter = helpPresenter;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        goToHomePresenter.start(view.getGoToHomeWidgetContainer());
        loggedInUserPresenter.start(view.getLoggedInUserWidgetContainer());
        helpPresenter.start(view.getHelpWidgetContainer());
    }

    @Override
    public void dispose() {

    }
}
