package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public interface NotePostedHandler extends EventHandler {

    void handleNotePosted(NotePostedEvent event);
}
