package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.place.ProjectListPlace;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class GoToHomePresenter implements HasDispose, Presenter {

    @Nonnull
    private final GoToHomeView view;

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public GoToHomePresenter(@Nonnull GoToHomeView view,
                             @Nonnull PlaceController placeController) {
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        this.view.setGoToHomeHandler(this::goToProjectListPlace);
        container.setWidget(view);
    }

    /**
     * Causes the URL to change to the project list
     */
    private void goToProjectListPlace() {
        placeController.goTo(new ProjectListPlace());
    }

    public void dispose() {

    }
}
