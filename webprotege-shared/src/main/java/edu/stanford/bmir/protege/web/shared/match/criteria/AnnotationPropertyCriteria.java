package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes(value = {
        @Type(IriEqualsCriteria.class),
        @Type(AnyAnnotationPropertyCriteria.class)
})
public interface AnnotationPropertyCriteria extends Criteria {

    <R> R accept(@Nonnull AnnotationPropertyCriteriaVisitor<R> visitor);
}
