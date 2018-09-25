package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldPresenter;
import edu.stanford.bmir.protege.web.shared.bulkop.MoveEntitiesToParentAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final MoveToParentView view;

    @Nonnull
    private final HierarchyFieldPresenter hierarchyFieldPresenter;

    @Nonnull
    private EntityType<?> entityType = EntityType.CLASS;

    @Inject
    public MoveToParentPresenter(@Nonnull ProjectId projectId, @Nonnull MoveToParentView view,
                                 @Nonnull HierarchyFieldPresenter presenter) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.hierarchyFieldPresenter = checkNotNull(presenter);
    }

    public void setEntityType(@Nonnull EntityType<?> entityType) {
        this.entityType = checkNotNull(entityType);
    }

    @Override
    public String getTitle() {
        return "Move to parent";
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "Move the selected " + entityType.getPluralPrintName().toLowerCase() + " from their current location to another location in the " + entityType.getPrintName().toLowerCase() + " hierarchy";
    }

    @Nonnull
    @Override
    public IsWidget getView() {
        hierarchyFieldPresenter.start(view.getHierarchyFieldContainer(),
                                      new WebProtegeEventBus(new SimpleEventBus()));
        return view;
    }

    @Override
    public boolean isDataWellFormed() {
        return hierarchyFieldPresenter.getEntity().isPresent();
    }

    @Nonnull
    @Override
    public Optional<MoveEntitiesToParentAction> createAction(@Nonnull ImmutableSet<OWLEntity> entities) {
        ImmutableSet<OWLClass> clses = entities.stream()
                .filter(OWLEntity::isOWLClass)
                .map(OWLEntity::asOWLClass)
                .collect(toImmutableSet());
        return hierarchyFieldPresenter.getEntity()
                .map(OWLEntityData::getEntity)
                .map(entity -> new MoveEntitiesToParentAction(projectId,
                                                              clses,
                                                              entity.asOWLClass()));
    }

    @Override
    public void displayErrorMessage() {

    }

    public void setHierarchyId(@Nonnull HierarchyId hierarchyId) {
        hierarchyFieldPresenter.setHierarchyId(hierarchyId);
    }
}
