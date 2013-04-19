package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public interface NoteContentEditorHandler {

    void handleAccept(Optional<NoteContent> noteContent);
}
