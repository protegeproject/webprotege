package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnyRelationshipPropertyCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnyRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityRelationshipCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
@AutoValue
public abstract class RelationshipTranslationOptions {

    public enum RelationshipMinification {
        MINIMIZED_RELATIONSHIPS,
        NON_MINIMIZED_RELATIONSHIPS
    }

    @Nonnull
    public static RelationshipTranslationOptions get(@Nonnull RelationshipCriteria outgoingRelationshipCriteria,
                                                     @Nullable RelationshipCriteria incomingRelationshipCriteria,
                                                     @Nullable RelationshipMinification relationshipMinification) {
        return new AutoValue_RelationshipTranslationOptions(outgoingRelationshipCriteria,
                                                            incomingRelationshipCriteria,
                                                            relationshipMinification);
    }

    @Nonnull
    public static RelationshipCriteria allOutgoingRelationships() {
        return RelationshipCriteria.get(AnyRelationshipPropertyCriteria.get(),
                   AnyRelationshipValueCriteria.get());
    }

    @Nullable
    public static RelationshipCriteria noIncomingRelationships() {
        return null;
    }

    @JsonProperty("outgoingRelationshipCriteria")
    @Nullable
    protected abstract RelationshipCriteria getOutgoingRelationshipCriteriaInternal();

    @JsonIgnore
    public Optional<RelationshipCriteria> getOutgoingRelationshipCriteria() {
        return Optional.ofNullable(getOutgoingRelationshipCriteriaInternal());
    }

    @JsonProperty("incomingRelationshipCriteria")
    @Nullable
    protected abstract RelationshipCriteria getIncomingRelationshipCriteriaInternal();

    @JsonIgnore
    public Optional<RelationshipCriteria> getIncomingRelationshipCriteria() {
        return Optional.ofNullable(getIncomingRelationshipCriteriaInternal());
    }


    @Nonnull
    public abstract RelationshipMinification getRelationshipMinification();
}
