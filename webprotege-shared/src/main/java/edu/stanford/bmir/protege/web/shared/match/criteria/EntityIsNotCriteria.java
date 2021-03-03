package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityIsNot")
public abstract class EntityIsNotCriteria implements EntityMatchCriteria {


    public static final String ENTITY = "entity";

    @JsonCreator
    public static EntityIsNotCriteria get(@JsonProperty(ENTITY) @Nonnull OWLEntity newEntity) {
        return new AutoValue_EntityIsNotCriteria(newEntity);
    }

    @JsonProperty(ENTITY)
    @Nonnull
    public abstract OWLEntity getEntity();

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public CompositeRootCriteria asCompositeRootCriteria() {
        return CompositeRootCriteria.get(ImmutableList.of(this), MultiMatchType.ALL);
    }
}
