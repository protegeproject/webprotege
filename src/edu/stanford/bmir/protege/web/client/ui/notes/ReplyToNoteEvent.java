package edu.stanford.bmir.protege.web.client.ui.notes;

import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class ReplyToNoteEvent {

    private NoteId noteId;

    private DiscussionThread context;

    public ReplyToNoteEvent(NoteId noteId, DiscussionThread context) {
        this.noteId = noteId;
        this.context = context;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public DiscussionThread getContext() {
        return context;
    }
}
