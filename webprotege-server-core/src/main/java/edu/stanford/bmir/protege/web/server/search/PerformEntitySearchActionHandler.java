package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.OboIdDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.PrefixedNameDictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Set;

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
        languages.addAll(languageManager.getLanguages());
        languages.add(OboIdDictionaryLanguage.get());
        languages.add(LocalNameDictionaryLanguage.get());
        languages.add(PrefixedNameDictionaryLanguage.get());

        EntitySearcher entitySearcher = entitySearcherFactory.create(entityTypes,
                                                                     searchString,
                                                                     executionContext.getUserId(),
                                                                     languages.build());
        PageRequest pageRequest = action.getPageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        return PerformEntitySearchResult.get(searchString, results);
    }
}

