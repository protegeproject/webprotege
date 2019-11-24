package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final NoFormDescriptorSelectedView noFormDescriptorSelectedView;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final Map<FormId, FormDescriptor> formDescriptors = new LinkedHashMap<>();

    @Inject
    public FormsManagerPresenter(@Nonnull ProjectId projectId,
                                 @Nonnull FormsManagerView formManagerView,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull NoFormDescriptorSelectedView noFormDescriptorSelectedView,
                                 @Nonnull FormsMessages formsMessages) {
        this.projectId = checkNotNull(projectId);
        this.formManagerView = checkNotNull(formManagerView);
        this.settingsPresenter = settingsPresenter;
        this.formDescriptorPresenter = formDescriptorPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
        this.noFormDescriptorSelectedView = noFormDescriptorSelectedView;
        this.formsMessages = formsMessages;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle(formsMessages.forms_Title());
        settingsPresenter.setApplySettingsHandler(this::saveForms);
        AcceptsOneWidget section = settingsPresenter.addSection(formsMessages.projectForms_Title());
        section.setWidget(formManagerView);
        formManagerView.setAddFormHandler(this::handleAddForm);
        formManagerView.setFormSelectionChangedHandler(this::handleFormSelectionChanged);
        formManagerView.getFormDescriptorContainer().setWidget(noFormDescriptorSelectedView);
        retrieveAndDisplayFormDescriptors();
    }

    private void saveForms() {
        FormDescriptor currentFormDescriptor = formDescriptorPresenter.getFormDescriptor();
        this.formDescriptors.put(currentFormDescriptor.getFormId(), currentFormDescriptor);
        ImmutableList<FormDescriptor> formDescriptors = ImmutableList.copyOf(this.formDescriptors.values());
        GWT.log(formDescriptors.toString());
        dispatchServiceManager.execute(new SetProjectFormDescriptorsAction(projectId,
                                                                           formDescriptors),
                                       result -> {});
    }

    private void displayFormDescriptor(FormId formId) {
        FormDescriptor formDescriptor = formDescriptors.get(formId);
        if(formDescriptor == null) {
            formDescriptor = FormDescriptor.builder(formId)
                                           .build();
            formDescriptors.put(formId, formDescriptor);
        }
        displayFormDescriptor(formDescriptor);
    }

    private void displayFormDescriptor(FormDescriptor formDescriptor) {
        try {
            dispatchServiceManager.beginBatch();
            FormId formId = formDescriptor.getFormId();
            formManagerView.setCurrentFormId(formId);
            AcceptsOneWidget descriptorContainer = formManagerView.getFormDescriptorContainer();
            formDescriptorPresenter.start(descriptorContainer, new SimpleEventBus());
            formDescriptorPresenter.setFormDescriptor(formDescriptor);
        } finally {
            dispatchServiceManager.executeCurrentBatch();
        }
    }

    private void handleAddForm() {
        formManagerView.displayCreateFormIdPrompt(newFormId -> {
            saveCurrentFormDescriptor();
            FormId formId = FormId.get(newFormId);
            formManagerView.addFormId(formId);
            displayFormDescriptor(formId);
        });
    }

    private void handleFormSelectionChanged() {
        saveCurrentFormDescriptor();
        formManagerView.getCurrentFormId()
            .ifPresent(this::displayFormDescriptor);
    }

    private void loadFormDescriptors(GetProjectFormDescriptorsResult result) {
        ImmutableList<FormDescriptor> formDescriptors = result.getFormDescriptors();
        this.formDescriptors.clear();
        formDescriptors.forEach(formDescriptor -> this.formDescriptors.put(formDescriptor.getFormId(),
                                                                           formDescriptor));
        List<FormId> formIds = formDescriptors.stream()
                                              .map(FormDescriptor::getFormId)
                                              .collect(Collectors.toList());
        formManagerView.setFormIds(formIds);
        this.formDescriptors.values()
                            .stream()
                            .findFirst()
                            .ifPresent(this::displayFormDescriptor);
    }

    private void retrieveAndDisplayFormDescriptors() {
        dispatchServiceManager.execute(new GetProjectFormDescriptorsAction(projectId),
                                       this,
                                       this::loadFormDescriptors);

    }

    private void saveCurrentFormDescriptor() {
        FormDescriptor formDescriptor = formDescriptorPresenter.getFormDescriptor();
        formDescriptors.put(formDescriptor.getFormId(), formDescriptor);
    }

    @Override
    public void setBusy(boolean busy) {

    }
}
