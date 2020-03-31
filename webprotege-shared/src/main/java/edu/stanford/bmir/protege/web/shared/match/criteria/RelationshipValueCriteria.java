package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(AnyRelationshipValueCriteria.class),
                @JsonSubTypes.Type(RelationshipValueEqualsEntityCriteria.class),
                @JsonSubTypes.Type(RelationshipValueEqualsLiteralCriteria.class),
                @JsonSubTypes.Type(RelationshipValueMatchesCriteria.class),
                @JsonSubTypes.Type(CompositeRelationshipValueCriteria.class)
        }
)
public interface RelationshipValueCriteria extends Criteria {

        <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor);
}
