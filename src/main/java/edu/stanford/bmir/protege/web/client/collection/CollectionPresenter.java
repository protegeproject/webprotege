package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
@ProjectSingleton
public class CollectionPresenter implements Presenter {

    @Nonnull
    private final CollectionView view;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final FormPresenter formPresenter;

    @Nonnull
    private final CollectionItemListPresenter listPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<CollectionViewPlace> current = Optional.empty();

    @Nonnull
    private final Provider<AddCollectionItemPrompt> addItemPromptProvider;

    @Inject
    public CollectionPresenter(@Nonnull CollectionView view,
                               @Nonnull PlaceController placeController,
                               @Nonnull FormPresenter formPresenter,
                               @Nonnull CollectionItemListPresenter listPresenter,
                               @Nonnull DispatchServiceManager dispatchServiceManager,
                               @Nonnull Provider<AddCollectionItemPrompt> addItemPromptProvider) {
        GWT.log("[CollectionPresenter] Instantiated CollectionPresenter");
        this.view = checkNotNull(view);
        this.view.setAddItemHandler(this::handleAddCollectionItems);
        this.placeController = checkNotNull(placeController);
        this.formPresenter = checkNotNull(formPresenter);
        this.listPresenter = checkNotNull(listPresenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.addItemPromptProvider = checkNotNull(addItemPromptProvider);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        GWT.log("[CollectionPresenter] Starting presenter");
        formPresenter.start(view.getFormContainer());
        listPresenter.start(view.getListContainer(), eventBus);
        view.setClearItemDataHandler(this::handleClearElementData);
        container.setWidget(view);
        view.onResize();
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> displayCurrentPlace());
        displayCurrentPlace();
    }

    private void handleClearElementData() {
        current.ifPresent(subject -> {
            subject.getSelection().ifPresent(selection -> {
                dispatchServiceManager.execute(new SetFormDataAction(subject.getProjectId(),
                                                                     subject.getCollectionId(),
                                                                     selection,
                                                                     subject.getFormId(),
                                                                     FormData.empty()),
                                               result -> formPresenter.clearData());
            });

        });
    }

    private void handleAddCollectionItems() {
        current.ifPresent(subject -> {
            addItemPromptProvider.get().showPrompt(itemName -> {
                CollectionItem freshElementId = CollectionItem.get(itemName);
                // Create Empty Data for the fresh item
                dispatchServiceManager.execute(new SetFormDataAction(subject.getProjectId(),
                                                                     subject.getCollectionId(),
                                                                     freshElementId,
                                                                     subject.getFormId(),
                                                                     FormData.empty()),
                                               result -> {
                                                   placeController.goTo(new CollectionViewPlace(
                                                           subject.getProjectId(),
                                                           subject.getCollectionId(),
                                                           subject.getFormId(),
                                                           Optional.of(freshElementId)
                                                   ));
                                                   listPresenter.refresh();
                                                   GWT.log("[CollectionPresenter] Created element: " + freshElementId);
                                               });
            });
        });
    }

    private void displayCurrentPlace() {
        Place currentPlace = placeController.getWhere();
        GWT.log("[CollectionPresenter] Display current place: " + currentPlace);
        if (currentPlace instanceof CollectionViewPlace) {
            submitFormData();
            CollectionViewPlace collectionViewPlace = (CollectionViewPlace) currentPlace;
            displayForm(collectionViewPlace);
        }
    }

    private void submitFormData() {
        GWT.log("[CollectionPresenter] Submitting form data.  Current place is " + current);
        FormData formData = formPresenter.getFormData();
        current.ifPresent(subject -> {
            GWT.log("[CollectionPresenter] Saving data for " + subject.getSelection());
            dispatchServiceManager.execute(new SetFormDataAction(subject.getProjectId(),
                                                                 subject.getCollectionId(),
                                                                 subject.getSelection().get(),
                                                                 subject.getFormId(),
                                                                 formData),
                                           result -> {
                                           });
        });

    }

    private void displayForm(CollectionViewPlace place) {
        view.setCollectionTitle(place.getCollectionId().getId());
        current = Optional.empty();
        GWT.log("[CollectionPresenter] Clearing form");
        place.getSelection().ifPresent(sel -> {
            view.setItem(sel);
            current = Optional.of(place);
            GWT.log("[CollectionPresenter] Selection: " + sel);
            dispatchServiceManager.execute(new GetFormDescriptorAction(
                                                   place.getProjectId(),
                                                   place.getCollectionId(),
                                                   place.getFormId(),
                                                   sel),
                                           result -> formPresenter.displayForm(result.getFormDescriptor(),
                                                                               result.getFormData()));
        });
        if (!place.getSelection().isPresent()) {
        }
    }
}
