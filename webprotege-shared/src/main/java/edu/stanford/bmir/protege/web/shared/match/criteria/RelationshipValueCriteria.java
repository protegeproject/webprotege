package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@JsonSubTypes(
        {
                @JsonSubTypes.Type(AnyRelationshipValueCriteria.class),
                @JsonSubTypes.Type(EntityValueRelationshipCriteria.class)
        }
)
public interface RelationshipValueCriteria extends Criteria {

        <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor);
}
