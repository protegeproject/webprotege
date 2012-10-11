package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/08/2012
 */
public abstract class ReplyToChange extends NoteChange {

    private InReplyToId inReplyToId;

    protected ReplyToChange(NoteId noteId, InReplyToId inReplyToId) {
        super(noteId);
        this.inReplyToId = inReplyToId;
    }
}
