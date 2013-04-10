package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadResult implements Result {

    private DiscussionThread thread;

    private GetDiscussionThreadResult() {
    }

    public GetDiscussionThreadResult(DiscussionThread thread) {
        this.thread = thread;
    }

    public DiscussionThread getThread() {
        return thread;
    }
}
