package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.AbstractCreateEntitiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.AbstractCreateEntityResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateEntitiesInHierarchyAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntitiesDialogController;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
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
    private CreateEntitiesDialogController dialogController;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    private Optional<String> currentLangTag = Optional.empty();

    @Inject
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull CreateEntitiesDialogController dialogController,
                                 @Nonnull ActiveProjectManager activeProjectManager,
                                 @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
        this.dialogController = checkNotNull(dialogController);
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull EntitiesCreatedHandler entitiesCreatedHandler,
                               @Nonnull ActionFactory actionFactory) {
        dialogController.clear();
        dialogController.setEntityType(entityType);
        dialogController.setCreateEntityHandler(createEntityInfo -> {
            handleCreateEntities(createEntityInfo, actionFactory, entitiesCreatedHandler);
        });
        dialogController.setResetLangTagHandler(this::resetLangTag);
        dialogController.setLangTagChangedHandler(this::handleLangTagChanged);
        displayCurrentLangTagOrProjectDefaultLangTag();
        WebProtegeDialog.showDialog(dialogController);
    }

    private void resetLangTag() {
        currentLangTag = Optional.empty();
        displayCurrentLangTagOrProjectDefaultLangTag();
    }

    private void handleLangTagChanged() {
        String langTag = dialogController.getLangTag();
        dialogController.setNoDisplayLanguageForLangTagVisible(false);
        if(displayNameSettingsManager.getLocalDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
            return;
        }
        activeProjectManager.getActiveProjectDetails(details -> {
            details.ifPresent(d -> {
                if(!d.getDefaultDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
                    dialogController.setNoDisplayLanguageForLangTagVisible(true);
                }
            });
        });
    }

    private void displayCurrentLangTagOrProjectDefaultLangTag() {
        activeProjectManager.getActiveProjectDetails(details -> {
            details.ifPresent(d -> {
                currentLangTag.ifPresent(l -> dialogController.setLangTag(l));
                if(!currentLangTag.isPresent()) {
                    String defaultLangTag = d.getDefaultDictionaryLanguage().getLang();
                    currentLangTag = Optional.of(defaultLangTag);
                    dialogController.setLangTag(defaultLangTag);
                    handleLangTagChanged();
                }
            });
        });
    }

    private <E extends OWLEntity> void handleCreateEntities(@Nonnull String enteredText,
                                      @Nonnull ActionFactory<E> actionFactory,
                                      @Nonnull EntitiesCreatedHandler entitiesCreatedHandler) {

        GWT.log("[CreateEntityPresenter] handleCreateEntities.  Lang: " + dialogController.getLangTag());
        currentLangTag = Optional.of(dialogController.getLangTag());
        AbstractCreateEntitiesAction<? extends AbstractCreateEntityResult<E>, E> action = actionFactory.createAction(projectId,
                                                                                                                  enteredText,
                                                                                                                  dialogController.getLangTag());
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
