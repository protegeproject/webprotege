package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "match"
)
@JsonSubTypes({
        @Type(AnyEdgeCriteria.class),
        @Type(NoEdgeCriteria.class),
        @Type(AnyInstanceOfEdgeCriteria.class),
        @Type(AnySubClassOfEdgeCriteria.class),
        @Type(AnyRelationshipEdgeCriteria.class),
        @Type(RelationshipEdgePropertyEqualsCriteria.class),
        @Type(HeadNodeMatchesCriteria.class),
        @Type(TailNodeMatchesCriteria.class),
        @Type(NegatedEdgeCriteria.class),
        @Type(CompositeEdgeCriteria.class)
              })
public interface EdgeCriteria extends Criteria {

    <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor);

    @JsonIgnore
    @Nonnull
    EdgeCriteria simplify();
}
