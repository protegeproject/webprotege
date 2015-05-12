package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

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
    private GetPossibleItemCompletionsResult() {
    }

    public GetPossibleItemCompletionsResult(List<T> possibleItemCompletions) {
        this.possibleItemCompletions = new ArrayList<>(possibleItemCompletions);
    }

    public List<T> getPossibleItemCompletions() {
        return new ArrayList<>(possibleItemCompletions);
    }
}
