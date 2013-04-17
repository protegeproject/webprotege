package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteContentEditorDialog extends WebProtegeDialog<Optional<NoteContent>> {

    public NoteContentEditorDialog() {
        super(new NoteEditorDialogController());
    }

    public void setMode(NoteContentEditorMode mode) {
        NoteEditorDialogController controller = (NoteEditorDialogController) getController();
        controller.setMode(mode);
        controller.setTitle(mode.getModeTitle());
    }
}
