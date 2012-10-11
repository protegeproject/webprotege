package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/08/2012
 */
public class CycleInDiscussionThreadException extends RuntimeException {

    private Note note;

    public CycleInDiscussionThreadException(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }

    @Override
    public String getMessage() {
        return "Cycle in discussion thread caused by " + note.toString();
    }
}
