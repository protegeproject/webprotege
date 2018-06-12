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
@JsonTypeName("EntityAnnotationValuesAreNotDisjoint")
public abstract class EntityAnnotationValuesAreNotDisjointCriteria implements EntityMatchCriteria {

    @JsonProperty("firstProperty")
    @Nonnull
    public abstract AnnotationPropertyCriteria getFirstProperty();

    @JsonProperty("secondProperty")
    @Nonnull
    public abstract AnnotationPropertyCriteria getSecondProperty();

    @JsonCreator
    @Nonnull
    public static EntityAnnotationValuesAreNotDisjointCriteria get(@Nonnull @JsonProperty("firstProperty") AnnotationPropertyCriteria firstProperty,
                                                                   @Nonnull @JsonProperty("secondProperty") AnnotationPropertyCriteria secondProperty) {
        return new AutoValue_EntityAnnotationValuesAreNotDisjointCriteria(firstProperty, secondProperty);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
