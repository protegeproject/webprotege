package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteContentEditorDialog extends WebProtegeDialog<Optional<NoteContent>> {

    public NoteContentEditorDialog(NoteContentEditorHandler handler) {
        super(new NoteEditorDialogController(handler));
    }

    public void setMode(NoteContentEditorMode mode) {
        NoteEditorDialogController controller = (NoteEditorDialogController) getController();
        controller.setMode(mode);
        controller.setTitle(mode.getModeTitle());
    }


    public static void showDialog(NoteContentEditorMode mode, final NoteContentEditorHandler handler) {
        NoteContentEditorDialog dlg = new NoteContentEditorDialog(handler);
        dlg.setMode(mode);
    }
}
