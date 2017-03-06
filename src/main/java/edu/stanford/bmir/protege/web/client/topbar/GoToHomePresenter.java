package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.place.ProjectListPlace;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class GoToHomePresenter implements HasDispose {

    private final GoToHomeView view;

    @Inject
    public GoToHomePresenter(GoToHomeView view, final PlaceController placeController) {
        this.view = view;
        view.addGoToHomeHandler(() -> placeController.goTo(new ProjectListPlace()));
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void dispose() {

    }
}
