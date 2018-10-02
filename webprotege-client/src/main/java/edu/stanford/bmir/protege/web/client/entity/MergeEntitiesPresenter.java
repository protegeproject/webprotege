package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.bulkop.BulkEditOperationPresenter;
import edu.stanford.bmir.protege.web.client.bulkop.BulkOpMessageFormatter;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldPresenter;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.MergeEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.entity.MergedEntityTreatment.DELETE_MERGED_ENTITY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MergeEntitiesView view;

    @Nonnull
    private final HierarchyFieldPresenter hierarchyFieldPresenter;

    @Nonnull
    private HandlerRegistration selectionHandlerRegistration = () -> {
    };

    @Inject
    public MergeEntitiesPresenter(@Nonnull MergeEntitiesView view,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull Messages messages,
                                  @Nonnull HierarchyFieldPresenter hierarchyFieldPresenter) {
        this.projectId = checkNotNull(projectId);
        this.messages = checkNotNull(messages);
        this.view = view;
        this.hierarchyFieldPresenter = checkNotNull(hierarchyFieldPresenter);
    }

    @Override
    public String getTitle() {
        return messages.merge_mergeEntities("Entities");
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "";
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        hierarchyFieldPresenter.setSyncWithCurrentSelectionVisible(false);
        hierarchyFieldPresenter.start(view.getHierarchyFieldContainer(), eventBus);
        container.setWidget(view);
    }

    @Override
    public boolean isDataWellFormed() {
        return hierarchyFieldPresenter.getEntity().isPresent();
    }

    @Nonnull
    @Override
    public Optional<? extends Action<?>> createAction(@Nonnull ImmutableSet<OWLEntity> entities, String commitMessage) {
        return hierarchyFieldPresenter
                .getEntity()
                .map(target -> createMergeAction(entities, target, commitMessage));
    }

    @Nonnull
    @Override
    public String getDefaultCommitMessage(@Nonnull ImmutableSet<? extends OWLEntityData> entities) {
        return "Merged "
                + BulkOpMessageFormatter.sortAndFormat(entities)
                + " into "
                + hierarchyFieldPresenter.getEntity()
                .map(HasBrowserText::getBrowserText)
                .orElse("other entity");
    }

    private MergeEntitiesAction createMergeAction(@Nonnull ImmutableSet<OWLEntity> entities,
                                                  @Nonnull OWLEntityData target,
                                                  @Nonnull String commitMessage) {
        return new MergeEntitiesAction(projectId, entities, target.getEntity(), DELETE_MERGED_ENTITY, commitMessage);
    }

    private void displayConfirmationMessage() {
        //                String typePluralName = sourceEntity.map(e -> e.getEntityType().getPluralPrintName()).orElse("Entities");
        //                String mergeEntitiesMsg = messages.merge_mergeEntities(typePluralName);
        //                MessageBox.showConfirmBox(QUESTION,
        //                                          mergeEntitiesMsg,
        //                                          messages.merge_confirmMergeMessage(),
        //                                          NO,
        //                                          this::end,
        //                                          DialogButton.get(mergeEntitiesMsg),
        //                                          this::performMerge,
        //                                                                            NO);
    }

    @Override
    public void displayErrorMessage() {

    }

    public void setHierarchyId(@Nonnull HierarchyId hierarchyId) {
        hierarchyFieldPresenter.setHierarchyId(hierarchyId);
    }
}
