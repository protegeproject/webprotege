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
 * 11 Jun 2018
 *
 * Represents criteria to match an annotation
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AnnotationComponents")
public abstract class AnnotationComponentsCriteria implements AnnotationCriteria {

    private static final String PROPERTY = "property";

    private static final String VALUE = "value";

    private static final String ANNOTATIONS = "annotations";

    /**
     * Creates criteria that match an annotation based on its property, its value,
     * its set of annotations.
     *  @param propertyCriteria      The criteria for matching the property.
     * @param valueCriteria         The criteria for matching the value.
     * @param annotationSetCriteria The criteria for matching annotations on the annotation.
     */
    @JsonCreator
    @Nonnull
    public static AnnotationComponentsCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                                   @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria,
                                                   @Nonnull @JsonProperty(ANNOTATIONS) AnnotationSetCriteria annotationSetCriteria) {
        return new AutoValue_AnnotationComponentsCriteria(propertyCriteria, valueCriteria, annotationSetCriteria);
    }

    /**
     * A convenience method to create criteria that match an annotation based on its property
     * and its value.  The annotation must be present.  Annotations on the annotation are ignored.
     *
     * @param propertyCriteria The criteria for matching the property.
     * @param valueCriteria    The criteria for matching the value.
     */
    @Nonnull
    public static AnnotationComponentsCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                                   @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria) {
        return get(propertyCriteria, valueCriteria, AnyAnnotationSetCriteria.get());
    }

    /**
     * A convenicence method to create criteria that will match any annotation.
     */
    @Nonnull
    public static AnnotationCriteria anyAnnotation() {
        return get(AnyAnnotationPropertyCriteria.get(),
                   AnyAnnotationValueCriteria.get());
    }

    @JsonProperty(PROPERTY)
    @Nonnull
    public abstract AnnotationPropertyCriteria getAnnotationPropertyCriteria();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract AnnotationValueCriteria getAnnotationValueCriteria();

    @JsonProperty(ANNOTATIONS)
    @Nonnull
    public abstract AnnotationSetCriteria getAnnotationSetCriteria();

    @Nonnull
    @Override
    public <R> R accept(@Nonnull AnnotationCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
