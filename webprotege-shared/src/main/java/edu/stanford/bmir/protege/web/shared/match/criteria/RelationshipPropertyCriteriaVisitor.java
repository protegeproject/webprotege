package edu.stanford.bmir.protege.web.shared.match.criteria;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public interface RelationshipPropertyCriteriaVisitor<R> {

    R visit(RelationshipPropertyEqualsCriteria criteria);

    R visit(AnyRelationshipPropertyCriteria criteria);
}
