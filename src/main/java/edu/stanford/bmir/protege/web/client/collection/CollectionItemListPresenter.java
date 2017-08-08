package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.collection.GetCollectionItemsAction;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2017
 *
 * Presents a list of collection elements
 */
public class CollectionItemListPresenter implements Presenter {

    @Nonnull
    private final CollectionItemListView view;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public CollectionItemListPresenter(@Nonnull CollectionItemListView view,
                                       @Nonnull PlaceController placeController,
                                       @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.view.setSelectionChangedHandler(() -> {
            GWT.log("[CollectionItemListPresenter] List notified me that the selection changed to " + view.getSelection());
            Place place = placeController.getWhere();
            if(place instanceof CollectionViewPlace) {
                CollectionViewPlace viewPlace = (CollectionViewPlace) place;
                CollectionViewPlace nextPlace = new CollectionViewPlace(
                        viewPlace.getProjectId(),
                        viewPlace.getCollectionId(),
                        viewPlace.getFormId(),
                        view.getSelection()
                );
                GWT.log("[CollectionItemListPresenter] Next place " + nextPlace);
                placeController.goTo(nextPlace);
            }
        });
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> displayCurrentPlace());
        refresh();
    }

    public void refresh() {
        Place place = placeController.getWhere();
        if(!(place instanceof CollectionViewPlace)) {
            return;
        }
        CollectionViewPlace collectionViewPlace = (CollectionViewPlace) place;
        dispatchServiceManager.execute(new GetCollectionItemsAction(collectionViewPlace.getProjectId(),
                                                                    collectionViewPlace.getCollectionId(),
                                                                    PageRequest.requestFirstPage()),
                                       busy -> {},
                                       result -> {
                                           Page<CollectionItem> page = result.getElementIdPage();
                                           view.setPageCount(page.getPageCount());
                                           view.setPageNumber(page.getPageNumber());
                                           view.setElements(page.getPageElements());
                                           collectionViewPlace.getSelection().ifPresent(view::setSelection);
                                       });
    }

    private void displayCurrentPlace() {
        Place place = placeController.getWhere();
        if(!(place instanceof CollectionViewPlace)) {
            return;
        }
        CollectionViewPlace collectionViewPlace = (CollectionViewPlace) place;
        collectionViewPlace.getSelection().ifPresent(view::setSelection);
    }

}
