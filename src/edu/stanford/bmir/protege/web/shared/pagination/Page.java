package edu.stanford.bmir.protege.web.shared.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class Page<T extends Serializable> implements Serializable, Iterable<T> {

    private int pageNumber;

    private int pageCount;

    private List<T> pageElements;

    /**
     * For serialization purposes only
     */
    private Page() {
    }

    public Page(int pageNumber, int pageCount, List<T> pageElements) {
        checkArgument(pageNumber > 0, "pageNumber must be greater than 0");
        this.pageNumber = pageNumber;
        checkArgument(pageCount > 0, "pageCount must be greater than 0");
        this.pageCount = pageCount;
        checkArgument(!(pageNumber > pageCount));
        this.pageElements = new ArrayList<T>(checkNotNull(pageElements));
    }

    public List<T> getPageElements() {
        return new ArrayList<T>(pageElements);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public Iterator<T> iterator() {
        return getPageElements().iterator();
    }
}
