package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static edu.stanford.bmir.protege.web.server.util.ProtegeStreams.entityStream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchActionHandler extends AbstractProjectActionHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HasGetRendering renderer;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public PerformEntitySearchActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull HasGetRendering renderer,
                                            @Nonnull @RootOntology OWLOntology rootOntology,
                                            @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.projectId = projectId;
        this.renderer = renderer;
        this.rootOntology = rootOntology;
        this.tagsManager = tagsManager;
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
        EntitySearcher entitySearcher = EntitySearcher.get(projectId,
                                                           tagsManager, executionContext.getUserId(),
                                                           () -> entityStream(entityTypes,
                                                                              rootOntology,
                                                                              Imports.INCLUDED),
                                                           renderer,
                                                           entityTypes,
                                                           searchString
        );
        PageRequest pageRequest = action.getPageRequest();
        int pageSize = pageRequest.getPageSize();
        entitySearcher.setLimit(pageSize);

        int pageNumber = pageRequest.getPageNumber();
        entitySearcher.setSkip((pageNumber - 1) * pageSize);

        entitySearcher.invoke();

        int totalSearchResults = entitySearcher.getSearchResultsCount();
        List<EntitySearchResult> results = entitySearcher.getResults();
        int pageCount = (totalSearchResults / pageSize) + 1;
        Page<EntitySearchResult> page = new Page<>(pageNumber > pageCount ? 1 : pageNumber,
                                                   pageCount, results, totalSearchResults);
        return new PerformEntitySearchResult(totalSearchResults, page);
    }
}

