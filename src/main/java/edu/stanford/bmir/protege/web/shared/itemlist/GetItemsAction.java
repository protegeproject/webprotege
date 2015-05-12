package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public abstract class GetItemsAction<T, R extends GetItemsResult<T>> implements Action<R> {

    private List<String> itemNames;

    /**
     * For serialization only
     */
    private GetItemsAction() {
    }

    public GetItemsAction(List<String> itemNames) {
        this.itemNames = new ArrayList<>();
    }

    public List<String> getItemNames() {
        return new ArrayList<>(itemNames);
    }
}
