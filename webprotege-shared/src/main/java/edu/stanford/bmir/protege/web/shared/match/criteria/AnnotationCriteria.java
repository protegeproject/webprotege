package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
public abstract class AnnotationCriteria {

    private static final String PROPERTY = "property";

    private static final String VALUE = "value";

    private static final String ANNOTATIONS = "annotations";

    private static final String PRESENCE = "presence";

    @JsonProperty(PROPERTY)
    @Nonnull
    public abstract AnnotationPropertyCriteria getAnnotationPropertyCriteria();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract AnnotationValueCriteria getAnnotationValueCriteria();

    @JsonProperty(PRESENCE)
    @Nonnull
    public abstract AnnotationPresence getPresence();

    @JsonProperty(ANNOTATIONS)
    @Nonnull
    public abstract AnnotationSetCriteria getAnnotationSetCriteria();

    @JsonCreator
    @Nonnull
    public static AnnotationCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                         @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria,
                                         @Nonnull @JsonProperty(PRESENCE) AnnotationPresence presence,
                                         @Nonnull @JsonProperty(ANNOTATIONS) AnnotationSetCriteria annotationSetCriteria) {
        return new AutoValue_AnnotationCriteria(propertyCriteria, valueCriteria, presence, annotationSetCriteria);
    }

    @Nonnull
    public static AnnotationCriteria get(@Nonnull @JsonProperty(PROPERTY) AnnotationPropertyCriteria propertyCriteria,
                                         @Nonnull @JsonProperty(VALUE) AnnotationValueCriteria valueCriteria) {
        return get(propertyCriteria, valueCriteria, AnnotationPresence.PRESENT, AnyAnnotationSetCriteria.get());
    }

    @Nonnull
    public static AnnotationCriteria anyAnnotation() {
        return get(AnyAnnotationPropertyCriteria.get(),
                   AnyAnnotationValueCriteria.get());
    }
}
