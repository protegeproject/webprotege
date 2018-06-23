package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes({
        @Type(EntityMatchCriteria.class)
})
public interface RootCriteria extends Criteria {

    <R> R accept(RootCriteriaVisitor<R> visitor);

    default CompositeRootCriteria asCompositeRootCriteria() {
        return CompositeRootCriteria.get(ImmutableList.of(this), MultiMatchType.ALL);
    }

}
