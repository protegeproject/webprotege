package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
public class RelationshipValueCriteriaPresenter implements CriteriaPresenter<RelationshipValueCriteria> {

    @Inject
    public RelationshipValueCriteriaPresenter(@Nonnull RelationshipValueCriteriaView view,
                                              @Nonnull BlankCriteriaView blankCriteriaView,
                                              @Nonnull RelationshipValueThatIsEqualToCriteriaPresenter valueThatIsEqualToCriteriaPresenter,
                                              @Nonnull EntityCriteriaPresenter entityCriteriaPresenter,
                                              @Nonnull RelationshipValueCriteriaListPresenter relationshipValueCriteriaListPresenter) {
        this.view = view;
        this.blankCriteriaView = blankCriteriaView;
        this.valueThatIsEqualToCriteriaPresenter = valueThatIsEqualToCriteriaPresenter;
        this.entityCriteriaPresenter = entityCriteriaPresenter;
        this.relationshipValueCriteriaListPresenter = relationshipValueCriteriaListPresenter;
    }

    @Nonnull
    private final RelationshipValueCriteriaView view;

    @Nonnull
    private final BlankCriteriaView blankCriteriaView;

    @Nonnull
    private final RelationshipValueThatIsEqualToCriteriaPresenter valueThatIsEqualToCriteriaPresenter;

    @Nonnull
    private final EntityCriteriaPresenter entityCriteriaPresenter;

    @Nonnull
    private final RelationshipValueCriteriaListPresenter relationshipValueCriteriaListPresenter;

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setValueMatchType(RelationshipValueMatchType.ANY_VALUE);
        view.getContainer().setWidget(blankCriteriaView);
        view.setValueMatchTypeChangedHandler(this::handleMatchTypeChanged);
    }

    @Override
    public void stop() {

    }

    private void handlePropertyChanged() {

    }

    private void handleMatchTypeChanged() {
        if(view.getValueMatchType() == RelationshipValueMatchType.ANY_VALUE) {
            view.getContainer().setWidget(blankCriteriaView);
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.SPECIFIC_VALUE) {
            valueThatIsEqualToCriteriaPresenter.start(view.getContainer());
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.VALUE_THAT_MATCHES) {
            entityCriteriaPresenter.start(view.getContainer());
        }
    }

    @Override
    public Optional<? extends RelationshipValueCriteria> getCriteria() {
        RelationshipValueCriteria valueCriteria = null;
        if(view.getValueMatchType() == RelationshipValueMatchType.SPECIFIC_VALUE) {
            valueCriteria = valueThatIsEqualToCriteriaPresenter
                    .getCriteria()
                    .map(criteria -> (RelationshipValueCriteria) criteria)
                    .orElse(AnyRelationshipValueCriteria.get());
        }
        else if(view.getValueMatchType() == RelationshipValueMatchType.VALUE_THAT_MATCHES) {
            valueCriteria = entityCriteriaPresenter.getCriteria()
                    .map(criteria -> (RelationshipValueCriteria) RelationshipValueMatchesCriteria.get(criteria))
                    .orElse(AnyRelationshipValueCriteria.get());
        }
        else {
            valueCriteria = AnyRelationshipValueCriteria.get();
        }
        return Optional.of(valueCriteria);
    }

    @Override
    public void setCriteria(@Nonnull RelationshipValueCriteria criteria) {
        criteria.accept(new RelationshipValueCriteriaVisitor<Void>() {
            @Override
            public Void visit(AnyRelationshipValueCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.ANY_VALUE);
                view.getContainer().setWidget(blankCriteriaView);
                return null;
            }

            @Override
            public Void visit(RelationshipValueMatchesCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.VALUE_THAT_MATCHES);
                entityCriteriaPresenter.start(view.getContainer());
                entityCriteriaPresenter.setCriteria(criteria.getMatchCriteria());
                return null;
            }

            @Override
            public Void visit(RelationshipValueEqualsLiteralCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.SPECIFIC_VALUE);
                valueThatIsEqualToCriteriaPresenter.start(view.getContainer());
                valueThatIsEqualToCriteriaPresenter.setCriteria(criteria);
                return null;
            }

            @Override
            public Void visit(RelationshipValueEqualsEntityCriteria criteria) {
                view.setValueMatchType(RelationshipValueMatchType.SPECIFIC_VALUE);
                valueThatIsEqualToCriteriaPresenter.start(view.getContainer());
                valueThatIsEqualToCriteriaPresenter.setCriteria(criteria);
                return null;
            }

            @Override
            public Void visit(CompositeRelationshipValueCriteria criteria) {
                relationshipValueCriteriaListPresenter.start(view.getContainer());
                relationshipValueCriteriaListPresenter.setCriteria(criteria);
                return null;
            }
        });
    }
}
