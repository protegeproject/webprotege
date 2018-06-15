package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityType")
public abstract class EntityTypeCriteria implements EntityMatchCriteria {

    @Nonnull
    public abstract EntityType<?> getEntityType();

    @JsonCreator
    @Nonnull
    public static EntityTypeCriteria get(@Nonnull EntityType entityType) {
        return new AutoValue_EntityTypeCriteria(entityType);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
