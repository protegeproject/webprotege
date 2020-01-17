package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public FormsManagerPresenter(@Nonnull ProjectId projectId,
                                 @Nonnull FormsManagerView formManagerView,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull NoFormDescriptorSelectedView noFormDescriptorSelectedView,
                                 @Nonnull FormsMessages formsMessages,
                                 @Nonnull PlaceController placeController) {
        this.projectId = checkNotNull(projectId);
        this.formManagerView = checkNotNull(formManagerView);
        this.settingsPresenter = settingsPresenter;
        this.formDescriptorPresenter = formDescriptorPresenter;
        this.dispatch = dispatchServiceManager;
        this.formsMessages = formsMessages;
        this.placeController = placeController;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle(formsMessages.forms_Title());
        settingsPresenter.setApplySettingsHandler(this::handleApply);
        AcceptsOneWidget section = settingsPresenter.addSection(formsMessages.projectForms_Title());
        section.setWidget(formManagerView);
        formManagerView.setAddFormHandler(this::handleAddForm);
        retrieveAndDisplayFormsList();
    }

    private void handleApply() {

    }


    private void handleAddForm() {
        dispatch.execute(new GetFreshFormIdAction(projectId),
                         result -> goToFreshFormPlace(result.getFormId()));
    }

    private void goToFreshFormPlace(@Nonnull FormId formId) {
        placeController.goTo(EditFormPlace.get(projectId, formId));
    }

    private void displayFormsList(GetProjectFormDescriptorsResult result) {

    }

    private void retrieveAndDisplayFormsList() {
        dispatch.execute(new GetProjectFormDescriptorsAction(projectId),
                         this,
                         this::displayFormsList);

    }

    @Override
    public void setBusy(boolean busy) {

    }
}
