package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.AbstractCreateEntitiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.AbstractCreateEntityResult;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 5 Dec 2017
 */
public class CreateEntityPresenter {

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
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull CreateEntityDialogView view,
                                 @Nonnull ModalManager modalManager,
                                 @Nonnull ActiveProjectManager activeProjectManager,
                                 @Nonnull DisplayNameSettingsManager displayNameSettingsManager,
                                 @Nonnull Messages messages) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
        this.view = view;
        this.modalManager = modalManager;
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
        this.messages = checkNotNull(messages);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull EntitiesCreatedHandler entitiesCreatedHandler,
                               @Nonnull ActionFactory actionFactory) {
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
            handleCreateEntities(view.getText(), actionFactory, entitiesCreatedHandler);
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
                currentLangTag.ifPresent(l -> view.setLangTag(l));
                if (!currentLangTag.isPresent()) {
                    String defaultLangTag = d.getDefaultDictionaryLanguage().getLang();
                    currentLangTag = Optional.of(defaultLangTag);
                    view.setLangTag(defaultLangTag);
                    handleLangTagChanged();
                }
            });
        });
    }

    private <E extends OWLEntity> void handleCreateEntities(@Nonnull String enteredText,
                                                            @Nonnull ActionFactory<E> actionFactory,
                                                            @Nonnull EntitiesCreatedHandler entitiesCreatedHandler) {

        GWT.log("[CreateEntityPresenter] handleCreateEntities.  Lang: " + view.getLangTag());
        currentLangTag = Optional.of(view.getLangTag());
        AbstractCreateEntitiesAction<? extends AbstractCreateEntityResult<E>, E> action = actionFactory.createAction(projectId,
                                                                                                                     enteredText,
                                                                                                                     view.getLangTag());
        dispatchServiceManager.execute(action,
                                       result -> entitiesCreatedHandler.handleEntitiesCreated(result.getEntities()));

    }

    public interface ActionFactory<E extends OWLEntity> {

        AbstractCreateEntitiesAction<?, E> createAction(
                @Nonnull ProjectId projectId,
                @Nonnull String createFromText, String langTag);
    }

    public interface EntitiesCreatedHandler {

        void handleEntitiesCreated(ImmutableCollection<EntityNode> entities);
    }
}
