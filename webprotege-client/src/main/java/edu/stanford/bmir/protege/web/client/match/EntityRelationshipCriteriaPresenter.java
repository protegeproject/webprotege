package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

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
    private final RootCriteriaPresenter rootCriteriaPresenter;

    @Nonnull
    private final RelationshipValueThatIsEqualToCriteriaPresenter valueThatIsEqualToCriteriaPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public EntityRelationshipCriteriaPresenter(@Nonnull ProjectId projectId,
                                               @Nonnull EntityRelationshipCriteriaView view,
                                               @Nonnull BlankCriteriaView blankCriteriaView,
                                               @Nonnull RootCriteriaPresenter rootCriteriaPresenter,
                                               @Nonnull RelationshipValueThatIsEqualToCriteriaPresenter valueThatIsEqualToCriteriaPresenter,
                                               @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.blankCriteriaView = checkNotNull(blankCriteriaView);
        this.rootCriteriaPresenter = checkNotNull(rootCriteriaPresenter);
        this.valueThatIsEqualToCriteriaPresenter = valueThatIsEqualToCriteriaPresenter;
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setValueMatchType(RelationshipValueMatchType.ANY_VALUE);
        view.getValueEntityCriteriaContainer().setWidget(blankCriteriaView);
        view.setPropertyChangeHandler(this::handlePropertyChanged);
        view.setValueMatchTypeChangedHandler(this::handleMatchTypeChanged);
    }

    @Override
    public void stop() {
        rootCriteriaPresenter.stop();
    }

    private void handlePropertyChanged() {

    }

    private void handleMatchTypeChanged() {
        if(view.getValueMatchType() == RelationshipValueMatchType.ANY_VALUE) {
            view.getValueEntityCriteriaContainer().setWidget(blankCriteriaView);
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.SPECIFIC_VALUE) {
            valueThatIsEqualToCriteriaPresenter.start(view.getValueEntityCriteriaContainer());
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.VALUE_THAT_MATCHES) {
            rootCriteriaPresenter.start(view.getValueEntityCriteriaContainer());
        }
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
        RelationshipValueCriteria valueCriteria = null;
        if(view.getValueMatchType() == RelationshipValueMatchType.SPECIFIC_VALUE) {
            valueCriteria = valueThatIsEqualToCriteriaPresenter
                    .getCriteria()
                    .map(criteria -> (RelationshipValueCriteria) criteria)
                    .orElse(AnyRelationshipValueCriteria.get());
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.VALUE_THAT_MATCHES) {
            valueCriteria = rootCriteriaPresenter.getCriteria()
                    .<RelationshipValueCriteria>map(EntityValueRelationshipCriteria::get)
                    .orElse(AnyRelationshipValueCriteria.get());
        }
        else {
            valueCriteria = AnyRelationshipValueCriteria.get();
        }
        EntityRelationshipCriteria criteria = EntityRelationshipCriteria.get(RelationshipPresence.AT_LEAST_ONE,
                                                                                               propertyCriteria,
                                                                                               valueCriteria);
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
        relationshipValueCriteria.accept(new RelationshipValueCriteriaVisitor<Void>() {
            @Override
            public Void visit(AnyRelationshipValueCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.ANY_VALUE);
                view.getValueEntityCriteriaContainer().setWidget(blankCriteriaView);
                return null;
            }

            @Override
            public Void visit(EntityValueRelationshipCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.VALUE_THAT_MATCHES);
                rootCriteriaPresenter.start(view.getValueEntityCriteriaContainer());
                rootCriteriaPresenter.setCriteria(criteria.getEntityMatchCriteria());
                return null;
            }

            @Override
            public Void visit(RelationshipValueThatIsEqualToCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.SPECIFIC_VALUE);
                valueThatIsEqualToCriteriaPresenter.start(view.getValueEntityCriteriaContainer());
                valueThatIsEqualToCriteriaPresenter.setCriteria(criteria);
                return null;
            }
        });
    }
}
