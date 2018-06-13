package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes({
        @Type(AnyDatatypeCriteria.class)
})
public interface DatatypeCriteria extends Criteria {

    <R> R accept(DatatypeCriteriaVisitor<R> visitor);
}
