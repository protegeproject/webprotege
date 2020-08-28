package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

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
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Nonnull
    private final EntityFormSelectorPresenter entityFormSelectorPresenter;

    private Optional<Place> nextPlace = Optional.empty();

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final MessageBox messageBox;

    @Inject
    public FormEditorPresenter(ProjectId projectId,
                               @Nonnull DispatchServiceManager dispatch,
                               @Nonnull SettingsPresenter settingsPresenter,
                               @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                               @Nonnull EntityFormSelectorPresenter entityFormSelectorPresenter,
                               @Nonnull PlaceController placeController,
                               @Nonnull MessageBox messageBox) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.formDescriptorPresenter = checkNotNull(formDescriptorPresenter);
        this.entityFormSelectorPresenter = entityFormSelectorPresenter;
        this.placeController = checkNotNull(placeController);
        this.messageBox = messageBox;
    }

    public void setFormId(@Nonnull FormId formId) {
        dispatch.execute(new GetEntityFormDescriptorAction(projectId, formId),
                         settingsPresenter,
                         result -> {
                             formDescriptorPresenter.clear();
                             formDescriptorPresenter.setFormId(formId);
                             Optional<FormDescriptor> formDescriptor = result.getFormDescriptor();
                             formDescriptor.ifPresent(this::setDescriptor);
                             entityFormSelectorPresenter.clear();
                             Optional<CompositeRootCriteria> formSelectorCriteria = result.getFormSelectorCriteria();
                             formSelectorCriteria
                                   .ifPresent(this::setSelector);
                         });

    }

    private void setSelector(CompositeRootCriteria selectorCriteria) {
        entityFormSelectorPresenter.setSelectorCriteria(selectorCriteria);
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
    }

    private void setDescriptor(@Nonnull FormDescriptor formDescriptors) {
        formDescriptorPresenter.setFormDescriptor(formDescriptors);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);

        AcceptsOneWidget descriptorViewContainer = settingsPresenter.addSection("Form");
        formDescriptorPresenter.start(descriptorViewContainer, eventBus);

        AcceptsOneWidget selectorContainer = settingsPresenter.addSection("Selector Criteria");

        entityFormSelectorPresenter.setSelectorCriteria(CompositeRootCriteria.get(
                ImmutableList.of(EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS))),
                MultiMatchType.ALL
        ));
        entityFormSelectorPresenter.start(selectorContainer);

        settingsPresenter.setApplySettingsHandler(this::handleApply);
        settingsPresenter.setCancelSettingsHandler(this::handleCancel);
    }

    private void handleApply() {
        LanguageMap languageMap = formDescriptorPresenter.getFormLabel();
        if(languageMap.asMap().isEmpty()) {
            messageBox.showAlert("Please provide a label for this form");
            return;
        }
        dispatch.execute(new SetEntityFormDescriptorAction(projectId,
                                                           formDescriptorPresenter.getFormDescriptor(),
                                                           entityFormSelectorPresenter.getSelectorCriteria().orElse(null)),
                         settingsPresenter,
                         result -> nextPlace.ifPresent(placeController::goTo));

    }

    private void handleCancel() {
        nextPlace.ifPresent(placeController::goTo);
    }
}
