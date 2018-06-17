package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;

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
public abstract class AnnotationComponentCriteria implements AnnotationCriteria {

    private static final String PROPERTY = "property";

    private static final String VALUE = "value";

    private static final String ANNOTATIONS = "annotations";

    private static final String PRESENCE = "presence";

    /**
     * Creates criteria that match an annotation based on its property, its value,
     * its set of annotations and its presence.
     *  @param propertyCriteria      The criteria for matching the property.
     * @param valueCriteria         The criteria for matching the value.
     * @param annotationSetCriteria The criteria for matching annotations on the annotation.
     * @param presence              The presence that specifies whether the annotation should be
*                              present or not.
     */
    @JsonCreator
    @Nonnull
    public static AnnotationCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                                  @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria,
                                                  @Nonnull @JsonProperty(ANNOTATIONS) AnnotationSetCriteria annotationSetCriteria,
                                                  @Nonnull @JsonProperty(PRESENCE) AnnotationPresence presence) {
        return new AutoValue_AnnotationComponentCriteria(propertyCriteria, valueCriteria, presence == AnnotationPresence.PRESENT, annotationSetCriteria);
    }

    /**
     * A convenience method to create criteria that match an annotation based on its property
     * and its value.  The annotation must be present.  Annotations on the annotation are ignored.
     *
     * @param propertyCriteria The criteria for matching the property.
     * @param valueCriteria    The criteria for matching the value.
     */
    @Nonnull
    public static AnnotationCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                                  @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria) {
        return get(propertyCriteria, valueCriteria, AnyAnnotationSetCriteria.get(), AnnotationPresence.PRESENT);
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

    public abstract boolean isPresent();

    @JsonProperty(PRESENCE)
    @Nonnull
    public AnnotationPresence getPresence() {
        return isPresent() ? AnnotationPresence.PRESENT : AnnotationPresence.ABSENT;
    }

    @JsonProperty(ANNOTATIONS)
    @Nonnull
    public abstract AnnotationSetCriteria getAnnotationSetCriteria();

    @Nonnull
    @Override
    public <R> R accept(@Nonnull AnnotationCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
