package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("IriHasAnnotation")
public abstract class IriHasAnnotationCriteria implements IriCriteria {

    private static final String ANNOTATION_CRITERIA = "annotationCriteria";

    @JsonProperty(ANNOTATION_CRITERIA)
    @Nonnull
    public abstract AnnotationCriteria getIriAnnotationCriteria();

    @JsonCreator
    @Nonnull
    public static IriHasAnnotationCriteria get(@Nonnull @JsonProperty(ANNOTATION_CRITERIA) AnnotationCriteria annotationCriteria) {
        return new AutoValue_IriHasAnnotationCriteria(annotationCriteria);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
