package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchAction implements ProjectAction<PerformEntitySearchResult>, HasProjectId {

    private ProjectId projectId;

    private String searchString;

    private Set<EntityType<?>> entityTypes;

    private LangTagFilter langTagFilter;

    private ImmutableList<EntitySearchFilter> searchFilters;

    private PageRequest pageRequest;

    private PerformEntitySearchAction() {
    }

    public PerformEntitySearchAction(@Nonnull ProjectId projectId,
                                     @Nonnull String searchString,
                                     @Nonnull Set<EntityType<?>> entityTypes,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                     @Nonnull PageRequest pageRequest) {
        this.projectId = checkNotNull(projectId);
        this.searchString = checkNotNull(searchString);
        this.entityTypes = checkNotNull(entityTypes);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.searchFilters = checkNotNull(searchFilters);
        this.pageRequest = checkNotNull(pageRequest);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Set<EntityType<?>> getEntityTypes() {
        return new HashSet<>(entityTypes);
    }

    @Nonnull
    public String getSearchString() {
        return searchString;
    }

    @Nonnull
    public LangTagFilter getLangTagFilter() {
        return langTagFilter;
    }

    @Nonnull
    public ImmutableList<EntitySearchFilter> getSearchFilters() {
        return searchFilters;
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
    }
}
