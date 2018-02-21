package edu.stanford.bmir.protege.web.client.pagination;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public interface HasPagination {

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    void setPageNumberChangedHandler(PageNumberChangedHandler handler);

    interface PageNumberChangedHandler {
        void handlePageNumberChanged(int pageNumber);
    }
}
