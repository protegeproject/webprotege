package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 *
 * Criteria for the matching of annotation values
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "match"
)
@JsonSubTypes({
        @Type(LiteralCriteria.class),
        @Type(IriCriteria.class),
        @Type(AnyAnnotationValueCriteria.class),
        @Type(CompositeAnnotationValueCriteria.class),
        @Type(IriEqualsCriteria.class)
})
public interface AnnotationValueCriteria extends Criteria {

    <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor);

    default CompositeAnnotationValueCriteria asCompositeAnnotationValueCriteria() {
        ImmutableList<AnnotationValueCriteria> singleton = ImmutableList.of(this);
        return CompositeAnnotationValueCriteria.get(singleton, MultiMatchType.ALL);
    }
}
