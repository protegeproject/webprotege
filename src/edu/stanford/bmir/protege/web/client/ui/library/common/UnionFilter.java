package edu.stanford.bmir.protege.web.client.ui.library.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/01/2012
 */
public class UnionFilter<T> implements Filter<T> {

    private List<Filter<T>> filters = new ArrayList<Filter<T>>();


    public UnionFilter(Filter<T> ... filters) {
        this(Arrays.asList(filters));
    }
    
    public UnionFilter(List<? extends Filter<T>> filters) {
        for(Filter<T> filter : filters) {
            this.filters.add(filter);
        }
    }

    public boolean isIncluded(T object) {
        for(Filter<T> filter : filters) {
            if(filter.isIncluded(object)) {
                return true;
            }
        }
        return false;
    }
}
