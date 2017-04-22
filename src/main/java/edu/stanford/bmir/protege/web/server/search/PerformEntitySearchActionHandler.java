package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import org.apache.commons.lang.StringUtils;
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
        EntitySearcher entitySearcher = new EntitySearcher(project.getRootOntology(),
                                                           project.getRenderingManager(),
                                                           entityTypes,
                                                           searchString).invoke();
        int totalSearchResults = entitySearcher.getTotalSearchResultsCount();
        List<EntitySearchResult> results = entitySearcher.getResults();
        return new PerformEntitySearchResult(totalSearchResults, results);
    }
}

