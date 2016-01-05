package edu.stanford.bmir.protege.web.client.ui.notes;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ReplyToNoteHandler {

    void handleReplyToNote(NoteId noteId);
}
