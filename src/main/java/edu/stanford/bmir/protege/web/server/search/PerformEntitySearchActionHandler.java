package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.util.ProtegeStreams;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.semanticweb.owlapi.model.EntityType;
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
public class PerformEntitySearchActionHandler extends AbstractHasProjectActionHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    @Inject
    public PerformEntitySearchActionHandler(@Nonnull ProjectManager projectManager,
                                            AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<PerformEntitySearchAction> getActionClass() {
        return PerformEntitySearchAction.class;
    }

    @Override
    protected PerformEntitySearchResult execute(PerformEntitySearchAction action,
                                                Project project,
                                                ExecutionContext executionContext) {
        Set<EntityType<?>> entityTypes = action.getEntityTypes();
        String searchString = action.getSearchString();
        EntitySearcher entitySearcher = EntitySearcher.get(project.getProjectId(),
                                                           executionContext.getUserId(),
                                                           () -> entityStream(entityTypes,
                                                                              project.getRootOntology(),
                                                                              Imports.INCLUDED),
                                                           project.getRenderingManager(),
                                                           entityTypes,
                                                           searchString);
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

