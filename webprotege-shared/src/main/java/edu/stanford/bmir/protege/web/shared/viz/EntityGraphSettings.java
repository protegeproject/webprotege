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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.stream.Collectors.toList;

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

    @Nonnull
    public static EntityGraphSettings getDefault() {
        return get(ImmutableList.of(), 1.0);
    }

    @JsonCreator
    public static EntityGraphSettings get(@Nonnull @JsonProperty(FILTERS) ImmutableList<EntityGraphFilter> criteria,
                                          @JsonProperty(value = RANK_SPACING,
                                                        defaultValue = "1.0") double rankSpacing) {
        return new AutoValue_EntityGraphSettings(criteria, rankSpacing);
    }

    @JsonIgnore
    @Nonnull
    public ImmutableList<FilterName> getActiveFilterNames() {
        return getFilters().stream()
                           .filter(EntityGraphFilter::isActive)
                           .map(EntityGraphFilter::getName)
                           .collect(toImmutableList());
    }

    /**
     * Gets the criteria that are used for filtering edges in an
     * entity graph
     *
     * @return The criteria.
     */
    @Nonnull
    @JsonProperty(FILTERS)
    public abstract ImmutableList<EntityGraphFilter> getFilters();

    @JsonIgnore
    @Nonnull
    public EdgeCriteria getCombinedActiveFilterCriteria() {
        List<CompositeEdgeCriteria> inclusionCriteria = getFilters()
                .stream()
                .filter(EntityGraphFilter::isActive)
                .map(EntityGraphFilter::getInclusionCriteria)
                .collect(toList());

        List<EdgeCriteria> exclusionCriteria = getFilters()
                .stream()
                .filter(EntityGraphFilter::isActive)
                .map(EntityGraphFilter::getExclusionCriteria)
                .map(NegatedEdgeCriteria::get)
                .collect(toList());
        return CompositeEdgeCriteria.get(MultiMatchType.ALL,
                CompositeEdgeCriteria.get(inclusionCriteria, MultiMatchType.ANY),
                                         CompositeEdgeCriteria.get(exclusionCriteria, MultiMatchType.ALL));
    }

    @JsonIgnore
    @Nonnull
    public ImmutableSet<FilterName> getFilterNames() {
        return getFilters().stream()
                           .map(EntityGraphFilter::getName)
                           .collect(toImmutableSet());
    }

    @JsonProperty(RANK_SPACING)
    public abstract double getRankSpacing();
}
