package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static cern.clhep.Units.s;
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
    private final CollectionElementsListPresenter listPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<CollectionViewPlace> current = Optional.empty();

    @Inject
    public CollectionPresenter(@Nonnull CollectionView view,
                               @Nonnull PlaceController placeController,
                               @Nonnull FormPresenter formPresenter,
                               @Nonnull CollectionElementsListPresenter listPresenter,
                               @Nonnull DispatchServiceManager dispatchServiceManager) {
        GWT.log("[CollectionPresenter] Instantiated CollectionPresenter");
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
        this.formPresenter = checkNotNull(formPresenter);
        this.listPresenter = checkNotNull(listPresenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        GWT.log("[CollectionPresenter] Starting presenter");
        formPresenter.start(view.getFormContainer());
        listPresenter.start(view.getListContainer(), eventBus);
        container.setWidget(view);
        submitFormData();
        view.onResize();
        displayCurrentPlace();
    }

    private void displayCurrentPlace() {
        GWT.log("[CollectionPresenter] Display current place");
        Place currentPlace = placeController.getWhere();
        if (currentPlace instanceof CollectionViewPlace) {
            CollectionViewPlace collectionViewPlace = (CollectionViewPlace) currentPlace;
            displayForm(collectionViewPlace);
        }
    }

    private void submitFormData() {
        GWT.log("[CollectionPresenter] Submitting form data");
        FormData formData = formPresenter.getFormData();
        current.ifPresent(subject -> {
            GWT.log("[CollectionPresenter] Saving current data");
            dispatchServiceManager.execute(new SetFormDataAction(subject.getProjectId(),
                                                                 subject.getCollectionId(),
                                                                 subject.getSelection().get(),
                                                                 subject.getFormId(),
                                                                 formData),
                                           result -> {});
        });

    }

    private void displayForm(CollectionViewPlace place) {
        view.setCollectionTitle(place.getCollectionId().getId());
        current = Optional.empty();
        GWT.log("[CollectionPresenter] Clearing form");
        place.getSelection().ifPresent(sel -> {
            view.setElementId(sel);
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
        if(!place.getSelection().isPresent()) {
        }
    }
}
