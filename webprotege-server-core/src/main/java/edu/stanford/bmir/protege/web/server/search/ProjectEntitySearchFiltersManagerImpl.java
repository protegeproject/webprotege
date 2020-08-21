package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class ProjectEntitySearchFiltersManagerImpl implements ProjectEntitySearchFiltersManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntitySearchFilterRepository repository;

    @Nonnull
    private final EntitySearchFilterIndexesManager indexesManager;

    @Inject
    public ProjectEntitySearchFiltersManagerImpl(@Nonnull ProjectId projectId,
                                                 @Nonnull EntitySearchFilterRepository repository,
                                                 @Nonnull EntitySearchFilterIndexesManager indexesManager) {
        this.projectId = checkNotNull(projectId);
        this.repository = checkNotNull(repository);
        this.indexesManager = checkNotNull(indexesManager);
    }

    @Nonnull
    @Override
    public ImmutableList<EntitySearchFilter> getSearchFilters() {
        return repository.getSearchFilters(projectId);
    }

    @Override
    public void setSearchFilters(@Nonnull ImmutableList<EntitySearchFilter> searchFilters) {
        repository.saveSearchFilters(searchFilters);
        indexesManager.updateEntitySearchFilterIndexes();
    }
}
