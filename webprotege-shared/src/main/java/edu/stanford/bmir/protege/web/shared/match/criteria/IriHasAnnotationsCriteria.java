package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("IriHasAnnotations")
public abstract class IriHasAnnotationsCriteria implements IriCriteria {

    @Nonnull
    public abstract ImmutableList<AnnotationCriteria> getIriAnnotationsCriteria();

    @JsonCreator
    @Nonnull
    public static IriHasAnnotationsCriteria get(ImmutableList<AnnotationCriteria> annotationCriteria) {
        return new AutoValue_IriHasAnnotationsCriteria(annotationCriteria);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
