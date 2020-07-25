package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.SearchResultMatch;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchActionHandler extends AbstractProjectActionHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    @Nonnull
    private final EntitySearcherFactory entitySearcherFactory;

    @Inject
    public PerformEntitySearchActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntitySearcherFactory entitySearcherFactory) {
        super(accessManager);
        this.entitySearcherFactory = entitySearcherFactory;
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
        Set<EntityType<?>> entityTypes = action.getEntityTypes();
        String searchString = action.getSearchString();
        EntitySearcher entitySearcher = entitySearcherFactory.create(entityTypes,
                                                                     searchString,
                                                                     executionContext.getUserId());
        PageRequest pageRequest = action.getPageRequest();
        entitySearcher.setPageRequest(pageRequest);
        entitySearcher.invoke();

        Page<EntitySearchResult> results = entitySearcher.getResults();
        return PerformEntitySearchResult.get(searchString, results);
    }
}

