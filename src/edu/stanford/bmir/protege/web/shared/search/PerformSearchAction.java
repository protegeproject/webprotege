package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class PerformSearchAction implements HasProjectAction<PerformSearchResult> {

    private ProjectId projectId;

    private Set<SearchType> searchTypes = new HashSet<SearchType>();

    private SearchRequest searchRequest;

    private PerformSearchAction() {
    }


    public PerformSearchAction(ProjectId projectId, Set<SearchType> searchTypes, SearchRequest searchRequest) {
        this.projectId = projectId;
        this.searchTypes = new HashSet<SearchType>(searchTypes);
        this.searchRequest = searchRequest;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public Set<SearchType> getSearchTypes() {
        return new HashSet<SearchType>(searchTypes);
    }

    public SearchRequest getSearchRequest() {
        return searchRequest;
    }
}
