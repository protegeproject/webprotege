package edu.stanford.bmir.protege.web.shared.itemlist;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public abstract class GetPossibleItemCompletionsAction<T> implements Action<GetPossibleItemCompletionsResult<T>> {

    private String completionText;

    private GetPossibleItemCompletionsAction() {
    }

    public GetPossibleItemCompletionsAction(String completionText) {
        this.completionText = completionText;
    }

    public String getCompletionText() {
        return completionText;
    }
}
