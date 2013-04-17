package edu.stanford.bmir.protege.web.shared.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public interface NoteStatusChangedHandler {

    void handleNoteStatusChanged(NoteStatusChangedEvent event);
}
