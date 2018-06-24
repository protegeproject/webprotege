package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("Annotations")
public abstract class CompositeAnnotationValueCriteria implements AnnotationValueCriteria {

    private static final String ANNOTATION_VALUES = "annotationValues";

    private static final String MATCH_TYPE = "matchType";

    @Nonnull
    @JsonProperty(ANNOTATION_VALUES)
    public abstract ImmutableList<? extends AnnotationValueCriteria> getAnnotationValueCriteria();

    @JsonProperty(MATCH_TYPE)
    @Nonnull
    public abstract MultiMatchType getMultiMatchType();

    @JsonCreator
    @Nonnull
    public static CompositeAnnotationValueCriteria get(@Nonnull @JsonProperty(ANNOTATION_VALUES) ImmutableList<? extends AnnotationValueCriteria> annotationValueCriteria,
                                                       @Nonnull @JsonProperty(MATCH_TYPE) MultiMatchType multiMatchType) {
        return new AutoValue_CompositeAnnotationValueCriteria(annotationValueCriteria, multiMatchType);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public CompositeAnnotationValueCriteria asCompositeAnnotationValueCriteria() {
        return this;
    }
}
