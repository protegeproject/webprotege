package edu.stanford.bmir.protege.web.client.pagination;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2017
 */
public interface HasPages {

    void setPageNumberChangedHandler(@Nonnull HasPagination.PageNumberChangedHandler pageNumberChangedHandler);

    int getPageNumber();

    void setPageNumber(int pageNumber);

    void setPageCount(int pageCount);

    void setElementCount(long elementCount);
}
