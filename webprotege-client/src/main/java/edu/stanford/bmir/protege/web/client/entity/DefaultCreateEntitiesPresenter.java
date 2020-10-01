package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-29
 */
public class DefaultCreateEntitiesPresenter {


    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateEntityDialogView view;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Nonnull
    private final Messages messages;

    private Optional<String> currentLangTag = Optional.empty();

    @Inject
    public DefaultCreateEntitiesPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull CreateEntityDialogView view,
                                          @Nonnull ModalManager modalManager,
                                          @Nonnull ActiveProjectManager activeProjectManager,
                                          @Nonnull DisplayNameSettingsManager displayNameSettingsManager,
                                          @Nonnull Messages messages) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.view = view;
        this.modalManager = modalManager;
        this.activeProjectManager = activeProjectManager;
        this.displayNameSettingsManager = displayNameSettingsManager;
        this.messages = messages;
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull Optional<? extends OWLEntity> parentEntity,
                               @Nonnull CreateEntityPresenter.EntitiesCreatedHandler entitiesCreatedHandler) {
        view.clear();
        view.setEntityType(entityType);
        view.setResetLangTagHandler(this::resetLangTag);
        view.setLangTagChangedHandler(this::handleLangTagChanged);
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.create() + " " + entityType.getPluralPrintName());
        modalPresenter.setView(view);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.CREATE);
        modalPresenter.setButtonHandler(DialogButton.CREATE, closer -> {
            handleCreateEntities(entityType, view.getText(), parentEntity, entitiesCreatedHandler);
            closer.closeModal();
        });
        modalManager.showModal(modalPresenter);
        displayCurrentLangTagOrProjectDefaultLangTag();

    }

    private void resetLangTag() {
        currentLangTag = Optional.empty();
        displayCurrentLangTagOrProjectDefaultLangTag();
    }

    private void handleLangTagChanged() {
        String langTag = view.getLangTag();
        view.setNoDisplayLanguageForLangTagVisible(false);
        if (displayNameSettingsManager.getLocalDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
            return;
        }
        activeProjectManager.getActiveProjectDetails(details -> {
            details.ifPresent(d -> {
                if (!d.getDefaultDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
                    view.setNoDisplayLanguageForLangTagVisible(true);
                }
            });
        });
    }

    private void displayCurrentLangTagOrProjectDefaultLangTag() {
        activeProjectManager.getActiveProjectDetails(details -> {
            details.ifPresent(d -> {
                currentLangTag.ifPresent(view::setLangTag);
                if (!currentLangTag.isPresent()) {
                    String defaultLangTag = d.getDefaultDictionaryLanguage().getLang();
                    currentLangTag = Optional.of(defaultLangTag);
                    view.setLangTag(defaultLangTag);
                    handleLangTagChanged();
                }
            });
        });
    }

    private <E extends OWLEntity> void handleCreateEntities(@Nonnull EntityType<?> entityType,
                                                            @Nonnull String enteredText,
                                                            @Nonnull Optional<? extends OWLEntity> parent,
                                                            @Nonnull CreateEntityPresenter.EntitiesCreatedHandler entitiesCreatedHandler) {

        GWT.log("[CreateEntityPresenter] handleCreateEntities.  Lang: " + view.getLangTag());
        currentLangTag = Optional.of(view.getLangTag());
        ProjectAction<? extends AbstractCreateEntityResult<?>> action = getAction(entityType,
                                                                                  parent,
                                                                                  enteredText);
        dispatchServiceManager.execute(action,
                                       result -> entitiesCreatedHandler.handleEntitiesCreated(result.getEntities()));

    }

    private ProjectAction<? extends AbstractCreateEntityResult<?>> getAction(EntityType<?> entityType,
                                                                             Optional<? extends OWLEntity> parent,
                                                                             String enteredText) {
        if(entityType.equals(EntityType.CLASS)) {
            ImmutableSet<OWLClass> parentClses = getParents(parent, DataFactory::getOWLThing);
            return new CreateClassesAction(projectId, enteredText, view.getLangTag(), parentClses);
        }
        else if(entityType.equals(EntityType.OBJECT_PROPERTY)) {
            ImmutableSet<OWLObjectProperty> parentProperties = getParents(parent, () -> DataFactory.get().getOWLTopObjectProperty());
            return new CreateObjectPropertiesAction(projectId, enteredText, view.getLangTag(), parentProperties);
        }
        else if(entityType.equals(EntityType.DATA_PROPERTY)) {
            ImmutableSet<OWLDataProperty> parentProperties = getParents(parent, () -> DataFactory.get().getOWLTopDataProperty());
            return new CreateDataPropertiesAction(projectId, enteredText, view.getLangTag(), parentProperties);
        }
        else if(entityType.equals(EntityType.ANNOTATION_PROPERTY)) {
            ImmutableSet<OWLAnnotationProperty> parentProperties = getParentsSet(parent, ImmutableSet::of);
            return new CreateAnnotationPropertiesAction(projectId, enteredText, view.getLangTag(), parentProperties);
        }
        else if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            ImmutableSet<OWLClass> parentClses = getParents(parent, DataFactory::getOWLThing);
            return new CreateNamedIndividualsAction(projectId, parentClses, enteredText, view.getLangTag());
        }
        else {
            throw new RuntimeException("Unsupported entity type: " + entityType);
        }
    }

    private static <E extends OWLEntity> ImmutableSet<E> getParents(Optional<? extends OWLEntity> parent,
                                                              Supplier<E> defaultSupplier) {
        return getParentsSet(parent, () -> ImmutableSet.of(defaultSupplier.get()));
    }

    private static <E extends OWLEntity> ImmutableSet<E> getParentsSet(Optional<? extends OWLEntity> parent,
                                                                    Supplier<ImmutableSet<E>> defaultSupplier) {
        return parent.map(p -> ImmutableSet.of((E) p)).orElseGet(defaultSupplier);
    }


    public interface EntitiesCreatedHandler {

        void handleEntitiesCreated(ImmutableCollection<EntityNode> entities);
    }
}
