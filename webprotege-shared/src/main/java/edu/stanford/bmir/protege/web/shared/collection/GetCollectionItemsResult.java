package edu.stanford.bmir.protege.web.shared.collection;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Aug 2017
 */
public class GetCollectionItemsResult implements Result {

    private Page<CollectionItem> elementIdList;

    private PageRequest pageRequest;

    public GetCollectionItemsResult(Page<CollectionItem> elementIdList,
                                    PageRequest pageRequest) {
        this.elementIdList = elementIdList;
        this.pageRequest = pageRequest;
    }

    @GwtSerializationConstructor
    private GetCollectionItemsResult() {
    }

    public Page<CollectionItem> getElementIdPage() {
        return elementIdList;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }
}
