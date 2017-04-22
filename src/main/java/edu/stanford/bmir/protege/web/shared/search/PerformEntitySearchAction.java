package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class PerformEntitySearchAction implements Action<PerformEntitySearchResult>, HasProjectId {

    private ProjectId projectId;

    private String searchString;

    private Set<EntityType<?>> entityTypes;

    private PerformEntitySearchAction() {
    }

    public PerformEntitySearchAction(ProjectId projectId,
                                     String searchString,
                                     Set<EntityType<?>> entityTypes) {
        this.projectId = projectId;
        this.searchString = searchString;
        this.entityTypes = entityTypes;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public Set<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }

    public String getSearchString() {
        return searchString;
    }


}
