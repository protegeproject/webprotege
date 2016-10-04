package edu.stanford.bmir.protege.web.shared.pagination;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2013
 */
public class PageRequest implements Serializable, IsSerializable {

    public static final int DEFAULT_PAGE_SIZE = 30;

    private static final int FIRST_PAGE = 1;

    public static final int MAX_PAGE_SIZE = Integer.MAX_VALUE;

    private int pageNumber;

    private int pageSize;

    /**
     * For serialization purposes only
     */
    private PageRequest() {
    }

    private PageRequest(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    private PageRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static PageRequest requestFirstPage() {
        return requestPage(FIRST_PAGE);
    }

    public static PageRequest requestSinglePage() {
        return requestPageWithSize(FIRST_PAGE, MAX_PAGE_SIZE);
    }

    public static PageRequest requestPage(int pageNumber) {
        return requestPageWithSize(pageNumber, DEFAULT_PAGE_SIZE);
    }

    public static PageRequest requestPageWithSize(int pageNumber, int pageSize) {
        checkElementIndex(pageNumber - 1, Integer.MAX_VALUE, "pageNumber must be greater than zero");
        checkArgument(pageSize > 0, "pageSize must be greater than zero");
        return new PageRequest(pageNumber, pageSize);
    }



    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int hashCode() {
        return "PageRequest".hashCode() + pageNumber + pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof PageRequest)) {
            return false;
        }
        PageRequest other = (PageRequest) o;
        return this.pageNumber == other.pageNumber && this.pageSize == other.pageSize;
    }
}
