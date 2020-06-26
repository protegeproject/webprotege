package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class EntityRelationshipCriteriaPresenter implements CriteriaPresenter<EntityRelationshipCriteria> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityRelationshipCriteriaView view;

    @Nonnull
    private final BlankCriteriaView blankCriteriaView;

    @Nonnull
    private final RelationshipValueCriteriaPresenter relationshipValueCriteriaPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public EntityRelationshipCriteriaPresenter(@Nonnull ProjectId projectId,
                                               @Nonnull EntityRelationshipCriteriaView view,
                                               @Nonnull BlankCriteriaView blankCriteriaView,
                                               @Nonnull RelationshipValueThatIsEqualToCriteriaPresenter valueThatIsEqualToCriteriaPresenter,
                                               @Nonnull RelationshipValueCriteriaListPresenter relationshipValueCriteriaListPresenter,
                                               @Nonnull RelationshipValueCriteriaPresenter relationshipValueCriteriaPresenter,
                                               @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.blankCriteriaView = checkNotNull(blankCriteriaView);
        this.relationshipValueCriteriaPresenter = checkNotNull(relationshipValueCriteriaPresenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.getValueCriteriaContainer().setWidget(blankCriteriaView);
        view.setPropertyChangeHandler(this::handlePropertyChanged);
        relationshipValueCriteriaPresenter.start(view.getValueCriteriaContainer());
    }

    @Override
    public void stop() {
        relationshipValueCriteriaPresenter.stop();
    }

    private void handlePropertyChanged() {

    }


    @Override
    public Optional<? extends EntityRelationshipCriteria> getCriteria() {
        Optional<OWLPropertyData> propertyData = view.getProperty();
        RelationshipPropertyCriteria propertyCriteria;
        if(propertyData.isPresent()) {
            propertyCriteria = RelationshipPropertyEqualsCriteria.get(propertyData.get().getEntity());
        }
        else {
            propertyCriteria = AnyRelationshipPropertyCriteria.get();
        }
        Optional<RelationshipValueCriteria> valueCriteria = relationshipValueCriteriaPresenter.getCriteria().map(c -> c);
        EntityRelationshipCriteria criteria = EntityRelationshipCriteria.get(RelationshipPresence.AT_LEAST_ONE,
                                                                                               propertyCriteria,
                                                                                               valueCriteria.orElse(AnyRelationshipValueCriteria.get()));
        return Optional.of(criteria);
    }

    @Override
    public void setCriteria(@Nonnull EntityRelationshipCriteria criteria) {
        RelationshipPropertyCriteria relationshipPropertyCriteria = criteria.getRelationshipPropertyCriteria();
        relationshipPropertyCriteria.accept(new RelationshipPropertyCriteriaVisitor<Void>() {
            @Override
            public Void visit(RelationshipPropertyEqualsCriteria criteria) {
                dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, criteria.getProperty()),
                                               result -> view.setProperty((OWLPropertyData) result.getEntityData()));
                return null;
            }

            @Override
            public Void visit(AnyRelationshipPropertyCriteria criteria) {
                view.clearProperty();
                return null;
            }
        });
        RelationshipValueCriteria relationshipValueCriteria = criteria.getRelationshipValueCriteria();
        relationshipValueCriteriaPresenter.setCriteria(relationshipValueCriteria);
    }
}
