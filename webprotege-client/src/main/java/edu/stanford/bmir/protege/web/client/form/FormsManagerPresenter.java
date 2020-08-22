package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
@ProjectSingleton
public class FormsManagerPresenter implements Presenter, HasBusy {

    @Nonnull
    private final FormsManagerView formManagerView;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormsManagerService formsManagerService;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final CopyFormsFromProjectModalPresenter copyFormsFromProjectModalPresenter;

    @Nonnull
    private final FormsManagerObjectListPresenter listPresenter;

    @Inject
    public FormsManagerPresenter(@Nonnull FormsManagerView formManagerView,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormsManagerService formsManagerService,
                                 @Nonnull FormsMessages formsMessages,
                                 @Nonnull PlaceController placeController,
                                 @Nonnull Messages messages,
                                 @Nonnull CopyFormsFromProjectModalPresenter copyFormsFromProjectModalPresenter,
                                 @Nonnull FormsManagerObjectListPresenter listPresenter) {
        this.formManagerView = checkNotNull(formManagerView);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.formsManagerService = checkNotNull(formsManagerService);
        this.formsMessages = checkNotNull(formsMessages);
        this.placeController = checkNotNull(placeController);
        this.messages = checkNotNull(messages);
        this.copyFormsFromProjectModalPresenter = checkNotNull(copyFormsFromProjectModalPresenter);
        this.listPresenter = checkNotNull(listPresenter);
    }

    private void displayFormsList(ImmutableList<FormDescriptor> formDescriptors,
                                  ImmutableList<EntityFormSelector> formSelectors) {
        listPresenter.setValues(formDescriptors);
    }

    private void handleApply() {
        ImmutableList<FormDescriptor> formDescriptors = listPresenter.getValues();
        formsManagerService.setForms(formDescriptors,
                                     this,
                                     this::goToNextPlace);
    }

    private void goToNextPlace() {
        Place currentPlace = placeController.getWhere();
        if (currentPlace instanceof FormsPlace) {
            ((FormsPlace) currentPlace).getNextPlace().ifPresent(placeController::goTo);
        }
    }

    private void handleCopyFormsFromProject() {
        copyFormsFromProjectModalPresenter.show();
    }


    private void retrieveAndDisplayFormsList() {
        formsManagerService.getForms(this,
                                     this::displayFormsList);
    }

    @Override
    public void setBusy(boolean busy) {
        settingsPresenter.setBusy(busy);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.setSettingsTitle(formsMessages.forms_Title());
        settingsPresenter.setApplySettingsHandler(this::handleApply);
        settingsPresenter.start(container);
        settingsPresenter.setBusy(true);
        settingsPresenter.setCancelButtonVisible(false);
        settingsPresenter.setApplyButtonText(messages.dialog_ok());
        AcceptsOneWidget section = settingsPresenter.addSection(formsMessages.projectForms_Title());
        section.setWidget(formManagerView);
        formManagerView.clear();

        listPresenter.start(formManagerView.getFormsListContainer(), eventBus);

        formManagerView.setCopyFormsFromProjectHandler(this::handleCopyFormsFromProject);
        copyFormsFromProjectModalPresenter.setFormsCopiedHandler(this::retrieveAndDisplayFormsList);
        retrieveAndDisplayFormsList();
    }
}
