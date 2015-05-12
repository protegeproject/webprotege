package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public abstract class GetItemsResult<T> implements Result {

    private List<T> items;

    /**
     * For serialization only
     */
    private GetItemsResult() {
    }

    public GetItemsResult(List<T> items) {
        this.items = new ArrayList<>(items);
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }
}
