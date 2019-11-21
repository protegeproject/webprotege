package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;
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
    private final FormsManagerView view;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final Map<FormId, FormDescriptor> formDescriptors = new LinkedHashMap<>();

    @Inject
    public FormsManagerPresenter(@Nonnull ProjectId projectId,
                                 @Nonnull FormsManagerView view,
                                 @Nonnull SettingsPresenter settingsPresenter,
                                 @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.settingsPresenter = settingsPresenter;
        this.formDescriptorPresenter = formDescriptorPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
//        settingsPresenter.setBusy(container, true);
        settingsPresenter.setSettingsTitle("Forms");
        AcceptsOneWidget formsManagerContainer = settingsPresenter.addSection("Project forms");
        formsManagerContainer.setWidget(view);
        AcceptsOneWidget descriptorContainer = view.getFormDescriptorContainer();
        formDescriptorPresenter.start(descriptorContainer, eventBus);
        retrieveAndDisplayFormDescriptors();
    }

    @Override
    public void setBusy(boolean busy) {

    }

    private void retrieveAndDisplayFormDescriptors() {
            dispatchServiceManager.execute(new GetProjectFormDescriptorsAction(projectId),
                                           this,
                                           this::loadFormDescriptors);

    }

    private void loadFormDescriptors(GetProjectFormDescriptorsResult result) {
            ImmutableList<FormDescriptor> formDescriptors = result.getFormDescriptors();
            this.formDescriptors.clear();
            formDescriptors.forEach(formDescriptor -> this.formDescriptors.put(formDescriptor.getFormId(),
                                                                               formDescriptor));
        List<FormId> formIds = formDescriptors.stream()
                                              .map(FormDescriptor::getFormId)
                                              .collect(Collectors.toList());
        view.setFormIds(formIds);
            this.formDescriptors.values()
                    .stream()
                    .findFirst()
                    .ifPresent(this::displayFormDescriptor);
    }

    private void displayFormDescriptor(FormDescriptor formDescriptor) {
        try {
            dispatchServiceManager.beginBatch();
            view.setCurrentFormId(formDescriptor.getFormId());
            formDescriptorPresenter.setFormDescriptor(formDescriptor);
        } finally {
            dispatchServiceManager.executeCurrentBatch();
        }
    }
}
