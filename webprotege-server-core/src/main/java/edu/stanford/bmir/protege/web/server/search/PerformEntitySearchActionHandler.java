package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.shared.shortform.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageFilter.EmptyLangTagTreatment.INCLUDE_EMPTY_LANG_TAGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchActionHandler extends AbstractProjectActionHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    @Nonnull
    private final EntitySearcherFactory entitySearcherFactory;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public PerformEntitySearchActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntitySearcherFactory entitySearcherFactory,
                                            @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.entitySearcherFactory = entitySearcherFactory;
        this.languageManager = languageManager;
    }

    @Nonnull
    @Override
    public Class<PerformEntitySearchAction> getActionClass() {
        return PerformEntitySearchAction.class;
    }

    @Nonnull
    @Override
    public PerformEntitySearchResult execute(@Nonnull PerformEntitySearchAction action,
                                             @Nonnull ExecutionContext executionContext) {
        var entityTypes = action.getEntityTypes();
        var searchString = action.getSearchString();
        var languages = ImmutableList.<DictionaryLanguage>builder();
        var langTagFilter = action.getLangTagFilter();
        var dictionaryLanguageFilter = DictionaryLanguageFilter.get(langTagFilter, INCLUDE_EMPTY_LANG_TAGS);
        languageManager.getLanguages().stream().filter(dictionaryLanguageFilter::isIncluded).forEach(languages::add);
        languages.add(OboIdDictionaryLanguage.get());
        languages.add(LocalNameDictionaryLanguage.get());
        languages.add(PrefixedNameDictionaryLanguage.get());

        var searchFilters = action.getSearchFilters();

        var entitySearcher = entitySearcherFactory.create(entityTypes,
                                                          searchString,
                                                          executionContext.getUserId(),
                                                          languages.build(),
                                                          searchFilters);
        PageRequest pageRequest = action.getPageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        return PerformEntitySearchResult.get(searchString, results);
    }
}

