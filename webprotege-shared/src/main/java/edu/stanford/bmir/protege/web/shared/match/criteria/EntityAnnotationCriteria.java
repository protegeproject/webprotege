package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityAnnotation")
public abstract class EntityAnnotationCriteria implements EntityMatchCriteria {

    private static final String ANNOTATION = "annotation";

    private static final String PRESENCE = "presence";

    @JsonProperty(ANNOTATION)
    @Nonnull
    public abstract AnnotationCriteria getAnnotationCriteria();

    @JsonIgnore
    protected abstract int getAnnotationPresenceOrdinal();

    @JsonProperty(PRESENCE)
    @Nonnull
    public AnnotationPresence getAnnotationPresence() {
        return AnnotationPresence.values()[getAnnotationPresenceOrdinal()];
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonCreator
    @Nonnull
    public static EntityAnnotationCriteria get(@Nonnull @JsonProperty(ANNOTATION) AnnotationCriteria criteria,
                                               @Nonnull @JsonProperty(PRESENCE) AnnotationPresence presence) {
        return new AutoValue_EntityAnnotationCriteria(criteria, presence.ordinal());
    }

    @Nonnull
    public static EntityAnnotationCriteria get(@Nonnull @JsonProperty("annotation") AnnotationCriteria criteria) {
        return new AutoValue_EntityAnnotationCriteria(criteria, AnnotationPresence.AT_LEAST_ONE.ordinal());
    }

    public static EntityAnnotationCriteria get(@Nonnull AnnotationPropertyCriteria propertyCriteria,
                                               @Nonnull AnnotationValueCriteria valueCriteria) {
        return get(AnnotationComponentsCriteria.get(propertyCriteria, valueCriteria));
    }
}
