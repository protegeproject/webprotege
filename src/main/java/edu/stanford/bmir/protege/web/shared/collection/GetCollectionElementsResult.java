package edu.stanford.bmir.protege.web.shared.collection;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class GetCollectionElementsResult implements Result {

    private Page<CollectionElementId> elementIdList;

    private PageRequest pageRequest;

    public GetCollectionElementsResult(Page<CollectionElementId> elementIdList,
                                       PageRequest pageRequest) {
        this.elementIdList = elementIdList;
        this.pageRequest = pageRequest;
    }

    @GwtSerializationConstructor
    private GetCollectionElementsResult() {
    }

    public Page<CollectionElementId> getElementIdPage() {
        return elementIdList;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }
}
