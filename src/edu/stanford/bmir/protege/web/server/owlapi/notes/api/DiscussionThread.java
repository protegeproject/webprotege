package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public class DiscussionThread {

    private final InReplyToId inReplyToId;

    private final List<DiscussionThread> replyThreads = new ArrayList<DiscussionThread>();

    public DiscussionThread(InReplyToId inReplyToId, List<DiscussionThread> replyThreads) {
        this.inReplyToId = inReplyToId;
        this.replyThreads.addAll(replyThreads);
    }

    public InReplyToId getReplyableId() {
        return inReplyToId;
    }

    public List<DiscussionThread> getReplies() {
        return new ArrayList<DiscussionThread>(replyThreads);
    }

    public boolean isEmpty() {
        return replyThreads.isEmpty();
    }


    @Override
    public int hashCode() {
        return DiscussionThread.class.getSimpleName().hashCode() + inReplyToId.hashCode() + replyThreads.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DiscussionThread)) {
            return false;
        }
        DiscussionThread other = (DiscussionThread) obj;
        return this.inReplyToId.equals(other.inReplyToId) && this.replyThreads.equals(other.replyThreads);
    }
}
