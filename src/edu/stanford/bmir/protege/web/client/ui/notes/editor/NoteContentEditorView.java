package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import edu.stanford.bmir.protege.web.client.ui.library.common.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public interface NoteContentEditorView extends ValueEditor<NoteContent>, HasInitialFocusable {

    void setMode(NoteContentEditorMode mode);
}
