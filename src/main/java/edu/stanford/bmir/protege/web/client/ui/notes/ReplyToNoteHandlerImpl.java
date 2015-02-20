package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class ReplyToNoteHandlerImpl implements ReplyToNoteHandler {

    private NoteId noteId;


    public ReplyToNoteHandlerImpl(NoteId noteId) {
        this.noteId = checkNotNull(noteId);
    }

    @Override
    public void handleReplyToNote() {
        NoteEditorDialogController controller = new NoteEditorDialogController(new NoteContentEditorHandler() {
            @Override
            public void handleAccept(Optional<NoteContent> noteContent) {
                if (noteContent.isPresent()) {
                    doReply(noteContent.get());
                }
            }
        });
        controller.setMode(NoteContentEditorMode.REPLY);
        WebProtegeDialog.showDialog(controller);
    }

    private void doReply(NoteContent content) {
        ProjectId projectId = Application.get().getActiveProject().get();
        DispatchServiceManager.get().execute(new AddReplyToNoteAction(projectId, noteId, content), new AbstractDispatchServiceCallback<AddReplyToNoteResult>() {
            @Override
            public void handleSuccess(AddReplyToNoteResult result) {
                GWT.log("Replied o.k. " + result);
            }
        });
    }



}
