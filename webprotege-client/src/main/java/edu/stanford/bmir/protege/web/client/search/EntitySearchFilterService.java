package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class EntitySearchFilterService {

    @Nonnull
    private final EntitySearchSettingsService searchSettingsService;

    @Inject
    public EntitySearchFilterService(@Nonnull EntitySearchSettingsService searchSettingsService) {
        this.searchSettingsService = checkNotNull(searchSettingsService);
    }

    public void getSearchFilters(@Nonnull Consumer<ImmutableList<EntitySearchFilter>> searchFiltersConsumer) {
        searchSettingsService.getFilters(searchFiltersConsumer);
    }
}
