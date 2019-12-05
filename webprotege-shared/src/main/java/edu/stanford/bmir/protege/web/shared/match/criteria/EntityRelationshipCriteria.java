package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@AutoValue
@JsonTypeName("EntityRelationship")
@GwtCompatible(serializable = true)
public abstract class EntityRelationshipCriteria implements EntityMatchCriteria {

    private static final String PRESENCE = "presence";

    private static final String PROPERTY = "property";

    private static final String VALUE = "value";

    @Nonnull
    @JsonCreator
    public static EntityRelationshipCriteria get(@Nonnull @JsonProperty(PRESENCE) RelationshipPresence presence,
                                                 @Nonnull @JsonProperty(PROPERTY) RelationshipPropertyCriteria propertyCriteria,
                                                 @Nonnull @JsonProperty(VALUE) RelationshipValueCriteria valueCriteria) {
        return new AutoValue_EntityRelationshipCriteria(presence, propertyCriteria, valueCriteria);
    }

    @Nonnull
    @JsonProperty(PRESENCE)
    public abstract RelationshipPresence getRelationshipPresence();

    @Nonnull
    @JsonProperty(PROPERTY)
    public abstract RelationshipPropertyCriteria getRelationshipPropertyCriteria();

    @Nonnull
    @JsonProperty(VALUE)
    public abstract RelationshipValueCriteria getRelationshipValueCriteria();

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
