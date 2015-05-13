package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
    protected GetItemsAction() {
    }

    public GetItemsAction(List<String> itemNames) {
        this.itemNames = new ArrayList<>(checkNotNull(itemNames));
    }

    public List<String> getItemNames() {
        return new ArrayList<>(itemNames);
    }
}
