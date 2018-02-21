package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public abstract class GetPossibleItemCompletionsResult<T> implements Result {

    private List<T> possibleItemCompletions;

    /**
     * For serialization only
     */
    protected GetPossibleItemCompletionsResult() {
    }

    public GetPossibleItemCompletionsResult(List<T> possibleItemCompletions) {
        this.possibleItemCompletions = new ArrayList<>(checkNotNull(possibleItemCompletions));
    }

    public List<T> getPossibleItemCompletions() {
        return new ArrayList<>(possibleItemCompletions);
    }
}
