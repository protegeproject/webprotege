package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
@ProjectSingleton
public class FormsManagerPresenter implements Presenter, HasBusy {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormsManagerView formManagerView;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final Provider<FormIdPresenter> formIdPresenterProvider;

    @Nonnull
    private final Map<FormId, FormIdPresenter> formIdPresentersByFormId = new HashMap<>();

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final CopyFormsFromProjectModalPresenter copyFormsFromProjectModalPresenter;

    @Nonnull

    @Inject
    public FormsManagerPresenter(@Nonnull ProjectId projectId,
                                 @Nonnull FormsManagerView formManagerView,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull FormsMessages formsMessages,
                                 @Nonnull PlaceController placeController,
                                 @Nonnull Provider<FormIdPresenter> formIdPresenterProvider,
                                 @Nonnull Messages messages,
                                 @Nonnull CopyFormsFromProjectModalPresenter copyFormsFromProjectModalPresenter) {
        this.projectId = checkNotNull(projectId);
        this.formManagerView = checkNotNull(formManagerView);
        this.settingsPresenter = settingsPresenter;
        this.dispatch = dispatchServiceManager;
        this.formsMessages = formsMessages;
        this.placeController = placeController;
        this.formIdPresenterProvider = formIdPresenterProvider;
        this.messages = messages;
        this.copyFormsFromProjectModalPresenter = copyFormsFromProjectModalPresenter;
    }

    private void deleteForm(@Nonnull FormId formId) {
        dispatch.execute(new DeleteFormAction(projectId, formId),
                         this,
                         this::handleDeleteFormResult);
    }

    private void handleDeleteFormResult(DeleteFormResult result) {
        retrieveAndDisplayFormsList();
    }

    private void displayFormsList(GetProjectFormDescriptorsResult result) {
        formIdPresentersByFormId.clear();
        ImmutableList<FormDescriptor> formDescriptors = result.getFormDescriptors();
        List<FormIdPresenter> formIdPresenters =
                formDescriptors.stream()
                               .map(fd -> {
                                   FormIdPresenter formIdPresenter = formIdPresenterProvider.get();
                                   formIdPresenter.start(new SimplePanel());
                                   formIdPresenter.setFormDescriptor(fd);
                                   this.formIdPresentersByFormId.put(fd.getFormId(), formIdPresenter);
                                   return formIdPresenter;
                               })
                               .collect(Collectors.toList());
        formManagerView.setForms(formIdPresenters);

    }

    private FormsPlace getBackHere() {
        Place currentPlace = placeController.getWhere();
        Optional<Place> nextPlace;
        if(currentPlace instanceof FormsPlace) {
            nextPlace = ((FormsPlace) currentPlace).getNextPlace();
        }
        else {
            nextPlace = Optional.empty();
        }
        return FormsPlace.get(projectId, nextPlace);
    }

    private EditFormPlace getFormsPlaceWithBackHere(@Nonnull FormId formId) {
        FormsPlace backHere = getBackHere();
        return EditFormPlace.get(projectId, formId, backHere);
    }

    private void goToFreshFormPlace(@Nonnull FormId formId) {
        placeController.goTo(EditFormPlace.get(projectId, formId, getFormsPlaceWithBackHere(formId)));
    }

    private void handleAddForm() {
        dispatch.execute(new GetFreshFormIdAction(projectId),
                         this,
                         result -> goToFreshFormPlace(result.getFormId()));
    }

    private void handleApply() {
        Place currentPlace = placeController.getWhere();
        if(currentPlace instanceof FormsPlace) {
            ((FormsPlace) currentPlace).getNextPlace()
                                       .ifPresent(placeController::goTo);

        }

    }

    private void handleCopyFormsFromProject() {
        copyFormsFromProjectModalPresenter.show();
    }

    private void handleDeleteForm(@Nonnull FormId formId) {
        FormIdPresenter formIdPresenter = formIdPresentersByFormId.get(formId);
        if(formIdPresenter == null) {
            return;
        }
        String displayName = formIdPresenter.getDisplayName();
        formManagerView.displayDeleteFormConfirmationMessage(displayName,
                                                             formId,
                                                             this::deleteForm);
    }

    private void handleEditForm(@Nonnull FormId formId) {
        EditFormPlace newPlace = getFormsPlaceWithBackHere(formId);
        placeController.goTo(newPlace);
    }

    private void retrieveAndDisplayFormsList() {
        dispatch.execute(new GetProjectFormDescriptorsAction(projectId),
                         this,
                         this::displayFormsList);

    }

    @Override
    public void setBusy(boolean busy) {
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.setSettingsTitle(formsMessages.forms_Title());
        settingsPresenter.setApplySettingsHandler(this::handleApply);
        settingsPresenter.start(container);
        settingsPresenter.setCancelButtonVisible(false);
        settingsPresenter.setApplyButtonText(messages.dialog_ok());
        AcceptsOneWidget section = settingsPresenter.addSection(formsMessages.projectForms_Title());
        section.setWidget(formManagerView);
        formManagerView.clear();
        formManagerView.setAddFormHandler(this::handleAddForm);
        formManagerView.setDeleteFormHandler(this::handleDeleteForm);
        formManagerView.setEditFormHandler(this::handleEditForm);
        formManagerView.setCopyFormsFromProjectHandler(this::handleCopyFormsFromProject);
        copyFormsFromProjectModalPresenter.setFormsCopiedHandler(this::retrieveAndDisplayFormsList);
        retrieveAndDisplayFormsList();
    }
}
