package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.place.ProjectListPlace;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class GoToHomePresenter implements HasDispose, Presenter {

    private final GoToHomeView view;

    @Inject
    public GoToHomePresenter(GoToHomeView view, final PlaceController placeController) {
        this.view = view;
        view.addGoToHomeHandler(() -> placeController.goTo(new ProjectListPlace()));
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
    }

    public void dispose() {

    }
}
