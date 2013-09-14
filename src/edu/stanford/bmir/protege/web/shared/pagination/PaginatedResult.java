package edu.stanford.bmir.protege.web.shared.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class PaginatedResult<T> implements Serializable {

    private int totalLength;

    private Range range;

    private List<T> elements;

    /**
     * For serialization purposes only.
     */
    private PaginatedResult() {
    }

    public PaginatedResult(int totalLength, int start, List<T> elements) {
        this.totalLength = totalLength;
        this.range = new Range(start, elements.size());
        this.elements = checkNotNull(elements);
    }

    public int getTotalLength() {
        return totalLength;
    }

    public Range getRange() {
        return range;
    }

    public List<T> getElements() {
        return new ArrayList<T>(elements);
    }
}
