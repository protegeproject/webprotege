package edu.stanford.bmir.protege.web.client.ui.notes;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public interface DeleteNoteHandler {

    void handleDeleteNote(OWLEntity targetEntity, NoteId noteId);
}
