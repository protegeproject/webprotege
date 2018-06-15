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
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityAnnotation")
public abstract class EntityAnnotationCriteria implements EntityMatchCriteria {

    private static final String ANNOTATION = "annotation";

    @JsonProperty(ANNOTATION)
    @Nonnull
    public abstract AnnotationComponentCriteria getAnnotationCriteria();

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonCreator
    @Nonnull
    public static EntityAnnotationCriteria get(@Nonnull @JsonProperty("annotation") AnnotationComponentCriteria criteria) {
        return new AutoValue_EntityAnnotationCriteria(criteria);
    }

    public static EntityAnnotationCriteria get(@Nonnull AnnotationPropertyCriteria propertyCriteria,
                                               @Nonnull AnnotationValueCriteria valueCriteria) {
        return get(AnnotationComponentCriteria.get(propertyCriteria, valueCriteria));
    }
}
