package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityGraphSettings implements IsSerializable {

    public static final String FILTERS = "filters";

    public static final String RANK_SPACING = "rankSpacing";

    @JsonCreator
    public static EntityGraphSettings get(@Nonnull @JsonProperty(FILTERS) ImmutableList<EntityGraphFilter> criteria,
                                          @JsonProperty(value = RANK_SPACING, defaultValue = "1.0") double rankSpacing) {
        return new AutoValue_EntityGraphSettings(criteria, rankSpacing);
    }

    @Nonnull
    public static EntityGraphSettings getDefault() {
        return get(ImmutableList.of(), 1.0);
    }


    /**
     * Gets the criteria that are used for filtering edges in an
     * entity graph
     * @return The criteria.
     */
    @Nonnull
    @JsonProperty(FILTERS)
    public abstract ImmutableList<EntityGraphFilter> getFilters();

    @JsonProperty(RANK_SPACING)
    public abstract double getRankSpacing();

    @JsonIgnore
    @Nonnull
    public CompositeEdgeCriteria getCombinedActiveFilterCriteria() {
        ImmutableList<EdgeCriteria> combined = getFilters().stream()
                    .filter(EntityGraphFilter::isActive)
                    .map(EntityGraphFilter::getCombinedCriteria)
                    .collect(toImmutableList());
        return CompositeEdgeCriteria.get(combined, MultiMatchType.ALL);
    }
}
