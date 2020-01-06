package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetProjectFormDescriptorsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class FormEditorPresenter implements Presenter {

    private final ProjectId projectId;

    @Nonnull
    private final FormEditorView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormDescriptorPresenter formDescriptorPresenter;

    private HasBusy hasBusy = busy -> {};

    private Optional<Place> nextPlace = Optional.empty();

    @Nonnull
    private final PlaceController placeController;

    @Inject
    public FormEditorPresenter(ProjectId projectId,
                               @Nonnull FormEditorView view,
                               @Nonnull DispatchServiceManager dispatch,
                               @Nonnull SettingsPresenter settingsPresenter,
                               @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                               @Nonnull PlaceController placeController) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.formDescriptorPresenter = checkNotNull(formDescriptorPresenter);
        this.placeController = checkNotNull(placeController);
    }

    public void setFormId(@Nonnull FormId formId) {
        dispatch.execute(new GetProjectFormDescriptorsAction(projectId),
                         hasBusy,
                         result -> setDescriptor(formId, result.getFormDescriptors()));
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
    }

    private void setDescriptor(@Nonnull FormId formId,
                               @Nonnull ImmutableList<FormDescriptor> formDescriptors) {
        formDescriptors.stream()
                       .filter(formDescriptor -> formDescriptor.getFormId().equals(formId))
                       .findFirst()
                       .ifPresent(formDescriptorPresenter::setFormDescriptor);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);
        AcceptsOneWidget descriptorViewContainer = settingsPresenter.addSection("Form");
        formDescriptorPresenter.start(descriptorViewContainer, eventBus);
        hasBusy = busy -> settingsPresenter.setBusy(container, busy);

        settingsPresenter.setApplySettingsHandler(this::saveDescriptor);
        settingsPresenter.setCancelSettingsHandler(this::handleCancel);
    }

    private void saveDescriptor() {
        // TODO
        nextPlace.ifPresent(placeController::goTo);
    }

    private void handleCancel() {
        nextPlace.ifPresent(placeController::goTo);
    }
}
