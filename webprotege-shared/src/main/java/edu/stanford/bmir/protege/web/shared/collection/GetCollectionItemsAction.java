package edu.stanford.bmir.protege.web.shared.collection;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class GetCollectionItemsAction implements ProjectAction<GetCollectionItemsResult> {

    private ProjectId projectId;

    private CollectionId collectionId;

    private PageRequest pageRequest;

    public GetCollectionItemsAction(ProjectId projectId,
                                    CollectionId collectionId,
                                    PageRequest pageRequest) {
        this.projectId = projectId;
        this.collectionId = collectionId;
        this.pageRequest = pageRequest;
    }

    @GwtSerializationConstructor
    private GetCollectionItemsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public CollectionId getCollectionId() {
        return collectionId;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }
}
