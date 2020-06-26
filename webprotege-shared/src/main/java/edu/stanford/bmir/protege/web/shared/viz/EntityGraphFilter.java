package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.DeserializationUtil.nonNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityGraphFilter {

    private static final String NAME = "name";

    private static final String DESCRIPTION = "description";

    private static final String INCLUSION_CRITERIA = "inclusionCriteria";

    private static final String EXCLUSION_CRITERIA = "exclusionCriteria";

    private static final String ACTIVE = "active";


    @JsonCreator
    public static EntityGraphFilter get(@Nonnull @JsonProperty(NAME) FilterName name,
                                        @Nullable @JsonProperty(DESCRIPTION) String description,
                                        @Nonnull @JsonProperty(INCLUSION_CRITERIA) CompositeEdgeCriteria inclusionCriteria,
                                        @Nonnull @JsonProperty(EXCLUSION_CRITERIA) CompositeEdgeCriteria exclusionCriteria,
                                        @JsonProperty(ACTIVE) boolean active) {
        return new AutoValue_EntityGraphFilter(name, nonNull(description), inclusionCriteria, exclusionCriteria, active);
    }

    /**
     * Gets the filter name.  May be an empty string.
     */
    @Nonnull
    @JsonProperty(NAME)
    public abstract FilterName getName();

    @Nonnull
    @JsonProperty(DESCRIPTION)
    public abstract String getDescription();

    @Nonnull
    @JsonProperty(INCLUSION_CRITERIA)
    public abstract CompositeEdgeCriteria getInclusionCriteria();

    @Nonnull
    @JsonProperty(EXCLUSION_CRITERIA)
    public abstract CompositeEdgeCriteria getExclusionCriteria();

    @Nonnull
    public EdgeCriteria getCombinedCriteria() {
        return CompositeEdgeCriteria.get(MultiMatchType.ALL,
                                         getInclusionCriteria(),
                                         NegatedEdgeCriteria.get(getExclusionCriteria()));
    }

    @JsonProperty(ACTIVE)
    public abstract boolean isActive();

    @JsonIgnore
    public EntityGraphFilter withActive(boolean active) {
        if(isActive() == active) {
            return this;
        }
        return get(getName(),
                   getDescription(),
                   getInclusionCriteria(),
                   getExclusionCriteria(),
                   active);
    }
}
