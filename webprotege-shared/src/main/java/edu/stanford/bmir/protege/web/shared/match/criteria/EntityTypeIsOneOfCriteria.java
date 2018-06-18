package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityTypeIsOneOf")
public abstract class EntityTypeIsOneOfCriteria implements EntityMatchCriteria {

    private static final String TYPES = "types";

    @JsonProperty(TYPES)
    @Nonnull
    public abstract ImmutableSet<EntityType<?>> getEntityTypes();

    @JsonCreator
    @Nonnull
    public static EntityTypeIsOneOfCriteria get(@Nonnull @JsonProperty(TYPES) ImmutableSet<EntityType<?>> entityTypes) {
        return new AutoValue_EntityTypeIsOneOfCriteria(entityTypes);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
