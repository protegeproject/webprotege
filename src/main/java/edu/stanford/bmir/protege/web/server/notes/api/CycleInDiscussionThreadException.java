package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public class CycleInDiscussionThreadException extends NoteChangeException {

    public CycleInDiscussionThreadException(Throwable cause, Note note) {
        super(cause, note);
    }

    public CycleInDiscussionThreadException(Note note) {
        super(note);
    }
}
