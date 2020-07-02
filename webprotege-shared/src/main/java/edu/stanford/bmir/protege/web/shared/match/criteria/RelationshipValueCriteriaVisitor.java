package edu.stanford.bmir.protege.web.shared.match.criteria;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public interface RelationshipValueCriteriaVisitor<R> {

    R visit(AnyRelationshipValueCriteria criteria);

    R visit(RelationshipValueMatchesCriteria criteria);

    R visit(RelationshipValueEqualsEntityCriteria criteria);

    R visit(RelationshipValueEqualsLiteralCriteria criteria);

    R visit(CompositeRelationshipValueCriteria compositeRelationshipValueCriteria);
}
