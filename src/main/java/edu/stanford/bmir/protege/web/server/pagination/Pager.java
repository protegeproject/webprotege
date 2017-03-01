package edu.stanford.bmir.protege.web.server.pagination;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2013
 * <p>
 *     A utility for creating pages of data.
 * </p>
 */
public class Pager<T> implements Serializable {

    private List<List<T>> partition;

    private int sourceDataSize;

    /**
     * For serialization purposes only
     */
    private Pager() {
    }

    private Pager(List<T> sourceData) {
        this(sourceData, PageRequest.DEFAULT_PAGE_SIZE);
    }

    private Pager(List<T> sourceData, int pageSize) {
        checkArgument(pageSize > 0);
        this.sourceDataSize = sourceData.size();
        this.partition = Lists.partition(sourceData, pageSize);

    }

    public static <T> Pager<T> getPagerForPageSize(List<T> sourceData, int pageSize) {
        return new Pager<T>(sourceData, pageSize);
    }

    /**
     * Gets the page count, which is dependent on page size.
     * @return The page count.  This value is always greater or equal to one.
     */
    public int getPageCount() {
        if(partition.isEmpty()) {
            return 1;
        }
        else {
            return partition.size();
        }
    }

    /**
     * Gets the specified page of data.
     * @param pageNumber The page number.
     * @return The {@link Page} whose page number is the specified value.  Not {@code null}.
     * @throws IndexOutOfBoundsException if {@code pageNumber} is less than 1 or greater than the page count (the
     * value returned by {@link #getPageCount()}).
     */
    public Page<T> getPage(int pageNumber) {
        checkElementIndex(pageNumber - 1, getPageCount());
        if(partition.isEmpty()) {
            return new Page<T>(pageNumber, getPageCount(), Collections.emptyList(), 0);
        }
        else {
            return new Page<T>(pageNumber, getPageCount(), partition.get(pageNumber - 1), sourceDataSize);
        }
    }
}
