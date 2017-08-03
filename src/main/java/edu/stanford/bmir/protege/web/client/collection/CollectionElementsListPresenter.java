package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2017
 */
public class CollectionElementsListPresenter implements Presenter {

    @Nonnull
    private final CollectionElementsListView view;

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public CollectionElementsListPresenter(@Nonnull CollectionElementsListView view, @Nonnull PlaceController placeController) {
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        view.setElements(Arrays.asList(
                CollectionElementId.get("Alanine"),
                CollectionElementId.get("Glutamine"),
                CollectionElementId.get("Histadine")
        ));
        view.setSelectionChangedHandler(() -> {
            Place place = placeController.getWhere();
            if(place instanceof CollectionViewPlace) {
                CollectionViewPlace viewPlace = (CollectionViewPlace) place;
                CollectionViewPlace nextPlace = new CollectionViewPlace(
                        viewPlace.getProjectId(),
                        viewPlace.getCollectionId(),
                        viewPlace.getFormId(),
                        view.getSelection()
                        );
                placeController.goTo(nextPlace);
            }
        });
        Place place = placeController.getWhere();
        if(place instanceof CollectionViewPlace) {
            ((CollectionViewPlace) place).getSelection().ifPresent(view::setSelection);
        }
    }
}
