package edu.stanford.bmir.protege.web.shared.notes;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public interface NoteDeletedHandler extends EventHandler {

    void handleNoteDeleted(NoteDeletedEvent event);
}
