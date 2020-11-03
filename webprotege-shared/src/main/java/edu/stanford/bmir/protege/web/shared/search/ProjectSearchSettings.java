package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-03
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectSearchSettings {

    public static final String SEARCH_FILTERS = "searchFilters";

    public static final String PROJECT_ID = "projectId";


    @JsonCreator
    @Nonnull
    public static ProjectSearchSettings get(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                            @JsonProperty(SEARCH_FILTERS)@Nonnull ImmutableList<EntitySearchFilter> searchFilters) {
        return new AutoValue_ProjectSearchSettings(projectId, searchFilters);
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(SEARCH_FILTERS)
    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getSearchFilters();

}
