package edu.stanford.bmir.protege.web.client.pagination;

import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public interface HasPagination {

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    default int getPageSize() {
        return FormPageRequest.DEFAULT_PAGE_SIZE;
    }

    void setPageNumberChangedHandler(PageNumberChangedHandler handler);

    interface PageNumberChangedHandler {
        void handlePageNumberChanged(int pageNumber);
    }
}
